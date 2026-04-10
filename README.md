# KitchenComplianceHub

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
- Noindex operator utilities under `/tools/*` for grease logs, hood record binders, and inspection reminder planning
- SSR pages include canonical, robots, structured data, source stack, and last-verified date
- Server-side click attribution now wraps local next-action CTAs and provider outbound links
- Local provider finder pages now include short operator lead forms, and all local verdict pages include sponsor inquiry forms
- Local route views and operator-tool views now also log server-side analytics with persistent visitor ids and verdict-state labels
- Read-only attribution and lead dashboard is available at `/admin`
- `/admin` now includes a freshness watch section for indexed-route review timing and near-due source checks
- `/admin` now includes a deploy-readiness section that rolls freshness, source quality, and finder coverage into a single pre-deploy route gate
- `/admin` now includes verdict-state breakdowns and operator-utility revisit metrics from page-view tracking
- `/admin` now includes lead totals, operator vs sponsor mix, provider-intent breakdowns, and recent lead rows from persistent CSV intake
- Provider finder cards are ordered by evidence quality first and now show an evidence label plus cited authority link when available
- `/admin/exports/*` exposes raw attribution CSV, attribution summary CSV, raw lead CSV, lead summary CSV, freshness watch CSV, source quality CSV, deploy readiness CSV, operator utility summary CSV, evidence index CSV, and ops alert Markdown
- Attribution events append to `${APP_ATTRIBUTION_LOG_DIR}` when set, or default to `${user.home}/.kitchencompliancehub/attribution/click-attribution.csv`
- Lead events append to `${APP_LEAD_LOG_DIR}` when set, or default to `${user.home}/.kitchencompliancehub/leads/lead-intake.csv`
- Scheduled ops audits now write freshness, source quality, deploy readiness, attribution, evidence index, source evidence snapshots, and alert outputs under `${APP_OPS_AUDIT_DIR}` or `${user.home}/.kitchencompliancehub/ops`
- Indexed pages are guarded by a freshness verification test that fails the build if any required source is past `nextReviewOn`
- Indexed pages are also guarded by a deploy-readiness gate that collapses freshness, source depth, and minimum finder inventory into a single blocker test

## Run and test
- Run app: `.\mvnw.cmd spring-boot:run`
- Run tests: `.\mvnw.cmd test`
- Persist attribution across redeploys: set `APP_ATTRIBUTION_LOG_DIR` to a mounted host or volume path before starting the app
- Persist leads across redeploys: set `APP_LEAD_LOG_DIR` to a mounted host or volume path before starting the app
- Review route freshness, source quality, deploy readiness, verdict-state, operator-utility revisit totals, and lead intake: open `/admin`
- Download ops exports: `/admin/exports/attribution-events.csv`, `/admin/exports/attribution-summary.csv`, `/admin/exports/lead-intake.csv`, `/admin/exports/lead-summary.csv`, `/admin/exports/freshness-watch.csv`, `/admin/exports/source-quality-watch.csv`, `/admin/exports/deploy-readiness.csv`, `/admin/exports/operator-utility-summary.csv`, `/admin/exports/evidence-index.csv`, `/admin/exports/ops-alerts.md`
- Download operator worksheets: `/tools/grease-log.csv`, `/tools/hood-record-binder.csv`, `/tools/inspection-reminder-plan.csv`
- Override scheduled audit behavior with `APP_OPS_AUDIT_DIR`, `APP_FRESHNESS_AUDIT_ENABLED`, `APP_FRESHNESS_AUDIT_CRON`, and `APP_FRESHNESS_AUDIT_ZONE`
- Current launch-surface progress: 5 of 5 Tier 1 cities are live from `spec/10`, and Nashville, TN, Grand Island, NE, and Miami, FL complete the planned Tier 2 expansion set

## Oracle VM deploy
- GitHub Actions deploy example: `.github/workflows/deploy-oracle-vm.yml`
- Systemd service example: `deploy/oracle/systemd/kitchencompliancehub.service`
- Environment file example: `deploy/oracle/systemd/kitchencompliancehub.env.example`
- Deployment notes: `deploy/oracle/README.md`
- Recommended persistent directories on the VM:
- `/var/lib/kitchencompliancehub/attribution`
- `/var/lib/kitchencompliancehub/leads`
- `/var/lib/kitchencompliancehub/ops`

## Agent read order
1. `AGENT_START_HERE.md`
2. `ops/context_tracker.md`
3. This file
4. `spec/00_strategy.md` through `spec/13_product_principles_and_page_checklists.md`

## Build principles
- This is a compliance plus next-action product, not a generic restaurant operations blog.
- Home can be issue-first, but canonical indexed destinations must resolve to a local authority context.
- Canonical pages must be tied to a local rule holder: city, utility, or fire authority.
- Every local page must show the exact requirement, the proof to keep on site, the consequence of missing it, and the next action.
- Official requirements and sponsor content must stay visibly separate.
- Launch scope is fixed to FOG, Type I hood cleaning, suppression and inspection-prep, and the records needed to survive inspection.
- Do not sprawl into full restaurant licensing, HACCP, labor posters, or opening-a-restaurant content.
- Build for direct sponsor sales and repeat operator utility, not display ad traffic.
- Keep source `verifiedOn` and `nextReviewOn` dates current for any route that remains indexable.
