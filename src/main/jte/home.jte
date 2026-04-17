@import owner.kitchencompliance.model.AuthorityRouteLink
@import owner.kitchencompliance.model.CityCard
@import owner.kitchencompliance.model.HomeIssueCard
@import owner.kitchencompliance.model.HomePanelLink
@import owner.kitchencompliance.model.HomePageViewModel
@import owner.kitchencompliance.model.RelatedPageLink

@param HomePageViewModel page
@param String ga4MeasurementId

@template.layout.page(meta = page.meta(), ga4MeasurementId = ga4MeasurementId, content = @`
    <main class="page-shell page-shell--home">
        <p class="visually-hidden">${page.title()}</p>
        <section class="home-hero">
            <div class="home-hero__copy">
                <div class="eyebrow-pill">
                    <span class="material-symbols-outlined">verified</span>
                    Official sources reviewed locally
                </div>

                <h1>Local compliance + operator action for commercial kitchens</h1>
                <p class="home-hero__summary">
                    Start with the issue on your desk, open the local rule page for your city, stage the proof inspectors ask for, and then move into guides, tools, or provider routing.
                </p>

                <div class="action-row">
                    <a class="button button--primary" href="#operator-start">
                        Start With The Issue
                        <span class="material-symbols-outlined">arrow_forward</span>
                    </a>
                    <a class="button button--secondary" href="#active-cities">Browse City Coverage</a>
                    <a class="button button--ghost" href="/authorities">Browse Authorities</a>
                </div>
            </div>

            <div class="home-hero__media">
                <div class="home-hero__frame">
                    <img alt="Commercial kitchen" class="home-hero__image" src="/assets/home-kitchen.png">
                </div>
                <div class="home-hero__rail">
                    <div class="status-card__eyebrow">Current city coverage</div>
                    <div class="status-card__title">Pick a city, then open the rule page you need</div>
                    <p class="status-card__body">Every city card below links into local grease, hood, inspection, and authority paths so operators can move straight to the next action.</p>
                </div>
            </div>
        </section>

        <section class="home-section" id="operator-start">
            <div class="section-heading">
                <div>
                    <div class="section-heading__eyebrow">First-visit operator flow</div>
                    <h2 class="section-heading__title">Start With The Issue On Your Desk</h2>
                </div>
                <div class="section-heading__summary">Pick the issue first, then choose the city that applies. Guides stay available as backup, not as the first stop.</div>
            </div>

            <div class="home-start-grid">
                @for(HomeIssueCard issue : page.issueCards())
                    <article class="home-start-card">
                        <span class="material-symbols-outlined">${issue.iconName()}</span>
                        <h3>${issue.title()}</h3>
                        <p>${issue.summary()}</p>

                        <div class="action-row">
                            <a class="button button--primary" href="${issue.destinationPath()}">
                                ${issue.destinationLabel()}
                                <span class="material-symbols-outlined">arrow_forward</span>
                            </a>
                            <a class="button button--ghost" href="${issue.supportPath()}">${issue.supportLabel()}</a>
                        </div>
                    </article>
                @endfor
            </div>
        </section>

        <section class="home-section home-section--band" id="active-cities">
            <div class="section-heading">
                <div>
                    <div class="section-heading__eyebrow">Current city coverage</div>
                    <h2 class="section-heading__title">Choose A City</h2>
                    <div class="section-heading__summary">Open the city that matches your kitchen, then jump into local grease rules, hood requirements, inspection checklists, or the rule holder directory.</div>
                </div>
                <a class="section-heading__tag" href="/authorities">Browse Authorities</a>
            </div>
            <div class="city-grid city-grid--home">
                @for(CityCard city : page.cityCards())
                    <article class="city-card city-card--home">
                        <div class="city-card__state">${city.state()}</div>
                        <div class="city-card__title">${city.city()}</div>
                        <p class="city-card__summary">${city.summary()}</p>

                        <div class="city-card__authorities">
                            @for(AuthorityRouteLink authorityRoute : city.authorityRoutes())
                                <a class="city-card__authority" href="${authorityRoute.canonicalPath()}">
                                    <span class="city-card__authority-label">${authorityRoute.authorityTypeLabel()}</span>
                                    <strong>${authorityRoute.authorityName()}</strong>
                                </a>
                            @endfor
                        </div>

                        <div class="city-card__links">
                            @for(RelatedPageLink link : city.primaryLinks())
                                <a href="${link.path()}">
                                    <span>${link.label()}</span>
                                    <span class="material-symbols-outlined">chevron_right</span>
                                </a>
                            @endfor
                        </div>
                    </article>
                @endfor
            </div>
        </section>

        <section class="home-section">
            <div class="section-heading">
                <div>
                    <h2 class="section-heading__title">What we solve</h2>
                </div>
            </div>

            <div class="problem-grid">
                <article class="problem-card problem-card--alert">
                    <span class="material-symbols-outlined">warning</span>
                    <h3>Wrong Authority</h3>
                    <p>${page.scopeBullets().size() > 0 ? page.scopeBullets().get(0) : page.summary()}</p>
                </article>

                <article class="problem-card">
                    <span class="material-symbols-outlined">help_outline</span>
                    <h3>Missing Proof</h3>
                    <p>${page.scopeBullets().size() > 1 ? page.scopeBullets().get(1) : page.summary()}</p>
                </article>

                <article class="problem-card problem-card--danger">
                    <span class="material-symbols-outlined">hub</span>
                    <h3>Premature Vendor Calls</h3>
                    <p>${page.scopeBullets().size() > 2 ? page.scopeBullets().get(2) : page.summary()}</p>
                </article>
            </div>
        </section>

        <section class="home-section">
            <div class="dual-panel-grid">
                <article class="panel list-panel list-panel--guides">
                    <div class="list-panel__headline">
                        <span class="material-symbols-outlined">menu_book</span>
                        Practical Guides
                    </div>
                    <div class="list-panel__items">
                        @for(HomePanelLink guide : page.guideLinks())
                            <a class="guide-link" href="${guide.path()}">
                                <span class="guide-link__title">${guide.label()}</span>
                                <span class="guide-link__summary">${guide.summary()}</span>
                            </a>
                        @endfor
                    </div>
                    <a class="text-link" href="/guides/fog-vs-grease-trap-cleaning">View All Guides</a>
                </article>

                <article class="panel list-panel list-panel--tools">
                    <div class="list-panel__headline">
                        <span class="material-symbols-outlined">construction</span>
                        Operator Tools
                    </div>
                    <div class="tool-stack">
                        @for(HomePanelLink tool : page.operatorToolLinks())
                            <a class="tool-card" href="${tool.path()}">
                                <span>
                                    <span class="tool-card__title">${tool.label()}</span>
                                    <span class="tool-card__summary">${tool.summary()}</span>
                                </span>
                                <span class="material-symbols-outlined">${tool.iconName()}</span>
                            </a>
                        @endfor
                    </div>
                    <a class="text-link" href="/tools/grease-log">View Tooling</a>
                </article>
            </div>
        </section>
    </main>
`)
