package owner.kitchencompliance.web;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.IssueType;
import owner.kitchencompliance.data.PageFamily;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;

@Service
public class LeadCaptureService {

    private static final String HEADER =
            "captured_at,lead_type,visitor_id,city,state,page_family,issue_type,authority_id,source_path,verdict_state,provider_intent,contact_name,business_name,email,phone,coverage_note,notes,routing_consent\n";

    private final LeadCaptureProperties properties;
    private final Clock clock;
    private final AttributionService attributionService;

    @Autowired
    public LeadCaptureService(
            LeadCaptureProperties properties,
            AttributionService attributionService
    ) {
        this(properties, Clock.systemDefaultZone(), attributionService);
    }

    public LeadCaptureService(
            LeadCaptureProperties properties,
            Clock clock,
            AttributionService attributionService
    ) {
        this.properties = properties;
        this.clock = clock;
        this.attributionService = attributionService;
    }

    public CaptureResult captureOperatorLead(
            RouteRecord route,
            String sourcePath,
            HttpServletRequest request,
            HttpServletResponse response,
            String contactName,
            String businessName,
            String email,
            String phone,
            String notes,
            boolean routingConsent
    ) {
        if (route.template() != RouteTemplate.FIND_GREASE_SERVICE && route.template() != RouteTemplate.FIND_HOOD_CLEANER) {
            return CaptureResult.invalid("operator-invalid");
        }
        if (!isValidContact(contactName, businessName, email) || !routingConsent) {
            return CaptureResult.invalid(routingConsent ? "operator-invalid" : "consent-required");
        }

        String visitorId = attributionService.ensureVisitorId(request, response);
        appendRow(List.of(
                OffsetDateTime.now(clock).toString(),
                "operator_request",
                visitorId,
                route.city(),
                route.state(),
                pageFamilyValue(route.template().pageFamily()),
                issueTypeValue(issueTypeFor(route.template())),
                route.authorityId(),
                sourcePath,
                attributionService.verdictStateForRoute(route),
                operatorIntent(route.template()),
                contactName.trim(),
                businessName.trim(),
                email.trim(),
                safe(phone),
                "",
                safe(notes),
                Boolean.toString(true)
        ));
        return CaptureResult.success("operator-submitted");
    }

    public CaptureResult captureSponsorInquiry(
            RouteRecord route,
            String sourcePath,
            HttpServletRequest request,
            HttpServletResponse response,
            String contactName,
            String businessName,
            String email,
            String phone,
            String coverageNote,
            String notes,
            boolean routingConsent
    ) {
        if (!isValidContact(contactName, businessName, email) || !routingConsent) {
            return CaptureResult.invalid(routingConsent ? "sponsor-invalid" : "consent-required");
        }

        String visitorId = attributionService.ensureVisitorId(request, response);
        appendRow(List.of(
                OffsetDateTime.now(clock).toString(),
                "sponsor_inquiry",
                visitorId,
                route.city(),
                route.state(),
                pageFamilyValue(route.template().pageFamily()),
                issueTypeValue(issueTypeFor(route.template())),
                route.authorityId(),
                sourcePath,
                attributionService.verdictStateForRoute(route),
                sponsorIntent(route.template()),
                contactName.trim(),
                businessName.trim(),
                email.trim(),
                safe(phone),
                safe(coverageNote),
                safe(notes),
                Boolean.toString(true)
        ));
        return CaptureResult.success("sponsor-submitted");
    }

    private boolean isValidContact(String contactName, String businessName, String email) {
        return !isBlank(contactName) && !isBlank(businessName) && !isBlank(email);
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
            Files.writeString(logFile, csvLine(columns), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to append lead capture event.", ex);
        }
    }

    private String operatorIntent(RouteTemplate template) {
        return switch (template) {
            case FIND_GREASE_SERVICE -> "need_grease_service";
            case FIND_HOOD_CLEANER -> "need_hood_cleaner";
            default -> "operator_request";
        };
    }

    private String sponsorIntent(RouteTemplate template) {
        return switch (template) {
            case FOG_RULES, APPROVED_HAULERS, FIND_GREASE_SERVICE -> "grease_service_sponsor_slot";
            case HOOD_REQUIREMENTS, FIND_HOOD_CLEANER -> "hood_cleaning_sponsor_slot";
            case INSPECTION_CHECKLIST -> "inspection_prep_sponsor_slot";
        };
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

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public record CaptureResult(
            boolean success,
            String noticeCode
    ) {
        public static CaptureResult success(String noticeCode) {
            return new CaptureResult(true, noticeCode);
        }

        public static CaptureResult invalid(String noticeCode) {
            return new CaptureResult(false, noticeCode);
        }
    }
}
