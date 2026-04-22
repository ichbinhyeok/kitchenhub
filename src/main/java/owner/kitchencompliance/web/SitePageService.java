package owner.kitchencompliance.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.ApprovedHaulerMode;
import owner.kitchencompliance.data.AuthorityRecord;
import owner.kitchencompliance.data.AuthorityType;
import owner.kitchencompliance.data.CityComplianceProfile;
import owner.kitchencompliance.data.FogRuleRecord;
import owner.kitchencompliance.data.HoodRuleRecord;
import owner.kitchencompliance.data.InspectionPrepRecord;
import owner.kitchencompliance.data.ProviderRecord;
import owner.kitchencompliance.data.ProviderType;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.data.SourceRecord;
import owner.kitchencompliance.model.AuthorityRouteLink;
import owner.kitchencompliance.model.CallToAction;
import owner.kitchencompliance.model.CityCard;
import owner.kitchencompliance.model.CityVerdict;
import owner.kitchencompliance.model.GuidePageViewModel;
import owner.kitchencompliance.model.GuideSection;
import owner.kitchencompliance.model.AuthorityBrowseCard;
import owner.kitchencompliance.model.AuthorityBrowseFilterOption;
import owner.kitchencompliance.model.AuthorityBrowsePageViewModel;
import owner.kitchencompliance.model.AuthorityBrowseRouteLink;
import owner.kitchencompliance.model.AuthorityBrowseSection;
import owner.kitchencompliance.model.AuthorityBrowseStateJump;
import owner.kitchencompliance.model.HomeIssueCard;
import owner.kitchencompliance.model.HomePanelLink;
import owner.kitchencompliance.model.HomePageViewModel;
import owner.kitchencompliance.model.InfoPageViewModel;
import owner.kitchencompliance.model.LeadCapturePanel;
import owner.kitchencompliance.model.LocalPageViewModel;
import owner.kitchencompliance.model.PageMeta;
import owner.kitchencompliance.model.ProviderCard;
import owner.kitchencompliance.model.ProviderRoutingDecision;
import owner.kitchencompliance.model.RelatedPageLink;
import owner.kitchencompliance.model.ResolvedPage;
import owner.kitchencompliance.model.SitemapEntry;
import owner.kitchencompliance.model.SourceAttribution;
import owner.kitchencompliance.model.SubmissionNotice;
import owner.kitchencompliance.ops.IndexingPolicyService;
import owner.kitchencompliance.rules.CityVerdictService;
import owner.kitchencompliance.rules.ProviderEvidenceService;
import owner.kitchencompliance.rules.ProviderRoutingDecisionService;

@Service
public class SitePageService {

    private final SeedRegistry seedRegistry;
    private final CityVerdictService cityVerdictService;
    private final ProviderRoutingDecisionService providerRoutingDecisionService;
    private final IndexingPolicyService indexingPolicyService;
    private final SiteProperties siteProperties;
    private final GuideCatalog guideCatalog;
    private final InfoPageCatalog infoPageCatalog;
    private final ObjectMapper objectMapper;
    private final AttributionService attributionService;
    private final ProviderEvidenceService providerEvidenceService;
    private final OperatorToolService operatorToolService;

    public SitePageService(
            SeedRegistry seedRegistry,
            CityVerdictService cityVerdictService,
            ProviderRoutingDecisionService providerRoutingDecisionService,
            IndexingPolicyService indexingPolicyService,
            SiteProperties siteProperties,
            GuideCatalog guideCatalog,
            InfoPageCatalog infoPageCatalog,
            ObjectMapper objectMapper,
            AttributionService attributionService,
            ProviderEvidenceService providerEvidenceService,
            OperatorToolService operatorToolService
    ) {
        this.seedRegistry = seedRegistry;
        this.cityVerdictService = cityVerdictService;
        this.providerRoutingDecisionService = providerRoutingDecisionService;
        this.indexingPolicyService = indexingPolicyService;
        this.siteProperties = siteProperties;
        this.guideCatalog = guideCatalog;
        this.infoPageCatalog = infoPageCatalog;
        this.objectMapper = objectMapper;
        this.attributionService = attributionService;
        this.providerEvidenceService = providerEvidenceService;
        this.operatorToolService = operatorToolService;
    }

    public HomePageViewModel homePage() {
        Map<String, Integer> cityOrder = Map.of(
                "Austin", 0,
                "Charlotte", 1,
                "Miami", 2,
                "Tampa", 3,
                "Portland", 4,
                "Santa Clara", 5,
                "Nashville", 6,
                "Grand Island", 7
        );
        List<CityCard> cityCards = seedRegistry.profiles().stream()
                .map(profile -> new CityCard(
                        displayCity(profile.city()),
                        profile.state().toUpperCase(),
                        profile.homeSummary(),
                        List.of(
                                authorityRouteLink(profile.profileId(), RouteTemplate.FOG_RULES, "Grease rule holder"),
                                authorityRouteLink(profile.profileId(), RouteTemplate.HOOD_REQUIREMENTS, "Hood and fire rule holder")
                        ),
                        List.of(
                                link("Grease rules", seedRegistry.routeFor(profile.profileId(), RouteTemplate.FOG_RULES).path()),
                                link("Hood requirements", seedRegistry.routeFor(profile.profileId(), RouteTemplate.HOOD_REQUIREMENTS).path()),
                                link("Inspection checklist", seedRegistry.routeFor(profile.profileId(), RouteTemplate.INSPECTION_CHECKLIST).path())
                        )))
                .sorted((left, right) -> Integer.compare(
                        cityOrder.getOrDefault(left.city(), Integer.MAX_VALUE),
                        cityOrder.getOrDefault(right.city(), Integer.MAX_VALUE)
                ))
                .toList();
        List<HomeIssueCard> issueCards = List.of(
                issueCard(
                        "Grease, hauling, and manifests",
                        "Start from the city that matches the kitchen, then open the local grease rule page before you call for service.",
                        "Choose grease city",
                        "#active-cities",
                        "Read grease guide",
                        "/guides/fog-vs-grease-trap-cleaning",
                        "delete_outline"
                ),
                issueCard(
                        "Hood, suppression, and tags",
                        "Pick the city first, then use the hood requirement page to confirm reports, tags, and inspection timing.",
                        "Choose hood city",
                        "#active-cities",
                        "Read hood guide",
                        "/guides/how-often-clean-commercial-hood",
                        "local_fire_department"
                ),
                issueCard(
                        "Inspection-ready records",
                        "Choose the city first so the inspection checklist matches the local authority, paperwork, and common failures.",
                        "Choose inspection city",
                        "#active-cities",
                        "Read inspection guide",
                        "/guides/what-records-restaurant-inspections-check",
                        "fact_check"
                )
        );

        List<HomePanelLink> guideLinks = guideCatalog.allGuides().stream()
                .map(guide -> new HomePanelLink(
                        guide.title(),
                        guide.summary(),
                        "/guides/" + guide.slug(),
                        "menu_book"
                ))
                .toList();

        PageMeta meta = new PageMeta(
                "Commercial Kitchen Compliance by City | Grease, Hood, Inspections",
                "Local restaurant grease trap rules, hood cleaning requirements, fire inspection checklists, and next steps from official city sources.",
                canonicalUrl("/"),
                "index,follow",
                siteLatestVerifiedDate(),
                structuredDataJson(websiteStructuredData(siteProperties.title(), canonicalUrl("/")))
        );

        return new HomePageViewModel(
                meta,
                "Kitchen compliance with a local next action",
                "KitchenRuleHub helps commercial kitchen operators start with grease, hood, or inspection work, confirm the local rule holder, and then move into the next action without mixing provider copy into rule guidance.",
                List.of(
                        "Choose the local rule holder before you rely on a city summary or provider page.",
                        "Stage the records, tags, manifests, and checklists inspectors usually ask for on site.",
                        "Use guides and provider pages only after the local rule page tells you what work actually applies."
                ),
                issueCards,
                cityCards,
                guideLinks,
                operatorToolService.homeToolLinks()
        );
    }

