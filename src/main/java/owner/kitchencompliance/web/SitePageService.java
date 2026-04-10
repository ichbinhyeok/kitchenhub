package owner.kitchencompliance.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.ApprovedHaulerMode;
import owner.kitchencompliance.data.AuthorityRecord;
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
import owner.kitchencompliance.model.CallToAction;
import owner.kitchencompliance.model.CityCard;
import owner.kitchencompliance.model.CityVerdict;
import owner.kitchencompliance.model.GuidePageViewModel;
import owner.kitchencompliance.model.HomePanelLink;
import owner.kitchencompliance.model.HomePageViewModel;
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
        this.objectMapper = objectMapper;
        this.attributionService = attributionService;
        this.providerEvidenceService = providerEvidenceService;
        this.operatorToolService = operatorToolService;
    }

    public HomePageViewModel homePage() {
        Map<String, Integer> cityOrder = Map.of(
                "Austin", 0,
                "Charlotte", 1,
                "Tampa", 2,
                "Portland", 3,
                "Santa Clara", 4,
                "Nashville", 5,
                "Grand Island", 6,
                "Miami", 7
        );

        List<CityCard> cityCards = seedRegistry.profiles().stream()
                .map(profile -> new CityCard(
                        displayCity(profile.city()),
                        profile.state().toUpperCase(),
                        profile.decisionReason(),
                        List.of(
                                link("FOG rules", seedRegistry.routeFor(profile.profileId(), RouteTemplate.FOG_RULES).path()),
                                link("Hood requirements", seedRegistry.routeFor(profile.profileId(), RouteTemplate.HOOD_REQUIREMENTS).path())
                        )))
                .sorted((left, right) -> Integer.compare(
                        cityOrder.getOrDefault(left.city(), Integer.MAX_VALUE),
                        cityOrder.getOrDefault(right.city(), Integer.MAX_VALUE)
                ))
                .toList();

        List<HomePanelLink> guideLinks = guideCatalog.allGuides().stream()
                .map(guide -> new HomePanelLink(
                        guide.title(),
                        guide.summary(),
                        "/guides/" + guide.slug(),
                        "menu_book"
                ))
                .toList();

        PageMeta meta = new PageMeta(
                siteProperties.title() + " | FOG, hood, and inspection prep",
                "Choose a city, verify the local authority, stage the required proof, and route service only when the site still needs help.",
                canonicalUrl("/"),
                "index,follow",
                LocalDate.now(),
                structuredData("WebSite", siteProperties.title(), canonicalUrl("/"), null)
        );

        return new HomePageViewModel(
                meta,
                "Kitchen compliance with a local next action",
                "KitchenComplianceHub helps commercial kitchen operators start with the local authority, confirm what must stay on site, and then move into the next action without mixing vendor copy into official guidance.",
                List.of(
                        "Start from the city or authority that governs the site.",
                        "See exactly what proof should stay on site.",
                        "Move from rule clarity to the next service action without blending sponsor copy into official guidance."
                ),
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
            case FOG_RULES -> createFogRulesPage(route, profile, cityName, authority, fogRule, verdict, sourceAttributions, indexable, noticeCode);
            case APPROVED_HAULERS -> createApprovedHaulersPage(route, profile, cityName, authority, fogRule, verdict, sourceAttributions, indexable, noticeCode);
            case HOOD_REQUIREMENTS -> createHoodPage(route, profile, cityName, authority, hoodRule, verdict, sourceAttributions, indexable, noticeCode);
            case INSPECTION_CHECKLIST -> createInspectionPage(route, profile, cityName, authority, inspectionPrep, verdict, sourceAttributions, indexable, noticeCode);
            case FIND_GREASE_SERVICE, FIND_HOOD_CLEANER -> createProviderFinderPage(route, profile, cityName, authority, verdict, providers, sourceAttributions, indexable, noticeCode);
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
                LocalDate.now(),
                structuredData("Article", guide.title(), canonicalUrl("/guides/" + guide.slug()), guide.summary())
        );
        return new GuidePageViewModel(meta, guide.title(), guide.summary(), guide.sections(), guide.relatedLinks());
    }

    public List<SitemapEntry> sitemapEntries() {
        List<SitemapEntry> entries = new ArrayList<>();
        entries.add(new SitemapEntry(canonicalUrl("/"), "weekly", "1.0"));

        for (RouteRecord route : seedRegistry.routes()) {
            boolean indexable = indexingPolicyService.isIndexable(route, seedRegistry.sourcesFor(route), providersFor(route, route.profileId()));
            if (indexable) {
                entries.add(new SitemapEntry(canonicalUrl(route.path()), "weekly", "0.8"));
            }
        }

        for (GuideCatalog.GuideDefinition guide : guideCatalog.allGuides()) {
            entries.add(new SitemapEntry(canonicalUrl("/guides/" + guide.slug()), "monthly", "0.6"));
        }

        return List.copyOf(entries);
    }

    private LocalPageViewModel createFogRulesPage(
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
                route,
                profile,
                authority,
                indexable,
                seedRegistry.lastVerifiedFor(route),
                cityName + " FOG rules",
                cityName + " grease trap and interceptor rules",
                "What " + authority.authorityName() + " publishes for interceptor approval, pump-out timing, manifests, and permitted hauler checks.",
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
                cityName + " publishes a source-backed service cadence and verification workflow, so the page can stay explicit without inventing a generic national default.",
                fogRule.approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST
                        ? cityName + " publishes an authority-backed hauler or preferred-pumper list, but it does not recommend or endorse any provider on that list."
                        : cityName + " does not publish a safe approved list for this workflow, so the operator must verify the vendor directly.",
                null,
                new CallToAction(
                        "Need a hauler check before the next pump-out?",
                        "Start with the city's official list and then confirm the vendor still covers grease waste and manifest handling.",
                        "Review the " + cityName + " hauler workflow",
                        seedRegistry.routeFor(profile.profileId(), RouteTemplate.APPROVED_HAULERS).path(),
                        false
                ),
                null,
                sponsorPanel(route, cityName),
                submissionNotice(noticeCode),
                verdict,
                null,
                List.of(),
                relatedLinks(profile.profileId(), route.template()),
                sources
        );
    }

    private LocalPageViewModel createApprovedHaulersPage(
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
                route,
                profile,
                authority,
                indexable,
                seedRegistry.lastVerifiedFor(route),
                cityName + " permitted haulers",
                cityName + " approved grease hauler workflow",
                fogRule.approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST
                        ? cityName + " has an authority-backed hauler or preferred-pumper registry for grease service, and the listing should still be treated as verification rather than endorsement."
                        : cityName + " does not publish a safe approved-hauler list, so this page stays focused on a source-backed self-verification workflow.",
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
                        seedRegistry.routeFor(profile.profileId(), RouteTemplate.FIND_GREASE_SERVICE).path(),
                        false
                ),
                null,
                sponsorPanel(route, cityName),
                submissionNotice(noticeCode),
                verdict,
                null,
                List.of(),
                relatedLinks(profile.profileId(), route.template()),
                sources
        );
    }

    private LocalPageViewModel createHoodPage(
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
                route,
                profile,
                authority,
                indexable,
                seedRegistry.lastVerifiedFor(route),
                cityName + " hood requirements",
                cityName + " hood-system cleaning and inspection prep",
                cityName + "'s published fire materials are strongest on hood-system inspection frequency, service tags, and inspection-ready reports.",
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
                "This route stays focused on local fire paperwork and inspection prep, not a vendor's universal cleaning promise.",
                null,
                new CallToAction(
                        "Need a cleaner who can leave inspection-ready paperwork?",
                        "Use the finder route carefully: it keeps official guidance separate from provider listings and only stays indexed while coverage remains strong.",
                        "Check hood cleaner coverage",
                        seedRegistry.routeFor(profile.profileId(), RouteTemplate.FIND_HOOD_CLEANER).path(),
                        false
                ),
                null,
                sponsorPanel(route, cityName),
                submissionNotice(noticeCode),
                verdict,
                null,
                List.of(),
                relatedLinks(profile.profileId(), route.template()),
                sources
        );
    }

    private LocalPageViewModel createInspectionPage(
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
                route,
                profile,
                authority,
                indexable,
                seedRegistry.lastVerifiedFor(route),
                cityName + " inspection checklist",
                cityName + " restaurant fire inspection checklist",
                "Inspection prep is a proof burden first: show the current hood, suppression, and site-safety records before the inspector has to ask twice.",
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
                        seedRegistry.routeFor(profile.profileId(), RouteTemplate.HOOD_REQUIREMENTS).path(),
                        false
                ),
                null,
                sponsorPanel(route, cityName),
                submissionNotice(noticeCode),
                verdict,
                null,
                List.of(),
                relatedLinks(profile.profileId(), route.template()),
                sources
        );
    }

    private LocalPageViewModel createProviderFinderPage(
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
        List<ProviderCard> providerCards = providerEvidenceService.sortByEvidenceQuality(providers).stream()
                .filter(indexingPolicyService::isPubliclyRenderable)
                .map(provider -> new ProviderCard(
                        provider.providerName(),
                        provider.providerType().name().toLowerCase().replace('_', ' '),
                        attributionService.providerClickPath(route, provider),
                        provider.email(),
                        provider.phone(),
                        provider.listingMode() == owner.kitchencompliance.data.ListingMode.PUBLIC
                                ? "Public listing"
                                : "Sponsor placement",
                        provider.sponsorStatus().name().toLowerCase(),
                        providerEvidenceService.evidenceLabel(provider),
                        providerEvidenceService.providerNote(provider),
                        provider.officialApprovalSourceUrl()
                ))
                .toList();
        int renderableProviderCount = indexingPolicyService.renderableProviderCount(providers);

        String providerModeSummary;
        if (routingDecision.routingMode() == owner.kitchencompliance.model.RoutingMode.MANUAL_ONLY) {
            providerModeSummary = "Coverage is still weak, so this page stays noindex and gives operator verification steps instead of a public vendor ranking.";
        } else if (!indexable && renderableProviderCount < indexingPolicyService.minimumFinderProviderCount()) {
            providerModeSummary = "Coverage is below the launch threshold of "
                    + indexingPolicyService.minimumFinderProviderCount()
                    + " public or active options, so this page stays noindex and guidance-first.";
        } else if (!indexable) {
            providerModeSummary = "The provider cards are visible, but the route is temporarily noindex until freshness and source-quality gates are back in bounds.";
        } else {
            providerModeSummary = "Only public listings or clearly separated sponsor placements appear here.";
        }

        String noteTitle;
        String noteBody;
        CallToAction callToAction;
        if (route.template() == RouteTemplate.FIND_GREASE_SERVICE) {
            noteTitle = "Grease service rule";
            noteBody = "This page should follow the city's hauler and manifest rules, not pretend the vendor list is official guidance.";
            callToAction = new CallToAction(
                    "Need the rule summary first?",
                    "Go back to the grease rule page if staff still needs the actual city requirement before calling anyone.",
                    "Return to " + cityName + " grease trap rules",
                    seedRegistry.routeFor(profile.profileId(), RouteTemplate.FOG_RULES).path(),
                    false
            );
        } else {
            noteTitle = "Hood cleaner rule";
            noteBody = "This page should route operators only after the hood-system paperwork burden is already clear.";
            callToAction = new CallToAction(
                    "Need the hood paperwork rule first?",
                    "Go back to the hood requirement page if the team still needs the official inspection-ready record list.",
                    "Return to " + cityName + " hood requirements",
                    seedRegistry.routeFor(profile.profileId(), RouteTemplate.HOOD_REQUIREMENTS).path(),
                    false
            );
        }

        return baseLocalPage(
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
                        ? "A finder route for the operator's next step after the local FOG rule is already clear."
                        : "A finder route for hood-system help after the local inspection paperwork burden is already clear.",
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
                sponsorPanel(route, cityName),
                submissionNotice(noticeCode),
                verdict,
                routingDecision,
                providerCards,
                relatedLinks(profile.profileId(), route.template()),
                sources
        );
    }

    private LocalPageViewModel baseLocalPage(
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
            LeadCapturePanel sponsorPanel,
            SubmissionNotice submissionNotice,
            CityVerdict cityVerdict,
            ProviderRoutingDecision routingDecision,
            List<ProviderCard> providers,
            List<RelatedPageLink> relatedLinks,
            List<SourceAttribution> sources
    ) {
        PageMeta meta = new PageMeta(
                title + " | " + siteProperties.title(),
                summary,
                canonicalUrl(route.canonicalPath()),
                indexable ? "index,follow" : "noindex,follow",
                lastVerified,
                structuredData("WebPage", title, canonicalUrl(route.canonicalPath()), summary)
        );

        return new LocalPageViewModel(
                meta,
                route.template(),
                route.path(),
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
                trackedCallToAction(route, callToAction),
                operatorLeadPanel,
                sponsorPanel,
                submissionNotice,
                cityVerdict,
                routingDecision,
                providers,
                relatedLinks,
                sources
        );
    }

    private CallToAction trackedCallToAction(RouteRecord route, CallToAction callToAction) {
        if (callToAction == null) {
            return null;
        }
        return new CallToAction(
                callToAction.title(),
                callToAction.description(),
                callToAction.label(),
                attributionService.ctaClickPath(route, callToAction.path(), callToAction.sponsored()),
                callToAction.sponsored()
        );
    }

    private LeadCapturePanel operatorLeadPanel(RouteRecord route, String cityName) {
        if (route.template() != RouteTemplate.FIND_GREASE_SERVICE && route.template() != RouteTemplate.FIND_HOOD_CLEANER) {
            return null;
        }
        String description = route.template() == RouteTemplate.FIND_GREASE_SERVICE
                ? "Send a short service request for " + cityName + " grease help. This stays separate from official guidance and lands in the local admin CSV."
                : "Send a short service request for " + cityName + " hood cleaning help. This stays separate from official guidance and lands in the local admin CSV.";
        return new LeadCapturePanel(
                "service-request",
                "Short lead form",
                route.template() == RouteTemplate.FIND_GREASE_SERVICE ? "Need grease service help?" : "Need hood cleaning help?",
                description,
                "/lead-intake/operator",
                "Send service request",
                "I consent to routing this request for local service follow-up.",
                false
        );
    }

    private LeadCapturePanel sponsorPanel(RouteRecord route, String cityName) {
        return new LeadCapturePanel(
                "sponsor-slot",
                "Sponsor slot",
                "Want sponsor placement on " + cityName + " coverage?",
                "This inquiry is for direct local sponsor visibility only. It does not change the authority summary or source-backed rule content on the page.",
                "/lead-intake/sponsor",
                "Request sponsor details",
                "I consent to follow-up about sponsor placement for this route family.",
                true
        );
    }

    private SubmissionNotice submissionNotice(String noticeCode) {
        if (noticeCode == null || noticeCode.isBlank()) {
            return null;
        }
        return switch (noticeCode) {
            case "operator-submitted" -> new SubmissionNotice(
                    "Service request saved",
                    "The operator request is in the persistent lead CSV and now shows up in /admin."
            );
            case "sponsor-submitted" -> new SubmissionNotice(
                    "Sponsor inquiry saved",
                    "The sponsor inquiry is in the persistent lead CSV and now shows up in /admin."
            );
            case "consent-required" -> new SubmissionNotice(
                    "Consent required",
                    "Check the consent box before sending the request so the route can be tracked safely."
            );
            case "operator-invalid" -> new SubmissionNotice(
                    "Service request incomplete",
                    "Add a contact name, business name, and email before submitting."
            );
            case "sponsor-invalid" -> new SubmissionNotice(
                    "Sponsor inquiry incomplete",
                    "Add a contact name, business name, and email before submitting."
            );
            default -> null;
        };
    }

    private List<RelatedPageLink> relatedLinks(String profileId, RouteTemplate currentTemplate) {
        String cityName = displayCity(seedRegistry.profile(profileId).city());
        return switch (currentTemplate) {
            case FOG_RULES -> List.of(
                    link(cityName + " approved haulers", seedRegistry.routeFor(profileId, RouteTemplate.APPROVED_HAULERS).path()),
                    link(cityName + " hood requirements", seedRegistry.routeFor(profileId, RouteTemplate.HOOD_REQUIREMENTS).path()),
                    link(cityName + " fire inspection checklist", seedRegistry.routeFor(profileId, RouteTemplate.INSPECTION_CHECKLIST).path())
            );
            case APPROVED_HAULERS -> List.of(
                    link(cityName + " grease trap rules", seedRegistry.routeFor(profileId, RouteTemplate.FOG_RULES).path()),
                    link(cityName + " grease service finder", seedRegistry.routeFor(profileId, RouteTemplate.FIND_GREASE_SERVICE).path()),
                    link(cityName + " fire inspection checklist", seedRegistry.routeFor(profileId, RouteTemplate.INSPECTION_CHECKLIST).path())
            );
            case HOOD_REQUIREMENTS -> List.of(
                    link(cityName + " fire inspection checklist", seedRegistry.routeFor(profileId, RouteTemplate.INSPECTION_CHECKLIST).path()),
                    link(cityName + " hood cleaner finder", seedRegistry.routeFor(profileId, RouteTemplate.FIND_HOOD_CLEANER).path()),
                    link(cityName + " grease trap rules", seedRegistry.routeFor(profileId, RouteTemplate.FOG_RULES).path())
            );
            case INSPECTION_CHECKLIST -> List.of(
                    link(cityName + " grease trap rules", seedRegistry.routeFor(profileId, RouteTemplate.FOG_RULES).path()),
                    link(cityName + " hood requirements", seedRegistry.routeFor(profileId, RouteTemplate.HOOD_REQUIREMENTS).path()),
                    link(cityName + " hood cleaner finder", seedRegistry.routeFor(profileId, RouteTemplate.FIND_HOOD_CLEANER).path())
            );
            case FIND_GREASE_SERVICE -> List.of(
                    link(cityName + " grease trap rules", seedRegistry.routeFor(profileId, RouteTemplate.FOG_RULES).path()),
                    link(cityName + " approved haulers", seedRegistry.routeFor(profileId, RouteTemplate.APPROVED_HAULERS).path()),
                    link(cityName + " fire inspection checklist", seedRegistry.routeFor(profileId, RouteTemplate.INSPECTION_CHECKLIST).path())
            );
            case FIND_HOOD_CLEANER -> List.of(
                    link(cityName + " hood requirements", seedRegistry.routeFor(profileId, RouteTemplate.HOOD_REQUIREMENTS).path()),
                    link(cityName + " fire inspection checklist", seedRegistry.routeFor(profileId, RouteTemplate.INSPECTION_CHECKLIST).path()),
                    link(cityName + " grease trap rules", seedRegistry.routeFor(profileId, RouteTemplate.FOG_RULES).path())
            );
        };
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

    private String canonicalUrl(String path) {
        if (path.equals("/")) {
            return siteProperties.baseUrl();
        }
        return siteProperties.baseUrl() + path;
    }

    private String structuredData(String type, String name, String url, String description) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("@context", "https://schema.org");
        payload.put("@type", type);
        payload.put("name", name);
        payload.put("url", url);
        if (description != null) {
            payload.put("description", description);
        }
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to create structured data.", ex);
        }
    }
}
