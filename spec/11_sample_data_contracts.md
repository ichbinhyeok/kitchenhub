# 11 Sample Data Contracts

## Goal
Give builders concrete seed shapes so they do not invent ad hoc JSON or CSV structures during the first pass.

## `authority-record.json`
```json
{
  "authorityId": "austin-fire-marshal",
  "authorityType": "fire_ahj",
  "authorityName": "Austin Fire Department Fire Marshal's Office",
  "city": "austin",
  "state": "tx",
  "baseUrl": "https://www.austintexas.gov/department/fire-marshals-office",
  "contactUrl": "https://www.austintexas.gov/services/request-fire-inspection",
  "lastVerified": "2026-04-07",
  "verificationStatus": "verified"
}
```

## `city-compliance-profile.json`
```json
{
  "profileId": "austin-tx-kitchen-compliance",
  "city": "austin",
  "state": "tx",
  "fogAuthorityId": "austin-water-pretreatment",
  "hoodAuthorityId": "austin-fire-marshal",
  "launchTier": 1,
  "indexable": true,
  "homeSummary": "Austin kitchens can check grease rules, haul tickets, hood records, and fire inspection prep in one local workflow.",
  "decisionReason": "strong official fog and inspection guidance"
}
```

## `fog-rule.json`
```json
{
  "ruleId": "austin-fog-food-service",
  "authorityId": "austin-water-pretreatment",
  "city": "austin",
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
  "city": "portland",
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
  "providerId": "hoodz-austin-hood-cleaning",
  "providerName": "HOODZ of Austin",
  "providerType": "hood_cleaner",
  "coverageTargets": ["austin-tx-kitchen-compliance"],
  "siteUrl": "https://www.hoodzinternational.com/austin/request-service/",
  "email": "jennifer.mouser@hoodz.us.com",
  "phone": "512-770-6540",
  "officialApprovalSourceUrl": "https://www.austintexas.gov/sites/default/files/files/Fire/Prevention/Maintenance-Inspection-Considerations.pdf",
  "internalNote": "HOODZ of Austin publishes Austin request-service contact details and emphasizes compliance-ready commercial kitchen cleaning support."
}
```

## `route-record.json`
```json
{
  "path": "/tx/austin/find-grease-service",
  "template": "find-grease-service",
  "state": "tx",
  "city": "austin",
  "authorityId": "austin-water-pretreatment",
  "profileId": "austin-tx-kitchen-compliance",
  "canonicalPath": "/tx/austin/find-grease-service",
  "indexable": false,
  "decisionReason": "route exists because operators still need a city-aware next step even before the finder is safe to index",
  "noindexReason": "Austin Water publishes an official hauler workflow, but no current grease-service provider card carries authority-backed evidence, so this finder stays noindex-monitored.",
  "promotionChecklist": [
    "Attach at least one authority-backed hauler or transporter evidence link to a renderable provider card.",
    "Keep three renderable providers with direct Austin contact details and visible service scope.",
    "Re-check the route after the authority-backed evidence is added and only then promote it into search."
  ],
  "promotionReviewOn": "2026-04-21",
  "lastGenerated": "2026-04-07T15:00:00+09:00"
}
```

## `search-demand-snapshot.json`
```json
{
  "routePath": "/tx/austin/find-grease-service",
  "topQuery": "austin grease service",
  "impressions28d": 184,
  "clicks28d": 9,
  "averagePosition": 11.8,
  "capturedOn": "2026-04-20",
  "note": "Useful demand signal, but route should stay held until authority-backed provider evidence exists."
}
```

## `lead-intake.csv`
```csv
captured_at,lead_type,visitor_id,city,state,page_family,issue_type,authority_id,source_path,verdict_state,provider_intent,contact_name,business_name,email,phone,coverage_note,notes,routing_consent
2026-04-07T15:00:00+09:00,operator_request,2c4f7c8e-3a56-4b39-9f01-2a0f3a17d7c1,austin,tx,provider_finder,fog_cleaning,austin-water-pretreatment,/tx/austin/find-grease-service,provider_multi,need_grease_service,Jordan Lee,Lee Street Kitchen,jordan@example.com,512-555-0100,,Need grease service before next inspection.,true
```

## Contract law
- Seed files must stay human-editable.
- File names should map cleanly to entities, not templates.
- No template may require fields that are absent from these contracts.
- Do not add removed legacy listing or paid-placement fields.
