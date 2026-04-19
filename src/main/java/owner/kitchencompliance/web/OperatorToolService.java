package owner.kitchencompliance.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.Normalizer;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import owner.kitchencompliance.data.IssueType;
import owner.kitchencompliance.data.PageFamily;
import owner.kitchencompliance.model.HomePanelLink;
import owner.kitchencompliance.model.OperatorToolPageViewModel;
import owner.kitchencompliance.model.OperatorToolPageViewModel.AttachmentBundleItem;
import owner.kitchencompliance.model.OperatorToolPageViewModel.ActiveReferenceLink;
import owner.kitchencompliance.model.OperatorToolPageViewModel.CityOption;
import owner.kitchencompliance.model.OperatorToolPageViewModel.DownloadLink;
import owner.kitchencompliance.model.OperatorToolPageViewModel.HoodAttachmentBundle;
import owner.kitchencompliance.model.OperatorToolPageViewModel.HoodServiceReportForm;
import owner.kitchencompliance.model.OperatorToolPageViewModel.HoodPacketSummary;
import owner.kitchencompliance.model.OperatorToolPageViewModel.PacketItem;
import owner.kitchencompliance.model.OperatorToolPageViewModel.SampleReportPreview;
import owner.kitchencompliance.model.OperatorToolPageViewModel.SendReadinessPanel;
import owner.kitchencompliance.model.PageMeta;
import owner.kitchencompliance.model.RelatedPageLink;

@Service
public class OperatorToolService {

    private static final DateTimeFormatter HUMAN_DATE = DateTimeFormatter.ofPattern("MMMM d, uuuu", Locale.ENGLISH);
    private static final List<HoodCityProfile> HOOD_CITY_PROFILES = List.of(
            new HoodCityProfile(
                    "austin",
                    "Austin",
                    "tx",
                    "austin-fire-marshal",
                    "Austin Fire Marshal",
                    "/authority/tx/austin-fire-marshal/hood-cleaning-requirements",
                    "120 Congress Ave, Austin, TX"
            ),
            new HoodCityProfile(
                    "charlotte",
                    "Charlotte",
                    "nc",
                    "charlotte-fire-prevention",
                    "Charlotte Fire Prevention",
                    "/authority/nc/charlotte-fire-prevention/hood-cleaning-requirements",
                    "101 Trade St, Charlotte, NC"
            ),
            new HoodCityProfile(
                    "miami",
                    "Miami",
                    "fl",
                    "miami-dade-building-mechanical",
                    "Miami-Dade Building Mechanical",
                    "/fl/miami/hood-cleaning-requirements",
                    "225 NE 2nd St, Miami, FL"
            )
    );

    private final SiteProperties siteProperties;
    private final OperatorToolCatalog operatorToolCatalog;

    public OperatorToolService(SiteProperties siteProperties, OperatorToolCatalog operatorToolCatalog) {
        this.siteProperties = siteProperties;
        this.operatorToolCatalog = operatorToolCatalog;
    }

    public List<RelatedPageLink> toolLinks() {
        return operatorToolCatalog.allTools().stream()
                .map(tool -> new RelatedPageLink(tool.title(), "/tools/" + tool.slug()))
                .toList();
    }

    public List<HomePanelLink> homeToolLinks() {
        return operatorToolCatalog.allTools().stream()
                .map(tool -> new HomePanelLink(
                        tool.title(),
                        tool.summary(),
                        "/tools/" + tool.slug(),
                        switch (tool.slug()) {
                            case "hood-service-report" -> "description";
                            case "grease-log" -> "download";
                            case "hood-record-binder" -> "upload_file";
                            case "missing-proof-tracker" -> "rule";
                            case "inspection-reminder-plan" -> "calculate";
                            default -> "construction";
                        }
                ))
                .toList();
    }

    public void requireKnownTool(String slug) {
        operatorToolCatalog.tool(slug);
    }

    public OperatorToolPageViewModel toolPage(String slug) {
        return toolPage(slug, new LinkedMultiValueMap<>());
    }

