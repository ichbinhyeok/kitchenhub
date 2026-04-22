# 09 Domain Model And Entity Schema

## Goal
Turn the product packet into an implementation-ready model so multiple agents can build against the same nouns and persisted contracts.

## Package map
- `owner.kitchencompliance.data` - file-backed records and loaders
- `owner.kitchencompliance.model` - page-facing view models and decision outputs
- `owner.kitchencompliance.rules` - compliance decision logic
- `owner.kitchencompliance.ops` - freshness, verification, and review workflows
- `owner.kitchencompliance.web` - controllers and route binding

## Core entities

### `AuthorityRecord`
- `authorityId`
- `authorityType`
  - `utility`
  - `fire_ahj`
  - `city_department`
- `authorityName`
- `city`
- `state`
- `baseUrl`
- `contactUrl`
- `lastVerified`
- `verificationStatus`

Why it exists:
- A city can have more than one rule holder. FOG and hood rules may come from different authorities.

### `CityComplianceProfile`
- `profileId`
- `city`
- `state`
- `fogAuthorityId`
- `hoodAuthorityId`
- `launchTier`
- `indexable`
- `homeSummary`
- `decisionReason`

Why it exists:
- Canonical city page unit that binds the city to the right rule holders.

### `FogRuleRecord`
- `ruleId`
- `authorityId`
- `city`
- `state`
- `foodServiceApplicability`
- `interceptorType`
- `pumpOutFrequency`
- `manifestRequirement`
- `approvedHaulerMode`
  - `official_list`
  - `operator_must_verify`
  - `unclear`
- `submissionMethod`
- `enforcementNote`
- `sourceRefs[]`
- `lastVerified`

### `HoodRuleRecord`
- `ruleId`
- `authorityId`
- `city`
- `state`
- `hoodType`
- `cleaningFrequencyBands[]`
- `certificateRequirement`
- `serviceTagRequirement`
- `reportRetentionRule`
- `suppressionInspectionRequirement`
- `sourceRefs[]`
- `lastVerified`

### `InspectionPrepRecord`
- `recordId`
- `city`
- `state`
- `inspectionType`
  - `fog`
  - `fire`
  - `hood`
- `whatMustBeOnSite[]`
- `commonFailureReasons[]`
- `rescheduleMethod`
- `penaltyOrEscalation`
- `sourceRefs[]`

### `ProviderRecord`
- `providerId`
- `providerName`
- `providerType`
  - `grease_hauler`
  - `grease_trap_service`
  - `hood_cleaner`
  - `suppression_service`
- `coverageTargets[]`
- `siteUrl`
- `email`
- `phone`
- `officialApprovalSourceUrl`
- `internalNote`

Why it exists:
- Public provider evidence is part of the operator next-action layer, but providers are never first-party paid placements in this repo.

### `RouteRecord`
- `path`
- `template`
- `state`
- `city`
- `authorityId`
- `profileId`
- `canonicalPath`
- `indexable`
- `decisionReason`
- `noindexReason`
- `promotionChecklist[]`
- `promotionReviewOn`
- `lastGenerated`

### `SourceRecord`
- `sourceId`
- `scopeType`
  - `authority`
  - `fog_rule`
  - `hood_rule`
  - `inspection_prep`
- `scopeKey`
- `sourceTier`
- `agency`
- `title`
- `sourceUrl`
- `quoteSummary`
- `verifiedOn`
- `nextReviewOn`

### `SearchDemandSnapshotRecord`
- `routePath`
- `topQuery`
- `impressions28d`
- `clicks28d`
- `averagePosition`
- `capturedOn`
- `note`

### `LeadIntakeRecord`
- `leadId`
- `capturedAt`
- `city`
- `state`
- `pageFamily`
- `issueType`
- `operatorType`
- `authorityId`
- `providerIntent`
- `notes`
- `routingConsent`

Note:
- This typed data record exists in the codebase, but the launch runtime does not persist it directly.

### `LeadCaptureEventRow`
- `captured_at`
- `lead_type`
- `visitor_id`
- `city`
- `state`
- `page_family`
- `issue_type`
- `authority_id`
- `source_path`
- `verdict_state`
- `provider_intent`
- `contact_name`
- `business_name`
- `email`
- `phone`
- `coverage_note`
- `notes`
- `routing_consent`

Why it exists:
- This is the actual launch persistence contract used by `lead-intake.csv` and the `/admin` lead exports.

## Launch-safe enums
- `pageFamily`
  - `fog_rules`
  - `approved_haulers`
  - `hood_requirements`
  - `inspection_checklist`
  - `provider_finder`
  - `operator_tool`
- `issueType`
  - `fog_cleaning`
  - `manifest_or_log`
  - `hood_cleaning`
  - `inspection_prep`
  - `provider_search`
  - `operator_utility`

## Decision outputs

### `CityVerdict`
- `city`
- `state`
- `authoritySummary`
- `whatAppliesNow[]`
- `whatToKeepOnSite[]`
- `whatFailsInspections[]`
- `nextActions[]`
- `ctaType`

### `ProviderRoutingDecision`
- `leadId`
- `providerIds[]`
- `routingMode`
  - `single`
  - `multi`
  - `manual_only`
- `reason`

## Build rule
- Do not let templates invent fields.
- Every rendered page must be traceable to one or more records above.
- Do not reintroduce removed listing or paid-placement-only fields that are absent from the live runtime.
