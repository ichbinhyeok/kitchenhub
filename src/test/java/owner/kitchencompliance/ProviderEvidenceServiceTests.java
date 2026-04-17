package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import owner.kitchencompliance.data.ListingMode;
import owner.kitchencompliance.data.ProviderRecord;
import owner.kitchencompliance.data.ProviderType;
import owner.kitchencompliance.data.SponsorStatus;
import owner.kitchencompliance.rules.ProviderEvidenceService;

class ProviderEvidenceServiceTests {

    private final ProviderEvidenceService providerEvidenceService = new ProviderEvidenceService();

    @Test
    void sortsByTrustFirstThenContactQualityAndScopeFocus() {
        ProviderRecord publicOfficial = provider(
                "public-official",
                "Public Official",
                ProviderType.GREASE_HAULER,
                ListingMode.PUBLIC,
                SponsorStatus.ACTIVE,
                "https://example.com/public-official",
                "public@example.com",
                "512-555-0100",
                List.of("austin-tx-kitchen-compliance")
        );
        ProviderRecord sponsorOfficial = provider(
                "sponsor-official",
                "Sponsor Official",
                ProviderType.GREASE_HAULER,
                ListingMode.SPONSOR_ONLY,
                SponsorStatus.ACTIVE,
                "https://example.com/sponsor-official",
                "sponsor@example.com",
                "512-555-0101",
                List.of("austin-tx-kitchen-compliance")
        );
        ProviderRecord publicContact = provider(
                "public-contact",
                "Public Contact",
                ProviderType.GREASE_HAULER,
                ListingMode.PUBLIC,
                SponsorStatus.ACTIVE,
                "",
                "public-only@example.com",
                "512-555-0102",
                List.of("austin-tx-kitchen-compliance")
        );
        ProviderRecord sponsorWeak = provider(
                "sponsor-weak",
                "Sponsor Weak",
                ProviderType.GREASE_HAULER,
                ListingMode.SPONSOR_ONLY,
                SponsorStatus.PROSPECT,
                "",
                " ",
                " ",
                List.of("austin-tx-kitchen-compliance", "charlotte-nc-kitchen-compliance", "tampa-fl-kitchen-compliance")
        );

        List<ProviderRecord> sorted = providerEvidenceService.sortByEvidenceQuality(
                List.of(publicContact, sponsorWeak, sponsorOfficial, publicOfficial)
        );

        assertThat(sorted).extracting(ProviderRecord::providerName)
                .containsExactly(
                        "Public Official",
                        "Sponsor Official",
                        "Public Contact",
                        "Sponsor Weak"
                );
    }

    @Test
    void evidenceLabelAndNotesDifferentiatePublicSponsorAndOfficialCoverage() {
        ProviderRecord officialPublic = provider(
                "official-public",
                "Official Public",
                ProviderType.HOOD_CLEANER,
                ListingMode.PUBLIC,
                SponsorStatus.ACTIVE,
                "https://example.com/official",
                "ops@example.com",
                "512-555-0199",
                List.of("austin-tx-kitchen-compliance")
        );
        ProviderRecord sponsorPartial = provider(
                "sponsor-partial",
                "Sponsor Partial",
                ProviderType.HOOD_CLEANER,
                ListingMode.SPONSOR_ONLY,
                SponsorStatus.HOLD,
                "",
                "ops@example.com",
                " ",
                List.of("austin-tx-kitchen-compliance", "charlotte-nc-kitchen-compliance")
        );

        assertThat(providerEvidenceService.evidenceLabel(officialPublic))
                .isEqualTo("Official source + public contact");
        assertThat(providerEvidenceService.providerNote(officialPublic))
                .contains("official source link");
        assertThat(providerEvidenceService.coverageConfidenceLabel(officialPublic))
                .isEqualTo("High");
        assertThat(providerEvidenceService.whyListed(officialPublic))
                .contains("Official source link");
        assertThat(providerEvidenceService.evidenceLabel(sponsorPartial))
                .isEqualTo("Paid placement only");
        assertThat(providerEvidenceService.providerNote(sponsorPartial))
                .contains("partial contact details");
        assertThat(providerEvidenceService.coverageConfidenceLabel(sponsorPartial))
                .isEqualTo("Needs operator verification");
        assertThat(providerEvidenceService.whyListed(sponsorPartial))
                .contains("Paid placement");
    }

    private ProviderRecord provider(
            String providerId,
            String providerName,
            ProviderType providerType,
            ListingMode listingMode,
            SponsorStatus sponsorStatus,
            String officialApprovalSourceUrl,
            String email,
            String phone,
            List<String> coverageTargets
    ) {
        return new ProviderRecord(
                providerId,
                providerName,
                providerType,
                coverageTargets,
                listingMode,
                sponsorStatus,
                "https://example.com/" + providerId,
                email,
                phone,
                officialApprovalSourceUrl,
                "test note"
        );
    }
}
