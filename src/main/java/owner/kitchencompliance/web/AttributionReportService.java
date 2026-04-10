package owner.kitchencompliance.web;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.model.AdminBreakdownRow;
import owner.kitchencompliance.model.AdminRecentEvent;
import owner.kitchencompliance.model.AttributionDashboardSnapshot;

@Service
public class AttributionReportService {

    private static final int EXPECTED_COLUMNS_V1 = 14;
    private static final int EXPECTED_COLUMNS_V2 = 17;
    private static final int RECENT_EVENT_LIMIT = 12;
    private static final int BREAKDOWN_LIMIT = 8;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss xxx");
    private static final String HEADER =
            "captured_at,event_type,visitor_id,verdict_state,city,state,page_family,issue_type,authority_id,source_path,target_path,target_url,provider_id,provider_type,cta_type,sponsored,tool_slug\n";

    private final AttributionProperties properties;

    public AttributionReportService(AttributionProperties properties) {
        this.properties = properties;
    }

    public AttributionDashboardSnapshot readDashboard() {
        Path logFile = properties.logFilePath();
        if (Files.notExists(logFile)) {
            return emptyDashboard(logFile);
        }

        List<AttributionEvent> events = readEvents(logFile);
        if (events.isEmpty()) {
            return emptyDashboard(logFile);
        }

        List<AttributionEvent> sortedEvents = events.stream()
                .sorted(Comparator.comparing(AttributionEvent::capturedAt).reversed())
                .toList();

        long pageViewEvents = events.stream().filter(event -> event.eventType().equals("page_view")).count();
        long utilityViewEvents = events.stream()
                .filter(event -> event.eventType().equals("page_view"))
                .filter(event -> event.pageFamily().equals("operator_tool"))
                .count();
        long providerClicks = events.stream().filter(event -> event.eventType().equals("provider_click")).count();
        long ctaClicks = events.stream().filter(event -> event.eventType().equals("cta_click")).count();
        long sponsoredClicks = events.stream().filter(AttributionEvent::sponsored).count();

        UtilityRevisitSummary utilityRevisitSummary = utilityRevisitSummary(events);

        return new AttributionDashboardSnapshot(
                logFile.toAbsolutePath().normalize().toString(),
                "To keep attribution through redeploys, point APP_ATTRIBUTION_LOG_DIR at a mounted persistent directory on the host.",
                true,
                events.size(),
                pageViewEvents,
                utilityViewEvents,
                providerClicks,
                ctaClicks,
                sponsoredClicks,
                utilityRevisitSummary.returningVisitors(),
                utilityRevisitSummary.rateLabel(),
                formatTimestamp(sortedEvents.getFirst().capturedAt()),
                breakdown(events, event -> cityLabel(event.city(), event.state())),
                breakdown(events, event -> pageFamilyLabel(event.pageFamily())),
                breakdown(events, event -> verdictStateLabel(event.verdictState())),
                breakdown(events, this::destinationLabel),
                sortedEvents.stream()
                        .limit(RECENT_EVENT_LIMIT)
                        .map(this::toRecentEvent)
                        .toList()
        );
    }

