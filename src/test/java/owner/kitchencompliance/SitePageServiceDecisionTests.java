package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import owner.kitchencompliance.data.ApprovedHaulerMode;
import owner.kitchencompliance.data.AuthorityRecord;
import owner.kitchencompliance.data.AuthorityType;
import owner.kitchencompliance.data.CityComplianceProfile;
import owner.kitchencompliance.data.FogRuleRecord;
import owner.kitchencompliance.data.HoodRuleRecord;
import owner.kitchencompliance.data.InspectionPrepRecord;
import owner.kitchencompliance.data.InspectionType;
import owner.kitchencompliance.data.ListingMode;
import owner.kitchencompliance.data.ProviderRecord;
import owner.kitchencompliance.data.ProviderType;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.data.SourceRecord;
import owner.kitchencompliance.data.SourceScopeType;
import owner.kitchencompliance.data.SourceTier;
import owner.kitchencompliance.data.SponsorStatus;
import owner.kitchencompliance.data.VerificationStatus;
import owner.kitchencompliance.model.LocalPageViewModel;
import owner.kitchencompliance.model.ResolvedPage;
import owner.kitchencompliance.model.RoutingMode;
import owner.kitchencompliance.ops.IndexingPolicyService;
import owner.kitchencompliance.ops.SourceFreshnessService;
import owner.kitchencompliance.ops.SourceQualityAssessmentService;
import owner.kitchencompliance.rules.CityVerdictService;
import owner.kitchencompliance.rules.ProviderEvidenceService;
import owner.kitchencompliance.rules.ProviderRoutingDecisionService;
import owner.kitchencompliance.web.AttributionProperties;
import owner.kitchencompliance.web.AttributionService;
import owner.kitchencompliance.web.GuideCatalog;
import owner.kitchencompliance.web.InfoPageCatalog;
import owner.kitchencompliance.web.OperatorToolCatalog;
import owner.kitchencompliance.web.OperatorToolService;
import owner.kitchencompliance.web.SitePageService;
import owner.kitchencompliance.web.SiteProperties;

class SitePageServiceDecisionTests {

    @Test
    void fogPageUsesOfficialListCopyWhenAuthorityPublishesOne() {
        SitePageService service = createService(fogRule(ApprovedHaulerMode.OFFICIAL_LIST), List.of(), RouteTemplate.FOG_RULES);

        ResolvedPage resolvedPage = service.localPage("/tx/austin/restaurant-grease-trap-rules", null);
        LocalPageViewModel page = (LocalPageViewModel) resolvedPage.page();

        assertThat(page.officialListStatement()).contains("authority-backed hauler or preferred-pumper list");
        assertThat(page.meta().robots()).isEqualTo("index,follow");
        assertThat(page.canonicalPath()).isEqualTo("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules");
        assertThat(page.governanceHeading()).contains("Utility-owned");
    }

    @Test
    void fogPageFallsBackToOperatorVerificationCopyWhenNoOfficialListExists() {
        SitePageService service = createService(fogRule(ApprovedHaulerMode.OPERATOR_MUST_VERIFY), List.of(), RouteTemplate.FOG_RULES);

        ResolvedPage resolvedPage = service.localPage("/tx/austin/restaurant-grease-trap-rules", null);
        LocalPageViewModel page = (LocalPageViewModel) resolvedPage.page();

        assertThat(page.officialListStatement()).contains("operator must verify");
    }

