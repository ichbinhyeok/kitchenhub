# Agent Start Here

## Project
KitchenComplianceHub

## Package root
`owner.kitchencompliance`

## What this folder contains
- A self-contained product and implementation spec for a local-rule commercial kitchen compliance site.
- Enough context for a new agent to resume work without chat history.

## Read order
1. `ops/context_tracker.md`
2. `README.md`
3. `spec/00_strategy.md`
4. `spec/01_query_and_user_map.md`
5. `spec/02_site_architecture.md`
6. `spec/03_data_and_operations.md`
7. `spec/04_commercial_model.md`
8. `spec/05_editorial_rules_and_execution.md`
9. `spec/06_indexing_quality_and_analytics.md`
10. `spec/07_technical_architecture.md`
11. `spec/08_delivery_and_handoff.md`
12. `spec/09_domain_model_and_entity_schema.md`
13. `spec/10_route_inventory_and_launch_surface.md`
14. `spec/11_sample_data_contracts.md`
15. `spec/12_acceptance_test_matrix.md`
16. `spec/13_product_principles_and_page_checklists.md`
17. `spec/14_seo_indexing_strategy.md`
18. `spec/15_authority_first_route_design.md`

## Rules
- Launch scope is FOG plus hood cleaning plus inspection prep only.
- This is not a generic restaurant compliance portal.
- Utility, city, and fire-AHJ differences must be preserved, not flattened.
- Do not publish city pages without authority-backed source coverage.
- Never imply a vendor is officially approved unless the authority publishes that list.
- Where no approved list exists, show self-verification steps instead of inventing certainty.
- Every local page must carry a source stack and a last-verified date.
- Noindex routes must stay visible in the promotion queue with a reason, checklist, and next review date.
- Update `ops/context_tracker.md` after meaningful changes.

## Minimum handoff standard
- Update `Current status`
- Update `Latest decisions`
- Update `What changed this session`
- Update `Next recommended tasks`
- Update `Open questions`
