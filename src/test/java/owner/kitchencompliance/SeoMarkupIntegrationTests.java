package owner.kitchencompliance;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "app.attribution.log-dir=target/test-seo-attribution",
        "app.leads.log-dir=target/test-seo-leads"
})
@AutoConfigureMockMvc
class SeoMarkupIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void pagesExposeOpenGraphAndTwitterMetadata() throws Exception {
        mockMvc.perform(get("/tx/austin/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("property=\"og:title\" content=\"Austin, TX Grease Trap Rules for Restaurants | Pump-Outs &amp; Manifests\"")))
                .andExpect(content().string(containsString("property=\"og:url\" content=\"http://localhost:8080/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("property=\"og:site_name\" content=\"KitchenRuleHub\"")))
                .andExpect(content().string(containsString("name=\"twitter:card\" content=\"summary\"")))
                .andExpect(content().string(containsString("name=\"twitter:title\" content=\"Austin, TX Grease Trap Rules for Restaurants | Pump-Outs &amp; Manifests\"")));
    }

    @Test
    void sitemapIncludesLastModifiedDatesForIndexedEntries() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "<loc>http://localhost:8080/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules</loc><lastmod>2026-04-07</lastmod>"
                )))
                .andExpect(content().string(containsString(
                        "<loc>http://localhost:8080/authorities/tx/austin-water-pretreatment</loc><lastmod>2026-04-07</lastmod>"
                )))
                .andExpect(content().string(not(containsString(
                        "<loc>http://localhost:8080/about</loc><lastmod>"
                ))));
    }

    @Test
    void localPagesUseActionableButtonsAndQualifiedOutboundLinks() throws Exception {
        mockMvc.perform(get("/tx/austin/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(">Review sources<")))
                .andExpect(content().string(containsString("href=\"/tools/grease-log\">Open grease log tool</a>")))
                .andExpect(content().string(not(containsString("Print Paperwork"))))
                .andExpect(content().string(not(containsString("Download Log Tool"))));

        mockMvc.perform(get("/tx/austin/restaurant-fire-inspection-checklist"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("href=\"/tools/inspection-reminder-plan\">Open reminder plan</a>")))
                .andExpect(content().string(not(containsString("Download PDF Checklist"))));

        mockMvc.perform(get("/tx/austin/find-grease-service"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "href=\"/out/providers/liquid-environmental-solutions-austin?source=/tx/austin/find-grease-service\" rel=\"noopener noreferrer\" target=\"_blank\">Visit provider site</a>"
                )));
    }
}
