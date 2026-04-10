package owner.kitchencompliance.ops;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.ProviderRecord;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.data.SourceRecord;

@Service
public class DeployReadinessAssessmentService {

    private static final int DUE_SOON_DAYS = 30;

    private final SeedRegistry seedRegistry;
    private final SourceFreshnessService sourceFreshnessService;
    private final SourceQualityAssessmentService sourceQualityAssessmentService;
    private final IndexingPolicyService indexingPolicyService;
    private final Clock clock;

    public DeployReadinessAssessmentService(
            SeedRegistry seedRegistry,
            SourceFreshnessService sourceFreshnessService,
            SourceQualityAssessmentService sourceQualityAssessmentService,
            IndexingPolicyService indexingPolicyService,
            Clock clock
    ) {
        this.seedRegistry = seedRegistry;
        this.sourceFreshnessService = sourceFreshnessService;
        this.sourceQualityAssessmentService = sourceQualityAssessmentService;
        this.indexingPolicyService = indexingPolicyService;
        this.clock = clock;
    }

    public List<RouteDeployReadinessAssessment> assessIndexedRoutes() {
        return seedRegistry.routes().stream()
                .filter(RouteRecord::indexable)
                .map(this::assess)
                .sorted(Comparator
                        .comparingInt((RouteDeployReadinessAssessment assessment) -> assessment.status().priority())
                        .thenComparing(RouteDeployReadinessAssessment::cityLabel)
                        .thenComparing(RouteDeployReadinessAssessment::path))
                .toList();
    }

    public RouteDeployReadinessAssessment assess(RouteRecord route) {
        List<SourceRecord> sources = seedRegistry.sourcesFor(route);
        List<ProviderRecord> providers = providersFor(route);
        LocalDate today = LocalDate.now(clock);
        LocalDate dueSoonCutoff = today.plusDays(DUE_SOON_DAYS);
        LocalDate nextReviewOn = sources.stream()
                .map(SourceRecord::nextReviewOn)
                .min(LocalDate::compareTo)
                .orElse(today);

        List<String> blockers = new ArrayList<>();
        List<String> watchNotes = new ArrayList<>();

        if (!sourceFreshnessService.allFresh(sources)) {
            blockers.add("One or more cited sources are stale.");
        } else if (!nextReviewOn.isAfter(dueSoonCutoff)) {
            watchNotes.add("Next source review is due by " + nextReviewOn + ".");
        }

        var sourceQuality = sourceQualityAssessmentService.assess(route);
        if (sourceQuality.status() == SourceQualityAssessmentService.SourceQualityStatus.CRITICAL) {
            blockers.add(sourceQuality.note());
        } else if (sourceQuality.status() == SourceQualityAssessmentService.SourceQualityStatus.THIN) {
            watchNotes.add("Source stack is still thin for an indexed route.");
        }

        int renderableProviders = 0;
        int authorityBackedProviders = 0;
        if (isFinder(route.template())) {
            renderableProviders = indexingPolicyService.renderableProviderCount(providers);
            authorityBackedProviders = indexingPolicyService.authorityBackedProviderCount(providers);
            if (renderableProviders < indexingPolicyService.minimumFinderProviderCount()) {
                blockers.add("Finder has only " + renderableProviders + " renderable providers; needs at least "
                        + indexingPolicyService.minimumFinderProviderCount() + ".");
            } else if (renderableProviders == indexingPolicyService.minimumFinderProviderCount()) {
                watchNotes.add("Finder is just at the minimum public coverage threshold.");
            }
            if (authorityBackedProviders == 0) {
                watchNotes.add("Finder has no provider card tied to an authority citation yet.");
            }
        }

        RouteDeployStatus status;
        String note;
        if (!blockers.isEmpty()) {
            status = RouteDeployStatus.BLOCKED;
            note = String.join(" ", blockers);
        } else if (!watchNotes.isEmpty()) {
            status = RouteDeployStatus.WATCH;
            note = String.join(" ", watchNotes);
        } else {
            status = RouteDeployStatus.READY;
            note = "Route clears freshness, source quality, and finder coverage gates.";
        }

        boolean indexableNow = indexingPolicyService.isIndexable(route, sources, providers);

        return new RouteDeployReadinessAssessment(
                cityLabel(route.profileId()),
                pageLabel(route.template()),
                route.path(),
                status,
                indexableNow,
                nextReviewOn,
                sourceQuality.totalSources(),
                sourceQuality.strongSources(),
                renderableProviders,
                authorityBackedProviders,
                note
        );
    }

    private List<ProviderRecord> providersFor(RouteRecord route) {
        return switch (route.template()) {
            case FIND_GREASE_SERVICE -> seedRegistry.providersFor(route.profileId(), owner.kitchencompliance.data.ProviderType.GREASE_HAULER);
            case FIND_HOOD_CLEANER -> seedRegistry.providersFor(route.profileId(), owner.kitchencompliance.data.ProviderType.HOOD_CLEANER);
            default -> List.of();
        };
    }

    private boolean isFinder(RouteTemplate template) {
        return template == RouteTemplate.FIND_GREASE_SERVICE || template == RouteTemplate.FIND_HOOD_CLEANER;
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

    public record RouteDeployReadinessAssessment(
            String cityLabel,
            String pageLabel,
            String path,
            RouteDeployStatus status,
            boolean indexableNow,
            LocalDate nextReviewOn,
            int totalSources,
            int strongSources,
            int renderableProviders,
            int authorityBackedProviders,
            String note
    ) {
    }

    public enum RouteDeployStatus {
        BLOCKED("Blocked", 0),
        WATCH("Watch", 1),
        READY("Ready", 2);

        private final String label;
        private final int priority;

        RouteDeployStatus(String label, int priority) {
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
