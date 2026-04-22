# 10 Route Inventory And Launch Surface

## Goal
Define the current public route families so the spec packet stays aligned with the shipped authority-first product.

## Local route families

| Route pattern | Purpose | Indexing default |
|---|---|---|
| `/{state}/{city}/restaurant-grease-trap-rules` | city entry for FOG requirements | index |
| `/{state}/{city}/approved-grease-haulers` | official list or verification workflow | index or hold by evidence |
| `/{state}/{city}/hood-cleaning-requirements` | local hood cleaning and paperwork rule | index |
| `/{state}/{city}/restaurant-fire-inspection-checklist` | inspection-prep page | index |
| `/{state}/{city}/find-grease-service` | public provider finder | index or `noindex-monitored` |
| `/{state}/{city}/find-hood-cleaner` | public provider finder | index or `noindex-monitored` |

## Authority and support route families

| Route pattern | Purpose | Indexing default |
|---|---|---|
| `/authority/{state}/{authorityId}/{slug}` | authority-canonical alias for local routes | mirrors underlying local route |
| `/authorities` | browse all known rule holders | index |
| `/authorities/{state}/{authorityId}` | authority detail plus linked workflows | index |
| `/guides/fog-vs-grease-trap-cleaning` | evergreen support guide | index |
| `/guides/how-often-clean-commercial-hood` | evergreen support guide | index |
| `/guides/what-records-restaurant-inspections-check` | evergreen support guide | index |
| `/tools/grease-log` | operator worksheet page | noindex |
| `/tools/hood-record-binder` | operator worksheet page | noindex |
| `/tools/missing-proof-tracker` | operator worksheet page | noindex |
| `/tools/inspection-reminder-plan` | operator worksheet page | noindex |
| `/about`, `/methodology`, `/contact`, `/privacy`, `/terms`, `/not-government-affiliated`, `/corrections` | trust and policy pages | route-specific robots |

## Launch city cohort
Use cities where official FOG or hood guidance is explicit enough to support trust-first pages.

### Tier 1 launch candidates
- Santa Clara, CA
- Austin, TX
- Tampa, FL
- Portland, OR
- Charlotte, NC

### Tier 2 after first proof
- Nashville, TN
- Grand Island, NE
- Miami, FL

## Current seeded surface
- 48 local route records across 8 cities
- 8 city FOG pages
- 8 city approved-hauler or verification pages
- 8 city hood pages
- 8 city inspection-prep pages
- 8 grease-service finders
- 8 hood-cleaner finders
- 3 evergreen guides
- 4 operator tools
- 7 trust and policy pages
- 1 authority directory
- 17 authority detail pages
- authority alias mirrors of the local route set

Named public pages before alias expansion:
- `80` pages plus home

## Indexing law
- Do not index a city page if neither the authority nor the local workflow is source-backed.
- Provider finder pages may stay live but should not be indexed if provider coverage or evidence quality is still weak.

## Internal linking law
- every city FOG page links to:
  - approved hauler page
  - hood requirement page
  - inspection checklist
  - at least one relevant operator tool
- every provider finder page links back to the rule page that justifies the CTA
- every guide and operator tool links back into canonical local pages or authority pages

## Expansion rule
1. Add cities as six-route packs, not as isolated pages.
2. Add authority records, trust pages, and tools only when they deepen the operator workflow.
3. Keep future provider-sales surfaces out of this route inventory.
