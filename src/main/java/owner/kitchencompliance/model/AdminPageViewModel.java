package owner.kitchencompliance.model;

public record AdminPageViewModel(
        PageMeta meta,
        String title,
        String summary,
        AttributionDashboardSnapshot dashboard,
        LeadDashboardSnapshot leads,
        FreshnessDashboardSnapshot freshness,
        SourceQualityDashboardSnapshot sourceQuality,
        DeployReadinessDashboardSnapshot deployReadiness,
        NoindexPromotionQueueSnapshot noindexPromotionQueue,
        SearchDemandDashboardSnapshot searchDemand
) {
}
