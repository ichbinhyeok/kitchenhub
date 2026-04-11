package owner.kitchencompliance;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "app.attribution.log-dir=target/test-admin-attribution",
        "app.leads.log-dir=target/test-admin-leads"
})
@AutoConfigureMockMvc
class AdminPageIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private final Path logFile = Path.of("target", "test-admin-attribution", "click-attribution.csv");
    private final Path leadLogFile = Path.of("target", "test-admin-leads", "lead-intake.csv");

    @BeforeEach
    void resetLogFile() throws IOException {
        Files.deleteIfExists(logFile);
        Files.deleteIfExists(leadLogFile);
        Files.createDirectories(logFile.getParent());
        Files.createDirectories(leadLogFile.getParent());
    }

    @Test
    void adminRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminPageShowsEmptyStateAndResolvedStoragePath() throws Exception {
        mockMvc.perform(get("/admin").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Attribution admin")))
                .andExpect(content().string(containsString("Freshness watch")))
                .andExpect(content().string(containsString("Indexed route review status")))
                .andExpect(content().string(containsString("Source quality")))
                .andExpect(content().string(containsString("Indexed route source depth")))
                .andExpect(content().string(containsString("Deploy readiness")))
                .andExpect(content().string(containsString("Pre-deploy route gate")))
                .andExpect(content().string(containsString("Noindex promotion queue")))
                .andExpect(content().string(containsString("Search Console demand")))
                .andExpect(content().string(containsString("Imported route demand snapshot")))
                .andExpect(content().string(containsString("/authority/tx/austin-water-pretreatment/find-grease-service")))
                .andExpect(content().string(containsString("/fl/miami/find-grease-service")))
                .andExpect(content().string(containsString("Austin, TX")))
                .andExpect(content().string(containsString("Miami, FL")))
                .andExpect(content().string(containsString("austin grease hauler service")))
                .andExpect(content().string(containsString("Utility revisit rate")))
                .andExpect(content().string(containsString("No leads recorded yet")))
                .andExpect(content().string(containsString("No attribution events yet")))
                .andExpect(content().string(containsString("/admin/exports/attribution-events.csv")))
                .andExpect(content().string(containsString("/admin/exports/attribution-summary.csv")))
                .andExpect(content().string(containsString("/admin/exports/lead-intake.csv")))
                .andExpect(content().string(containsString("/admin/exports/lead-summary.csv")))
                .andExpect(content().string(containsString("/admin/exports/freshness-watch.csv")))
                .andExpect(content().string(containsString("/admin/exports/source-quality-watch.csv")))
                .andExpect(content().string(containsString("/admin/exports/deploy-readiness.csv")))
                .andExpect(content().string(containsString("/admin/exports/noindex-promotion-queue.csv")))
                .andExpect(content().string(containsString("/admin/exports/search-demand-watch.csv")))
                .andExpect(content().string(containsString("/admin/exports/operator-utility-summary.csv")))
                .andExpect(content().string(containsString("/admin/exports/evidence-index.csv")))
                .andExpect(content().string(containsString("/admin/exports/ops-alerts.md")))
                .andExpect(content().string(containsString(logFile.toAbsolutePath().normalize().toString())))
                .andExpect(content().string(containsString(leadLogFile.toAbsolutePath().normalize().toString())))
                .andExpect(content().string(containsString("content=\"noindex,nofollow\"")));
    }

    @Test
    void adminPageSummarizesExistingAttributionRows() throws Exception {
        String csv = """
                captured_at,event_type,visitor_id,verdict_state,city,state,page_family,issue_type,authority_id,source_path,target_path,target_url,provider_id,provider_type,cta_type,sponsored,tool_slug
                2026-04-07T15:00:00+09:00,page_view,visitor-1,official_list,austin,tx,fog_rules,fog_cleaning,austin-water-pretreatment,/tx/austin/restaurant-grease-trap-rules,,,,,page_view,false,
                2026-04-07T15:05:00+09:00,page_view,visitor-2,operator_tool,,,operator_tool,inspection_prep,,/tools/inspection-reminder-plan,,,,,page_view,false,inspection-reminder-plan
                2026-04-08T15:05:00+09:00,page_view,visitor-2,operator_tool,,,operator_tool,inspection_prep,,/tools/inspection-reminder-plan,,,,,page_view,false,inspection-reminder-plan
                2026-04-07T15:10:00+09:00,provider_click,visitor-3,provider_multi,austin,tx,provider_finder,fog_cleaning,austin-water-pretreatment,/tx/austin/find-grease-service,,https://www.lesclean.com/,liquid-environmental-solutions-austin,grease_hauler,provider_outbound,false,
                2026-04-07T15:12:00+09:00,cta_click,visitor-4,official_list,tampa,fl,fog_rules,fog_cleaning,tampa-wastewater-grease-ordinance,/fl/tampa/restaurant-grease-trap-rules,/fl/tampa/approved-grease-haulers,,,,next_action_cta,false,
                2026-04-07T15:14:00+09:00,cta_click,visitor-5,provider_multi,charlotte,nc,provider_finder,hood_cleaning,charlotte-fire-marshal,/nc/charlotte/find-hood-cleaner,/nc/charlotte/hood-cleaning-requirements,,,,sponsor_cta,true,
                """;
        String leadCsv = """
                captured_at,lead_type,visitor_id,city,state,page_family,issue_type,authority_id,source_path,verdict_state,provider_intent,contact_name,business_name,email,phone,coverage_note,notes,routing_consent
                2026-04-07T15:20:00+09:00,operator_request,visitor-11,austin,tx,provider_finder,fog_cleaning,austin-water-pretreatment,/tx/austin/find-grease-service,provider_multi,need_grease_service,Alex Kim,Kim Kitchen,alex@example.com,010-1234-5678,,Need service,true
                2026-04-07T16:20:00+09:00,sponsor_inquiry,visitor-12,charlotte,nc,provider_finder,hood_cleaning,charlotte-fire-marshal,/nc/charlotte/find-hood-cleaner,provider_multi,hood_cleaning_sponsor_slot,Jamie Lee,Vendor Co,jamie@example.com,555-000-1111,Charlotte metro,Interested,true
                """;
        Files.writeString(logFile, csv, StandardCharsets.UTF_8);
        Files.writeString(leadLogFile, leadCsv, StandardCharsets.UTF_8);

        mockMvc.perform(get("/admin").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Freshness watch")))
                .andExpect(content().string(containsString("Source quality")))
                .andExpect(content().string(containsString("Deploy readiness")))
                .andExpect(content().string(containsString("Noindex promotion queue")))
                .andExpect(content().string(containsString("Search Console demand")))
                .andExpect(content().string(containsString("Utility revisit rate")))
                .andExpect(content().string(containsString("1 of 1 visitors (100%)")))
                .andExpect(content().string(containsString("Verdict states")))
                .andExpect(content().string(containsString("Total leads")))
                .andExpect(content().string(containsString("Operator requests")))
                .andExpect(content().string(containsString("Sponsor inquiries")))
                .andExpect(content().string(containsString("What local buyers ask for")))
                .andExpect(content().string(containsString("Operator request")))
                .andExpect(content().string(containsString("Sponsor inquiry")))
                .andExpect(content().string(containsString("Indexed routes")))
                .andExpect(content().string(containsString("Review soon")))
                .andExpect(content().string(containsString(">6<")))
                .andExpect(content().string(containsString(">1<")))
                .andExpect(content().string(containsString(">3<")))
                .andExpect(content().string(containsString("Austin, TX")))
                .andExpect(content().string(containsString("Tampa, FL")))
                .andExpect(content().string(containsString("Alex Kim | Kim Kitchen | alex@example.com")))
                .andExpect(content().string(containsString("Jamie Lee | Vendor Co | jamie@example.com")))
                .andExpect(content().string(containsString("Provider outbound")))
                .andExpect(content().string(containsString("Operator tool view")))
                .andExpect(content().string(containsString("liquid-environmental-solutions-austin")))
                .andExpect(content().string(containsString("/fl/tampa/approved-grease-haulers")));
    }

    @Test
    void adminCsvExportsReturnExpectedHeaders() throws Exception {
        mockMvc.perform(get("/admin/exports/attribution-summary.csv").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("city,state,page_family,event_type,verdict_state,destination,sponsored,count")));

        mockMvc.perform(get("/admin/exports/lead-intake.csv").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("captured_at,lead_type,visitor_id,city,state,page_family,issue_type,authority_id,source_path")));

        mockMvc.perform(get("/admin/exports/lead-summary.csv").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("city,state,lead_type,page_family,provider_intent,routing_consent,count")));

        mockMvc.perform(get("/admin/exports/freshness-watch.csv").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("city,page,path,status,next_review_on,note")));

        mockMvc.perform(get("/admin/exports/source-quality-watch.csv").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("city,page,path,status,total_sources,strong_sources,note")));

        mockMvc.perform(get("/admin/exports/deploy-readiness.csv").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("city,page,path,status,indexable_now,next_review_on,total_sources,strong_sources,renderable_providers,authority_backed_providers,note")));

        mockMvc.perform(get("/admin/exports/noindex-promotion-queue.csv").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("city,page,path,status,ready_to_promote,next_review_on,reason,total_sources,strong_sources,renderable_providers,authority_backed_providers,promotion_checklist")));

        mockMvc.perform(get("/admin/exports/search-demand-watch.csv").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("city,page,path,route_state,status,top_query,impressions_28d,clicks_28d,ctr,average_position,captured_on,note")));

        mockMvc.perform(get("/admin/exports/operator-utility-summary.csv").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("tool_slug,page_views,unique_visitors,returning_visitors,revisit_rate")));

        mockMvc.perform(get("/admin/exports/evidence-index.csv").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("city,page,path,status,indexable_now,next_review_on,snapshot_file")));

        mockMvc.perform(get("/admin/exports/ops-alerts.md").with(httpBasic("admin", "tlsgur3108")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("# Ops Alert Snapshot")));
    }
}
