package owner.kitchencompliance.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import owner.kitchencompliance.data.ApprovedHaulerMode;
import owner.kitchencompliance.data.IssueType;
import owner.kitchencompliance.data.PageFamily;
import owner.kitchencompliance.data.ProviderRecord;
import owner.kitchencompliance.data.ProviderType;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.data.SeedRegistry;
import owner.kitchencompliance.ops.IndexingPolicyService;

@Service
public class AttributionService {

    private static final String HEADER =
            "captured_at,event_type,visitor_id,verdict_state,city,state,page_family,issue_type,authority_id,source_path,target_path,target_url,provider_id,provider_type,cta_type,sponsored,tool_slug\n";
    private static final String VISITOR_COOKIE_NAME = "kch_vid";
    private static final int VISITOR_COOKIE_MAX_AGE_SECONDS = 180 * 24 * 60 * 60;

    private final AttributionProperties properties;
    private final Clock clock;
    private final SeedRegistry seedRegistry;
    private final IndexingPolicyService indexingPolicyService;

    @Autowired
    public AttributionService(
            AttributionProperties properties,
            SeedRegistry seedRegistry,
            IndexingPolicyService indexingPolicyService
    ) {
        this(properties, Clock.systemDefaultZone(), seedRegistry, indexingPolicyService);
    }

    public AttributionService(
            AttributionProperties properties,
            Clock clock,
            SeedRegistry seedRegistry,
            IndexingPolicyService indexingPolicyService
    ) {
        this.properties = properties;
        this.clock = clock;
        this.seedRegistry = seedRegistry;
        this.indexingPolicyService = indexingPolicyService;
    }

    public String providerClickPath(RouteRecord route, ProviderRecord provider) {
        return UriComponentsBuilder.fromPath("/out/providers/{providerId}")
                .queryParam("source", route.path())
                .buildAndExpand(provider.providerId())
                .toUriString();
    }

    public String ctaClickPath(RouteRecord route, String targetPath, boolean sponsored) {
        return UriComponentsBuilder.fromPath("/out/cta")
                .queryParam("source", route.path())
                .queryParam("target", targetPath)
                .queryParam("sponsored", sponsored)
                .toUriString();
    }

