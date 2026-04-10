# Source Audit - 2026-04-07

## Scope
- Audit date: 2026-04-07
- Cohort reviewed: all live Tier 1 and Tier 2 cities
- Method: fresh web search against official city, county, utility, and fire-authority domains plus comparison against local JSON source records

## Summary
- Tier 1 cities reviewed: Austin, Charlotte, Tampa, Portland, Santa Clara
- Tier 2 cities reviewed: Nashville, Grand Island, Miami
- Direct source corrections made:
  - Austin FOG primary source upgraded to the 2024 Austin Water grease-interceptor brochure
  - Santa Clara FOG canonical URL updated to the newer sewer-utility path
  - Miami added with current county FOG, mechanical, and fire-inspection sources
- Cohort status after audit:
  - All eight live cities remain source-backed
  - No indexed route needed to be de-indexed
  - The weakest live pages remain Grand Island hood pages, because the public materials are permit-and-final-inspection heavy rather than cadence heavy

## City Notes

### Austin, TX
- FOG checked against official Austin Water grease-interceptor documents and liquid waste hauler program
- Current strongest official FOG artifact found in search:
  - [Grease Interceptors - Customer Responsibilities](https://www.austintexas.gov/sites/default/files/files/Water/Discharge_Permits/Customer%20Responsibilities_Grease%20Interceptors_Aug_2024.pdf)
- Current hauler-verification artifact remains valid:
  - [City of Austin - Liquid Waste Haulers](https://services.austintexas.gov/water/weirs/index.cfm?fuseaction=report.publicLWHauler)
- Fire/hood assumptions remain consistent with Austin Fire maintenance and inspection materials
- Action taken:
  - upgraded the primary FOG source record to the newer Austin Water brochure

### Charlotte, NC
- FOG checked against Charlotte Water FlowFree and current Charlotte Water BMP/policy material
- Current search surfaced:
  - [BMP Manual Updated January 2024](https://www.charlottenc.gov/files/sharedassets/cltwater/v/1/documents/developmentprojects/customer-assistance/flow-free/bmp-manual-updated-january-2024.pdf)
  - [FlowFree](https://www.charlottenc.gov/water/Customer-Care/Plumbing-Guidance-Compliance/FlowFree)
- Hood assumptions still align with Charlotte Fire's hood system service interpretation
- Action taken:
  - no schema changes needed; current rule copy still matches official materials

### Tampa, FL
- FOG checked against Tampa grease ordinance and registered grease hauler workflow
- Hood and inspection assumptions remain supported by Fire Marshal and restaurant safety materials
- Current official anchors remain:
  - [Grease Ordinance](https://www.tampa.gov/wastewater/programs/grease-ordinance)
  - [Registered Grease Haulers with City of Tampa](https://www.tampa.gov/sites/default/files/document/2025/grease-hauler-approved-09-02-25.pdf)
  - [Fire Marshal's Office](https://www.tampa.gov/fire-rescue/about-us/fire-marshals-office)
- Action taken:
  - no changes required

### Portland, OR
- FOG checked against BES FOG rules and Preferred Pumper workflow
- Hood and inspection assumptions still align with Portland fire inspection-frequency and ITM sources
- Current official anchors remain:
  - [Grease Interceptors and How to Maintain Them](https://www.portland.gov/bes/article/406702)
  - [Preferred Pumper Matrix](https://static1.squarespace.com/static/58c02df503596ee56b2eee03/t/68278731d3231448c94c45d5/1747420977838/Preferred%2BPumper%2BMatrix%2BMay2025.pdf)
  - [Fire Code Inspection Frequency](https://www.portland.gov/sites/default/files/2020-06/fir-1.10-inspection-program-frequency-042918-754436.pdf)
- Action taken:
  - no changes required

### Santa Clara, CA
- FOG checked against current Santa Clara sewer utility FOG program path
- Fire assumptions still align with municipal fire code and commercial cooking operations guidance
- Current official anchors remain:
  - [FOG Program](https://www.santaclaraca.gov/our-city/departments-g-z/water-sewer-utilities/sewer-utility/fats-oils-and-grease-fog-program)
  - [Municipal Fire and Environmental Code](https://www.santaclaraca.gov/our-city/departments-a-f/fire-department/municipal-fire-and-environmental-code)
- Action taken:
  - corrected the FOG authority URL, source URL, and linked provider evidence URLs to the newer canonical path

### Nashville, TN
- FOG checked against Metro Water grease management, 2025 FOG policy, and approved-hauler workflow
- Hood assumptions remain supported by Nashville Fire inspections, IROL, and vent hood procedures
- Current official anchors remain:
  - [Grease Management](https://www.nashville.gov/departments/water/environmental-compliance/grease-management)
  - [FOG Policy 2025](https://www.nashville.gov/sites/default/files/2025-02/FOG_Policy_2025.pdf?ct=1739375957)
  - [Inspection Reports Online](https://www.nashville.gov/departments/fire/fire-marshal-office/inspection-reports-online)
  - [Vent Hood Cleaning Procedures & Regulations](https://www.nashville.gov/sites/default/files/2025-10/vent_hood_cleaning_procedures_regulations.pdf?ct=1761337530)
- Audit note:
  - current seed still uses the existing public approved-hauler list record already in the repo; the 2025 FOG policy was rechecked and confirms the approved-hauler agreement requirement

### Grand Island, NE
- FOG checked against the city FOG program and food-service establishment workflow
- Hood and inspection assumptions remain permit-and-final-inspection based, not cadence based
- Current official anchors remain:
  - [Fats, Oils and Grease (FOG) Program](https://www.grand-island.com/page/fats-oils-and-grease-fog-program/)
  - [Food Service Establishments](https://www.grand-island.com/o/cgi/page/food-service-establishments)
  - [Apply for a Permit](https://www.grand-island.com/o/gifire/page/apply-for-a-permit)
  - [Inspections](https://www.grand-island.com/o/cgi/page/inspections)
- Audit note:
  - Grand Island stays source-backed, but its hood route should continue to be written as permit-and-inspection prep rather than a hard local cleaning-frequency page

### Miami, FL
- Added and checked county-level FOG, transporter, building, and fire-inspection sources
- Core official anchors now in the registry:
  - [FOG Discharge Control Operating Permit](https://www.miamidade.gov/global/permit.page?Mduid_permit=per1731510597042250)
  - [FOG Fact Sheet](https://www.miamidade.gov/resources/environment/permits/documents/fats-oils-grease-fact-sheet.pdf)
  - [Liquid Waste Transporter Permit](https://www.miamidade.gov/global/permit.page?Mduid_permit=per1719600493495599)
  - [Mechanical Inspection Guidelines](https://wwwx.miamidade.gov/permits/library/checklists/mechanical-inspection.pdf)
  - [Fire Inspections](https://www.miamidade.gov/global/permit.page?Mduid_permit=per1518190923457614)
  - [Certificate of Occupancy and Certificate of Use](https://www.miamidade.gov/global/economy/building/cert-of-occupancy.page)
- Audit note:
  - Miami's FOG and hood governance is county-heavy, so the city path is still accurate for search intent, but the authority names remain county entities in the page body

## Follow-up Candidates
- Consider upgrading Charlotte's FOG source stack to include the 2024 BMP manual alongside the older policy PDF
- Consider adding Austin's newer 2025 FSE guide as a supporting source if the page later needs more operator-facing BMP language
- Recheck the Nashville public approved-hauler list URL on the next audit and swap it if Metro Water exposes a newer direct PDF
