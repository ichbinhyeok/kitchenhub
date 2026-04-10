package owner.kitchencompliance.model;

public record DeployReadinessWatchRow(
        String cityLabel,
        String pageLabel,
        String path,
        String statusLabel,
        String indexableLabel,
        String nextReviewOn,
        int totalSources,
        int strongSources,
        int renderableProviders,
        int authorityBackedProviders,
        String note
) {
}
