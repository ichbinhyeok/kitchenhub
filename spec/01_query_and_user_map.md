# 01 Query And User Map

## User states

### Open-new
- Trigger: opening or taking over a kitchen
- Questions:
  - Do I need a grease interceptor?
  - Which office controls the process?
  - What should be in place before the first inspection?

### Steady-state
- Trigger: routine operations
- Questions:
  - Am I overdue on hood cleaning?
  - Which records have to stay on site?
  - Does this city require manifests or logs for years?

### Inspection-prep
- Trigger: utility, fire, or city inspection
- Questions:
  - What will they ask to see?
  - What is missing from my files?
  - What can fail the visit?

### Post-fail
- Trigger: warning, deficiency, or reinspection
- Questions:
  - What must be fixed first?
  - Which vendor category is relevant?
  - What proof will be checked on reinspection?

### Multi-location
- Trigger: more than one restaurant in more than one jurisdiction
- Questions:
  - Which cities differ?
  - Where am I overdue?
  - How do I standardize logs and reminders?

## Query families
- `city grease trap rules`
- `restaurant grease interceptor requirements`
- `city approved grease hauler`
- `grease trap manifest requirements`
- `city hood cleaning requirements`
- `restaurant fire inspection checklist`
- `hood cleaning records`
- `hood sticker requirements`
- `failed hood inspection what now`
- `grease trap violation checklist`
- `find hood cleaner city`
- `find grease service city`

## Priority page set
- `/{city}/restaurant-grease-trap-rules`
- `/{city}/approved-grease-haulers`
- `/{city}/hood-cleaning-requirements`
- `/{city}/restaurant-fire-inspection-checklist`
- `/{city}/hood-cleaning-records`
- `/{city}/grease-interceptor-log-template`
- `/{city}/find-hood-cleaner`
- `/{city}/find-grease-service`

## Funnel logic
- Search lands on rule page or inspection-prep page.
- Page resolves applicability and proof burden.
- Verdict or checklist identifies the missing step.
- CTA routes to the matching vendor category, not a generic directory.
