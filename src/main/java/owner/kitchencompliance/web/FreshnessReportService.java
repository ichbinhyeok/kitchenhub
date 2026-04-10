package owner.kitchencompliance.web;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.data.SourceRecord;
import owner.kitchencompliance.model.FreshnessDashboardSnapshot;
import owner.kitchencompliance.model.FreshnessWatchRow;
import owner.kitchencompliance.ops.SourceFreshnessAssessment;
import owner.kitchencompliance.ops.SourceFreshnessService;

@Service
public class FreshnessReportService {

    private static final int WATCH_LIMIT = 12;
    private static final int DUE_SOON_DAYS = 30;

    private final SeedRegistry seedRegistry;
    private final SourceFreshnessService sourceFreshnessService;
    private final Clock clock;

    public FreshnessReportService(
            SeedRegistry seedRegistry,
            SourceFreshnessService sourceFreshnessService,
            Clock clock
    ) {
        this.seedRegistry = seedRegistry;
        this.sourceFreshnessService = sourceFreshnessService;
        this.clock = clock;
    }

    public FreshnessDashboardSnapshot readDashboard() {
        List<RouteFreshnessSnapshot> routeSnapshots = routeSnapshots();

        long staleRoutes = routeSnapshots.stream().filter(RouteFreshnessSnapshot::stale).count();
        long dueSoonRoutes = routeSnapshots.stream().filter(RouteFreshnessSnapshot::dueSoon).count();
        long freshRoutes = routeSnapshots.size() - staleRoutes - dueSoonRoutes;
        String nextReviewDue = routeSnapshots.stream()
                .map(RouteFreshnessSnapshot::nextReviewDate)
                .min(LocalDate::compareTo)
                .map(LocalDate::toString)
                .orElse("No indexed routes");

        return new FreshnessDashboardSnapshot(
                routeSnapshots.size(),
                freshRoutes,
                dueSoonRoutes,
                staleRoutes,
                nextReviewDue,
                routeSnapshots.stream()
                        .limit(WATCH_LIMIT)
                        .map(RouteFreshnessSnapshot::toWatchRow)
                        .toList()
        );
    }

    public String exportWatchCsv() {
        List<RouteFreshnessSnapshot> routeSnapshots = routeSnapshots();
        StringBuilder builder = new StringBuilder("city,page,path,status,next_review_on,note\n");
        routeSnapshots.forEach(row -> builder.append(csv(row.cityLabel())).append(',')
                .append(csv(row.pageLabel())).append(',')
                .append(csv(row.path())).append(',')
                .append(csv(row.statusLabel())).append(',')
                .append(csv(row.nextReviewDate().toString())).append(',')
                .append(csv(row.note())).append('\n'));
        return builder.toString();
    }

    private List<RouteFreshnessSnapshot> routeSnapshots() {
        LocalDate today = LocalDate.now(clock);
        LocalDate dueSoonCutoff = today.plusDays(DUE_SOON_DAYS);
        return seedRegistry.routes().stream()
                .filter(RouteRecord::indexable)
                .map(route -> snapshot(route, today, dueSoonCutoff))
                .sorted(Comparator
                        .comparingInt((RouteFreshnessSnapshot row) -> row.priority())
                        .thenComparing(RouteFreshnessSnapshot::nextReviewDate)
                        .thenComparing(RouteFreshnessSnapshot::path))
                .toList();
    }

    private RouteFreshnessSnapshot snapshot(RouteRecord route, LocalDate today, LocalDate dueSoonCutoff) {
        List<SourceRecord> sources = seedRegistry.sourcesFor(route);
        List<SourceFreshnessAssessment> assessments = sourceFreshnessService.assess(sources);
        LocalDate nextReviewDate = sources.stream()
                .map(SourceRecord::nextReviewOn)
                .min(LocalDate::compareTo)
                .orElse(today);

        List<SourceFreshnessAssessment> staleAssessments = assessments.stream()
                .filter(assessment -> !assessment.fresh())
                .toList();
        boolean stale = !staleAssessments.isEmpty();
        boolean dueSoon = !stale && !nextReviewDate.isAfter(dueSoonCutoff);

        String statusLabel;
        int priority;
        String note;
        if (stale) {
            SourceFreshnessAssessment firstStale = staleAssessments.getFirst();
            statusLabel = "Stale";
            priority = 0;
            note = firstStale.sourceTitle() + " is past review.";
        } else if (dueSoon) {
            SourceRecord nextSource = sources.stream()
                    .min(Comparator.comparing(SourceRecord::nextReviewOn).thenComparing(SourceRecord::title))
                    .orElseThrow();
            statusLabel = "Review soon";
            priority = 1;
            note = nextSource.title() + " is due by " + nextSource.nextReviewOn() + ".";
        } else {
            SourceRecord nextSource = sources.stream()
                    .min(Comparator.comparing(SourceRecord::nextReviewOn).thenComparing(SourceRecord::title))
                    .orElseThrow();
            statusLabel = "Fresh";
            priority = 2;
            note = nextSource.title() + " is the next source due.";
        }

        return new RouteFreshnessSnapshot(
                cityLabel(route.profileId()),
                pageLabel(route.template()),
                route.path(),
                nextReviewDate,
                statusLabel,
                note,
                stale,
                dueSoon,
                priority
        );
    }

    private String cityLabel(String profileId) {
        var profile = seedRegistry.profile(profileId);
        return titleCase(profile.city()) + ", " + profile.state().toUpperCase();
    }

    private String pageLabel(RouteTemplate template) {
        return switch (template) {
            case FOG_RULES -> "FOG rules";
            case APPROVED_HAULERS -> "Approved haulers";
            case HOOD_REQUIREMENTS -> "Hood requirements";
            case INSPECTION_CHECKLIST -> "Inspection checklist";
            case FIND_GREASE_SERVICE -> "Grease service finder";
            case FIND_HOOD_CLEANER -> "Hood cleaner finder";
        };
    }

    private String titleCase(String value) {
        String[] parts = value.trim().split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1).toLowerCase());
            }
        }
        return builder.toString();
    }

    private record RouteFreshnessSnapshot(
            String cityLabel,
            String pageLabel,
            String path,
            LocalDate nextReviewDate,
            String statusLabel,
            String note,
            boolean stale,
            boolean dueSoon,
            int priority
    ) {
        private FreshnessWatchRow toWatchRow() {
            return new FreshnessWatchRow(
                    cityLabel,
                    pageLabel,
                    path,
                    statusLabel,
                    nextReviewDate.toString(),
                    note
            );
        }
    }

    private String csv(String value) {
        String safe = value == null ? "" : value;
        String escaped = safe.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
