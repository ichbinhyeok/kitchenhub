package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "app.attribution.log-dir=target/test-attribution")
@AutoConfigureMockMvc
class AttributionRedirectIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private final Path logFile = Path.of("target", "test-attribution", "click-attribution.csv");

    @BeforeEach
    void resetAttributionLog() throws IOException {
        Files.deleteIfExists(logFile);
        Files.createDirectories(logFile.getParent());
    }

    @Test
    void providerRedirectLogsCityPageFamilyAndIssueType() throws Exception {
        mockMvc.perform(get("/out/providers/greenway-waste-solutions-charlotte")
                        .queryParam("source", "/nc/charlotte/find-grease-service"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://www.greenwaywastesolutions.com/services/"));

        String csv = Files.readString(logFile);
        assertThat(csv).contains("event_type,visitor_id,verdict_state,city,state,page_family,issue_type,authority_id,source_path");
        assertThat(csv).contains("provider_click");
        assertThat(csv).contains("charlotte,nc,provider_finder,fog_cleaning,charlotte-water-flow-free,/nc/charlotte/find-grease-service");
        assertThat(csv).contains("provider_multi");
        assertThat(csv).contains("greenway-waste-solutions-charlotte");
        assertThat(csv).contains("grease_hauler");
    }

    @Test
    void ctaRedirectLogsCityPageFamilyAndCtaType() throws Exception {
        mockMvc.perform(get("/out/cta")
                        .queryParam("source", "/fl/tampa/restaurant-grease-trap-rules")
                        .queryParam("target", "/fl/tampa/approved-grease-haulers"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/fl/tampa/approved-grease-haulers"));

        String csv = Files.readString(logFile);
        assertThat(csv).contains("cta_click");
        assertThat(csv).contains("official_list,tampa,fl,fog_rules,fog_cleaning,tampa-wastewater-grease-ordinance,/fl/tampa/restaurant-grease-trap-rules,/fl/tampa/approved-grease-haulers");
        assertThat(csv).contains("next_action_cta");
    }

    @Test
    void authorityAliasSourcePathIsPreservedInRedirectLogs() throws Exception {
        mockMvc.perform(get("/out/cta")
                        .queryParam("source", "/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules")
                        .queryParam("target", "/authority/tx/austin-water-pretreatment/approved-grease-haulers"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authority/tx/austin-water-pretreatment/approved-grease-haulers"));

        String csv = Files.readString(logFile);
        assertThat(csv).contains("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules,/authority/tx/austin-water-pretreatment/approved-grease-haulers");
    }

    @Test
    void hoodServiceReportEventEndpointIsNotPublic() throws Exception {
        mockMvc.perform(post("/tools/hood-service-report/events")
                        .param("action", "open_rule_page")
                        .param("city", "charlotte")
                        .param("targetPath", "/authority/nc/charlotte-fire-prevention/hood-cleaning-requirements"))
                .andExpect(status().isNotFound());

        assertThat(Files.exists(logFile)).isFalse();
    }
}
