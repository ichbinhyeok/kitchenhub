# Route Promotion Triggers - 2026-04-12

## Why this exists

KitchenRuleHub now has enough live routes that promotion, SEO work, and sponsor focus cannot stay in memory.

This document is the operating rule:

- every route lives in one stage
- every stage has a small set of valid next actions
- promotion only happens when a trigger is true and the trust checklist is green
- the tracker CSV is the weekly source of truth

## Stage model

- `S0_BUILD`
  - The route exists on paper or in draft only.
  - Do not spend SEO or sponsor time here yet.

- `S1_WATCH`
  - The route is live, source-backed, and safe to keep live.
  - The job is not expansion. The job is demand observation and freshness control.

- `S2_OPTIMIZE`
  - The route has a real signal.
  - Improve title, intro, internal links, snippet clarity, and CTA fit.

- `S3_PROMOTE_READY`
  - A held route now passes its promotion checklist.
  - It can move from `noindex-monitored` to `indexable` in the next shipping cycle.

- `S4_MONETIZE`
  - The route has enough proof to support sponsor packets, sponsor screenshots, or recurring commercial review.
  - This does not mean the whole site is ready. It means this route is worth selling against.

- `HOLD`
  - Do not promote.
  - A source, evidence, freshness, or trust blocker is still active.

## Hard triggers

### Move into `S1_WATCH`

All of the following must be true:

- route is live
- source stack is present
- freshness is green
- verdict language is still source-safe

### Move from `S1_WATCH` to `S2_OPTIMIZE`

Move when any one is true:

- `28d impressions >= 50`
- `28d clicks >= 5`
- average position is between `8` and `20`
- provider outbound clicks are non-zero
- sponsor-facing route is in a city or category already named in the active beta plan

### Move from `S2_OPTIMIZE` to `S4_MONETIZE`

Move when any one is true:

- `28d clicks >= 10`
- provider outbound clicks are repeatable over the last `28d`
- sponsor inquiry count is non-zero
- the route is already listed as a primary or backup proof route in the sponsor beta plan

### Move from `HOLD` to `S3_PROMOTE_READY`

All of the following must be true:

- every route-level `promotionChecklist` item is complete
- freshness is green
- at least `3` renderable providers remain visible
- at least `1` provider card carries authority-backed local evidence if the route depends on official hauler or transporter workflow
- the page still reads as a trust-safe workflow page, not a public-contact-only filler page

### Move any route back to `HOLD`

Move immediately when any one is true:

- freshness gate fails
- authority owner or cadence becomes uncertain
- provider cards fall below the visible evidence bar
- the page starts reading like weak directory filler
- the local rule claim is stronger than the evidence can support

## Default action by stage

- `S0_BUILD`
  - finish sources
  - finish verdict copy
  - set next review dates

- `S1_WATCH`
  - do not rewrite by instinct
  - only refresh sources, watch demand, and keep CTA taxonomy clean

- `S2_OPTIMIZE`
  - tighten title
  - tighten hero intro
  - add or fix internal links from guide and city entry surfaces
  - clarify next action copy

- `S3_PROMOTE_READY`
  - flip route metadata from hold to indexable
  - ship
  - verify sitemap, canonical, and robots behavior
  - move the row back to `S1_WATCH` after release

- `S4_MONETIZE`
  - capture screenshots
  - review click and inquiry proof
  - update sponsor packet inputs
  - keep SEO work narrow and route-specific

- `HOLD`
  - fix the blocker only
  - do not spend time polishing unrelated copy

## Weekly review loop

### Monday

1. Open `/admin`.
2. Review demand snapshots, noindex promotion queue, freshness watch, and lead or click summaries.
3. Open `ops/route_trigger_tracker_2026-04-12.csv`.
4. Sort by `next_review_on`, then by `commercial_priority`.

### During the review

For each due row, choose exactly one next action:

- `refresh_sources`
- `rewrite_title_intro`
- `add_internal_links`
- `upgrade_provider_evidence`
- `promote_to_index`
- `prepare_sponsor_proof`
- `hold`

Then update:

- `stage`
- `next_action`
- `next_review_on`
- `notes`

### Friday

- review only `S4_MONETIZE` rows for sponsor proof and route screenshots
- review only `HOLD` rows for blocker removal progress
- do not create new route work until the current week's due rows are updated

## Current standing exceptions

- Austin grease service stays in `HOLD` for search promotion until at least one provider card carries authority-backed evidence, even though the rule cluster is already viable for sponsor proof.
- Miami grease service stays in `HOLD` for the same reason.
- Miami hood cleaner stays in `HOLD` until one provider card carries stronger local evidence than a generic public contact page.
- Grand Island hood routes should stay permit and inspection-led in tone. Do not force cadence-heavy claims there.

## What this replaces

This replaces:

- memory-based promotion decisions
- ad hoc "maybe optimize this page" lists
- vague weekly planning without route-level dates

If a route is not in the tracker, it is not in the weekly operating loop.
