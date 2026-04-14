@import owner.kitchencompliance.model.OperatorToolPageViewModel
@import owner.kitchencompliance.model.OperatorToolPageViewModel.DownloadLink
@import owner.kitchencompliance.model.RelatedPageLink

@param OperatorToolPageViewModel page
@param String ga4MeasurementId

@template.layout.page(meta = page.meta(), ga4MeasurementId = ga4MeasurementId, content = @`
    <main class="page-shell">
        <section class="hero hero--tool">
            <p class="eyebrow">${page.eyebrow()}</p>
            <h1>${page.title()}</h1>
            <p class="hero__summary">${page.summary()}</p>
        </section>

        <section class="grid-two">
            <article class="panel panel--plain">
                <div class="panel__eyebrow">How to use it</div>
                <h2>Operator workflow</h2>
                <ul class="bullet-list" style="margin-top: 16px;">
                    @for(String item : page.checklist())
                        <li>${item}</li>
                    @endfor
                </ul>
            </article>

            <article class="panel panel--accent">
                <div class="panel__eyebrow">Downloads</div>
                <h2>Operator worksheet files</h2>
                <ul class="link-list" style="margin-top: 16px;">
                    @for(DownloadLink download : page.downloads())
                        <li>
                            <a href="${download.path()}">${download.label()}</a>
                            <div class="panel__note">${download.note()}</div>
                        </li>
                    @endfor
                </ul>
            </article>
        </section>

        <section class="panel" style="margin-top: 18px;">
            <div class="panel__eyebrow">Related local pages</div>
            <h2>Use the tool after the rule is clear</h2>
            <ul class="link-list" style="margin-top: 16px;">
                @for(RelatedPageLink link : page.relatedLinks())
                    <li><a href="${link.path()}">${link.label()}</a></li>
                @endfor
            </ul>
        </section>
    </main>
`)