    @Test
    void weakCoverageFinderStaysManualOnlyAndNoindex() {
        ProviderRecord heldProvider = new ProviderRecord(
                "pending-provider",
                "Pending provider",
                ProviderType.HOOD_CLEANER,
                List.of("austin-tx-kitchen-compliance"),
                ListingMode.SPONSOR_ONLY,
                SponsorStatus.HOLD,
                "https://not-yet-vetted.invalid/provider",
                "coverage-review@kitchenrulehub.local",
                "000-000-0000",
                "",
                "Hold until vetted"
        );
        SitePageService service = createService(fogRule(ApprovedHaulerMode.OFFICIAL_LIST), List.of(heldProvider), RouteTemplate.FIND_HOOD_CLEANER);

        ResolvedPage resolvedPage = service.localPage("/tx/austin/find-hood-cleaner", null);
        LocalPageViewModel page = (LocalPageViewModel) resolvedPage.page();

        assertThat(page.meta().robots()).isEqualTo("noindex,follow");
        assertThat(page.routingDecision().routingMode()).isEqualTo(RoutingMode.MANUAL_ONLY);
        assertThat(page.providers()).isEmpty();
        assertThat(page.providerModeSummary()).contains("Operator review is still required");
    }

    @Test
    void finderBelowMinimumPublicCoverageThresholdStaysNoindex() {
        ProviderRecord publicOne = new ProviderRecord(
                "public-one",
                "Public One",
                ProviderType.HOOD_CLEANER,
                List.of("austin-tx-kitchen-compliance"),
                ListingMode.PUBLIC,
                SponsorStatus.ACTIVE,
                "https://example.com/public-one",
                "one@example.com",
                "111-111-1111",
                "",
                "Public coverage"
        );
        ProviderRecord publicTwo = new ProviderRecord(
                "public-two",
                "Public Two",
                ProviderType.HOOD_CLEANER,
                List.of("austin-tx-kitchen-compliance"),
                ListingMode.PUBLIC,
                SponsorStatus.ACTIVE,
                "https://example.com/public-two",
                "two@example.com",
                "222-222-2222",
                "",
                "Public coverage"
        );
        SitePageService service = createService(fogRule(ApprovedHaulerMode.OFFICIAL_LIST), List.of(publicOne, publicTwo), RouteTemplate.FIND_HOOD_CLEANER);

        ResolvedPage resolvedPage = service.localPage("/tx/austin/find-hood-cleaner", null);
        LocalPageViewModel page = (LocalPageViewModel) resolvedPage.page();

        assertThat(page.meta().robots()).isEqualTo("noindex,follow");
        assertThat(page.providers()).hasSize(2);
        assertThat(page.providerModeSummary()).contains("booking threshold of 3");
    }

    @Test
    void finderOrdersProvidersByEvidenceQuality() {
        ProviderRecord publicWithoutOfficial = new ProviderRecord(
                "public-no-official",
                "Public No Official",
                ProviderType.GREASE_HAULER,
                List.of("austin-tx-kitchen-compliance"),
                ListingMode.PUBLIC,
                SponsorStatus.ACTIVE,
                "https://example.com/public",
                "public@example.com",
                "111-111-1111",
                "",
                "Public site only"
        );
        ProviderRecord publicWithOfficial = new ProviderRecord(
                "public-with-official",
                "Public With Official",
                ProviderType.GREASE_HAULER,
                List.of("austin-tx-kitchen-compliance"),
                ListingMode.PUBLIC,
                SponsorStatus.ACTIVE,
                "https://example.com/official",
                "official@example.com",
                "222-222-2222",
                "https://example.com/city-list",
                "Authority source cited"
        );

        SitePageService service = createService(
                fogRule(ApprovedHaulerMode.OFFICIAL_LIST),
                List.of(publicWithoutOfficial, publicWithOfficial),
                RouteTemplate.FIND_GREASE_SERVICE
        );

        ResolvedPage resolvedPage = service.localPage("/tx/austin/find-grease-service", null);
        LocalPageViewModel page = (LocalPageViewModel) resolvedPage.page();

        assertThat(page.providers()).extracting(provider -> provider.providerName())
                .containsExactly("Public With Official", "Public No Official");
        assertThat(page.providers().getFirst().evidenceLabel()).isEqualTo("Authority-backed public contact");
    }

