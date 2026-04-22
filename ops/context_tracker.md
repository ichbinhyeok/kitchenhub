# Context Tracker

## Current status
- KitchenRuleHub is now documented as a B2C-only operator product.
- A Spring Boot MVC implementation exists, and the last recorded clean verification run was `.\mvnw.cmd test`.
- The active wedge remains local FOG, hood, and inspection-prep guidance for anxious operators, not broad restaurant compliance.
- Austin, Charlotte, and Miami remain the primary weekly review set; the rest of the live cities stay maintained for freshness, source quality, and trust.
- Authority-backed local rule pages are the SEO core. Provider finder pages and tools can support next actions, but they must stay clearly separate from official guidance.
- Freshness, source depth, provider evidence quality, and held-route promotion checks are the main release gates for indexed pages.
- `ops/source_audit_2026-04-07.md`, `ops/wedge_focus_2026-04-12.md`, and the route-review docs are the current operating sources of truth inside `ops/`.

## Latest decisions
- Package root stays `owner.kitchencompliance`.
- This repository must stay B2C-only. Do not add mixed-audience or partner-marketplace workflows here.
- Any future provider-sales product or sales motion must live on a separate domain and outside this codebase.
- Canonical entities are local enforcement authorities and cities, not generic restaurant categories.
- Launch scope remains FOG control, hood cleaning cadence and records, suppression and recordkeeping, and inspection prep.
- Home should stay issue-first for anxious operators, while canonical indexed pages stay city-first or authority-first depending on governance.
- Every local verdict page should follow the same operator order: rule holder, proof on site, fail conditions, then next action.
- Approved-list pages require official list evidence. Otherwise the product must fall back to self-verification guidance.
- Weak or incomplete routes should move into a monitored hold or noindex promotion queue, not disappear into an unowned backlog.
- Provider pages may help an operator choose a next step, but they must not read like endorsements, paid placements, or an official directory.
- Weekly review should focus first on the Austin, Charlotte, and Miami Tier A rule pages, then on Tier B support routes that help operators complete the next action safely.

## What changed this session
- Rewrote `AGENT_START_HERE.md` and the live ops docs so they now describe KitchenRuleHub as a B2C-only operator product.
- Removed legacy commercial beta guidance from the current handoff docs and route-review docs.
- Recast the route-promotion model around source safety, search demand, provider evidence quality, and operator next-action clarity instead of sales proof.
- Deleted obsolete separate-domain sales docs for commercial outreach and partner-product planning.

## Next recommended tasks
- Keep source dates, authority ownership, and provider evidence current; indexed routes should continue to fail closed when freshness or proof drops below the bar.
- Use `ops/route_trigger_tracker_2026-04-12.csv` as the weekly route-review queue and only promote held routes when the trigger doc and route checklist both pass.
- Treat the Austin, Charlotte, and Miami Tier A rule pages as the first review surface for Search Console, snippet rewrites, and internal-link decisions.
- Continue tightening provider ranking with city-specific coverage, direct local evidence, and freshness instead of generic listing volume.
- Audit out-of-scope strategy and spec docs later if ownership allows; legacy commercial references may still remain outside `AGENT_START_HERE.md` and `ops/`.
- Recheck authority-first canonical adoption in Search Console where mixed-governance city URLs still outrank authority aliases.

## Open questions
- Which city cluster should receive the next B2C expansion after Austin, Charlotte, and Miami stabilize?
- Should authority-level pages become more prominent in navigation for mixed-governance markets?
- What is the minimum provider-count and evidence bar for keeping a finder route indexed over time?
- How much operator tooling should stay fully public versus remain noindex support surfaces?

## Source notes
- Original ideation source: legacy Sharp v1-v3 note stored in the author's OneDrive project folder.
