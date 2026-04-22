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
  - Which service category, record fix, or authority follow-up is relevant?
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
- `hood sticker requirements`
- `failed hood inspection what now`
- `grease trap violation checklist`
- `find hood cleaner city`
- `find grease service city`
- `grease log template`
- `hood record binder`
- `missing proof tracker`
- `inspection reminder plan`

## Priority page set
- `/{state}/{city}/restaurant-grease-trap-rules`
- `/{state}/{city}/approved-grease-haulers`
- `/{state}/{city}/hood-cleaning-requirements`
- `/{state}/{city}/restaurant-fire-inspection-checklist`
- `/{state}/{city}/find-hood-cleaner`
- `/{state}/{city}/find-grease-service`
- `/guides/fog-vs-grease-trap-cleaning`
- `/guides/how-often-clean-commercial-hood`
- `/guides/what-records-restaurant-inspections-check`
- `/tools/grease-log`
- `/tools/hood-record-binder`
- `/tools/missing-proof-tracker`
- `/tools/inspection-reminder-plan`

## Funnel logic
- Search lands on a rule page, inspection-prep page, guide, or operator tool.
- Page resolves applicability and proof burden.
- Verdict or checklist identifies the missing step.
- Next action routes to the right authority contact path, operator tool, public provider finder, or page-level routing request.
- Provider routing stays secondary to rule clarity and must never behave like a generic directory.
