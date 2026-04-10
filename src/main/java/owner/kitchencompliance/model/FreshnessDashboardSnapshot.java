package owner.kitchencompliance.model;

import java.util.List;

public record FreshnessDashboardSnapshot(
        long indexedRoutes,
        long freshRoutes,
        long dueSoonRoutes,
        long staleRoutes,
        String nextReviewDue,
        List<FreshnessWatchRow> watchRows
) {
}
