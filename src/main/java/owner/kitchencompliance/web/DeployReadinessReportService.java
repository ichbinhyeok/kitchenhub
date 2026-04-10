package owner.kitchencompliance.web;

import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.model.DeployReadinessDashboardSnapshot;
import owner.kitchencompliance.model.DeployReadinessWatchRow;
import owner.kitchencompliance.ops.DeployReadinessAssessmentService;
import owner.kitchencompliance.ops.DeployReadinessAssessmentService.RouteDeployReadinessAssessment;
import owner.kitchencompliance.ops.DeployReadinessAssessmentService.RouteDeployStatus;

@Service
public class DeployReadinessReportService {

    private static final int WATCH_LIMIT = 12;

    private final DeployReadinessAssessmentService deployReadinessAssessmentService;

    public DeployReadinessReportService(DeployReadinessAssessmentService deployReadinessAssessmentService) {
        this.deployReadinessAssessmentService = deployReadinessAssessmentService;
    }

    public DeployReadinessDashboardSnapshot readDashboard() {
        List<RouteDeployReadinessAssessment> assessments = deployReadinessAssessmentService.assessIndexedRoutes();
        long readyRoutes = assessments.stream().filter(assessment -> assessment.status() == RouteDeployStatus.READY).count();
        long watchRoutes = assessments.stream().filter(assessment -> assessment.status() == RouteDeployStatus.WATCH).count();
        long blockedRoutes = assessments.stream().filter(assessment -> assessment.status() == RouteDeployStatus.BLOCKED).count();

        return new DeployReadinessDashboardSnapshot(
                assessments.size(),
                readyRoutes,
                watchRoutes,
                blockedRoutes,
                assessments.stream()
                        .limit(WATCH_LIMIT)
                        .map(assessment -> new DeployReadinessWatchRow(
                                assessment.cityLabel(),
                                assessment.pageLabel(),
                                assessment.path(),
                                assessment.status().label(),
                                assessment.indexableNow() ? "Indexable" : "Noindex now",
                                assessment.nextReviewOn().toString(),
                                assessment.totalSources(),
                                assessment.strongSources(),
                                assessment.renderableProviders(),
                                assessment.authorityBackedProviders(),
                                assessment.note()))
                        .toList()
        );
    }

    public String exportWatchCsv() {
        List<RouteDeployReadinessAssessment> assessments = deployReadinessAssessmentService.assessIndexedRoutes();
        StringBuilder builder = new StringBuilder(
                "city,page,path,status,indexable_now,next_review_on,total_sources,strong_sources,renderable_providers,authority_backed_providers,note\n");
        assessments.forEach(row -> builder.append(csv(row.cityLabel())).append(',')
                .append(csv(row.pageLabel())).append(',')
                .append(csv(row.path())).append(',')
                .append(csv(row.status().label())).append(',')
                .append(row.indexableNow()).append(',')
                .append(csv(row.nextReviewOn().toString())).append(',')
                .append(row.totalSources()).append(',')
                .append(row.strongSources()).append(',')
                .append(row.renderableProviders()).append(',')
                .append(row.authorityBackedProviders()).append(',')
                .append(csv(row.note())).append('\n'));
        return builder.toString();
    }

    private String csv(String value) {
        String safe = value == null ? "" : value;
        String escaped = safe.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
