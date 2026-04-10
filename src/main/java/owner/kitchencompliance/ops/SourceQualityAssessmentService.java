package owner.kitchencompliance.ops;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.ApprovedHaulerMode;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.data.SourceRecord;
import owner.kitchencompliance.data.SourceTier;

@Service
public class SourceQualityAssessmentService {

    private final SeedRegistry seedRegistry;

    public SourceQualityAssessmentService(SeedRegistry seedRegistry) {
        this.seedRegistry = seedRegistry;
    }

    public List<RouteSourceQualityAssessment> assessIndexedRoutes() {
        return seedRegistry.routes().stream()
                .filter(RouteRecord::indexable)
                .map(this::assess)
                .sorted(Comparator
                        .comparingInt((RouteSourceQualityAssessment assessment) -> assessment.status().priority())
                        .thenComparing(RouteSourceQualityAssessment::cityLabel)
                        .thenComparing(RouteSourceQualityAssessment::path))
                .toList();
    }

    public RouteSourceQualityAssessment assess(RouteRecord route) {
        List<SourceRecord> sources = seedRegistry.sourcesFor(route);
        int totalSources = sources.size();
        int strongSources = (int) sources.stream()
                .filter(source -> source.sourceTier() == SourceTier.TIER_1 || source.sourceTier() == SourceTier.TIER_2)
                .count();
        int minimumSources = minimumSources(route.template());

        boolean hasStrongSource = strongSources > 0;
        boolean meetsSourceCount = totalSources >= minimumSources;
        boolean requiresOfficialListEvidence = route.template() == RouteTemplate.APPROVED_HAULERS
                || (route.template() == RouteTemplate.FIND_GREASE_SERVICE
                && seedRegistry.fogRule(route.profileId()).approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST);
        boolean hasOfficialListEvidence = !requiresOfficialListEvidence || sources.stream()
                .anyMatch(this::isOfficialListEvidence);

        SourceQualityStatus status;
        String note;
        if (!meetsSourceCount) {
            status = SourceQualityStatus.CRITICAL;
            note = "Needs at least " + minimumSources + " local sources for this indexed route.";
        } else if (!hasStrongSource) {
            status = SourceQualityStatus.CRITICAL;
            note = "Needs at least one Tier 1 or Tier 2 official source.";
        } else if (!hasOfficialListEvidence) {
            status = SourceQualityStatus.CRITICAL;
            note = "Official-list copy is live, but the source stack does not clearly prove a hauler list or registry.";
        } else if (strongSources >= 2 || totalSources >= 4) {
            status = SourceQualityStatus.STRONG;
            note = "Source stack has multiple strong official sources.";
        } else if (totalSources == minimumSources && strongSources == 1) {
            status = SourceQualityStatus.THIN;
            note = "Launch-safe, but the route still leans on a thin source stack.";
        } else {
            status = SourceQualityStatus.ADEQUATE;
            note = "Meets launch quality gates with at least one strong official source.";
        }

        return new RouteSourceQualityAssessment(
                cityLabel(route.profileId()),
                pageLabel(route.template()),
                route.path(),
                status,
                totalSources,
                strongSources,
                note
        );
    }

    private boolean isOfficialListEvidence(SourceRecord source) {
        String haystack = (source.title() + " " + source.quoteSummary() + " " + source.sourceUrl()).toLowerCase(Locale.ROOT);
        return haystack.contains("hauler")
                || haystack.contains("preferred pumper")
                || haystack.contains("transporter")
                || haystack.contains("registry")
                || haystack.contains("list");
    }

    private int minimumSources(RouteTemplate template) {
        return switch (template) {
            case FOG_RULES, APPROVED_HAULERS, HOOD_REQUIREMENTS, INSPECTION_CHECKLIST, FIND_GREASE_SERVICE, FIND_HOOD_CLEANER -> 2;
        };
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

    public record RouteSourceQualityAssessment(
            String cityLabel,
            String pageLabel,
            String path,
            SourceQualityStatus status,
            int totalSources,
            int strongSources,
            String note
    ) {
    }

    public enum SourceQualityStatus {
        CRITICAL("Critical", 0),
        THIN("Thin", 1),
        ADEQUATE("Adequate", 2),
        STRONG("Strong", 3);

        private final String label;
        private final int priority;

        SourceQualityStatus(String label, int priority) {
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
