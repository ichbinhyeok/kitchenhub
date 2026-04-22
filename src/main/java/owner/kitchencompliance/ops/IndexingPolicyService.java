package owner.kitchencompliance.ops;

import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.ApprovedHaulerMode;
import owner.kitchencompliance.data.ProviderRecord;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.data.SourceRecord;

@Service
public class IndexingPolicyService {

    private static final int MINIMUM_FINDER_PROVIDER_COUNT = 3;

    private final SourceFreshnessService sourceFreshnessService;
    private final SourceQualityAssessmentService sourceQualityAssessmentService;
    private final SeedRegistry seedRegistry;

    public IndexingPolicyService(
            SourceFreshnessService sourceFreshnessService,
            SourceQualityAssessmentService sourceQualityAssessmentService,
            SeedRegistry seedRegistry
    ) {
        this.sourceFreshnessService = sourceFreshnessService;
        this.sourceQualityAssessmentService = sourceQualityAssessmentService;
        this.seedRegistry = seedRegistry;
    }

    public boolean isIndexable(RouteRecord route, List<SourceRecord> sources, List<ProviderRecord> providers) {
        return route.indexable() && passesIndexingGates(route, sources, providers);
    }

    public boolean passesIndexingGates(RouteRecord route, List<SourceRecord> sources, List<ProviderRecord> providers) {
        if (sources.isEmpty() || !sourceFreshnessService.allFresh(sources)) {
            return false;
        }
        if (sourceQualityAssessmentService.assess(route).status() == SourceQualityAssessmentService.SourceQualityStatus.CRITICAL) {
            return false;
        }
        if (route.template() == RouteTemplate.FIND_GREASE_SERVICE || route.template() == RouteTemplate.FIND_HOOD_CLEANER) {
            if (renderableProviderCount(providers) < minimumFinderProviderCount()) {
                return false;
            }
            if (directContactProviderCount(providers) < minimumFinderProviderCount()) {
                return false;
            }
            if (requiresAuthorityBackedProvider(route) && authorityBackedProviderCount(providers) == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isPubliclyRenderable(ProviderRecord provider) {
        return hasDirectContact(provider);
    }

    public int minimumFinderProviderCount() {
        return MINIMUM_FINDER_PROVIDER_COUNT;
    }

    public int renderableProviderCount(List<ProviderRecord> providers) {
        return (int) providers.stream()
                .filter(this::isPubliclyRenderable)
                .count();
    }

    public int authorityBackedProviderCount(List<ProviderRecord> providers) {
        return (int) providers.stream()
                .filter(this::isPubliclyRenderable)
                .filter(provider -> provider.officialApprovalSourceUrl() != null && !provider.officialApprovalSourceUrl().isBlank())
                .count();
    }

    public int directContactProviderCount(List<ProviderRecord> providers) {
        return (int) providers.stream()
                .filter(this::isPubliclyRenderable)
                .filter(this::hasDirectContact)
                .count();
    }

    public boolean requiresAuthorityBackedProvider(RouteRecord route) {
        if (route.template() == RouteTemplate.FIND_GREASE_SERVICE) {
            return seedRegistry.fogRule(route.profileId()).approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST;
        }
        return false;
    }

    public boolean isWeakFinderRoute(RouteRecord route, List<SourceRecord> sources, List<ProviderRecord> providers) {
        if (route.template() != RouteTemplate.FIND_GREASE_SERVICE && route.template() != RouteTemplate.FIND_HOOD_CLEANER) {
            return false;
        }
        if (!route.indexable()) {
            return true;
        }
        if (renderableProviderCount(providers) < minimumFinderProviderCount()) {
            return true;
        }
        if (directContactProviderCount(providers) < minimumFinderProviderCount()) {
            return true;
        }
        if (requiresAuthorityBackedProvider(route) && authorityBackedProviderCount(providers) == 0) {
            return true;
        }
        return !passesIndexingGates(route, sources, providers);
    }

    private boolean hasDirectContact(ProviderRecord provider) {
        return provider.email() != null
                && !provider.email().isBlank()
                && provider.phone() != null
                && !provider.phone().isBlank();
    }
}
