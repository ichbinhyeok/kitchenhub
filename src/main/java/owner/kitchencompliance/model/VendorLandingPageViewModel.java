package owner.kitchencompliance.model;

import java.util.List;

public record VendorLandingPageViewModel(
        PageMeta meta,
        String eyebrow,
        String title,
        String summary,
        List<String> heroBullets,
        List<RelatedPageLink> actionLinks,
        List<GuideSection> sections,
        List<RelatedPageLink> wedgeLinks
) {
}