    public String ensureVisitorId(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (VISITOR_COOKIE_NAME.equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                    return cookie.getValue();
                }
            }
        }

        String visitorId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(VISITOR_COOKIE_NAME, visitorId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(VISITOR_COOKIE_MAX_AGE_SECONDS);
        response.addCookie(cookie);
        return visitorId;
    }

    public void recordLocalPageView(RouteRecord route, String visitorId) {
        appendRow(List.of(
                OffsetDateTime.now(clock).toString(),
                "page_view",
                visitorId,
                verdictStateForRoute(route),
                route.city(),
                route.state(),
                pageFamilyValue(route.template().pageFamily()),
                issueTypeValue(issueTypeFor(route.template())),
                route.authorityId(),
                route.path(),
                "",
                "",
                "",
                "",
                "page_view",
                Boolean.toString(false),
                ""
        ));
    }

    public void recordOperatorToolView(String slug, String visitorId, IssueType issueType) {
        appendRow(List.of(
                OffsetDateTime.now(clock).toString(),
                "page_view",
                visitorId,
                "operator_tool",
                "",
                "",
                pageFamilyValue(PageFamily.OPERATOR_TOOL),
                issueTypeValue(issueType),
                "",
                "/tools/" + slug,
                "",
                "",
                "",
                "",
                "page_view",
                Boolean.toString(false),
                slug
        ));
    }

    public void recordProviderClick(RouteRecord route, ProviderRecord provider, String visitorId) {
        appendRow(List.of(
                OffsetDateTime.now(clock).toString(),
                "provider_click",
                visitorId,
                verdictStateForRoute(route),
                route.city(),
                route.state(),
                pageFamilyValue(route.template().pageFamily()),
                issueTypeValue(issueTypeFor(route.template())),
                route.authorityId(),
                route.path(),
                "",
                provider.siteUrl(),
                provider.providerId(),
                provider.providerType().name().toLowerCase(),
                "provider_outbound",
                Boolean.toString(provider.listingMode() != owner.kitchencompliance.data.ListingMode.PUBLIC),
                ""
        ));
    }

    public void recordCtaClick(RouteRecord route, String targetPath, boolean sponsored, String visitorId) {
        appendRow(List.of(
                OffsetDateTime.now(clock).toString(),
                "cta_click",
                visitorId,
                verdictStateForRoute(route),
                route.city(),
                route.state(),
                pageFamilyValue(route.template().pageFamily()),
                issueTypeValue(issueTypeFor(route.template())),
                route.authorityId(),
                route.path(),
                targetPath,
                "",
                "",
                "",
                sponsored ? "sponsor_cta" : "next_action_cta",
                Boolean.toString(sponsored),
                ""
        ));
    }

    private void appendRow(List<String> columns) {
        if (!properties.enabled()) {
            return;
        }

        try {
            Path logDir = properties.logDirectoryPath();
            Files.createDirectories(logDir);
            Path logFile = properties.logFilePath();
            if (Files.notExists(logFile)) {
                Files.writeString(logFile, HEADER, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
            }
            Files.writeString(
                    logFile,
                    csvLine(columns),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND
            );
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to append attribution event.", ex);
        }
    }

    public String verdictStateForRoute(RouteRecord route) {
        return switch (route.template()) {
            case FOG_RULES, APPROVED_HAULERS -> approvedHaulerModeLabel(route);
            case HOOD_REQUIREMENTS -> "hood_rule";
            case INSPECTION_CHECKLIST -> "inspection_prep";
            case FIND_GREASE_SERVICE, FIND_HOOD_CLEANER -> finderVerdictLabel(route);
        };
    }

    private String approvedHaulerModeLabel(RouteRecord route) {
        ApprovedHaulerMode mode = seedRegistry.fogRule(route.profileId()).approvedHaulerMode();
        return switch (mode) {
            case OFFICIAL_LIST -> "official_list";
            case OPERATOR_MUST_VERIFY -> "operator_must_verify";
            case UNCLEAR -> "unclear";
        };
    }

    private String finderVerdictLabel(RouteRecord route) {
        ProviderType providerType = route.template() == RouteTemplate.FIND_HOOD_CLEANER
                ? ProviderType.HOOD_CLEANER
                : ProviderType.GREASE_HAULER;
        int renderableProviders = indexingPolicyService.renderableProviderCount(
                seedRegistry.providersFor(route.profileId(), providerType)
        );
        if (renderableProviders == 0) {
            return "manual_only";
        }
        if (renderableProviders == 1) {
            return "provider_single";
        }
        return "provider_multi";
    }

    private String csvLine(List<String> columns) {
        return columns.stream()
                .map(this::escapeCsv)
                .reduce((left, right) -> left + "," + right)
                .orElse("")
                + "\n";
    }

    private String escapeCsv(String value) {
        String safe = value == null ? "" : value;
        if (safe.contains(",") || safe.contains("\"") || safe.contains("\n")) {
            return "\"" + safe.replace("\"", "\"\"") + "\"";
        }
        return safe;
    }

    private IssueType issueTypeFor(RouteTemplate template) {
        return switch (template) {
            case FOG_RULES, APPROVED_HAULERS, FIND_GREASE_SERVICE -> IssueType.FOG_CLEANING;
            case HOOD_REQUIREMENTS, FIND_HOOD_CLEANER -> IssueType.HOOD_CLEANING;
            case INSPECTION_CHECKLIST -> IssueType.INSPECTION_PREP;
        };
    }

    private String pageFamilyValue(PageFamily pageFamily) {
        return switch (pageFamily) {
            case FOG_RULES -> "fog_rules";
            case APPROVED_HAULERS -> "approved_haulers";
            case HOOD_REQUIREMENTS -> "hood_requirements";
            case INSPECTION_CHECKLIST -> "inspection_checklist";
            case PROVIDER_FINDER -> "provider_finder";
            case OPERATOR_TOOL -> "operator_tool";
        };
    }

    private String issueTypeValue(IssueType issueType) {
        return switch (issueType) {
            case FOG_CLEANING -> "fog_cleaning";
            case MANIFEST_OR_LOG -> "manifest_or_log";
            case HOOD_CLEANING -> "hood_cleaning";
            case INSPECTION_PREP -> "inspection_prep";
            case VENDOR_SEARCH -> "vendor_search";
            case OPERATOR_UTILITY -> "operator_utility";
        };
    }
}
