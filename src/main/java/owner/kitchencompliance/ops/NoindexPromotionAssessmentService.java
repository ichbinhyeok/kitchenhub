package owner.kitchencompliance.ops;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.ApprovedHaulerMode;
import owner.kitchencompliance.data.ProviderRecord;
import owner.kitchencompliance.data.ProviderType;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.data.SourceRecord;
import owner.kitchencompliance.data.SourceTier;

@Service
public class NoindexPromotionAssessmentService {

    private static final int DUE_SOON_DAYS = 14;
    private static final int DEFAULT_REVIEW_DAYS = 30;

    private final SeedRegistry seedRegistry;
    private final SourceFreshnessService sourceFreshnessService;
    private final SourceQualityAssessmentService sourceQualityAssessmentService;
    private final IndexingPolicyService indexingPolicyService;
    private final Clock clock;

    public NoindexPromotionAssessmentService(
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

    public List<RouteNoindexPromotionAssessment> assessQueuedRoutes() {
        return seedRegistry.routes().stream()
                .filter(route -> !route.indexable())
                .map(this::assess)
                .sorted(Comparator
                        .comparingInt((RouteNoindexPromotionAssessment assessment) -> assessment.status().priority())
                        .thenComparing(RouteNoindexPromotionAssessment::cityLabel)
                        .thenComparing(RouteNoindexPromotionAssessment::path))
                .toList();
    }

    public RouteNoindexPromotionAssessment assess(RouteRecord route) {
        if (route.indexable()) {
            throw new IllegalArgumentException("Promotion queue only applies to routes that are currently marked noindex.");
        }

        List<SourceRecord> sources = seedRegistry.sourcesFor(route);
        List<ProviderRecord> providers = providersFor(route);
        var sourceQuality = sourceQualityAssessmentService.assess(route);
        boolean fresh = sourceFreshnessService.allFresh(sources);
        boolean readyToPromote = indexingPolicyService.passesIndexingGates(route, sources, providers);
        int renderableProviders = isFinder(route.template()) ? indexingPolicyService.renderableProviderCount(providers) : 0;
        int authorityBackedProviders = isFinder(route.template()) ? indexingPolicyService.authorityBackedProviderCount(providers) : 0;

        LocalDate today = LocalDate.now(clock);
        LocalDate nextReviewOn = route.promotionReviewOn() != null
                ? route.promotionReviewOn()
                : inferredReviewOn(route, sources, today);

        PromotionStatus status;
        if (readyToPromote) {
            status = PromotionStatus.READY_TO_PROMOTE;
        } else if (nextReviewOn.isBefore(today)) {
            status = PromotionStatus.OVERDUE;
        } else if (!nextReviewOn.isAfter(today.plusDays(DUE_SOON_DAYS))) {
            status = PromotionStatus.DUE_SOON;
        } else {
            status = PromotionStatus.QUEUED;
        }

        String reason = explicitOrInferredReason(route, fresh, sourceQuality, renderableProviders, authorityBackedProviders, sources);
        List<String> checklist = route.promotionChecklist().isEmpty()
                ? inferredChecklist(route, nextReviewOn, fresh, sourceQuality, renderableProviders, authorityBackedProviders, sources)
                : route.promotionChecklist();

        return new RouteNoindexPromotionAssessment(
                cityLabel(route.profileId()),
                pageLabel(route.template()),
                seedRegistry.canonicalPath(route),
                status,
                readyToPromote,
                nextReviewOn,
                reason,
                checklist,
                sourceQuality.totalSources(),
                sourceQuality.strongSources(),
                renderableProviders,
                authorityBackedProviders
        );
    }

    private LocalDate inferredReviewOn(RouteRecord route, List<SourceRecord> sources, LocalDate today) {
        return sources.stream()
                .map(SourceRecord::nextReviewOn)
                .min(LocalDate::compareTo)
                .orElse(route.lastGenerated().toLocalDate().plusDays(DEFAULT_REVIEW_DAYS));
    }

    private String explicitOrInferredReason(
            RouteRecord route,
            boolean fresh,
            SourceQualityAssessmentService.RouteSourceQualityAssessment sourceQuality,
            int renderableProviders,
            int authorityBackedProviders,
            List<SourceRecord> sources
    ) {
        if (route.noindexReason() != null && !route.noindexReason().isBlank()) {
            return route.noindexReason();
        }
        if (!fresh) {
            return "One or more cited sources are stale or due for refresh before promotion.";
        }
        if (sourceQuality.status() == SourceQualityAssessmentService.SourceQualityStatus.CRITICAL) {
            return sourceQuality.note();
        }
        if (isFinder(route.template()) && renderableProviders < indexingPolicyService.minimumFinderProviderCount()) {
            return "Public provider coverage is below the minimum threshold for an indexed finder.";
        }
        if (isFinder(route.template()) && authorityBackedProviders == 0) {
            return "Provider evidence is still too weak to support an indexed finder.";
        }
        if (requiresOfficialListEvidence(route) && !hasOfficialListEvidence(sources)) {
            return "Official-list language is not supported strongly enough by the current source stack.";
        }
        return route.decisionReason();
    }

    private List<String> inferredChecklist(
            RouteRecord route,
            LocalDate nextReviewOn,
            boolean fresh,
            SourceQualityAssessmentService.RouteSourceQualityAssessment sourceQuality,
            int renderableProviders,
            int authorityBackedProviders,
            List<SourceRecord> sources
    ) {
        Set<String> steps = new LinkedHashSet<>();
        if (!fresh) {
            steps.add("Refresh stale cited sources and update verifiedOn/nextReviewOn before promotion.");
        }
        if (sourceQuality.status() == SourceQualityAssessmentService.SourceQualityStatus.CRITICAL) {
            steps.add("Add or strengthen local authority sources until the route clears source-quality gates.");
        } else if (sourceQuality.status() == SourceQualityAssessmentService.SourceQualityStatus.THIN) {
            steps.add("Add one more local authority source so the route does not launch on a thin stack.");
        }
        if (sourceQuality.strongSources() == 0) {
            steps.add("Add at least one Tier 1 or Tier 2 official local source.");
        }
        if (requiresOfficialListEvidence(route) && !hasOfficialListEvidence(sources)) {
            steps.add("Confirm whether the authority publishes an approved or permitted list before using official-list language.");
        }
        if (isFinder(route.template()) && renderableProviders < indexingPolicyService.minimumFinderProviderCount()) {
            steps.add("Add at least " + indexingPolicyService.minimumFinderProviderCount() + " renderable providers with local coverage.");
        }
        if (isFinder(route.template()) && authorityBackedProviders == 0) {
            steps.add("Add an authority-backed evidence link on at least one provider card or keep the route verification-first.");
        }
        if (steps.isEmpty()) {
            steps.add("Re-review the route and confirm the noindex decision is still intentional.");
        }
        steps.add("Review the route on or before " + nextReviewOn + " and flip indexable only after the checklist is complete.");
        return List.copyOf(steps);
    }

    private boolean requiresOfficialListEvidence(RouteRecord route) {
        return route.template() == RouteTemplate.APPROVED_HAULERS
                || (route.template() == RouteTemplate.FIND_GREASE_SERVICE
                && seedRegistry.fogRule(route.profileId()).approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST);
    }

    private boolean hasOfficialListEvidence(List<SourceRecord> sources) {
        return sources.stream().anyMatch(source -> {
            String haystack = (source.title() + " " + source.quoteSummary() + " " + source.sourceUrl()).toLowerCase(Locale.ROOT);
            return haystack.contains("hauler")
                    || haystack.contains("preferred pumper")
                    || haystack.contains("transporter")
                    || haystack.contains("registry")
                    || haystack.contains("list");
        });
    }

    private List<ProviderRecord> providersFor(RouteRecord route) {
        return switch (route.template()) {
            case FIND_GREASE_SERVICE -> seedRegistry.providersFor(route.profileId(), ProviderType.GREASE_HAULER);
            case FIND_HOOD_CLEANER -> seedRegistry.providersFor(route.profileId(), ProviderType.HOOD_CLEANER);
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

    public record RouteNoindexPromotionAssessment(
            String cityLabel,
            String pageLabel,
            String path,
            PromotionStatus status,
            boolean readyToPromote,
            LocalDate nextReviewOn,
            String reason,
            List<String> checklist,
            int totalSources,
            int strongSources,
            int renderableProviders,
            int authorityBackedProviders
    ) {
    }

    public enum PromotionStatus {
        READY_TO_PROMOTE("Ready to promote", 0),
        OVERDUE("Overdue", 1),
        DUE_SOON("Due soon", 2),
        QUEUED("Queued", 3);

        private final String label;
        private final int priority;

        PromotionStatus(String label, int priority) {
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
