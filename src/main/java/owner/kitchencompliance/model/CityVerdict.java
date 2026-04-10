package owner.kitchencompliance.model;

import java.util.List;

public record CityVerdict(
        String city,
        String state,
        String authoritySummary,
        List<String> whatAppliesNow,
        List<String> whatToKeepOnSite,
        List<String> whatFailsInspections,
        List<String> nextActions,
        String ctaType
) {
}
