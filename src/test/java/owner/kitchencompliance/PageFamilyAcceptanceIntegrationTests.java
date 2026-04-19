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

        assertThat(html).contains("Kitchen compliance with a local next action");
        assertThat(html).contains("Start With The Issue On Your Desk");
        assertThat(html).contains("Choose A City");
        assertThat(html).contains("Current city coverage");
        assertThat(html).contains("What we solve");
        assertThat(html).contains("Operator Tools");
        assertThat(html).contains("Official sources reviewed locally");
        assertThat(indexOf(html, "Start With The Issue On Your Desk"))
                .isLessThan(indexOf(html, "Choose A City"));
        assertThat(indexOf(html, "Choose A City"))
                .isLessThan(indexOf(html, "What we solve"));
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
        assertThat(html).contains("/tools/hood-service-report");
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
    void hoodServiceReportToolLeadsWithTheSendableArtifactBeforeSupportSections() throws Exception {
        String html = body(get("/tools/hood-service-report"));

        assertThat(indexOf(html, "Use this as the customer-facing handoff"))
                .isLessThan(indexOf(html, "Operator workflow"));
        assertThat(indexOf(html, "Build the handoff in under two minutes"))
                .isLessThan(indexOf(html, "Operator workflow"));
        assertThat(indexOf(html, "Use this as the customer-facing handoff"))
                .isLessThan(indexOf(html, "Ready-to-send email"));
        assertThat(indexOf(html, "Use this as the customer-facing handoff"))
                .isLessThan(indexOf(html, "Where this fits in the office"));
        assertThat(indexOf(html, "Recipient name"))
                .isLessThan(indexOf(html, "Ready-to-send email"));
        assertThat(indexOf(html, "Replace sample values before send"))
                .isLessThan(indexOf(html, "Operator workflow"));
        assertThat(indexOf(html, "Customer-facing report PDF or printout is ready."))
                .isLessThan(indexOf(html, "Operator workflow"));
        assertThat(indexOf(html, "Local proof photos"))
                .isLessThan(indexOf(html, "Operator workflow"));
        assertThat(indexOf(html, "Recipient email is blank. Copy the handoff or send it from your existing thread."))
                .isLessThan(indexOf(html, "Related local pages"));
        assertThat(indexOf(html, "Attach the rule link without weakening the handoff"))
                .isLessThan(indexOf(html, "Related local pages"));
        assertThat(html).doesNotContain("Sample handoff");
        assertThat(html).doesNotContain("Vendor-only setup");
        assertThat(html).doesNotContain("Ask about setup");
        assertThat(html).contains("method=\"post\"");
    }

    @Test
    void vendorLandingLeadsWithTheFreeHoodWorkflowBeforeSupportLinks() throws Exception {
        String html = body(get("/for-vendors"));

        assertThat(indexOf(html, "Send today's hood closeout in a few minutes"))
                .isLessThan(indexOf(html, "When you'd use this"));
        assertThat(indexOf(html, "Start today's hood closeout"))
                .isLessThan(indexOf(html, "City rule pages"));
        assertThat(indexOf(html, "What you send"))
                .isLessThan(indexOf(html, "City rule pages"));
        assertThat(indexOf(html, "Why it helps the office"))
                .isLessThan(indexOf(html, "City rule pages"));
        assertThat(html).contains("/tools/hood-service-report?draft=blank");
        assertThat(html).contains("?draft=blank#customer-copy");
        assertThat(html).contains("customer-ready record without writing the whole handoff from scratch.");
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
