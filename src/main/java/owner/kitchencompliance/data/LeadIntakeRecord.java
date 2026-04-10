package owner.kitchencompliance.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record LeadIntakeRecord(
        @NotBlank String leadId,
        @NotNull OffsetDateTime capturedAt,
        @NotBlank String city,
        @NotBlank String state,
        @NotNull PageFamily pageFamily,
        @NotNull IssueType issueType,
        @NotBlank String operatorType,
        @NotBlank String authorityId,
        @NotBlank String providerIntent,
        @NotBlank String notes,
        boolean routingConsent
) {
}
