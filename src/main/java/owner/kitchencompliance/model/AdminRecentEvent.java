package owner.kitchencompliance.model;

public record AdminRecentEvent(
        String capturedAt,
        String eventType,
        String location,
        String sourcePath,
        String destination,
        String pageFamily,
        String verdictState,
        String sponsoredLabel
) {
}
