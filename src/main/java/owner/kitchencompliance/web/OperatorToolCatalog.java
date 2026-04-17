package owner.kitchencompliance.web;

import java.util.List;
import org.springframework.stereotype.Component;
import owner.kitchencompliance.model.OperatorToolPageViewModel.DownloadLink;
import owner.kitchencompliance.model.RelatedPageLink;

@Component
public class OperatorToolCatalog {

    public ToolDefinition tool(String slug) {
        return allTools().stream()
                .filter(tool -> tool.slug().equals(slug))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown tool slug: " + slug));
    }

    public List<ToolDefinition> allTools() {
        return List.of(
                new ToolDefinition(
                        "grease-log",
                        "Grease log",
                        "Operator tool",
                        "Grease service log template",
                        "A worksheet for tracking pump-outs, manifests, and the binder location of proof before a utility or health inspection.",
                        List.of(
                                "Record every pump-out or maintenance action with the service date, vendor, gallons removed, and receipt or manifest reference.",
                                "Write down the authority that governs the site and the binder location where the current proof lives.",
                                "Reconcile the log against the latest haul ticket or manifest before the next inspection window."
                        ),
                        List.of(
                                new DownloadLink("Download grease log CSV", "/tools/grease-log.csv", "Binder-first worksheet for date, authority, vendor, gallons, manifest, receipt, and next-review tracking.")
                        ),
                        List.of(
                                new RelatedPageLink("Austin grease trap rules", "/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules"),
                                new RelatedPageLink("Charlotte grease trap rules", "/authority/nc/charlotte-water-flow-free/restaurant-grease-trap-rules"),
                                new RelatedPageLink("Miami grease trap rules", "/fl/miami/restaurant-grease-trap-rules")
                        )
                ),
                new ToolDefinition(
                        "hood-record-binder",
                        "Hood binder",
                        "Operator tool",
                        "Hood record binder checklist",
                        "A checklist for keeping hood cleaning, suppression, tags, and inspection-ready paperwork in one place without collapsing them into the same obligation.",
                        List.of(
                                "Separate hood cleaning reports from suppression inspection records when the authority treats them differently.",
                                "Keep the most recent tag, certificate, and service report in the same binder or folder with the authority name written on the tab.",
                                "Use the checklist before the next fire inspection so the proof burden is explicit and the missing item is obvious."
                        ),
                        List.of(
                                new DownloadLink("Download hood binder CSV", "/tools/hood-record-binder.csv", "Checklist for report, tag, certificate, binder location, and deficiency follow-up.")
                        ),
                        List.of(
                                new RelatedPageLink("Austin hood requirements", "/authority/tx/austin-fire-marshal/hood-cleaning-requirements"),
                                new RelatedPageLink("Charlotte hood requirements", "/authority/nc/charlotte-fire-prevention/hood-cleaning-requirements"),
                                new RelatedPageLink("Miami hood requirements", "/fl/miami/hood-cleaning-requirements")
                        )
                ),
                new ToolDefinition(
                        "missing-proof-tracker",
                        "Missing proof tracker",
                        "Operator tool",
                        "Missing proof tracker",
                        "A worksheet for logging what proof is missing, which route or authority created the requirement, who owns the follow-up, and when the gap must be closed before inspection pressure hits.",
                        List.of(
                                "Write down the missing document, tag, manifest, or report the moment the gap is discovered.",
                                "Tie the gap to the source route or authority page so staff can reopen the right local rule without guessing.",
                                "Assign an owner and next review date so the proof gap closes before the next inspection or service visit."
                        ),
                        List.of(
                                new DownloadLink("Download missing proof tracker CSV", "/tools/missing-proof-tracker.csv", "Queue for missing proof item, source route, owner, next review date, and closure status.")
                        ),
                        List.of(
                                new RelatedPageLink("Austin grease trap rules", "/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules"),
                                new RelatedPageLink("Charlotte hood requirements", "/authority/nc/charlotte-fire-prevention/hood-cleaning-requirements"),
                                new RelatedPageLink("Miami fire inspection checklist", "/authority/fl/miami-dade-fire-rescue/restaurant-fire-inspection-checklist")
                        )
                ),
                new ToolDefinition(
                        "inspection-reminder-plan",
                        "Inspection reminders",
                        "Operator tool",
                        "Inspection reminder plan",
                        "A reminder worksheet for mapping due dates, missing proof, and service follow-up before an inspection window closes.",
                        List.of(
                                "Capture the next inspection window, the responsible authority, and the missing proof item.",
                                "Assign the repair or paperwork follow-up before the reminder becomes a last-minute scramble.",
                                "Use this as an operator queue, not as a substitute for the local rule page."
                        ),
                        List.of(
                                new DownloadLink("Download reminder plan CSV", "/tools/inspection-reminder-plan.csv", "Reminder queue for authority, due date, issue type, source route, and action owner.")
                        ),
                        List.of(
                                new RelatedPageLink("Austin fire inspection checklist", "/authority/tx/austin-fire-marshal/restaurant-fire-inspection-checklist"),
                                new RelatedPageLink("Charlotte fire inspection checklist", "/authority/nc/charlotte-fire-prevention/restaurant-fire-inspection-checklist"),
                                new RelatedPageLink("Miami fire inspection checklist", "/authority/fl/miami-dade-fire-rescue/restaurant-fire-inspection-checklist")
                        )
                )
        );
    }

    public record ToolDefinition(
            String slug,
            String navLabel,
            String eyebrow,
            String title,
            String summary,
            List<String> checklist,
            List<DownloadLink> downloads,
            List<RelatedPageLink> relatedLinks
    ) {
    }
}
