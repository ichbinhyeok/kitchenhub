# Context Tracker

## Current status
- Product docs are merged and agent-readable.
- A Spring Boot MVC implementation now exists and runs from Maven Wrapper.
- The sharp wedge is local FOG plus hood plus inspection-prep, not broad restaurant compliance.
- The strongest monetization path is direct sponsor routing for hood cleaning, grease hauling, and grease service vendors.
- The strongest product moat is city and authority rule normalization plus recurring operator workflows.
- Code bootstrap package roots are now scaffolded under `src/main/java` and `src/test/java`.
- Implementation-prep specs now include entity schema, route inventory, sample data contracts, and acceptance tests.
- Austin, TX, Charlotte, NC, Tampa, FL, Portland, OR, Santa Clara, CA, Nashville, TN, Grand Island, NE, and Miami, FL are implemented as live seed slices with SSR local pages and three guides.
- Contract, routing, decision, freshness, attribution, admin, and SSR integration tests pass with `.\mvnw.cmd test` across the five-city Tier 1 cohort plus the full three-city Tier 2 set.

## Latest decisions
- Package root is `owner.kitchencompliance`.
- Canonical entities are local enforcement authorities and cities, not generic restaurant categories.
- Launch scope is fixed to FOG control, hood cleaning cadence, suppression and recordkeeping, and inspection-prep.
- Vendor pages are secondary. Rule and verdict pages are the primary entry surfaces.
- Approved-list pages require official list evidence. Otherwise the product must fall back to self-verification guidance.
- This product should be treated as a direct sponsor and retention candidate, not a pure affiliate bet.
- Spring Boot `3.4.4` is the implementation baseline because it cleanly supports the expected `com.fasterxml.jackson` MVC stack.
- Provider finder pages stay live but `noindex` until real public or active sponsor inventory exists.
- Indexed routes now have an explicit freshness gate: if any source is past `nextReviewOn`, the route is treated as stale and the build-level verification test fails.
- Local CTA links and provider outbound links now flow through a server-side attribution redirect that logs city, page family, issue type, and sponsored status.
- Attribution storage now defaults outside the app working directory and can be pinned with `APP_ATTRIBUTION_LOG_DIR` for deploy-safe persistence.
- Deploy readiness is now a first-class gate: freshness, source depth, and minimum finder coverage are assessed together before an indexed route is treated as deploy-safe.
- Operator utilities now exist as noindex SSR surfaces for grease logs, hood binders, and inspection reminder planning.
- Server-side analytics now track visitor ids, verdict states, and operator-tool revisits in addition to click events.
- Lead capture now stays file-backed and deploy-safe by default via `${user.home}/.kitchenrulehub/leads`, with `/admin` exposing raw and summary lead exports.
- Home should be issue-first for anxious operators, while canonical indexed pages must stay city-first and authority-aware.
- Every local verdict page should follow the same operator order: authority requirement, proof on site, fail conditions, then next action.
- Finder trust now depends on visible provider evidence quality and explicit separation from official guidance.
- Weak routes should move into a monitored noindex promotion queue, not disappear into an unowned backlog.
- Organic acquisition is allowed, but canonical local rule pages remain the SEO core and weak pages stay monitored instead of forced into indexation.
- `/admin` is a protected surface and should never rely on `noindex` as its only guardrail.
- Public trust pages should exist before launch so the product can explain independence, methodology, sponsor boundaries, privacy, and corrections in its own voice.
- City-first entry can stay, but mixed-governance markets need an authority-first canonical design rule.
- Authority-first canonical logic is now implemented for utility- and fire-owned routes, with city URLs acting as entry surfaces and `/authority/...` aliases acting as canonical paths.
- Search-demand snapshots should be treated as an explicit ops input, not tribal knowledge, when deciding promotion, CTR fixes, or discoverability work.
- Public brand and launch domain are now `KitchenRuleHub` and `kitchenrulehub.com`, while the internal Java package root stays `owner.kitchencompliance`.
- Production deploy now targets Docker Hub plus OCI over SSH using `shinhyeok22/kitchenhub`, with the legacy jar-copy Oracle workflow removed from `main` pushes.

