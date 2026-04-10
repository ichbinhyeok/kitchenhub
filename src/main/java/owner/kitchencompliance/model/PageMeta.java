package owner.kitchencompliance.model;

import java.time.LocalDate;

public record PageMeta(
        String title,
        String description,
        String canonicalUrl,
        String robots,
        LocalDate lastVerified,
        String structuredDataJson
) {
}
