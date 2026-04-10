package owner.kitchencompliance.rules;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.ProviderRecord;
import owner.kitchencompliance.model.ProviderRoutingDecision;
import owner.kitchencompliance.model.RoutingMode;
import owner.kitchencompliance.ops.IndexingPolicyService;

@Service
public class ProviderRoutingDecisionService {

    private final IndexingPolicyService indexingPolicyService;
    private final ProviderEvidenceService providerEvidenceService;

    public ProviderRoutingDecisionService(
            IndexingPolicyService indexingPolicyService,
            ProviderEvidenceService providerEvidenceService
    ) {
        this.indexingPolicyService = indexingPolicyService;
        this.providerEvidenceService = providerEvidenceService;
    }

    public ProviderRoutingDecision decide(String routeKey, List<ProviderRecord> providers) {
        List<ProviderRecord> renderableProviders = providerEvidenceService.sortByEvidenceQuality(providers.stream()
                .filter(indexingPolicyService::isPubliclyRenderable)
                .toList());

        if (renderableProviders.isEmpty()) {
            return new ProviderRoutingDecision(
                    routeKey + "-" + UUID.nameUUIDFromBytes(routeKey.getBytes(StandardCharsets.UTF_8)),
                    List.of(),
                    RoutingMode.MANUAL_ONLY,
                    "Coverage is not yet strong enough for a public finder list, so the page stays guidance-first."
            );
        }

        if (renderableProviders.size() == 1) {
            return new ProviderRoutingDecision(
                    routeKey,
                    List.of(renderableProviders.get(0).providerId()),
                    RoutingMode.SINGLE,
                    "One public or active sponsor option is available for this route."
            );
        }

        return new ProviderRoutingDecision(
                routeKey,
                renderableProviders.stream().map(ProviderRecord::providerId).collect(Collectors.toList()),
                RoutingMode.MULTI,
                "Multiple public or active sponsor options are available for this route, ordered by evidence quality first."
        );
    }
}
