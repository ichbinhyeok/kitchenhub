package owner.kitchencompliance.model;

import java.time.LocalDate;

public record SitemapEntry(
        String location,
        LocalDate lastModified,
        String changeFrequency,
        String priority
) {
}
