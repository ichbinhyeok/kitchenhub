# Free Vendor Wedge - 2026-04-19

## Why this exists

The current question is no longer broad sponsor logic or broad operator SEO.

The immediate question is:

What is the first free B2B artifact that a small, busy kitchen-service vendor office would actually use and send this week?

This note records the answer after a structured 20-perspective debate:

- 10 product and workflow perspectives
- 10 small-vendor office perspectives

## Decision

The first free B2B wedge is:

`hood service report`

More specifically:

- a factual post-job hood cleaning closeout artifact
- sent by a vendor office after service
- useful to the restaurant immediately
- narrow enough to use weekly
- safe enough to avoid sounding official

## Why this won

It won because it is the most naturally sendable artifact in the current wedge.

The send moment already exists:

- the hood job is done
- the photos exist
- the customer wants proof
- the office needs a clean handoff

This means the product is not asking the vendor to create a new habit.
It is inserting into an existing admin moment.

## Why the other options lost

### Inspection prep brief

Strong second product.
Weak first free wedge.

Why:

- urgent, but event-driven
- usually created only when inspection pressure is already active
- more operator-native than vendor-native
- heavier to assemble than a post-job report

### Mixed FOG / hood / inspection packet

Too broad for the first free product.

Why:

- easy to turn into a generic packet nobody sends
- too many fields
- invites portal thinking
- slows first adoption

### Missing proof tracker

Useful companion tool, not the first vendor send artifact.

Why:

- strongest as an internal ops worksheet
- weaker as a customer-facing handoff

## Naming decision

Use:

`hood service report`

Why this name:

- plain and factual
- familiar to vendor offices
- does not imply government approval
- does not imply code certification
- easier to trust than `certificate`
- easier to send than `binder` as the front label

Avoid leading with:

- certificate
- certified
- compliant
- inspection-ready guarantee

## V1 product shape

The first ship should stay inside the existing noindex operator-tool layer.

V1 should include:

- one tool page
- one vendor landing page that points into the tool without replacing the main operator home
- one vendor-side builder that fills the report in place
- one TXT handoff email export
- one CSV export of the same draft
- one clear list of what sections belong in the report
- one language-guardrail section
- links back to the Austin, Charlotte, and Miami hood rule pages

## V1 report structure

1. Job header
2. Work completed
3. Proof attached
4. Follow-up items
5. Next service window
6. Customer handoff

## Copy guardrails

Use phrases like:

- service performed
- photos attached
- follow-up item
- for your records

Avoid phrases like:

- officially approved
- certified compliant
- passed inspection
- guaranteed fire-safe

If a city or authority rule link is included, attach it separately or label it as reference material.
Do not turn the report body into an official-sounding compliance statement.

## Product boundary

Do not expand this into:

- CRM
- dashboard
- account system
- all-vendor workflow product
- broad restaurant compliance packet

The first free wedge is just:

`a sendable hood service report`

That means the free version is only good enough when a small vendor office can:

- fill it in under two minutes
- export the same draft into the customer email and worksheet
- keep the restaurant account and the actual recipient separate
- stop using sample placeholders once real editing starts
- catch missing attachments before the office hits send
- attach the right city rule page without editing the report body
- carry more than one follow-up item without rewriting the report
- print a clean PDF-style closeout without the vendor UI wrapped around it

## Reaction signals before monetization

Do not design setup, pricing, or account features yet.

The only question right now is whether the free report gets used in a real customer thread.

Watch for these signals first:

- the office can fill and send it in under two minutes
- copy subject, copy body, email draft, print/PDF, and rule-link clicks show up as real send-loop actions instead of silent UI behavior
- the TXT or PDF version goes out without manual rewrite
- the customer forwards it, files it, or asks for the linked rule page
- the vendor uses it again on a second real job
- the follow-up block carries real deferred items without turning back into ad hoc email text

If these signals appear, monetization can be discussed later.

Until then:

- keep setup CTA off the main free hood flow
- keep repeat-use memory browser-local instead of adding accounts or server-stored setup
- keep the last working hood draft browser-local too, so the office can restore the recent job without turning free usage into an account product
- keep the landing vendor-first and demo-first
- measure send behavior before adding packaging language

Free should still feel like a real office tool, so the closeout should also help with:

- restoring the last job draft from the same browser
- naming the attached PDF, photo zip, and invoice/work-order file consistently
- exporting a print/PDF version that still reads cleanly after the web UI is removed
- dropping local hood photos into the closeout so the same browser tab can print a proof-rich PDF without uploading files to a server

## 2026-04-19 refinement notes

- The first two repeat-use gaps are now tightened: the office can clear the teaching sample into a true blank closeout in one click, and the browser can store a small set of saved customer setups for repeat accounts without introducing server-side memory.
- Saved customer setups should stay limited to stable account fields only: city, restaurant account, location, recipient, site, systems, and handoff defaults. Do not save work completed, proof references, service dates, or follow-up notes into that preset layer.
- Blank closeouts should not force fake follow-up state. If there are no follow-up items, the customer copy should stop at `No follow-up items noted.` and omit owner or due-date filler text.
- The vendor landing should point to a real blank closeout, not a sample-led demo. The first vendor CTA is stronger when it says "start today's hood closeout" rather than asking the office to replace example values before the product feels usable.
- Proof references should not feel mandatory in the free flow. If photos or the invoice/report file are already attached, the product should read that as valid proof and only use reference names as optional office shorthand.
