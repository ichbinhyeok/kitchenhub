package owner.kitchencompliance.model;

import java.util.List;

public record InfoPageViewModel(
        PageMeta meta,
        String eyebrow,
        String title,
        String summary,
        List<GuideSection> sections,
        List<RelatedPageLink> relatedLinks
) {
}
