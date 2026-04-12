# Agent Start Here

## Project
KitchenRuleHub

## Package root
`owner.kitchencompliance`

## What this folder contains
- A self-contained product and implementation spec for a local-rule commercial kitchen compliance site.
- Enough context for a new agent to resume work without chat history.

## Read order
1. `ops/context_tracker.md`
2. `ops/wedge_focus_2026-04-12.md`
3. `README.md`
4. `spec/00_strategy.md`
5. `spec/01_query_and_user_map.md`
6. `spec/02_site_architecture.md`
7. `spec/03_data_and_operations.md`
8. `spec/04_commercial_model.md`
9. `spec/05_editorial_rules_and_execution.md`
10. `spec/06_indexing_quality_and_analytics.md`
11. `spec/07_technical_architecture.md`
12. `spec/08_delivery_and_handoff.md`
13. `spec/09_domain_model_and_entity_schema.md`
14. `spec/10_route_inventory_and_launch_surface.md`
15. `spec/11_sample_data_contracts.md`
16. `spec/12_acceptance_test_matrix.md`
17. `spec/13_product_principles_and_page_checklists.md`
18. `spec/14_seo_indexing_strategy.md`
19. `spec/15_authority_first_route_design.md`

## Rules
- Launch scope is FOG plus hood cleaning plus inspection prep only.
- This is not a generic restaurant compliance portal.
- Utility, city, and fire-AHJ differences must be preserved, not flattened.
- Do not publish city pages without authority-backed source coverage.
- Never imply a vendor is officially approved unless the authority publishes that list.
- Where no approved list exists, show self-verification steps instead of inventing certainty.
- Every local page must carry a source stack and a last-verified date.
- Noindex routes must stay visible in the promotion queue with a reason, checklist, and next review date.
- Keep the published site surface and the active operating wedge separate in your head; the wedge doc controls weekly focus.
- Update `ops/context_tracker.md` after meaningful changes.

## Minimum handoff standard
- Update `Current status`
- Update `Latest decisions`
- Update `What changed this session`
- Update `Next recommended tasks`
- Update `Open questions`
