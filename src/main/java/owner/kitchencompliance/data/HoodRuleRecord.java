package owner.kitchencompliance.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record HoodRuleRecord(
        @NotBlank String ruleId,
        @NotBlank String authorityId,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String hoodType,
        @NotEmpty List<@NotBlank String> cleaningFrequencyBands,
        @NotBlank String certificateRequirement,
        @NotBlank String serviceTagRequirement,
        @NotBlank String reportRetentionRule,
        @NotBlank String suppressionInspectionRequirement,
        @NotEmpty List<@NotBlank String> sourceRefs,
        @NotNull LocalDate lastVerified
) {
}
