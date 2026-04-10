package owner.kitchencompliance.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record FogRuleRecord(
        @NotBlank String ruleId,
        @NotBlank String authorityId,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String foodServiceApplicability,
        @NotBlank String interceptorType,
        @NotBlank String pumpOutFrequency,
        @NotBlank String manifestRequirement,
        @NotNull ApprovedHaulerMode approvedHaulerMode,
        @NotBlank String submissionMethod,
        @NotBlank String enforcementNote,
        @NotEmpty List<@NotBlank String> sourceRefs,
        @NotNull LocalDate lastVerified
) {
}
