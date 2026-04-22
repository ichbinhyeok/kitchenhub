# 06 Indexing Quality And Analytics

## Indexing rules
- Index only pages with authority-backed local rule coverage.
- Keep weak routes in a monitored `noindex` promotion queue instead of forgetting them.
- Every `noindex` route should carry a reason, a promotion checklist, and a next review date.
- See `spec/14_seo_indexing_strategy.md` for the canonical local SEO model, internal linking policy, and organic priority plan.
- Noindex cities that only have weak provider coverage and no real rule clarity.
- Noindex transactional pages if rule context and provider evidence are too thin.
- Do not publish broad city swaps that do not materially change the answer.

## Quality gates
- At least one Tier 1 or Tier 2 local source
- A concrete next-action block
- A visible source stack
- Last-verified date
- Provider routing clearly separated from official guidance

## Kill rules
- Approved list removed or unpublished with no replacement evidence
- Fire or utility page turns stale beyond the configured review window
- Provider coverage exists but cannot be safely described
- Rule page loses its core applicability block

## Promotion rules
- `noindex` does not mean abandoned; it means the route is held in a monitored promotion queue.
- A route can be promoted only after it clears source quality, freshness, and route-type-specific trust gates.
- Finder routes should not graduate from `noindex` until provider evidence quality is visible and public coverage is genuinely useful.
- `/admin` and scheduled ops snapshots should make the promotion queue visible so agents can revisit it without relying on memory.

## Analytics plan
- Track event type
- Track page family
- Track city and authority
- Track verdict output state
- Track CTA type and provider intent
- Track tool slug for operator-tool usage
- Measure revisit rate for operator utility pages

## Core KPIs
- Organic clicks to local rule pages
- CTA click-through rate by page family
- Lead rate by provider intent
- Returning users for reminder or log surfaces
- Promotion rate from `noindex-monitored` to `index`
