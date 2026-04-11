@import owner.kitchencompliance.model.AuthorityRouteLink
@import owner.kitchencompliance.model.GuidePageViewModel
@import owner.kitchencompliance.model.GuideSection
@import owner.kitchencompliance.model.RelatedPageLink

@param GuidePageViewModel page

@template.layout.page(meta = page.meta(), content = @`
    <main class="page-shell">
        <section class="hero hero--guide">
            <p class="eyebrow">Evergreen guide</p>
            <h1>${page.title()}</h1>
            <p class="hero__summary">${page.summary()}</p>
        </section>

        <section class="guide-grid">
            @for(GuideSection section : page.sections())
                <article class="panel panel--plain">
                    <div class="panel__eyebrow">Guide section</div>
                    <h2>${section.heading()}</h2>
                    <ul class="bullet-list" style="margin-top: 16px;">
                        @for(String item : section.bullets())
                            <li>${item}</li>
                        @endfor
                    </ul>
                </article>
            @endfor
        </section>

        @if(page.authorityRoutes() != null && !page.authorityRoutes().isEmpty())
            <section class="panel guide-authority-panel" style="margin-top: 18px;">
                <div class="panel__eyebrow">Authority-first route map</div>
                <h2>City search is the entry. The rule holder is the truth.</h2>
                <p class="panel__note" style="margin-top: 10px;">Use these live local routes when the guide needs to drop into an actual utility, fire AHJ, or department workflow.</p>
                <div class="guide-authority-grid">
                    @for(AuthorityRouteLink authorityRoute : page.authorityRoutes())
                        <article class="guide-authority-card">
                            <div class="panel__eyebrow">${authorityRoute.title()}</div>
                            <h3>${authorityRoute.cityLabel()}</h3>
                            <p class="guide-authority-card__authority">${authorityRoute.authorityName()} | ${authorityRoute.authorityTypeLabel()}</p>
                            <div class="guide-authority-card__actions">
                                <a class="button button--dark" href="${authorityRoute.canonicalPath()}">Open authority page</a>
                                <a class="button button--secondary" href="${authorityRoute.cityPath()}">Open city entry</a>
                            </div>
                        </article>
                    @endfor
                </div>
            </section>
        @endif

        <section class="panel" style="margin-top: 18px;">
            <div class="panel__eyebrow">City entry links</div>
            <h2>Continue through the workflow</h2>
            <ul class="link-list" style="margin-top: 16px;">
                @for(RelatedPageLink link : page.relatedLinks())
                    <li><a href="${link.path()}">${link.label()}</a></li>
                @endfor
            </ul>
        </section>

        <div class="visually-hidden">
            <span>Austin</span>
            <span>Santa Clara</span>
            <span>Charlotte</span>
        </div>
    </main>
`)
