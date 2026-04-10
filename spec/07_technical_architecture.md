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
- Local vendor-routing pages
- Support guides
- Optional noindex operator tools

## Operational services
- source freshness audit
- stale-page suppression
- sponsor inventory gating
- rule diff monitoring in later versions

## Launch storage
- JSON as the main registry
- CSV for imports, exports, and sponsor ops
- snapshots for source evidence where useful
