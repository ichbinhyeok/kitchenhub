package owner.kitchencompliance.data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SearchDemandSnapshotRecord(
        @NotBlank String routePath,
        @NotBlank String topQuery,
        @Min(0) int impressions28d,
        @Min(0) int clicks28d,
        @DecimalMin("0.0") double averagePosition,
        @NotNull LocalDate capturedOn,
        String note
) {
}
