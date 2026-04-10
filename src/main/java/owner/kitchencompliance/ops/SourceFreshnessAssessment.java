package owner.kitchencompliance.ops;

import java.time.LocalDate;

public record SourceFreshnessAssessment(
        String sourceId,
        String sourceTitle,
        LocalDate verifiedOn,
        LocalDate nextReviewOn,
        boolean fresh,
        String statusMessage
) {
}
