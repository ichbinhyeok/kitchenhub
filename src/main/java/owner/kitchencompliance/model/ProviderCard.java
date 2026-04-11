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
        String coverageConfidenceLabel,
        String whyListed,
        String evidenceReviewLabel,
        String note,
        String officialApprovalSourceUrl
) {
}
