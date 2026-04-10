package owner.kitchencompliance.model;

import java.util.List;

public record LeadDashboardSnapshot(
        String storagePath,
        String storageGuidance,
        boolean hasLeads,
        long totalLeads,
        long operatorRequests,
        long sponsorInquiries,
        long consentedLeads,
        String lastCapturedAt,
        List<AdminBreakdownRow> cityBreakdown,
        List<AdminBreakdownRow> leadTypeBreakdown,
        List<AdminBreakdownRow> providerIntentBreakdown,
        List<AdminRecentLead> recentLeads
) {
}
