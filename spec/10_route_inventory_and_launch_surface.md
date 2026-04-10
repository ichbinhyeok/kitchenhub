# 10 Route Inventory And Launch Surface

## Goal
Define the exact route families to ship before implementation drifts into a broad restaurant-operations portal.

## Canonical route families

| Route pattern | Purpose | Indexing default |
|---|---|---|
| `/{state}/{city}/restaurant-grease-trap-rules` | city FOG requirements | index |
| `/{state}/{city}/approved-grease-haulers` | official list or verification workflow | index |
| `/{state}/{city}/hood-cleaning-requirements` | local hood cleaning rule | index |
| `/{state}/{city}/restaurant-fire-inspection-checklist` | inspection-prep page | index |
| `/{state}/{city}/find-grease-service` | transactional finder | index or hold by coverage |
| `/{state}/{city}/find-hood-cleaner` | transactional finder | index or hold by coverage |
| `/guides/fog-vs-grease-trap-cleaning` | evergreen support guide | index |
| `/guides/how-often-clean-commercial-hood` | evergreen support guide | index |
| `/guides/what-records-restaurant-inspections-check` | evergreen support guide | index |

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

## Page count for v1
- 5 city FOG pages
- 5 city approved-hauler pages
- 5 city hood pages
- 5 city inspection-prep pages
- 6 city provider finder pages
- 3 evergreen guides

Total:
- `29` launch pages

## Indexing law
- Do not index a city page if neither the authority nor the local workflow is source-backed.
- Provider finder pages may stay live but should not be indexed if provider coverage is still weak.

## Internal linking law
- every city FOG page links to:
  - approved hauler page
  - hood requirement page
  - inspection checklist
- every provider finder page links back to the rule page that justifies the CTA

## First implementation slice
1. city FOG page
2. city hood page
3. city inspection checklist
4. approved-hauler page
5. provider finder page
6. evergreen guides
