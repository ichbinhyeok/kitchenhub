package owner.kitchencompliance.web;

import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.model.SourceQualityDashboardSnapshot;
import owner.kitchencompliance.model.SourceQualityWatchRow;
import owner.kitchencompliance.ops.SourceQualityAssessmentService;
import owner.kitchencompliance.ops.SourceQualityAssessmentService.RouteSourceQualityAssessment;
import owner.kitchencompliance.ops.SourceQualityAssessmentService.SourceQualityStatus;

@Service
public class SourceQualityReportService {

    private static final int WATCH_LIMIT = 12;

    private final SourceQualityAssessmentService sourceQualityAssessmentService;

    public SourceQualityReportService(SourceQualityAssessmentService sourceQualityAssessmentService) {
        this.sourceQualityAssessmentService = sourceQualityAssessmentService;
    }

    public SourceQualityDashboardSnapshot readDashboard() {
        List<RouteSourceQualityAssessment> assessments = sourceQualityAssessmentService.assessIndexedRoutes();
        long strongRoutes = assessments.stream().filter(assessment -> assessment.status() == SourceQualityStatus.STRONG).count();
        long adequateRoutes = assessments.stream().filter(assessment -> assessment.status() == SourceQualityStatus.ADEQUATE).count();
        long thinRoutes = assessments.stream().filter(assessment -> assessment.status() == SourceQualityStatus.THIN).count();
        long criticalRoutes = assessments.stream().filter(assessment -> assessment.status() == SourceQualityStatus.CRITICAL).count();

        return new SourceQualityDashboardSnapshot(
                assessments.size(),
                strongRoutes,
                adequateRoutes,
                thinRoutes,
                criticalRoutes,
                assessments.stream()
                        .limit(WATCH_LIMIT)
                        .map(assessment -> new SourceQualityWatchRow(
                                assessment.cityLabel(),
                                assessment.pageLabel(),
                                assessment.path(),
                                assessment.status().label(),
                                assessment.totalSources(),
                                assessment.strongSources(),
                                assessment.note()))
                        .toList()
        );
    }

    public String exportWatchCsv() {
        List<RouteSourceQualityAssessment> assessments = sourceQualityAssessmentService.assessIndexedRoutes();
        StringBuilder builder = new StringBuilder("city,page,path,status,total_sources,strong_sources,note\n");
        assessments.forEach(row -> builder.append(csv(row.cityLabel())).append(',')
                .append(csv(row.pageLabel())).append(',')
                .append(csv(row.path())).append(',')
                .append(csv(row.status().label())).append(',')
                .append(row.totalSources()).append(',')
                .append(row.strongSources()).append(',')
                .append(csv(row.note())).append('\n'));
        return builder.toString();
    }

    private String csv(String value) {
        String safe = value == null ? "" : value;
        String escaped = safe.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