    public ResolvedPage localPage(String path, String noticeCode) {
        RouteRecord route = seedRegistry.route(path);
        CityComplianceProfile profile = seedRegistry.profile(route.profileId());
        String cityName = displayCity(profile.city());
        AuthorityRecord authority = seedRegistry.authority(route.authorityId());
        FogRuleRecord fogRule = seedRegistry.fogRule(profile.profileId());
        HoodRuleRecord hoodRule = seedRegistry.hoodRule(profile.profileId());
        InspectionPrepRecord inspectionPrep = seedRegistry.inspectionPrep(profile.profileId());
        List<ProviderRecord> providers = providersFor(route, profile.profileId());
        List<SourceRecord> sources = seedRegistry.sourcesFor(route);
        boolean indexable = indexingPolicyService.isIndexable(route, sources, providers);
        List<SourceAttribution> sourceAttributions = sources.stream().map(this::toSourceAttribution).toList();

        CityVerdict verdict = cityVerdictService.create(
                route,
                profile,
                authority.authorityName() + " (" + authority.authorityType().label() + ")",
                fogRule,
                hoodRule,
                inspectionPrep
        );

        LocalPageViewModel page = switch (route.template()) {
            case FOG_RULES -> createFogRulesPage(path, route, profile, cityName, authority, fogRule, verdict, sourceAttributions, indexable, noticeCode);
            case APPROVED_HAULERS -> createApprovedHaulersPage(path, route, profile, cityName, authority, fogRule, verdict, sourceAttributions, indexable, noticeCode);
            case HOOD_REQUIREMENTS -> createHoodPage(path, route, profile, cityName, authority, hoodRule, verdict, sourceAttributions, indexable, noticeCode);
            case INSPECTION_CHECKLIST -> createInspectionPage(path, route, profile, cityName, authority, inspectionPrep, verdict, sourceAttributions, indexable, noticeCode);
            case FIND_GREASE_SERVICE, FIND_HOOD_CLEANER -> createProviderFinderPage(path, route, profile, cityName, authority, verdict, providers, sourceAttributions, indexable, noticeCode);
        };

        return new ResolvedPage(route.template().viewName(), page);
    }

    public GuidePageViewModel guidePage(String slug) {
        GuideCatalog.GuideDefinition guide = guideCatalog.guide(slug);
        PageMeta meta = new PageMeta(
                guide.title() + " | " + siteProperties.title(),
                guide.summary(),
                canonicalUrl("/guides/" + guide.slug()),
                "index,follow",
                guideLastVerifiedDate(guide),
                structuredDataJson(List.of(
                        articleStructuredData(guide.title(), canonicalUrl("/guides/" + guide.slug()), guide.summary()),
                        breadcrumbStructuredData(List.of(
                                breadcrumbItem("Home", canonicalUrl("/")),
                                breadcrumbItem(guide.title(), canonicalUrl("/guides/" + guide.slug()))
                        ))
                ))
        );
        return new GuidePageViewModel(
                meta,
                guide.title(),
                guide.summary(),
                guide.sections(),
                guide.authorityReferences().stream()
                        .map(reference -> authorityRouteLink(reference.profileId(), reference.template(), reference.title()))
                        .toList(),
                guide.relatedLinks()
        );
    }

    public InfoPageViewModel infoPage(String slug) {
        InfoPageCatalog.InfoPageDefinition page = infoPageCatalog.page(slug);
        PageMeta meta = new PageMeta(
                page.title() + " | " + siteProperties.title(),
                page.summary(),
                canonicalUrl("/" + page.slug()),
                page.robots(),
                null,
                structuredDataJson(List.of(
                        schemaObject(page.schemaType(), page.title(), canonicalUrl("/" + page.slug()), page.summary()),
                        breadcrumbStructuredData(List.of(
                                breadcrumbItem("Home", canonicalUrl("/")),
                                breadcrumbItem(page.title(), canonicalUrl("/" + page.slug()))
                        ))
                ))
        );
        return new InfoPageViewModel(meta, page.eyebrow(), page.title(), page.summary(), page.sections(), page.relatedLinks());
    }

    public AuthorityBrowsePageViewModel authorityIndexPage(String typeFilter) {
        AuthorityType activeType = parseAuthorityType(typeFilter);
        List<AuthorityBrowseCard> authorityCards = seedRegistry.authoritiesById().values().stream()
                .filter(authority -> activeType == null || authority.authorityType() == activeType)
                .sorted(Comparator.comparing(AuthorityRecord::state)
                        .thenComparing(AuthorityRecord::city)
                        .thenComparing(AuthorityRecord::authorityName))
                .map(this::authorityBrowseCard)
                .toList();
        List<AuthorityBrowseSection> sections = authoritySections(authorityCards);

        PageMeta meta = new PageMeta(
                (activeType == null ? "Rule holder directory" : activeType.label() + " offices") + " | " + siteProperties.title(),
                "Browse the utility, fire office, or local department that actually sets each rule before you rely on a city summary.",
                canonicalUrl("/authorities"),
                activeType == null ? "index,follow" : "noindex,follow",
                authorityDirectoryLastVerifiedDate(authorityCards),
                structuredDataJson(List.of(
                        collectionPageStructuredData(
                                "Rule holder directory",
                                canonicalUrl("/authorities"),
                                "Browse the actual local rule holder before acting on city-level kitchen compliance summaries."
                        ),
                        breadcrumbStructuredData(List.of(
                                breadcrumbItem("Home", canonicalUrl("/")),
                                breadcrumbItem("Authorities", canonicalUrl("/authorities"))
                        )),
                        authorityItemListStructuredData(canonicalUrl("/authorities"), authorityCards)
                ))
        );

        return new AuthorityBrowsePageViewModel(
                meta,
                "Rule holder directory",
                activeType == null ? "Browse by actual rule holder" : activeType.label() + " offices",
                activeType == null
                        ? "Use this directory when you need to see which utility, fire office, or department actually owns the rule behind a city page."
                        : "Filtered to " + activeType.label() + " offices so you can go straight to the rule holder you need.",
                false,
                authorityCards,
                authorityFilterOptions(activeType),
                stateJumps(sections),
                sections
        );
    }

