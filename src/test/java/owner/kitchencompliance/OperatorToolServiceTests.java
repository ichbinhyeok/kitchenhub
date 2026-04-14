package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import owner.kitchencompliance.data.PageFamily;
import owner.kitchencompliance.model.OperatorToolPageViewModel;
import owner.kitchencompliance.web.OperatorToolCatalog;
import owner.kitchencompliance.web.OperatorToolCatalog.ToolDefinition;
import owner.kitchencompliance.web.OperatorToolService;
import owner.kitchencompliance.web.SiteProperties;

class OperatorToolServiceTests {

    private final OperatorToolCatalog catalog = new OperatorToolCatalog();
    private final OperatorToolService service = new OperatorToolService(
            new SiteProperties("http://localhost:8080", "KitchenRuleHub", "tx", "G-K0NZM8LCFF"),
            catalog
    );

    @Test
    void toolCatalogStaysUtilityFirstAndLocalRouteLinked() {
        List<ToolDefinition> tools = catalog.allTools();

        assertThat(tools).hasSize(4);
        assertThat(tools).allSatisfy(tool -> {
            assertThat(tool.summary()).contains("noindex");
            assertThat(tool.checklist()).hasSizeGreaterThanOrEqualTo(3);
            assertThat(tool.downloads()).hasSize(1);
            assertThat(tool.relatedLinks()).hasSize(3);
        });

        assertThat(tools.getFirst().relatedLinks().stream().map(link -> link.path()))
                .anyMatch(path -> path.startsWith("/tx/austin/"));
        assertThat(tools.getLast().relatedLinks().stream().map(link -> link.path()))
                .anyMatch(path -> path.startsWith("/tx/austin/") || path.startsWith("/ca/santa-clara/"));
    }

    @Test
    void toolPageAndCsvTemplatesStageProofOnSiteFields() {
        OperatorToolPageViewModel page = service.toolPage("grease-log");

        assertThat(page.meta().robots()).isEqualTo("noindex,follow");
        assertThat(page.title()).contains("Grease service log template");
        assertThat(page.summary()).contains("binder location");
        assertThat(page.checklist()).anySatisfy(item -> assertThat(item).contains("manifest"));
        assertThat(page.downloads()).singleElement().satisfies(download ->
                assertThat(download.note()).containsIgnoringCase("worksheet"));

        assertThat(service.csvTemplate("grease-log")).contains("manifest_reference");
        assertThat(service.csvTemplate("hood-record-binder")).contains("proof_status");
        assertThat(service.csvTemplate("missing-proof-tracker")).contains("missing_proof");
        assertThat(service.csvTemplate("inspection-reminder-plan")).contains("source_route");
    }

    @Test
    void operatorToolPageFamilyStaysUtilityFocused() {
        assertThat(service.pageFamily()).isEqualTo(PageFamily.OPERATOR_TOOL);
        assertThat(service.issueTypeFor("grease-log").name()).isEqualTo("MANIFEST_OR_LOG");
        assertThat(service.issueTypeFor("hood-record-binder").name()).isEqualTo("HOOD_CLEANING");
        assertThat(service.issueTypeFor("missing-proof-tracker").name()).isEqualTo("OPERATOR_UTILITY");
        assertThat(service.issueTypeFor("inspection-reminder-plan").name()).isEqualTo("INSPECTION_PREP");
    }
}
