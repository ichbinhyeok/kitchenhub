# 15 Authority-First Route Design

## Problem
- A city is often the operator's mental entry point.
- A city is not always the true rule holder.
- In mixed-governance markets, the governing body may be a utility, county department, fire marshal, or other local authority.

## Product rule
- Entry can stay city-first for usability.
- Canonical logic must stay authority-first whenever the authority is the real decision maker.
- The route, summary, proof burden, and next action should follow the authority that can enforce the requirement.

## Why this matters
- Operators lose trust when a city page hides that the real requirement comes from another body.
- Search pages become weak when multiple city pages say almost the same thing and bury the governing authority.
- Finder pages become dangerous when vendor routing appears before the actual rule holder is made explicit.

## Current launch stance
- Keep current city-shaped URLs for the launch slice.
- Make the authority name, authority type, and source stack explicit above the fold.
- Use methodology and trust pages to explain that city display is not the same thing as rule ownership.
- Do not claim that a city is the sole authority when the real workflow belongs to a utility, county, or fire authority.

## Canonical decision rules

### City-first is acceptable when
- The city is the clear rule holder.
- The city page still makes the governing authority explicit.
- The page can answer the local rule question without flattening mixed governance.

### Authority-first should outrank city framing when
- The governing workflow belongs to a utility or county program.
- Fire inspection or hood-system enforcement sits with a fire marshal or AHJ rather than the city brand itself.
- The operator's next action depends on contacting or verifying a non-city authority.

## UX rules
- First screen must show:
  - authority name
  - authority type
  - proof on site
  - fail conditions
  - next action
- City name can orient the user, but it should not hide the authority.
- When governance is mixed, explain that explicitly instead of pretending the city page alone resolves it.

## Future routing options

### Option A: city URL, authority-led body
- Keep routes like `/{state}/{city}/{slug}`.
- Use stronger authority UI and copy.
- Best for launch continuity.

### Option B: city URL plus authority aliases
- Keep city pages as operator-facing entry points.
- Add alternate authority paths or redirects for markets where the authority is the query target.
- Best when search demand starts appearing around utility or county names.

### Option C: authority-canonical in mixed markets
- Promote utility or authority pages to canonical status when the city page becomes mostly a wrapper.
- Use the city page as a navigational layer, not the canonical answer.
- Best when many cities share the same non-city authority logic.

## Promotion gates for authority-canonical experiments
- The authority has enough unique rule content to justify its own route.
- Multiple city pages would otherwise be near-duplicates.
- The page can still answer operator questions with local applicability, proof burden, and next action.
- Internal linking remains clear from city entry pages.

## Operational notes
- Treat authority-first design as a trust feature, not just an SEO refactor.
- Do not migrate URL shape until search, ops, and content maintenance benefits are clear.
- Revisit this document whenever mixed-governance cities expand beyond the current launch slice.
