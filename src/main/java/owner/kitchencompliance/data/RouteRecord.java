package owner.kitchencompliance.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

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
        @NotNull OffsetDateTime lastGenerated
) {
}
