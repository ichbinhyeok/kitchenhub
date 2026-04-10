package owner.kitchencompliance.ops;

import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.ListingMode;
import owner.kitchencompliance.data.ProviderRecord;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SourceRecord;
import owner.kitchencompliance.data.SponsorStatus;

@Service
public class IndexingPolicyService {

    private static final int MINIMUM_FINDER_PROVIDER_COUNT = 3;

    private final SourceFreshnessService sourceFreshnessService;
    private final SourceQualityAssessmentService sourceQualityAssessmentService;

    public IndexingPolicyService(
            SourceFreshnessService sourceFreshnessService,
            SourceQualityAssessmentService sourceQualityAssessmentService
    ) {
        this.sourceFreshnessService = sourceFreshnessService;
        this.sourceQualityAssessmentService = sourceQualityAssessmentService;
    }

    public boolean isIndexable(RouteRecord route, List<SourceRecord> sources, List<ProviderRecord> providers) {
        if (!route.indexable()) {
            return false;
        }
        if (sources.isEmpty() || !sourceFreshnessService.allFresh(sources)) {
            return false;
        }
        if (sourceQualityAssessmentService.assess(route).status() == SourceQualityAssessmentService.SourceQualityStatus.CRITICAL) {
            return false;
        }
        if (route.template() == RouteTemplate.FIND_GREASE_SERVICE || route.template() == RouteTemplate.FIND_HOOD_CLEANER) {
            return renderableProviderCount(providers) >= minimumFinderProviderCount();
        }
        return true;
    }

    public boolean isPubliclyRenderable(ProviderRecord provider) {
        if (provider.listingMode() == ListingMode.PUBLIC) {
            return provider.sponsorStatus() != SponsorStatus.HOLD;
        }
        return provider.sponsorStatus() == SponsorStatus.ACTIVE;
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
}
