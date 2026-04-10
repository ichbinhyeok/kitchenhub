@import owner.kitchencompliance.model.AdminBreakdownRow
@import owner.kitchencompliance.model.AdminPageViewModel
@import owner.kitchencompliance.model.AdminRecentEvent
@import owner.kitchencompliance.model.AdminRecentLead
@import owner.kitchencompliance.model.DeployReadinessWatchRow
@import owner.kitchencompliance.model.FreshnessWatchRow
@import owner.kitchencompliance.model.SourceQualityWatchRow

@param AdminPageViewModel page

@template.layout.page(meta = page.meta(), content = @`
    <main class="page-shell">
        <section class="hero">
            <p class="eyebrow">Internal ops</p>
            <h1>${page.title()}</h1>
            <p class="hero__summary">${page.summary()}</p>
        </section>

        <section class="panel">
            <div class="panel__eyebrow">Storage</div>
            <h2>Persistent ops files</h2>
            <p><strong>${page.dashboard().storagePath()}</strong></p>
            <p class="panel__note">${page.dashboard().storageGuidance()}</p>
            <p><strong>${page.leads().storagePath()}</strong></p>
            <p class="panel__note">${page.leads().storageGuidance()}</p>
            <ul class="link-list">
                <li><a href="/admin/exports/attribution-events.csv">Download raw attribution CSV</a></li>
                <li><a href="/admin/exports/attribution-summary.csv">Download attribution summary CSV</a></li>
                <li><a href="/admin/exports/lead-intake.csv">Download raw lead CSV</a></li>
                <li><a href="/admin/exports/lead-summary.csv">Download lead summary CSV</a></li>
                <li><a href="/admin/exports/freshness-watch.csv">Download freshness watch CSV</a></li>
                <li><a href="/admin/exports/source-quality-watch.csv">Download source quality watch CSV</a></li>
                <li><a href="/admin/exports/deploy-readiness.csv">Download deploy readiness CSV</a></li>
                <li><a href="/admin/exports/operator-utility-summary.csv">Download operator utility summary CSV</a></li>
                <li><a href="/admin/exports/evidence-index.csv">Download evidence index CSV</a></li>
                <li><a href="/admin/exports/ops-alerts.md">Download ops alerts Markdown</a></li>
            </ul>
        </section>

        <section class="panel">
            <div class="panel__eyebrow">Freshness watch</div>
            <h2>Indexed route review status</h2>
            <div class="metric-grid">
                <article class="metric-card">
                    <div class="panel__eyebrow">Indexed routes</div>
                    <div class="metric-card__value">${page.freshness().indexedRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Fresh</div>
                    <div class="metric-card__value">${page.freshness().freshRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Review soon</div>
                    <div class="metric-card__value">${page.freshness().dueSoonRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Stale</div>
                    <div class="metric-card__value">${page.freshness().staleRoutes()}</div>
                </article>
            </div>
            <p class="panel__note">Next source review due: ${page.freshness().nextReviewDue()}</p>
            <div class="admin-events">
                @for(FreshnessWatchRow row : page.freshness().watchRows())
                    <article class="admin-event">
                        <p class="admin-event__meta">${row.cityLabel()} | ${row.pageLabel()} | ${row.statusLabel()}</p>
                        <p><strong>${row.path()}</strong></p>
                        <p class="admin-event__path">Next review: ${row.nextReviewOn()}</p>
                        <p class="admin-event__path">${row.note()}</p>
                    </article>
                @endfor
            </div>
        </section>

        <section class="panel">
            <div class="panel__eyebrow">Source quality</div>
            <h2>Indexed route source depth</h2>
            <div class="metric-grid">
                <article class="metric-card">
                    <div class="panel__eyebrow">Indexed routes</div>
                    <div class="metric-card__value">${page.sourceQuality().indexedRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Strong</div>
                    <div class="metric-card__value">${page.sourceQuality().strongRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Adequate</div>
                    <div class="metric-card__value">${page.sourceQuality().adequateRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Thin</div>
                    <div class="metric-card__value">${page.sourceQuality().thinRoutes()}</div>
                </article>
            </div>
            <p class="panel__note">Critical source quality issues should be zero for deploy-safe indexed routes.</p>
            <div class="admin-events">
                @for(SourceQualityWatchRow row : page.sourceQuality().watchRows())
                    <article class="admin-event">
                        <p class="admin-event__meta">${row.cityLabel()} | ${row.pageLabel()} | ${row.statusLabel()}</p>
                        <p><strong>${row.path()}</strong></p>
                        <p class="admin-event__path">Sources: ${row.totalSources()} total / ${row.strongSources()} strong</p>
                        <p class="admin-event__path">${row.note()}</p>
                    </article>
                @endfor
            </div>
        </section>

        <section class="panel">
            <div class="panel__eyebrow">Deploy readiness</div>
            <h2>Pre-deploy route gate</h2>
            <div class="metric-grid">
                <article class="metric-card">
                    <div class="panel__eyebrow">Indexed routes</div>
                    <div class="metric-card__value">${page.deployReadiness().indexedRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Ready</div>
                    <div class="metric-card__value">${page.deployReadiness().readyRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Watch</div>
                    <div class="metric-card__value">${page.deployReadiness().watchRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Blocked</div>
                    <div class="metric-card__value">${page.deployReadiness().blockedRoutes()}</div>
                </article>
            </div>
            <p class="panel__note">Blocked routes should be zero before deploy. Watch routes are live but need source or provider follow-up.</p>
            <div class="admin-events">
                @for(DeployReadinessWatchRow row : page.deployReadiness().watchRows())
                    <article class="admin-event">
                        <p class="admin-event__meta">${row.cityLabel()} | ${row.pageLabel()} | ${row.statusLabel()} | ${row.indexableLabel()}</p>
                        <p><strong>${row.path()}</strong></p>
                        <p class="admin-event__path">Next review: ${row.nextReviewOn()}</p>
                        <p class="admin-event__path">Sources: ${row.totalSources()} total / ${row.strongSources()} strong</p>
                        <p class="admin-event__path">Providers: ${row.renderableProviders()} renderable / ${row.authorityBackedProviders()} authority-backed</p>
                        <p class="admin-event__path">${row.note()}</p>
                    </article>
                @endfor
            </div>
        </section>

        <section class="metric-grid">
            <article class="metric-card">
                <div class="panel__eyebrow">Total leads</div>
                <div class="metric-card__value">${page.leads().totalLeads()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">Operator requests</div>
                <div class="metric-card__value">${page.leads().operatorRequests()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">Sponsor inquiries</div>
                <div class="metric-card__value">${page.leads().sponsorInquiries()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">Consented leads</div>
                <div class="metric-card__value">${page.leads().consentedLeads()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">Page views</div>
                <div class="metric-card__value">${page.dashboard().pageViewEvents()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">Utility views</div>
                <div class="metric-card__value">${page.dashboard().utilityViewEvents()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">Total events</div>
                <div class="metric-card__value">${page.dashboard().totalEvents()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">Provider outbound</div>
                <div class="metric-card__value">${page.dashboard().providerClicks()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">CTA clicks</div>
                <div class="metric-card__value">${page.dashboard().ctaClicks()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">Sponsored clicks</div>
                <div class="metric-card__value">${page.dashboard().sponsoredClicks()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">Returning utility visitors</div>
                <div class="metric-card__value">${page.dashboard().returningUtilityVisitors()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">Utility revisit rate</div>
                <div class="metric-card__value">${page.dashboard().utilityRevisitRateLabel()}</div>
            </article>
        </section>

        @if(page.dashboard().hasEvents())
            <section class="panel">
                <div class="panel__eyebrow">Latest event</div>
                <h2>${page.dashboard().lastCapturedAt()}</h2>
            </section>

            <section class="grid-two">
                <article class="panel">
                    <div class="panel__eyebrow">By city</div>
                    <h2>Top local demand</h2>
                    <ul class="admin-breakdown">
                        @for(AdminBreakdownRow row : page.dashboard().cityBreakdown())
                            <li><span>${row.label()}</span><strong>${row.count()}</strong></li>
                        @endfor
                    </ul>
                </article>
                <article class="panel">
                    <div class="panel__eyebrow">By page family</div>
                    <h2>Where clicks start</h2>
                    <ul class="admin-breakdown">
                        @for(AdminBreakdownRow row : page.dashboard().pageFamilyBreakdown())
                            <li><span>${row.label()}</span><strong>${row.count()}</strong></li>
                        @endfor
                    </ul>
                </article>
            </section>

            <section class="panel">
                <div class="panel__eyebrow">Verdict states</div>
                <h2>What state the route resolved to</h2>
                <ul class="admin-breakdown">
                    @for(AdminBreakdownRow row : page.dashboard().verdictBreakdown())
                        <li><span>${row.label()}</span><strong>${row.count()}</strong></li>
                    @endfor
                </ul>
            </section>

            <section class="panel">
                <div class="panel__eyebrow">Top destinations</div>
                <h2>What operators click into</h2>
                <ul class="admin-breakdown">
                    @for(AdminBreakdownRow row : page.dashboard().destinationBreakdown())
                        <li><span>${row.label()}</span><strong>${row.count()}</strong></li>
                    @endfor
                </ul>
            </section>

            <section class="panel">
                <div class="panel__eyebrow">Recent events</div>
                <h2>Latest attribution rows</h2>
                <div class="admin-events">
                    @for(AdminRecentEvent event : page.dashboard().recentEvents())
                        <article class="admin-event">
                            <p class="admin-event__meta">${event.capturedAt()} | ${event.eventType()} | ${event.sponsoredLabel()}</p>
                            <p><strong>${event.location()}</strong> via ${event.pageFamily()}</p>
                            <p class="admin-event__path">Verdict: ${event.verdictState()}</p>
                            <p class="admin-event__path">Source: ${event.sourcePath()}</p>
                            <p class="admin-event__path">Destination: ${event.destination()}</p>
                        </article>
                    @endfor
                </div>
            </section>
        @else
            <section class="panel">
                <div class="panel__eyebrow">Status</div>
                <h2>No attribution events yet</h2>
                <p class="panel__note">The file path is ready. The dashboard will populate after the first CTA or provider outbound click is recorded.</p>
            </section>
        @endif

        @if(page.leads().hasLeads())
            <section class="grid-two">
                <article class="panel">
                    <div class="panel__eyebrow">Leads by city</div>
                    <h2>Where operator demand appears</h2>
                    <ul class="admin-breakdown">
                        @for(AdminBreakdownRow row : page.leads().cityBreakdown())
                            <li><span>${row.label()}</span><strong>${row.count()}</strong></li>
                        @endfor
                    </ul>
                </article>
                <article class="panel">
                    <div class="panel__eyebrow">Lead types</div>
                    <h2>Operator vs sponsor mix</h2>
                    <ul class="admin-breakdown">
                        @for(AdminBreakdownRow row : page.leads().leadTypeBreakdown())
                            <li><span>${row.label()}</span><strong>${row.count()}</strong></li>
                        @endfor
                    </ul>
                </article>
            </section>

            <section class="panel">
                <div class="panel__eyebrow">Lead intents</div>
                <h2>What local buyers ask for</h2>
                <ul class="admin-breakdown">
                    @for(AdminBreakdownRow row : page.leads().providerIntentBreakdown())
                        <li><span>${row.label()}</span><strong>${row.count()}</strong></li>
                    @endfor
                </ul>
            </section>

            <section class="panel">
                <div class="panel__eyebrow">Recent leads</div>
                <h2>Latest intake rows</h2>
                <div class="admin-events">
                    @for(AdminRecentLead lead : page.leads().recentLeads())
                        <article class="admin-event">
                            <p class="admin-event__meta">${lead.capturedAt()} | ${lead.leadType()} | ${lead.consentLabel()}</p>
                            <p><strong>${lead.location()}</strong> | ${lead.providerIntent()}</p>
                            <p class="admin-event__path">${lead.contact()}</p>
                            <p class="admin-event__path">Source: ${lead.sourcePath()}</p>
                        </article>
                    @endfor
                </div>
            </section>
        @else
            <section class="panel">
                <div class="panel__eyebrow">Lead status</div>
                <h2>No leads recorded yet</h2>
                <p class="panel__note">Service requests and sponsor inquiries will populate here after the first form submission.</p>
            </section>
        @endif
    </main>
`)
