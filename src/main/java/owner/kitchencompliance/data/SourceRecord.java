package owner.kitchencompliance.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SourceRecord(
        @NotBlank String sourceId,
        @NotNull SourceScopeType scopeType,
        @NotBlank String scopeKey,
        @NotNull SourceTier sourceTier,
        @NotBlank String agency,
        @NotBlank String title,
        @NotBlank String sourceUrl,
        @NotBlank String quoteSummary,
        @NotNull LocalDate verifiedOn,
        @NotNull LocalDate nextReviewOn
) {
}
