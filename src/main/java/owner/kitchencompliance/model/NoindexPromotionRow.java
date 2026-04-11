package owner.kitchencompliance.model;

import java.util.List;

public record NoindexPromotionRow(
        String cityLabel,
        String pageLabel,
        String path,
        String statusLabel,
        String readinessLabel,
        String nextReviewOn,
        String reason,
        List<String> checklist,
        int totalSources,
        int strongSources,
        int renderableProviders,
        int authorityBackedProviders,
        String searchDemandStatusLabel,
        String topQuery,
        Integer impressions28d,
        Integer clicks28d,
        String ctrLabel,
        String averagePositionLabel
) {
}