    public OperatorToolPageViewModel toolPage(String slug, MultiValueMap<String, String> params) {
        OperatorToolCatalog.ToolDefinition tool = operatorToolCatalog.tool(slug);
        PageMeta meta = new PageMeta(
                tool.title() + " | " + siteProperties.title(),
                tool.summary(),
                canonicalUrl("/tools/" + tool.slug()),
                "noindex,follow",
                LocalDate.now(),
                null
        );

        if (!"hood-service-report".equals(slug)) {
            return new OperatorToolPageViewModel(
                    meta,
                    tool.slug(),
                    tool.eyebrow(),
                    tool.title(),
                    tool.summary(),
                    tool.checklist(),
                    tool.downloads(),
                    tool.relatedLinks(),
                    tool.sendSections(),
                    tool.languageGuardrails(),
                    tool.deliveryChecklist(),
                    tool.emailSubject(),
                    tool.emailBodyLines(),
                    tool.vendorWorkflowMoments(),
                    tool.customerOutcomes(),
                    tool.repeatReasons(),
                    tool.sampleReportPreview(),
                    tool.referenceAddOnNotes(),
                    tool.referenceSnippet(),
                    tool.vendorSetupPanel(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        HoodServiceReportDraft draft = hoodServiceReportDraft(params);
        return new OperatorToolPageViewModel(
                meta,
                tool.slug(),
                tool.eyebrow(),
                tool.title(),
                tool.summary(),
                tool.checklist(),
                tool.downloads(),
                tool.relatedLinks(),
                tool.sendSections(),
                tool.languageGuardrails(),
                tool.deliveryChecklist(),
                hoodServiceReportEmailSubject(draft),
                hoodServiceReportEmailBody(draft),
                tool.vendorWorkflowMoments(),
                tool.customerOutcomes(),
                tool.repeatReasons(),
                hoodServiceReportPreview(draft),
                tool.referenceAddOnNotes(),
                hoodServiceReportReferenceSnippet(draft),
                tool.vendorSetupPanel(),
                hoodServiceReportForm(draft),
                activeReferenceLink(draft),
                hoodServiceSendReadiness(draft),
                hoodPacketSummary(draft),
                hoodAttachmentBundle(draft),
                hoodServiceReportEmailDraftMailto(draft)
        );
    }

    public String csvTemplate(String slug) {
        return csvTemplate(slug, new LinkedMultiValueMap<>());
    }

    public String csvTemplate(String slug, MultiValueMap<String, String> params) {
        if ("hood-service-report".equals(slug)) {
            HoodServiceReportDraft draft = hoodServiceReportDraft(params);
            return """
                    service_date,city,authority,customer_name,location_name,recipient_name,recipient_email,site_address,vendor_name,crew_or_technician,systems_serviced,photo_reference,report_attachment_ready,photo_set_attached,report_file_attached,reference_link_added,follow_up_item,next_service_date,customer_handoff_note
                    %s
                    """.formatted(String.join(",",
                    csvEscape(draft.serviceDate()),
                    csvEscape(draft.cityLabel()),
                    csvEscape(draft.authorityLabel()),
                    csvEscape(draft.customerName()),
                    csvEscape(draft.siteName()),
                    csvEscape(draft.recipientName()),
                    csvEscape(draft.recipientEmail()),
                    csvEscape(draft.siteAddress()),
                    csvEscape(draft.vendorName()),
                    csvEscape(draft.crewOrTechnician()),
                    csvEscape(draft.systemsServiced()),
                    csvEscape(draft.photoReference()),
                    csvEscape(Boolean.toString(draft.reportAttachmentReady())),
                    csvEscape(Boolean.toString(draft.photoSetAttached())),
                    csvEscape(Boolean.toString(draft.reportFileAttached())),
                    csvEscape(Boolean.toString(draft.referenceLinkAdded())),
                    csvEscape(joinWithSeparator(draft.followUpItems(), " | ") + " | Owner: " + draft.followUpOwner() + " | Target date: " + displayDate(draft.followUpDueDate())),
                    csvEscape(draft.nextServiceDate()),
                    csvEscape(draft.customerHandoffNote() + (draft.includeReferenceLink() ? "; local rule link attached separately" : ""))
            ));
        }

        return switch (slug) {
            case "grease-log" -> """
                    service_date,city,authority,vendor_name,service_type,gallons_removed,manifest_reference,receipt_reference,binder_location,next_review_on,notes
                    2026-04-07,Austin,Austin Water,Example Hauler,pump_out,240,MAN-1001,INV-1001,FOG binder shelf A,2026-04-21,"Pulled before inspection week"
                    """;
            case "hood-record-binder" -> """
                    document_type,city,authority,last_service_date,location_in_binder,next_due_date,proof_status,follow_up
                    hood_cleaning_report,Austin,Austin Fire Department,2026-04-07,Section A,2026-07-07,complete,Confirm next cleaning
                    suppression_tag,Austin,Austin Fire Department,2026-04-07,Section B,2027-04-07,visible,Keep visible on site
                    """;
            case "missing-proof-tracker" -> """
                    detected_on,city,authority,issue_type,missing_proof,source_route,owner,next_review_on,status,closure_note
                    2026-04-10,Austin,Austin Water,manifest_or_log,Current pump-out manifest,/tx/austin/approved-grease-haulers,Kitchen manager,2026-04-14,open,"Confirm hauler receipt and file in binder"
                    """;
            case "inspection-reminder-plan" -> """
                    reminder_date,city,authority,issue_type,source_route,missing_proof,next_action,owner,status
                    2026-04-21,Austin,Austin Fire Department,inspection_prep,/tx/austin/restaurant-fire-inspection-checklist,Current hood report,Book service and file report,Kitchen manager,open
                    """;
            default -> throw new IllegalArgumentException("Unknown tool slug: " + slug);
        };
    }

    public String textTemplate(String slug) {
        return textTemplate(slug, new LinkedMultiValueMap<>());
    }

    public String textTemplate(String slug, MultiValueMap<String, String> params) {
        if ("hood-service-report".equals(slug)) {
            HoodServiceReportDraft draft = hoodServiceReportDraft(params);
            return String.join("\n", buildTextTemplateLines(draft)) + "\n";
        }

        throw new IllegalArgumentException("Unknown text template slug: " + slug);
    }

    public PageFamily pageFamily() {
        return PageFamily.OPERATOR_TOOL;
    }

    public IssueType issueTypeFor(String slug) {
        return switch (slug) {
            case "hood-service-report" -> IssueType.HOOD_CLEANING;
            case "grease-log" -> IssueType.MANIFEST_OR_LOG;
            case "hood-record-binder" -> IssueType.HOOD_CLEANING;
            case "missing-proof-tracker" -> IssueType.OPERATOR_UTILITY;
            case "inspection-reminder-plan" -> IssueType.INSPECTION_PREP;
            default -> IssueType.OPERATOR_UTILITY;
        };
    }

    public ToolAttributionContext toolAttributionContext(String slug, String cityKey) {
        if (!"hood-service-report".equals(slug)) {
            return new ToolAttributionContext("", "", "");
        }
        HoodCityProfile profile = findHoodCityProfile(cityKey).orElse(HOOD_CITY_PROFILES.get(1));
        return new ToolAttributionContext(profile.key(), profile.state(), profile.authorityId());
    }

    private String canonicalUrl(String path) {
        if (path.equals("/")) {
            return siteProperties.baseUrl();
        }
        return siteProperties.baseUrl() + path;
    }

    private SampleReportPreview hoodServiceReportPreview(HoodServiceReportDraft draft) {
        return new SampleReportPreview(
                "Draft hood service report",
                "Built from the vendor-side fields below so the office can send a clean closeout without rewriting the customer email.",
                List.of(
                        "Restaurant account: " + draft.customerName(),
                        "Location name: " + draft.siteName(),
                        "Site: " + draft.siteAddress(),
                        "Service date: " + displayDate(draft.serviceDate()),
                        "Vendor: " + draft.vendorName() + " | " + draft.crewOrTechnician(),
                        "Work order: " + draft.workOrderReference(),
                        "Systems serviced: " + draft.systemsServiced()
                ),
                splitLines(draft.completedWork()),
                buildAttachedProofLines(draft),
                buildProofPackItems(draft),
                buildFollowUpItems(draft),
                "Next recommended service date: " + displayDate(draft.nextServiceDate()),
                "For your records: this report documents service performed and attached proof. It is not a fire inspection or code determination."
        );
    }

    private String hoodServiceReportEmailSubject(HoodServiceReportDraft draft) {
        return "Hood service report | " + emailReportLabel(draft) + " | " + displayDate(draft.serviceDate());
    }

    private List<String> hoodServiceReportEmailBody(HoodServiceReportDraft draft) {
        List<String> lines = new ArrayList<>();
        lines.add(greetingLine(draft.recipientName()));
        lines.add("Attached is today's hood service report for " + emailReportLabel(draft) + ".");
        lines.add("It shows what was cleaned, what proof is attached, and the follow-up items we flagged on site.");
        if (!draft.systemsServiced().isBlank()) {
            lines.add("Systems serviced: " + draft.systemsServiced() + ".");
        }
        if (draft.followUpItems().isEmpty()) {
            lines.add("No follow-up items were noted on site.");
        } else {
            lines.add("Follow-up items: " + joinWithSeparator(draft.followUpItems(), "; ") + ".");
            if (!draft.followUpOwner().isBlank() || !draft.followUpDueDate().isBlank()) {
                lines.add("Follow-up owner: " + blankOrFallback(draft.followUpOwner(), "TBD")
                        + " | Target date: "
                        + blankOrFallback(displayDate(draft.followUpDueDate()), "TBD") + ".");
            }
        }
        if (draft.includeReferenceLink()) {
            lines.add("Local rule reference for records only: " + draft.referenceUrl());
        }
        lines.add("Next recommended service date: " + displayDate(draft.nextServiceDate()) + ".");
        lines.add("Thanks,");
        lines.add(draft.vendorName());
        return lines;
    }

    private List<String> buildTextTemplateLines(HoodServiceReportDraft draft) {
        List<String> lines = new ArrayList<>();
        if (!draft.recipientEmail().isBlank()) {
            lines.add("To: " + draft.recipientEmail());
        }
        lines.add("Subject: " + hoodServiceReportEmailSubject(draft));
        lines.add("");
        lines.addAll(hoodServiceReportEmailBody(draft));
        return lines;
    }

    private String hoodServiceReportReferenceSnippet(HoodServiceReportDraft draft) {
        return "Need the local hood rule page for your records? Reference link for " + draft.cityLabel() + ": " + draft.referenceUrl();
    }

    private String reportLabel(HoodServiceReportDraft draft) {
        return draft.siteName() == null || draft.siteName().isBlank()
                ? draft.customerName()
                : draft.siteName();
    }

    private String emailReportLabel(HoodServiceReportDraft draft) {
        String label = reportLabel(draft);
        if (label != null && !label.isBlank()) {
            return label;
        }
        return draft.cityLabel() + " location";
    }

    private HoodServiceReportForm hoodServiceReportForm(HoodServiceReportDraft draft) {
        return new HoodServiceReportForm(
                "/tools/hood-service-report",
                HOOD_CITY_PROFILES.stream()
                        .map(profile -> new CityOption(profile.key(), profile.label()))
                        .toList(),
                draft.cityKey(),
                draft.serviceDate(),
                draft.nextServiceDate(),
                draft.customerName(),
                draft.siteName(),
                draft.recipientName(),
                draft.recipientEmail(),
                draft.siteAddress(),
                draft.vendorName(),
                draft.crewOrTechnician(),
                draft.workOrderReference(),
                draft.systemsServiced(),
                draft.completedWork(),
                draft.photoReference(),
                draft.reportReference(),
                draft.customerHandoffNote(),
                String.join("\n", draft.followUpItems()),
                draft.followUpOwner(),
                draft.followUpDueDate(),
                draft.reportAttachmentReady(),
                draft.photoSetAttached(),
                draft.reportFileAttached(),
                draft.referenceLinkAdded(),
                draft.includeReferenceLink()
        );
    }

    private ActiveReferenceLink activeReferenceLink(HoodServiceReportDraft draft) {
        return new ActiveReferenceLink(
                draft.cityLabel() + " hood requirements",
                draft.referencePath(),
                draft.referenceUrl(),
                draft.authorityLabel() + " route for the customer record trail."
        );
    }

    private String hoodServiceReportEmailDraftMailto(HoodServiceReportDraft draft) {
        String recipient = draft.recipientEmail().isBlank() ? "" : draft.recipientEmail().trim();
        return "mailto:" + recipient
                + "?subject=" + urlEncode(hoodServiceReportEmailSubject(draft))
                + "&body=" + urlEncode(String.join("\n", hoodServiceReportEmailBody(draft)));
    }

    private SendReadinessPanel hoodServiceSendReadiness(HoodServiceReportDraft draft) {
        if (draft.sampleDraft()) {
            return new SendReadinessPanel(
                    false,
                    "Replace sample values before send",
                    "This page opens with example data so the office can see the handoff shape before using it on a real job.",
                    List.of(
                            "Replace the restaurant account and location name.",
                            "Replace the vendor, photo, and report references with the real job details.",
                            "Add the real recipient or keep the copy in your existing send thread."
                    )
            );
        }

        List<String> items = new ArrayList<>();
        if (draft.customerName().isBlank()) {
            items.add("Add the restaurant account name.");
        }
        if (draft.vendorName().isBlank()) {
            items.add("Add the vendor or office name.");
        }
        if (draft.completedWork().isBlank()) {
            items.add("Add the work completed summary.");
        }
        if (!hasPhotoProof(draft)) {
            items.add("Add the photo reference or confirm the photo set is attached.");
        }
        if (!hasReportProof(draft)) {
            items.add("Add the invoice or internal report reference.");
        }
        if (draft.siteAddress().isBlank()) {
            items.add("Add the site address for the customer record trail.");
        }
        if (!draft.reportAttachmentReady()) {
            items.add("Export or print the customer-facing report before send.");
        }
        if (!draft.photoReference().isBlank() && !draft.photoSetAttached()) {
            items.add("Attach the photo set referenced in the report.");
        }
        if (!draft.reportReference().isBlank() && !draft.reportFileAttached()) {
            items.add("Attach the invoice or internal report file tied to the report reference.");
        }
        if (draft.includeReferenceLink() && !draft.referenceLinkAdded()) {
            items.add("Add the city rule link separately before send.");
        }

        if (!items.isEmpty()) {
            return new SendReadinessPanel(
                false,
                "Before you send this",
                    "Finish the missing handoff details so the report can go out without a second cleanup pass.",
                    items
            );
        }

        List<String> readyItems = new ArrayList<>();
        readyItems.add("Restaurant account, location, and vendor details are present.");
        readyItems.add("Photo and report proof are covered by the attached files or a named reference.");
        readyItems.add("Attachment checklist is complete.");
        if (draft.recipientEmail().isBlank()) {
            readyItems.add("Recipient email is optional here if your office sends from an existing thread.");
        } else {
            readyItems.add("Recipient email is set for the email-draft action.");
        }
        readyItems.add("Use the email draft, TXT export, or PDF printout for the actual send.");
        return new SendReadinessPanel(
                true,
                "Ready to send",
                "The draft has the core details a small vendor office usually needs before it goes to the customer.",
                readyItems
        );
    }

    private HoodPacketSummary hoodPacketSummary(HoodServiceReportDraft draft) {
        List<PacketItem> items = List.of(
                new PacketItem(
                        "Customer-facing report",
                        draft.reportAttachmentReady(),
                        draft.reportAttachmentReady()
                                ? "Print or PDF is marked ready for the customer thread."
                                : "Generate the printable closeout before send."
                ),
                new PacketItem(
                        "Photo set",
                        draft.photoSetAttached(),
                        draft.photoSetAttached()
                                ? "The referenced photo set is marked attached."
                                : "Attach the before/after photos that match the photo reference."
                ),
                new PacketItem(
                        "Invoice or internal report",
                        draft.reportFileAttached(),
                        draft.reportFileAttached()
                                ? "The invoice or internal report file is marked attached."
                                : "Attach the invoice or internal report file tied to this closeout."
                ),
                new PacketItem(
                        "Reference rule link",
                        !draft.includeReferenceLink() || draft.referenceLinkAdded(),
                        draft.includeReferenceLink()
                                ? (draft.referenceLinkAdded()
                                        ? "The city rule link is marked added separately."
                                        : "If this customer wants rule context, add the city rule link separately.")
                                : "Not required for this send."
                )
        );
        int readyItems = (int) items.stream().filter(PacketItem::ready).count();
        String summary = readyItems == items.size()
                ? "The packet reads like a finished closeout, not a half-assembled office note."
                : "Use this as the final check before the office sends the closeout.";
        return new HoodPacketSummary(
                "Closeout packet status",
                summary,
                readyItems,
                items.size(),
                items
        );
    }

    private HoodAttachmentBundle hoodAttachmentBundle(HoodServiceReportDraft draft) {
        List<AttachmentBundleItem> items = List.of(
                new AttachmentBundleItem(
                        "Customer-facing report PDF",
                        draft.reportAttachmentReady(),
                        attachmentStem(draft) + "-hood-service-report.pdf",
                        draft.reportAttachmentReady()
                                ? "Use the printed report or PDF from this page under the same file name."
                                : "Export this page to PDF before the office sends the closeout."
                ),
                new AttachmentBundleItem(
                        "Before and after photos",
                        draft.photoSetAttached(),
                        attachmentStem(draft) + "-before-after-photos.zip",
                        draft.photoReference().isBlank()
                                ? "Zip the job photos into one attachment so the customer thread stays clean."
                                : "Match this attachment to photo reference " + draft.photoReference() + "."
                ),
                new AttachmentBundleItem(
                        "Invoice or work order file",
                        draft.reportFileAttached(),
                        attachmentStem(draft) + "-invoice-or-work-order.pdf",
                        draft.reportReference().isBlank()
                                ? "Attach the invoice or work order file that backs this closeout."
                                : "Match this attachment to report reference " + draft.reportReference() + "."
                ),
                new AttachmentBundleItem(
                        "Optional rule reference",
                        !draft.includeReferenceLink() || draft.referenceLinkAdded(),
                        attachmentStem(draft) + "-" + draft.cityKey() + "-hood-rule-link.txt",
                        draft.includeReferenceLink()
                                ? "Only include this when the customer actually wants rule context, and keep it separate from the report body."
                                : "Skip this unless the customer asks for the local rule page."
                )
        );
        long readyItems = items.stream().filter(AttachmentBundleItem::ready).count();
        String summary = readyItems == items.size()
                ? "The closeout files are named consistently and ready to forward."
                : "Use consistent file names so the office, customer, and later follow-up all point to the same closeout.";
        return new HoodAttachmentBundle(
                "Attachment bundle plan",
                summary,
                items
        );
    }

    private HoodServiceReportDraft hoodServiceReportDraft(MultiValueMap<String, String> params) {
        boolean sampleDraft = params == null || params.isEmpty();
        if ("blank".equalsIgnoreCase(firstValue(params, "draft"))) {
            sampleDraft = false;
        }
        HoodCityProfile cityProfile = hoodCityProfile(firstValue(params, "city"));
        LocalDate serviceDate = parseDate(firstValue(params, "serviceDate"), LocalDate.now());
        LocalDate nextServiceDate = parseDate(firstValue(params, "nextServiceDate"), serviceDate.plusDays(90));
        String followUpDueDate = sampleDraft
                ? parseDate(firstValue(params, "followUpDueDate"), serviceDate.plusDays(21)).toString()
                : optionalDate(firstValue(params, "followUpDueDate"));
        String customerName = sampleDraft
                ? defaulted(firstValue(params, "customerName"), "Example Bistro")
                : trimmedOrBlank(firstValue(params, "customerName"));
        boolean includeReferenceLink = firstValue(params, "includeReferenceLink") != null;
        List<String> followUpItems = sampleDraft
                ? nonEmptyLines(
                        defaulted(
                                normalizeMultiline(firstValue(params, "followUpItems")),
                                """
                                Replace missing access panel before the next inspection window.
                                Keep suppression paperwork filed separately from this hood cleaning record.
                                """
                        )
                )
                : nonEmptyLines(trimmedOrBlank(normalizeMultiline(firstValue(params, "followUpItems"))));
        return new HoodServiceReportDraft(
                cityProfile.key(),
                cityProfile.label(),
                cityProfile.authorityLabel(),
                cityProfile.referencePath(),
                canonicalUrl(cityProfile.referencePath()),
                serviceDate.toString(),
                customerName,
                sampleDraft ? defaulted(firstValue(params, "siteName"), customerName) : trimmedOrBlank(firstValue(params, "siteName")),
                sampleDraft ? defaulted(firstValue(params, "recipientName"), "Kitchen manager") : trimmedOrBlank(firstValue(params, "recipientName")),
                trimmedOrBlank(firstValue(params, "recipientEmail")),
                sampleDraft ? defaulted(firstValue(params, "siteAddress"), cityProfile.defaultAddress()) : trimmedOrBlank(firstValue(params, "siteAddress")),
                sampleDraft ? defaulted(firstValue(params, "vendorName"), "Example Hood Service") : trimmedOrBlank(firstValue(params, "vendorName")),
                sampleDraft ? defaulted(firstValue(params, "crewOrTechnician"), "Crew A") : trimmedOrBlank(firstValue(params, "crewOrTechnician")),
                sampleDraft ? defaulted(firstValue(params, "workOrderReference"), "WO-2048") : trimmedOrBlank(firstValue(params, "workOrderReference")),
                sampleDraft ? defaulted(firstValue(params, "systemsServiced"), "hood canopy, accessible duct, filters, rooftop fan") : trimmedOrBlank(firstValue(params, "systemsServiced")),
                sampleDraft
                        ? defaulted(
                                normalizeMultiline(firstValue(params, "completedWork")),
                                """
                                Cleaned hood canopy, accessible duct run, filters, and rooftop fan.
                                Removed grease buildup from accessible surfaces and reinstalled filters.
                                Documented before and after condition with photo set 12.
                                """
                        )
                        : trimmedOrBlank(normalizeMultiline(firstValue(params, "completedWork"))),
                sampleDraft ? defaulted(firstValue(params, "photoReference"), "before-after-set-12") : trimmedOrBlank(firstValue(params, "photoReference")),
                sampleDraft ? defaulted(firstValue(params, "reportReference"), "INV-2048") : trimmedOrBlank(firstValue(params, "reportReference")),
                sampleDraft ? defaulted(firstValue(params, "customerHandoffNote"), "Sent to GM with invoice") : trimmedOrBlank(firstValue(params, "customerHandoffNote")),
                followUpItems,
                sampleDraft ? defaulted(firstValue(params, "followUpOwner"), "Restaurant maintenance") : trimmedOrBlank(firstValue(params, "followUpOwner")),
                followUpDueDate,
                nextServiceDate.toString(),
                firstValue(params, "reportAttachmentReady") != null,
                firstValue(params, "photoSetAttached") != null,
                firstValue(params, "reportFileAttached") != null,
                firstValue(params, "referenceLinkAdded") != null,
                includeReferenceLink,
                sampleDraft
        );
    }

    private HoodCityProfile hoodCityProfile(String requestedKey) {
        return HOOD_CITY_PROFILES.stream()
                .filter(profile -> profile.key().equals(defaulted(requestedKey, "charlotte")))
                .findFirst()
                .orElseGet(() -> HOOD_CITY_PROFILES.get(1));
    }

    private java.util.Optional<HoodCityProfile> findHoodCityProfile(String requestedKey) {
        return HOOD_CITY_PROFILES.stream()
                .filter(profile -> profile.key().equals(defaulted(requestedKey, "charlotte")))
                .findFirst();
    }

    private List<String> splitLines(String value) {
        return value.lines()
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .toList();
    }

    private List<String> nonEmptyLines(String value) {
        return splitLines(value);
    }

    private List<String> buildProofPackItems(HoodServiceReportDraft draft) {
        List<String> items = new ArrayList<>();
        items.add("Customer-facing service report: this page or PDF printout");
        if (!draft.photoReference().isBlank()) {
            items.add("Before and after photo set: " + draft.photoReference());
        } else if (draft.photoSetAttached()) {
            items.add("Before and after photos: attached separately");
        }
        if (!draft.reportReference().isBlank()) {
            items.add("Invoice or internal report reference: " + draft.reportReference());
        } else if (draft.reportFileAttached()) {
            items.add("Invoice or internal report: attached separately");
        }
        if (!draft.customerHandoffNote().isBlank()) {
            items.add("Recipient note: " + draft.customerHandoffNote());
        }
        if (draft.includeReferenceLink()) {
            items.add("Reference-only rule page: " + draft.cityLabel() + " hood requirements");
        }
        return items;
    }

    private List<String> buildAttachedProofLines(HoodServiceReportDraft draft) {
        List<String> items = new ArrayList<>();
        if (!draft.photoReference().isBlank()) {
            items.add("Before and after photo reference: " + draft.photoReference());
        } else if (draft.photoSetAttached()) {
            items.add("Before and after photos: attached separately");
        }
        if (!draft.reportReference().isBlank()) {
            items.add("Internal report reference: " + draft.reportReference());
        } else if (draft.reportFileAttached()) {
            items.add("Invoice or internal report: attached separately");
        }
        if (!draft.customerHandoffNote().isBlank()) {
            items.add("Customer handoff note: " + draft.customerHandoffNote());
        }
        return items;
    }

    private boolean hasPhotoProof(HoodServiceReportDraft draft) {
        return !draft.photoReference().isBlank() || draft.photoSetAttached();
    }

    private boolean hasReportProof(HoodServiceReportDraft draft) {
        return !draft.reportReference().isBlank() || draft.reportFileAttached();
    }

    private List<String> buildFollowUpItems(HoodServiceReportDraft draft) {
        List<String> items = new ArrayList<>(draft.followUpItems());
        if (items.isEmpty()) {
            items.add("No follow-up items noted.");
            return items;
        }
        if (!draft.followUpOwner().isBlank() || !draft.followUpDueDate().isBlank()) {
            items.add("Owner: " + blankOrFallback(draft.followUpOwner(), "TBD")
                    + " | Target date: "
                    + blankOrFallback(displayDate(draft.followUpDueDate()), "TBD"));
        }
        return items;
    }

    private String joinWithSeparator(List<String> values, String separator) {
        return values.stream()
                .filter(value -> value != null && !value.isBlank())
                .reduce((left, right) -> left + separator + right)
                .orElse("");
    }

    private String attachmentStem(HoodServiceReportDraft draft) {
        String subject = slugify(reportLabel(draft));
        if (subject.isBlank()) {
            subject = draft.cityKey() + "-hood-closeout";
        }
        return subject + "-" + draft.serviceDate();
    }

    private String firstValue(MultiValueMap<String, String> params, String key) {
        return params == null ? null : params.getFirst(key);
    }

    private String defaulted(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private String trimmedOrBlank(String value) {
        return value == null ? "" : value.trim();
    }

    private String blankOrFallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private String normalizeMultiline(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.lines()
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .reduce((left, right) -> left + "\n" + right)
                .orElse(null);
    }

    private LocalDate parseDate(String value, LocalDate fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException ex) {
            return fallback;
        }
    }

    private String optionalDate(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        try {
            return LocalDate.parse(value.trim()).toString();
        } catch (DateTimeParseException ex) {
            return "";
        }
    }

    private String displayDate(String isoDate) {
        try {
            return LocalDate.parse(isoDate).format(HUMAN_DATE);
        } catch (DateTimeParseException ex) {
            return isoDate;
        }
    }

    private String csvEscape(String value) {
        String safe = value == null ? "" : value;
        if (safe.contains(",") || safe.contains("\"") || safe.contains("\n")) {
            return "\"" + safe.replace("\"", "\"\"") + "\"";
        }
        return safe;
    }

    private String greetingLine(String recipientName) {
        return recipientName == null || recipientName.isBlank()
                ? "Hi,"
                : "Hi " + recipientName + ",";
    }

    private String slugify(String value) {
        String normalized = Normalizer.normalize(blankOrFallback(value, ""), Normalizer.Form.NFKD)
                .replaceAll("\\p{M}+", "");
        return normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private record HoodCityProfile(
            String key,
            String label,
            String state,
            String authorityId,
            String authorityLabel,
            String referencePath,
            String defaultAddress
    ) {
    }

    public record ToolAttributionContext(
            String city,
            String state,
            String authorityId
    ) {
    }

    private record HoodServiceReportDraft(
            String cityKey,
            String cityLabel,
            String authorityLabel,
            String referencePath,
            String referenceUrl,
            String serviceDate,
            String customerName,
            String siteName,
            String recipientName,
            String recipientEmail,
            String siteAddress,
            String vendorName,
            String crewOrTechnician,
            String workOrderReference,
            String systemsServiced,
            String completedWork,
            String photoReference,
            String reportReference,
            String customerHandoffNote,
            List<String> followUpItems,
            String followUpOwner,
            String followUpDueDate,
            String nextServiceDate,
            boolean reportAttachmentReady,
            boolean photoSetAttached,
            boolean reportFileAttached,
            boolean referenceLinkAdded,
            boolean includeReferenceLink,
            boolean sampleDraft
    ) {
    }
}
