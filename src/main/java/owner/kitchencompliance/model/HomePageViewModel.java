package owner.kitchencompliance.model;

import java.util.List;

public record HomePageViewModel(
        PageMeta meta,
        String title,
        String summary,
        List<String> scopeBullets,
        List<CityCard> cityCards,
        List<HomePanelLink> guideLinks,
        List<HomePanelLink> operatorToolLinks
) {
}
