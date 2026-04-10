# 12 Acceptance Test Matrix

## Goal
Define launch-done behavior before code is written.

## Product acceptance

### Home and navigation
- home explains `FOG + hood + inspection prep` clearly
- site does not read like a generic restaurant blog
- city and guide routes are discoverable from home

### City FOG page
- shows local rule holder
- states what must be maintained
- states whether an approved hauler list exists
- links to source-backed next action

### City hood page
- shows cleaning requirement
- shows what proof must stay on site
- separates hood cleaning from suppression inspection when they differ

### Inspection checklist page
- lists on-site proof and common failure reasons
- points to the correct local authority
- contains a transactional CTA only after the rule summary

### Provider finder page
- never implies official approval unless the authority publishes it
- routes to public providers only, or clearly labeled sponsor mode
- stays noindex if coverage is weak

## Trust acceptance
- every indexed local page has source stack and last-verified date
- sponsor copy is visibly separate from official requirement text
- no claim of approval without an official list

## Technical acceptance
- canonical, robots, sitemap, and structured data render correctly
- city pages are SSR and linkable without JavaScript
- no raw admin or ops route is indexable

## Data acceptance
- entity fields in `spec/09` are sufficient to render launch pages
- seed JSON matches contracts in `spec/11`
- launch route inventory matches `spec/10`

## Commercial acceptance
- CTA placement follows the rule summary, not the other way around
- lead attribution preserves city, page family, and issue type
- provider routing never exceeds the consent model documented publicly
