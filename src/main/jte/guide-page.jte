@import owner.kitchencompliance.model.AuthorityRouteLink
@import owner.kitchencompliance.model.GuidePageViewModel
@import owner.kitchencompliance.model.GuideSection
@import owner.kitchencompliance.model.RelatedPageLink

@param GuidePageViewModel page
@param String ga4MeasurementId

@template.layout.page(meta = page.meta(), ga4MeasurementId = ga4MeasurementId, content = @`
    <main class="page-shell">
        <section class="hero hero--guide">
            <p class="eyebrow">Practical guide</p>
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
                <div class="panel__eyebrow">Local pages to open next</div>
                <h2>Use a local rule page before you book service</h2>
                <p class="panel__note" style="margin-top: 10px;">Open the matching rule page first, then use the city page if that is the easier way to get back later.</p>
                <div class="guide-authority-grid">
                    @for(AuthorityRouteLink authorityRoute : page.authorityRoutes())
                        <article class="guide-authority-card">
                            <div class="panel__eyebrow">${authorityRoute.title()}</div>
                            <h3>${authorityRoute.cityLabel()}</h3>
                            <p class="guide-authority-card__authority">${authorityRoute.authorityName()} | ${authorityRoute.authorityTypeLabel()}</p>
                            <div class="guide-authority-card__actions">
                                <a class="button button--dark" href="${authorityRoute.canonicalPath()}">Open rule page</a>
                                <a class="button button--secondary" href="${authorityRoute.cityPath()}">Open city page</a>
                            </div>
                        </article>
                    @endfor
                </div>
            </section>
        @endif

        <section class="panel" style="margin-top: 18px;">
            <div class="panel__eyebrow">Local rule routes</div>
            <h2>Keep going with a local page</h2>
            <p class="panel__note" style="margin-top: 10px;">Use these links to move from the guide into the matching city or rule page.</p>
            <ul class="link-list" style="margin-top: 16px;">
                @for(RelatedPageLink link : page.relatedLinks())
                    <li><a href="${link.path()}">${link.label()}</a></li>
                @endfor
            </ul>
        </section>
    </main>
`)
