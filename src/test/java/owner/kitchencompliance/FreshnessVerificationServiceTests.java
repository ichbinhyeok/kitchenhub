package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.data.SourceRecord;
import owner.kitchencompliance.data.SourceScopeType;
import owner.kitchencompliance.data.SourceTier;
import owner.kitchencompliance.ops.FreshnessVerificationService;
import owner.kitchencompliance.ops.SourceFreshnessService;

class FreshnessVerificationServiceTests {

    @Test
    void verifyIndexedRoutesFailsWhenAnyIndexedRouteHasStaleSources() {
        SeedRegistry seedRegistry = mock(SeedRegistry.class);
        FreshnessVerificationService freshnessVerificationService = new FreshnessVerificationService(
                seedRegistry,
                new SourceFreshnessService(java.time.Clock.systemDefaultZone())
        );
        RouteRecord route = new RouteRecord(
                "/nc/charlotte/restaurant-grease-trap-rules",
                RouteTemplate.FOG_RULES,
                "nc",
                "charlotte",
                "charlotte-water-flow-free",
                "charlotte-nc-kitchen-compliance",
                "/nc/charlotte/restaurant-grease-trap-rules",
                true,
                "Charlotte Water publishes a grease-trap policy and approved hauler list.",
                OffsetDateTime.parse("2026-04-07T15:00:00+09:00")
        );
        SourceRecord staleSource = new SourceRecord(
                "stale-source",
                SourceScopeType.FOG_RULE,
                "charlotte-fog-food-service",
                SourceTier.TIER_1,
                "Charlotte Water",
                "Stale source",
                "https://example.com/stale",
                "Stale source summary",
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 6)
        );

        when(seedRegistry.routes()).thenReturn(List.of(route));
        when(seedRegistry.sourcesFor(route)).thenReturn(List.of(staleSource));

        assertThatThrownBy(freshnessVerificationService::verifyIndexedRoutes)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Freshness gate failed for indexed routes")
                .hasMessageContaining("/nc/charlotte/restaurant-grease-trap-rules");
    }
}
