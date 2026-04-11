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
                        "Separate interceptor maintenance, waste hauling, and on-site proof so operators do not confuse one obligation for another.",
                        List.of(
                                new GuideSection("What changes by local rule", List.of(
                                        "Cities and utilities can set different maintenance triggers, approved-hauler rules, and retention windows.",
                                        "The safest workflow starts with the local rule holder, not a vendor sales page."
                                )),
                                new GuideSection("What should always stay on site", List.of(
                                        "Recent manifests or trip tickets.",
                                        "The current service cadence decision and the reason it was chosen.",
                                        "Any city- or utility-specific permit or approval paperwork."
                                ))
                        ),
                        List.of(
                                new GuideAuthorityReference("Austin grease workflow", "austin-tx-kitchen-compliance", RouteTemplate.FOG_RULES),
                                new GuideAuthorityReference("Santa Clara grease workflow", "santa-clara-ca-kitchen-compliance", RouteTemplate.FOG_RULES),
                                new GuideAuthorityReference("Charlotte grease workflow", "charlotte-nc-kitchen-compliance", RouteTemplate.FOG_RULES)
                        ),
                        List.of(
                                new RelatedPageLink("Austin grease trap rules", "/tx/austin/restaurant-grease-trap-rules"),
                                new RelatedPageLink("Santa Clara grease trap rules", "/ca/santa-clara/restaurant-grease-trap-rules"),
                                new RelatedPageLink("Charlotte grease trap rules", "/nc/charlotte/restaurant-grease-trap-rules")
                        )
                ),
                new GuideDefinition(
                        "how-often-clean-commercial-hood",
                        "How often to clean a commercial hood",
                        "Use the local fire authority's published inspection and paperwork rules first, then fit service cadence around the real cooking load.",
                        List.of(
                                new GuideSection("What local pages should answer", List.of(
                                        "Which authority governs the kitchen hood system.",
                                        "Whether the city publishes an inspection schedule, retention rule, or service-tag expectation.",
                                        "What proof staff must produce on inspection day."
                                )),
                                new GuideSection("Avoid the common shortcut", List.of(
                                        "Do not collapse hood cleaning, suppression service, and inspection prep into one vague reminder.",
                                        "Keep separate dates and reports whenever the authority treats them separately."
                                ))
                        ),
                        List.of(
                                new GuideAuthorityReference("Santa Clara hood workflow", "santa-clara-ca-kitchen-compliance", RouteTemplate.HOOD_REQUIREMENTS),
                                new GuideAuthorityReference("Tampa hood workflow", "tampa-fl-kitchen-compliance", RouteTemplate.HOOD_REQUIREMENTS),
                                new GuideAuthorityReference("Portland hood workflow", "portland-or-kitchen-compliance", RouteTemplate.HOOD_REQUIREMENTS)
                        ),
                        List.of(
                                new RelatedPageLink("Santa Clara hood requirements", "/ca/santa-clara/hood-cleaning-requirements"),
                                new RelatedPageLink("Tampa hood requirements", "/fl/tampa/hood-cleaning-requirements"),
                                new RelatedPageLink("Portland hood requirements", "/or/portland/hood-cleaning-requirements")
                        )
                ),
                new GuideDefinition(
                        "what-records-restaurant-inspections-check",
                        "What records restaurant inspections check",
                        "Treat inspection prep as a records problem first: proof must be ready before the inspector asks for it.",
                        List.of(
                                new GuideSection("Record categories that matter", List.of(
                                        "Grease manifests and permit checks.",
                                        "Hood-system reports, service tags, and suppression follow-up.",
                                        "Any city-specific approval letters tied to the site."
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
                                new GuideAuthorityReference("Portland inspection workflow", "portland-or-kitchen-compliance", RouteTemplate.INSPECTION_CHECKLIST)
                        ),
                        List.of(
                                new RelatedPageLink("Austin fire inspection checklist", "/tx/austin/restaurant-fire-inspection-checklist"),
                                new RelatedPageLink("Charlotte fire inspection checklist", "/nc/charlotte/restaurant-fire-inspection-checklist"),
                                new RelatedPageLink("Portland fire inspection checklist", "/or/portland/restaurant-fire-inspection-checklist")
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
