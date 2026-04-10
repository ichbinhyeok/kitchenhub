package owner.kitchencompliance.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record InspectionPrepRecord(
        @NotBlank String recordId,
        @NotBlank String city,
        @NotBlank String state,
        @NotNull InspectionType inspectionType,
        @NotEmpty List<@NotBlank String> whatMustBeOnSite,
        @NotEmpty List<@NotBlank String> commonFailureReasons,
        @NotBlank String rescheduleMethod,
        @NotBlank String penaltyOrEscalation,
        @NotEmpty List<@NotBlank String> sourceRefs
) {
}
