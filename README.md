# KitchenRuleHub

**Date:** 2026-04-07 (Asia/Seoul)  
**Purpose:** This folder is a self-contained design packet for building a US-focused **commercial kitchen compliance and next-action site** centered on FOG control, hood cleaning, and inspection-prep workflows.

## What you are building
A city and authority aware operations-compliance site for restaurant owners, kitchen managers, and multi-location operators who need to know what the local rule requires, what proof must be kept on site, what happens when something is overdue or missing, and which local vendor to call next.

## Why this concept is attractive
- Search intent is operational and action-heavy, not generic educational traffic.
- Demand repeats because cleaning, manifests, inspections, and records recur.
- The moat is local rule normalization plus operator workflow clarity.
- Revenue can come from hood cleaners, grease haulers, trap services, inspection-prep bundles, and reminder products.

## File map
- `AGENT_START_HERE.md` - read order and handoff rules for any future agent
- `ops/context_tracker.md` - current status, decisions, and next tasks
- `ops/wedge_focus_2026-04-12.md` - current primary wedge, route tiers, and the narrow operating loop for the next 4 to 6 weeks
- `ops/source_audit_2026-04-07.md` - latest web-backed source verification pass across the live city cohort
- `spec/00_strategy.md` - market thesis, positioning, core wedge, and rollout philosophy
- `spec/01_query_and_user_map.md` - jobs-to-be-done, user states, query families, and priority pages
- `spec/02_site_architecture.md` - canonical entities, URL graph, page modules, and internal linking
- `spec/03_data_and_operations.md` - data model, source hierarchy, verification workflow, and refresh cadence
- `spec/04_commercial_model.md` - sponsor model, CTA logic, lead handling, and retention paths
- `spec/05_editorial_rules_and_execution.md` - writing rules, trust guardrails, launch phases, and definition of done
- `spec/06_indexing_quality_and_analytics.md` - indexing rules, quality gates, kill rules, and measurement plan
- `spec/07_technical_architecture.md` - system boundaries, package map, rendering model, and operational services
- `spec/08_delivery_and_handoff.md` - workstreams, milestones, acceptance criteria, and handoff order
- `spec/09_domain_model_and_entity_schema.md` - concrete entities, package boundaries, and launch-safe field definitions
- `spec/10_route_inventory_and_launch_surface.md` - route families, initial city cohort, and launch page counts
- `spec/11_sample_data_contracts.md` - JSON/CSV-shaped examples for authorities, providers, routes, and leads
- `spec/12_acceptance_test_matrix.md` - implementation-level done checks for launch-critical user flows
- `spec/13_product_principles_and_page_checklists.md` - page-family ship criteria for home, local rules, finders, guides, and operator tools
- `spec/14_seo_indexing_strategy.md` - organic acquisition, internal linking, indexing states, and 90-day SEO operating model
- `spec/15_authority_first_route_design.md` - mixed-governance route rules for city-first entry with authority-first canonical logic

## Package root
`owner.kitchencompliance`

## Code bootstrap
- `src/main/java/owner/kitchencompliance` - application package root
- `src/test/java/owner/kitchencompliance` - test package root

## Current implementation
- Spring Boot MVC plus jte app scaffolded with Maven Wrapper
- File-backed JSON registry under `src/main/resources/data`
- Austin, TX seed coverage for:
  - FOG rules
  - approved haulers
  - hood requirements
  - fire inspection checklist
  - grease service finder
  - hood cleaner finder
