package owner.kitchencompliance.ops;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SearchDemandSnapshotRecord;
import owner.kitchencompliance.data.SeedRegistry;

@Service
public class SearchDemandAssessmentService {

    private static final int PROMOTION_DEMAND_IMPRESSIONS = 50;
    private static final int OPTIMIZATION_IMPRESSIONS = 100;
    private static final double CTR_WORK_THRESHOLD = 0.08;
    private static final double CTR_WORK_MAX_POSITION = 12.0;
    private static final double DISCOVERABILITY_MIN_POSITION = 12.0;

    private final SeedRegistry seedRegistry;

    public SearchDemandAssessmentService(SeedRegistry seedRegistry) {
        this.seedRegistry = seedRegistry;
    }

    public List<RouteSearchDemandAssessment> assessTrackedRoutes() {
        return seedRegistry.searchDemandSnapshots().stream()
                .map(snapshot -> assess(seedRegistry.route(snapshot.routePath()), snapshot))
                .sorted(Comparator
                        .comparingInt((RouteSearchDemandAssessment assessment) -> assessment.status().priority())
                        .thenComparing(RouteSearchDemandAssessment::impressions28d, Comparator.reverseOrder())
                        .thenComparing(RouteSearchDemandAssessment::path))
                .toList();
    }

    public Optional<RouteSearchDemandAssessment> assessmentFor(RouteRecord route) {
        return seedRegistry.searchDemandSnapshot(route)
                .map(snapshot -> assess(route, snapshot));
    }

    private RouteSearchDemandAssessment assess(RouteRecord route, SearchDemandSnapshotRecord snapshot) {
        double ctr = snapshot.impressions28d() == 0
                ? 0.0
                : (double) snapshot.clicks28d() / snapshot.impressions28d();

        SearchDemandStatus status;
        String note;
        if (!route.indexable() && snapshot.impressions28d() >= PROMOTION_DEMAND_IMPRESSIONS) {
            status = SearchDemandStatus.PROMOTION_DEMAND;
            note = "Demand exists for this held route. Keep the evidence gate explicit, but do not let the promotion queue disappear from view.";
        } else if (route.indexable()
                && snapshot.impressions28d() >= OPTIMIZATION_IMPRESSIONS
                && snapshot.averagePosition() <= CTR_WORK_MAX_POSITION
                && ctr < CTR_WORK_THRESHOLD) {
            status = SearchDemandStatus.CTR_WORK;
            note = "The route is already visible. Tighten title, summary, and issue framing before expanding content volume.";
        } else if (route.indexable()
                && snapshot.impressions28d() >= OPTIMIZATION_IMPRESSIONS
                && snapshot.averagePosition() > DISCOVERABILITY_MIN_POSITION) {
            status = SearchDemandStatus.DISCOVERABILITY_WORK;
            note = "The route has query demand but still sits too far down the page. Strengthen internal linking and authority-to-city routing surfaces.";
        } else {
            status = SearchDemandStatus.MONITOR;
            note = snapshot.note() == null || snapshot.note().isBlank()
                    ? "Keep monitoring this route before changing indexing or copy."
                    : snapshot.note();
        }

        return new RouteSearchDemandAssessment(
                cityLabel(route.profileId()),
                pageLabel(route.template()),
                seedRegistry.canonicalPath(route),
                route.indexable() ? "Indexed" : "Noindex queue",
                status,
                snapshot.topQuery(),
                snapshot.impressions28d(),
                snapshot.clicks28d(),
                ctr,
                snapshot.averagePosition(),
                snapshot.capturedOn().toString(),
                note
        );
    }

    private String cityLabel(String profileId) {
        var profile = seedRegistry.profile(profileId);
        return titleCase(profile.city()) + ", " + profile.state().toUpperCase(Locale.ROOT);
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
                builder.append(part.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return builder.toString();
    }

    public record RouteSearchDemandAssessment(
            String cityLabel,
            String pageLabel,
            String path,
            String routeStateLabel,
            SearchDemandStatus status,
            String topQuery,
            int impressions28d,
            int clicks28d,
            double ctr,
            double averagePosition,
            String capturedOn,
            String note
    ) {
    }

    public enum SearchDemandStatus {
        PROMOTION_DEMAND("Promotion demand", 0),
        CTR_WORK("CTR work", 1),
        DISCOVERABILITY_WORK("Discoverability work", 2),
        MONITOR("Monitor", 3);

        private final String label;
        private final int priority;

        SearchDemandStatus(String label, int priority) {
            this.label = label;
            this.priority = priority;
        }

        public String label() {
            return label;
        }

        public int priority() {
            return priority;
        }
    }
}
