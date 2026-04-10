@import owner.kitchencompliance.model.CityCard
@import owner.kitchencompliance.model.HomePanelLink
@import owner.kitchencompliance.model.HomePageViewModel
@import owner.kitchencompliance.model.RelatedPageLink

@param HomePageViewModel page

@template.layout.page(meta = page.meta(), content = @`
    <main class="page-shell page-shell--home">
        <p class="visually-hidden">${page.title()}</p>
        <section class="home-hero">
            <div class="home-hero__copy">
                <div class="eyebrow-pill">
                    <span class="material-symbols-outlined">verified</span>
                    Verified municipal data for kitchen operators
                </div>

                <h1>Choose your city. Check the rule. Keep the proof on site.</h1>
                <p class="home-hero__summary">
                    Start from the local authority that governs grease, hood, or fire inspection work. Verify the rule, stage the records, and only then route service if the site still needs help.
                </p>

                <div class="action-row">
                    <a class="button button--primary" href="#active-cities">
                        Choose Your City
                        <span class="material-symbols-outlined">arrow_forward</span>
                    </a>
                    <a class="button button--secondary" href="#operator-start">How It Works</a>
                </div>
            </div>

            <div class="home-hero__media">
                <div class="home-hero__frame">
                    <img alt="Commercial kitchen" class="home-hero__image" src="/assets/home-kitchen.png">
                </div>
                <div class="status-card">
                    <div class="status-card__eyebrow">Operator start</div>
                    <div class="status-card__title">City -> Rule -> Proof -> Service</div>
                    <p class="status-card__body">Use vendor routing only after the local requirement and on-site paperwork burden are clear.</p>
                </div>
            </div>
        </section>

        <section class="home-section" id="operator-start">
            <div class="section-heading">
                <div>
                    <div class="section-heading__eyebrow">First-visit operator flow</div>
                    <h2 class="section-heading__title">Start With The Issue On Your Desk</h2>
                </div>
                <div class="section-heading__summary">Most operators arrive with one urgent problem: grease routing, hood paperwork, or inspection prep. Start there before picking a vendor.</div>
            </div>

            <div class="home-start-grid">
                <a class="home-start-card" href="/guides/fog-vs-grease-trap-cleaning">
                    <span class="material-symbols-outlined">delete_outline</span>
                    <h3>Grease, hauling, and manifests</h3>
                    <p>Use this when the question is grease traps, pump-outs, Austin Water hauler rules, or liquid waste manifests.</p>
                </a>

                <a class="home-start-card" href="/guides/how-often-clean-commercial-hood">
                    <span class="material-symbols-outlined">local_fire_department</span>
                    <h3>Hood, suppression, and tags</h3>
                    <p>Use this when the site needs hood-cleaning paperwork, visible tags, or fire-system inspection context.</p>
                </a>

                <a class="home-start-card" href="/guides/what-records-restaurant-inspections-check">
                    <span class="material-symbols-outlined">fact_check</span>
                    <h3>Inspection-ready records</h3>
                    <p>Use this when an inspector is coming soon and the team needs to know exactly what must be staged on site.</p>
                </a>
            </div>
        </section>

        <section class="home-section home-section--band" id="active-cities">
            <div class="section-heading">
                <div>
                    <div class="section-heading__eyebrow">Verified local code for commercial operators</div>
                    <h2 class="section-heading__title">Active City Jurisdictions</h2>
                    <div class="section-heading__summary">Choose the city first, then open the exact local route you need for grease, hood, or inspection prep.</div>
                </div>
                <div class="section-heading__tag">Live Database</div>
            </div>

            <div class="city-grid city-grid--home">
                @for(CityCard city : page.cityCards())
                    <article class="city-card city-card--home">
                        <div class="city-card__state">${city.state()}</div>
                        <div class="city-card__title">${city.city()}</div>
                        <p class="city-card__summary">${city.summary()}</p>

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
                    <h2 class="section-heading__title">Why operators use it</h2>
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
                        Evergreen Guides
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
