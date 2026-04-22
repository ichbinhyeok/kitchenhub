# 03 Data And Operations

## Core entities
- `AuthorityRecord`: rule holder, contact path, verification state, and last-verified date
- `CityComplianceProfile`: city entry point, authority bindings, home summary, launch tier, and indexability
- `FogRuleRecord`: applicability, device type, pump-out rule, manifest rule, approved-hauler mode, submission method, enforcement note
- `HoodRuleRecord`: cadence bands, certificate and tag requirements, report retention rule, suppression inspection requirement
- `InspectionPrepRecord`: on-site checklist, common fail reasons, reschedule path, escalation note
- `ProviderRecord`: provider type, coverage targets, direct contact details, authority-backed evidence link when present, internal ops note
- `RouteRecord`: page template, canonical path, indexability, noindex reason, promotion checklist, review date
- `SourceRecord`: scope, source tier, quote summary, verifiedOn, nextReviewOn
- `SearchDemandSnapshotRecord`: route-level demand snapshot imported for promotion review

## Source hierarchy
- Tier 1: ordinance, code, official department rule page
- Tier 2: official permit application, PDF guide, department manual
- Tier 3: official checklist, FAQ, or notice
- Tier 4: provider or trade-association summary only as a backup explainer, never as canonical evidence

## Freshness policy
- forms, fees, approved lists, and application steps: 30 to 90 days
- active checklists and FAQ pages: 90 to 180 days
- broader principles and evergreen rule summaries: up to 365 days

## Launch data workflow
1. Select candidate city or authority.
2. Verify public source coverage and actual rule ownership.
3. Normalize rules, providers, and route decisions into registry records.
4. Decide whether official-list logic, public-provider evidence, or operator-verification logic applies.
5. Publish or hold the route based on freshness, evidence visibility, and indexing policy.
6. Feed noindex review and search-demand snapshots back into the promotion queue.
