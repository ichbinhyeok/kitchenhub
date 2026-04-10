package owner.kitchencompliance.model;

import java.time.LocalDate;

public record SourceAttribution(
        String title,
        String agency,
        String url,
        String summary,
        String tierLabel,
        LocalDate verifiedOn
) {
}
