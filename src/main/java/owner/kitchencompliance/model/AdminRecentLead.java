package owner.kitchencompliance.model;

public record AdminRecentLead(
        String capturedAt,
        String leadType,
        String location,
        String providerIntent,
        String contact,
        String consentLabel,
        String sourcePath
) {
}
