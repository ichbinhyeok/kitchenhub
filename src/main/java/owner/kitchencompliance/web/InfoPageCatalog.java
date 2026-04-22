package owner.kitchencompliance.web;

import java.util.List;
import org.springframework.stereotype.Component;
import owner.kitchencompliance.model.GuideSection;
import owner.kitchencompliance.model.RelatedPageLink;

@Component
public class InfoPageCatalog {

    public InfoPageDefinition page(String slug) {
        return allPages().stream()
                .filter(page -> page.slug().equals(slug))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown info page slug: " + slug));
    }

    public List<InfoPageDefinition> allPages() {
        return List.of(
                new InfoPageDefinition(
                        "about",
                        "About KitchenRuleHub",
                        "An operator-first local compliance workflow",
                        "KitchenRuleHub is an independent product for commercial kitchen operators. It summarizes local authority guidance, keeps on-site record requirements visible, and keeps provider routing secondary to rule clarity.",
                        "AboutPage",
                        "index,follow",
                        List.of(
                                new GuideSection("What the product is", List.of(
                                        "A city- and authority-aware workflow for grease, hood, and inspection preparation.",
                                        "A local summary layer grounded in official source material that points operators to the governing utility, fire authority, county, or city workflow."
                                )),
                                new GuideSection("What the product is not", List.of(
                                        "Not a government site or municipal portal.",
                                        "Not a generic restaurant operations blog.",
                                        "Not a provider directory that outranks local authority guidance."
                                ))
                        ),
                        List.of(
                                new RelatedPageLink("Methodology", "/methodology"),
                                new RelatedPageLink("Not government affiliated", "/not-government-affiliated"),
                                new RelatedPageLink("Corrections policy", "/corrections")
                        )
                ),
                new InfoPageDefinition(
                        "methodology",
                        "Methodology",
                        "How KitchenRuleHub decides what to publish",
                        "Each indexed local route is built from the office that can actually enforce the rule. City display helps operators orient themselves, but the governing rule holder may be a utility, fire marshal, county, or other local authority.",
                        "WebPage",
                        "index,follow",
                        List.of(
                                new GuideSection("Source and route rules", List.of(
                                        "Canonical local pages must identify the authority that can actually enforce the rule.",
                                        "Every local page is expected to show the requirement, the records to keep on site, likely fail conditions, and the next action before service routing."
                                )),
                                new GuideSection("Finder and routing rules", List.of(
                                        "Provider routing stays secondary to local rule clarity.",
                                        "Provider visibility does not change the rule summary, source list, or inspection guidance on a page."
                                )),
                                new GuideSection("Freshness and mixed governance", List.of(
                                        "Last verified dates are shown at the page level, not as a site-wide placeholder.",
                                        "When utility, county, or fire authority rules matter more than city branding, the authority logic wins."
                                ))
                        ),
                        List.of(
                                new RelatedPageLink("About", "/about"),
                                new RelatedPageLink("Corrections policy", "/corrections")
                        )
                ),
                new InfoPageDefinition(
                        "contact",
                        "Contact",
                        "Current contact paths",
                        "KitchenRuleHub currently supports page-level operator routing requests. Use the form shown on the relevant local page so the request stays attached to the correct city and authority context.",
                        "ContactPage",
                        "index,follow",
                        List.of(
                                new GuideSection("Operator routing requests", List.of(
                                        "Use the service-request form on the relevant grease or hood finder page.",
                                        "Open the city and issue route first so the request stays attached to the right authority and workflow."
                                )),
                                new GuideSection("Corrections and trust issues", List.of(
                                        "Use the corrections workflow described on the corrections page when a source summary, route label, or provider evidence signal looks wrong.",
                                        "A broader support channel can be added later, but the current launch surface is page-specific and traceable."
                                ))
                        ),
                        List.of(
                                new RelatedPageLink("Corrections policy", "/corrections"),
                                new RelatedPageLink("Methodology", "/methodology"),
                                new RelatedPageLink("Privacy", "/privacy")
                        )
                ),
                new InfoPageDefinition(
                        "privacy",
                        "Privacy",
                        "Privacy policy",
                        "KitchenRuleHub stores operator contact data only when a user submits a page-level form. The stored data is used for routing, follow-up, attribution review, and operational auditing inside the product.",
                        "WebPage",
                        "noindex,follow",
                        List.of(
                                new GuideSection("What is collected", List.of(
                                        "Page-level lead forms collect the contact fields shown on the form, plus the route context required to understand the city, issue type, and authority involved.",
                                        "Basic attribution events may log local page views, tool views, CTA clicks, and provider outbound clicks for product operations."
                                )),
                                new GuideSection("How it is used", List.of(
                                        "Operator requests are used to route service help through the product's operations workflow."
                                )),
                                new GuideSection("What this policy does not claim", List.of(
                                        "This policy does not claim government handling, city ownership of the data, or any official submission status.",
                                        "No page-level lead form should be treated as an official government submission channel."
                                ))
                        ),
                        List.of(
                                new RelatedPageLink("Terms", "/terms"),
                                new RelatedPageLink("Contact", "/contact"),
                                new RelatedPageLink("Not government affiliated", "/not-government-affiliated")
                        )
                ),
                new InfoPageDefinition(
                        "terms",
                        "Terms",
                        "Terms of use",
                        "KitchenRuleHub is an independent information and routing product. Operators are responsible for confirming the current authority requirement for their specific site before relying on any service decision or inspection-prep action.",
                        "WebPage",
                        "noindex,follow",
                        List.of(
                                new GuideSection("Use conditions", List.of(
                                        "Treat the product as a workflow aid grounded in official sources, not as legal advice or a government determination.",
                                        "Always confirm the live rule holder when the page tells you to verify details or when a city page points you to a utility, fire office, or county department."
                                )),
                                new GuideSection("Provider listing boundaries", List.of(
                                        "Listings are routing support only and do not imply municipal endorsement.",
                                        "A provider listing does not convert that company into an approved or certified local authority listing."
                                ))
                        ),
                        List.of(
                                new RelatedPageLink("Methodology", "/methodology"),
                                new RelatedPageLink("Privacy", "/privacy")
                        )
                ),
                new InfoPageDefinition(
                        "not-government-affiliated",
                        "Independence",
                        "KitchenRuleHub is not government affiliated",
                        "KitchenRuleHub is an independent product. It is not operated by a city, county, utility, fire department, or other public authority, and it should not present itself as an official portal or municipal system.",
                        "WebPage",
                        "index,follow",
                        List.of(
                                new GuideSection("What operators should expect", List.of(
                                        "Page-level authority summaries point back to the public source material that supports them.",
                                        "When a government action is required, the product should tell the operator which authority to contact or verify."
                                )),
                                new GuideSection("What the product should not imply", List.of(
                                        "No city seal, official portal language, invented certification badge, or endorsement claim should appear unless the source truly supports it.",
                                        "Provider pages should not imply municipal approval unless the public source says so."
                                ))
                        ),
                        List.of(
                                new RelatedPageLink("About", "/about"),
                                new RelatedPageLink("Methodology", "/methodology"),
                                new RelatedPageLink("Corrections policy", "/corrections")
                        )
                ),
                new InfoPageDefinition(
                        "corrections",
                        "Corrections",
                        "Corrections and source dispute policy",
                        "KitchenRuleHub should surface uncertainty early and correct public mistakes quickly. When a route, source summary, or provider-evidence label looks wrong, the product should prefer explicit correction over quiet copy drift.",
                        "WebPage",
                        "index,follow",
                        List.of(
                                new GuideSection("What should be corrected", List.of(
                                        "Wrong authority naming, stale record lists, overstated approval language, or missing source nuance.",
                                        "Provider evidence labels that imply approval, certification, or endorsement without support."
                                )),
                                new GuideSection("How the product responds", List.of(
                                        "A route can be held from search while the evidence or authority logic is re-verified.",
                                        "Corrections should preserve the source trail so operators can see why the page changed."
                                ))
                        ),
                        List.of(
                                new RelatedPageLink("Methodology", "/methodology"),
                                new RelatedPageLink("Contact", "/contact"),
                                new RelatedPageLink("Privacy", "/privacy")
                        )
                )
        );
    }

    public record InfoPageDefinition(
            String slug,
            String eyebrow,
            String title,
            String summary,
            String schemaType,
            String robots,
            List<GuideSection> sections,
            List<RelatedPageLink> relatedLinks
    ) {
    }
}
