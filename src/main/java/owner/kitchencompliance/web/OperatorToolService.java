package owner.kitchencompliance.web;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.IssueType;
import owner.kitchencompliance.data.PageFamily;
import owner.kitchencompliance.model.HomePanelLink;
import owner.kitchencompliance.model.OperatorToolPageViewModel;
import owner.kitchencompliance.model.PageMeta;
import owner.kitchencompliance.model.RelatedPageLink;

@Service
public class OperatorToolService {

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
                            case "grease-log" -> "download";
                            case "hood-record-binder" -> "upload_file";
                            case "inspection-reminder-plan" -> "calculate";
                            default -> "construction";
                        }
                ))
                .toList();
    }

    public OperatorToolPageViewModel toolPage(String slug) {
        OperatorToolCatalog.ToolDefinition tool = operatorToolCatalog.tool(slug);
        PageMeta meta = new PageMeta(
                tool.title() + " | " + siteProperties.title(),
                tool.summary(),
                canonicalUrl("/tools/" + tool.slug()),
                "noindex,follow",
                LocalDate.now(),
                null
        );
        return new OperatorToolPageViewModel(
                meta,
                tool.slug(),
                tool.eyebrow(),
                tool.title(),
                tool.summary(),
                tool.checklist(),
                tool.downloads(),
                tool.relatedLinks()
        );
    }

    public String csvTemplate(String slug) {
        return switch (slug) {
            case "grease-log" -> """
                    service_date,vendor_name,service_type,gallons_removed,receipt_reference,log_location,notes
                    2026-04-07,Example Hauler,pump_out,240,INV-1001,FOG binder,"Pulled before inspection week"
                    """;
            case "hood-record-binder" -> """
                    document_type,last_service_date,authority_or_vendor,location_in_binder,next_due_date,follow_up
                    hood_cleaning_report,2026-04-07,Example Hood Cleaner,Section A,2026-07-07,Confirm next cleaning
                    suppression_tag,2026-04-07,Example Fire Vendor,Section B,2027-04-07,Keep visible on site
                    """;
            case "inspection-reminder-plan" -> """
                    reminder_date,city,authority,issue_type,missing_proof,next_action,owner,status
                    2026-04-21,Austin,Austin Fire Department,inspection_prep,Current hood report,Book service and file report,Kitchen manager,open
                    """;
            default -> throw new IllegalArgumentException("Unknown tool slug: " + slug);
        };
    }

    public PageFamily pageFamily() {
        return PageFamily.OPERATOR_TOOL;
    }

    public IssueType issueTypeFor(String slug) {
        return switch (slug) {
            case "grease-log" -> IssueType.MANIFEST_OR_LOG;
            case "hood-record-binder" -> IssueType.HOOD_CLEANING;
            case "inspection-reminder-plan" -> IssueType.INSPECTION_PREP;
            default -> IssueType.OPERATOR_UTILITY;
        };
    }

    private String canonicalUrl(String path) {
        if (path.equals("/")) {
            return siteProperties.baseUrl();
        }
        return siteProperties.baseUrl() + path;
    }
}
