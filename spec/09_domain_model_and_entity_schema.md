# 09 Domain Model And Entity Schema

## Goal
Turn the product packet into an implementation-ready model so multiple agents can build against the same nouns.

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
  - `suppression_vendor`
- `coverageTargets[]`
- `listingMode`
  - `public`
  - `sponsor_only`
- `sponsorStatus`
  - `prospect`
  - `active`
  - `hold`
- `siteUrl`
- `email`
- `phone`
- `officialApprovalSourceUrl`
- `internalNote`

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

## Launch-safe enums
- `pageFamily`
  - `fog_rules`
  - `approved_haulers`
  - `hood_requirements`
  - `inspection_checklist`
  - `provider_finder`
- `issueType`
  - `fog_cleaning`
  - `manifest_or_log`
  - `hood_cleaning`
  - `inspection_prep`
  - `vendor_search`

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