    public AuthorityBrowsePageViewModel authorityDetailPage(String state, String authorityId) {
        AuthorityRecord authority = seedRegistry.authority(authorityId);
        if (!authority.state().equalsIgnoreCase(state)) {
            throw new IllegalArgumentException("Authority state mismatch for " + authorityId);
        }

        AuthorityBrowseCard authorityCard = authorityBrowseCard(authority);
        String path = "/authorities/" + authority.state() + "/" + authority.authorityId();
        PageMeta meta = new PageMeta(
                authority.authorityName() + " | Rule holder routes | " + siteProperties.title(),
                "Kitchen compliance routes and city links for " + authority.authorityName() + ".",
                canonicalUrl(path),
                "index,follow",
                authority.lastVerified(),
                structuredDataJson(List.of(
                        collectionPageStructuredData(
                                authority.authorityName() + " routes",
                                canonicalUrl(path),
                                "Browse authority-owned kitchen compliance routes for " + authority.authorityName() + "."
                        ),
                        breadcrumbStructuredData(List.of(
                                breadcrumbItem("Home", canonicalUrl("/")),
                                breadcrumbItem("Authorities", canonicalUrl("/authorities")),
                                breadcrumbItem(authority.authorityName(), canonicalUrl(path))
                        )),
                        authorityRouteItemListStructuredData(canonicalUrl(path), authorityCard.routeLinks())
                ))
        );

        return new AuthorityBrowsePageViewModel(
                meta,
                authority.authorityType().label(),
                authority.authorityName(),
                "Routes from " + authority.authorityName() + " for " + displayCity(authority.city()) + ", " + authority.state().toUpperCase()
                        + ". Use these when you need the office that actually sets the rule.",
                true,
                List.of(authorityCard),
                List.of(),
                List.of(),
                List.of(new AuthorityBrowseSection(
                        "state-" + authority.state().toLowerCase(),
                        authority.state().toUpperCase(),
                        1,
                        List.of(authorityCard)
                ))
        );
    }

    public List<SitemapEntry> sitemapEntries() {
        List<SitemapEntry> entries = new ArrayList<>();
        entries.add(new SitemapEntry(canonicalUrl("/"), siteLatestVerifiedDate(), "weekly", "1.0"));
        entries.add(new SitemapEntry(canonicalUrl("/authorities"), authorityDirectoryLastVerifiedDate(), "weekly", "0.7"));

        seedRegistry.authoritiesById().values().stream()
                .sorted(Comparator.comparing(AuthorityRecord::state)
                        .thenComparing(AuthorityRecord::authorityId))
                .forEach(authority -> entries.add(new SitemapEntry(
                        canonicalUrl("/authorities/" + authority.state() + "/" + authority.authorityId()),
                        authority.lastVerified(),
                        "monthly",
                        "0.5"
                )));

        for (RouteRecord route : seedRegistry.routes()) {
            boolean indexable = indexingPolicyService.isIndexable(route, seedRegistry.sourcesFor(route), providersFor(route, route.profileId()));
            if (indexable) {
                entries.add(new SitemapEntry(
                        canonicalUrl(seedRegistry.canonicalPath(route)),
                        seedRegistry.lastVerifiedFor(route),
                        "weekly",
                        "0.8"
                ));
            }
        }

        for (GuideCatalog.GuideDefinition guide : guideCatalog.allGuides()) {
            entries.add(new SitemapEntry(
                    canonicalUrl("/guides/" + guide.slug()),
                    guideLastVerifiedDate(guide),
                    "monthly",
                    "0.6"
            ));
        }

        for (InfoPageCatalog.InfoPageDefinition infoPage : infoPageCatalog.allPages()) {
            if (infoPage.robots().startsWith("index")) {
                entries.add(new SitemapEntry(canonicalUrl("/" + infoPage.slug()), null, "monthly", "0.4"));
            }
        }

        return List.copyOf(entries);
    }

    private LocalPageViewModel createFogRulesPage(
            String requestPath,
            RouteRecord route,
            CityComplianceProfile profile,
            String cityName,
            AuthorityRecord authority,
            FogRuleRecord fogRule,
            CityVerdict verdict,
            List<SourceAttribution> sources,
            boolean indexable,
            String noticeCode
    ) {
        return baseLocalPage(
                requestPath,
                route,
                profile,
                authority,
                indexable,
                seedRegistry.lastVerifiedFor(route),
                cityName + " FOG rules",
                cityName + " grease trap and interceptor rules",
                cityName + ", " + profile.state().toUpperCase()
                        + " grease trap rules for restaurants: interceptor approval, pump-out timing, manifests to keep on site, and hauler checks.",
                "Official requirement",
                List.of(
                        fogRule.foodServiceApplicability(),
                        authority.authorityName() + " approves the interceptor setup through plan review.",
                        fogRule.pumpOutFrequency()
                ),
                "Keep on site",
                List.of(
                        fogRule.manifestRequirement(),
                        "The interceptor approval letter or equivalent plan-review record.",
                        "A service history that explains why the current cadence is safe."
                ),
                "Inspection and enforcement risk",
                List.of(
                        fogRule.enforcementNote(),
                        "A missing manifest trail weakens every pump-out claim.",
                        "Overdue service or an unclear interceptor setup can push the issue back to the operator."
                ),
                "Next action",
                verdict.nextActions(),
                "Authority note",
                cityName + " publishes a clear local service cadence and verification workflow, so this page can stay specific without falling back to generic national advice.",
                fogRule.approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST
                        ? cityName + " publishes an official hauler or preferred-pumper list, but it does not recommend or endorse any provider on that list."
                        : cityName + " does not publish a safe approved list for this workflow, so the operator must verify the service company directly.",
                null,
                new CallToAction(
                        "Need a hauler check before the next pump-out?",
                        "Start with the city's official list and then confirm the service company still covers grease waste and manifest handling.",
                        "Review the " + cityName + " hauler workflow",
                        localRoutePath(profile.profileId(), RouteTemplate.APPROVED_HAULERS)
                ),
                null,
                submissionNotice(noticeCode),
                verdict,
                null,
                List.of(),
                relatedLinks(profile.profileId(), route.template()),
                operatorToolLinks(route.template()),
                sources,
                null,
                null,
                List.of(),
                true
        );
    }

    private LocalPageViewModel createApprovedHaulersPage(
            String requestPath,
            RouteRecord route,
            CityComplianceProfile profile,
            String cityName,
            AuthorityRecord authority,
            FogRuleRecord fogRule,
            CityVerdict verdict,
            List<SourceAttribution> sources,
            boolean indexable,
            String noticeCode
    ) {
        return baseLocalPage(
                requestPath,
                route,
                profile,
                authority,
                indexable,
                seedRegistry.lastVerifiedFor(route),
                cityName + " permitted haulers",
                cityName + " approved grease hauler workflow",
                fogRule.approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST
                        ? cityName + ", " + profile.state().toUpperCase()
                                + " grease hauler workflow: official list status, manifest rules, and what to verify before booking service."
                        : cityName + ", " + profile.state().toUpperCase()
                                + " grease hauler workflow: no official list, operator verification steps, and the paperwork to keep on site.",
                "Official list logic",
                verdict.whatAppliesNow(),
                "What to keep on site",
                verdict.whatToKeepOnSite(),
                "Where operators get exposed",
                verdict.whatFailsInspections(),
                "Next action",
                verdict.nextActions(),
                "Approval language rule",
                "This page uses approved language only because the city publishes a live hauler report. The language stays paired with the city's non-endorsement disclaimer.",
                fogRule.approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST
                        ? cityName + "'s list is a verification tool, not a recommendation list."
                        : "No official approved-hauler list is available for this city, so this page falls back to self-verification steps.",
                null,
                new CallToAction(
                        "Still need service help?",
                        "Move from the list check to an action page that tells staff what to confirm before booking.",
                        "Open grease service next steps",
                        localRoutePath(profile.profileId(), RouteTemplate.FIND_GREASE_SERVICE)
                ),
                null,
                submissionNotice(noticeCode),
                verdict,
                null,
                List.of(),
                relatedLinks(profile.profileId(), route.template()),
                operatorToolLinks(route.template()),
                sources,
                null,
                null,
                List.of(),
                true
        );
    }

