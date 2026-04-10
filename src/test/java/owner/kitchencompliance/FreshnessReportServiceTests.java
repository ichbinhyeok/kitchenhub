package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.ops.SourceFreshnessService;
import owner.kitchencompliance.web.FreshnessReportService;

@SpringBootTest
class FreshnessReportServiceTests {

    @Autowired
    private SeedRegistry seedRegistry;

    @Test
    void dashboardSummarizesIndexedRoutesAndWatchRows() {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-04-07T00:00:00Z"), ZoneId.of("Asia/Seoul"));
        FreshnessReportService reportService = new FreshnessReportService(
                seedRegistry,
                new SourceFreshnessService(fixedClock),
                fixedClock
        );

        var dashboard = reportService.readDashboard();

        assertThat(dashboard.indexedRoutes()).isEqualTo(48);
        assertThat(dashboard.staleRoutes()).isZero();
        assertThat(dashboard.dueSoonRoutes()).isPositive();
        assertThat(dashboard.freshRoutes() + dashboard.dueSoonRoutes()).isEqualTo(48);
        assertThat(dashboard.nextReviewDue()).isNotBlank();
        assertThat(dashboard.watchRows()).isNotEmpty();
        assertThat(dashboard.watchRows())
                .anySatisfy(row -> {
                    assertThat(row.statusLabel()).isIn("Review soon", "Fresh");
                    assertThat(row.note()).isNotBlank();
                });
    }
}
