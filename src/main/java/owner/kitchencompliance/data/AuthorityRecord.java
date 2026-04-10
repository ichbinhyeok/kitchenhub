package owner.kitchencompliance.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AuthorityRecord(
        @NotBlank String authorityId,
        @NotNull AuthorityType authorityType,
        @NotBlank String authorityName,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String baseUrl,
        @NotBlank String contactUrl,
        @NotNull LocalDate lastVerified,
        @NotNull VerificationStatus verificationStatus
) {
}
