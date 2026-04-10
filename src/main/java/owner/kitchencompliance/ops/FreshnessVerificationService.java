package owner.kitchencompliance.ops;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.data.SourceRecord;

@Service
public class FreshnessVerificationService {

    private final SeedRegistry seedRegistry;
    private final SourceFreshnessService sourceFreshnessService;

    public FreshnessVerificationService(SeedRegistry seedRegistry, SourceFreshnessService sourceFreshnessService) {
        this.seedRegistry = seedRegistry;
        this.sourceFreshnessService = sourceFreshnessService;
    }

    public void verifyIndexedRoutes() {
        verifyIndexedRoutes(seedRegistry.routes());
    }

    public void verifyIndexedRoutes(List<RouteRecord> routes) {
        List<RouteFreshnessViolation> violations = new ArrayList<>();
        for (RouteRecord route : routes) {
            if (!route.indexable()) {
                continue;
            }
            List<SourceRecord> sources = seedRegistry.sourcesFor(route);
            List<SourceFreshnessAssessment> staleSources = sourceFreshnessService.assess(sources).stream()
                    .filter(assessment -> !assessment.fresh())
                    .toList();
            if (!staleSources.isEmpty()) {
                violations.add(new RouteFreshnessViolation(route.path(), route.decisionReason(), staleSources));
            }
        }

        if (!violations.isEmpty()) {
            throw new IllegalStateException(buildMessage(violations));
        }
    }

    private String buildMessage(List<RouteFreshnessViolation> violations) {
        StringBuilder message = new StringBuilder("Freshness gate failed for indexed routes:");
        for (RouteFreshnessViolation violation : violations) {
            message.append(System.lineSeparator())
                    .append("- ")
                    .append(violation.path())
                    .append(" [")
                    .append(violation.decisionReason())
                    .append("]");
            for (SourceFreshnessAssessment staleSource : violation.staleSources()) {
                message.append(System.lineSeparator())
                        .append("  - ")
                        .append(staleSource.sourceId())
                        .append(" (")
                        .append(staleSource.sourceTitle())
                        .append("): ")
                        .append(staleSource.statusMessage());
            }
        }
        return message.toString();
    }

    private record RouteFreshnessViolation(
            String path,
            String decisionReason,
            List<SourceFreshnessAssessment> staleSources
    ) {
    }
}
