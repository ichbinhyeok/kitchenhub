package owner.kitchencompliance.model;

public record ProviderCard(
        String providerName,
        String providerTypeLabel,
        String siteUrl,
        String email,
        String phone,
        String listingLabel,
        String sponsorStatusLabel,
        String evidenceLabel,
        String note,
        String officialApprovalSourceUrl
) {
}
