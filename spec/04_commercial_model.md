# 04 Operator Routing And Retention Model

## Current product stance
- This repo serves the operator-facing B2C product.
- The product helps commercial kitchen operators understand local rules, keep proof on site, and take the next action.
- Public provider routing exists only as operator support.
- Any future paid-placement, provider-sales, or third-party transaction workflow should live on a separate domain or repository.

## Value architecture

### Rule-clarity surfaces
- local rule pages
- inspection-prep pages
- authority browse pages
- trust and methodology pages

### Next-action surfaces
- approved-hauler and verification workflow pages
- public provider finder pages
- page-level operator routing requests when the operator wants help

### Retention surfaces
- grease log worksheet
- hood binder checklist
- missing proof tracker
- inspection reminder plan

## CTA rules
- CTA appears after the rule summary, proof burden, and fail conditions.
- CTA must match the issue state: authority contact, operator tool, checklist, provider finder, or routing request.
- Official approval language may appear only when an official list or authority source supports it.
- Provider routing must never appear as official guidance.
- Lead capture must stay clearly optional and secondary to public guidance.

## Current conversion path
1. Search lands on a local rule page, inspection-prep page, guide, or operator tool.
2. The page resolves authority, proof-on-site, and urgency.
3. The operator either fixes the records problem directly or uses a finder or routing request for outside help.
4. The operator returns through tools, reminders, and repeat-use local pages.

## What not to build here
- paid provider slots
- paid provider ranking
- third-party transaction workflows
- separate-domain provider CRM or sales ops
- ad-led surfaces that outrank authority guidance
