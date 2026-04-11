package owner.kitchencompliance.model;

import java.util.List;

public record AuthorityBrowseSection(
        String anchorId,
        String stateLabel,
        int authorityCount,
        List<AuthorityBrowseCard> authorityCards
) {
}
