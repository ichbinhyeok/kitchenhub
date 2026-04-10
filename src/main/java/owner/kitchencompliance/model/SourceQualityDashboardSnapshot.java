package owner.kitchencompliance.model;

import java.util.List;

public record SourceQualityDashboardSnapshot(
        long indexedRoutes,
        long strongRoutes,
        long adequateRoutes,
        long thinRoutes,
        long criticalRoutes,
        List<SourceQualityWatchRow> watchRows
) {
}
