package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "app.attribution.log-dir=target/test-page-family-attribution",
        "app.leads.log-dir=target/test-page-family-leads"
})
@AutoConfigureMockMvc
class PageFamilyAcceptanceIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homePageStaysIssueFirstBeforeCityBrowseAndTools() throws Exception {
        String html = body(get("/"));

        assertThat(html).contains("Local compliance + operator action for commercial kitchens");
        assertThat(html).contains("Start With The Issue On Your Desk");
        assertThat(html).contains("Active City Jurisdictions");
        assertThat(html).contains("Operator Tools");
        assertThat(html).contains("Verified local authority summaries");
    }

    @Test
    void fogRulePageFollowsAuthorityProofRiskAndActionSequence() throws Exception {
        String html = body(get("/tx/austin/restaurant-grease-trap-rules"));

        assertThat(indexOf(html, "Authority Summary"))
                .isLessThan(indexOf(html, "Sizing &amp; Frequency Mandates"));
        assertThat(indexOf(html, "Sizing &amp; Frequency Mandates"))
                .isLessThan(indexOf(html, "Inspection-Ready Proof"));
        assertThat(indexOf(html, "Inspection-Ready Proof"))
                .isLessThan(indexOf(html, "Common Inspection Failures"));
        assertThat(indexOf(html, "Common Inspection Failures"))
                .isLessThan(indexOf(html, "Need a hauler check before the next pump-out?"));
        assertThat(html).contains("/tools/grease-log");
        assertThat(html).contains("/tools/inspection-reminder-plan");
    }

    @Test
    void hoodPageKeepsRequirementAndProofAheadOfCleanerRouting() throws Exception {
        String html = body(get("/tx/austin/hood-cleaning-requirements"));

        assertThat(indexOf(html, "Authority Summary"))
                .isLessThan(indexOf(html, "Cleaning Frequency Requirements"));
        assertThat(indexOf(html, "Cleaning Frequency Requirements"))
                .isLessThan(indexOf(html, "Required Cleaning Tags"));
        assertThat(indexOf(html, "Required Cleaning Tags"))
                .isLessThan(indexOf(html, "Common Inspection Fails"));
        assertThat(indexOf(html, "Common Inspection Fails"))
                .isLessThan(indexOf(html, "Cleaner routing"));
    }

    @Test
    void inspectionPageKeepsChecklistAndRecordsAheadOfServiceRouting() throws Exception {
        String html = body(get("/tx/austin/restaurant-fire-inspection-checklist"));

        assertThat(indexOf(html, "Potential fines &amp; closures"))
                .isLessThan(indexOf(html, "Records binder"));
        assertThat(indexOf(html, "Records binder"))
                .isLessThan(indexOf(html, "Need to close a paperwork gap before inspection?"));
        assertThat(html).contains("/tools/hood-record-binder");
        assertThat(html).contains("/tools/inspection-reminder-plan");
    }

    @Test
    void providerFinderShowsQualificationAndSourceBeforeListings() throws Exception {
        String html = body(get("/nc/charlotte/find-grease-service"));

        assertThat(html).contains("Evidence-backed routing is live");
        assertThat(html).contains("Primary source");
        assertThat(html).contains("Provider Listings");
        assertThat(indexOf(html, "Evidence-backed routing is live"))
                .isLessThan(indexOf(html, "Provider Listings"));
        assertThat(indexOf(html, "Primary source"))
                .isLessThan(indexOf(html, "Provider Listings"));
        assertThat(indexOf(html, "Provider Listings"))
                .isLessThan(indexOf(html, "Listings are routing support only."));
        assertThat(indexOf(html, "Listings are routing support only."))
                .isLessThan(indexOf(html, "Need grease service help?"));
        assertThat(html).contains("Verification checklist");
    }

    private String body(org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }

    private int indexOf(String html, String needle) {
        int index = html.indexOf(needle);
        assertThat(index).as("Expected to find <%s> in rendered page", needle).isGreaterThanOrEqualTo(0);
        return index;
    }
}
