# 11 Sample Data Contracts

## Goal
Give builders concrete seed shapes so they do not invent ad hoc JSON structures during the first pass.

## `city-compliance-profile.json`
```json
{
  "profileId": "austin-tx-kitchen-compliance",
  "city": "Austin",
  "state": "tx",
  "fogAuthorityId": "austin-water-pretreatment",
  "hoodAuthorityId": "austin-fire-department",
  "launchTier": 1,
  "indexable": true,
  "decisionReason": "strong official fog and inspection guidance"
}
```

## `fog-rule.json`
```json
{
  "ruleId": "austin-fog-food-service",
  "authorityId": "austin-water-pretreatment",
  "city": "Austin",
  "state": "tx",
  "foodServiceApplicability": "food service establishments with grease-bearing waste",
  "interceptorType": "grease interceptor or grease trap depending on facility design",
  "pumpOutFrequency": "operator-specific, evidence-backed maintenance required",
  "manifestRequirement": "haul manifests should be retained",
  "approvedHaulerMode": "operator_must_verify",
  "submissionMethod": "authority guidance and forms",
  "enforcementNote": "pretreatment noncompliance can trigger notices and escalation",
  "sourceRefs": ["src-austin-fog-faq"],
  "lastVerified": "2026-04-07"
}
```

## `hood-rule.json`
```json
{
  "ruleId": "portland-or-hood-cleaning",
  "authorityId": "portland-fire",
  "city": "Portland",
  "state": "or",
  "hoodType": "type-i-commercial-cooking-exhaust",
  "cleaningFrequencyBands": [
    "high-volume cooking requires more frequent cleaning",
    "follow local fire rule and inspection expectations"
  ],
  "certificateRequirement": "service documentation required",
  "serviceTagRequirement": "field sticker or tag must remain visible when required",
  "reportRetentionRule": "cleaning report retained on site",
  "suppressionInspectionRequirement": "separate suppression inspection may still apply",
  "sourceRefs": ["src-portland-itm"],
  "lastVerified": "2026-04-07"
}
```

## `provider-record.json`
```json
{
  "providerId": "example-hood-cleaning-austin",
  "providerName": "Example Hood Cleaning Austin",
  "providerType": "hood_cleaner",
  "coverageTargets": ["austin-tx-kitchen-compliance"],
  "listingMode": "sponsor_only",
  "sponsorStatus": "prospect",
  "siteUrl": "https://example.com",
  "email": "ops@example.com",
  "phone": "512-555-0100",
  "officialApprovalSourceUrl": "",
  "internalNote": "verify suppression capability before activation"
}
```

## `route-record.json`
```json
{
  "path": "/tx/austin/find-grease-service",
  "template": "find-grease-service",
  "state": "tx",
  "city": "Austin",
  "authorityId": "austin-water-pretreatment",
  "profileId": "austin-tx-kitchen-compliance",
  "canonicalPath": "/tx/austin/find-grease-service",
  "indexable": false,
  "decisionReason": "route exists because operators still need a city-aware next step even before the finder is safe to index",
  "noindexReason": "public provider coverage is still too thin for an indexed finder",
  "promotionChecklist": [
    "add at least three renderable providers",
    "show visible evidence quality on provider cards",
    "recheck source freshness before promotion"
  ],
  "promotionReviewOn": "2026-05-01",
  "lastGenerated": "2026-04-07T15:00:00+09:00"
}
```

## `lead-intake.json`
```json
{
  "leadId": "uuid",
  "capturedAt": "2026-04-07T15:00:00+09:00",
  "city": "Austin",
  "state": "tx",
  "pageFamily": "hood_requirements",
  "issueType": "hood_cleaning",
  "operatorType": "restaurant_owner",
  "authorityId": "austin-fire-department",
  "providerIntent": "find-hood-cleaner",
  "notes": "Need hood cleaning before inspection next week.",
  "routingConsent": true
}
```

## Contract law
- Seed files must stay human-editable.
- File names should map cleanly to entities, not templates.
- No template may require fields that are absent from these contracts.
