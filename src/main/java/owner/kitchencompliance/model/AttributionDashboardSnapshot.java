package owner.kitchencompliance.model;

import java.util.List;

public record AttributionDashboardSnapshot(
        String storagePath,
        String storageGuidance,
        boolean hasEvents,
        long totalEvents,
        long pageViewEvents,
        long utilityViewEvents,
        long providerClicks,
        long ctaClicks,
        long sponsoredClicks,
        long returningUtilityVisitors,
        String utilityRevisitRateLabel,
        String lastCapturedAt,
        List<AdminBreakdownRow> cityBreakdown,
        List<AdminBreakdownRow> pageFamilyBreakdown,
        List<AdminBreakdownRow> verdictBreakdown,
        List<AdminBreakdownRow> destinationBreakdown,
        List<AdminRecentEvent> recentEvents
) {
}