    @Test
    void providerFinderIncludesOperatorLeadAndSponsorPanels() {
        SitePageService service = createService(fogRule(ApprovedHaulerMode.OFFICIAL_LIST), List.of(), RouteTemplate.FIND_GREASE_SERVICE);

        ResolvedPage resolvedPage = service.localPage("/tx/austin/find-grease-service", "operator-submitted");
        LocalPageViewModel page = (LocalPageViewModel) resolvedPage.page();

        assertThat(page.operatorLeadPanel()).isNotNull();
        assertThat(page.prioritizeOperatorLeadPanel()).isFalse();
        assertThat(page.sponsorPanel()).isNotNull();
        assertThat(page.submissionNotice().title()).contains("saved");
    }

    @Test
    void rulePagesExposeSponsorPanelOnly() {
        SitePageService service = createService(fogRule(ApprovedHaulerMode.OFFICIAL_LIST), List.of(), RouteTemplate.FOG_RULES);

        ResolvedPage resolvedPage = service.localPage("/tx/austin/restaurant-grease-trap-rules", null);
        LocalPageViewModel page = (LocalPageViewModel) resolvedPage.page();

        assertThat(page.operatorLeadPanel()).isNull();
        assertThat(page.sponsorPanel()).isNotNull();
    }

    @Test
    void localRulePagesBuildSearchFocusedTitlesDescriptionsAndSummaries() {
        SitePageService service = createService(fogRule(ApprovedHaulerMode.OFFICIAL_LIST), List.of(), RouteTemplate.FOG_RULES);

        LocalPageViewModel fogPage = (LocalPageViewModel) service.localPage("/tx/austin/restaurant-grease-trap-rules", null).page();
        LocalPageViewModel hoodPage = (LocalPageViewModel) service.localPage("/authority/tx/austin-fire-marshal/hood-cleaning-requirements", null).page();

        assertThat(fogPage.meta().title()).isEqualTo("Austin, TX Grease Trap Rules for Restaurants | Pump-Outs & Manifests");
        assertThat(fogPage.meta().description()).contains("interceptor approval, pump-out timing, manifests to keep on site");
        assertThat(fogPage.summary()).contains("grease trap rules for restaurants");

        assertThat(hoodPage.meta().title()).isEqualTo("Austin, TX Hood Cleaning Requirements | Reports & Inspection Prep");
        assertThat(hoodPage.meta().description()).contains("service reports, tags, and inspection-ready paperwork");
        assertThat(hoodPage.summary()).contains("hood cleaning requirements for restaurants");
    }

