package owner.kitchencompliance.web;

import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.model.NoindexPromotionQueueSnapshot;
import owner.kitchencompliance.model.NoindexPromotionRow;
import owner.kitchencompliance.ops.NoindexPromotionAssessmentService;
import owner.kitchencompliance.ops.NoindexPromotionAssessmentService.PromotionStatus;
import owner.kitchencompliance.ops.NoindexPromotionAssessmentService.RouteNoindexPromotionAssessment;
import owner.kitchencompliance.ops.SearchDemandAssessmentService;

@Service
public class NoindexPromotionReportService {

    private static final int ROW_LIMIT = 12;

    private final NoindexPromotionAssessmentService noindexPromotionAssessmentService;
    private final SeedRegistry seedRegistry;
    private final SearchDemandAssessmentService searchDemandAssessmentService;
    private final SearchDemandReportService searchDemandReportService;

    public NoindexPromotionReportService(
            NoindexPromotionAssessmentService noindexPromotionAssessmentService,
            SeedRegistry seedRegistry,
            SearchDemandAssessmentService searchDemandAssessmentService,
            SearchDemandReportService searchDemandReportService
    ) {
        this.noindexPromotionAssessmentService = noindexPromotionAssessmentService;
        this.seedRegistry = seedRegistry;
        this.searchDemandAssessmentService = searchDemandAssessmentService;
        this.searchDemandReportService = searchDemandReportService;
    }

    public NoindexPromotionQueueSnapshot readDashboard() {
        List<RouteNoindexPromotionAssessment> assessments = noindexPromotionAssessmentService.assessQueuedRoutes();
        long readyToPromoteRoutes = assessments.stream().filter(assessment -> assessment.status() == PromotionStatus.READY_TO_PROMOTE).count();
        long overdueRoutes = assessments.stream().filter(assessment -> assessment.status() == PromotionStatus.OVERDUE).count();
        long dueSoonRoutes = assessments.stream().filter(assessment -> assessment.status() == PromotionStatus.DUE_SOON).count();

        return new NoindexPromotionQueueSnapshot(
                assessments.size(),
                readyToPromoteRoutes,
                dueSoonRoutes,
                overdueRoutes,
                assessments.stream()
                        .limit(ROW_LIMIT)
                        .map(this::toRow)
                        .toList()
        );
    }

    public String exportQueueCsv() {
        List<RouteNoindexPromotionAssessment> assessments = noindexPromotionAssessmentService.assessQueuedRoutes();
        StringBuilder builder = new StringBuilder(
                "city,page,path,status,ready_to_promote,next_review_on,reason,total_sources,strong_sources,renderable_providers,authority_backed_providers,promotion_checklist,search_demand_status,top_query,impressions_28d,clicks_28d,ctr,average_position\n");
        assessments.forEach(assessment -> {
            NoindexPromotionRow row = toRow(assessment);
            builder.append(csv(row.cityLabel())).append(',')
                    .append(csv(row.pageLabel())).append(',')
                    .append(csv(row.path())).append(',')
                    .append(csv(row.statusLabel())).append(',')
                    .append("Ready if reviewed".equals(row.readinessLabel())).append(',')
                    .append(csv(row.nextReviewOn())).append(',')
                    .append(csv(row.reason())).append(',')
                    .append(row.totalSources()).append(',')
                    .append(row.strongSources()).append(',')
                    .append(row.renderableProviders()).append(',')
                    .append(row.authorityBackedProviders()).append(',')
                    .append(csv(String.join(" | ", row.checklist()))).append(',')
                    .append(csv(row.searchDemandStatusLabel())).append(',')
                    .append(csv(row.topQuery())).append(',')
                    .append(row.impressions28d() == null ? "" : row.impressions28d()).append(',')
                    .append(row.clicks28d() == null ? "" : row.clicks28d()).append(',')
                    .append(csv(row.ctrLabel())).append(',')
                    .append(csv(row.averagePositionLabel()))
                    .append('\n');
        });
        return builder.toString();
    }

    private NoindexPromotionRow toRow(RouteNoindexPromotionAssessment assessment) {
        var demand = searchDemandAssessmentService.assessmentFor(seedRegistry.route(assessment.path()))
                .map(searchDemandReportService::rowFor)
                .orElse(null);
        return new NoindexPromotionRow(
                assessment.cityLabel(),
                assessment.pageLabel(),
                assessment.path(),
                assessment.status().label(),
                assessment.readyToPromote() ? "Ready if reviewed" : "Keep noindex",
                assessment.nextReviewOn().toString(),
                assessment.reason(),
                assessment.checklist(),
                assessment.totalSources(),
                assessment.strongSources(),
                assessment.renderableProviders(),
                assessment.authorityBackedProviders(),
                demand == null ? null : demand.statusLabel(),
                demand == null ? null : demand.topQuery(),
                demand == null ? null : demand.impressions28d(),
                demand == null ? null : demand.clicks28d(),
                demand == null ? null : demand.ctrLabel(),
                demand == null ? null : demand.averagePositionLabel()
        );
    }

    private String csv(String value) {
        String safe = value == null ? "" : value;
        String escaped = safe.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
