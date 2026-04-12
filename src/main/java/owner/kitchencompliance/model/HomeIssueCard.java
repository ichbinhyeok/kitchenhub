package owner.kitchencompliance.model;

public record HomeIssueCard(
        String title,
        String summary,
        String destinationLabel,
        String destinationPath,
        String supportLabel,
        String supportPath,
        String iconName
) {
}
