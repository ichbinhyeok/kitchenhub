# 03 Data And Operations

## Core entities
- `Jurisdiction`: state, city, county, governing utility, governing fire authority, launch status, last verified date
- `FOGRule`: applicability, required device type, registration requirement, plan check flag, cleaning rule, manifest or recordkeeping rule, approved-hauler requirement, fees
- `HoodRule`: cadence by cooking profile, suppression notes, decal or sticker rule, certificate rule, deficiency notes
- `InspectionRule`: checklist items, on-site document list, common failure triggers, reinspection path
- `VendorRegistry`: city, vendor category, approved-list source if present, self-verification steps if no list exists, sponsor eligibility
- `SourceRecord`: source tier, title, agency, url, quote summary, verified_on, next_review_on, freshness class

## Source hierarchy
- Tier 1: ordinance, code, official department rule page
- Tier 2: official permit application, PDF guide, department manual
- Tier 3: official checklist, FAQ, or notice
- Tier 4: vendor or association summary only as a backup explainer

## Freshness policy
- forms, fees, approved lists, and application steps: 30 to 90 days
- active checklists and FAQ pages: 90 to 180 days
- broader principles and evergreen rule summaries: up to 365 days

## Launch data workflow
1. Select candidate city or authority.
2. Verify public source coverage.
3. Normalize rule and checklist data into registry records.
4. Decide whether approved-list logic or self-verification logic applies.
5. Publish page only if source and CTA rules are satisfied.