    private LocalPageViewModel createHoodPage(
            String requestPath,
            RouteRecord route,
            CityComplianceProfile profile,
            String cityName,
            AuthorityRecord authority,
            HoodRuleRecord hoodRule,
            CityVerdict verdict,
            List<SourceAttribution> sources,
            boolean indexable,
            String noticeCode
    ) {
        return baseLocalPage(
                requestPath,
                route,
                profile,
                authority,
                indexable,
                seedRegistry.lastVerifiedFor(route),
                cityName + " hood requirements",
                cityName + " hood-system cleaning and inspection prep",
                cityName + ", " + profile.state().toUpperCase()
                        + " hood cleaning requirements for restaurants: service reports, tags, and inspection-ready paperwork before the next fire visit.",
                "What " + cityName + " publishes",
                verdict.whatAppliesNow(),
                "Keep on site",
                verdict.whatToKeepOnSite(),
                "Inspection risk",
                verdict.whatFailsInspections(),
                "Next action",
                verdict.nextActions(),
                "Separation rule",
                cityName + "'s cited fire documents support hood-system paperwork and scheduled system attention. They should not be collapsed into a single generic claim about all cleaning intervals.",
                "This route stays focused on local fire paperwork and inspection prep, not a cleaner's universal marketing promise.",
                null,
                new CallToAction(
                        "Need a cleaner who can leave inspection-ready paperwork?",
                        "Use the finder route carefully: it keeps authority guidance separate from provider listings and only stays indexed while coverage remains strong.",
                        "Check hood cleaner coverage",
                        localRoutePath(profile.profileId(), RouteTemplate.FIND_HOOD_CLEANER)
                ),
                null,
                submissionNotice(noticeCode),
                verdict,
                null,
                List.of(),
                relatedLinks(profile.profileId(), route.template()),
                operatorToolLinks(route.template()),
                sources,
                null,
                null,
                List.of(),
                true
        );
    }

    private LocalPageViewModel createInspectionPage(
            String requestPath,
            RouteRecord route,
            CityComplianceProfile profile,
            String cityName,
            AuthorityRecord authority,
            InspectionPrepRecord inspectionPrep,
            CityVerdict verdict,
            List<SourceAttribution> sources,
            boolean indexable,
            String noticeCode
    ) {
        return baseLocalPage(
                requestPath,
                route,
                profile,
                authority,
                indexable,
                seedRegistry.lastVerifiedFor(route),
                cityName + " inspection checklist",
                cityName + " restaurant fire inspection checklist",
                cityName + ", " + profile.state().toUpperCase()
                        + " restaurant fire inspection checklist: hood reports, extinguisher records, egress checks, and the proof to stage before the next visit.",
                "Checklist",
                List.copyOf(inspectionPrep.whatMustBeOnSite()),
                "Records to stage",
                List.copyOf(inspectionPrep.whatMustBeOnSite()),
                "Common failure reasons",
                List.copyOf(inspectionPrep.commonFailureReasons()),
                "Next action",
                verdict.nextActions(),
                "CTA rule",
                "The service CTA appears after the checklist because the product resolves the compliance state before it offers routing.",
                null,
                null,
                new CallToAction(
                        "Need to close a paperwork gap before inspection?",
                        "Start from the hood route if the missing item is a report, tag, or system service record.",
                        "Review hood requirements",
                        seedRegistry.routeFor(profile.profileId(), RouteTemplate.HOOD_REQUIREMENTS).path()
                ),
                null,
                submissionNotice(noticeCode),
                verdict,
                null,
                List.of(),
                relatedLinks(profile.profileId(), route.template()),
                operatorToolLinks(route.template()),
                sources,
                null,
                null,
                List.of(),
                true
        );
    }

    private LocalPageViewModel createProviderFinderPage(
            String requestPath,
            RouteRecord route,
            CityComplianceProfile profile,
            String cityName,
            AuthorityRecord authority,
            CityVerdict verdict,
            List<ProviderRecord> providers,
            List<SourceAttribution> sources,
            boolean indexable,
            String noticeCode
    ) {
        ProviderRoutingDecision routingDecision = providerRoutingDecisionService.decide(route.path(), providers);
        int authorityBackedProviderCount = indexingPolicyService.authorityBackedProviderCount(providers);
        int directContactProviderCount = indexingPolicyService.directContactProviderCount(providers);
        List<ProviderCard> providerCards = providerEvidenceService.sortByEvidenceQuality(providers).stream()
                .filter(indexingPolicyService::isPubliclyRenderable)
                .map(provider -> new ProviderCard(
                        provider.providerName(),
                        provider.providerType().name().toLowerCase().replace('_', ' '),
                        attributionService.providerClickPath(requestPath, provider),
                        provider.email(),
                        provider.phone(),
                        "Public listing",
                        providerEvidenceService.evidenceLabel(provider),
                        providerEvidenceService.coverageConfidenceLabel(provider),
                        providerEvidenceService.whyListed(provider),
                        "Route evidence reviewed " + seedRegistry.lastVerifiedFor(route),
                        providerEvidenceService.providerNote(provider),
                        provider.officialApprovalSourceUrl()
                ))
                .toList();
        int renderableProviderCount = indexingPolicyService.renderableProviderCount(providers);
        boolean verificationRequired = !indexable || authorityBackedProviderCount == 0;

        String providerModeSummary;
        if (routingDecision.routingMode() == owner.kitchencompliance.model.RoutingMode.MANUAL_ONLY) {
            providerModeSummary = "Use these listings as starting points only. Confirm current coverage and paperwork before you book anyone.";
        } else if (!indexable && indexingPolicyService.requiresAuthorityBackedProvider(route) && authorityBackedProviderCount == 0) {
            providerModeSummary = "The city publishes a formal grease workflow, but the listings below still need stronger official proof before you should rely on them alone.";
        } else if (!indexable && renderableProviderCount < indexingPolicyService.minimumFinderProviderCount()) {
            providerModeSummary = "There are fewer than "
                    + indexingPolicyService.minimumFinderProviderCount()
                    + " solid local options on this page right now, so start with the rule page and checklist before you call anyone.";
        } else if (!indexable) {
            providerModeSummary = "This page can still help you compare options, but some local details are being rechecked. Treat the cards below as a starting point.";
        } else if (authorityBackedProviderCount == 0) {
            providerModeSummary = "These listings can help you make first contact, but you should still verify service scope, manifest handling, and current city coverage before booking.";
        } else {
            providerModeSummary = "The cards below separate stronger evidence-backed listings from general service listings and keep the paperwork signals visible before booking.";
        }

        String noteTitle;
        String noteBody;
        CallToAction callToAction;
        if (route.template() == RouteTemplate.FIND_GREASE_SERVICE) {
            noteTitle = "Before you call a grease hauler";
            noteBody = "Use the local rule page to confirm who can haul, what paperwork must stay on site, and whether the city publishes an approved list.";
            callToAction = new CallToAction(
                    "Need the rule summary first?",
                    "Go back to the grease rule page if staff still needs the actual city requirement before calling anyone.",
                    "Return to " + cityName + " grease trap rules",
                    localRoutePath(profile.profileId(), RouteTemplate.FOG_RULES)
            );
        } else {
            noteTitle = "Before you call a hood cleaner";
            noteBody = "Use the local rule page to confirm the report, tag, and inspection paperwork your site needs after the cleaning visit.";
            callToAction = new CallToAction(
                    "Need the hood paperwork rule first?",
                    "Go back to the hood requirement page if the team still needs the local inspection-ready record list.",
                    "Return to " + cityName + " hood requirements",
                    localRoutePath(profile.profileId(), RouteTemplate.HOOD_REQUIREMENTS)
            );
        }

        return baseLocalPage(
                requestPath,
                route,
                profile,
                authority,
                indexable,
                seedRegistry.lastVerifiedFor(route),
                route.template() == RouteTemplate.FIND_GREASE_SERVICE ? cityName + " grease service" : cityName + " hood cleaners",
                route.template() == RouteTemplate.FIND_GREASE_SERVICE
                        ? cityName + " grease service finder"
                        : cityName + " hood cleaner finder",
                route.template() == RouteTemplate.FIND_GREASE_SERVICE
                        ? "Compare grease service options for restaurants in " + cityName + ", " + profile.state().toUpperCase()
                                + " after you confirm the local rule, manifest burden, and what the hauler must leave on site."
                        : "Compare hood cleaners for restaurants in " + cityName + ", " + profile.state().toUpperCase()
                                + " after you confirm the local report, tag, and inspection-prep burden.",
                "Rule-backed action state",
                verdict.whatAppliesNow(),
                "What to verify before booking",
                verdict.whatToKeepOnSite(),
                "Why this can still fail",
                verdict.whatFailsInspections(),
                "Operator next steps",
                verdict.nextActions(),
                noteTitle,
                noteBody,
                null,
                providerModeSummary,
                callToAction,
                operatorLeadPanel(route, cityName),
                submissionNotice(noticeCode),
                verdict,
                routingDecision,
                providerCards,
                relatedLinks(profile.profileId(), route.template()),
                operatorToolLinks(route.template()),
                sources,
                providerTrustTitle(indexable, verificationRequired),
                providerTrustBody(route, cityName, renderableProviderCount, authorityBackedProviderCount, directContactProviderCount, indexable, verificationRequired),
                providerVerificationChecklist(route, cityName, verificationRequired),
                false
        );
    }

