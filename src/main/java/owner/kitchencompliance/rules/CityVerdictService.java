package owner.kitchencompliance.rules;

import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.ApprovedHaulerMode;
import owner.kitchencompliance.data.CityComplianceProfile;
import owner.kitchencompliance.data.FogRuleRecord;
import owner.kitchencompliance.data.HoodRuleRecord;
import owner.kitchencompliance.data.InspectionPrepRecord;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.model.CityVerdict;

@Service
public class CityVerdictService {

    public CityVerdict create(
            RouteRecord route,
            CityComplianceProfile profile,
            String authoritySummary,
            FogRuleRecord fogRule,
            HoodRuleRecord hoodRule,
            InspectionPrepRecord inspectionPrep
    ) {
        String cityName = displayCity(profile.city());
        return switch (route.template()) {
            case FOG_RULES -> new CityVerdict(
                    cityName,
                    profile.state(),
                    authoritySummary,
                    List.of(
                            fogRule.foodServiceApplicability(),
                            cityName + " requires an approved interceptor setup before wastewater enters the sanitary sewer.",
                            fogRule.pumpOutFrequency()
                    ),
                    List.of(
                            fogRule.manifestRequirement(),
                            "Approval letters and device details from plan review.",
                            "Hauler trip tickets retained on site."
                    ),
                    List.of(
                            fogRule.enforcementNote(),
                            "Missing manifests or overdue cleaning will slow inspection readiness.",
                            "A weak hauler paper trail can trigger follow-up from pretreatment staff."
                    ),
                    List.of(
                            "Verify the installed interceptor type against the city approval letter.",
                            "Match cleaning cadence to the local baseline and accumulation trigger described by the authority.",
                            "Keep manifest records accessible for staff and inspectors."
                    ),
                    fogRule.approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST ? "official_list" : "manual_verification"
            );
            case APPROVED_HAULERS -> new CityVerdict(
                    cityName,
                    profile.state(),
                    authoritySummary,
                    approvedHaulerRequirements(cityName, fogRule.approvedHaulerMode()),
                    List.of(
                            "Recent manifests or trip tickets.",
                            fogRule.approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST
                                    ? "The vendor's current listing or program status in the city's published registry."
                                    : "The vendor's current self-verification proof for grease-waste coverage.",
                            "Receiving-station or disposal paperwork when applicable."
                    ),
                    List.of(
                            fogRule.approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST
                                    ? "Using a vendor outside the published registry can break the paper trail " + cityName + " expects."
                                    : "Using an unverified vendor can break the paper trail " + cityName + " expects.",
                            "An outdated provider check can leave the operator without defensible records."
                    ),
                    approvedHaulerActions(cityName, fogRule.approvedHaulerMode()),
                    fogRule.approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST ? "official_list" : "manual_verification"
            );
            case HOOD_REQUIREMENTS -> new CityVerdict(
                    cityName,
                    profile.state(),
                    authoritySummary,
                    List.of(
                            hoodRule.cleaningFrequencyBands().get(0),
                            hoodRule.cleaningFrequencyBands().get(1),
                            hoodRule.suppressionInspectionRequirement()
                    ),
                    List.of(
                            hoodRule.certificateRequirement(),
                            hoodRule.serviceTagRequirement(),
                            hoodRule.reportRetentionRule()
                    ),
                    List.of(
                            "Missing tags or reports weakens the fire-inspection story immediately.",
                            cityName + "'s published schedule distinguishes hood-system service from general inspection prep.",
                            "Suppression work and hood cleaning should not be treated as the same record."
                    ),
                    List.of(
                            "Keep the latest hood-system report on site.",
                            "Confirm the visible tag matches the latest service paperwork.",
                            "If a fire inspection is coming up, review both hood and suppression records together."
                    ),
                    "schedule_service"
            );
            case INSPECTION_CHECKLIST -> new CityVerdict(
                    cityName,
                    profile.state(),
                    authoritySummary,
                    List.copyOf(inspectionPrep.whatMustBeOnSite()),
                    List.copyOf(inspectionPrep.whatMustBeOnSite()),
                    List.copyOf(inspectionPrep.commonFailureReasons()),
                    List.of(
                            "Stage every required record in one binder or digital packet that staff can reach quickly.",
                            "Fix missing tags, expired reports, or blocked egress before requesting inspection.",
                            inspectionPrep.rescheduleMethod()
                    ),
                    "inspection_prep"
            );
            case FIND_GREASE_SERVICE -> new CityVerdict(
                    cityName,
                    profile.state(),
                    authoritySummary,
                    List.of(
                            fogRule.pumpOutFrequency(),
                            fogRule.approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST
                                    ? "Your vendor must align with the city's published hauler or preferred-pumper program."
                                    : "Your vendor must align with the city's grease-hauling verification workflow.",
                            "Manifest retention is part of the operating requirement, not just a vendor detail."
                    ),
                    List.of(
                            fogRule.manifestRequirement(),
                            fogRule.approvedHaulerMode() == ApprovedHaulerMode.OFFICIAL_LIST
                                    ? "The current registry or preferred-pumper status check."
                                    : "The current vendor verification check.",
                            "Your last cleaning date and next due window."
                    ),
                    List.of(
                            "Waiting until the device is already at the 50% threshold raises inspection risk.",
                            "A missing provider verification check turns a service call into a compliance problem."
                    ),
                    greaseFinderActions(fogRule.approvedHaulerMode()),
                    "vendor_search"
            );
            case FIND_HOOD_CLEANER -> new CityVerdict(
                    cityName,
                    profile.state(),
                    authoritySummary,
                    List.of(
                            hoodRule.cleaningFrequencyBands().get(0),
                            hoodRule.reportRetentionRule(),
                            hoodRule.suppressionInspectionRequirement()
                    ),
                    List.of(
                            hoodRule.certificateRequirement(),
                            hoodRule.serviceTagRequirement(),
                            "The last cleaning report and any suppression follow-up."
                    ),
                    List.of(
                            "If records are fragmented across vendors, fire inspection prep gets harder fast.",
                            "A generic hood-cleaned claim is weaker than a real report and visible tag."
                    ),
                    List.of(
                            "Request a provider that can supply inspection-ready paperwork.",
                            "Confirm whether hood cleaning and suppression work are separate visits.",
                            "Stage the final report where staff can show it immediately."
                    ),
                    "vendor_search"
            );
        };
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

    private List<String> approvedHaulerRequirements(String cityName, ApprovedHaulerMode mode) {
        if (mode == ApprovedHaulerMode.OFFICIAL_LIST) {
            return List.of(
                    cityName + " publishes an official hauler or preferred-pumper registry for grease service.",
                    "The published registry is a verification tool, not a recommendation or endorsement.",
                    "Operators still need to confirm waste-type coverage and current standing before booking."
            );
        }
        return List.of(
                cityName + " does not publish a safe approved-hauler list for this workflow.",
                "Operators need a self-verification workflow for grease-waste coverage and paper-trail requirements.",
                "Vendor routing must stay separate from authority guidance until verification is complete."
        );
    }

    private List<String> approvedHaulerActions(String cityName, ApprovedHaulerMode mode) {
        if (mode == ApprovedHaulerMode.OFFICIAL_LIST) {
            return List.of(
                    "Start from the city's published registry, then confirm the vendor still covers grease waste.",
                    "Keep the registry check with your manifests.",
                    "If a vendor cannot verify coverage, switch before the next pump-out."
            );
        }
        return List.of(
                "Ask the vendor to confirm grease-waste coverage and disposal workflow in writing.",
                "Keep the verification check together with your manifests.",
                "If a vendor cannot verify coverage, switch before the next pump-out."
        );
    }

    private List<String> greaseFinderActions(ApprovedHaulerMode mode) {
        if (mode == ApprovedHaulerMode.OFFICIAL_LIST) {
            return List.of(
                    "Use the published hauler or preferred-pumper registry as the first filter.",
                    "Ask the vendor to confirm grease-waste coverage and manifest handling.",
                    "Store the registry check with your next trip ticket."
            );
        }
        return List.of(
                "Use a documented self-verification checklist before booking service.",
                "Ask the vendor to confirm grease-waste coverage and manifest handling.",
                "Store the verification check with your next trip ticket."
        );
    }
}
