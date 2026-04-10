package owner.kitchencompliance.model;

import java.util.List;

public record OperatorToolPageViewModel(
        PageMeta meta,
        String slug,
        String eyebrow,
        String title,
        String summary,
        List<String> checklist,
        List<DownloadLink> downloads,
        List<RelatedPageLink> relatedLinks
) {

    public record DownloadLink(
            String label,
            String path,
            String note
    ) {
    }
}
