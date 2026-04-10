package owner.kitchencompliance.rules;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.ListingMode;
import owner.kitchencompliance.data.ProviderRecord;

@Service
public class ProviderEvidenceService {

    public List<ProviderRecord> sortByEvidenceQuality(List<ProviderRecord> providers) {
        return providers.stream()
                .sorted(Comparator
                        .comparingInt(this::evidenceScore).reversed()
                        .thenComparing(ProviderRecord::providerName))
                .toList();
    }

    public String evidenceLabel(ProviderRecord provider) {
        if (hasOfficialEvidence(provider)) {
            return "Authority list + public contact";
        }
        if (provider.listingMode() == ListingMode.PUBLIC) {
            return "Public contact only";
        }
        return "Sponsor contact only";
    }

    public String providerNote(ProviderRecord provider) {
        if (hasOfficialEvidence(provider)) {
            return "Ranked first when an authority, preferred-pumper, or official approval source is cited alongside a public service contact.";
        }
        if (provider.listingMode() == ListingMode.PUBLIC) {
            return "Shown from a public service page with direct contact details, but the operator should still verify current local coverage.";
        }
        return "Shown as a sponsor placement with direct contact details; operator verification still applies.";
    }

    private int evidenceScore(ProviderRecord provider) {
        int score = 0;
        if (hasOfficialEvidence(provider)) {
            score += 4;
        }
        if (provider.listingMode() == ListingMode.PUBLIC) {
            score += 2;
        }
        if (!provider.email().isBlank()) {
            score += 1;
        }
        if (!provider.phone().isBlank()) {
            score += 1;
        }
        return score;
    }

    private boolean hasOfficialEvidence(ProviderRecord provider) {
        return provider.officialApprovalSourceUrl() != null && !provider.officialApprovalSourceUrl().isBlank();
    }
}