    private LocalPageViewModel baseLocalPage(
            String requestPath,
            RouteRecord route,
            CityComplianceProfile profile,
            AuthorityRecord authority,
            boolean indexable,
            LocalDate lastVerified,
            String kicker,
            String title,
            String summary,
            String officialHeading,
            List<String> officialRequirements,
            String recordsHeading,
            List<String> onSiteRecords,
            String riskHeading,
            List<String> failureReasons,
            String actionsHeading,
            List<String> nextActions,
            String noteTitle,
            String noteBody,
            String officialListStatement,
            String providerModeSummary,
            CallToAction callToAction,
            LeadCapturePanel operatorLeadPanel,
            SubmissionNotice submissionNotice,
            CityVerdict cityVerdict,
            ProviderRoutingDecision routingDecision,
            List<ProviderCard> providers,
            List<RelatedPageLink> relatedLinks,
            List<RelatedPageLink> operatorToolLinks,
            List<SourceAttribution> sources,
            String trustBannerTitle,
            String trustBannerBody,
            List<String> verificationChecklist,
            boolean prioritizeOperatorLeadPanel
    ) {
        String canonicalPath = seedRegistry.canonicalPath(route);
        String metaTitle = localPageMetaTitle(route, profile, cityName(profile), authority);
        String metaDescription = localPageMetaDescription(route, profile, authority, summary);
        PageMeta meta = new PageMeta(
                metaTitle,
                metaDescription,
                canonicalUrl(canonicalPath),
                indexable ? "index,follow" : "noindex,follow",
                lastVerified,
                structuredDataJson(localPageStructuredData(route, profile, title, canonicalUrl(canonicalPath), metaDescription, providers))
        );

        return new LocalPageViewModel(
                meta,
                route.template(),
                requestPath,
                route.path(),
                canonicalPath,
                kicker,
                title,
                summary,
                authority.authorityName(),
                authority.authorityType().label(),
                displayCity(profile.city()),
                profile.state().toUpperCase(),
                officialHeading,
                officialRequirements,
                recordsHeading,
                onSiteRecords,
                riskHeading,
                failureReasons,
                actionsHeading,
                nextActions,
                noteTitle,
                noteBody,
                officialListStatement,
                providerModeSummary,
                trackedCallToAction(requestPath, callToAction),
                operatorLeadPanel,
                submissionNotice,
                cityVerdict,
                routingDecision,
                providers,
                relatedLinks,
                operatorToolLinks,
                sources,
                governanceHeading(route, authority),
                governanceBody(route, authority, displayCity(profile.city())),
                trustBannerTitle,
                trustBannerBody,
                verificationChecklist,
                prioritizeOperatorLeadPanel
        );
    }

    private List<RelatedPageLink> operatorToolLinks(RouteTemplate template) {
        return switch (template) {
            case FOG_RULES, APPROVED_HAULERS, FIND_GREASE_SERVICE -> List.of(
                    new RelatedPageLink("Grease log worksheet", "/tools/grease-log"),
                    new RelatedPageLink("Missing proof tracker", "/tools/missing-proof-tracker"),
                    new RelatedPageLink("Inspection reminder plan", "/tools/inspection-reminder-plan")
            );
            case HOOD_REQUIREMENTS, FIND_HOOD_CLEANER -> List.of(
                    new RelatedPageLink("Hood record binder", "/tools/hood-record-binder"),
                    new RelatedPageLink("Missing proof tracker", "/tools/missing-proof-tracker"),
                    new RelatedPageLink("Inspection reminder plan", "/tools/inspection-reminder-plan")
            );
            case INSPECTION_CHECKLIST -> List.of(
                    new RelatedPageLink("Missing proof tracker", "/tools/missing-proof-tracker"),
                    new RelatedPageLink("Inspection reminder plan", "/tools/inspection-reminder-plan"),
                    new RelatedPageLink("Hood record binder", "/tools/hood-record-binder"),
                    new RelatedPageLink("Grease log worksheet", "/tools/grease-log")
            );
        };
    }

    private CallToAction trackedCallToAction(String sourcePath, CallToAction callToAction) {
        if (callToAction == null) {
            return null;
        }
        return new CallToAction(
                callToAction.title(),
                callToAction.description(),
                callToAction.label(),
                attributionService.ctaClickPath(sourcePath, callToAction.path())
        );
    }

