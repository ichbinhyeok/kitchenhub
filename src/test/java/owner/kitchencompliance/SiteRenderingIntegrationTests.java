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
        "app.attribution.log-dir=target/test-site-rendering-attribution",
        "app.leads.log-dir=target/test-site-rendering-leads"
})
@AutoConfigureMockMvc
class SiteRenderingIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homePageExplainsScopeAndLinksIntoAllLiveCities() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Kitchen compliance with a local next action")))
                .andExpect(content().string(containsString("/tx/austin/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/ca/santa-clara/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/nc/charlotte/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/fl/miami/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/fl/tampa/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/ne/grand-island/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/or/portland/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/tn/nashville/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/guides/fog-vs-grease-trap-cleaning")))
                .andExpect(content().string(containsString("/tools/grease-log")))
                .andExpect(content().string(containsString("/tools/inspection-reminder-plan")))
                .andExpect(content().string(containsString("application/ld+json")));
    }

    @Test
    void operatorToolsRenderAsNoindexUtilitiesWithDownloads() throws Exception {
        mockMvc.perform(get("/tools/grease-log"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"noindex,follow\"")))
                .andExpect(content().string(containsString("Grease service log template")))
                .andExpect(content().string(containsString("/tools/grease-log.csv")));

        mockMvc.perform(get("/tools/inspection-reminder-plan.csv"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("reminder_date,city,authority,issue_type,missing_proof,next_action,owner,status")));
    }

    @Test
    void fogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/tx/austin/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/tx/austin/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Authority-backed sources")))
                .andExpect(content().string(containsString("Austin publishes an authority-backed hauler or preferred-pumper list")))
                .andExpect(content().string(containsString("/tx/austin/approved-grease-haulers")))
                .andExpect(content().string(containsString("action=\"/lead-intake/sponsor\"")))
                .andExpect(content().string(containsString("Want sponsor placement on Austin coverage?")));
    }

    @Test
    void approvedHaulersPagePreservesNonEndorsementLanguage() throws Exception {
        mockMvc.perform(get("/tx/austin/approved-grease-haulers"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("verification tool, not a recommendation list")))
                .andExpect(content().string(containsString("neither recommends nor endorses")));
    }

    @Test
    void charlotteFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/nc/charlotte/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/nc/charlotte/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Charlotte publishes an authority-backed hauler or preferred-pumper list")));
    }

    @Test
    void tampaFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/fl/tampa/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/fl/tampa/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Tampa publishes an authority-backed hauler or preferred-pumper list")));
    }

    @Test
    void miamiFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/fl/miami/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/fl/miami/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Miami publishes an authority-backed hauler or preferred-pumper list")));
    }

    @Test
    void grandIslandFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/ne/grand-island/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/ne/grand-island/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Grand Island publishes an authority-backed hauler or preferred-pumper list")));
    }

    @Test
    void portlandFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/or/portland/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/or/portland/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Portland publishes an authority-backed hauler or preferred-pumper list")));
    }

    @Test
    void santaClaraFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/ca/santa-clara/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/ca/santa-clara/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Santa Clara publishes an authority-backed hauler or preferred-pumper list")));
    }

    @Test
    void nashvilleFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/tn/nashville/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/tn/nashville/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Nashville publishes an authority-backed hauler or preferred-pumper list")));
    }

    @Test
    void charlotteProviderFinderPagesRenderPublicListingsWhenCoverageIsStrong() throws Exception {
        mockMvc.perform(get("/nc/charlotte/find-grease-service"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("Greenway Waste Solutions")))
                .andExpect(content().string(containsString("Express Jet Services")))
                .andExpect(content().string(containsString("Greasecycle")))
                .andExpect(content().string(containsString("/out/providers/greenway-waste-solutions-charlotte")))
                .andExpect(content().string(containsString("/out/providers/express-jet-services-charlotte")))
                .andExpect(content().string(containsString("/out/providers/greasecycle-charlotte")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));

        mockMvc.perform(get("/nc/charlotte/find-hood-cleaner"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("Kitchen Guard of Charlotte")))
                .andExpect(content().string(containsString("Hood Cleaning Charlotte")))
                .andExpect(content().string(containsString("HOODZ of Upstate SC and Greater Charlotte")))
                .andExpect(content().string(containsString("/out/providers/kitchen-guard-charlotte-hood-cleaning")))
                .andExpect(content().string(containsString("/out/providers/hood-cleaning-charlotte-nc")))
                .andExpect(content().string(containsString("/out/providers/hoodz-charlotte-hood-cleaning")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));
    }

    @Test
    void providerFinderPagesRenderPublicListingsWhenCoverageIsStrong() throws Exception {
        mockMvc.perform(get("/tx/austin/find-grease-service"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("Liquid Environmental Solutions")))
                .andExpect(content().string(containsString("Austin Rooter")))
                .andExpect(content().string(containsString("Mahoney Environmental")))
                .andExpect(content().string(containsString("/out/providers/liquid-environmental-solutions-austin")))
                .andExpect(content().string(containsString("/out/providers/austin-rooter-grease-service")))
                .andExpect(content().string(containsString("/out/providers/mahoney-environmental-austin")))
                .andExpect(content().string(containsString("action=\"/lead-intake/operator\"")))
                .andExpect(content().string(containsString("Send service request")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));

        mockMvc.perform(get("/tx/austin/find-hood-cleaner"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("Kitchen Guard of Austin")))
                .andExpect(content().string(containsString("Pro Hood Cleaning")))
                .andExpect(content().string(containsString("HOODZ of Austin")))
                .andExpect(content().string(containsString("/out/providers/kitchen-guard-austin-hood-cleaning")))
                .andExpect(content().string(containsString("/out/providers/pro-hood-cleaning-austin")))
                .andExpect(content().string(containsString("/out/providers/hoodz-austin-hood-cleaning")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));
    }

    @Test
    void tampaProviderFinderPagesRenderPublicListingsWhenCoverageIsStrong() throws Exception {
        mockMvc.perform(get("/fl/tampa/find-grease-service"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("RJ Green Environmental")))
                .andExpect(content().string(containsString("ACE Septic &amp; Waste")))
                .andExpect(content().string(containsString("Liquid Environmental Solutions")))
                .andExpect(content().string(containsString("/out/providers/rj-green-environmental-tampa")))
                .andExpect(content().string(containsString("/out/providers/ace-septic-waste-tampa")))
                .andExpect(content().string(containsString("/out/providers/liquid-environmental-solutions-tampa")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));

        mockMvc.perform(get("/fl/tampa/find-hood-cleaner"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("Kitchen Guard of Tampa-Lakeland")))
                .andExpect(content().string(containsString("HoodKlean")))
                .andExpect(content().string(containsString("Hood-Tek")))
                .andExpect(content().string(containsString("/out/providers/kitchen-guard-tampa-hood-cleaning")))
                .andExpect(content().string(containsString("/out/providers/hoodklean-tampa")))
                .andExpect(content().string(containsString("/out/providers/hood-tek-tampa")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));
    }

    @Test
    void miamiProviderFinderPagesRenderPublicListingsWhenCoverageIsStrong() throws Exception {
        mockMvc.perform(get("/fl/miami/find-grease-service"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("Ameri-Clean Pumping, Inc.")))
                .andExpect(content().string(containsString("Grease Trap FL")))
                .andExpect(content().string(containsString("United Septic and Grease")))
                .andExpect(content().string(containsString("/out/providers/ameri-clean-pumping-miami")))
                .andExpect(content().string(containsString("/out/providers/grease-trap-fl-miami")))
                .andExpect(content().string(containsString("/out/providers/united-septic-grease-miami")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));

        mockMvc.perform(get("/fl/miami/find-hood-cleaner"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("Hoods Cleaning Experts")))
                .andExpect(content().string(containsString("Kitchen Guard of Miami")))
                .andExpect(content().string(containsString("Miami Hood Cleaning LLC")))
                .andExpect(content().string(containsString("/out/providers/hood-cleaning-experts-miami")))
                .andExpect(content().string(containsString("/out/providers/kitchen-guard-miami")))
                .andExpect(content().string(containsString("/out/providers/miami-hood-cleaning-llc")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));
    }

    @Test
    void grandIslandProviderFinderPagesRenderPublicListingsWhenCoverageIsStrong() throws Exception {
        mockMvc.perform(get("/ne/grand-island/find-grease-service"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("Eberl Plumbing &amp; Drain")))
                .andExpect(content().string(containsString("Herman Plumbing Co., Inc.")))
                .andExpect(content().string(containsString("Logue Plumbing LLC")))
                .andExpect(content().string(containsString("/out/providers/eberl-plumbing-grand-island")))
                .andExpect(content().string(containsString("/out/providers/herman-plumbing-grand-island")))
                .andExpect(content().string(containsString("/out/providers/logue-plumbing-grand-island")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));

        mockMvc.perform(get("/ne/grand-island/find-hood-cleaner"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("AFT Hood &amp; Carpet Cleaning")))
                .andExpect(content().string(containsString("HOODMASTERS")))
                .andExpect(content().string(containsString("Kitchen Guard of Nebraska")))
                .andExpect(content().string(containsString("/out/providers/aft-hood-cleaning-grand-island")))
                .andExpect(content().string(containsString("/out/providers/hoodmasters-grand-island")))
                .andExpect(content().string(containsString("/out/providers/kitchen-guard-grand-island")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));
    }

    @Test
    void portlandProviderFinderPagesRenderPublicListingsWhenCoverageIsStrong() throws Exception {
        mockMvc.perform(get("/or/portland/find-grease-service"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("NW Biofuel")))
                .andExpect(content().string(containsString("Oregon Oils, Inc.")))
                .andExpect(content().string(containsString("Mahoney Environmental")))
                .andExpect(content().string(containsString("/out/providers/nw-biofuel-portland")))
                .andExpect(content().string(containsString("/out/providers/oregon-oils-portland")))
                .andExpect(content().string(containsString("/out/providers/mahoney-environmental-portland")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));

        mockMvc.perform(get("/or/portland/find-hood-cleaner"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("Silver Lining Hood Cleaners")))
                .andExpect(content().string(containsString("Restaurant Exhaust Cleaning Specialists")))
                .andExpect(content().string(containsString("APEX Hood Cleaning")))
                .andExpect(content().string(containsString("/out/providers/silver-lining-hood-cleaners-portland")))
                .andExpect(content().string(containsString("/out/providers/restaurant-exhaust-cleaning-specialists-portland")))
                .andExpect(content().string(containsString("/out/providers/apex-hood-cleaning-portland")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));
    }

    @Test
    void santaClaraProviderFinderPagesRenderPublicListingsWhenCoverageIsStrong() throws Exception {
        mockMvc.perform(get("/ca/santa-clara/find-grease-service"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("A-1 Septic Tank Service")))
                .andExpect(content().string(containsString("Aaron's Septic Tank Service")))
                .andExpect(content().string(containsString("Baker Commodities")))
                .andExpect(content().string(containsString("/out/providers/a1-tank-santa-clara-grease-service")))
                .andExpect(content().string(containsString("/out/providers/aarons-septic-santa-clara-grease-service")))
                .andExpect(content().string(containsString("/out/providers/baker-commodities-santa-clara")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));

        mockMvc.perform(get("/ca/santa-clara/find-hood-cleaner"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("HOODZ of San Francisco and West San Jose")))
                .andExpect(content().string(containsString("Bay Area Hood Cleaning")))
                .andExpect(content().string(containsString("APEX Hood Cleaning")))
                .andExpect(content().string(containsString("/out/providers/hoodz-santa-clara-hood-cleaning")))
                .andExpect(content().string(containsString("/out/providers/bay-area-hood-cleaning-santa-clara")))
                .andExpect(content().string(containsString("/out/providers/apex-hood-cleaning-santa-clara")))
                .andExpect(content().string(containsString("Evidence: ")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));
    }

    @Test
    void nashvilleProviderFinderPagesRenderPublicListingsWhenCoverageIsStrong() throws Exception {
        mockMvc.perform(get("/tn/nashville/find-grease-service"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("Onsite Environmental")))
                .andExpect(content().string(containsString("Liquid Environmental Solutions")))
                .andExpect(content().string(containsString("Maxwell Septic Pumping")))
                .andExpect(content().string(containsString("/out/providers/onsite-environmental-nashville")))
                .andExpect(content().string(containsString("/out/providers/liquid-environmental-solutions-nashville")))
                .andExpect(content().string(containsString("/out/providers/maxwell-septic-nashville")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));

        mockMvc.perform(get("/tn/nashville/find-hood-cleaner"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("Nashville Hood Cleaning Pros")))
                .andExpect(content().string(containsString("Music City Sanitizing Services")))
                .andExpect(content().string(containsString("DDAN Hood Cleaning and Repair")))
                .andExpect(content().string(containsString("/out/providers/nashville-hood-cleaning-pros")))
                .andExpect(content().string(containsString("/out/providers/music-city-sanitizing-nashville")))
                .andExpect(content().string(containsString("/out/providers/ddan-hood-cleaning-nashville")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));
    }

    @Test
    void ctaLinksRenderThroughTrackedRedirects() throws Exception {
        mockMvc.perform(get("/tx/austin/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "href=\"/out/cta?source=/tx/austin/restaurant-grease-trap-rules&amp;target=/tx/austin/approved-grease-haulers&amp;sponsored=false\""
                )));
    }

    @Test
    void sitemapIncludesIndexedPagesForAllLiveCities() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("http://localhost:8080/ca/santa-clara/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/fl/miami/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/fl/tampa/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/ne/grand-island/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/or/portland/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/tn/nashville/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/tx/austin/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/nc/charlotte/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/guides/what-records-restaurant-inspections-check")))
                .andExpect(content().string(containsString("http://localhost:8080/ca/santa-clara/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/ca/santa-clara/find-grease-service")))
                .andExpect(content().string(containsString("http://localhost:8080/fl/miami/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/fl/miami/find-grease-service")))
                .andExpect(content().string(containsString("http://localhost:8080/or/portland/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/or/portland/find-grease-service")))
                .andExpect(content().string(containsString("http://localhost:8080/ne/grand-island/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/ne/grand-island/find-grease-service")))
                .andExpect(content().string(containsString("http://localhost:8080/tn/nashville/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/tn/nashville/find-grease-service")))
                .andExpect(content().string(containsString("http://localhost:8080/fl/tampa/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/fl/tampa/find-grease-service")))
                .andExpect(content().string(containsString("http://localhost:8080/tx/austin/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/tx/austin/find-grease-service")))
                .andExpect(content().string(containsString("http://localhost:8080/nc/charlotte/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/nc/charlotte/find-grease-service")));
    }

    @Test
    void guidePagesUseSharedLinksThatReferenceMultipleLiveCities() throws Exception {
        mockMvc.perform(get("/guides/fog-vs-grease-trap-cleaning"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/tx/austin/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/ca/santa-clara/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/nc/charlotte/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString(">Austin<")))
                .andExpect(content().string(containsString(">Santa Clara<")))
                .andExpect(content().string(containsString(">Charlotte<")));
    }
}
