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
        "app.site.base-url=http://localhost:8080",
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

        assertThat(html).contains("Find what is missing before inspection does");
        assertThat(html).contains("Start With The Problem On Your Desk");
        assertThat(html).contains("Choose A City");
        assertThat(html).contains("Current city coverage");
        assertThat(html).contains("What You Should Know In A Minute");
        assertThat(html).contains("Repeat-Use Worksheets");
        assertThat(html).contains("For restaurant owners and kitchen managers");
        assertThat(html).doesNotContain("/for-vendors");
        assertThat(indexOf(html, "Start With The Problem On Your Desk"))
                .isLessThan(indexOf(html, "Choose A City"));
        assertThat(indexOf(html, "Choose A City"))
                .isLessThan(indexOf(html, "What You Should Know In A Minute"));
    }

    @Test
    void fogRulePageFollowsAuthorityProofRiskAndActionSequence() throws Exception {
        String html = body(get("/tx/austin/restaurant-grease-trap-rules"));

        assertThat(indexOf(html, "Authority Summary"))
                .isLessThan(indexOf(html, "Local Interceptor Requirements"));
        assertThat(indexOf(html, "Local Interceptor Requirements"))
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
                .isLessThan(indexOf(html, "Local Hood-System Requirements"));
        assertThat(indexOf(html, "Local Hood-System Requirements"))
                .isLessThan(indexOf(html, "Required Cleaning Tags"));
        assertThat(indexOf(html, "Required Cleaning Tags"))
                .isLessThan(indexOf(html, "Common Inspection Fails"));
        assertThat(indexOf(html, "Common Inspection Fails"))
                .isLessThan(indexOf(html, "Cleaner routing"));
        assertThat(html).contains("/tools/hood-record-binder");
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

        assertThat(html).contains("Ready to compare providers");
        assertThat(html).contains("Primary source");
        assertThat(html).contains("Provider Listings");
        assertThat(indexOf(html, "Ready to compare providers"))
                .isLessThan(indexOf(html, "Provider Listings"));
        assertThat(indexOf(html, "Primary source"))
                .isLessThan(indexOf(html, "Provider Listings"));
        assertThat(indexOf(html, "Provider Listings"))
                .isLessThan(indexOf(html, "Listings are routing support only."));
        assertThat(indexOf(html, "Listings are routing support only."))
                .isLessThan(indexOf(html, "Need grease service help?"));
        assertThat(html).contains("Verification checklist");
    }

    @Test
    void hoodServiceReportToolIsNotPubliclyReachable() throws Exception {
        mockMvc.perform(get("/tools/hood-service-report"))
                .andExpect(status().isNotFound());
    }

    @Test
    void vendorLandingRouteIsNotPubliclyReachable() throws Exception {
        mockMvc.perform(get("/for-vendors"))
                .andExpect(status().isNotFound());
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
