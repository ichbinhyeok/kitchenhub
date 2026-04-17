package owner.kitchencompliance.data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CityComplianceProfile(
        @NotBlank String profileId,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String fogAuthorityId,
        @NotBlank String hoodAuthorityId,
        @Min(1) int launchTier,
        boolean indexable,
        @NotBlank String homeSummary,
        @NotBlank String decisionReason
) {
}
