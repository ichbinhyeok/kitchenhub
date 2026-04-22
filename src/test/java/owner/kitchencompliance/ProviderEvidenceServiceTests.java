package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import owner.kitchencompliance.data.ProviderRecord;
import owner.kitchencompliance.data.ProviderType;
import owner.kitchencompliance.rules.ProviderEvidenceService;

class ProviderEvidenceServiceTests {

    private final ProviderEvidenceService providerEvidenceService = new ProviderEvidenceService();

    @Test
    void sortsByTrustFirstThenContactQualityAndScopeFocus() {
        ProviderRecord officialFocused = provider(
                "official-focused",
                "Official Focused",
                ProviderType.GREASE_HAULER,
                "https://example.com/official-focused",
                "focused@example.com",
                "512-555-0100",
                List.of("austin-tx-kitchen-compliance")
        );
        ProviderRecord officialBroad = provider(
                "official-broad",
                "Official Broad",
                ProviderType.GREASE_HAULER,
                "https://example.com/official-broad",
                "broad@example.com",
                "512-555-0101",
                List.of("austin-tx-kitchen-compliance", "charlotte-nc-kitchen-compliance", "tampa-fl-kitchen-compliance")
        );
        ProviderRecord publicContact = provider(
                "public-contact",
                "Public Contact",
                ProviderType.GREASE_HAULER,
                "",
                "public-only@example.com",
                "512-555-0102",
                List.of("austin-tx-kitchen-compliance")
        );
        ProviderRecord partialContact = provider(
                "partial-contact",
                "Partial Contact",
                ProviderType.GREASE_HAULER,
                "",
                " ",
                " ",
                List.of("austin-tx-kitchen-compliance", "charlotte-nc-kitchen-compliance", "tampa-fl-kitchen-compliance")
        );

        List<ProviderRecord> sorted = providerEvidenceService.sortByEvidenceQuality(
                List.of(publicContact, partialContact, officialBroad, officialFocused)
        );

        assertThat(sorted).extracting(ProviderRecord::providerName)
                .containsExactly(
                        "Official Focused",
                        "Official Broad",
                        "Public Contact",
                        "Partial Contact"
                );
    }

    @Test
    void evidenceLabelAndNotesDifferentiatePublicAndOfficialCoverage() {
        ProviderRecord officialPublic = provider(
                "official-public",
                "Official Public",
                ProviderType.HOOD_CLEANER,
                "https://example.com/official",
                "ops@example.com",
                "512-555-0199",
                List.of("austin-tx-kitchen-compliance")
        );
        ProviderRecord partialPublic = provider(
                "partial-public",
                "Partial Public",
                ProviderType.HOOD_CLEANER,
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
        assertThat(providerEvidenceService.evidenceLabel(partialPublic))
                .isEqualTo("Public contact only");
        assertThat(providerEvidenceService.providerNote(partialPublic))
                .contains("partial contact details");
        assertThat(providerEvidenceService.coverageConfidenceLabel(partialPublic))
                .isEqualTo("Needs operator verification");
        assertThat(providerEvidenceService.whyListed(partialPublic))
                .contains("partial contact details");
    }

    private ProviderRecord provider(
            String providerId,
            String providerName,
            ProviderType providerType,
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
                "https://example.com/" + providerId,
                email,
                phone,
                officialApprovalSourceUrl,
                "test note"
        );
    }
}