    public String exportEventsCsv() {
        Path logFile = properties.logFilePath();
        if (Files.notExists(logFile)) {
            return HEADER;
        }
        try {
            return Files.readString(logFile, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read attribution export.", ex);
        }
    }

    public String exportSummaryCsv() {
        List<AttributionEvent> events = readEvents(properties.logFilePath());
        StringBuilder builder = new StringBuilder("city,state,page_family,event_type,verdict_state,destination,sponsored,count\n");
        events.stream()
                .collect(Collectors.groupingBy(
                        event -> new SummaryKey(
                                event.city(),
                                event.state(),
                                event.pageFamily(),
                                event.eventType(),
                                event.verdictState(),
                                destinationLabel(event),
                                event.sponsored()
                        ),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<SummaryKey, Long>comparingByValue().reversed()
                        .thenComparing(entry -> entry.getKey().city())
                        .thenComparing(entry -> entry.getKey().pageFamily()))
                .forEach(entry -> builder.append(csv(entry.getKey().city())).append(',')
                        .append(csv(entry.getKey().state())).append(',')
                        .append(csv(entry.getKey().pageFamily())).append(',')
                        .append(csv(entry.getKey().eventType())).append(',')
                        .append(csv(entry.getKey().verdictState())).append(',')
                        .append(csv(entry.getKey().destination())).append(',')
                        .append(entry.getKey().sponsored()).append(',')
                        .append(entry.getValue())
                        .append('\n'));
        return builder.toString();
    }

    public String exportOperatorUtilitySummaryCsv() {
        List<AttributionEvent> utilityEvents = readEvents(properties.logFilePath()).stream()
                .filter(event -> event.eventType().equals("page_view"))
                .filter(event -> event.pageFamily().equals("operator_tool"))
                .toList();
        StringBuilder builder = new StringBuilder("tool_slug,page_views,unique_visitors,returning_visitors,revisit_rate\n");
        utilityEvents.stream()
                .collect(Collectors.groupingBy(AttributionEvent::toolSlug, LinkedHashMap::new, Collectors.toList()))
                .forEach((toolSlug, toolRows) -> {
                    long uniqueVisitors = toolRows.stream()
                            .map(AttributionEvent::visitorId)
                            .filter(visitorId -> !visitorId.isBlank())
                            .distinct()
                            .count();
                    long returningVisitors = toolRows.stream()
                            .filter(event -> !event.visitorId().isBlank())
                            .collect(Collectors.groupingBy(AttributionEvent::visitorId,
                                    Collectors.mapping(event -> event.capturedAt().toLocalDate(), Collectors.toSet())))
                            .values().stream()
                            .filter(days -> days.size() > 1)
                            .count();
                    builder.append(csv(toolSlug)).append(',')
                            .append(toolRows.size()).append(',')
                            .append(uniqueVisitors).append(',')
                            .append(returningVisitors).append(',')
                            .append(csv(revisitRateLabel(returningVisitors, uniqueVisitors)))
                            .append('\n');
                });
        return builder.toString();
    }

    private AttributionDashboardSnapshot emptyDashboard(Path logFile) {
        return new AttributionDashboardSnapshot(
                logFile.toAbsolutePath().normalize().toString(),
                "To keep attribution through redeploys, point APP_ATTRIBUTION_LOG_DIR at a mounted persistent directory on the host.",
                false,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                "0 of 0 visitors",
                "No clicks recorded yet",
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );
    }

    private List<AttributionEvent> readEvents(Path logFile) {
        if (Files.notExists(logFile)) {
            return List.of();
        }
        try {
            List<AttributionEvent> events = new ArrayList<>();
            for (String line : Files.readAllLines(logFile, StandardCharsets.UTF_8)) {
                if (line.isBlank() || line.startsWith("captured_at,")) {
                    continue;
                }
                List<String> columns = parseCsvLine(line);
                try {
                    events.add(parseEvent(columns));
                } catch (DateTimeParseException ignored) {
                    // Skip malformed rows so the admin page still loads.
                }
            }
            return List.copyOf(events);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read attribution log.", ex);
        }
    }

    private AttributionEvent parseEvent(List<String> columns) {
        if (columns.size() == EXPECTED_COLUMNS_V2) {
            return new AttributionEvent(
                    OffsetDateTime.parse(columns.get(0)),
                    columns.get(1),
                    columns.get(2),
                    columns.get(3),
                    columns.get(4),
                    columns.get(5),
                    columns.get(6),
                    columns.get(7),
                    columns.get(8),
                    columns.get(9),
                    columns.get(10),
                    columns.get(11),
                    columns.get(12),
                    columns.get(13),
                    columns.get(14),
                    Boolean.parseBoolean(columns.get(15)),
                    columns.get(16)
            );
        }
        if (columns.size() == EXPECTED_COLUMNS_V1) {
            return new AttributionEvent(
                    OffsetDateTime.parse(columns.get(0)),
                    columns.get(1),
                    "",
                    "",
                    columns.get(2),
                    columns.get(3),
                    columns.get(4),
                    columns.get(5),
                    columns.get(6),
                    columns.get(7),
                    columns.get(8),
                    columns.get(9),
                    columns.get(10),
                    columns.get(11),
                    columns.get(12),
                    Boolean.parseBoolean(columns.get(13)),
                    ""
            );
        }
        throw new DateTimeParseException("Unexpected attribution row width", "", 0);
    }

    private List<String> parseCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int index = 0; index < line.length(); index++) {
            char currentChar = line.charAt(index);
            if (inQuotes) {
                if (currentChar == '"') {
                    if (index + 1 < line.length() && line.charAt(index + 1) == '"') {
                        current.append('"');
                        index++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(currentChar);
                }
            } else if (currentChar == '"') {
                inQuotes = true;
            } else if (currentChar == ',') {
                columns.add(current.toString());
                current.setLength(0);
            } else {
                current.append(currentChar);
            }
        }

        columns.add(current.toString());
        return columns;
    }

    private List<AdminBreakdownRow> breakdown(List<AttributionEvent> events, Function<AttributionEvent, String> labelExtractor) {
        Map<String, Long> counts = events.stream()
                .collect(Collectors.groupingBy(labelExtractor, LinkedHashMap::new, Collectors.counting()));

        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry::getKey))
                .limit(BREAKDOWN_LIMIT)
                .map(entry -> new AdminBreakdownRow(entry.getKey(), entry.getValue()))
                .toList();
    }

    private AdminRecentEvent toRecentEvent(AttributionEvent event) {
        return new AdminRecentEvent(
                formatTimestamp(event.capturedAt()),
                eventTypeLabel(event),
                cityLabel(event.city(), event.state()),
                event.sourcePath(),
                destinationLabel(event),
                pageFamilyLabel(event.pageFamily()),
                verdictStateLabel(event.verdictState()),
                event.sponsored() ? "Sponsored" : "Organic"
        );
    }

    private UtilityRevisitSummary utilityRevisitSummary(List<AttributionEvent> events) {
        Map<String, java.util.Set<LocalDate>> visitsByVisitor = events.stream()
                .filter(event -> event.eventType().equals("page_view"))
                .filter(event -> event.pageFamily().equals("operator_tool"))
                .filter(event -> !event.visitorId().isBlank())
                .collect(Collectors.groupingBy(
                        AttributionEvent::visitorId,
                        Collectors.mapping(event -> event.capturedAt().toLocalDate(), Collectors.toSet())
                ));

        long uniqueVisitors = visitsByVisitor.size();
        long returningVisitors = visitsByVisitor.values().stream()
                .filter(days -> days.size() > 1)
                .count();
        return new UtilityRevisitSummary(returningVisitors, revisitRateLabel(returningVisitors, uniqueVisitors));
    }

    private String revisitRateLabel(long returningVisitors, long uniqueVisitors) {
        if (uniqueVisitors == 0) {
            return "0 of 0 visitors";
        }
        long percent = Math.round((returningVisitors * 100.0) / uniqueVisitors);
        return returningVisitors + " of " + uniqueVisitors + " visitors (" + percent + "%)";
    }

    private String formatTimestamp(OffsetDateTime timestamp) {
        return TIMESTAMP_FORMAT.format(timestamp);
    }

    private String cityLabel(String city, String state) {
        if (city == null || city.isBlank() || state == null || state.isBlank()) {
            return "Cross-city utility";
        }
        return titleCase(city) + ", " + state.toUpperCase();
    }

    private String pageFamilyLabel(String pageFamily) {
        return switch (pageFamily) {
            case "fog_rules" -> "FOG rules";
            case "approved_haulers" -> "Approved haulers";
            case "hood_requirements" -> "Hood requirements";
            case "inspection_checklist" -> "Inspection checklist";
            case "provider_finder" -> "Provider finder";
            case "operator_tool" -> "Operator tool";
            default -> pageFamily;
        };
    }

    private String verdictStateLabel(String verdictState) {
        if (verdictState == null || verdictState.isBlank()) {
            return "Unknown";
        }
        return java.util.Arrays.stream(verdictState.split("_"))
                .filter(part -> !part.isBlank())
                .map(part -> Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    private String eventTypeLabel(AttributionEvent event) {
        return switch (event.eventType()) {
            case "provider_click" -> "Provider outbound";
            case "cta_click" -> event.sponsored() ? "Sponsored CTA" : "Next-action CTA";
            case "page_view" -> event.pageFamily().equals("operator_tool") ? "Operator tool view" : "Local page view";
            default -> event.eventType();
        };
    }

    private String destinationLabel(AttributionEvent event) {
        if (!event.providerId().isBlank()) {
            String host = hostLabel(event.targetUrl());
            if (!host.isBlank()) {
                return event.providerId() + " -> " + host;
            }
            return event.providerId();
        }
        if (!event.targetPath().isBlank()) {
            return event.targetPath();
        }
        if (!event.targetUrl().isBlank()) {
            return hostLabel(event.targetUrl());
        }
        if (!event.toolSlug().isBlank()) {
            return event.toolSlug();
        }
        if (!event.sourcePath().isBlank()) {
            return event.sourcePath();
        }
        return "Unknown destination";
    }

    private String hostLabel(String targetUrl) {
        if (targetUrl == null || targetUrl.isBlank()) {
            return "";
        }
        try {
            URI uri = new URI(targetUrl);
            return uri.getHost() == null ? targetUrl : uri.getHost();
        } catch (URISyntaxException ex) {
            return targetUrl;
        }
    }

    private String titleCase(String value) {
        String[] parts = value.trim().split("\\s+");
        return java.util.Arrays.stream(parts)
                .filter(part -> !part.isBlank())
                .map(part -> Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    private record AttributionEvent(
            OffsetDateTime capturedAt,
            String eventType,
            String visitorId,
            String verdictState,
            String city,
            String state,
            String pageFamily,
            String issueType,
            String authorityId,
            String sourcePath,
            String targetPath,
            String targetUrl,
            String providerId,
            String providerType,
            String ctaType,
            boolean sponsored,
            String toolSlug
    ) {
    }

    private record SummaryKey(
            String city,
            String state,
            String pageFamily,
            String eventType,
            String verdictState,
            String destination,
            boolean sponsored
    ) {
    }

    private record UtilityRevisitSummary(
            long returningVisitors,
            String rateLabel
    ) {
    }

    private String csv(String value) {
        String safe = value == null ? "" : value;
        String escaped = safe.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
