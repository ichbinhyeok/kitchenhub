# 07 Technical Architecture

## System shape
- Server-rendered launch architecture
- File-backed source of truth
- Registry-driven page generation
- No database required for launch

## Recommended package map
- `owner.kitchencompliance.data`
- `owner.kitchencompliance.model`
- `owner.kitchencompliance.rules`
- `owner.kitchencompliance.ops`
- `owner.kitchencompliance.web`
- `owner.kitchencompliance.templates`

## Rendering model
- Local rule pages
- Local checklist pages
- Local provider-routing pages
- Authority browse and detail pages
- Support guides
- Noindex operator tools
- Trust and methodology pages

## Operational services
- source freshness audit
- stale-page suppression
- provider evidence gating
- noindex promotion queue reporting
- search-demand snapshot import and review
- rule diff monitoring in later versions

## Launch storage
- JSON as the main registry
- CSV for attribution, lead intake, demand imports, and downloadable operator worksheets
- snapshots for source evidence where useful
