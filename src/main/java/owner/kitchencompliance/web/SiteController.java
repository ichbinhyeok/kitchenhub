package owner.kitchencompliance.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import owner.kitchencompliance.data.ProviderRecord;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.model.ResolvedPage;
import owner.kitchencompliance.model.SitemapEntry;

@Controller
public class SiteController {

    private final SitePageService sitePageService;
    private final AdminPageService adminPageService;
    private final SiteProperties siteProperties;
    private final SeedRegistry seedRegistry;
    private final AttributionService attributionService;
    private final OperatorToolService operatorToolService;
    private final LeadCaptureService leadCaptureService;

    public SiteController(
            SitePageService sitePageService,
            AdminPageService adminPageService,
            SiteProperties siteProperties,
            SeedRegistry seedRegistry,
            AttributionService attributionService,
            OperatorToolService operatorToolService,
            LeadCaptureService leadCaptureService
    ) {
        this.sitePageService = sitePageService;
        this.adminPageService = adminPageService;
        this.siteProperties = siteProperties;
        this.seedRegistry = seedRegistry;
        this.attributionService = attributionService;
        this.operatorToolService = operatorToolService;
        this.leadCaptureService = leadCaptureService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("page", sitePageService.homePage());
        return "home";
    }

    @GetMapping("/guides/{slug}")
    public String guide(@PathVariable String slug, Model model) {
        try {
            model.addAttribute("page", sitePageService.guidePage(slug));
            return "guide-page";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/authorities")
    public String authorityIndex(@RequestParam(required = false) String type, Model model) {
        try {
            model.addAttribute("page", sitePageService.authorityIndexPage(type));
            return "authority-browse";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @GetMapping("/authorities/{state}/{authorityId}")
    public String authorityDetail(@PathVariable String state, @PathVariable String authorityId, Model model) {
        try {
            model.addAttribute("page", sitePageService.authorityDetailPage(state, authorityId));
            return "authority-browse";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping({
            "/about",
            "/methodology",
            "/contact",
            "/privacy",
            "/terms",
            "/sponsor-policy",
            "/not-government-affiliated",
            "/corrections"
    })
    public String infoPage(HttpServletRequest request, Model model) {
        String slug = request.getRequestURI().replaceFirst("^/", "");
        try {
            model.addAttribute("page", sitePageService.infoPage(slug));
            return "info-page";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/{state}/{city}/{slug}")
    public String localPage(
            @PathVariable String state,
            @PathVariable String city,
            @PathVariable String slug,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) String lead
    ) {
        String path = "/" + state + "/" + city + "/" + slug;
        return renderLocalPage(path, model, request, response, lead);
    }

    @GetMapping("/authority/{state}/{authoritySlug}/{slug}")
    public String authorityLocalPage(
            @PathVariable String state,
            @PathVariable String authoritySlug,
            @PathVariable String slug,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) String lead
    ) {
        String path = "/authority/" + state + "/" + authoritySlug + "/" + slug;
        return renderLocalPage(path, model, request, response, lead);
    }

    private String renderLocalPage(
            String path,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            String lead
    ) {
        try {
            ResolvedPage page = sitePageService.localPage(path, lead);
            model.addAttribute("page", page.page());
            RouteRecord route = seedRegistry.route(path);
            String visitorId = attributionService.ensureVisitorId(request, response);
            attributionService.recordLocalPageView(route, path, visitorId);
            return page.viewName();
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/tools/{slug}")
    public String operatorTool(
            @PathVariable String slug,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            model.addAttribute("page", operatorToolService.toolPage(slug));
            String visitorId = attributionService.ensureVisitorId(request, response);
            attributionService.recordOperatorToolView(slug, visitorId, operatorToolService.issueTypeFor(slug));
            return "operator-tool";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping(value = "/tools/{slug}.csv", produces = "text/csv")
    @ResponseBody
    public String operatorToolCsv(@PathVariable String slug) {
        try {
            return operatorToolService.csvTemplate(slug);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("page", adminPageService.adminPage());
        return "admin";
    }

    @GetMapping(value = "/admin/exports/attribution-events.csv", produces = "text/csv")
    @ResponseBody
    public String attributionEventsExport() {
        return adminPageService.exportAttributionEventsCsv();
    }

    @GetMapping(value = "/admin/exports/attribution-summary.csv", produces = "text/csv")
    @ResponseBody
    public String attributionSummaryExport() {
        return adminPageService.exportAttributionSummaryCsv();
    }

    @GetMapping(value = "/admin/exports/lead-intake.csv", produces = "text/csv")
    @ResponseBody
    public String leadEventsExport() {
        return adminPageService.exportLeadEventsCsv();
    }

    @GetMapping(value = "/admin/exports/lead-summary.csv", produces = "text/csv")
    @ResponseBody
    public String leadSummaryExport() {
        return adminPageService.exportLeadSummaryCsv();
    }

    @GetMapping(value = "/admin/exports/freshness-watch.csv", produces = "text/csv")
    @ResponseBody
    public String freshnessWatchExport() {
        return adminPageService.exportFreshnessWatchCsv();
    }

    @GetMapping(value = "/admin/exports/source-quality-watch.csv", produces = "text/csv")
    @ResponseBody
    public String sourceQualityWatchExport() {
        return adminPageService.exportSourceQualityWatchCsv();
    }

    @GetMapping(value = "/admin/exports/deploy-readiness.csv", produces = "text/csv")
    @ResponseBody
    public String deployReadinessExport() {
        return adminPageService.exportDeployReadinessCsv();
    }

    @GetMapping(value = "/admin/exports/noindex-promotion-queue.csv", produces = "text/csv")
    @ResponseBody
    public String noindexPromotionQueueExport() {
        return adminPageService.exportNoindexPromotionQueueCsv();
    }

    @GetMapping(value = "/admin/exports/search-demand-watch.csv", produces = "text/csv")
    @ResponseBody
    public String searchDemandWatchExport() {
        return adminPageService.exportSearchDemandWatchCsv();
    }

    @GetMapping(value = "/admin/exports/operator-utility-summary.csv", produces = "text/csv")
    @ResponseBody
    public String operatorUtilitySummaryExport() {
        return adminPageService.exportOperatorUtilitySummaryCsv();
    }

    @GetMapping(value = "/admin/exports/evidence-index.csv", produces = "text/csv")
    @ResponseBody
    public String evidenceIndexExport() {
        return adminPageService.exportEvidenceIndexCsv();
    }

    @GetMapping(value = "/admin/exports/ops-alerts.md", produces = "text/markdown")
    @ResponseBody
    public String opsAlertsExport() {
        return adminPageService.exportOpsAlertsMarkdown();
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        List<SitemapEntry> entries = sitePageService.sitemapEntries();
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        for (SitemapEntry entry : entries) {
            builder.append("<url>");
            builder.append("<loc>").append(entry.location()).append("</loc>");
            builder.append("<changefreq>").append(entry.changeFrequency()).append("</changefreq>");
            builder.append("<priority>").append(entry.priority()).append("</priority>");
            builder.append("</url>");
        }
        builder.append("</urlset>");
        return builder.toString();
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String robots() {
        return "User-agent: *\nAllow: /\nSitemap: " + siteProperties.baseUrl() + "/sitemap.xml\n";
    }

    @GetMapping("/out/providers/{providerId}")
    public String providerRedirect(
            @PathVariable String providerId,
            @RequestParam String source,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            RouteRecord route = seedRegistry.route(source);
            ProviderRecord provider = seedRegistry.provider(providerId);
            String visitorId = attributionService.ensureVisitorId(request, response);
            attributionService.recordProviderClick(route, provider, source, visitorId);
            return "redirect:" + provider.siteUrl();
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/out/cta")
    public String ctaRedirect(
            @RequestParam String source,
            @RequestParam String target,
            @RequestParam(defaultValue = "false") boolean sponsored,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (!target.startsWith("/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CTA target must stay local.");
        }
        try {
            RouteRecord route = seedRegistry.route(source);
            String visitorId = attributionService.ensureVisitorId(request, response);
            attributionService.recordCtaClick(route, source, target, sponsored, visitorId);
            return "redirect:" + target;
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("/lead-intake/operator")
    public String operatorLead(
            @RequestParam String source,
            @RequestParam String contactName,
            @RequestParam String businessName,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String notes,
            @RequestParam(defaultValue = "false") boolean routingConsent,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            RouteRecord route = seedRegistry.route(source);
            LeadCaptureService.CaptureResult result = leadCaptureService.captureOperatorLead(
                    route,
                    source,
                    request,
                    response,
                    contactName,
                    businessName,
                    email,
                    phone,
                    notes,
                    routingConsent
            );
            return "redirect:" + source + "?lead=" + result.noticeCode() + "#service-request";
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("/lead-intake/sponsor")
    public String sponsorLead(
            @RequestParam String source,
            @RequestParam String contactName,
            @RequestParam String businessName,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String coverageNote,
            @RequestParam(required = false) String notes,
            @RequestParam(defaultValue = "false") boolean routingConsent,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            RouteRecord route = seedRegistry.route(source);
            LeadCaptureService.CaptureResult result = leadCaptureService.captureSponsorInquiry(
                    route,
                    source,
                    request,
                    response,
                    contactName,
                    businessName,
                    email,
                    phone,
                    coverageNote,
                    notes,
                    routingConsent
            );
            return "redirect:" + source + "?lead=" + result.noticeCode() + "#sponsor-slot";
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
}