    private LeadCapturePanel operatorLeadPanel(RouteRecord route, String cityName) {
        if (route.template() != RouteTemplate.FIND_GREASE_SERVICE && route.template() != RouteTemplate.FIND_HOOD_CLEANER) {
            return null;
        }
        String description = route.template() == RouteTemplate.FIND_GREASE_SERVICE
                ? "Send a short request for " + cityName + " grease help. We use the city and issue details from this page when we follow up."
                : "Send a short request for " + cityName + " hood cleaning help. We use the city and issue details from this page when we follow up.";
        return new LeadCapturePanel(
                "service-request",
                "Short lead form",
                route.template() == RouteTemplate.FIND_GREASE_SERVICE ? "Need grease service help?" : "Need hood cleaning help?",
                description,
                "/lead-intake/operator",
                "Send service request",
                "I consent to routing this request for local service follow-up."
        );
    }

    private SubmissionNotice submissionNotice(String noticeCode) {
        if (noticeCode == null || noticeCode.isBlank()) {
            return null;
        }
        return switch (noticeCode) {
            case "operator-submitted" -> new SubmissionNotice(
                    "Service request saved",
                    "We received your request and will use the city and issue details from this page when we follow up."
            );
            case "consent-required" -> new SubmissionNotice(
                    "Consent required",
                    "Check the consent box before sending the request."
            );
            case "operator-invalid" -> new SubmissionNotice(
                    "Service request incomplete",
                    "Add a contact name, business name, and email before submitting."
            );
            default -> null;
        };
    }

    private String providerTrustTitle(boolean indexable, boolean verificationRequired) {
        if (!indexable) {
            return "Check before you book";
        }
        if (verificationRequired) {
            return "Verify the paperwork";
        }
        return "Ready to compare providers";
    }

    private String providerTrustBody(
            RouteRecord route,
            String cityName,
            int renderableProviderCount,
            int authorityBackedProviderCount,
            int directContactProviderCount,
            boolean indexable,
            boolean verificationRequired
    ) {
        if (!indexable) {
            return "Use this page to compare names and contacts, then confirm local coverage and paperwork with the checklist below before you book.";
        }
        if (verificationRequired) {
            return "This page can still help with first contact, but you should confirm current "
                    + (route.template() == RouteTemplate.FIND_GREASE_SERVICE ? "manifest and hauler" : "cleaning-report and coverage")
                    + " details before booking because the public proof is still limited.";
        }
        return "This page has current local coverage, direct contact details, and official source signals to help you compare providers before booking.";
    }

    private List<String> providerVerificationChecklist(RouteRecord route, String cityName, boolean verificationRequired) {
        if (route.template() == RouteTemplate.FIND_GREASE_SERVICE) {
            return List.of(
                    "Confirm the current city hauler or transporter workflow before booking, especially if the authority publishes a list or registry.",
                    "Ask the service company to confirm current " + cityName + " grease coverage, manifest handling, and who leaves the receipt trail on site.",
                    "File the trip ticket, receipt, and follow-up date into the grease log and reminder plan as soon as service is complete."
            );
        }
        if (verificationRequired) {
            return List.of(
                    "Confirm the cleaner still serves " + cityName + " and leaves an inspection-ready report or tag after the visit.",
                    "Match the promised paperwork to the hood, duct, and suppression records your site already keeps on hand.",
                    "Move the report into the hood binder and reminder plan before the next inspection window opens."
            );
        }
        return List.of(
                "Use the evidence-backed provider cards first, then confirm the exact report or tag the crew will leave behind.",
                "Match the booked service to the local hood-system record burden before the visit happens.",
                "File the report into the hood binder and set the next review date before the paperwork gets stale."
        );
    }

    private List<RelatedPageLink> relatedLinks(String profileId, RouteTemplate currentTemplate) {
        String cityName = displayCity(seedRegistry.profile(profileId).city());
        return switch (currentTemplate) {
            case FOG_RULES -> List.of(
                    link(cityName + " fire inspection checklist", localRoutePath(profileId, RouteTemplate.INSPECTION_CHECKLIST)),
                    link(cityName + " hood requirements", localRoutePath(profileId, RouteTemplate.HOOD_REQUIREMENTS)),
                    link(cityName + " approved haulers", localRoutePath(profileId, RouteTemplate.APPROVED_HAULERS))
            );
            case APPROVED_HAULERS -> List.of(
                    link(cityName + " grease trap rules", localRoutePath(profileId, RouteTemplate.FOG_RULES)),
                    link(cityName + " fire inspection checklist", localRoutePath(profileId, RouteTemplate.INSPECTION_CHECKLIST)),
                    link(cityName + " grease service finder", localRoutePath(profileId, RouteTemplate.FIND_GREASE_SERVICE))
            );
            case HOOD_REQUIREMENTS -> List.of(
                    link(cityName + " fire inspection checklist", localRoutePath(profileId, RouteTemplate.INSPECTION_CHECKLIST)),
                    link(cityName + " grease trap rules", localRoutePath(profileId, RouteTemplate.FOG_RULES)),
                    link(cityName + " hood cleaner finder", localRoutePath(profileId, RouteTemplate.FIND_HOOD_CLEANER))
            );
            case INSPECTION_CHECKLIST -> List.of(
                    link(cityName + " hood requirements", localRoutePath(profileId, RouteTemplate.HOOD_REQUIREMENTS)),
                    link(cityName + " grease trap rules", localRoutePath(profileId, RouteTemplate.FOG_RULES)),
                    link(cityName + " hood cleaner finder", localRoutePath(profileId, RouteTemplate.FIND_HOOD_CLEANER))
            );
            case FIND_GREASE_SERVICE -> List.of(
                    link(cityName + " grease trap rules", localRoutePath(profileId, RouteTemplate.FOG_RULES)),
                    link(cityName + " fire inspection checklist", localRoutePath(profileId, RouteTemplate.INSPECTION_CHECKLIST)),
                    link(cityName + " approved haulers", localRoutePath(profileId, RouteTemplate.APPROVED_HAULERS))
            );
            case FIND_HOOD_CLEANER -> List.of(
                    link(cityName + " hood requirements", localRoutePath(profileId, RouteTemplate.HOOD_REQUIREMENTS)),
                    link(cityName + " fire inspection checklist", localRoutePath(profileId, RouteTemplate.INSPECTION_CHECKLIST)),
                    link(cityName + " grease trap rules", localRoutePath(profileId, RouteTemplate.FOG_RULES))
            );
        };
    }

    private String governanceHeading(RouteRecord route, AuthorityRecord authority) {
        return switch (authority.authorityType()) {
            case UTILITY -> "Local utility office";
            case FIRE_AHJ -> "Local fire office";
            case CITY_DEPARTMENT -> "Local department";
        };
    }

    private String governanceBody(RouteRecord route, AuthorityRecord authority, String cityName) {
        if (!seedRegistry.usesAuthorityCanonical(route)) {
            return "For " + cityName + ", the department above is the office that sets and enforces the rule on this page.";
        }
        String workflow = switch (route.template()) {
            case FOG_RULES, APPROVED_HAULERS, FIND_GREASE_SERVICE -> "grease, hauling, and paperwork";
            case HOOD_REQUIREMENTS, FIND_HOOD_CLEANER -> "hood cleaning and fire paperwork";
            case INSPECTION_CHECKLIST -> "inspection preparation";
        };
        return cityName + " operators often search by city name, but " + authority.authorityName()
                + " is the office that sets the local " + workflow + " rules on this page.";
    }

