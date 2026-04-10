package owner.kitchencompliance.model;

public record LeadCapturePanel(
        String panelId,
        String eyebrow,
        String title,
        String description,
        String submitPath,
        String submitLabel,
        String consentLabel,
        boolean sponsor
) {
}
