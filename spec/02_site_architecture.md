# 02 Site Architecture

## Canonical entities
- City compliance profile
- Authority record
- FOG rule set
- Hood rule set
- Inspection-prep checklist
- Provider record
- Source record
- Search-demand snapshot
- Operator tool

## URL structure
- `/{state}/{city}/restaurant-grease-trap-rules`
- `/{state}/{city}/approved-grease-haulers`
- `/{state}/{city}/hood-cleaning-requirements`
- `/{state}/{city}/restaurant-fire-inspection-checklist`
- `/{state}/{city}/find-hood-cleaner`
- `/{state}/{city}/find-grease-service`
- `/authority/{state}/{authorityId}/{slug}`
- `/authorities`
- `/authorities/{state}/{authorityId}`
- `/guides/fog-vs-grease-trap-cleaning`
- `/guides/how-often-clean-commercial-hood`
- `/guides/what-records-restaurant-inspections-check`
- `/tools/grease-log`
- `/tools/hood-record-binder`
- `/tools/missing-proof-tracker`
- `/tools/inspection-reminder-plan`
- `/about`
- `/methodology`
- `/contact`
- `/privacy`
- `/terms`
- `/not-government-affiliated`
- `/corrections`

## Page modules
Every local rule page must include:
- authority or governance block
- applicability
- actual local requirement
- records to keep on site
- what happens if this is missing or overdue
- next action CTA
- source stack
- last verified date
- related operator tool links when useful

Every finder page must also include:
- why each provider appears
- evidence label and cited authority link when available
- service geography and scope
- verification note where approval is not official
- page-level routing request UI only after the evidence block

## Verdict modules

### FOG status
Inputs:
- city or utility
- device type
- last pump date
- manifest on file
- hauler status
- log completeness

Outputs:
- green, yellow, or red state
- why this status exists
- documents missing
- next service category or authority follow-up

### Hood status
Inputs:
- city or fire authority
- cooking profile
- last hood cleaning date
- last suppression inspection date
- sticker or certificate presence

Outputs:
- service due state
- inspection-prep readiness
- likely deficiency reason
- next service action

### Operator tool modules
Inputs:
- source route
- city
- authority
- issue type
- current missing proof or due date

Outputs:
- downloadable worksheet
- local follow-up checklist
- links back to the governing local pages
