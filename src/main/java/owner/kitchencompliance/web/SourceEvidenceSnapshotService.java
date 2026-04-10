package owner.kitchencompliance.web;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.data.SourceRecord;
import owner.kitchencompliance.ops.DeployReadinessAssessmentService;

@Service
public class SourceEvidenceSnapshotService {

    private final SeedRegistry seedRegistry;
    private final DeployReadinessAssessmentService deployReadinessAssessmentService;
    private final Clock clock;

    public SourceEvidenceSnapshotService(
            SeedRegistry seedRegistry,
            DeployReadinessAssessmentService deployReadinessAssessmentService,
            Clock clock
    ) {
        this.seedRegistry = seedRegistry;
        this.deployReadinessAssessmentService = deployReadinessAssessmentService;
        this.clock = clock;
    }

    public String exportIndexCsv() {
        StringBuilder builder = new StringBuilder("city,page,path,status,indexable_now,next_review_on,snapshot_file\n");
        seedRegistry.routes().stream()
                .filter(RouteRecord::indexable)
                .map(deployReadinessAssessmentService::assess)
                .forEach(assessment -> builder.append(csv(assessment.cityLabel())).append(',')
                        .append(csv(assessment.pageLabel())).append(',')
                        .append(csv(assessment.path())).append(',')
                        .append(csv(assessment.status().label())).append(',')
                        .append(assessment.indexableNow()).append(',')
                        .append(csv(assessment.nextReviewOn().toString())).append(',')
                        .append(csv(snapshotFilename(assessment.path())))
                        .append('\n'));
        return builder.toString();
    }

    public List<SnapshotFile> snapshotFiles() {
        LocalDate today = LocalDate.now(clock);
        return seedRegistry.routes().stream()
                .filter(RouteRecord::indexable)
                .map(route -> new SnapshotFile(
                        snapshotFilename(route.path()),
                        snapshotMarkdown(route, today)))
                .toList();
    }

    private String snapshotMarkdown(RouteRecord route, LocalDate today) {
        var assessment = deployReadinessAssessmentService.assess(route);
        var profile = seedRegistry.profile(route.profileId());
        List<SourceRecord> sources = seedRegistry.sourcesFor(route);

        StringBuilder builder = new StringBuilder();
        builder.append("# Source Evidence Snapshot").append(System.lineSeparator()).append(System.lineSeparator());
        builder.append("- Captured on: ").append(today).append(System.lineSeparator());
        builder.append("- City: ").append(titleCase(profile.city())).append(", ").append(profile.state().toUpperCase(Locale.ROOT)).append(System.lineSeparator());
        builder.append("- Page: ").append(pageLabel(route.template())).append(System.lineSeparator());
        builder.append("- Path: ").append(route.path()).append(System.lineSeparator());
        builder.append("- Deploy status: ").append(assessment.status().label()).append(System.lineSeparator());
        builder.append("- Indexable now: ").append(assessment.indexableNow()).append(System.lineSeparator());
        builder.append("- Next review on: ").append(assessment.nextReviewOn()).append(System.lineSeparator());
        builder.append("- Note: ").append(assessment.note()).append(System.lineSeparator()).append(System.lineSeparator());
        builder.append("## Sources").append(System.lineSeparator()).append(System.lineSeparator());
        for (SourceRecord source : sources) {
            builder.append("### ").append(source.title()).append(System.lineSeparator());
            builder.append("- Tier: ").append(source.sourceTier().label()).append(System.lineSeparator());
            builder.append("- Agency: ").append(source.agency()).append(System.lineSeparator());
            builder.append("- URL: ").append(source.sourceUrl()).append(System.lineSeparator());
            builder.append("- Verified on: ").append(source.verifiedOn()).append(System.lineSeparator());
            builder.append("- Next review on: ").append(source.nextReviewOn()).append(System.lineSeparator());
            builder.append("- Summary: ").append(source.quoteSummary()).append(System.lineSeparator()).append(System.lineSeparator());
        }
        return builder.toString();
    }

    private String snapshotFilename(String path) {
        String slug = path.replaceFirst("^/", "").replace('/', '-');
        return slug + ".md";
    }

    private String pageLabel(RouteTemplate template) {
        return switch (template) {
            case FOG_RULES -> "FOG rules";
            case APPROVED_HAULERS -> "Approved haulers";
            case HOOD_REQUIREMENTS -> "Hood requirements";
            case INSPECTION_CHECKLIST -> "Inspection checklist";
            case FIND_GREASE_SERVICE -> "Grease service finder";
            case FIND_HOOD_CLEANER -> "Hood cleaner finder";
        };
    }

    private String titleCase(String value) {
        String[] parts = value.trim().split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return builder.toString();
    }

    public record SnapshotFile(
            String filename,
            String contents
    ) {
    }

    private String csv(String value) {
        String safe = value == null ? "" : value;
        String escaped = safe.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