- Charlotte, NC seed coverage for the same six local route families, including public finder inventory for grease service and hood cleaning
- Tampa, FL seed coverage for the same six local route families, including public finder inventory for grease service and hood cleaning
- Portland, OR seed coverage for the same six local route families, including public finder inventory for grease service and hood cleaning
- Santa Clara, CA seed coverage for the same six local route families, including public finder inventory for grease service and hood cleaning
- Nashville, TN seed coverage for the same six local route families, including Metro Water FOG guidance, Fire Marshal inspection workflow, and public finder inventory for grease service and hood cleaning
- Grand Island, NE seed coverage for the same six local route families, including city FOG guidance, preferred hauler workflow, fire permit workflow, and public finder inventory for grease service and hood cleaning
- Miami, FL seed coverage for the same six local route families, including Miami-Dade FOG permit workflow, county building and fire inspection guidance, and public finder inventory for grease service and hood cleaning
- Each city finder route now carries at least three public provider listings per category so the launch cohort is not single-listing thin
- Evergreen guides under `/guides/*`
- Noindex operator utilities under `/tools/*` for grease logs, hood record binders, missing-proof tracking, and inspection reminder planning
- SSR pages include canonical, robots, structured data, source stack, and last-verified date
- Server-side click attribution now wraps local next-action CTAs and provider outbound links
- Local provider finder pages now include short operator lead forms, and all local verdict pages include sponsor inquiry forms
- Local route views and operator-tool views now also log server-side analytics with persistent visitor ids and verdict-state labels
- Read-only attribution and lead dashboard is available at `/admin`
- `/admin` and `/admin/exports/*` now require Spring Security authentication instead of relying on `noindex`
- `/admin` now includes a freshness watch section for indexed-route review timing and near-due source checks
- `/admin` now includes a deploy-readiness section that rolls freshness, source quality, and finder coverage into a single pre-deploy route gate
- `/admin` now includes a noindex promotion queue so intentionally held routes keep a visible reason, promotion checklist, and next review date
- `/admin` now includes verdict-state breakdowns and operator-utility revisit metrics from page-view tracking
- `/admin` now includes lead totals, operator vs sponsor mix, provider-intent breakdowns, and recent lead rows from persistent CSV intake
- Provider finder cards are ordered by evidence quality first and now show an evidence label plus cited authority link when available
- Weak finder routes can stay live for operators while remaining `noindex-monitored` until provider evidence is strong enough for search
- Public trust pages now exist for `/about`, `/methodology`, `/contact`, `/privacy`, `/terms`, `/sponsor-policy`, `/not-government-affiliated`, and `/corrections`
- Finder pages now keep evidence, source, and provider context ahead of operator lead capture even when the route is strong enough to index
- Mixed-governance routes now keep city URLs as entry surfaces while emitting authority-first canonical URLs and live `/authority/{state}/{authorityId}/{slug}` aliases for utility- and fire-owned workflows
- Authority alias page views, CTA redirects, provider outbound links, lead intake, sitemap entries, and admin path reporting now preserve the authority-first path instead of collapsing everything back to the city entry URL
- Home city cards now expose the live rule holder for grease and hood workflows, and evergreen guides now include authority-first route maps that link both the direct authority page and the city entry page
- A public `/authorities` browse surface now exposes the actual utility, fire AHJ, or local department behind each city entry, with detail pages for each rule holder
- `/admin` now includes an imported Search Console demand snapshot so noindex promotion, CTR work, and discoverability work are visible without relying on memory
- Finder provider cards now show coverage-confidence, why-listed, and route-evidence-review signals instead of stopping at a generic listing label
- Local pages now emit more precise structured data, including breadcrumb trails and provider/authority item lists where the page is actually a browse surface
- `/admin/exports/*` exposes raw attribution CSV, attribution summary CSV, raw lead CSV, lead summary CSV, freshness watch CSV, source quality CSV, deploy readiness CSV, operator utility summary CSV, evidence index CSV, and ops alert Markdown
- `/admin/exports/*` also exposes search demand watch CSV for imported route-demand review
- Attribution events append to `${APP_ATTRIBUTION_LOG_DIR}` when set, or default to `${user.home}/.kitchenrulehub/attribution/click-attribution.csv`
- Lead events append to `${APP_LEAD_LOG_DIR}` when set, or default to `${user.home}/.kitchenrulehub/leads/lead-intake.csv`
- Scheduled ops audits now write freshness, source quality, deploy readiness, attribution, evidence index, source evidence snapshots, and alert outputs under `${APP_OPS_AUDIT_DIR}` or `${user.home}/.kitchenrulehub/ops`
- Scheduled ops audits now also write a noindex promotion queue snapshot so held routes are not forgotten between sessions
- Indexed pages are guarded by a freshness verification test that fails the build if any required source is past `nextReviewOn`
- Indexed pages are also guarded by a deploy-readiness gate that collapses freshness, source depth, and minimum finder inventory into a single blocker test

