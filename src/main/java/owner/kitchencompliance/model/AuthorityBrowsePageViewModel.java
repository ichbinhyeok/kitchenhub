package owner.kitchencompliance.model;

import java.util.List;

public record AuthorityBrowsePageViewModel(
        PageMeta meta,
        String eyebrow,
        String title,
        String summary,
        boolean detailView,
        List<AuthorityBrowseCard> authorityCards,
        List<AuthorityBrowseFilterOption> filterOptions,
        List<AuthorityBrowseStateJump> stateJumps,
        List<AuthorityBrowseSection> sections
) {
}
