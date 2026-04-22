package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProviderRecord(
        @NotBlank String providerId,
        @NotBlank String providerName,
        @NotNull ProviderType providerType,
        @NotEmpty List<@NotBlank String> coverageTargets,
        @NotBlank String siteUrl,
        @NotBlank String email,
        @NotBlank String phone,
        String officialApprovalSourceUrl,
        @NotBlank String internalNote
) {
}
