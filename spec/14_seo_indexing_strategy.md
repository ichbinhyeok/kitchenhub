# 14 SEO Indexing Strategy

## Goal
- Use organic search as acquisition without turning the product into thin programmatic SEO.
- Keep canonical local rule pages as the SEO core.
- Preserve trust by keeping weak finder and routing pages in monitored states instead of forcing indexation.

## SEO thesis
- Search should bring operators into authoritative local rule pages first.
- Guides should support understanding and route users into local canonical pages.
- Finder pages should be indexable only when coverage, source quality, and evidence visibility are strong enough to help an operator.
- Freshness is part of search quality because stale local guidance creates real operational risk.

## Page hierarchy

### SEO core
- Canonical local rule pages are the primary indexable assets.
- These pages answer the local question directly:
  - what the authority requires
  - what proof must stay on site
  - what fails inspection
  - what the next action is
- Local rule pages should be the primary destination for:
  - city FOG rules
  - hood requirements
  - inspection-prep pages
  - approved hauler or verification pages when the authority has a real local workflow

### Support layer
- Evergreen guides are support pages, not the canonical answer.
- Their job is to translate broad search intent into a local rule page or operator tool.
- Guides should never outrank the city or authority page for a local query.

### Provider-routing layer
- Finder pages are operator-support pages.
- They can be indexable when trust is strong.
- They should never replace the authority summary or read like a provider directory first.

### Utility layer
- Noindex operator tools are retention assets.
- They should support repeat use and should link back to canonical local pages.

## Internal linking policy

### From home
- Home should point into the issue state first:
  - grease / manifests
  - hood / suppression / tags
  - inspection prep / records
- Home should then branch into city-aware local pages, not into a generic directory.

### From guides
- Each evergreen guide should point to at least one local canonical page and one operator tool.
- Each guide should answer a broad question, then hand the user to a city or authority page.
- Guides should not become dead ends.

### From canonical local pages
- FOG pages should link to approved hauler or verification workflow pages and the inspection-prep page.
- Hood pages should link to the inspection checklist and, when relevant, the hood cleaner finder.
- Inspection pages should link back to the relevant hood or FOG page for missing proof.
- Finder pages should link back to the rule page that justifies the booking decision.

### From operator tools
- Tools should link back to the local page family they support.
- Tools should be framed as reminders, binders, or logs, not as standalone SEO destinations.

## Indexing states

### `index`
- Use when the page is local, authority-aware, and trust-safe.
- The page should have:
  - visible authority context
  - visible proof-on-site
  - clear fail conditions
  - a real next action
  - fresh source coverage

### `noindex-monitored`
- Use when the route is intentionally held back but should remain visible in ops tools.
- The route should carry:
  - a reason it is not indexable yet
  - a promotion checklist
  - a review date
  - a gate that explains what would need to change
- This is the default holding state for weak finder pages and early routing pages.

### `blocked`
- Use when the route is not safe to work on yet.
- Blocked means the page lacks the source or policy basis required to become useful.
- Typical blocked causes:
  - missing authority source
  - stale source stack
  - no trustworthy local rule answer
  - routing pressure exceeding available evidence

## Finder indexing thresholds
- Finder pages may be indexed only when all of the following are true:
  - local rule context is strong
  - provider coverage is meaningfully useful to an operator
  - evidence quality is visible on the card
  - provider evidence and routing UI are visually separate from authority content
  - the page resolves a real next action, not just a list of names
  - official-list grease workflows have at least one authority-backed provider card, not only public contact listings
- Practical threshold:
  - at least 3 renderable providers
  - at least 1 provider with authority-backed evidence when the local market supports it
  - no obvious false endorsement risk
  - no stale local source driving the page decision
- If coverage is thin, keep the route `noindex-monitored`.
- If the route is still public-contact-only in a workflow that needs stronger evidence, keep it `noindex-monitored`.

## Freshness discipline
- Freshness is a search quality requirement, not a bookkeeping detail.
- Every indexable local page should carry:
  - `verifiedOn`
  - `nextReviewOn`
  - a clear source stack
- If any required source falls outside the review window, treat the route as stale and hold or remove it from indexable status.
- Freshness review should happen before the route is published, not after the page gets stale.
- For finder pages, freshness must cover both the authority source and the provider evidence that justifies the page.

## Organic acquisition model
- Organic acquisition should prioritize high-intent local pages.
- Support pages should exist to widen intent capture, but they should not become thin content farms.
- The best SEO loop is:
  - guide or query lands on site
  - user is routed to canonical local page
  - local page explains the rule and proof
  - next action routes to tool, checklist, or verified provider
  - returning operator uses tools again

## 90-day organic priority model

### Days 1-30
- Lock the canonical local page structure.
- Keep titles, H1s, summaries, and source stacks aligned with local authority language.
- Make sure every indexable local page has a clear local answer, proof-on-site block, and next action.
- Keep weak routes in `noindex-monitored` rather than forcing indexation.
- Make sure guides link into local pages, not around them.

### Days 31-60
- Tighten internal linking across home, guides, local pages, and tools.
- Improve title and summary phrasing around the highest-intent local queries.
- Review finder pages for trust, coverage, and evidence quality.
- Promote only the routes that clear the operational gate.
- Expand or revise noindex checklists for pages that are still waiting on evidence.

### Days 61-90
- Revisit the pages that earned the most clicks and the ones that failed to convert.
- Refresh stale source stacks before they age out.
- Promote the strongest finder pages and keep the weaker ones monitored.
- Prune or hold pages that still cannot justify indexation.
- Add more repeat-use utility where operators keep returning under inspection pressure.

## Operational rules
- Never create a page just to satisfy a keyword if it cannot answer the local authority question.
- Never let a guide outrank a canonical local rule page for the same intent.
- Never promote a finder page without evidence quality that operators can see.
- Never leave a noindex route without a reason and a review date.
- Never let freshness drift become a hidden tax on search trust.
- Never layer future provider-sales or mixed-audience acquisition pages into this repository's SEO model.

## Measurement
- Track organic clicks by page family.
- Track whether organic entry lands on canonical local pages or on support pages.
- Track indexable vs `noindex-monitored` counts over time.
- Track finder promotion rate and the number of routes still waiting on evidence.
- Track returning utility usage from operators under inspection pressure.

## Acceptable outcome
- Organic search drives operators into local authority pages.
- Guides widen discovery but do not dilute trust.
- Weak pages are held, not forgotten.
- Finder pages earn indexation instead of assuming it.
- The site grows without losing its compliance-first identity.
