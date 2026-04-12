package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "app.attribution.log-dir=target/test-lead-attribution",
        "app.leads.log-dir=target/test-leads"
})
@AutoConfigureMockMvc
class LeadCaptureIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private final Path leadLogFile = Path.of("target", "test-leads", "lead-intake.csv");

    @BeforeEach
    void resetLeadLog() throws IOException {
        Files.deleteIfExists(leadLogFile);
        Files.createDirectories(leadLogFile.getParent());
    }

    @Test
    void operatorLeadPersistsCsvAndRedirectsBackToFinder() throws Exception {
        mockMvc.perform(post("/lead-intake/operator")
                        .param("source", "/tx/austin/find-grease-service")
                        .param("formLoadedAt", String.valueOf(Clock.systemUTC().millis() - 5_000))
                        .param("website", "")
                        .param("contactName", " Alex   Kim ")
                        .param("businessName", "Kim   Kitchen ")
                        .param("email", "ALEX@EXAMPLE.COM ")
                        .param("phone", "010-1234-5678")
                        .param("notes", "Need service before Friday inspection")
                        .param("routingConsent", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tx/austin/find-grease-service?lead=operator-submitted#service-request"));

        String csv = Files.readString(leadLogFile);
        assertThat(csv).contains("lead_type,visitor_id,city,state,page_family");
        assertThat(csv).contains("operator_request");
        assertThat(csv).contains("austin,tx,provider_finder,fog_cleaning");
        assertThat(csv).contains("need_grease_service");
        assertThat(csv).contains("Alex Kim");
        assertThat(csv).contains("Kim Kitchen");
        assertThat(csv).contains("alex@example.com");
    }

    @Test
    void sponsorInquiryPersistsCsvAndRedirectsBackToSourcePage() throws Exception {
        mockMvc.perform(post("/lead-intake/sponsor")
                        .param("source", "/tx/austin/restaurant-grease-trap-rules")
                        .param("formLoadedAt", String.valueOf(Clock.systemUTC().millis() - 5_000))
                        .param("website", "")
                        .param("contactName", "Jamie Lee")
                        .param("businessName", "Grease Vendor Co")
                        .param("email", "JAMIE@VENDOR.EXAMPLE")
                        .param("phone", "555-000-1111")
                        .param("coverageNote", "Austin and Round Rock")
                        .param("notes", "Interested in grease pages")
                        .param("routingConsent", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tx/austin/restaurant-grease-trap-rules?lead=sponsor-submitted#sponsor-slot"));

        String csv = Files.readString(leadLogFile);
        assertThat(csv).contains("sponsor_inquiry");
        assertThat(csv).contains("grease_service_sponsor_slot");
        assertThat(csv).contains("Jamie Lee");
        assertThat(csv).contains("Austin and Round Rock");
        assertThat(csv).contains("jamie@vendor.example");
    }

    @Test
    void missingConsentDoesNotWriteLeadRow() throws Exception {
        mockMvc.perform(post("/lead-intake/operator")
                        .param("source", "/tx/austin/find-grease-service")
                        .param("formLoadedAt", String.valueOf(Clock.systemUTC().millis() - 5_000))
                        .param("website", "")
                        .param("contactName", "Alex Kim")
                        .param("businessName", "Kim Kitchen")
                        .param("email", "alex@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tx/austin/find-grease-service?lead=consent-required#service-request"));

        assertThat(Files.exists(leadLogFile)).isFalse();
    }

    @Test
    void honeypotSubmissionDoesNotWriteLeadRow() throws Exception {
        mockMvc.perform(post("/lead-intake/operator")
                        .param("source", "/tx/austin/find-grease-service")
                        .param("formLoadedAt", String.valueOf(Clock.systemUTC().millis() - 5_000))
                        .param("website", "https://example.com")
                        .param("contactName", "Alex Kim")
                        .param("businessName", "Kim Kitchen")
                        .param("email", "alex@example.com")
                        .param("routingConsent", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tx/austin/find-grease-service?lead=operator-invalid#service-request"));

        assertThat(Files.exists(leadLogFile)).isFalse();
    }

    @Test
    void tooFastSubmissionDoesNotWriteLeadRow() throws Exception {
        mockMvc.perform(post("/lead-intake/sponsor")
                        .param("source", "/tx/austin/restaurant-grease-trap-rules")
                        .param("formLoadedAt", String.valueOf(Clock.systemUTC().millis()))
                        .param("website", "")
                        .param("contactName", "Jamie Lee")
                        .param("businessName", "Grease Vendor Co")
                        .param("email", "jamie@vendor.example")
                        .param("routingConsent", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tx/austin/restaurant-grease-trap-rules?lead=sponsor-invalid#sponsor-slot"));

        assertThat(Files.exists(leadLogFile)).isFalse();
    }

    @Test
    void overlongContactNameDoesNotWriteLeadRow() throws Exception {
        mockMvc.perform(post("/lead-intake/operator")
                        .param("source", "/tx/austin/find-grease-service")
                        .param("formLoadedAt", String.valueOf(Clock.systemUTC().millis() - 5_000))
                        .param("website", "")
                        .param("contactName", "A".repeat(121))
                        .param("businessName", "Kim Kitchen")
                        .param("email", "alex@example.com")
                        .param("routingConsent", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tx/austin/find-grease-service?lead=operator-invalid#service-request"));

        assertThat(Files.exists(leadLogFile)).isFalse();
    }

    @Test
    void authorityAliasSourcePathIsPreservedInLeadStorageAndRedirect() throws Exception {
        mockMvc.perform(post("/lead-intake/sponsor")
                        .param("source", "/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules")
                        .param("formLoadedAt", String.valueOf(Clock.systemUTC().millis() - 5_000))
                        .param("website", "")
                        .param("contactName", "Jamie Lee")
                        .param("businessName", "Grease Vendor Co")
                        .param("email", "jamie@vendor.example")
                        .param("routingConsent", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules?lead=sponsor-submitted#sponsor-slot"));

        String csv = Files.readString(leadLogFile);
        assertThat(csv).contains("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules");
    }
}
