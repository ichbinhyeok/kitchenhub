package owner.kitchencompliance.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import owner.kitchencompliance.model.InspectionChecklistSection;
import owner.kitchencompliance.model.ProviderCard;
import owner.kitchencompliance.model.SourceAttribution;

public final class ViewFormatSupport {

    private ViewFormatSupport() {
    }

    public static String initials(String value) {
        if (value == null || value.isBlank()) {
            return "KC";
        }

        String[] parts = value.trim().split("\\s+");
        StringBuilder builder = new StringBuilder(2);
        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            builder.append(Character.toUpperCase(part.charAt(0)));
            if (builder.length() == 2) {
                break;
            }
        }

        if (builder.isEmpty()) {
            return "KC";
        }
        if (builder.length() == 1) {
            builder.append('H');
        }
        return builder.toString();
    }

    public static String frequencyLabel(String text, int index) {
        String normalized = text == null ? "" : text.toLowerCase(Locale.ROOT);
        if (normalized.contains("monthly")) {
            return "Monthly";
        }
        if (normalized.contains("quarter")) {
            return "Quarterly";
        }
        if (normalized.contains("semi")) {
            return "Semi-Annual";
        }
        if (normalized.contains("annual") || normalized.contains("year")) {
            return "Annual";
        }
        return switch (index) {
            case 0 -> "Monthly";
            case 1 -> "Quarterly";
            case 2 -> "Semi-Annual";
            default -> "As Directed";
        };
    }

    public static String frequencyContext(String text, int index) {
        String normalized = text == null ? "" : text.toLowerCase(Locale.ROOT);
        if (normalized.contains("solid fuel") || normalized.contains("wood") || normalized.contains("char")) {
            return "Solid fuel cooking";
        }
        if (normalized.contains("24") || normalized.contains("high-volume") || normalized.contains("high volume")) {
            return "High-volume operations";
        }
        if (normalized.contains("moderate")) {
            return "Moderate volume";
        }
        return switch (index) {
            case 0, 1 -> "High-volume operations";
            case 2 -> "Moderate volume";
            default -> "Authority schedule";
        };
    }

    public static List<SourceAttribution> topSources(List<SourceAttribution> sources, int limit) {
        if (sources == null || sources.isEmpty() || limit <= 0) {
            return List.of();
        }
        return sources.stream().limit(limit).toList();
    }

    public static List<InspectionChecklistSection> inspectionSections(List<String> records, List<String> failures) {
        Map<String, Bucket> buckets = new LinkedHashMap<>();
        buckets.put("suppression", new Bucket("Extinguishers & Suppression", "fire_extinguisher"));
        buckets.put("records", new Bucket("Records Binder", "folder_managed"));
        buckets.put("egress", new Bucket("Exit & Egress", "exit_to_app"));
        buckets.put("electrical", new Bucket("Electrical Safety", "bolt"));

        for (String record : records) {
            buckets.get(categoryFor(record)).requirements.add(record);
        }
        for (String failure : failures) {
            buckets.get(categoryFor(failure)).failures.add(failure);
        }

        List<InspectionChecklistSection> sections = new ArrayList<>();
        for (Bucket bucket : buckets.values()) {
            if (bucket.requirements.isEmpty() && bucket.failures.isEmpty()) {
                continue;
            }
            sections.add(new InspectionChecklistSection(
                    bucket.title,
                    bucket.iconName,
                    List.copyOf(bucket.requirements),
                    List.copyOf(bucket.failures)
            ));
        }
        return sections;
    }

    public static boolean hasOfficialEvidence(String url) {
        return url != null && !url.isBlank();
    }

    public static String firstOrFallback(List<String> values, String fallback) {
        if (values == null || values.isEmpty()) {
            return fallback;
        }
        String first = values.getFirst();
        if (first == null || first.isBlank()) {
            return fallback;
        }
        return first;
    }

    public static int authorityBackedProviderCount(List<ProviderCard> providers) {
        if (providers == null || providers.isEmpty()) {
            return 0;
        }
        return (int) providers.stream()
                .filter(provider -> hasOfficialEvidence(provider.officialApprovalSourceUrl()))
                .count();
    }

    public static int verificationRequiredProviderCount(List<ProviderCard> providers) {
        if (providers == null || providers.isEmpty()) {
            return 0;
        }
        return (int) providers.stream()
                .filter(provider -> !hasOfficialEvidence(provider.officialApprovalSourceUrl()))
                .count();
    }

    public static boolean isSponsoredProvider(ProviderCard provider) {
        if (provider == null) {
            return false;
        }
        String listingLabel = provider.listingLabel() == null ? "" : provider.listingLabel().toLowerCase(Locale.ROOT);
        String sponsorStatusLabel = provider.sponsorStatusLabel() == null
                ? ""
                : provider.sponsorStatusLabel().toLowerCase(Locale.ROOT);
        return listingLabel.contains("sponsor") || sponsorStatusLabel.contains("sponsor");
    }

    public static String providerOutboundRel(ProviderCard provider) {
        return isSponsoredProvider(provider)
                ? "sponsored nofollow noopener noreferrer"
                : "noopener noreferrer";
    }

    private static String categoryFor(String value) {
        String normalized = value == null ? "" : value.toLowerCase(Locale.ROOT);
        if (containsAny(normalized, "egress", "exit", "aisle", "path")) {
            return "egress";
        }
        if (containsAny(normalized, "electrical", "panel", "cord", "power", "adapter")) {
            return "electrical";
        }
        if (containsAny(normalized, "sticker", "tag", "binder", "certificate", "manifest", "documentation")) {
            return "records";
        }
        if (containsAny(normalized, "suppression", "hood-system", "hood system", "extinguisher", "fire protection", "pull station", "report")) {
            return "suppression";
        }
        return "records";
    }

    private static boolean containsAny(String value, String... parts) {
        for (String part : parts) {
            if (value.contains(part)) {
                return true;
            }
        }
        return false;
    }

    private static final class Bucket {
        private final String title;
        private final String iconName;
        private final List<String> requirements = new ArrayList<>();
        private final List<String> failures = new ArrayList<>();

        private Bucket(String title, String iconName) {
            this.title = title;
            this.iconName = iconName;
        }
    }
}
