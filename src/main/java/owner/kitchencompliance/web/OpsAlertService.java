package owner.kitchencompliance.web;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.ops.DeployReadinessAssessmentService;
import owner.kitchencompliance.ops.DeployReadinessAssessmentService.RouteDeployReadinessAssessment;
import owner.kitchencompliance.ops.DeployReadinessAssessmentService.RouteDeployStatus;

@Service
public class OpsAlertService {

    private final DeployReadinessAssessmentService deployReadinessAssessmentService;
    private final Clock clock;

    public OpsAlertService(DeployReadinessAssessmentService deployReadinessAssessmentService, Clock clock) {
        this.deployReadinessAssessmentService = deployReadinessAssessmentService;
        this.clock = clock;
    }

    public String latestAlertMarkdown() {
        LocalDate today = LocalDate.now(clock);
        List<RouteDeployReadinessAssessment> assessments = deployReadinessAssessmentService.assessIndexedRoutes();
        List<RouteDeployReadinessAssessment> blocked = assessments.stream()
                .filter(assessment -> assessment.status() == RouteDeployStatus.BLOCKED)
                .toList();
        List<RouteDeployReadinessAssessment> watch = assessments.stream()
                .filter(assessment -> assessment.status() == RouteDeployStatus.WATCH)
                .toList();

        StringBuilder builder = new StringBuilder();
        builder.append("# Ops Alert Snapshot").append(System.lineSeparator()).append(System.lineSeparator());
        builder.append("- Generated on: ").append(today).append(System.lineSeparator());
        builder.append("- Blocked routes: ").append(blocked.size()).append(System.lineSeparator());
        builder.append("- Watch routes: ").append(watch.size()).append(System.lineSeparator()).append(System.lineSeparator());

        builder.append("## Blocked").append(System.lineSeparator()).append(System.lineSeparator());
        if (blocked.isEmpty()) {
            builder.append("- None").append(System.lineSeparator()).append(System.lineSeparator());
        } else {
            for (RouteDeployReadinessAssessment assessment : blocked) {
                builder.append("- ").append(assessment.path()).append(" | ").append(assessment.note()).append(System.lineSeparator());
            }
            builder.append(System.lineSeparator());
        }

        builder.append("## Watch").append(System.lineSeparator()).append(System.lineSeparator());
        if (watch.isEmpty()) {
            builder.append("- None").append(System.lineSeparator());
        } else {
            for (RouteDeployReadinessAssessment assessment : watch) {
                builder.append("- ").append(assessment.path()).append(" | ").append(assessment.note()).append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

    public AlertSummary summary() {
        List<RouteDeployReadinessAssessment> assessments = deployReadinessAssessmentService.assessIndexedRoutes();
        long blocked = assessments.stream().filter(assessment -> assessment.status() == RouteDeployStatus.BLOCKED).count();
        long watch = assessments.stream().filter(assessment -> assessment.status() == RouteDeployStatus.WATCH).count();
        return new AlertSummary(blocked, watch);
    }

    public record AlertSummary(
            long blockedRoutes,
            long watchRoutes
    ) {
    }
}
