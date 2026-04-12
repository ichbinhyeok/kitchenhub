package owner.kitchencompliance.model;

import java.util.List;

public record SponsorBetaFocusSnapshot(
        long sponsoredClicks,
        long sponsorInquiries,
        List<AdminBreakdownRow> launchCities,
        List<AdminBreakdownRow> launchCategories,
        String note
) {
}
