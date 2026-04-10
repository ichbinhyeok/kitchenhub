package owner.kitchencompliance.ops;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DeployReadinessVerificationService {

    private final DeployReadinessAssessmentService deployReadinessAssessmentService;

    public DeployReadinessVerificationService(DeployReadinessAssessmentService deployReadinessAssessmentService) {
        this.deployReadinessAssessmentService = deployReadinessAssessmentService;
    }

    public void verifyIndexedRoutes() {
        List<DeployReadinessAssessmentService.RouteDeployReadinessAssessment> failures = deployReadinessAssessmentService
                .assessIndexedRoutes().stream()
                .filter(assessment -> assessment.status() == DeployReadinessAssessmentService.RouteDeployStatus.BLOCKED)
                .toList();

        if (failures.isEmpty()) {
            return;
        }

        StringBuilder message = new StringBuilder("Deploy readiness gate failed for indexed routes:");
        for (DeployReadinessAssessmentService.RouteDeployReadinessAssessment failure : failures) {
            message.append(System.lineSeparator())
                    .append("- ")
                    .append(failure.path())
                    .append(" [")
                    .append(failure.status().label())
                    .append("] ")
                    .append(failure.note())
                    .append(" (indexableNow=")
                    .append(failure.indexableNow())
                    .append(", nextReviewOn=")
                    .append(failure.nextReviewOn())
                    .append(")");
        }
        throw new IllegalStateException(message.toString());
    }
}
