package owner.kitchencompliance.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record RouteRecord(
        @NotBlank String path,
        @NotNull RouteTemplate template,
        @NotBlank String state,
        @NotBlank String city,
        @NotBlank String authorityId,
        @NotBlank String profileId,
        @NotBlank String canonicalPath,
        boolean indexable,
        @NotBlank String decisionReason,
        String noindexReason,
        List<String> promotionChecklist,
        LocalDate promotionReviewOn,
        @NotNull OffsetDateTime lastGenerated
) {
    public RouteRecord {
        promotionChecklist = promotionChecklist == null ? List.of() : List.copyOf(promotionChecklist);
    }
}
