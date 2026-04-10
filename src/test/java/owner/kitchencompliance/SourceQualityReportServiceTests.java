package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import owner.kitchencompliance.web.SourceQualityReportService;

@SpringBootTest
class SourceQualityReportServiceTests {

    @Autowired
    private SourceQualityReportService sourceQualityReportService;

    @Test
    void dashboardSummarizesIndexedRouteSourceDepth() {
        var dashboard = sourceQualityReportService.readDashboard();

        assertThat(dashboard.indexedRoutes()).isEqualTo(48);
        assertThat(dashboard.criticalRoutes()).isZero();
        assertThat(dashboard.strongRoutes()).isPositive();
        assertThat(dashboard.strongRoutes() + dashboard.adequateRoutes() + dashboard.thinRoutes() + dashboard.criticalRoutes())
                .isEqualTo(48);
        assertThat(dashboard.watchRows()).isNotEmpty();
        assertThat(dashboard.watchRows()).allSatisfy(row -> {
            assertThat(row.statusLabel()).isIn("Critical", "Thin", "Adequate", "Strong");
            assertThat(row.totalSources()).isGreaterThanOrEqualTo(2);
            assertThat(row.strongSources()).isPositive();
            assertThat(row.note()).isNotBlank();
        });
    }
}