    private String localRoutePath(String profileId, RouteTemplate template) {
        return seedRegistry.canonicalPath(seedRegistry.routeFor(profileId, template));
    }

    private AuthorityRouteLink authorityRouteLink(String profileId, RouteTemplate template, String title) {
        CityComplianceProfile profile = seedRegistry.profile(profileId);
        RouteRecord route = seedRegistry.routeFor(profileId, template);
        AuthorityRecord authority = seedRegistry.authority(route.authorityId());
        return new AuthorityRouteLink(
                title,
                displayCity(profile.city()) + ", " + profile.state().toUpperCase(),
                authority.authorityName(),
                authority.authorityType().label(),
                seedRegistry.canonicalPath(route),
                route.path()
        );
    }

    private List<ProviderRecord> providersFor(RouteRecord route, String profileId) {
        ProviderType providerType = route.template() == RouteTemplate.FIND_HOOD_CLEANER
                ? ProviderType.HOOD_CLEANER
                : ProviderType.GREASE_HAULER;
        return seedRegistry.providersFor(profileId, providerType);
    }

    private SourceAttribution toSourceAttribution(SourceRecord source) {
        return new SourceAttribution(
                source.title(),
                source.agency(),
                source.sourceUrl(),
                source.quoteSummary(),
                source.sourceTier().label(),
                source.verifiedOn()
        );
    }

    private RelatedPageLink link(String label, String path) {
        return new RelatedPageLink(label, path);
    }

    private LocalDate siteLatestVerifiedDate() {
        return seedRegistry.routes().stream()
                .map(seedRegistry::lastVerifiedFor)
                .filter(date -> date != null)
                .max(LocalDate::compareTo)
                .orElse(null);
    }

    private LocalDate guideLastVerifiedDate(GuideCatalog.GuideDefinition guide) {
        return guide.authorityReferences().stream()
                .map(reference -> seedRegistry.routeFor(reference.profileId(), reference.template()))
                .map(seedRegistry::lastVerifiedFor)
                .filter(date -> date != null)
                .max(LocalDate::compareTo)
                .orElse(null);
    }

    private LocalDate authorityDirectoryLastVerifiedDate() {
        return authorityDirectoryLastVerifiedDate(seedRegistry.authoritiesById().values().stream()
                .map(this::authorityBrowseCard)
                .toList());
    }

    private LocalDate authorityDirectoryLastVerifiedDate(List<AuthorityBrowseCard> authorityCards) {
        return authorityCards.stream()
                .map(AuthorityBrowseCard::lastVerified)
                .map(LocalDate::parse)
                .max(LocalDate::compareTo)
                .orElse(null);
    }

    private HomeIssueCard issueCard(
            String title,
            String summary,
            String destinationLabel,
            String destinationPath,
            String supportLabel,
            String supportPath,
            String iconName
    ) {
        return new HomeIssueCard(title, summary, destinationLabel, destinationPath, supportLabel, supportPath, iconName);
    }

