package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import owner.kitchencompliance.web.DeployReadinessReportService;

@SpringBootTest
class DeployReadinessReportServiceTests {

    @Autowired
    private DeployReadinessReportService deployReadinessReportService;

    @Test
    void dashboardSummarizesIndexedRouteDeployReadiness() {
        var dashboard = deployReadinessReportService.readDashboard();

        assertThat(dashboard.indexedRoutes()).isEqualTo(48);
        assertThat(dashboard.blockedRoutes()).isZero();
        assertThat(dashboard.readyRoutes() + dashboard.watchRoutes() + dashboard.blockedRoutes()).isEqualTo(48);
        assertThat(dashboard.watchRows()).isNotEmpty();
        assertThat(dashboard.watchRows()).allSatisfy(row -> {
            assertThat(row.statusLabel()).isIn("Blocked", "Watch", "Ready");
            assertThat(row.indexableLabel()).isIn("Indexable", "Noindex now");
            assertThat(row.totalSources()).isGreaterThanOrEqualTo(2);
            assertThat(row.strongSources()).isPositive();
            assertThat(row.note()).isNotBlank();
        });
    }
}
