@import owner.kitchencompliance.data.RouteTemplate
@import owner.kitchencompliance.model.InspectionChecklistSection
@import owner.kitchencompliance.model.LocalPageViewModel
@import owner.kitchencompliance.model.ProviderCard
@import owner.kitchencompliance.model.RelatedPageLink
@import owner.kitchencompliance.model.SourceAttribution
@import owner.kitchencompliance.web.ViewFormatSupport

@param LocalPageViewModel page

<main class="page-shell page-shell--local${page.template() == RouteTemplate.FIND_GREASE_SERVICE || page.template() == RouteTemplate.FIND_HOOD_CLEANER ? " local-page--provider" : ""}">
    <div class="local-layout">
        <aside class="local-sidebar">
            <div class="local-sidebar__panel">
                <div class="local-sidebar__eyebrow">
                    ${page.template() == RouteTemplate.HOOD_REQUIREMENTS || page.template() == RouteTemplate.INSPECTION_CHECKLIST ? "Authority-backed route" : "Local rule summary"}
                </div>
                <div class="local-sidebar__city">
                    <span aria-hidden="true" class="local-sidebar__city-mark">${page.state()}</span>
                    <div>
                        <div class="local-sidebar__city-title">${page.city()}, ${page.state()}</div>
                        <div class="local-sidebar__city-subtitle">${page.authorityName()}</div>
                    </div>
                </div>
            </div>

            <nav aria-label="Local navigation" class="local-sidebar__nav">
                <a class="is-current" href="${page.sourcePath()}">
                    <span class="material-symbols-outlined">verified_user</span>
                    <span>${page.title()}</span>
                </a>
                @for(RelatedPageLink link : page.relatedLinks())
                    <a href="${link.path()}">
                        <span class="material-symbols-outlined">chevron_right</span>
                        <span>${link.label()}</span>
                    </a>
                @endfor
            </nav>

            @if(page.operatorToolLinks() != null && !page.operatorToolLinks().isEmpty())
                <div class="local-sidebar__panel">
                    <div class="local-sidebar__eyebrow">Repeat-use tools</div>
                    <nav aria-label="Operator tools" class="local-sidebar__nav">
                        @for(RelatedPageLink link : page.operatorToolLinks())
                            <a href="${link.path()}">
                                <span class="material-symbols-outlined">construction</span>
                                <span>${link.label()}</span>
                            </a>
                        @endfor
                    </nav>
                </div>
            @endif

            <a class="button button--dark button--block" href="#official-sources">Print Paperwork</a>
        </aside>

        <div class="local-main">
            @if(page.template() == RouteTemplate.HOOD_REQUIREMENTS || page.template() == RouteTemplate.INSPECTION_CHECKLIST)
                <div class="context-tabs" aria-label="Local portal tabs">
                    <span class="context-tab${page.template() == RouteTemplate.HOOD_REQUIREMENTS ? " is-active" : ""}">Hood Cleaning</span>
                    <span class="context-tab${page.template() == RouteTemplate.INSPECTION_CHECKLIST ? " is-active" : ""}">Fire Inspection</span>
                    <span class="context-tab">Authority Guidance</span>
                </div>
            @endif

            @if(page.template() != RouteTemplate.FIND_GREASE_SERVICE && page.template() != RouteTemplate.FIND_HOOD_CLEANER)
                <section class="panel governance-note">
                    <div class="panel__eyebrow">${page.governanceHeading()}</div>
                    <h2>Rule holder: ${page.authorityName()}</h2>
                    <p class="panel__note" style="margin-top: 10px;">${page.governanceBody()}</p>
                    <div class="action-row governance-note__actions">
                        @if(!page.sourcePath().equals(page.canonicalPath()))
                            <a class="button button--secondary" href="${page.canonicalPath()}">Open authority page</a>
                        @endif
                        @if(!page.sourcePath().equals(page.cityEntryPath()))
                            <a class="button button--ghost" href="${page.cityEntryPath()}">Open city entry</a>
                        @endif
                    </div>
                </section>
            @endif

            @if(page.template() == RouteTemplate.FOG_RULES || page.template() == RouteTemplate.APPROVED_HAULERS)
                <section class="local-hero">
                    <div class="local-hero__eyebrow">${page.kicker()}</div>
                    <h1>${page.title()}</h1>
                    <p class="local-hero__summary">${page.summary()}</p>
                    <div class="local-hero__meta">
                        <span>${page.authorityName()}</span>
                        <span>${page.authorityTypeLabel()}</span>
                        <span>Last verified ${page.meta().lastVerified().toString()}</span>
                    </div>
                </section>

                <section class="metric-strip trust-strip">
                    <article class="metric-card">
                        <div class="metric-card__label">Authority</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.officialRequirements(), page.summary())}</div>
                    </article>
                    <article class="metric-card">
                        <div class="metric-card__label">Proof on site</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.onSiteRecords(), page.summary())}</div>
                    </article>
                    <article class="metric-card">
                        <div class="metric-card__label">Likely fail trigger</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.failureReasons(), page.summary())}</div>
                    </article>
                    <article class="metric-card">
                        <div class="metric-card__label">Next action</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.nextActions(), page.summary())}</div>
                    </article>
                </section>

                <section class="fog-overview">
                    <article class="authority-card">
                        <div class="authority-card__label">Overall verdict</div>
                        <div class="authority-card__title">${page.cityVerdict().authoritySummary()}</div>
                        <p class="authority-card__body">${page.noteBody() != null ? page.noteBody() : page.summary()}</p>
                    </article>

                    <div class="fog-overview__cards">
                        <article class="authority-snippet">
                            <h3>Authority Summary</h3>
                            <p>${page.officialRequirements().size() > 0 ? page.officialRequirements().get(0) : page.summary()}</p>
                        </article>
                        <article class="authority-snippet authority-snippet--warning">
                            <h3>${page.recordsHeading()}</h3>
                            <p>${page.onSiteRecords().size() > 0 ? page.onSiteRecords().get(0) : page.summary()}</p>
                        </article>
                    </div>
                </section>

                <section class="panel">
                    <div class="panel__eyebrow">${page.officialHeading()}</div>
                    <h2>Sizing &amp; Frequency Mandates</h2>
                    <div class="metric-strip" style="margin-top: 18px;">
                        @for(String item : page.officialRequirements())
                            <article class="metric-card">
                                <div class="metric-card__label">Official requirement</div>
                                <div class="metric-card__body">${item}</div>
                            </article>
                        @endfor
                    </div>
                </section>

                <section class="panel">
                    <div class="panel__eyebrow">${page.recordsHeading()}</div>
                    <h2>Inspection-Ready Proof</h2>
                    <div class="proof-grid" style="margin-top: 18px;">
                        @for(String item : page.onSiteRecords())
                            <article class="proof-item">
                                <div class="proof-item__icon">
                                    <span class="material-symbols-outlined">fact_check</span>
                                </div>
                                <h3>${item}</h3>
                                <p>Keep this accessible before the inspector has to ask twice.</p>
                            </article>
                        @endfor
                    </div>
                </section>

                <section class="failure-panel">
                    <div class="failure-panel__image">
                        <img alt="Grease pipe" src="/assets/grease-pipe.png">
                    </div>

                    <article class="failure-list">
                        <div class="failure-list__eyebrow">${page.riskHeading()}</div>
                        <h2>Common Inspection Failures</h2>
                        <div class="failure-list__items">
                            @for(String item : page.failureReasons())
                                <div class="failure-item">
                                    <div>
                                        <strong>${item}</strong>
                                        <p>Failure to resolve this condition can trigger corrective action or delayed approval.</p>
                                    </div>
                                    <div class="failure-item__cost">High risk</div>
                                </div>
                            @endfor
                        </div>
                    </article>
                </section>

                @if(page.callToAction() != null)
                    <section class="cta-band">
                        <div>
                            <h2>${page.callToAction().title()}</h2>
                            <p>${page.callToAction().description()}</p>
                        </div>
                        <div class="action-row" style="margin-top: 0;">
                            <a class="button button--accent" href="${page.callToAction().path()}">${page.callToAction().label()}</a>
                            <a class="button button--secondary" href="#official-sources">Download Log Tool</a>
                        </div>
                    </section>
                @endif

                @if(page.providerModeSummary() != null || page.officialListStatement() != null || page.template() == RouteTemplate.APPROVED_HAULERS)
                    <article class="support-note">
                        @if(page.providerModeSummary() != null)
                            <p>${page.providerModeSummary()}</p>
                        @endif
                        @if(page.officialListStatement() != null)
                            <p>${page.officialListStatement()}</p>
                        @endif
                        @if(page.template() == RouteTemplate.APPROVED_HAULERS)
                            <p>The city publishes this list as a verification tool, neither recommends nor endorses any provider on it.</p>
                        @endif
                    </article>
                @endif

                @if(page.sponsorPanel() != null)
                    <section class="panel">
                        <div class="panel__eyebrow">${page.sponsorPanel().eyebrow()}</div>
                        <h2>${page.sponsorPanel().title()}</h2>
                        <p class="panel__note" style="margin-top: 8px;">${page.sponsorPanel().description()}</p>
                        @template.layout.leadForm(page.sourcePath(), page.sponsorPanel())
                    </section>
                @endif

                @if(page.providers() != null && !page.providers().isEmpty())
                    <section class="panel">
                        <div class="panel__eyebrow">Local service providers</div>
                        <h2>Qualified providers serving the ${page.city()} area</h2>
                        <div class="provider-list" style="margin-top: 18px;">
                            @for(ProviderCard provider : page.providers())
                                <article class="provider-card${provider.listingLabel().toLowerCase().contains("sponsor") || provider.sponsorStatusLabel().toLowerCase().contains("sponsor") ? " provider-card--featured" : ""}">
                                    <div class="provider-card__identity">
                                        <div class="provider-card__mark">${ViewFormatSupport.initials(provider.providerName())}</div>
                                        <div class="provider-card__heading">
                                            <div class="provider-card__topline">
                                                <div class="provider-card__title">${provider.providerName()}</div>
                                                <div class="provider-card__badge${provider.listingLabel().toLowerCase().contains("sponsor") || provider.sponsorStatusLabel().toLowerCase().contains("sponsor") ? "" : " provider-card__badge--neutral"}">
                                                    ${provider.listingLabel()}
                                                </div>
                                            </div>
                                            <div class="provider-card__meta">${provider.providerTypeLabel()} | Evidence: ${provider.evidenceLabel()} | Coverage confidence: ${provider.coverageConfidenceLabel()}</div>
                                        </div>
                                    </div>
                                    <div class="provider-card__signals">
                                        <div class="provider-card__signal">
                                            <span class="provider-card__signal-label">Why listed</span>
                                            <span>${provider.whyListed()}</span>
                                        </div>
                                        <div class="provider-card__signal provider-card__signal--muted">
                                            <span class="provider-card__signal-label">Evidence review</span>
                                            <span>${provider.evidenceReviewLabel()}</span>
                                        </div>
                                    </div>
                                    <p class="provider-card__note">${provider.note()}</p>
                                    <div class="provider-card__footer">
                                        <span class="panel__note">${provider.sponsorStatusLabel()}</span>
                                        <a class="text-link" href="${provider.siteUrl()}" rel="noopener noreferrer" target="_blank">Visit provider site</a>
                                    </div>
                                </article>
                            @endfor
                        </div>
                    </section>
                @endif
            @endif

            @if(page.template() == RouteTemplate.HOOD_REQUIREMENTS)
                <section class="hero-banner">
                    <div class="hero-banner__tag">Fire Safety Directive</div>
                    <h1>${page.title()}</h1>
                    <p>${page.summary()}</p>
                </section>

                <section class="metric-strip trust-strip">
                    <article class="metric-card">
                        <div class="metric-card__label">Authority</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.officialRequirements(), page.summary())}</div>
                    </article>
                    <article class="metric-card">
                        <div class="metric-card__label">Proof on site</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.onSiteRecords(), page.summary())}</div>
                    </article>
                    <article class="metric-card">
                        <div class="metric-card__label">Likely fail trigger</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.failureReasons(), page.summary())}</div>
                    </article>
                    <article class="metric-card">
                        <div class="metric-card__label">Next action</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.nextActions(), page.summary())}</div>
                    </article>
                </section>

                <section class="hood-portal">
                    <div class="hood-portal__main">
                        <article class="hood-authority">
                            <div class="hood-authority__icon">
                                <span class="material-symbols-outlined">account_balance</span>
                            </div>
                            <div>
                                <div class="panel__eyebrow">Authority summary</div>
                                <h2>Authority Summary</h2>
                                <p>${page.noteBody() != null ? page.noteBody() : page.summary()}</p>
                            </div>
                        </article>

                        @if(!page.sources().isEmpty())
                            <section class="source-inline-grid">
                                @for(SourceAttribution source : ViewFormatSupport.topSources(page.sources(), 2))
                                    <article class="source-inline-card">
                                        <div class="panel__eyebrow">Primary source</div>
                                        <h3><a href="${source.url()}" rel="noopener noreferrer" target="_blank">${source.title()}</a></h3>
                                        <p>${source.summary()}</p>
                                        <div class="source-inline-card__meta">${source.agency()} | ${source.tierLabel()} | ${source.verifiedOn().toString()}</div>
                                    </article>
                                @endfor
                            </section>
                        @endif

                        <section class="hood-frequency">
                            <div class="panel__eyebrow">${page.officialHeading()}</div>
                            <h2>Cleaning Frequency Requirements</h2>
                            <div class="hood-frequency__grid">
                                @for(int i = 0; i < page.officialRequirements().size(); i++)
                                    <article class="hood-frequency__card${i == 0 ? " hood-frequency__card--featured" : ""}">
                                        <div class="hood-frequency__value">${ViewFormatSupport.frequencyLabel(page.officialRequirements().get(i), i)}</div>
                                        <div class="hood-frequency__context">${ViewFormatSupport.frequencyContext(page.officialRequirements().get(i), i)}</div>
                                        <p>${page.officialRequirements().get(i)}</p>
                                    </article>
                                @endfor
                            </div>
                        </section>

                        <section class="hood-detail-grid">
                            <article class="panel panel--plain">
                                <div class="panel__eyebrow">${page.recordsHeading()}</div>
                                <h2>Required Cleaning Tags</h2>
                                <div class="tag-ladder" style="margin-top: 18px;">
                                    <div class="tag-ladder__row">
                                        <div class="tag-ladder__bar tag-ladder__bar--green"></div>
                                        <div>
                                            <div class="tag-ladder__label">Green Tag</div>
                                            <p>${page.onSiteRecords().size() > 0 ? page.onSiteRecords().get(0) : page.summary()}</p>
                                        </div>
                                    </div>
                                    <div class="tag-ladder__row">
                                        <div class="tag-ladder__bar tag-ladder__bar--yellow"></div>
                                        <div>
                                            <div class="tag-ladder__label">Yellow Tag</div>
                                            <p>${page.onSiteRecords().size() > 1 ? page.onSiteRecords().get(1) : page.summary()}</p>
                                        </div>
                                    </div>
                                    <div class="tag-ladder__row">
                                        <div class="tag-ladder__bar tag-ladder__bar--red"></div>
                                        <div>
                                            <div class="tag-ladder__label">Red Tag</div>
                                            <p>${page.failureReasons().size() > 0 ? page.failureReasons().get(0) : page.summary()}</p>
                                        </div>
                                    </div>
                                </div>
                            </article>

                            <article class="hood-proofbox">
                                <div class="hood-proofbox__inner">
                                    <span class="material-symbols-outlined">fact_check</span>
                                    <h3>Post-Service Reports</h3>
                                    <p>${page.onSiteRecords().size() > 1 ? page.onSiteRecords().get(1) : page.recordsHeading()}</p>
                                </div>
                            </article>
                        </section>

                        <section class="hood-fails">
                            <h2>
                                <span class="material-symbols-outlined">warning</span>
                                Common Inspection Fails
                            </h2>
                            <div class="hood-fails__grid">
                                @for(String item : page.failureReasons())
                                    <article class="hood-fail">
                                        <div class="hood-fail__bar"></div>
                                        <h3>${item}</h3>
                                        <p>Inspectors flag this when the hood, duct, or report trail is not inspection-ready.</p>
                                    </article>
                                @endfor
                            </div>
                        </section>
                    </div>

                    <div class="hood-rail">
                        @if(page.callToAction() != null)
                            <article class="panel">
                                <div class="panel__eyebrow">Cleaner routing</div>
                                <h2>${page.callToAction().title()}</h2>
                                <p class="panel__note" style="margin-top: 8px;">${page.callToAction().description()}</p>
                                <a class="button button--dark button--block" href="${page.callToAction().path()}" style="margin-top: 16px;">
                                    ${page.callToAction().label()}
                                </a>
                            </article>
                        @endif

                        <article class="hood-prooflist">
                            <div class="panel__eyebrow">Inspection-ready proof</div>
                            <h3>Keep these records visible</h3>
                            <div class="hood-prooflist__items">
                                @for(String item : page.onSiteRecords())
                                    <div class="hood-prooflist__item">
                                        <span class="material-symbols-outlined">verified</span>
                                        <div>
                                            <strong>${item}</strong>
                                            <p>Required to be staged before the inspector asks.</p>
                                        </div>
                                    </div>
                                @endfor
                            </div>
                        </article>

                        @if(page.sponsorPanel() != null)
                            <section class="partner-stack">
                                <div class="partner-stack__label">Verified Partners</div>
                                <article class="partner-card">
                                    <div class="partner-card__mark">SP</div>
                                    <div>
                                        <h4>${page.sponsorPanel().title()}</h4>
                                        <p>${page.sponsorPanel().description()}</p>
                                    </div>
                                </article>
                            </section>
                        @endif
                    </div>
                </section>
            @endif

            @if(page.template() == RouteTemplate.INSPECTION_CHECKLIST)
                <section class="inspection-hero">
                    <article class="local-hero">
                        <div class="local-hero__eyebrow">${page.kicker()}</div>
                        <h1>${page.title()}</h1>
                        <p class="local-hero__summary">${page.summary()}</p>
                        <div class="local-hero__meta">
                            <span>${page.authorityName()}</span>
                            <span>${page.authorityTypeLabel()}</span>
                            <span>Last verified ${page.meta().lastVerified().toString()}</span>
                        </div>
                    </article>

                    <div class="inspection-hero__actions">
                        <a class="button button--dark button--block" href="#official-sources">Download PDF Checklist</a>
                        @if(page.callToAction() != null)
                            <a class="button button--secondary button--block" href="${page.callToAction().path()}">${page.callToAction().label()}</a>
                        @endif
                        <article class="inspection-hero__window">
                            <div class="panel__eyebrow">Next inspection window</div>
                            <h2>Prep the next inspection window</h2>
                            <p class="panel__note" style="margin-top: 8px;">Local inspectors can confirm preferred staging order before an access check.</p>
                        </article>
                    </div>
                </section>

                <section class="metric-strip trust-strip">
                    <article class="metric-card">
                        <div class="metric-card__label">Authority</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.officialRequirements(), page.summary())}</div>
                    </article>
                    <article class="metric-card">
                        <div class="metric-card__label">Proof on site</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.onSiteRecords(), page.summary())}</div>
                    </article>
                    <article class="metric-card">
                        <div class="metric-card__label">Likely fail trigger</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.failureReasons(), page.summary())}</div>
                    </article>
                    <article class="metric-card">
                        <div class="metric-card__label">Next action</div>
                        <div class="metric-card__body">${ViewFormatSupport.firstOrFallback(page.nextActions(), page.summary())}</div>
                    </article>
                </section>

                <section class="inspection-callout">
                    <article class="inspection-callout__warning">
                        <div class="panel__eyebrow" style="color: var(--accent);">Potential fines &amp; closures</div>
                        <h2>Potential fines &amp; closures</h2>
                        <ul>
                            @for(String item : page.failureReasons())
                                <li>${item}</li>
                            @endfor
                        </ul>
                    </article>

                    <article class="panel">
                        <div class="panel__eyebrow">Authority reminder</div>
                        <h2>${page.cityVerdict().authoritySummary()}</h2>
                        <p class="panel__note" style="margin-top: 10px;">${page.noteBody() != null ? page.noteBody() : page.summary()}</p>
                        @if(!page.sources().isEmpty())
                            @for(SourceAttribution source : ViewFormatSupport.topSources(page.sources(), 1))
                                <article class="source-inline-card source-inline-card--compact" style="margin-top: 18px;">
                                    <div class="panel__eyebrow">Primary source</div>
                                    <h3><a href="${source.url()}" rel="noopener noreferrer" target="_blank">${source.title()}</a></h3>
                                    <p>${source.summary()}</p>
                                    <div class="source-inline-card__meta">${source.agency()} | ${source.tierLabel()} | ${source.verifiedOn().toString()}</div>
                                </article>
                            @endfor
                        @endif
                    </article>
                </section>

                <section class="checklist-panel">
                    @for(InspectionChecklistSection section : ViewFormatSupport.inspectionSections(page.onSiteRecords(), page.failureReasons()))
                        <article class="checklist-card">
                            <div class="checklist-card__header">
                                <span class="checklist-card__number">
                                    <span class="material-symbols-outlined">${section.iconName()}</span>
                                </span>
                                ${section.title()}
                            </div>

                            @for(String requirement : section.requirements())
                                <div class="checklist-card__item">
                                    <div class="checkmark-box"></div>
                                    <div>
                                        <strong>${requirement}</strong>
                                        <p>Current evidence should be staged before the inspector asks for it.</p>
                                    </div>
                                </div>
                            @endfor

                            @for(String failure : section.failures())
                                <div class="checklist-card__item">
                                    <div class="checkmark-box"></div>
                                    <div>
                                        <strong>${failure}</strong>
                                        <p>Resolve this before the inspection day to avoid follow-up notices.</p>
                                    </div>
                                </div>
                            @endfor
                        </article>
                    @endfor
                </section>

                <section class="records-showcase">
                    <article class="panel panel--plain">
                        <div class="panel__eyebrow">${page.recordsHeading()}</div>
                        <h2>Records binder</h2>
                        <ul class="bullet-list" style="margin-top: 16px;">
                            @for(String item : page.onSiteRecords())
                                <li>${item}</li>
                            @endfor
                        </ul>
                    </article>

                    <div class="records-showcase__image">
                        <img alt="Inspection binder" src="/assets/inspection-binder.png">
                        <div class="records-showcase__caption">Centralized digital &amp; physical archive</div>
                    </div>
                </section>

                @if(page.callToAction() != null)
                    <section class="cta-band">
                        <div>
                            <h2>${page.callToAction().title()}</h2>
                            <p>${page.callToAction().description()}</p>
                        </div>
                        <a class="button button--accent" href="${page.callToAction().path()}">${page.callToAction().label()}</a>
                    </section>
                @endif
            @endif

            @if(page.template() == RouteTemplate.FIND_GREASE_SERVICE || page.template() == RouteTemplate.FIND_HOOD_CLEANER)
                <section class="provider-hero">
                    <article class="provider-summary">
                        <div class="provider-governance-rail">
                            <div>
                                <div class="panel__eyebrow">${page.governanceHeading()}</div>
                                <p>${page.governanceBody()}</p>
                            </div>
                            <div class="provider-governance-rail__actions">
                                @if(!page.sourcePath().equals(page.canonicalPath()))
                                    <a class="button button--secondary" href="${page.canonicalPath()}">Authority path</a>
                                @endif
                                @if(!page.sourcePath().equals(page.cityEntryPath()))
                                    <a class="button button--ghost" href="${page.cityEntryPath()}">City entry</a>
                                @endif
                            </div>
                        </div>
                        <div class="local-hero__eyebrow">${page.kicker()}</div>
                        <h1 style="font-size: clamp(2.3rem, 4.6vw, 3.5rem); line-height: 0.95; margin-top: 8px;">${page.title()}</h1>
                        <p class="local-hero__summary">${page.summary()}</p>
                        <div class="local-hero__meta">
                            <span>${page.authorityName()}</span>
                            <span>${page.authorityTypeLabel()}</span>
                            <span>Last verified ${page.meta().lastVerified().toString()}</span>
                        </div>
                        <article class="authority-snippet">
                            <h3>${page.noteTitle()}</h3>
                            <p>${page.noteBody()}</p>
                        </article>
                    </article>

                    <article class="provider-form">
                        @if(page.operatorLeadPanel() != null && page.prioritizeOperatorLeadPanel())
                            <div class="provider-form__rail">Verification stays separate from vendor routing.</div>
                            <div class="panel__eyebrow">${page.operatorLeadPanel().eyebrow()}</div>
                            <h2>${page.operatorLeadPanel().title()}</h2>
                            <p class="panel__note" style="margin-top: 8px;">${page.operatorLeadPanel().description()}</p>
                            @template.layout.leadForm(page.sourcePath(), page.operatorLeadPanel())
                        @else
                            <div class="panel__eyebrow">Verification first</div>
                            <h2>${page.trustBannerTitle()}</h2>
                            <p class="panel__note" style="margin-top: 8px;">${page.trustBannerBody()}</p>
                            @if(page.verificationChecklist() != null && !page.verificationChecklist().isEmpty())
                                <ul class="bullet-list" style="margin-top: 16px;">
                                    @for(String item : page.verificationChecklist())
                                        <li>${item}</li>
                                    @endfor
                                </ul>
                            @endif
                            @if(page.callToAction() != null)
                                <a class="button button--dark button--block" href="${page.callToAction().path()}" style="margin-top: 16px;">
                                    ${page.callToAction().label()}
                                </a>
                            @endif
                        @endif
                    </article>
                </section>

                <section class="provider-method-grid">
                    <article class="source-inline-card">
                        <div class="panel__eyebrow">How listings are qualified</div>
                        <h3>${page.trustBannerTitle() != null ? page.trustBannerTitle() : "Evidence-first routing"}</h3>
                        <p>${page.providerModeSummary() != null ? page.providerModeSummary() : page.summary()}</p>
                        <div class="source-inline-card__meta">
                            Authority-backed ${ViewFormatSupport.authorityBackedProviderCount(page.providers())} |
                            Verification required ${ViewFormatSupport.verificationRequiredProviderCount(page.providers())} |
                            Last verified ${page.meta().lastVerified().toString()}
                        </div>
                    </article>

                    @if(!page.sources().isEmpty())
                        <article class="source-inline-card">
                            <div class="panel__eyebrow">Primary source</div>
                            @for(SourceAttribution source : ViewFormatSupport.topSources(page.sources(), 1))
                                <h3><a href="${source.url()}" rel="noopener noreferrer" target="_blank">${source.title()}</a></h3>
                                <p>${source.summary()}</p>
                                <div class="source-inline-card__meta">${source.agency()} | ${source.tierLabel()} | ${source.verifiedOn().toString()}</div>
                            @endfor
                        </article>
                    @endif
                </section>

                @if(page.verificationChecklist() != null && !page.verificationChecklist().isEmpty())
                    <article class="support-note support-note--provider">
                        <div class="panel__eyebrow">Before you book</div>
                        <strong>Verification checklist</strong>
                        <ul class="bullet-list" style="margin-top: 12px;">
                            @for(String item : page.verificationChecklist())
                                <li>${item}</li>
                            @endfor
                        </ul>
                    </article>
                @endif

                <section class="panel">
                    <div class="section-heading">
                        <div>
                            <div class="section-heading__eyebrow">Active routing coverage in ${page.city()}</div>
                            <h2 class="section-heading__title">Provider Listings</h2>
                        </div>
                        <div class="section-heading__tag">Evidence and sponsor status</div>
                    </div>

                    <div class="provider-list">
                        @for(ProviderCard provider : page.providers())
                            <article class="provider-card${provider.listingLabel().toLowerCase().contains("sponsor") || provider.sponsorStatusLabel().toLowerCase().contains("sponsor") ? " provider-card--featured" : ""}">
                                <div class="provider-card__identity">
                                    <div class="provider-card__mark">${ViewFormatSupport.initials(provider.providerName())}</div>
                                    <div class="provider-card__heading">
                                        <div class="provider-card__topline">
                                            <div class="provider-card__title">${provider.providerName()}</div>
                                            <div class="provider-card__badge${provider.listingLabel().toLowerCase().contains("sponsor") || provider.sponsorStatusLabel().toLowerCase().contains("sponsor") ? "" : " provider-card__badge--neutral"}">
                                                ${provider.listingLabel()}
                                            </div>
                                        </div>
                                        <div class="provider-card__meta">${provider.providerTypeLabel()} | Evidence: ${provider.evidenceLabel()} | Coverage confidence: ${provider.coverageConfidenceLabel()}</div>
                                    </div>
                                </div>
                                <div class="provider-card__signals">
                                    <div class="provider-card__signal">
                                        <span class="provider-card__signal-label">Why listed</span>
                                        <span>${provider.whyListed()}</span>
                                    </div>
                                    <div class="provider-card__signal provider-card__signal--muted">
                                        <span class="provider-card__signal-label">Evidence review</span>
                                        <span>${provider.evidenceReviewLabel()}</span>
                                    </div>
                                </div>
                                <p>${provider.note()}</p>
                                <div class="provider-card__contacts">
                                    <span>${provider.phone()}</span>
                                    <span>${provider.email()}</span>
                                </div>
                                <div class="provider-card__proof${ViewFormatSupport.hasOfficialEvidence(provider.officialApprovalSourceUrl()) ? "" : " provider-card__proof--muted"}">
                                    @if(ViewFormatSupport.hasOfficialEvidence(provider.officialApprovalSourceUrl()))
                                        <a href="${provider.officialApprovalSourceUrl()}" rel="noopener noreferrer" target="_blank">View authority-backed evidence</a>
                                    @else
                                        Public service page only; operator should confirm current city coverage.
                                    @endif
                                </div>
                                <div class="provider-card__footer">
                                    <span class="panel__note">${provider.sponsorStatusLabel()}</span>
                                    <a class="text-link" href="${provider.siteUrl()}" rel="noopener noreferrer" target="_blank">Visit provider site</a>
                                </div>
                            </article>
                        @endfor
                    </div>
                </section>

                <article class="support-note support-note--provider">
                    Listings are routing support only. They are not municipal endorsement, and operators should confirm current manifest, service scope, and coverage before booking.
                </article>

                @if(page.operatorLeadPanel() != null && !page.prioritizeOperatorLeadPanel())
                    <section class="panel">
                        <div class="panel__eyebrow">${page.operatorLeadPanel().eyebrow()}</div>
                        <h2>${page.operatorLeadPanel().title()}</h2>
                        <p class="panel__note" style="margin-top: 8px;">${page.operatorLeadPanel().description()}</p>
                        @template.layout.leadForm(page.sourcePath(), page.operatorLeadPanel())
                    </section>
                @endif

                @if(page.sponsorPanel() != null)
                    <section class="panel panel--accent">
                        <div class="panel__eyebrow">${page.sponsorPanel().eyebrow()}</div>
                        <h2>${page.sponsorPanel().title()}</h2>
                        <p class="panel__note" style="margin-top: 8px;">${page.sponsorPanel().description()}</p>
                        @template.layout.leadForm(page.sourcePath(), page.sponsorPanel())
                    </section>
                @endif
            @endif

            @if(page.submissionNotice() != null)
                <article class="support-note">${page.submissionNotice().title()} - ${page.submissionNotice().body()}</article>
            @endif

            @if(page.routingDecision() != null)
                <article class="visually-hidden">${page.routingDecision().routingMode()} - ${page.routingDecision().reason()}</article>
            @endif

            <div id="official-sources">
                @template.layout.sourceStack(page.meta().lastVerified(), page.sources())
            </div>
        </div>
    </div>
</main>
