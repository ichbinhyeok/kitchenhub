package owner.kitchencompliance.model;

import java.util.List;

public record CityCard(
        String city,
        String state,
        String summary,
        List<RelatedPageLink> primaryLinks
) {
}