    private String displayCity(String city) {
        String[] parts = city.trim().split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1).toLowerCase());
            }
        }
        return builder.toString();
    }

    private String cityName(CityComplianceProfile profile) {
        return displayCity(profile.city());
    }

    private String localPageMetaTitle(
            RouteRecord route,
            CityComplianceProfile profile,
            String cityName,
            AuthorityRecord authority
    ) {
        String location = cityName + ", " + profile.state().toUpperCase();
        return switch (route.template()) {
            case FOG_RULES -> location + " Grease Trap Rules for Restaurants | Pump-Outs & Manifests";
            case APPROVED_HAULERS -> location + " Approved Grease Haulers | Official List & Verification";
            case HOOD_REQUIREMENTS -> location + " Hood Cleaning Requirements | Reports & Inspection Prep";
            case INSPECTION_CHECKLIST -> location + " Restaurant Fire Inspection Checklist | Proof to Stage";
            case FIND_GREASE_SERVICE -> location + " Grease Service Companies | Manifests & Coverage";
            case FIND_HOOD_CLEANER -> location + " Hood Cleaners for Restaurants | Reports & Coverage";
        };
    }

    private String localPageMetaDescription(
            RouteRecord route,
            CityComplianceProfile profile,
            AuthorityRecord authority,
            String summary
    ) {
        String location = cityName(profile) + ", " + profile.state().toUpperCase();
        String authorityLabel = authoritySnippetLabel(authority, cityName(profile));
        return switch (route.template()) {
            case FOG_RULES -> location
                    + " grease trap rules for restaurants: interceptor approval, pump-out timing, manifests to keep on site, and hauler checks under "
                    + authorityLabel + ".";
            case APPROVED_HAULERS -> location
                    + " approved grease hauler workflow: official list status, manifest rules, and how to verify service before booking.";
            case HOOD_REQUIREMENTS -> location
                    + " hood cleaning requirements for restaurants: service reports, tags, and inspection-ready paperwork under "
                    + authorityLabel + ".";
            case INSPECTION_CHECKLIST -> location
                    + " restaurant fire inspection checklist: hood reports, extinguisher records, egress checks, and proof to stage before the next visit.";
            case FIND_GREASE_SERVICE -> location
                    + " grease service options for restaurants after you confirm local manifests, hauler checks, and what paperwork must stay on site.";
            case FIND_HOOD_CLEANER -> location
                    + " hood cleaner options for restaurants after you confirm local reports, tags, and inspection-prep paperwork.";
        };
    }

    private String authoritySnippetLabel(AuthorityRecord authority, String cityName) {
        return switch (authority.authorityType()) {
            case UTILITY -> authority.authorityName().length() <= 36
                    ? authority.authorityName()
                    : "the local utility";
            case FIRE_AHJ -> cityName + " fire marshal";
            case CITY_DEPARTMENT -> authority.authorityName().length() <= 36
                    ? authority.authorityName()
                    : "the local department";
        };
    }

    private String canonicalUrl(String path) {
        if (path.equals("/")) {
            return siteProperties.baseUrl();
        }
        return siteProperties.baseUrl() + path;
    }

    private AuthorityBrowseCard authorityBrowseCard(AuthorityRecord authority) {
        List<AuthorityBrowseRouteLink> routeLinks = seedRegistry.routes().stream()
                .filter(route -> route.authorityId().equals(authority.authorityId()))
                .sorted(Comparator.comparingInt(route -> route.template().ordinal()))
                .map(route -> new AuthorityBrowseRouteLink(
                        authorityRouteTitle(route.template(), displayCity(authority.city())),
                        seedRegistry.canonicalPath(route),
                        route.path()
                ))
                .toList();

        return new AuthorityBrowseCard(
                authority.authorityName(),
                authority.authorityType().label(),
                displayCity(authority.city()) + ", " + authority.state().toUpperCase(),
                authority.state().toUpperCase(),
                "/authorities/" + authority.state() + "/" + authority.authorityId(),
                authority.baseUrl(),
                authority.contactUrl(),
                authority.lastVerified().toString(),
                titleCase(authority.verificationStatus().name()),
                routeLinks
        );
    }

    private List<AuthorityBrowseFilterOption> authorityFilterOptions(AuthorityType activeType) {
        List<AuthorityBrowseFilterOption> options = new ArrayList<>();
        options.add(new AuthorityBrowseFilterOption("All rule holders", "/authorities", activeType == null));
        for (AuthorityType authorityType : AuthorityType.values()) {
            options.add(new AuthorityBrowseFilterOption(
                    authorityType.label(),
                    "/authorities?type=" + authorityType.name().toLowerCase(),
                    authorityType == activeType
            ));
        }
        return List.copyOf(options);
    }

    private List<AuthorityBrowseSection> authoritySections(List<AuthorityBrowseCard> authorityCards) {
        Map<String, List<AuthorityBrowseCard>> grouped = new LinkedHashMap<>();
        for (AuthorityBrowseCard authorityCard : authorityCards) {
            grouped.computeIfAbsent(authorityCard.stateLabel(), ignored -> new ArrayList<>()).add(authorityCard);
        }
        return grouped.entrySet().stream()
                .map(entry -> new AuthorityBrowseSection(
                        "state-" + entry.getKey().toLowerCase(),
                        entry.getKey(),
                        entry.getValue().size(),
                        List.copyOf(entry.getValue())
                ))
                .toList();
    }

    private List<AuthorityBrowseStateJump> stateJumps(List<AuthorityBrowseSection> sections) {
        return sections.stream()
                .map(section -> new AuthorityBrowseStateJump(
                        section.stateLabel(),
                        section.anchorId(),
                        section.authorityCount()
                ))
                .toList();
    }

    private AuthorityType parseAuthorityType(String typeFilter) {
        if (typeFilter == null || typeFilter.isBlank()) {
            return null;
        }
        try {
            return AuthorityType.valueOf(typeFilter.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown authority filter: " + typeFilter);
        }
    }

    private String authorityRouteTitle(RouteTemplate template, String cityName) {
        return switch (template) {
            case FOG_RULES -> cityName + " FOG rules";
            case APPROVED_HAULERS -> cityName + " approved haulers";
            case HOOD_REQUIREMENTS -> cityName + " hood requirements";
            case INSPECTION_CHECKLIST -> cityName + " inspection checklist";
            case FIND_GREASE_SERVICE -> cityName + " grease service finder";
            case FIND_HOOD_CLEANER -> cityName + " hood cleaner finder";
        };
    }

    private List<Map<String, Object>> localPageStructuredData(
            RouteRecord route,
            CityComplianceProfile profile,
            String title,
            String url,
            String description,
            List<ProviderCard> providers
    ) {
        List<Map<String, Object>> payloads = new ArrayList<>();
        payloads.add(webPageStructuredData(title, url, description));

        List<Map<String, Object>> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(breadcrumbItem("Home", canonicalUrl("/")));
        if (!seedRegistry.canonicalPath(route).equals(route.path())) {
            breadcrumbs.add(breadcrumbItem(
                    displayCity(profile.city()) + ", " + profile.state().toUpperCase(),
                    canonicalUrl(route.path())
            ));
        }
        breadcrumbs.add(breadcrumbItem(title, url));
        payloads.add(breadcrumbStructuredData(breadcrumbs));

        if ((route.template() == RouteTemplate.FIND_GREASE_SERVICE || route.template() == RouteTemplate.FIND_HOOD_CLEANER)
                && providers != null
                && !providers.isEmpty()) {
            payloads.add(providerItemListStructuredData(url, providers));
        }
        return payloads;
    }

    private Map<String, Object> websiteStructuredData(String name, String url) {
        return schemaObject("WebSite", name, url, null);
    }

    private Map<String, Object> articleStructuredData(String name, String url, String description) {
        return schemaObject("Article", name, url, description);
    }

    private Map<String, Object> webPageStructuredData(String name, String url, String description) {
        return schemaObject("WebPage", name, url, description);
    }

    private Map<String, Object> collectionPageStructuredData(String name, String url, String description) {
        return schemaObject("CollectionPage", name, url, description);
    }

    private Map<String, Object> schemaObject(String type, String name, String url, String description) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("@context", "https://schema.org");
        payload.put("@type", type);
        payload.put("name", name);
        payload.put("url", url);
        if (description != null) {
            payload.put("description", description);
        }
        return payload;
    }

    private Map<String, Object> breadcrumbStructuredData(List<Map<String, Object>> items) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("@context", "https://schema.org");
        payload.put("@type", "BreadcrumbList");
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int index = 0; index < items.size(); index++) {
            Map<String, Object> listItem = new LinkedHashMap<>();
            listItem.put("@type", "ListItem");
            listItem.put("position", index + 1);
            listItem.putAll(items.get(index));
            listItems.add(listItem);
        }
        payload.put("itemListElement", listItems);
        return payload;
    }

    private Map<String, Object> breadcrumbItem(String name, String url) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("name", name);
        item.put("item", url);
        return item;
    }

    private Map<String, Object> providerItemListStructuredData(String url, List<ProviderCard> providers) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("@context", "https://schema.org");
        payload.put("@type", "ItemList");
        payload.put("url", url);
        List<Map<String, Object>> items = new ArrayList<>();
        for (int index = 0; index < providers.size(); index++) {
            ProviderCard provider = providers.get(index);
            Map<String, Object> listItem = new LinkedHashMap<>();
            listItem.put("@type", "ListItem");
            listItem.put("position", index + 1);
            listItem.put("name", provider.providerName());
            listItem.put("url", provider.siteUrl());
            listItem.put("description", provider.whyListed());
            items.add(listItem);
        }
        payload.put("itemListElement", items);
        return payload;
    }

    private Map<String, Object> authorityItemListStructuredData(String url, List<AuthorityBrowseCard> authorities) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("@context", "https://schema.org");
        payload.put("@type", "ItemList");
        payload.put("url", url);
        List<Map<String, Object>> items = new ArrayList<>();
        for (int index = 0; index < authorities.size(); index++) {
            AuthorityBrowseCard authority = authorities.get(index);
            Map<String, Object> listItem = new LinkedHashMap<>();
            listItem.put("@type", "ListItem");
            listItem.put("position", index + 1);
            listItem.put("name", authority.authorityName());
            listItem.put("url", canonicalUrl(authority.detailPath()));
            listItem.put("description", authority.authorityTypeLabel() + " for " + authority.cityLabel());
            items.add(listItem);
        }
        payload.put("itemListElement", items);
        return payload;
    }

    private Map<String, Object> authorityRouteItemListStructuredData(String url, List<AuthorityBrowseRouteLink> routes) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("@context", "https://schema.org");
        payload.put("@type", "ItemList");
        payload.put("url", url);
        List<Map<String, Object>> items = new ArrayList<>();
        for (int index = 0; index < routes.size(); index++) {
            AuthorityBrowseRouteLink route = routes.get(index);
            Map<String, Object> listItem = new LinkedHashMap<>();
            listItem.put("@type", "ListItem");
            listItem.put("position", index + 1);
            listItem.put("name", route.title());
            listItem.put("url", canonicalUrl(route.canonicalPath()));
            items.add(listItem);
        }
        payload.put("itemListElement", items);
        return payload;
    }

    private String structuredDataJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to create structured data.", ex);
        }
    }

    private String titleCase(String value) {
        String normalized = value == null ? "" : value.trim().replace('_', ' ');
        String[] parts = normalized.split("\\s+");
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
                builder.append(part.substring(1).toLowerCase());
            }
        }
        return builder.toString();
    }
}
