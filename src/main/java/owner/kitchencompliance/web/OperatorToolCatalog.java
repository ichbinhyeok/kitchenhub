package owner.kitchencompliance.web;

import java.util.List;
import org.springframework.stereotype.Component;
import owner.kitchencompliance.model.OperatorToolPageViewModel.DownloadLink;
import owner.kitchencompliance.model.OperatorToolPageViewModel.SampleReportPreview;
import owner.kitchencompliance.model.OperatorToolPageViewModel.VendorSetupPanel;
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
                        "hood-service-report",
                        "Hood service report",
                        "Vendor workflow",
                        "Hood service report",
                        "Fill today's hood job, send a customer-ready closeout, and keep proof plus follow-up in one factual handoff.",
                        List.of(
                                "Fill this out right after the crew finishes while the notes and proof are still easy to pull together.",
                                "Keep the wording factual: say what was cleaned, what proof is attached, and what still needs follow-up.",
                                "Send the report first, then add the city rule page separately only if the customer needs records context."
                        ),
                        List.of(
                                new DownloadLink("Download hood service report CSV", "/tools/hood-service-report.csv", "Blank closeout worksheet for job header, work completed, proof attached, follow-up items, and next service date."),
                                new DownloadLink("Download handoff email TXT", "/tools/hood-service-report.txt", "Blank customer handoff email template.")
                        ),
                        List.of(
                                new RelatedPageLink("Austin hood requirements", "/authority/tx/austin-fire-marshal/hood-cleaning-requirements"),
                                new RelatedPageLink("Charlotte hood requirements", "/authority/nc/charlotte-fire-prevention/hood-cleaning-requirements"),
                                new RelatedPageLink("Miami hood requirements", "/fl/miami/hood-cleaning-requirements")
                        ),
                        List.of(
                                "Job header: customer, site, service date, vendor, crew, and work-order reference.",
                                "Work completed: hood, filters, duct, fan, and any access limits or incomplete areas.",
                                "Proof attached: before and after photo reference, report or invoice reference, and any tag or certificate notes.",
                                "Follow-up items: deferred work, deficiency notes, and who owns the next step.",
                                "Next service window: recommended next date and the rebooking line you want the customer to see.",
                                "Customer handoff: who received the report and where the records should live on site."
                        ),
                        List.of(
                                "Use phrases like \"service performed\" and \"photos attached\" instead of \"certified compliant\" or \"passed inspection.\"",
                                "Keep hood cleaning records separate from suppression inspection records when the site treats them as different proof.",
                                "Put the local rule link in the email or as a separate reference line, not as an official claim inside the report."
                        ),
                        List.of(
                                "The hood service report itself",
                                "The before and after photo set or photo reference",
                                "Any tag, invoice, or internal report reference the customer may file with it",
                                "The matching local rule page only if the customer wants authority context, labeled as reference material"
                        ),
                        "Hood service report | [Restaurant Name] | [Service Date]",
                        List.of(
                                "Hi [Customer Name],",
                                "Attached is today's hood service report for [Site Name].",
                                "It shows what was cleaned, what proof is attached, and any follow-up item we flagged on site.",
                                "If you need the local rule page for your records, attach that separately as a reference link.",
                                "Next recommended service date: [Date].",
                                "Thanks,",
                                "[Vendor Name]"
                        ),
                        List.of(
                                "Right after the crew finishes, when photos and job notes are still easy to pull together.",
                                "When the office sends the invoice or wrap-up email and the customer expects proof in the same thread.",
                                "When a repeat customer asks for the latest hood paperwork before the next inspection or internal review."
                        ),
                        List.of(
                                "A clear record of what was cleaned on this visit.",
                                "A simple list of what proof should be filed with the job records.",
                                "Any deferred item or deficiency that still needs attention.",
                                "The next recommended service date without sounding like a compliance guarantee."
                        ),
                        List.of(
                                "It fits an existing office habit instead of asking the vendor to learn a new system.",
                                "Customers can forward it to the GM, owner, or records binder without rewriting anything.",
                                "The same report can become the default closeout for recurring hood jobs once the wording feels right.",
                                "When the customer needs rule context, the attached city rule link can pull them back into the local authority page without weakening the vendor handoff."
                        ),
                        new SampleReportPreview(
                                "Customer-ready hood closeout",
                                "This is the one-page handoff the office sends after the job, without turning it into a formal certification packet.",
                                List.of(
                                        "Customer: Example Bistro",
                                        "Site: 101 Trade St, Charlotte, NC",
                                        "Service date: April 12, 2026",
                                        "Vendor: Example Hood Service | Crew A",
                                        "Work order: WO-2048"
                                ),
                                List.of(
                                        "Cleaned hood canopy, accessible duct run, filters, and rooftop fan.",
                                        "Removed grease buildup from accessible surfaces and reinstalled filters.",
                                        "Documented before and after condition with photo set 12."
                                ),
                                List.of(
                                        "Before and after photo reference: before-after-set-12",
                                        "Internal report reference: INV-2048",
                                        "Customer handoff note: sent to GM with invoice"
                                ),
                                List.of(
                                        "Customer-facing service report: this page or PDF printout",
                                        "Before and after photo set: before-after-set-12",
                                        "Invoice or internal report reference: INV-2048",
                                        "Recipient note: sent to GM with invoice"
                                ),
                                List.of(
                                        "Replace missing access panel before the next inspection window.",
                                        "Keep suppression paperwork filed separately from this hood cleaning record."
                                ),
                                "Next recommended service date: July 12, 2026",
                                "For your records: this report documents service performed and attached proof. It is not a fire inspection or code determination."
                        ),
                        List.of(
                                "Keep the hood service report as the primary attachment and the vendor handoff at the center.",
                                "Attach only one city-specific hood rule page when the customer wants records or inspection context.",
                                "Label the rule link as reference material so it does not read like an official approval or guarantee."
                        ),
                        "Need the local hood rule page for your records? Reference link: [City-specific hood requirements URL]",
                        null
                ),
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
                        ),
                        List.of(),
                        List.of(),
                        List.of(),
                        null,
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(),
                        null,
                        List.of(),
                        null,
                        null
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
                        ),
                        List.of(),
                        List.of(),
                        List.of(),
                        null,
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(),
                        null,
                        List.of(),
                        null,
                        null
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
                        ),
                        List.of(),
                        List.of(),
                        List.of(),
                        null,
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(),
                        null,
                        List.of(),
                        null,
                        null
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
                        ),
                        List.of(),
                        List.of(),
                        List.of(),
                        null,
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(),
                        null,
                        List.of(),
                        null,
                        null
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
            List<RelatedPageLink> relatedLinks,
            List<String> sendSections,
            List<String> languageGuardrails,
            List<String> deliveryChecklist,
            String emailSubject,
            List<String> emailBodyLines,
            List<String> vendorWorkflowMoments,
            List<String> customerOutcomes,
            List<String> repeatReasons,
            SampleReportPreview sampleReportPreview,
            List<String> referenceAddOnNotes,
            String referenceSnippet,
            VendorSetupPanel vendorSetupPanel
    ) {
    }
}
