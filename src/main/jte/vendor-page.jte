@import owner.kitchencompliance.model.GuideSection
@import owner.kitchencompliance.model.RelatedPageLink
@import owner.kitchencompliance.model.VendorLandingPageViewModel

@param VendorLandingPageViewModel page
@param String ga4MeasurementId

@template.layout.page(meta = page.meta(), ga4MeasurementId = ga4MeasurementId, content = @`
    <main class="page-shell vendor-bridge-page">
        <section class="hero hero--guide vendor-bridge-hero">
            <div class="vendor-bridge-hero__copy">
                <p class="eyebrow">${page.eyebrow()}</p>
                <h1>${page.title()}</h1>
                <p class="hero__summary">${page.summary()}</p>

                <ul class="bullet-list vendor-bridge-bullets">
                    @for(String item : page.heroBullets())
                        <li>${item}</li>
                    @endfor
                </ul>

                @if(!page.actionLinks().isEmpty())
                    <div class="action-row vendor-bridge-hero__actions">
                        <a class="button button--accent" href="${page.actionLinks().get(0).path()}">${page.actionLinks().get(0).label()}</a>
                    </div>
                @endif
            </div>

            @if(page.actionLinks().size() > 1)
                <aside class="vendor-bridge-hero__rail">
                    <div class="vendor-bridge-hero__rail-head">
                        <div class="panel__eyebrow">Quick start</div>
                        <div class="vendor-bridge-hero__rail-mark">Today</div>
                    </div>
                    <div class="vendor-bridge-quick-links">
                        @for(int i = 1; i < page.actionLinks().size(); i++)
                            <a class="vendor-bridge-quick-link" href="${page.actionLinks().get(i).path()}">
                                <strong>${page.actionLinks().get(i).label()}</strong>
                                <span>Open the page you would actually send or reference.</span>
                            </a>
                        @endfor
                    </div>
                </aside>
            @endif
        </section>

        <details class="mobile-drawer-group vendor-bridge-more" data-mobile-collapsible open>
            <summary>
                <div>
                    <div class="panel__eyebrow">More details</div>
                    <div class="mobile-drawer-group__title">When to use it, what you send, and the city rule pages</div>
                </div>
                <span class="mobile-drawer-group__toggle" aria-hidden="true"></span>
            </summary>
            <div class="mobile-drawer-group__body">
                <section class="grid-two vendor-bridge-grid">
                    @for(GuideSection section : page.sections())
                        <article class="panel panel--plain vendor-bridge-card">
                            <div class="panel__eyebrow">${page.eyebrow()}</div>
                            <h2>${section.heading()}</h2>
                            <ul class="bullet-list" style="margin-top: 16px;">
                                @for(String item : section.bullets())
                                    <li>${item}</li>
                                @endfor
                            </ul>
                        </article>
                    @endfor
                </section>

                <section class="panel vendor-bridge-links" style="margin-top: 18px;">
                    <div class="panel__eyebrow">City rule pages</div>
                    <h2>Use the free report first, then open the local rule page only when the customer asks for records</h2>
                    <ul class="link-list" style="margin-top: 16px;">
                        @for(RelatedPageLink link : page.wedgeLinks())
                            <li><a href="${link.path()}">${link.label()}</a></li>
                        @endfor
                    </ul>
                </section>
            </div>
        </details>
    </main>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const mobileQuery = window.matchMedia('(max-width: 760px)');

            function collapseMobileSections() {
                if (!mobileQuery.matches) {
                    return;
                }
                document.querySelectorAll('[data-mobile-collapsible]').forEach(function (element) {
                    element.open = false;
                });
            }

            collapseMobileSections();
            if (mobileQuery.addEventListener) {
                mobileQuery.addEventListener('change', function (event) {
                    if (event.matches) {
                        collapseMobileSections();
                    }
                });
            }
        });
    </script>
`)
