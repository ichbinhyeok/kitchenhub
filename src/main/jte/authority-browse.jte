@import owner.kitchencompliance.model.AuthorityBrowseCard
@import owner.kitchencompliance.model.AuthorityBrowseFilterOption
@import owner.kitchencompliance.model.AuthorityBrowsePageViewModel
@import owner.kitchencompliance.model.AuthorityBrowseRouteLink
@import owner.kitchencompliance.model.AuthorityBrowseSection
@import owner.kitchencompliance.model.AuthorityBrowseStateJump

@param AuthorityBrowsePageViewModel page
@param String ga4MeasurementId

@template.layout.page(meta = page.meta(), ga4MeasurementId = ga4MeasurementId, content = @`
    <main class="page-shell">
        <section class="hero hero--authority-directory">
            <p class="eyebrow">${page.eyebrow()}</p>
            <h1>${page.title()}</h1>
            <p class="hero__summary">${page.summary()}</p>
        </section>

        <section class="panel authority-directory-intro">
            <div class="panel__eyebrow">Why this surface exists</div>
            <h2>${page.detailView() ? "Authority-owned route map" : "Mixed-governance browse surface"}</h2>
            <p class="panel__note">City pages help anxious operators get started. This directory makes the true utility, fire AHJ, or department visible before anyone confuses city entry with rule ownership.</p>
        </section>

        @if(!page.detailView())
            <section class="panel authority-directory-toolbar">
                <div class="panel__eyebrow">Filter by rule holder</div>
                <div class="authority-filter-chips">
                    @for(AuthorityBrowseFilterOption option : page.filterOptions())
                        <a class="authority-chip${option.active() ? " authority-chip--active" : ""}" href="${option.path()}">${option.label()}</a>
                    @endfor
                </div>

                @if(!page.stateJumps().isEmpty())
                    <div class="authority-directory-toolbar__jump">
                        <div class="panel__eyebrow">Jump to state</div>
                        <div class="authority-state-jumps">
                            @for(AuthorityBrowseStateJump jump : page.stateJumps())
                                <a class="authority-jump" href="#${jump.anchorId()}">${jump.label()} <span>${jump.authorityCount()}</span></a>
                            @endfor
                        </div>
                    </div>
                @endif
            </section>

            @for(AuthorityBrowseSection section : page.sections())
                <section class="authority-state-section" id="${section.anchorId()}">
                    <div class="section-heading">
                        <div>
                            <div class="section-heading__eyebrow">State cluster</div>
                            <h2 class="section-heading__title">${section.stateLabel()}</h2>
                        </div>
                        <div class="section-heading__tag">${section.authorityCount()} authorities</div>
                    </div>

                    <div class="authority-directory-grid">
                        @for(AuthorityBrowseCard authority : section.authorityCards())
                            <article class="authority-directory-card">
                                <div class="authority-directory-card__header">
                                    <div>
                                        <div class="panel__eyebrow">${authority.authorityTypeLabel()}</div>
                                        <h2><a href="${authority.detailPath()}">${authority.authorityName()}</a></h2>
                                    </div>
                                    <div class="authority-directory-card__status">${authority.verificationStatusLabel()}</div>
                                </div>

                                <p class="authority-directory-card__meta">${authority.cityLabel()} | Last verified ${authority.lastVerified()}</p>

                                <div class="action-row authority-directory-card__actions">
                                    <a class="button button--secondary" href="${authority.baseUrl()}" rel="noopener noreferrer" target="_blank">Open official source</a>
                                    <a class="button button--ghost" href="${authority.contactUrl()}" rel="noopener noreferrer" target="_blank">Open contact page</a>
                                </div>

                                <div class="authority-directory-card__routes authority-directory-card__routes--desktop">
                                    @for(AuthorityBrowseRouteLink route : authority.routeLinks())
                                        <article class="authority-route-card">
                                            <h3>${route.title()}</h3>
                                            <div class="authority-route-card__actions">
                                                <a class="button button--dark" href="${route.canonicalPath()}">Authority path</a>
                                                <a class="button button--ghost" href="${route.cityEntryPath()}">City entry</a>
                                            </div>
                                        </article>
                                    @endfor
                                </div>

                                <details class="authority-route-fold">
                                    <summary>${authority.routeLinks().size()} authority routes</summary>
                                    <div class="authority-directory-card__routes authority-directory-card__routes--mobile">
                                        @for(AuthorityBrowseRouteLink route : authority.routeLinks())
                                            <article class="authority-route-card">
                                                <h3>${route.title()}</h3>
                                                <div class="authority-route-card__actions">
                                                    <a class="button button--dark" href="${route.canonicalPath()}">Authority path</a>
                                                    <a class="button button--ghost" href="${route.cityEntryPath()}">City entry</a>
                                                </div>
                                            </article>
                                        @endfor
                                    </div>
                                </details>
                            </article>
                        @endfor
                    </div>
                </section>
            @endfor
        @else
            <section class="authority-directory-grid">
                @for(AuthorityBrowseCard authority : page.authorityCards())
                    <article class="authority-directory-card">
                        <div class="authority-directory-card__header">
                            <div>
                                <div class="panel__eyebrow">${authority.authorityTypeLabel()}</div>
                                <h2><a href="${authority.detailPath()}">${authority.authorityName()}</a></h2>
                            </div>
                            <div class="authority-directory-card__status">${authority.verificationStatusLabel()}</div>
                        </div>

                        <p class="authority-directory-card__meta">${authority.cityLabel()} | Last verified ${authority.lastVerified()}</p>

                        <div class="action-row authority-directory-card__actions">
                            <a class="button button--secondary" href="${authority.baseUrl()}" rel="noopener noreferrer" target="_blank">Open official source</a>
                            <a class="button button--ghost" href="${authority.contactUrl()}" rel="noopener noreferrer" target="_blank">Open contact page</a>
                        </div>

                        <div class="authority-directory-card__routes authority-directory-card__routes--desktop">
                            @for(AuthorityBrowseRouteLink route : authority.routeLinks())
                                <article class="authority-route-card">
                                    <h3>${route.title()}</h3>
                                    <div class="authority-route-card__actions">
                                        <a class="button button--dark" href="${route.canonicalPath()}">Authority path</a>
                                        <a class="button button--ghost" href="${route.cityEntryPath()}">City entry</a>
                                    </div>
                                </article>
                            @endfor
                        </div>

                        <details class="authority-route-fold">
                            <summary>${authority.routeLinks().size()} authority routes</summary>
                            <div class="authority-directory-card__routes authority-directory-card__routes--mobile">
                                @for(AuthorityBrowseRouteLink route : authority.routeLinks())
                                    <article class="authority-route-card">
                                        <h3>${route.title()}</h3>
                                        <div class="authority-route-card__actions">
                                            <a class="button button--dark" href="${route.canonicalPath()}">Authority path</a>
                                            <a class="button button--ghost" href="${route.cityEntryPath()}">City entry</a>
                                        </div>
                                    </article>
                                @endfor
                            </div>
                        </details>
                    </article>
                @endfor
            </section>
        @endif
    </main>
`)
