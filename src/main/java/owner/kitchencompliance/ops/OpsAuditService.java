package owner.kitchencompliance.ops;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.web.AttributionReportService;
import owner.kitchencompliance.web.DeployReadinessReportService;
import owner.kitchencompliance.web.FreshnessReportService;
import owner.kitchencompliance.web.OpsAlertService;
import owner.kitchencompliance.web.SourceQualityReportService;
import owner.kitchencompliance.web.SourceEvidenceSnapshotService;

@Service
public class OpsAuditService {

    private static final Logger log = LoggerFactory.getLogger(OpsAuditService.class);

    private final OpsProperties properties;
    private final FreshnessReportService freshnessReportService;
    private final AttributionReportService attributionReportService;
    private final SourceQualityReportService sourceQualityReportService;
    private final DeployReadinessReportService deployReadinessReportService;
    private final SourceEvidenceSnapshotService sourceEvidenceSnapshotService;
    private final OpsAlertService opsAlertService;
    private final Clock clock;

    public OpsAuditService(
            OpsProperties properties,
            FreshnessReportService freshnessReportService,
            AttributionReportService attributionReportService,
            SourceQualityReportService sourceQualityReportService,
            DeployReadinessReportService deployReadinessReportService,
            SourceEvidenceSnapshotService sourceEvidenceSnapshotService,
            OpsAlertService opsAlertService,
            Clock clock
    ) {
        this.properties = properties;
        this.freshnessReportService = freshnessReportService;
        this.attributionReportService = attributionReportService;
        this.sourceQualityReportService = sourceQualityReportService;
        this.deployReadinessReportService = deployReadinessReportService;
        this.sourceEvidenceSnapshotService = sourceEvidenceSnapshotService;
        this.opsAlertService = opsAlertService;
        this.clock = clock;
    }

    public void writeSnapshot() {
        LocalDate today = LocalDate.now(clock);
        Path baseDir = properties.auditDirectoryPath();
        Path freshnessLatest = baseDir.resolve("freshness-watch-latest.csv");
        Path freshnessDated = baseDir.resolve("freshness-watch-" + today + ".csv");
        Path sourceQualityLatest = baseDir.resolve("source-quality-watch-latest.csv");
        Path sourceQualityDated = baseDir.resolve("source-quality-watch-" + today + ".csv");
        Path deployReadinessLatest = baseDir.resolve("deploy-readiness-latest.csv");
        Path deployReadinessDated = baseDir.resolve("deploy-readiness-" + today + ".csv");
        Path evidenceIndexLatest = baseDir.resolve("evidence-index-latest.csv");
        Path evidenceIndexDated = baseDir.resolve("evidence-index-" + today + ".csv");
        Path alertLatest = baseDir.resolve("ops-alerts-latest.md");
        Path alertDated = baseDir.resolve("ops-alerts-" + today + ".md");
        Path attributionLatest = baseDir.resolve("attribution-summary-latest.csv");
        Path attributionDated = baseDir.resolve("attribution-summary-" + today + ".csv");

        writeCsv(freshnessLatest, freshnessReportService.exportWatchCsv());
        writeCsv(freshnessDated, freshnessReportService.exportWatchCsv());
        writeCsv(sourceQualityLatest, sourceQualityReportService.exportWatchCsv());
        writeCsv(sourceQualityDated, sourceQualityReportService.exportWatchCsv());
        writeCsv(deployReadinessLatest, deployReadinessReportService.exportWatchCsv());
        writeCsv(deployReadinessDated, deployReadinessReportService.exportWatchCsv());
        writeCsv(evidenceIndexLatest, sourceEvidenceSnapshotService.exportIndexCsv());
        writeCsv(evidenceIndexDated, sourceEvidenceSnapshotService.exportIndexCsv());
        writeMarkdown(alertLatest, opsAlertService.latestAlertMarkdown());
        writeMarkdown(alertDated, opsAlertService.latestAlertMarkdown());
        writeCsv(attributionLatest, attributionReportService.exportSummaryCsv());
        writeCsv(attributionDated, attributionReportService.exportSummaryCsv());
        writeEvidenceSnapshots(baseDir.resolve("source-evidence").resolve("latest"));
        writeEvidenceSnapshots(baseDir.resolve("source-evidence").resolve(today.toString()));
        logAlertSummary();
    }

    private void writeCsv(Path path, String contents) {
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, contents, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write ops snapshot to " + path, ex);
        }
    }

    private void writeMarkdown(Path path, String contents) {
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, contents, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write ops markdown snapshot to " + path, ex);
        }
    }

    private void writeEvidenceSnapshots(Path directory) {
        for (SourceEvidenceSnapshotService.SnapshotFile snapshotFile : sourceEvidenceSnapshotService.snapshotFiles()) {
            writeMarkdown(directory.resolve(snapshotFile.filename()), snapshotFile.contents());
        }
    }

    private void logAlertSummary() {
        OpsAlertService.AlertSummary summary = opsAlertService.summary();
        if (summary.blockedRoutes() > 0) {
            log.warn("Ops audit found {} blocked routes and {} watch routes.", summary.blockedRoutes(), summary.watchRoutes());
        } else if (summary.watchRoutes() > 0) {
            log.warn("Ops audit found {} watch routes and no blocked routes.", summary.watchRoutes());
        } else {
            log.info("Ops audit found no blocked or watch routes.");
        }
    }
}
