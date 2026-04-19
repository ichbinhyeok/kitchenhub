package owner.kitchencompliance;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "app.site.base-url=http://localhost:8080",
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
                .andExpect(content().string(containsString("Official sources reviewed locally")))
                .andExpect(content().string(containsString("Browse City Coverage")))
                .andExpect(content().string(containsString("For Vendors")))
                .andExpect(content().string(containsString("href=\"/for-vendors\"")))
                .andExpect(content().string(containsString("Choose A City")))
                .andExpect(content().string(containsString("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/authority/nc/charlotte-fire-prevention/hood-cleaning-requirements")))
                .andExpect(content().string(containsString("/tx/austin/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/nc/charlotte/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/fl/miami/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/fl/tampa/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/ne/grand-island/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/ca/santa-clara/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/or/portland/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/tn/nashville/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/guides/fog-vs-grease-trap-cleaning")))
                .andExpect(content().string(containsString("Browse Authorities")))
                .andExpect(content().string(containsString("Austin Water Pretreatment Program")))
                .andExpect(content().string(containsString("/authorities")))
                .andExpect(content().string(containsString("/tools/hood-service-report")))
                .andExpect(content().string(containsString("/tools/grease-log")))
                .andExpect(content().string(containsString("/tools/missing-proof-tracker")))
                .andExpect(content().string(containsString("/tools/inspection-reminder-plan")))
                .andExpect(content().string(containsString("/methodology")))
                .andExpect(content().string(containsString("/not-government-affiliated")))
                .andExpect(content().string(containsString("https://www.googletagmanager.com/gtag/js?id=G-K0NZM8LCFF")))
                .andExpect(content().string(containsString("gtag('config', 'G-K0NZM8LCFF');")))
                .andExpect(content().string(not(containsString("Admin Login"))))
                .andExpect(content().string(containsString("application/ld+json")));
    }

    @Test
    void vendorLandingPageFramesTheFreeVendorWedgeWithoutReplacingHome() throws Exception {
        mockMvc.perform(get("/for-vendors"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"noindex,follow\"")))
                .andExpect(content().string(containsString("Send today's hood closeout in a few minutes")))
                .andExpect(content().string(containsString("Start today's hood closeout")))
                .andExpect(content().string(containsString("See the customer-ready report")))
                .andExpect(content().string(containsString("/tools/hood-service-report?draft=blank")))
                .andExpect(content().string(containsString("?draft=blank#customer-copy")))
                .andExpect(content().string(containsString("What you send")))
                .andExpect(content().string(containsString("A factual hood service report with the site, service date, work completed, proof attached, and any follow-up item.")))
                .andExpect(content().string(containsString("/authority/nc/charlotte-fire-prevention/hood-cleaning-requirements")))
                .andExpect(content().string(containsString("Why it helps the office")))
                .andExpect(content().string(containsString("What the customer gets")))
                .andExpect(content().string(containsString("Use the free report first, then open the local rule page only when the customer asks for records")))
                .andExpect(content().string(containsString("Find Your City")))
                .andExpect(content().string(containsString("For Vendors")))
                .andExpect(content().string(not(containsString("Admin Login"))));
    }

    @Test
    void publicTrustPagesRenderWithoutAdminLinksOrPlaceholderLegalRoutes() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("An operator-first local compliance workflow")))
                .andExpect(content().string(containsString("Not a government site or municipal portal.")))
                .andExpect(content().string(not(containsString("Admin Login"))))
                .andExpect(content().string(containsString("content=\"index,follow\"")));

        mockMvc.perform(get("/privacy"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Privacy policy")))
                .andExpect(content().string(containsString("content=\"noindex,follow\"")));

        mockMvc.perform(get("/methodology"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("the governing rule holder may be a utility, fire marshal, county, or other local authority")));
    }

    @Test
    void operatorToolsRenderAsNoindexUtilitiesWithDownloads() throws Exception {
        String today = LocalDate.now().toString();
        String displayToday = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, uuuu", Locale.US));

        mockMvc.perform(get("/tools/hood-service-report"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"noindex,follow\"")))
                .andExpect(content().string(containsString("Hood service report")))
                .andExpect(content().string(containsString("Use this as the customer-facing handoff")))
                .andExpect(content().string(containsString("Build the handoff in under two minutes")))
                .andExpect(content().string(containsString("Refresh handoff")))
                .andExpect(content().string(containsString("method=\"post\"")))
                .andExpect(content().string(containsString("Recipient name")))
                .andExpect(content().string(containsString("Recipient email")))
                .andExpect(content().string(containsString("Customer-facing report PDF or printout is ready.")))
                .andExpect(content().string(containsString("Photo set is attached.")))
                .andExpect(content().string(containsString("Invoice or internal report file is attached.")))
                .andExpect(content().string(containsString("Start faster")))
                .andExpect(content().string(containsString("Start blank closeout")))
                .andExpect(content().string(containsString("No follow-up on this visit")))
                .andExpect(content().string(containsString("Saved customer setup")))
                .andExpect(content().string(containsString("Save current customer")))
                .andExpect(content().string(containsString("Office defaults")))
                .andExpect(content().string(containsString("Apply saved defaults")))
                .andExpect(content().string(containsString("Save office defaults")))
                .andExpect(content().string(containsString("Recent local draft")))
                .andExpect(content().string(containsString("Restore recent draft")))
                .andExpect(content().string(containsString("Save current draft")))
                .andExpect(content().string(containsString("Save the current job draft in this browser only. Restore it later and keep working from the refreshed handoff.")))
                .andExpect(content().string(containsString("Local proof photos")))
                .andExpect(content().string(containsString("Add before and after images from this device only when you want the same browser tab to print them into the PDF closeout.")))
                .andExpect(content().string(containsString("Photos for this closeout")))
                .andExpect(content().string(containsString("accept=\"image/*\"")))
                .andExpect(content().string(containsString("Clear local photos")))
                .andExpect(content().string(containsString("Photo reference (optional)")))
                .andExpect(content().string(containsString("Report reference (optional)")))
                .andExpect(content().string(containsString("Use suggested photo label")))
                .andExpect(content().string(containsString("Use suggested report label")))
                .andExpect(content().string(containsString("Suggested photo label:")))
                .andExpect(content().string(containsString("Suggested report label:")))
                .andExpect(content().string(containsString("Leave these blank if the files are already attached and the customer does not need your internal file names.")))
                .andExpect(content().string(containsString("formaction=\"/tools/hood-service-report.txt\"")))
                .andExpect(content().string(containsString("formaction=\"/tools/hood-service-report.csv\"")))
                .andExpect(content().string(containsString("Print / PDF")))
                .andExpect(content().string(containsString("Include the city rule link in the customer email only when this account actually wants it.")))
                .andExpect(content().string(containsString("Keep the wording factual")))
                .andExpect(content().string(containsString("Attach these with it")))
                .andExpect(content().string(containsString("Closeout packet status")))
                .andExpect(content().string(containsString("Attachment bundle plan")))
                .andExpect(content().string(containsString("Use consistent file names so the office, customer, and later follow-up all point to the same closeout.")))
                .andExpect(content().string(containsString("example-bistro-" + today + "-hood-service-report.pdf")))
                .andExpect(content().string(containsString("example-bistro-" + today + "-before-after-photos.zip")))
                .andExpect(content().string(containsString("packet items ready")))
                .andExpect(content().string(containsString("Proof pack to send")))
                .andExpect(content().string(containsString("Customer-facing service report: this page or PDF printout")))
                .andExpect(content().string(containsString("Photo proof")))
                .andExpect(content().string(containsString("Local browser photos included in this closeout preview.")))
                .andExpect(content().string(containsString("Replace sample values before send")))
                .andExpect(content().string(containsString("Ready-to-send email")))
                .andExpect(content().string(containsString("Recipient email is blank. Copy the handoff or send it from your existing thread.")))
                .andExpect(content().string(containsString("Copy email body")))
                .andExpect(content().string(containsString("data-tool-event-endpoint=\"/tools/hood-service-report/events\"")))
                .andExpect(content().string(containsString("data-tool-action=\"copy_subject\"")))
                .andExpect(content().string(containsString("data-tool-action=\"copy_body\"")))
                .andExpect(content().string(containsString("data-tool-action=\"print_pdf\"")))
                .andExpect(content().string(containsString("data-tool-action=\"open_rule_page\"")))
                .andExpect(content().string(containsString("Location name: Example Bistro")))
                .andExpect(content().string(containsString("Where this fits in the office")))
                .andExpect(content().string(containsString("What the customer gets")))
                .andExpect(content().string(containsString("Why this gets reused")))
                .andExpect(content().string(containsString("Draft hood service report")))
                .andExpect(content().string(containsString("Prepared by:")))
                .andExpect(content().string(containsString("Closeout packet")))
                .andExpect(content().string(containsString("For your records: this report documents service performed and attached proof.")))
                .andExpect(content().string(containsString("This closeout is a customer record of service performed and attached proof. Local rule pages stay separate as reference.")))
                .andExpect(content().string(containsString("Attach the rule link without weakening the handoff")))
                .andExpect(content().string(containsString("Need the local hood rule page for your records? Reference link for Charlotte: http://localhost:8080/authority/nc/charlotte-fire-prevention/hood-cleaning-requirements")))
                .andExpect(content().string(not(containsString("Vendor-only setup"))))
                .andExpect(content().string(not(containsString("Setup lives only on this page"))))
                .andExpect(content().string(not(containsString("Ask about setup"))))
                .andExpect(content().string(not(containsString("repeat accounts"))))
                .andExpect(content().string(containsString("/tools/hood-service-report.csv")))
                .andExpect(content().string(containsString("/tools/hood-service-report.txt")))
                .andExpect(content().string(not(containsString("Sample handoff"))))
                .andExpect(content().string(not(containsString("href=\"/tools/hood-service-report.csv?"))))
                .andExpect(content().string(not(containsString("href=\"/tools/hood-service-report.txt?"))));

        mockMvc.perform(get("/tools/hood-service-report").param("draft", "blank"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Before you send this")))
                .andExpect(content().string(containsString("Hood service report | Charlotte location | " + displayToday)))
                .andExpect(content().string(not(containsString("Example Bistro"))))
                .andExpect(content().string(not(containsString("before-after-set-12"))))
                .andExpect(content().string(not(containsString("INV-2048"))))
                .andExpect(content().string(not(containsString("Hood service report | | " + displayToday))))
                .andExpect(content().string(not(containsString("Attached is today's hood service report for ."))))
                .andExpect(content().string(containsString("No follow-up items noted.")))
                .andExpect(content().string(not(containsString("Owner: TBD | Target date:"))));

        mockMvc.perform(get("/tools/grease-log"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Grease service log template")));

        mockMvc.perform(get("/tools/inspection-reminder-plan.csv"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("reminder_date,city,authority,issue_type,source_route,missing_proof,next_action,owner,status")));

        mockMvc.perform(get("/tools/hood-service-report.csv"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("service_date,city,authority,customer_name,location_name,recipient_name,recipient_email,site_address,vendor_name,crew_or_technician,systems_serviced,photo_reference,report_attachment_ready,photo_set_attached,report_file_attached,reference_link_added,follow_up_item,next_service_date,customer_handoff_note")));

        mockMvc.perform(get("/tools/hood-service-report.txt"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Subject: Hood service report | Example Bistro | " + displayToday)))
                .andExpect(content().string(containsString("Attached is today's hood service report for Example Bistro.")))
                .andExpect(content().string(not(containsString("Request setup"))))
                .andExpect(content().string(not(containsString("Vendor-only setup"))));

        mockMvc.perform(get("/tools/missing-proof-tracker.csv"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("detected_on,city,authority,issue_type,missing_proof,source_route,owner,next_review_on,status,closure_note")));
    }

    @Test
    void hoodServiceReportDraftFlowsIntoPageAndExports() throws Exception {
        mockMvc.perform(post("/tools/hood-service-report")
                        .param("city", "austin")
                        .param("serviceDate", "2026-04-19")
                        .param("nextServiceDate", "2026-07-19")
                        .param("customerName", "Sunset Tacos")
                        .param("siteName", "Sunset Tacos South")
                        .param("recipientName", "Maria")
                        .param("recipientEmail", "maria@sunsettacos.com")
                        .param("siteAddress", "500 Congress Ave, Austin, TX")
                        .param("vendorName", "Fire Clean Co")
                        .param("crewOrTechnician", "Crew 7")
                        .param("workOrderReference", "WO-9001")
                        .param("systemsServiced", "hood canopy, accessible duct, filters, rooftop fan")
                        .param("completedWork", "Cleaned canopy\nReinstalled filters")
                        .param("photoReference", "photo-set-44")
                        .param("reportReference", "INV-9001")
                        .param("customerHandoffNote", "Sent to owner and GM")
                        .param("followUpItems", "Replace bent hinge\nSchedule hinge re-check")
                        .param("followUpOwner", "GM")
                        .param("followUpDueDate", "2026-04-30")
                        .param("reportAttachmentReady", "true")
                        .param("photoSetAttached", "true")
                        .param("reportFileAttached", "true")
                        .param("referenceLinkAdded", "true")
                        .param("includeReferenceLink", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sunset Tacos")))
                .andExpect(content().string(containsString("500 Congress Ave, Austin, TX")))
                .andExpect(content().string(containsString("Fire Clean Co | Crew 7")))
                .andExpect(content().string(containsString("Systems serviced: hood canopy, accessible duct, filters, rooftop fan")))
                .andExpect(content().string(containsString("Replace bent hinge")))
                .andExpect(content().string(containsString("Schedule hinge re-check")))
                .andExpect(content().string(containsString("mailto:maria@sunsettacos.com?subject=")))
                .andExpect(content().string(containsString("data-tool-action=\"open_email_draft\"")))
                .andExpect(content().string(containsString("Hi Maria,")))
                .andExpect(content().string(containsString("Owner: GM | Target date: April 30, 2026")))
                .andExpect(content().string(containsString("Before and after photo set: photo-set-44")))
                .andExpect(content().string(containsString("Location name: Sunset Tacos South")))
                .andExpect(content().string(containsString("Ready to send")))
                .andExpect(content().string(containsString("Attachment checklist is complete.")))
                .andExpect(content().string(containsString("Open email draft")))
                .andExpect(content().string(containsString("sunset-tacos-south-2026-04-19-hood-service-report.pdf")))
                .andExpect(content().string(containsString("sunset-tacos-south-2026-04-19-before-after-photos.zip")))
                .andExpect(content().string(containsString("sunset-tacos-south-2026-04-19-austin-hood-rule-link.txt")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/tx/austin-fire-marshal/hood-cleaning-requirements")));

        mockMvc.perform(post("/tools/hood-service-report.txt")
                        .param("city", "austin")
                        .param("serviceDate", "2026-04-19")
                        .param("nextServiceDate", "2026-07-19")
                        .param("customerName", "Sunset Tacos")
                        .param("siteName", "Sunset Tacos South")
                        .param("recipientName", "Maria")
                        .param("recipientEmail", "maria@sunsettacos.com")
                        .param("vendorName", "Fire Clean Co")
                        .param("systemsServiced", "hood canopy, accessible duct, filters, rooftop fan")
                        .param("followUpItems", "Replace bent hinge\nSchedule hinge re-check")
                        .param("followUpOwner", "GM")
                        .param("followUpDueDate", "2026-04-30")
                        .param("reportAttachmentReady", "true")
                        .param("photoSetAttached", "true")
                        .param("reportFileAttached", "true")
                        .param("referenceLinkAdded", "true")
                        .param("includeReferenceLink", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("To: maria@sunsettacos.com")))
                .andExpect(content().string(containsString("Subject: Hood service report | Sunset Tacos South | April 19, 2026")))
                .andExpect(content().string(containsString("Hi Maria,")))
                .andExpect(content().string(containsString("Attached is today's hood service report for Sunset Tacos South.")))
                .andExpect(content().string(containsString("Systems serviced: hood canopy, accessible duct, filters, rooftop fan.")))
                .andExpect(content().string(containsString("Follow-up items: Replace bent hinge; Schedule hinge re-check.")))
                .andExpect(content().string(containsString("Local rule reference for records only: http://localhost:8080/authority/tx/austin-fire-marshal/hood-cleaning-requirements")))
                .andExpect(content().string(containsString("Fire Clean Co")));

        mockMvc.perform(post("/tools/hood-service-report")
                        .param("city", "austin")
                        .param("serviceDate", "2026-04-19")
                        .param("customerName", "")
                        .param("siteName", "")
                        .param("recipientName", "")
                        .param("recipientEmail", "")
                        .param("siteAddress", "")
                        .param("vendorName", "")
                        .param("crewOrTechnician", "")
                        .param("workOrderReference", "")
                        .param("completedWork", "")
                        .param("photoReference", "")
                        .param("reportReference", "")
                        .param("customerHandoffNote", "")
                        .param("followUpItems", "")
                        .param("followUpOwner", ""))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Before you send this")))
                .andExpect(content().string(containsString("Add the restaurant account name.")))
                .andExpect(content().string(containsString("Export or print the customer-facing report before send.")))
                .andExpect(content().string(not(containsString("Example Bistro"))))
                .andExpect(content().string(not(containsString("before-after-set-12"))))
                .andExpect(content().string(not(containsString("INV-2048"))))
                .andExpect(content().string(containsString("No follow-up items noted.")))
                .andExpect(content().string(not(containsString("Owner: TBD | Target date:"))));

        mockMvc.perform(post("/tools/hood-service-report")
                        .param("city", "miami")
                        .param("serviceDate", "2026-04-21")
                        .param("customerName", "Bayside Grill")
                        .param("siteName", "Bayside Grill Downtown")
                        .param("recipientName", "Andre")
                        .param("recipientEmail", "andre@baysidegrill.com")
                        .param("siteAddress", "225 NE 2nd St, Miami, FL")
                        .param("vendorName", "South Coast Hood")
                        .param("completedWork", "Cleaned canopy and filters")
                        .param("reportAttachmentReady", "true")
                        .param("photoSetAttached", "true")
                        .param("reportFileAttached", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Before and after photos: attached separately")))
                .andExpect(content().string(containsString("Invoice or internal report: attached separately")))
                .andExpect(content().string(containsString("Photo and report proof are covered by the attached files or a named reference.")))
                .andExpect(content().string(not(containsString("Add the photo reference or confirm the photo set is attached."))))
                .andExpect(content().string(not(containsString("Add the invoice or internal report reference."))));

                mockMvc.perform(post("/tools/hood-service-report.csv")
                        .param("city", "miami")
                        .param("serviceDate", "2026-04-21")
                        .param("customerName", "Bayside Grill")
                        .param("siteName", "Bayside Grill Downtown")
                        .param("recipientName", "Andre")
                        .param("recipientEmail", "andre@baysidegrill.com")
                        .param("siteAddress", "225 NE 2nd St, Miami, FL")
                        .param("vendorName", "South Coast Hood")
                        .param("reportAttachmentReady", "true")
                        .param("photoSetAttached", "true")
                        .param("reportFileAttached", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("2026-04-21,Miami,Miami-Dade Building Mechanical,Bayside Grill,Bayside Grill Downtown,Andre,andre@baysidegrill.com,\"225 NE 2nd St, Miami, FL\",South Coast Hood,,,,true,true,true,false")));
    }

    @Test
    void fogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/tx/austin/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Austin, TX Grease Trap Rules for Restaurants | Pump-Outs &amp; Manifests</title>")))
                .andExpect(content().string(containsString("content=\"Austin, TX grease trap rules for restaurants: interceptor approval, pump-out timing, manifests to keep on site, and hauler checks under Austin Water Pretreatment Program.\"")))
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Rule holder: Austin Water Pretreatment Program")))
                .andExpect(content().string(containsString("Open rule page")))
                .andExpect(content().string(containsString("Official sources for this page")))
                .andExpect(content().string(containsString("Austin publishes an official hauler or preferred-pumper list")))
                .andExpect(content().string(containsString("/authority/tx/austin-water-pretreatment/approved-grease-haulers")))
                .andExpect(content().string(containsString("action=\"/lead-intake/sponsor\"")))
                .andExpect(content().string(containsString("Want sponsor placement on Austin coverage?")));
    }

    @Test
    void authorityAliasRouteRendersAsCanonicalPage() throws Exception {
        mockMvc.perform(get("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Austin, TX Grease Trap Rules for Restaurants | Pump-Outs &amp; Manifests</title>")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Open city page")))
                .andExpect(content().string(containsString("Rule holder: Austin Water Pretreatment Program")));
    }

    @Test
    void authorityBrowseSurfaceRendersDirectoryAndDetailPages() throws Exception {
        mockMvc.perform(get("/authorities"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Browse by actual rule holder")))
                .andExpect(content().string(containsString("Filter by rule holder")))
                .andExpect(content().string(containsString("Jump to state")))
                .andExpect(content().string(containsString("href=\"/authorities?type=utility\"")))
                .andExpect(content().string(containsString("href=\"#state-tx\"")))
                .andExpect(content().string(containsString("Austin Water Pretreatment Program")))
                .andExpect(content().string(containsString("/authorities/tx/austin-water-pretreatment")))
                .andExpect(content().string(containsString("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("Open rule page")))
                .andExpect(content().string(containsString("CollectionPage")))
                .andExpect(content().string(containsString("ItemList")));

        mockMvc.perform(get("/authorities").param("type", "utility"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Utility offices")))
                .andExpect(content().string(containsString("content=\"noindex,follow\"")))
                .andExpect(content().string(containsString("Austin Water Pretreatment Program")))
                .andExpect(content().string(not(containsString("Austin Fire Marshal"))));

        mockMvc.perform(get("/authorities/tx/austin-water-pretreatment"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Routes from Austin Water Pretreatment Program for Austin, TX")))
                .andExpect(content().string(containsString("Routes from this office")))
                .andExpect(content().string(containsString("Open official source")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/authorities/tx/austin-water-pretreatment\"")))
                .andExpect(content().string(containsString("/tx/austin/restaurant-grease-trap-rules")));
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
                .andExpect(content().string(containsString("href=\"http://localhost:8080/authority/nc/charlotte-water-flow-free/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Charlotte publishes an official hauler or preferred-pumper list")));
    }

    @Test
    void tampaFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/fl/tampa/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/authority/fl/tampa-wastewater-grease-ordinance/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Tampa publishes an official hauler or preferred-pumper list")));
    }

    @Test
    void miamiFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/fl/miami/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/fl/miami/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Miami publishes an official hauler or preferred-pumper list")));
    }

    @Test
    void grandIslandFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/ne/grand-island/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/authority/ne/grand-island-utilities-fog-program/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Grand Island publishes an official hauler or preferred-pumper list")));
    }

    @Test
    void portlandFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/or/portland/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/authority/or/portland-bes-fog-program/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Portland publishes an official hauler or preferred-pumper list")));
    }

    @Test
    void santaClaraFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/ca/santa-clara/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/authority/ca/santa-clara-water-sewer-fog-program/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Santa Clara publishes an official hauler or preferred-pumper list")));
    }

    @Test
    void nashvilleFogRulesPageRendersCanonicalRobotsAndSources() throws Exception {
        mockMvc.perform(get("/tn/nashville/restaurant-grease-trap-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"index,follow\"")))
                .andExpect(content().string(containsString("href=\"http://localhost:8080/authority/tn/nashville-water-grease-management/restaurant-grease-trap-rules\"")))
                .andExpect(content().string(containsString("Nashville publishes an official hauler or preferred-pumper list")));
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
                .andExpect(content().string(containsString("Coverage ")))
                .andExpect(content().string(containsString("Route evidence reviewed")))
                .andExpect(content().string(containsString("Listings are routing support only.")))
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
                .andExpect(content().string(containsString("content=\"noindex,follow\"")))
                .andExpect(content().string(containsString("Liquid Environmental Solutions")))
                .andExpect(content().string(containsString("Austin Rooter")))
                .andExpect(content().string(containsString("Mahoney Environmental")))
                .andExpect(content().string(containsString("/out/providers/liquid-environmental-solutions-austin")))
                .andExpect(content().string(containsString("/out/providers/austin-rooter-grease-service")))
                .andExpect(content().string(containsString("/out/providers/mahoney-environmental-austin")))
                .andExpect(content().string(containsString("Check before you book")))
                .andExpect(content().string(containsString("Operator review needed before booking")))
                .andExpect(content().string(containsString("Verification checklist")))
                .andExpect(content().string(containsString("Open local route map")))
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
                .andExpect(content().string(containsString("Evidence and contact details")))
                .andExpect(content().string(containsString("Coverage ")))
                .andExpect(content().string(containsString("Route evidence reviewed")))
                .andExpect(content().string(containsString("Listings are routing support only.")))
                .andExpect(content().string(containsString("BreadcrumbList")))
                .andExpect(content().string(containsString("ItemList")))
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
                .andExpect(content().string(containsString("content=\"noindex,follow\"")))
                .andExpect(content().string(containsString("Ameri-Clean Pumping, Inc.")))
                .andExpect(content().string(containsString("Grease Trap FL")))
                .andExpect(content().string(containsString("United Septic and Grease")))
                .andExpect(content().string(containsString("/out/providers/ameri-clean-pumping-miami")))
                .andExpect(content().string(containsString("/out/providers/grease-trap-fl-miami")))
                .andExpect(content().string(containsString("/out/providers/united-septic-grease-miami")))
                .andExpect(content().string(containsString("Check before you book")))
                .andExpect(content().string(containsString("Operator review needed before booking")))
                .andExpect(content().string(containsString("Visit provider site")))
                .andExpect(content().string(not(containsString("MANUAL_ONLY"))));

        mockMvc.perform(get("/fl/miami/find-hood-cleaner"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("content=\"noindex,follow\"")))
                .andExpect(content().string(containsString("Hoods Cleaning Experts")))
                .andExpect(content().string(containsString("Kitchen Guard of Miami")))
                .andExpect(content().string(containsString("Miami Hood Cleaning LLC")))
                .andExpect(content().string(containsString("/out/providers/hood-cleaning-experts-miami")))
                .andExpect(content().string(containsString("/out/providers/kitchen-guard-miami")))
                .andExpect(content().string(containsString("/out/providers/miami-hood-cleaning-llc")))
                .andExpect(content().string(containsString("Check before you book")))
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
                .andExpect(content().string(containsString("Evidence ")))
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
                        "href=\"/out/cta?source=/tx/austin/restaurant-grease-trap-rules&amp;target=/authority/tx/austin-water-pretreatment/approved-grease-haulers&amp;sponsored=false\""
                )));
    }

    @Test
    void sitemapIncludesIndexedPagesForAllLiveCities() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("http://localhost:8080/authority/ca/santa-clara-water-sewer-fog-program/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/authorities")))
                .andExpect(content().string(containsString("http://localhost:8080/authorities/tx/austin-water-pretreatment")))
                .andExpect(content().string(containsString("http://localhost:8080/fl/miami/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/fl/tampa-wastewater-grease-ordinance/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/ne/grand-island-utilities-fog-program/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/or/portland-bes-fog-program/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/tn/nashville-water-grease-management/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/nc/charlotte-water-flow-free/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("http://localhost:8080/guides/what-records-restaurant-inspections-check")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/ca/santa-clara-fire-department/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/ca/santa-clara-water-sewer-fog-program/find-grease-service")))
                .andExpect(content().string(not(containsString("http://localhost:8080/fl/miami/find-hood-cleaner"))))
                .andExpect(content().string(not(containsString("http://localhost:8080/fl/miami/find-grease-service"))))
                .andExpect(content().string(containsString("http://localhost:8080/authority/or/portland-fire-safety-inspection-program/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/or/portland-bes-fog-program/find-grease-service")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/ne/grand-island-fire-department/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/ne/grand-island-utilities-fog-program/find-grease-service")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/tn/nashville-fire-marshal/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/tn/nashville-water-grease-management/find-grease-service")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/fl/tampa-fire-marshal/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/fl/tampa-wastewater-grease-ordinance/find-grease-service")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/tx/austin-fire-marshal/find-hood-cleaner")))
                .andExpect(content().string(not(containsString("http://localhost:8080/tx/austin/find-grease-service"))))
                .andExpect(content().string(containsString("http://localhost:8080/authority/nc/charlotte-fire-prevention/find-hood-cleaner")))
                .andExpect(content().string(containsString("http://localhost:8080/authority/nc/charlotte-water-flow-free/find-grease-service")));
    }

    @Test
    void guidePagesUseSharedLinksThatReferenceMultipleLiveCities() throws Exception {
        mockMvc.perform(get("/guides/fog-vs-grease-trap-cleaning"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Local pages to open next")))
                .andExpect(content().string(containsString("Keep going with a local page")))
                .andExpect(content().string(containsString("Austin Water Pretreatment Program")))
                .andExpect(content().string(containsString("/authority/tx/austin-water-pretreatment/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/authority/nc/charlotte-water-flow-free/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("/fl/miami/restaurant-grease-trap-rules")))
                .andExpect(content().string(containsString("Austin, TX")))
                .andExpect(content().string(containsString("Charlotte, NC")))
                .andExpect(content().string(containsString("Miami, FL")))
                .andExpect(content().string(not(containsString("Santa Clara, CA"))));
    }
}
