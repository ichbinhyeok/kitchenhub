package owner.kitchencompliance.model;

import java.util.List;

public record InspectionChecklistSection(
        String title,
        String iconName,
        List<String> requirements,
        List<String> failures
) {
}