## What changed this session
- Created a standardized design packet under `C:\Development\Owner\KitchenComplianceHub`.
- Converted the sharp v1-v3 source note into the shared `README + AGENT_START_HERE + ops + spec/00-08` format.
- Locked the project around local rule clarity, operator workflow states, source freshness, and direct vendor routing.
- Added bootstrap package roots for implementation.
- Added `spec/09` through `spec/12` for implementation handoff.
- Added Maven Wrapper, Spring Boot MVC app structure, JSON seed loading, route resolution, jte templates, and Austin launch data.
- Added sitemap, robots, canonical metadata, structured data, and source-stack rendering.
- Added test coverage for seed contracts, decision logic, rendered launch pages, and freshness verification.
- Added real Austin provider inventory and a second city slice for Charlotte, NC.
- Added public Charlotte provider inventory and moved Charlotte finder routes from held `noindex` to indexable.
- Added Tampa, FL as the third live city slice with official grease ordinance, fire inspection, and public provider inventory.
- Added Portland, OR as the fourth live city slice with BES FOG rules, Preferred Pumper guidance, fire inspection workflow, and public provider inventory.
- Added Santa Clara, CA as the fifth live city slice with city FOG program materials, fire inspection guidance, and public provider inventory.
- Added server-side click attribution for next-action CTAs and provider outbound links, with CSV logging under the configured external attribution directory.
- Added a read-only `/admin` dashboard for attribution totals, breakdowns, and recent events.
- Added a freshness watch section to `/admin` so indexed-route review timing is visible without running tests locally.
- Added third public provider listings across the five-city cohort so each finder category now has at least three public options.
- Added evidence-quality ordering for provider cards so authority-cited listings sort ahead of contact-only listings.
- Added `/admin/exports/*` CSV exports for raw attribution, attribution summaries, and freshness watch data.
- Added scheduled ops snapshots that write freshness and attribution summary CSVs to the configured ops directory.
- Moved default attribution storage to `${user.home}/.kitchenrulehub/attribution` and added env override support for mounted persistent storage.
- Added Nashville, TN as the first Tier 2 expansion city with Metro Water FOG policy coverage, approved hauler workflow, Fire Marshal hood reporting, inspection-prep records, and public finder inventory.
- Added Grand Island, NE as the second Tier 2 expansion city with city FOG guidance, approved preferred hauler workflow, Fire Department hood permit records, inspection-prep records, and public finder inventory.
- Added Miami, FL as the third Tier 2 expansion city with Miami-Dade FOG permit guidance, liquid waste transporter workflow, county building and fire inspection requirements, and public finder inventory.
- Ran a current-source audit across the live Tier 1 and Tier 2 cohort and corrected Santa Clara's FOG canonical URL to the newer sewer-utility path.
- Captured the live-cohort audit results in `ops/source_audit_2026-04-07.md`.
- Added a deploy-readiness dashboard, export, ops snapshot, and build gate so the pre-deploy surface now has a single blocker view across freshness, source quality, and finder inventory.
- Added noindex operator tools with CSV worksheet downloads for grease logs, hood record binders, and inspection reminder planning.
- Expanded analytics to include local page views, operator tool views, visitor ids, verdict states, and operator utility revisit summaries.
- Added source evidence snapshots and ops alert Markdown outputs to the scheduled audit bundle.
- Added short operator lead forms on provider finder pages and sponsor inquiry forms on local verdict pages, with persistent CSV capture and `/admin` lead reporting.
- Added an Oracle VM deployment skeleton with a GitHub Actions workflow, `systemd` service example, and persistent-path env example so redeploys do not wipe attribution or lead CSV files.
- Reworked the visual and content hierarchy on home, hood, inspection, and provider pages so the product reads as an operator compliance workflow instead of a vendor-led directory.
- Validated the current Austin operator flow against official Austin fire inspection and liquid waste hauler guidance, then tightened provider evidence visibility and inspection taxonomy accordingly.
- Added `spec/13_product_principles_and_page_checklists.md` so future work can be judged against page-family ship and no-ship criteria instead of ad hoc taste calls.
- Added a noindex promotion queue in `/admin`, export snapshots, and route metadata support so intentionally held routes keep a visible reason, checklist, and next review date.
- Added `spec/14_seo_indexing_strategy.md` to codify canonical local SEO, guide linking, indexing states, finder thresholds, freshness discipline, and a 90-day organic priority model.
- Added Spring Security protection for `/admin` and `/admin/exports/*` with env-overridable credentials.
- Added public trust and legal pages for about, methodology, contact, privacy, terms, sponsor policy, independence, and corrections.
- Removed quasi-official UI copy and hardcoded seal usage from public surfaces so the product reads as an independent source-backed workflow rather than a municipal portal.
- Forced all finder pages into evidence-first ordering so source and provider context appear before operator lead capture.
- Added `spec/15_authority_first_route_design.md` to document how city-first entry and authority-first canonical logic should coexist in mixed-governance markets.
- Implemented authority-first canonical routing, authority alias URLs, governance messaging on local pages, authority-aware sitemap output, and path-preserving attribution and lead logging.
- Extended authority-first exposure into home and evergreen guides so city entry surfaces now show the actual rule holder and guides can drop directly into authority-backed local routes.
- Added a public authority browse directory plus authority detail pages so mixed-governance markets have a first-class browse surface, not just hidden canonical aliases.
- Added imported search-demand snapshots, `/admin` demand reporting, CSV exports, and ops audit snapshots so noindex promotion and CTR work can be reviewed on a standing loop.
- Upgraded provider finder cards with coverage-confidence, why-listed, and route-evidence-review labels so evidence quality stays visible at the card level.
- Upgraded structured data on local and browse pages to include breadcrumb trails plus item-list markup only where the page truly behaves like a directory or collection.
- Reworked the home front door so issue-first cards now send the first click directly into local grease, hood, and inspection pages, with guides moved to support status.
- Hardened sponsor and operator lead intake with a honeypot field, form-age guard, stronger validation, and normalization while preserving the existing redirect flow.
- Added a sponsor-beta focus section to `/admin` so Austin, Miami, and Charlotte are visible as the primary launch-market sales slice.
- Hardened launch config by requiring production env values for base URL and admin credentials, updated deploy docs, and blocked `/admin` plus `/login` in `robots.txt`.
- Added `ops/sponsor_beta_plan_2026-04-12.md` and `ops/sponsor_beta_targets_2026-04-12.csv` so the next operator can run sponsor outreach from the strongest live demand routes instead of inventing a GTM plan from scratch.
- Added `ops/route_promotion_triggers_2026-04-12.md` and `ops/route_trigger_tracker_2026-04-12.csv` so promotion, hold states, and next review dates are tracked at the route level instead of living in memory.
- Rebranded the public product surface, deploy assets, and runtime metadata from `KitchenComplianceHub` to `KitchenRuleHub`, updated deploy defaults to `kitchenrulehub.com`, and renamed the Spring Boot entrypoint plus Oracle service examples accordingly.
- Added `Dockerfile`, `docker-compose.yml`, and `.github/workflows/deploy.yml` so `main` can build an ARM64 image, push to Docker Hub, ship `docker-compose.yml` to OCI, and restart the live container behind nginx.

