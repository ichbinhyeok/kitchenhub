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
                    For restaurant owners and kitchen managers
                </div>

                <h1>Find what is missing before inspection does</h1>
                <p class="home-hero__summary">
                    Pick the issue on your desk, open the city that matches your kitchen, and get the rule holder, missing proof, common fail points, and next action without digging through scattered local pages.
                </p>

                <div class="action-row">
                    <a class="button button--primary" href="#operator-start">
                        Start The Check
                        <span class="material-symbols-outlined">arrow_forward</span>
                    </a>
                    <a class="button button--secondary" href="#active-cities">Choose City</a>
                    <a class="button button--ghost" href="/guides/fog-vs-grease-trap-cleaning">Use A Guide</a>
                </div>
            </div>

            <div class="home-hero__media">
                <div class="home-hero__frame">
                    <img alt="Commercial kitchen" class="home-hero__image" src="/assets/home-kitchen.png">
                </div>
                <div class="home-hero__rail">
                    <div class="status-card__eyebrow">What you get fast</div>
                    <div class="status-card__title">1. Rule holder 2. Proof on site 3. Next action</div>
                    <p class="status-card__body">Use the issue cards and city links below when you need to know what applies, what is missing, and what to do next.</p>
                </div>
            </div>
        </section>

        <section class="home-section" id="operator-start">
            <div class="section-heading">
                <div>
                    <div class="section-heading__eyebrow">First-visit operator flow</div>
                    <h2 class="section-heading__title">Start With The Problem On Your Desk</h2>
                </div>
                <div class="section-heading__summary">Choose the problem first, then the city. The goal is to get to the right local page before you read around or call anyone.</div>
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
                    <div class="section-heading__summary">Open the city that matches your kitchen, then jump straight into the local rule page that tells you what is required, what proof must stay on site, and what to do next.</div>
                </div>
                <a class="section-heading__tag" href="/authorities">Need the office behind the rule?</a>
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
                    <div class="section-heading__eyebrow">Fast answer</div>
                    <h2 class="section-heading__title">What You Should Know In A Minute</h2>
                </div>
            </div>

            <div class="problem-grid">
                <article class="problem-card problem-card--alert">
                    <span class="material-symbols-outlined">warning</span>
                    <h3>Which Office Owns This</h3>
                    <p>${page.scopeBullets().size() > 0 ? page.scopeBullets().get(0) : page.summary()}</p>
                </article>

                <article class="problem-card">
                    <span class="material-symbols-outlined">help_outline</span>
                    <h3>What Proof Is Missing</h3>
                    <p>${page.scopeBullets().size() > 1 ? page.scopeBullets().get(1) : page.summary()}</p>
                </article>

                <article class="problem-card problem-card--danger">
                    <span class="material-symbols-outlined">arrow_selector_tool</span>
                    <h3>What To Do Next</h3>
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
                        Repeat-Use Worksheets
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
                    <a class="text-link" href="/tools/grease-log">Open Worksheets</a>
                </article>
            </div>
        </section>
    </main>
`)
