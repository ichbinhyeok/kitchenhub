package owner.kitchencompliance.model;

import java.util.List;

public record DeployReadinessDashboardSnapshot(
        long indexedRoutes,
        long readyRoutes,
        long watchRoutes,
        long blockedRoutes,
        List<DeployReadinessWatchRow> watchRows
) {
}
