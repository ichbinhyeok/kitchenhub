package owner.kitchencompliance.ops;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SourceQualityVerificationService {

    private final SourceQualityAssessmentService sourceQualityAssessmentService;

    public SourceQualityVerificationService(SourceQualityAssessmentService sourceQualityAssessmentService) {
        this.sourceQualityAssessmentService = sourceQualityAssessmentService;
    }

    public void verifyIndexedRoutes() {
        List<SourceQualityAssessmentService.RouteSourceQualityAssessment> failures = sourceQualityAssessmentService.assessIndexedRoutes().stream()
                .filter(assessment -> assessment.status() == SourceQualityAssessmentService.SourceQualityStatus.CRITICAL)
                .toList();

        if (failures.isEmpty()) {
            return;
        }

        StringBuilder message = new StringBuilder("Source quality gate failed for indexed routes:");
        for (SourceQualityAssessmentService.RouteSourceQualityAssessment failure : failures) {
            message.append(System.lineSeparator())
                    .append("- ")
                    .append(failure.path())
                    .append(" [")
                    .append(failure.status().label())
                    .append("] ")
                    .append(failure.note())
                    .append(" (sources=")
                    .append(failure.totalSources())
                    .append(", strong=")
                    .append(failure.strongSources())
                    .append(")");
        }
        throw new IllegalStateException(message.toString());
    }
}
