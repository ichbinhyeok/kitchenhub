package owner.kitchencompliance.web;

import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.model.SearchDemandDashboardSnapshot;
import owner.kitchencompliance.model.SearchDemandWatchRow;
import owner.kitchencompliance.ops.SearchDemandAssessmentService;
import owner.kitchencompliance.ops.SearchDemandAssessmentService.RouteSearchDemandAssessment;
import owner.kitchencompliance.ops.SearchDemandAssessmentService.SearchDemandStatus;

@Service
public class SearchDemandReportService {

    private static final int ROW_LIMIT = 12;

    private final SearchDemandAssessmentService searchDemandAssessmentService;

    public SearchDemandReportService(SearchDemandAssessmentService searchDemandAssessmentService) {
        this.searchDemandAssessmentService = searchDemandAssessmentService;
    }

    public SearchDemandDashboardSnapshot readDashboard() {
        List<RouteSearchDemandAssessment> assessments = searchDemandAssessmentService.assessTrackedRoutes();
        long promotionDemandRoutes = assessments.stream().filter(assessment -> assessment.status() == SearchDemandStatus.PROMOTION_DEMAND).count();
        long ctrWorkRoutes = assessments.stream().filter(assessment -> assessment.status() == SearchDemandStatus.CTR_WORK).count();
        long discoverabilityWorkRoutes = assessments.stream().filter(assessment -> assessment.status() == SearchDemandStatus.DISCOVERABILITY_WORK).count();
        long monitorRoutes = assessments.stream().filter(assessment -> assessment.status() == SearchDemandStatus.MONITOR).count();

        return new SearchDemandDashboardSnapshot(
                assessments.size(),
                promotionDemandRoutes,
                ctrWorkRoutes,
                discoverabilityWorkRoutes,
                monitorRoutes,
                assessments.stream()
                        .limit(ROW_LIMIT)
                        .map(this::toRow)
                        .toList()
        );
    }

    public String exportWatchCsv() {
        List<RouteSearchDemandAssessment> assessments = searchDemandAssessmentService.assessTrackedRoutes();
        StringBuilder builder = new StringBuilder(
                "city,page,path,route_state,status,top_query,impressions_28d,clicks_28d,ctr,average_position,captured_on,note\n");
        assessments.forEach(row -> builder.append(csv(row.cityLabel())).append(',')
                .append(csv(row.pageLabel())).append(',')
                .append(csv(row.path())).append(',')
                .append(csv(row.routeStateLabel())).append(',')
                .append(csv(row.status().label())).append(',')
                .append(csv(row.topQuery())).append(',')
                .append(row.impressions28d()).append(',')
                .append(row.clicks28d()).append(',')
                .append(csv(formatPercent(row.ctr()))).append(',')
                .append(csv(formatPosition(row.averagePosition()))).append(',')
                .append(csv(row.capturedOn())).append(',')
                .append(csv(row.note()))
                .append('\n'));
        return builder.toString();
    }

    public SearchDemandWatchRow rowFor(RouteSearchDemandAssessment assessment) {
        return toRow(assessment);
    }

    private SearchDemandWatchRow toRow(RouteSearchDemandAssessment assessment) {
        return new SearchDemandWatchRow(
                assessment.cityLabel(),
                assessment.pageLabel(),
                assessment.path(),
                assessment.routeStateLabel(),
                assessment.status().label(),
                assessment.topQuery(),
                assessment.impressions28d(),
                assessment.clicks28d(),
                formatPercent(assessment.ctr()),
                formatPosition(assessment.averagePosition()),
                assessment.capturedOn(),
                assessment.note()
        );
    }

    private String formatPercent(double value) {
        return String.format(Locale.ROOT, "%.1f%%", value * 100.0);
    }

    private String formatPosition(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    private String csv(String value) {
        String safe = value == null ? "" : value;
        String escaped = safe.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