## Run and test
- Run app: `.\mvnw.cmd spring-boot:run`
- Run tests: `.\mvnw.cmd test`
- Persist attribution across redeploys: set `APP_ATTRIBUTION_LOG_DIR` to a mounted host or volume path before starting the app
- Persist leads across redeploys: set `APP_LEAD_LOG_DIR` to a mounted host or volume path before starting the app
- Override admin login in deployment: set `APP_ADMIN_USERNAME` and `APP_ADMIN_PASSWORD` before starting the app
- Review route freshness, source quality, deploy readiness, verdict-state, operator-utility revisit totals, and lead intake: open `/admin`
- Download ops exports: `/admin/exports/attribution-events.csv`, `/admin/exports/attribution-summary.csv`, `/admin/exports/lead-intake.csv`, `/admin/exports/lead-summary.csv`, `/admin/exports/freshness-watch.csv`, `/admin/exports/source-quality-watch.csv`, `/admin/exports/deploy-readiness.csv`, `/admin/exports/operator-utility-summary.csv`, `/admin/exports/evidence-index.csv`, `/admin/exports/ops-alerts.md`
- Download noindex queue export: `/admin/exports/noindex-promotion-queue.csv`
- Download search demand export: `/admin/exports/search-demand-watch.csv`
- Download operator worksheets: `/tools/grease-log.csv`, `/tools/hood-record-binder.csv`, `/tools/missing-proof-tracker.csv`, `/tools/inspection-reminder-plan.csv`
- Override scheduled audit behavior with `APP_OPS_AUDIT_DIR`, `APP_FRESHNESS_AUDIT_ENABLED`, `APP_FRESHNESS_AUDIT_CRON`, and `APP_FRESHNESS_AUDIT_ZONE`
- Current launch-surface progress: 5 of 5 Tier 1 cities are live from `spec/10`, and Nashville, TN, Grand Island, NE, and Miami, FL complete the planned Tier 2 expansion set

## OCI Docker deploy
- GitHub Actions deploy workflow: `.github/workflows/deploy.yml`
- Runtime container definition: `docker-compose.yml`
- Build image definition: `Dockerfile`
- Required GitHub secrets:
  - `DOCKERHUB_USERNAME`
  - `DOCKERHUB_TOKEN`
  - `OCI_HOST`
  - `OCI_USERNAME`
  - `OCI_KEY`
  - `APP_ADMIN_USERNAME`
  - `APP_ADMIN_PASSWORD`
- Optional GitHub variable:
  - `OCI_APP_PORT` - host port that nginx should proxy to; defaults to `8096` if unset
- Runtime persistent directories on the OCI host:
  - `~/deploy/kitchenrulehub/var/attribution`
  - `~/deploy/kitchenrulehub/var/leads`
  - `~/deploy/kitchenrulehub/var/ops`

## Agent read order
1. `AGENT_START_HERE.md`
2. `ops/context_tracker.md`
3. This file
4. `spec/00_strategy.md` through `spec/15_authority_first_route_design.md`

## Build principles
- This is a compliance plus next-action product, not a generic restaurant operations blog.
- Home can be issue-first, but canonical indexed destinations must resolve to a local authority context.
- Canonical pages must be tied to a local rule holder: city, utility, or fire authority.
- Every local page must show the exact requirement, the proof to keep on site, the consequence of missing it, and the next action.
- Official requirements and sponsor content must stay visibly separate.
- `noindex` routes must live in a monitored promotion queue with a reason, a checklist, and a next review date.
- Organic search should support acquisition, but canonical local rule pages remain the SEO core.
- Launch scope is fixed to FOG, Type I hood cleaning, suppression and inspection-prep, and the records needed to survive inspection.
- Do not sprawl into full restaurant licensing, HACCP, labor posters, or opening-a-restaurant content.
- Build for direct sponsor sales and repeat operator utility, not display ad traffic.
- Keep the site surface broad if useful, but keep weekly learning and SEO interpretation narrow around the active wedge.
- Keep source `verifiedOn` and `nextReviewOn` dates current for any route that remains indexable.
