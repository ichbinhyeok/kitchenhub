package owner.kitchencompliance.rules;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.ProviderRecord;

@Service
public class ProviderEvidenceService {

    public List<ProviderRecord> sortByEvidenceQuality(List<ProviderRecord> providers) {
        return providers.stream()
                .sorted(Comparator
                        .comparingInt(this::rankingScore).reversed()
                        .thenComparing(ProviderRecord::providerName))
                .toList();
    }

    public String evidenceLabel(ProviderRecord provider) {
        if (hasOfficialEvidence(provider)) {
            return hasCompleteContact(provider)
                    ? "Official source + public contact"
                    : "Official source + limited contact";
        }
        return hasCompleteContact(provider)
                ? "Public contact"
                : "Public contact only";
    }

    public String providerNote(ProviderRecord provider) {
        if (hasOfficialEvidence(provider)) {
            return hasCompleteContact(provider)
                    ? "Ranked first when an official source link is paired with complete public contact details."
                    : "Official source is present, but the contact details are still incomplete, so the operator should verify local coverage before booking.";
        }
        return hasCompleteContact(provider)
                ? "Shown from a public service page with complete contact details, but the operator should still verify current local coverage."
                : "Shown from a public service page with partial contact details, so the operator should verify current local coverage before booking.";
    }

    public String coverageConfidenceLabel(ProviderRecord provider) {
        if (hasOfficialEvidence(provider) && provider.coverageTargets().size() <= 2) {
            return "High";
        }
        if (hasOfficialEvidence(provider) || hasCompleteContact(provider)) {
            return "Medium";
        }
        return "Needs operator verification";
    }

    public String whyListed(ProviderRecord provider) {
        if (hasOfficialEvidence(provider)) {
            return hasCompleteContact(provider)
                    ? "Official source link plus complete public contact details."
                    : "Official source link plus partial public contact details that still need a direct coverage check.";
        }
        return hasCompleteContact(provider)
                ? "Public listing with direct contact details and declared local coverage."
                : "Public listing with partial contact details that still needs a direct coverage check.";
    }

    private int rankingScore(ProviderRecord provider) {
        int score = 0;
        if (hasOfficialEvidence(provider)) {
            score += 10_000;
        }
        score += contactQualityScore(provider) * 120;
        score += scopeFocusScore(provider) * 25;
        return score;
    }

    private int contactQualityScore(ProviderRecord provider) {
        int score = 0;
        if (!isBlank(provider.email())) {
            score += 1;
        }
        if (!isBlank(provider.phone())) {
            score += 1;
        }
        return score;
    }

    private int scopeFocusScore(ProviderRecord provider) {
        int coverageTargets = provider.coverageTargets().size();
        if (coverageTargets <= 1) {
            return 4;
        }
        if (coverageTargets == 2) {
            return 3;
        }
        if (coverageTargets == 3) {
            return 2;
        }
        if (coverageTargets == 4) {
            return 1;
        }
        return 0;
    }

    private boolean hasOfficialEvidence(ProviderRecord provider) {
        return !isBlank(provider.officialApprovalSourceUrl());
    }

    private boolean hasCompleteContact(ProviderRecord provider) {
        return !isBlank(provider.email()) && !isBlank(provider.phone());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
