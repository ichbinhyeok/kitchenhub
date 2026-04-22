@import owner.kitchencompliance.model.OperatorToolPageViewModel
@import owner.kitchencompliance.model.OperatorToolPageViewModel.DownloadLink
@import owner.kitchencompliance.model.RelatedPageLink

@param OperatorToolPageViewModel page
@param String ga4MeasurementId

@template.layout.page(meta = page.meta(), ga4MeasurementId = ga4MeasurementId, content = @`
    <main class="page-shell tool-page">
        <section class="hero hero--tool">
            <p class="eyebrow">${page.eyebrow()}</p>
            <h1>${page.title()}</h1>
            <p class="hero__summary">${page.summary()}</p>
        </section>

        <div class="content-grid">
            <section class="panel panel--plain">
                <div class="panel__eyebrow">Use this worksheet</div>
                <h2>What to fill out first</h2>
                <ul class="bullet-list" style="margin-top: 16px;">
                    @for(String item : page.checklist())
                        <li>${item}</li>
                    @endfor
                </ul>
            </section>

            <section class="panel">
                <div class="panel__eyebrow">Downloads</div>
                <h2>Get the template</h2>
                <div class="link-stack" style="margin-top: 16px;">
                    @for(DownloadLink download : page.downloads())
                        <a class="link-card" href="${download.path()}">
                            <strong>${download.label()}</strong>
                            <span>${download.note()}</span>
                        </a>
                    @endfor
                </div>
            </section>
        </div>

        @if(page.relatedLinks() != null && !page.relatedLinks().isEmpty())
            <section class="panel" style="margin-top: 18px;">
                <div class="panel__eyebrow">Local rule pages</div>
                <h2>Open the matching authority page</h2>
                <div class="link-stack" style="margin-top: 16px;">
                    @for(RelatedPageLink link : page.relatedLinks())
                        <a class="link-card" href="${link.path()}">
                            <strong>${link.label()}</strong>
                            <span>Use the local rule page before you rely on the worksheet alone.</span>
                        </a>
                    @endfor
                </div>
            </section>
        @endif
    </main>
`)
