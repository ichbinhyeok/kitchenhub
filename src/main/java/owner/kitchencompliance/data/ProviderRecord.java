package owner.kitchencompliance.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ProviderRecord(
        @NotBlank String providerId,
        @NotBlank String providerName,
        @NotNull ProviderType providerType,
        @NotEmpty List<@NotBlank String> coverageTargets,
        @NotNull ListingMode listingMode,
        @NotNull SponsorStatus sponsorStatus,
        @NotBlank String siteUrl,
        @NotBlank String email,
        @NotBlank String phone,
        String officialApprovalSourceUrl,
        @NotBlank String internalNote
) {
}