    private SitePageService createService(FogRuleRecord fogRule, List<ProviderRecord> providers, RouteTemplate primaryTemplate) {
        SeedRegistry seedRegistry = mock(SeedRegistry.class);
        GuideCatalog guideCatalog = mock(GuideCatalog.class);
        InfoPageCatalog infoPageCatalog = new InfoPageCatalog();
        SiteProperties siteProperties = new SiteProperties("http://localhost:8080", "KitchenRuleHub", "tx", "G-K0NZM8LCFF");
        IndexingPolicyService indexingPolicyService = new IndexingPolicyService(
                new SourceFreshnessService(Clock.fixed(Instant.parse("2026-04-07T00:00:00Z"), java.time.ZoneOffset.UTC)),
                new SourceQualityAssessmentService(seedRegistry),
                seedRegistry
        );
        ProviderEvidenceService providerEvidenceService = new ProviderEvidenceService();
        ProviderRoutingDecisionService providerRoutingDecisionService = new ProviderRoutingDecisionService(indexingPolicyService, providerEvidenceService);
        CityVerdictService cityVerdictService = new CityVerdictService();
        ObjectMapper objectMapper = new ObjectMapper();
        AttributionService attributionService = new AttributionService(
                new AttributionProperties(false, "target/test-attribution"),
                Clock.fixed(Instant.parse("2026-04-07T00:00:00Z"), java.time.ZoneOffset.UTC),
                seedRegistry,
                indexingPolicyService
        );
        OperatorToolService operatorToolService = new OperatorToolService(siteProperties, new OperatorToolCatalog());

        CityComplianceProfile profile = new CityComplianceProfile(
                "austin-tx-kitchen-compliance",
                "austin",
                "tx",
                "austin-water-pretreatment",
                "austin-fire-marshal",
                1,
                true,
                "strong official coverage"
        );
        AuthorityRecord waterAuthority = new AuthorityRecord(
                "austin-water-pretreatment",
                AuthorityType.UTILITY,
                "Austin Water Pretreatment Program",
                "austin",
                "tx",
                "https://example.com/water",
                "https://example.com/water",
                LocalDate.of(2026, 4, 7),
                VerificationStatus.VERIFIED
        );
        AuthorityRecord fireAuthority = new AuthorityRecord(
                "austin-fire-marshal",
                AuthorityType.FIRE_AHJ,
                "Austin Fire Department Fire Marshal's Office",
                "austin",
                "tx",
                "https://example.com/fire",
                "https://example.com/fire",
                LocalDate.of(2026, 4, 7),
                VerificationStatus.VERIFIED
        );
        HoodRuleRecord hoodRule = new HoodRuleRecord(
                "austin-hood",
                "austin-fire-marshal",
                "austin",
                "tx",
                "hood-system",
                List.of(
                        "Semi-annual hood-system inspection in Austin's fire schedule.",
                        "Keep hood and suppression records separate."
                ),
                "Keep the latest report on site.",
                "Visible tag should match the service report.",
                "Retain the latest report on site.",
                "Suppression and hood systems are tracked separately.",
                List.of("src-fire"),
                LocalDate.of(2026, 4, 7)
        );
        InspectionPrepRecord inspectionPrep = new InspectionPrepRecord(
                "austin-fire-inspection",
                "austin",
                "tx",
                InspectionType.FIRE,
                List.of("Current hood-system report."),
                List.of("Expired report."),
                "Use the fire inspection request workflow.",
                "Follow-up will be required if proof is missing.",
                List.of("src-inspection")
        );
        SourceRecord source = new SourceRecord(
                "src-test",
                SourceScopeType.FOG_RULE,
                "austin",
                SourceTier.TIER_1,
                "Austin Water",
                "Test Source",
                "https://example.com/source",
                "Authority-backed summary.",
                LocalDate.of(2026, 4, 7),
                LocalDate.of(2026, 7, 7)
        );
        SourceRecord sourceTwo = new SourceRecord(
                "src-test-2",
                SourceScopeType.FOG_RULE,
                "austin",
                SourceTier.TIER_2,
                "Austin Fire",
                "Second Test Source",
                "https://example.com/source-two",
                "Official guide.",
                LocalDate.of(2026, 4, 7),
                LocalDate.of(2026, 8, 7)
        );

        RouteRecord fogRoute = route(RouteTemplate.FOG_RULES, "/tx/austin/restaurant-grease-trap-rules", "austin-water-pretreatment", true);
        RouteRecord approvedRoute = route(RouteTemplate.APPROVED_HAULERS, "/tx/austin/approved-grease-haulers", "austin-water-pretreatment", true);
        RouteRecord hoodRoute = route(RouteTemplate.HOOD_REQUIREMENTS, "/tx/austin/hood-cleaning-requirements", "austin-fire-marshal", true);
        RouteRecord inspectionRoute = route(RouteTemplate.INSPECTION_CHECKLIST, "/tx/austin/restaurant-fire-inspection-checklist", "austin-fire-marshal", true);
        RouteRecord greaseFinderRoute = route(RouteTemplate.FIND_GREASE_SERVICE, "/tx/austin/find-grease-service", "austin-water-pretreatment", true);
        RouteRecord hoodFinderRoute = route(RouteTemplate.FIND_HOOD_CLEANER, "/tx/austin/find-hood-cleaner", "austin-fire-marshal", true);

        when(seedRegistry.profile("austin-tx-kitchen-compliance")).thenReturn(profile);
        when(seedRegistry.fogRule("austin-tx-kitchen-compliance")).thenReturn(fogRule);
        when(seedRegistry.hoodRule("austin-tx-kitchen-compliance")).thenReturn(hoodRule);
        when(seedRegistry.inspectionPrep("austin-tx-kitchen-compliance")).thenReturn(inspectionPrep);
        when(seedRegistry.authority("austin-water-pretreatment")).thenReturn(waterAuthority);
        when(seedRegistry.authority("austin-fire-marshal")).thenReturn(fireAuthority);
        when(seedRegistry.providersFor(eq("austin-tx-kitchen-compliance"), any(ProviderType.class))).thenReturn(providers);
        when(seedRegistry.sourcesFor(any(RouteRecord.class))).thenReturn(List.of(source, sourceTwo));
        when(seedRegistry.lastVerifiedFor(any(RouteRecord.class))).thenReturn(LocalDate.of(2026, 4, 7));
        when(seedRegistry.routeFor(eq("austin-tx-kitchen-compliance"), any(RouteTemplate.class))).thenAnswer(invocation -> {
            RouteTemplate template = invocation.getArgument(1);
            return switch (template) {
                case FOG_RULES -> fogRoute;
                case APPROVED_HAULERS -> approvedRoute;
                case HOOD_REQUIREMENTS -> hoodRoute;
                case INSPECTION_CHECKLIST -> inspectionRoute;
                case FIND_GREASE_SERVICE -> greaseFinderRoute;
                case FIND_HOOD_CLEANER -> hoodFinderRoute;
            };
        });
        when(seedRegistry.route("/tx/austin/restaurant-grease-trap-rules")).thenReturn(fogRoute);
        when(seedRegistry.route("/authority/tx/austin-fire-marshal/hood-cleaning-requirements")).thenReturn(hoodRoute);
        when(seedRegistry.route("/tx/austin/find-grease-service")).thenReturn(greaseFinderRoute);
        when(seedRegistry.route("/tx/austin/find-hood-cleaner")).thenReturn(hoodFinderRoute);
        when(seedRegistry.canonicalPath(any(RouteRecord.class))).thenAnswer(invocation -> {
            RouteRecord route = invocation.getArgument(0);
            return "/authority/" + route.state() + "/" + route.authorityId() + "/" + route.path().substring(route.path().lastIndexOf('/') + 1);
        });
        when(seedRegistry.usesAuthorityCanonical(any(RouteRecord.class))).thenReturn(true);

        return new SitePageService(
                seedRegistry,
                cityVerdictService,
                providerRoutingDecisionService,
                indexingPolicyService,
                siteProperties,
                guideCatalog,
                infoPageCatalog,
                objectMapper,
                attributionService,
                providerEvidenceService,
                operatorToolService
        );
    }

    private RouteRecord route(RouteTemplate template, String path, String authorityId, boolean indexable) {
        return new RouteRecord(
                path,
                template,
                "tx",
                "austin",
                authorityId,
                "austin-tx-kitchen-compliance",
                path,
                indexable,
                "test route",
                null,
                List.of(),
                null,
                OffsetDateTime.parse("2026-04-07T15:00:00+09:00")
        );
    }

    private FogRuleRecord fogRule(ApprovedHaulerMode mode) {
        return new FogRuleRecord(
                "austin-fog",
                "austin-water-pretreatment",
                "austin",
                "tx",
                "Austin food businesses need an approved interceptor.",
                "Approved interceptor type.",
                "Quarterly or sooner at the 50 percent trigger.",
                "Keep manifests for up to three years.",
                mode,
                "Coordinate through Austin Water.",
                "Pretreatment escalation can follow weak maintenance records.",
                List.of("src-test"),
                LocalDate.of(2026, 4, 7)
        );
    }
}
