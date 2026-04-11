package owner.kitchencompliance.model;

import java.util.List;

public record GuidePageViewModel(
        PageMeta meta,
        String title,
        String summary,
        List<GuideSection> sections,
        List<AuthorityRouteLink> authorityRoutes,
        List<RelatedPageLink> relatedLinks
) {
}
