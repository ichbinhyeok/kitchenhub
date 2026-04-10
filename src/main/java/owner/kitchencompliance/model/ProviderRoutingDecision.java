package owner.kitchencompliance.model;

import java.util.List;

public record ProviderRoutingDecision(
        String leadId,
        List<String> providerIds,
        RoutingMode routingMode,
        String reason
) {
}
