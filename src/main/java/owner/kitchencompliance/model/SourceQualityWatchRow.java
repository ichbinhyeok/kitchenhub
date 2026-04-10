package owner.kitchencompliance.model;

public record SourceQualityWatchRow(
        String cityLabel,
        String pageLabel,
        String path,
        String statusLabel,
        int totalSources,
        int strongSources,
        String note
) {
}
