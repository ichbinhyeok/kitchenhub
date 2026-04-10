package owner.kitchencompliance.model;

public record SitemapEntry(
        String location,
        String changeFrequency,
        String priority
) {
}
