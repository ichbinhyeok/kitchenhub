package owner.kitchencompliance.model;

public record AuthorityRouteLink(
        String title,
        String cityLabel,
        String authorityName,
        String authorityTypeLabel,
        String canonicalPath,
        String cityPath
) {
}
