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

        <section class="panel" style="margin-top: 18px;">
            <div class="panel__eyebrow">Related links</div>
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
