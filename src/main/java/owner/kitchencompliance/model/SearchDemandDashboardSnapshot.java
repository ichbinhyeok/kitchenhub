package owner.kitchencompliance.model;

import java.util.List;

public record SearchDemandDashboardSnapshot(
        long trackedRoutes,
        long promotionDemandRoutes,
        long ctrWorkRoutes,
        long discoverabilityWorkRoutes,
        long monitorRoutes,
        List<SearchDemandWatchRow> rows
) {
}
