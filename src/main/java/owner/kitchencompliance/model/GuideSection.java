package owner.kitchencompliance.model;

import java.util.List;

public record GuideSection(
        String heading,
        List<String> bullets
) {
}
