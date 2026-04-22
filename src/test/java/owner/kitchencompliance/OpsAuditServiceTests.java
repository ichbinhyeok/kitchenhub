package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.ops.OpsAuditService;
import owner.kitchencompliance.ops.OpsProperties;
import owner.kitchencompliance.ops.SearchDemandAssessmentService;
import owner.kitchencompliance.ops.SourceFreshnessService;
import owner.kitchencompliance.ops.DeployReadinessAssessmentService;
import owner.kitchencompliance.web.AttributionProperties;
import owner.kitchencompliance.web.AttributionReportService;
import owner.kitchencompliance.web.DeployReadinessReportService;
import owner.kitchencompliance.web.FreshnessReportService;
import owner.kitchencompliance.web.NoindexPromotionReportService;
import owner.kitchencompliance.web.OpsAlertService;
import owner.kitchencompliance.web.SearchDemandReportService;
import owner.kitchencompliance.web.SourceQualityReportService;
import owner.kitchencompliance.ops.SourceQualityAssessmentService;
import owner.kitchencompliance.web.SourceEvidenceSnapshotService;

@SpringBootTest
class OpsAuditServiceTests {

    @Autowired
    private SeedRegistry seedRegistry;

    @Test
    void writesLatestAndDatedOpsSnapshots() throws Exception {
        Path tempDir = Files.createTempDirectory("kch-ops-audit");
        Clock fixedClock = Clock.fixed(Instant.parse("2026-04-07T00:00:00Z"), ZoneId.of("UTC"));
        FreshnessReportService freshnessReportService = new FreshnessReportService(
                seedRegistry,
                new SourceFreshnessService(fixedClock),
                fixedClock
        );
        AttributionReportService attributionReportService = new AttributionReportService(
                new AttributionProperties(false, tempDir.resolve("attribution").toString())
        );
        SourceQualityReportService sourceQualityReportService = new SourceQualityReportService(
                new SourceQualityAssessmentService(seedRegistry)
        );
        DeployReadinessReportService deployReadinessReportService = new DeployReadinessReportService(
                new DeployReadinessAssessmentService(
                        seedRegistry,
                        new SourceFreshnessService(fixedClock),
                        new SourceQualityAssessmentService(seedRegistry),
                        new owner.kitchencompliance.ops.IndexingPolicyService(
                                new SourceFreshnessService(fixedClock),
                                new SourceQualityAssessmentService(seedRegistry),
                                seedRegistry
                        ),
                        fixedClock
                )
        );
        NoindexPromotionReportService noindexPromotionReportService = new NoindexPromotionReportService(
                new owner.kitchencompliance.ops.NoindexPromotionAssessmentService(
                        seedRegistry,
                        new SourceFreshnessService(fixedClock),
                        new SourceQualityAssessmentService(seedRegistry),
                        new owner.kitchencompliance.ops.IndexingPolicyService(
                                new SourceFreshnessService(fixedClock),
                                new SourceQualityAssessmentService(seedRegistry),
                                seedRegistry
                        ),
                        fixedClock
                ),
                seedRegistry,
                new SearchDemandAssessmentService(seedRegistry),
                new SearchDemandReportService(new SearchDemandAssessmentService(seedRegistry))
        );
        SearchDemandReportService searchDemandReportService = new SearchDemandReportService(new SearchDemandAssessmentService(seedRegistry));
        SourceEvidenceSnapshotService sourceEvidenceSnapshotService = new SourceEvidenceSnapshotService(
                seedRegistry,
                new DeployReadinessAssessmentService(
                        seedRegistry,
                        new SourceFreshnessService(fixedClock),
                        new SourceQualityAssessmentService(seedRegistry),
                        new owner.kitchencompliance.ops.IndexingPolicyService(
                                new SourceFreshnessService(fixedClock),
                                new SourceQualityAssessmentService(seedRegistry),
                                seedRegistry
                        ),
                        fixedClock
                ),
                fixedClock
        );
        OpsAlertService opsAlertService = new OpsAlertService(
                new DeployReadinessAssessmentService(
                        seedRegistry,
                        new SourceFreshnessService(fixedClock),
                        new SourceQualityAssessmentService(seedRegistry),
                        new owner.kitchencompliance.ops.IndexingPolicyService(
                                new SourceFreshnessService(fixedClock),
                                new SourceQualityAssessmentService(seedRegistry),
                                seedRegistry
                        ),
                        fixedClock
                ),
                fixedClock
        );
        OpsAuditService service = new OpsAuditService(
                new OpsProperties(tempDir.toString(), true, "0 15 6 * * *", "UTC"),
                freshnessReportService,
                attributionReportService,
                sourceQualityReportService,
                deployReadinessReportService,
                noindexPromotionReportService,
                searchDemandReportService,
                sourceEvidenceSnapshotService,
                opsAlertService,
                fixedClock
        );

        service.writeSnapshot();

        assertThat(Files.readString(tempDir.resolve("freshness-watch-latest.csv"))).contains("city,page,path,status,next_review_on,note");
        assertThat(Files.readString(tempDir.resolve("freshness-watch-2026-04-07.csv")))
                .contains("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules");
        assertThat(Files.readString(tempDir.resolve("source-quality-watch-latest.csv"))).contains("city,page,path,status,total_sources,strong_sources,note");
        assertThat(Files.readString(tempDir.resolve("source-quality-watch-2026-04-07.csv"))).contains("/fl/miami/restaurant-grease-trap-rules");
        assertThat(Files.readString(tempDir.resolve("deploy-readiness-latest.csv"))).contains("city,page,path,status,indexable_now,next_review_on,total_sources,strong_sources,renderable_providers,authority_backed_providers,note");
        assertThat(Files.readString(tempDir.resolve("deploy-readiness-2026-04-07.csv")))
                .contains("/authority/tx/austin-fire-marshal/find-hood-cleaner");
        assertThat(Files.readString(tempDir.resolve("noindex-promotion-queue-latest.csv"))).contains("city,page,path,status,ready_to_promote,next_review_on,reason,total_sources,strong_sources,renderable_providers,authority_backed_providers,promotion_checklist");
        assertThat(Files.readString(tempDir.resolve("noindex-promotion-queue-2026-04-07.csv"))).contains("promotion_checklist");
        assertThat(Files.readString(tempDir.resolve("search-demand-watch-latest.csv"))).contains("top_query,impressions_28d,clicks_28d,ctr,average_position");
        assertThat(Files.readString(tempDir.resolve("search-demand-watch-2026-04-07.csv"))).contains("/authority/tx/austin-water-pretreatment/find-grease-service");
        assertThat(Files.readString(tempDir.resolve("evidence-index-latest.csv"))).contains("snapshot_file");
        assertThat(Files.readString(tempDir.resolve("evidence-index-2026-04-07.csv")))
                .contains("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules");
        assertThat(Files.readString(tempDir.resolve("ops-alerts-latest.md"))).contains("# Ops Alert Snapshot");
        assertThat(Files.readString(tempDir.resolve("ops-alerts-2026-04-07.md"))).contains("Blocked routes");
        assertThat(Files.readString(tempDir.resolve("source-evidence").resolve("latest").resolve("authority-tx-austin-water-pretreatment-restaurant-grease-trap-rules.md")))
                .contains("## Sources");
        assertThat(Files.readString(tempDir.resolve("source-evidence").resolve("2026-04-07").resolve("authority-tx-austin-water-pretreatment-restaurant-grease-trap-rules.md")))
                .contains("Deploy status");
        assertThat(Files.readString(tempDir.resolve("attribution-summary-latest.csv"))).contains("city,state,page_family,event_type,verdict_state,destination,count");
        assertThat(Files.readString(tempDir.resolve("attribution-summary-2026-04-07.csv"))).contains("city,state,page_family,event_type,verdict_state,destination,count");
    }
}
