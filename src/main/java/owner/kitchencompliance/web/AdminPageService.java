package owner.kitchencompliance.web;

import org.springframework.stereotype.Service;
import owner.kitchencompliance.model.AdminBreakdownRow;
import owner.kitchencompliance.model.AdminPageViewModel;
import owner.kitchencompliance.model.AttributionDashboardSnapshot;
import owner.kitchencompliance.model.PageMeta;
import owner.kitchencompliance.model.SearchDemandWatchRow;
import owner.kitchencompliance.model.SponsorBetaFocusSnapshot;

@Service
public class AdminPageService {

    private final SiteProperties siteProperties;
    private final AttributionReportService attributionReportService;
    private final LeadReportService leadReportService;
    private final FreshnessReportService freshnessReportService;
    private final SourceQualityReportService sourceQualityReportService;
    private final DeployReadinessReportService deployReadinessReportService;
    private final NoindexPromotionReportService noindexPromotionReportService;
    private final SearchDemandReportService searchDemandReportService;
    private final SourceEvidenceSnapshotService sourceEvidenceSnapshotService;
    private final OpsAlertService opsAlertService;

    public AdminPageService(
            SiteProperties siteProperties,
            AttributionReportService attributionReportService,
            LeadReportService leadReportService,
            FreshnessReportService freshnessReportService,
            SourceQualityReportService sourceQualityReportService,
            DeployReadinessReportService deployReadinessReportService,
            NoindexPromotionReportService noindexPromotionReportService,
            SearchDemandReportService searchDemandReportService,
            SourceEvidenceSnapshotService sourceEvidenceSnapshotService,
            OpsAlertService opsAlertService
    ) {
        this.siteProperties = siteProperties;
        this.attributionReportService = attributionReportService;
        this.leadReportService = leadReportService;
        this.freshnessReportService = freshnessReportService;
        this.sourceQualityReportService = sourceQualityReportService;
        this.deployReadinessReportService = deployReadinessReportService;
        this.noindexPromotionReportService = noindexPromotionReportService;
        this.searchDemandReportService = searchDemandReportService;
        this.sourceEvidenceSnapshotService = sourceEvidenceSnapshotService;
        this.opsAlertService = opsAlertService;
    }

    public AdminPageViewModel adminPage() {
        AttributionDashboardSnapshot dashboard = attributionReportService.readDashboard();
        var leads = leadReportService.readDashboard();
        var freshness = freshnessReportService.readDashboard();
        var sourceQuality = sourceQualityReportService.readDashboard();
        var deployReadiness = deployReadinessReportService.readDashboard();
        var noindexPromotionQueue = noindexPromotionReportService.readDashboard();
        var searchDemand = searchDemandReportService.readDashboard();
        var sponsorBetaFocus = sponsorBetaFocus(dashboard, leads, searchDemand);
        PageMeta meta = new PageMeta(
                "Sponsor beta admin | " + siteProperties.title(),
                "Read-only admin dashboard for sponsor beta traction, attribution, and next-action CTAs.",
                canonicalUrl("/admin"),
                "noindex,nofollow",
                null,
                null
        );

        return new AdminPageViewModel(
                meta,
                "Sponsor beta admin",
                "Read-only summary of sponsor beta traction, attribution, and deploy-readiness signals.",
                sponsorBetaFocus,
                dashboard,
                leads,
                freshness,
                sourceQuality,
                deployReadiness,
                noindexPromotionQueue,
                searchDemand
        );
    }

    public String exportAttributionEventsCsv() {
        return attributionReportService.exportEventsCsv();
    }

    public String exportAttributionSummaryCsv() {
        return attributionReportService.exportSummaryCsv();
    }

    public String exportLeadEventsCsv() {
        return leadReportService.exportEventsCsv();
    }

    public String exportLeadSummaryCsv() {
        return leadReportService.exportSummaryCsv();
    }

    public String exportFreshnessWatchCsv() {
        return freshnessReportService.exportWatchCsv();
    }

    public String exportSourceQualityWatchCsv() {
        return sourceQualityReportService.exportWatchCsv();
    }

    public String exportDeployReadinessCsv() {
        return deployReadinessReportService.exportWatchCsv();
    }

    public String exportNoindexPromotionQueueCsv() {
        return noindexPromotionReportService.exportQueueCsv();
    }

    public String exportSearchDemandWatchCsv() {
        return searchDemandReportService.exportWatchCsv();
    }

    public String exportOperatorUtilitySummaryCsv() {
        return attributionReportService.exportOperatorUtilitySummaryCsv();
    }

    public String exportEvidenceIndexCsv() {
        return sourceEvidenceSnapshotService.exportIndexCsv();
    }

    public String exportOpsAlertsMarkdown() {
        return opsAlertService.latestAlertMarkdown();
    }

    private SponsorBetaFocusSnapshot sponsorBetaFocus(
            AttributionDashboardSnapshot dashboard,
            owner.kitchencompliance.model.LeadDashboardSnapshot leads,
            owner.kitchencompliance.model.SearchDemandDashboardSnapshot searchDemand
    ) {
        return new SponsorBetaFocusSnapshot(
                dashboard.sponsoredClicks(),
                leads.sponsorInquiries(),
                focusBreakdown(searchDemand.rows(), SearchDemandWatchRow::cityLabel, "Austin, TX", "Miami, FL", "Charlotte, NC"),
                focusBreakdown(searchDemand.rows(), SearchDemandWatchRow::pageLabel, "FOG rules", "Hood requirements", "Inspection checklist"),
                "Pinned to Austin, Miami, and Charlotte so ops can read sponsor beta traction without scanning all-city output."
        );
    }

    private java.util.List<AdminBreakdownRow> focusBreakdown(
            java.util.List<SearchDemandWatchRow> rows,
            java.util.function.Function<SearchDemandWatchRow, String> labelExtractor,
            String... focusLabels
    ) {
        java.util.Map<String, Long> counts = new java.util.LinkedHashMap<>();
        for (String focusLabel : focusLabels) {
            counts.put(focusLabel, 0L);
        }
        for (SearchDemandWatchRow row : rows) {
            String label = labelExtractor.apply(row);
            if (counts.containsKey(label)) {
                counts.put(label, counts.get(label) + 1);
            }
        }
        return counts.entrySet().stream()
                .sorted(java.util.Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(java.util.Map.Entry::getKey))
                .map(entry -> new AdminBreakdownRow(entry.getKey(), entry.getValue()))
                .toList();
    }

    private String canonicalUrl(String path) {
        if (path.equals("/")) {
            return siteProperties.baseUrl();
        }
        return siteProperties.baseUrl() + path;
    }
}
