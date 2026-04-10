package owner.kitchencompliance.model;

public record CallToAction(
        String title,
        String description,
        String label,
        String path,
        boolean sponsored
) {
}
