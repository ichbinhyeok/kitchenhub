package owner.kitchencompliance.web;

import java.util.List;
import org.springframework.stereotype.Component;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.model.GuideSection;
import owner.kitchencompliance.model.RelatedPageLink;

@Component
public class GuideCatalog {

    public GuideDefinition guide(String slug) {
        return allGuides().stream()
                .filter(guide -> guide.slug().equals(slug))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown guide slug: " + slug));
    }

    public List<GuideDefinition> allGuides() {
        return List.of(
                new GuideDefinition(
                        "fog-vs-grease-trap-cleaning",
                        "FOG vs. grease trap cleaning",
                        "Separate interceptor maintenance, waste hauling, and on-site paperwork so staff do not book the wrong service for the wrong problem.",
                        List.of(
                                new GuideSection("When operators mix these up", List.of(
                                        "A grease trap cleaning visit does not automatically satisfy hauling or manifest requirements.",
                                        "A hauling ticket does not prove the interceptor was maintained the way the city expects.",
                                        "The wrong assumption usually shows up when staff cannot explain the last service or find the paperwork."
                                )),
                                new GuideSection("What to check before you call service", List.of(
                                        "Which office owns the grease rule for your city: utility, county, or local department.",
                                        "Whether the city uses a published hauler list, a transporter permit workflow, or an operator verification step.",
                                        "Whether your site is due for pump-out, cleaning, reporting, or simply records cleanup."
                                )),
                                new GuideSection("What should stay on site", List.of(
                                        "Recent manifests, trip tickets, or pump-out receipts.",
                                        "The current service cadence decision and why it was chosen.",
                                        "Any permit, approval letter, or program paperwork tied to the interceptor."
                                )),
                                new GuideSection("What to do next", List.of(
                                        "Open the local grease rule page before booking service.",
                                        "Confirm what the crew must leave behind after the visit.",
                                        "File the paperwork in the binder or grease log the same day."
                                ))
                        ),
                        List.of(
                                new GuideAuthorityReference("Austin grease workflow", "austin-tx-kitchen-compliance", RouteTemplate.FOG_RULES),
                                new GuideAuthorityReference("Charlotte grease workflow", "charlotte-nc-kitchen-compliance", RouteTemplate.FOG_RULES),
                                new GuideAuthorityReference("Miami grease workflow", "miami-fl-kitchen-compliance", RouteTemplate.FOG_RULES)
                        ),
                        List.of(
                                new RelatedPageLink("Austin grease trap rules", "/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules"),
                                new RelatedPageLink("Charlotte grease trap rules", "/authority/nc/charlotte-water-flow-free/restaurant-grease-trap-rules"),
                                new RelatedPageLink("Miami grease trap rules", "/fl/miami/restaurant-grease-trap-rules")
                        )
                ),
                new GuideDefinition(
                        "how-often-clean-commercial-hood",
                        "How often to clean a commercial hood",
                        "Start with the local fire paperwork rule, then fit cleaning cadence around the cooking load, the system type, and the proof inspectors expect to see.",
                        List.of(
                                new GuideSection("What the question really means", List.of(
                                        "Operators usually ask about cleaning frequency when they are actually worried about failed inspections, overdue reports, or missing tags.",
                                        "The right answer is not just a cadence. It is the cadence plus the paperwork the local fire office expects."
                                )),
                                new GuideSection("What local pages should answer", List.of(
                                        "Which office governs the hood, duct, and suppression paperwork.",
                                        "Whether the city publishes a schedule, a retention rule, or a tag requirement.",
                                        "What report, tag, or certificate staff must produce on inspection day."
                                )),
                                new GuideSection("Avoid the common shortcut", List.of(
                                        "Do not collapse hood cleaning, suppression service, and inspection prep into one vague reminder.",
                                        "Keep separate dates and reports whenever the local rule treats them separately.",
                                        "A cleaning crew visit does not replace a suppression inspection unless the city says it does."
                                )),
                                new GuideSection("What to do after each visit", List.of(
                                        "Match the new report to the hood system it covers.",
                                        "Check that tags, stickers, or certificates are current and legible.",
                                        "Move the paperwork into the hood binder before the next inspection window."
                                ))
                        ),
                        List.of(
                                new GuideAuthorityReference("Austin hood workflow", "austin-tx-kitchen-compliance", RouteTemplate.HOOD_REQUIREMENTS),
                                new GuideAuthorityReference("Charlotte hood workflow", "charlotte-nc-kitchen-compliance", RouteTemplate.HOOD_REQUIREMENTS),
                                new GuideAuthorityReference("Miami hood workflow", "miami-fl-kitchen-compliance", RouteTemplate.HOOD_REQUIREMENTS)
                        ),
                        List.of(
                                new RelatedPageLink("Austin hood requirements", "/authority/tx/austin-fire-marshal/hood-cleaning-requirements"),
                                new RelatedPageLink("Charlotte hood requirements", "/authority/nc/charlotte-fire-prevention/hood-cleaning-requirements"),
                                new RelatedPageLink("Miami hood requirements", "/fl/miami/hood-cleaning-requirements")
                        )
                ),
                new GuideDefinition(
                        "what-records-restaurant-inspections-check",
                        "What records restaurant inspections check",
                        "Treat inspection prep as a records problem first: the fastest way to fail is to know the rule but not have the paperwork ready when the inspector asks.",
                        List.of(
                                new GuideSection("What inspectors usually ask for first", List.of(
                                        "The latest hood cleaning or suppression records.",
                                        "Grease manifests, haul tickets, or maintenance logs.",
                                        "Any permit or approval paperwork tied to the site."
                                )),
                                new GuideSection("Record categories that matter", List.of(
                                        "Grease manifests and permit checks.",
                                        "Hood-system reports, service tags, and suppression follow-up.",
                                        "Any city-specific approval letters tied to the site."
                                )),
                                new GuideSection("How to stage the binder", List.of(
                                        "Group records by the office that can ask for them.",
                                        "Put the most recent proof at the front so staff can reach it fast.",
                                        "Flag anything expired, missing, or waiting on follow-up."
                                )),
                                new GuideSection("Prep workflow", List.of(
                                        "Match each record to the authority that can ask for it.",
                                        "Move missing or expired items into a repair queue before the inspection window.",
                                        "Only place service CTAs after the rule and proof burden are clear."
                                ))
                        ),
                        List.of(
                                new GuideAuthorityReference("Austin inspection workflow", "austin-tx-kitchen-compliance", RouteTemplate.INSPECTION_CHECKLIST),
                                new GuideAuthorityReference("Charlotte inspection workflow", "charlotte-nc-kitchen-compliance", RouteTemplate.INSPECTION_CHECKLIST),
                                new GuideAuthorityReference("Miami inspection workflow", "miami-fl-kitchen-compliance", RouteTemplate.INSPECTION_CHECKLIST)
                        ),
                        List.of(
                                new RelatedPageLink("Austin fire inspection checklist", "/authority/tx/austin-fire-marshal/restaurant-fire-inspection-checklist"),
                                new RelatedPageLink("Charlotte fire inspection checklist", "/authority/nc/charlotte-fire-prevention/restaurant-fire-inspection-checklist"),
                                new RelatedPageLink("Miami fire inspection checklist", "/authority/fl/miami-dade-fire-rescue/restaurant-fire-inspection-checklist")
                        )
                )
        );
    }

    public record GuideDefinition(
            String slug,
            String title,
            String summary,
            List<GuideSection> sections,
            List<GuideAuthorityReference> authorityReferences,
            List<RelatedPageLink> relatedLinks
    ) {
    }

    public record GuideAuthorityReference(
            String title,
            String profileId,
            RouteTemplate template
    ) {
    }
}
