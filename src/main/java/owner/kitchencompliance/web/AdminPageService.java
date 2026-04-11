package owner.kitchencompliance.web;

import org.springframework.stereotype.Service;
import owner.kitchencompliance.model.AdminPageViewModel;
import owner.kitchencompliance.model.AttributionDashboardSnapshot;
import owner.kitchencompliance.model.PageMeta;

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
        PageMeta meta = new PageMeta(
                "Attribution admin | " + siteProperties.title(),
                "Read-only attribution dashboard for provider outbound clicks and next-action CTAs.",
                canonicalUrl("/admin"),
                "noindex,nofollow",
                null,
                null
        );

        return new AdminPageViewModel(
                meta,
                "Attribution admin",
                "Read-only summary of attribution, lead intake, and deploy-readiness signals.",
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

    private String canonicalUrl(String path) {
        if (path.equals("/")) {
            return siteProperties.baseUrl();
        }
        return siteProperties.baseUrl() + path;
    }
}
