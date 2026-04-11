package owner.kitchencompliance.model;

public record SearchDemandWatchRow(
        String cityLabel,
        String pageLabel,
        String path,
        String routeStateLabel,
        String statusLabel,
        String topQuery,
        int impressions28d,
        int clicks28d,
        String ctrLabel,
        String averagePositionLabel,
        String capturedOn,
        String note
) {
}
