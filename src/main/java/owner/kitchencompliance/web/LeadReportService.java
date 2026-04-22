package owner.kitchencompliance.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
import owner.kitchencompliance.model.AdminRecentLead;
import owner.kitchencompliance.model.LeadDashboardSnapshot;

@Service
public class LeadReportService {

    private static final int EXPECTED_COLUMNS = 18;
    private static final int RECENT_LEAD_LIMIT = 12;
    private static final int BREAKDOWN_LIMIT = 8;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss xxx");
    private static final String HEADER =
            "captured_at,lead_type,visitor_id,city,state,page_family,issue_type,authority_id,source_path,verdict_state,provider_intent,contact_name,business_name,email,phone,coverage_note,notes,routing_consent\n";

    private final LeadCaptureProperties properties;

    public LeadReportService(LeadCaptureProperties properties) {
        this.properties = properties;
    }

    public LeadDashboardSnapshot readDashboard() {
        Path logFile = properties.logFilePath();
        if (Files.notExists(logFile)) {
            return emptyDashboard(logFile);
        }
        List<LeadEvent> leads = readLeads(logFile);
        List<LeadEvent> activeLeads = leads.stream()
                .filter(lead -> lead.leadType().equals("operator_request"))
                .toList();
        if (activeLeads.isEmpty()) {
            return emptyDashboard(logFile);
        }

        List<LeadEvent> sortedLeads = activeLeads.stream()
                .sorted(Comparator.comparing(LeadEvent::capturedAt).reversed())
                .toList();

        long operatorRequests = activeLeads.stream().filter(lead -> lead.leadType().equals("operator_request")).count();
        long consentedLeads = activeLeads.stream().filter(LeadEvent::routingConsent).count();

        return new LeadDashboardSnapshot(
                logFile.toAbsolutePath().normalize().toString(),
                "To keep leads through redeploys, point APP_LEAD_LOG_DIR at a mounted persistent directory on the host.",
                true,
                activeLeads.size(),
                operatorRequests,
                consentedLeads,
                TIMESTAMP_FORMAT.format(sortedLeads.getFirst().capturedAt()),
                breakdown(activeLeads, lead -> cityLabel(lead.city(), lead.state())),
                breakdown(activeLeads, this::leadTypeLabel),
                breakdown(activeLeads, lead -> titleCase(lead.providerIntent().replace('_', ' '))),
                sortedLeads.stream()
                        .limit(RECENT_LEAD_LIMIT)
                        .map(this::toRecentLead)
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
            throw new IllegalStateException("Failed to read lead export.", ex);
        }
    }

    public String exportSummaryCsv() {
        List<LeadEvent> leads = readLeads(properties.logFilePath());
        StringBuilder builder = new StringBuilder("city,state,lead_type,page_family,provider_intent,routing_consent,count\n");
        leads.stream()
                .collect(Collectors.groupingBy(
                        lead -> new SummaryKey(
                                lead.city(),
                                lead.state(),
                                lead.leadType(),
                                lead.pageFamily(),
                                lead.providerIntent(),
                                lead.routingConsent()
                        ),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<SummaryKey, Long>comparingByValue().reversed()
                        .thenComparing(entry -> entry.getKey().city())
                        .thenComparing(entry -> entry.getKey().leadType()))
                .forEach(entry -> builder.append(csv(entry.getKey().city())).append(',')
                        .append(csv(entry.getKey().state())).append(',')
                        .append(csv(entry.getKey().leadType())).append(',')
                        .append(csv(entry.getKey().pageFamily())).append(',')
                        .append(csv(entry.getKey().providerIntent())).append(',')
                        .append(entry.getKey().routingConsent()).append(',')
                        .append(entry.getValue())
                        .append('\n'));
        return builder.toString();
    }

    private LeadDashboardSnapshot emptyDashboard(Path logFile) {
        return new LeadDashboardSnapshot(
                logFile.toAbsolutePath().normalize().toString(),
                "To keep leads through redeploys, point APP_LEAD_LOG_DIR at a mounted persistent directory on the host.",
                false,
                0,
                0,
                0,
                "No leads recorded yet",
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );
    }

    private List<LeadEvent> readLeads(Path logFile) {
        if (Files.notExists(logFile)) {
            return List.of();
        }
        try {
            List<LeadEvent> leads = new ArrayList<>();
            for (String line : Files.readAllLines(logFile, StandardCharsets.UTF_8)) {
                if (line.isBlank() || line.startsWith("captured_at,")) {
                    continue;
                }
                List<String> columns = parseCsvLine(line);
                if (columns.size() != EXPECTED_COLUMNS) {
                    continue;
                }
                try {
                    leads.add(new LeadEvent(
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
                            columns.get(15),
                            columns.get(16),
                            Boolean.parseBoolean(columns.get(17))
                    ));
                } catch (DateTimeParseException ignored) {
                    // Skip malformed rows so admin still loads.
                }
            }
            return List.copyOf(leads);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read lead log.", ex);
        }
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

    private List<AdminBreakdownRow> breakdown(List<LeadEvent> leads, Function<LeadEvent, String> labelExtractor) {
        Map<String, Long> counts = leads.stream()
                .collect(Collectors.groupingBy(labelExtractor, LinkedHashMap::new, Collectors.counting()));

        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry::getKey))
                .limit(BREAKDOWN_LIMIT)
                .map(entry -> new AdminBreakdownRow(entry.getKey(), entry.getValue()))
                .toList();
    }

    private AdminRecentLead toRecentLead(LeadEvent lead) {
        return new AdminRecentLead(
                TIMESTAMP_FORMAT.format(lead.capturedAt()),
                leadTypeLabel(lead),
                cityLabel(lead.city(), lead.state()),
                titleCase(lead.providerIntent().replace('_', ' ')),
                lead.contactName() + " | " + lead.businessName() + " | " + lead.email(),
                lead.routingConsent() ? "Consented" : "No consent",
                lead.sourcePath()
        );
    }

    private String leadTypeLabel(LeadEvent lead) {
        return switch (lead.leadType()) {
            case "operator_request" -> "Operator request";
            default -> lead.leadType();
        };
    }

    private String cityLabel(String city, String state) {
        if (city == null || city.isBlank() || state == null || state.isBlank()) {
            return "Unknown market";
        }
        return titleCase(city) + ", " + state.toUpperCase();
    }

    private String titleCase(String value) {
        String[] parts = value.trim().split("\\s+");
        return java.util.Arrays.stream(parts)
                .filter(part -> !part.isBlank())
                .map(part -> Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    private record LeadEvent(
            OffsetDateTime capturedAt,
            String leadType,
            String visitorId,
            String city,
            String state,
            String pageFamily,
            String issueType,
            String authorityId,
            String sourcePath,
            String verdictState,
            String providerIntent,
            String contactName,
            String businessName,
            String email,
            String phone,
            String coverageNote,
            String notes,
            boolean routingConsent
    ) {
    }

    private record SummaryKey(
            String city,
            String state,
            String leadType,
            String pageFamily,
            String providerIntent,
            boolean routingConsent
    ) {
    }

    private String csv(String value) {
        String safe = value == null ? "" : value;
        String escaped = safe.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
