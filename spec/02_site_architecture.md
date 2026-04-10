# 02 Site Architecture

## Canonical entities
- Jurisdiction
- Utility or wastewater authority
- Fire authority
- FOG rule set
- Hood rule set
- Inspection-prep checklist
- Vendor category

## URL structure
- `/{state}/{city}/restaurant-grease-trap-rules`
- `/{state}/{city}/approved-grease-haulers`
- `/{state}/{city}/hood-cleaning-requirements`
- `/{state}/{city}/restaurant-fire-inspection-checklist`
- `/{state}/{city}/hood-cleaning-records`
- `/{state}/{city}/grease-interceptor-log-template`
- `/{state}/{city}/find-hood-cleaner`
- `/{state}/{city}/find-grease-service`
- `/guides/how-to-prepare-for-a-restaurant-fire-inspection`
- `/guides/grease-manifest-vs-cleaning-log`
- `/guides/how-approved-hauler-lists-work`

## Page modules
Every local rule page must include:
- applicability
- actual local requirement
- records to keep on site
- what happens if this is missing or overdue
- next action CTA
- source stack
- last verified date

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
- next vendor category

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
