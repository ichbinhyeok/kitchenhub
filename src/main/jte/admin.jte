@import owner.kitchencompliance.model.AdminBreakdownRow
@import owner.kitchencompliance.model.AdminPageViewModel
@import owner.kitchencompliance.model.AdminRecentEvent
@import owner.kitchencompliance.model.AdminRecentLead
@import owner.kitchencompliance.model.DeployReadinessWatchRow
@import owner.kitchencompliance.model.FreshnessWatchRow
@import owner.kitchencompliance.model.NoindexPromotionRow
@import owner.kitchencompliance.model.SearchDemandWatchRow
@import owner.kitchencompliance.model.SourceQualityWatchRow

@param AdminPageViewModel page
@param String ga4MeasurementId

@template.layout.page(meta = page.meta(), ga4MeasurementId = ga4MeasurementId, content = @`
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
                <li><a href="/admin/exports/noindex-promotion-queue.csv">Download noindex promotion queue CSV</a></li>
                <li><a href="/admin/exports/search-demand-watch.csv">Download search demand watch CSV</a></li>
                <li><a href="/admin/exports/operator-utility-summary.csv">Download operator utility summary CSV</a></li>
                <li><a href="/admin/exports/evidence-index.csv">Download evidence index CSV</a></li>
                <li><a href="/admin/exports/ops-alerts.md">Download ops alerts Markdown</a></li>
            </ul>
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
                <div class="panel__eyebrow">Tool actions</div>
                <div class="metric-card__value">${page.dashboard().toolActionEvents()}</div>
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
                <div class="panel__eyebrow">Utility revisit rate</div>
                <div class="metric-card__value">${page.dashboard().utilityRevisitRateLabel()}</div>
            </article>
            <article class="metric-card">
                <div class="panel__eyebrow">Total events</div>
                <div class="metric-card__value">${page.dashboard().totalEvents()}</div>
            </article>
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

        <section class="panel">
            <div class="panel__eyebrow">Noindex promotion queue</div>
            <h2>Queued routes that should not be forgotten</h2>
            <div class="metric-grid">
                <article class="metric-card">
                    <div class="panel__eyebrow">Queued routes</div>
                    <div class="metric-card__value">${page.noindexPromotionQueue().queuedRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Ready now</div>
                    <div class="metric-card__value">${page.noindexPromotionQueue().readyToPromoteRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Due soon</div>
                    <div class="metric-card__value">${page.noindexPromotionQueue().dueSoonRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Overdue</div>
                    <div class="metric-card__value">${page.noindexPromotionQueue().overdueRoutes()}</div>
                </article>
            </div>
            @if(page.noindexPromotionQueue().rows().isEmpty())
                <p class="panel__note">No noindex routes are currently queued for promotion.</p>
            @else
                <div class="admin-events">
                    @for(NoindexPromotionRow row : page.noindexPromotionQueue().rows())
                        <article class="admin-event">
                            <p class="admin-event__meta">${row.cityLabel()} | ${row.pageLabel()} | ${row.statusLabel()} | ${row.readinessLabel()}</p>
                            <p><strong>${row.path()}</strong></p>
                            <p class="admin-event__path">Next review: ${row.nextReviewOn()}</p>
                            <p class="admin-event__path">Reason: ${row.reason()}</p>
                            <p class="admin-event__path">Sources: ${row.totalSources()} total / ${row.strongSources()} strong</p>
                        </article>
                    @endfor
                </div>
            @endif
        </section>

        <section class="panel">
            <div class="panel__eyebrow">Search Console demand</div>
            <h2>Imported route demand snapshot</h2>
            <div class="metric-grid">
                <article class="metric-card">
                    <div class="panel__eyebrow">Tracked routes</div>
                    <div class="metric-card__value">${page.searchDemand().trackedRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Promotion demand</div>
                    <div class="metric-card__value">${page.searchDemand().promotionDemandRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">CTR work</div>
                    <div class="metric-card__value">${page.searchDemand().ctrWorkRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Discoverability work</div>
                    <div class="metric-card__value">${page.searchDemand().discoverabilityWorkRoutes()}</div>
                </article>
                <article class="metric-card">
                    <div class="panel__eyebrow">Monitor</div>
                    <div class="metric-card__value">${page.searchDemand().monitorRoutes()}</div>
                </article>
            </div>
            <div class="admin-events">
                @for(SearchDemandWatchRow row : page.searchDemand().rows())
                    <article class="admin-event">
                        <p class="admin-event__meta">${row.cityLabel()} | ${row.pageLabel()} | ${row.statusLabel()} | ${row.routeStateLabel()}</p>
                        <p><strong>${row.path()}</strong></p>
                        <p class="admin-event__path">Top query: ${row.topQuery()}</p>
                        <p class="admin-event__path">Metrics: ${row.impressions28d()} impressions / ${row.clicks28d()} clicks / ${row.ctrLabel()} CTR / avg pos ${row.averagePositionLabel()}</p>
                        <p class="admin-event__path">Captured: ${row.capturedOn()}</p>
                        <p class="admin-event__path">${row.note()}</p>
                    </article>
                @endfor
            </div>
        </section>

        <div class="content-grid">
            <section class="panel">
                <div class="panel__eyebrow">Lead intake</div>
                <h2>Recent routing requests</h2>
                @if(page.leads().recentLeads().isEmpty())
                    <p class="panel__note">No leads recorded yet</p>
                @else
                    <div class="admin-events">
                        @for(AdminRecentLead lead : page.leads().recentLeads())
                            <article class="admin-event">
                                <p class="admin-event__meta">${lead.capturedAt()} | ${lead.leadType()} | ${lead.location()}</p>
                                <p><strong>${lead.contact()}</strong></p>
                                <p class="admin-event__path">${lead.providerIntent()} | ${lead.consentLabel()}</p>
                                <p class="admin-event__path">${lead.sourcePath()}</p>
                            </article>
                        @endfor
                    </div>
                @endif
            </section>

            <section class="panel">
                <div class="panel__eyebrow">Attribution</div>
                <h2>Recent product events</h2>
                @if(page.dashboard().recentEvents().isEmpty())
                    <p class="panel__note">No attribution events yet</p>
                @else
                    <div class="admin-events">
                        @for(AdminRecentEvent event : page.dashboard().recentEvents())
                            <article class="admin-event">
                                <p class="admin-event__meta">${event.capturedAt()} | ${event.eventType()} | ${event.location()}</p>
                                <p><strong>${event.sourcePath()}</strong></p>
                                <p class="admin-event__path">${event.destination()}</p>
                                <p class="admin-event__path">${event.pageFamily()} | ${event.verdictState()}</p>
                            </article>
                        @endfor
                    </div>
                @endif
            </section>
        </div>

        <div class="content-grid">
            <section class="panel">
                <div class="panel__eyebrow">Verdict states</div>
                <h2>What operators are landing on</h2>
                <ul class="admin-breakdown">
                    @for(AdminBreakdownRow row : page.dashboard().verdictBreakdown())
                        <li><span>${row.label()}</span><strong>${row.count()}</strong></li>
                    @endfor
                </ul>
            </section>

            <section class="panel">
                <div class="panel__eyebrow">Destinations</div>
                <h2>Where clicks and actions go</h2>
                <ul class="admin-breakdown">
                    @for(AdminBreakdownRow row : page.dashboard().destinationBreakdown())
                        <li><span>${row.label()}</span><strong>${row.count()}</strong></li>
                    @endfor
                </ul>
            </section>
        </div>
    </main>
`)