## Next recommended tasks
- Improve provider ranking beyond evidence presence alone by weighting city-specific coverage, direct local numbers, and freshness of the cited authority source.
- Encode the new page-family checklist from `spec/13` into acceptance tests and recurring product QA so trust regressions are caught before deploy.
- Decide which future city or finder slices should enter the noindex promotion queue first instead of being built directly as indexed pages.
- Encode the SEO strategy into future page titles, guide links, and route promotion review cadence.
- Decide whether alert delivery should stay file-and-log based or escalate to email, Slack, or issue creation.
- Keep source dates and provider depth current; deploy readiness now fails closed when indexed routes fall out of bounds.
- Run the sponsor beta plan in `ops/sponsor_beta_plan_2026-04-12.md` before adding more cities or guide inventory.
- Use `ops/route_trigger_tracker_2026-04-12.csv` as the weekly route-review source of truth and only promote held routes when the trigger doc and route checklist both pass.
- Set the real production env file to `APP_SITE_BASE_URL=https://kitchenrulehub.com` before the first deploy and submit the live sitemap to Search Console after launch.
- Add GitHub secrets `APP_ADMIN_USERNAME` and `APP_ADMIN_PASSWORD` before the first OCI deploy; the new container workflow treats them as required and the production profile will not boot without them.
- Decide whether authority alias routes should stay secondary navigation inside home/guides or become first-class browse surfaces elsewhere in the product.
- Decide how imported Search Console snapshots should be refreshed operationally: manual JSON seed commit, admin upload flow, or external pull job.

## Open questions
- Which launch cluster is best: Hampton Roads, Northern California, Florida Gulf, or Texas?
- Should utility-level pages outrank city pages in mixed-governance markets?
- How much of the reminder and log workflow should exist in v1 versus v2?
- Should inspection-prep templates be gated lead magnets or fully public utilities?
- What is the minimum provider-count and vetting bar for keeping a finder route indexed over time?

## Source notes
- Original ideation source: `C:\Users\tlsgu\OneDrive\문서\카카오톡 받은 파일\KitchenComplianceHub_Sharp_v1-v3.md`
