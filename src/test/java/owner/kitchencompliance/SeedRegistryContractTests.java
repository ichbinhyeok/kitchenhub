package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import owner.kitchencompliance.data.ProviderType;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.SeedRegistry;

@SpringBootTest
class SeedRegistryContractTests {

    @Autowired
    private SeedRegistry seedRegistry;

    @Test
    void loadsEightCityLiveSurfaceFromJsonContracts() {
        List<String> paths = seedRegistry.routes().stream()
                .map(RouteRecord::path)
                .sorted()
                .toList();

        assertThat(paths).containsExactly(
                "/ca/santa-clara/approved-grease-haulers",
                "/ca/santa-clara/find-grease-service",
                "/ca/santa-clara/find-hood-cleaner",
                "/ca/santa-clara/hood-cleaning-requirements",
                "/ca/santa-clara/restaurant-fire-inspection-checklist",
                "/ca/santa-clara/restaurant-grease-trap-rules",
                "/fl/miami/approved-grease-haulers",
                "/fl/miami/find-grease-service",
                "/fl/miami/find-hood-cleaner",
                "/fl/miami/hood-cleaning-requirements",
                "/fl/miami/restaurant-fire-inspection-checklist",
                "/fl/miami/restaurant-grease-trap-rules",
                "/fl/tampa/approved-grease-haulers",
                "/fl/tampa/find-grease-service",
                "/fl/tampa/find-hood-cleaner",
                "/fl/tampa/hood-cleaning-requirements",
                "/fl/tampa/restaurant-fire-inspection-checklist",
                "/fl/tampa/restaurant-grease-trap-rules",
                "/nc/charlotte/approved-grease-haulers",
                "/nc/charlotte/find-grease-service",
                "/nc/charlotte/find-hood-cleaner",
                "/nc/charlotte/hood-cleaning-requirements",
                "/nc/charlotte/restaurant-fire-inspection-checklist",
                "/nc/charlotte/restaurant-grease-trap-rules",
                "/ne/grand-island/approved-grease-haulers",
                "/ne/grand-island/find-grease-service",
                "/ne/grand-island/find-hood-cleaner",
                "/ne/grand-island/hood-cleaning-requirements",
                "/ne/grand-island/restaurant-fire-inspection-checklist",
                "/ne/grand-island/restaurant-grease-trap-rules",
                "/or/portland/approved-grease-haulers",
                "/or/portland/find-grease-service",
                "/or/portland/find-hood-cleaner",
                "/or/portland/hood-cleaning-requirements",
                "/or/portland/restaurant-fire-inspection-checklist",
                "/or/portland/restaurant-grease-trap-rules",
                "/tn/nashville/approved-grease-haulers",
                "/tn/nashville/find-grease-service",
                "/tn/nashville/find-hood-cleaner",
                "/tn/nashville/hood-cleaning-requirements",
                "/tn/nashville/restaurant-fire-inspection-checklist",
                "/tn/nashville/restaurant-grease-trap-rules",
                "/tx/austin/approved-grease-haulers",
                "/tx/austin/find-grease-service",
                "/tx/austin/find-hood-cleaner",
                "/tx/austin/hood-cleaning-requirements",
                "/tx/austin/restaurant-fire-inspection-checklist",
                "/tx/austin/restaurant-grease-trap-rules"
        );
    }

    @Test
    void indexedRoutesHaveSourceCoverageAndLastVerifiedDates() {
        List<RouteRecord> indexedRoutes = seedRegistry.routes().stream()
                .filter(RouteRecord::indexable)
                .toList();

        assertThat(indexedRoutes).hasSize(45);
        assertThat(indexedRoutes)
                .allSatisfy(route -> {
                    assertThat(seedRegistry.sourcesFor(route)).isNotEmpty();
                    assertThat(seedRegistry.lastVerifiedFor(route)).isNotNull();
                });
    }

    @Test
    void authorityCanonicalAliasesResolveForMixedGovernanceRoutes() {
        RouteRecord route = seedRegistry.route("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules");

        assertThat(route.path()).isEqualTo("/tx/austin/restaurant-grease-trap-rules");
        assertThat(seedRegistry.canonicalPath(route)).isEqualTo("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules");
        assertThat(seedRegistry.usesAuthorityCanonical(route)).isTrue();
    }

    @Test
    void loadsEightCityProfilesFromJsonContracts() {
        assertThat(seedRegistry.profiles()).hasSize(8);
    }

    @Test
    void eachLiveCityHasAtLeastThreePublicProvidersPerFinderCategory() {
        assertThat(seedRegistry.providersFor("austin-tx-kitchen-compliance", ProviderType.GREASE_HAULER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("austin-tx-kitchen-compliance", ProviderType.HOOD_CLEANER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("charlotte-nc-kitchen-compliance", ProviderType.GREASE_HAULER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("charlotte-nc-kitchen-compliance", ProviderType.HOOD_CLEANER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("tampa-fl-kitchen-compliance", ProviderType.GREASE_HAULER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("tampa-fl-kitchen-compliance", ProviderType.HOOD_CLEANER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("portland-or-kitchen-compliance", ProviderType.GREASE_HAULER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("portland-or-kitchen-compliance", ProviderType.HOOD_CLEANER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("santa-clara-ca-kitchen-compliance", ProviderType.GREASE_HAULER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("santa-clara-ca-kitchen-compliance", ProviderType.HOOD_CLEANER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("nashville-tn-kitchen-compliance", ProviderType.GREASE_HAULER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("nashville-tn-kitchen-compliance", ProviderType.HOOD_CLEANER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("grand-island-ne-kitchen-compliance", ProviderType.GREASE_HAULER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("grand-island-ne-kitchen-compliance", ProviderType.HOOD_CLEANER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("miami-fl-kitchen-compliance", ProviderType.GREASE_HAULER)).hasSizeGreaterThanOrEqualTo(3);
        assertThat(seedRegistry.providersFor("miami-fl-kitchen-compliance", ProviderType.HOOD_CLEANER)).hasSizeGreaterThanOrEqualTo(3);
    }
}
