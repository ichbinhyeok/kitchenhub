package owner.kitchencompliance.model;

import java.util.List;

public record AuthorityBrowseCard(
        String authorityName,
        String authorityTypeLabel,
        String cityLabel,
        String stateLabel,
        String detailPath,
        String baseUrl,
        String contactUrl,
        String lastVerified,
        String verificationStatusLabel,
        List<AuthorityBrowseRouteLink> routeLinks
) {
}
