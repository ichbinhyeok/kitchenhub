package owner.kitchencompliance.model;

public record FreshnessWatchRow(
        String cityLabel,
        String pageLabel,
        String path,
        String statusLabel,
        String nextReviewOn,
        String note
) {
}
