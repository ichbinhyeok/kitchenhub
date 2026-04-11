package owner.kitchencompliance.model;

import java.util.List;

public record NoindexPromotionQueueSnapshot(
        long queuedRoutes,
        long readyToPromoteRoutes,
        long dueSoonRoutes,
        long overdueRoutes,
        List<NoindexPromotionRow> rows
) {
}
