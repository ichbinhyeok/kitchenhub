@import owner.kitchencompliance.model.OperatorToolPageViewModel
@import owner.kitchencompliance.model.OperatorToolPageViewModel.AttachmentBundleItem
@import owner.kitchencompliance.model.OperatorToolPageViewModel.CityOption
@import owner.kitchencompliance.model.OperatorToolPageViewModel.DownloadLink
@import owner.kitchencompliance.model.OperatorToolPageViewModel.PacketItem
@import owner.kitchencompliance.model.RelatedPageLink

@param OperatorToolPageViewModel page
@param String ga4MeasurementId

@template.layout.page(meta = page.meta(), ga4MeasurementId = ga4MeasurementId, content = @`
    <main class="page-shell tool-page"
          data-tool-event-endpoint="${page.hoodServiceReportForm() != null ? "/tools/" + page.slug() + "/events" : ""}"
          data-tool-city="${page.hoodServiceReportForm() != null ? page.hoodServiceReportForm().selectedCity() : ""}">
        <section class="hero hero--tool tool-print-hide">
            <p class="eyebrow">${page.eyebrow()}</p>
            <h1>${page.title()}</h1>
            <p class="hero__summary">${page.summary()}</p>
        </section>

        @if(page.sampleReportPreview() != null)
            <section class="tool-primary-preview">
                <div class="tool-primary-preview__header tool-print-hide">
                    <div>
                        <div class="panel__eyebrow">Sendable report body</div>
                        <h2>Use this as the customer-facing handoff</h2>
                        <p class="panel__note" style="margin-top: 10px;">The vendor fills out the left side once. The customer only gets the report, email copy, and optional rule reference.</p>
                    </div>
                    <div class="tool-primary-preview__actions">
                        @if(page.hoodServiceReportForm() == null)
                            @for(DownloadLink download : page.downloads())
                                <a class="button button--secondary" href="${download.path()}">${download.label()}</a>
                            @endfor
                        @else
                            <button class="button button--secondary" type="button" data-tool-action="print_pdf">Print / PDF</button>
                        @endif
                        @if(page.activeReferenceLink() != null)
                            <a class="button button--dark"
                               href="${page.activeReferenceLink().path()}"
                               data-tool-action="open_rule_page"
                               data-tool-target="${page.activeReferenceLink().path()}">${page.activeReferenceLink().label()}</a>
                        @endif
                    </div>
                </div>

                @if(page.hoodServiceReportForm() != null)
                    <div class="tool-compose-layout">
                        <section class="panel panel--plain tool-compose-panel tool-print-hide" id="vendor-fillout">
                            <div class="panel__eyebrow">Vendor fill-out</div>
                            <h2>Build the handoff in under two minutes</h2>
                            <p class="panel__note" style="margin-top: 12px;">This stays on the vendor side. The customer-facing copy stays factual and ready to forward.</p>

                            <details class="tool-inline-drawer tool-print-hide">
                                <summary>
                                    <div>
                                        <div class="panel__eyebrow">Repeat-use shortcuts</div>
                                        <div class="mobile-drawer-group__title">Start blank, reuse a saved customer, or restore the last draft</div>
                                    </div>
                                    <span class="mobile-drawer-group__toggle" aria-hidden="true"></span>
                                </summary>
                                <div class="tool-inline-drawer__body">
                                    <div class="tool-preset-panel">
                                        <div>
                                            <div class="tool-preset-panel__title">Start faster</div>
                                            <div class="panel__note">Clear the sample job in one click, keep repeat customer setups in this browser, and only fill the fields that change on this visit.</div>
                                        </div>
                                        <div class="tool-preset-panel__actions">
                                            <button class="button button--secondary" type="button" data-quick-action="blank">Start blank closeout</button>
                                            <button class="button button--secondary" type="button" data-quick-action="copy-account">Use account name for location</button>
                                            <button class="button button--ghost" type="button" data-quick-action="clear-follow-up">No follow-up on this visit</button>
                                        </div>
                                        <div class="panel__note tool-preset-status" data-quick-start-status hidden></div>
                                    </div>

                                    <div class="tool-preset-panel">
                                        <div>
                                            <div class="tool-preset-panel__title">Saved customer setup</div>
                                            <div class="panel__note">Save one customer or location setup in this browser only. It reuses city, contact, site, and handoff defaults, but keeps work completed, proof references, and follow-up notes specific to this visit.</div>
                                        </div>
                                        <div class="lead-form__row">
                                            <label class="lead-form__field">
                                                Setup name
                                                <input type="text" data-account-preset-name placeholder="Customer | Location">
                                            </label>

                                            <label class="lead-form__field">
                                                Saved customer
                                                <select data-account-preset-select>
                                                    <option value="">Choose a saved customer setup</option>
                                                </select>
                                            </label>
                                        </div>
                                        <div class="tool-preset-panel__actions">
                                            <button class="button button--secondary" type="button" data-account-preset-action="apply">Apply saved customer</button>
                                            <button class="button button--secondary" type="button" data-account-preset-action="save">Save current customer</button>
                                            <button class="button button--ghost" type="button" data-account-preset-action="delete">Delete saved customer</button>
                                        </div>
                                        <div class="panel__note tool-preset-status" data-account-preset-status hidden></div>
                                    </div>

                                    <div class="tool-preset-panel">
                                        <div>
                                            <div class="tool-preset-panel__title">Office defaults</div>
                                            <div class="panel__note">Save vendor name and repeat wording in this browser only. No account or server storage.</div>
                                        </div>
                                        <div class="tool-preset-panel__actions">
                                            <button class="button button--secondary" type="button" data-preset-action="apply">Apply saved defaults</button>
                                            <button class="button button--secondary" type="button" data-preset-action="save">Save office defaults</button>
                                            <button class="button button--ghost" type="button" data-preset-action="clear">Clear saved defaults</button>
                                        </div>
                                        <div class="panel__note tool-preset-status" data-preset-status hidden></div>
                                    </div>

                                    <div class="tool-preset-panel">
                                        <div>
                                            <div class="tool-preset-panel__title">Recent local draft</div>
                                            <div class="panel__note">Save the current job draft in this browser only. Restore it later and keep working from the refreshed handoff.</div>
                                        </div>
                                        <div class="tool-preset-panel__actions">
                                            <button class="button button--secondary" type="button" data-draft-action="restore">Restore recent draft</button>
                                            <button class="button button--secondary" type="button" data-draft-action="save">Save current draft</button>
                                            <button class="button button--ghost" type="button" data-draft-action="clear">Clear recent draft</button>
                                        </div>
                                        <div class="panel__note tool-preset-status" data-draft-status hidden></div>
                                    </div>
                                </div>
                            </details>

                            <form action="${page.hoodServiceReportForm().actionPath()}" method="post" class="lead-form" style="margin-top: 18px;">
                                <div class="lead-form__row">
                                    <label class="lead-form__field">
                                        City
                                        <select name="city">
                                            @for(CityOption option : page.hoodServiceReportForm().cityOptions())
                                                <option value="${option.value()}" selected="${option.value().equals(page.hoodServiceReportForm().selectedCity())}">${option.label()}</option>
                                            @endfor
                                        </select>
                                    </label>

                                    <label class="lead-form__field">
                                        Service date
                                        <input type="date" name="serviceDate" value="${page.hoodServiceReportForm().serviceDate()}">
                                    </label>
                                </div>

                                <div class="lead-form__row">
                                    <label class="lead-form__field">
                                        Restaurant account
                                        <input type="text" name="customerName" value="${page.hoodServiceReportForm().customerName()}">
                                    </label>

                                    <label class="lead-form__field">
                                        Location name
                                        <input type="text" name="siteName" value="${page.hoodServiceReportForm().siteName()}">
                                    </label>
                                </div>

                                <div class="lead-form__row">
                                    <label class="lead-form__field">
                                        Recipient name
                                        <input type="text" name="recipientName" value="${page.hoodServiceReportForm().recipientName()}">
                                    </label>

                                    <label class="lead-form__field">
                                        Recipient email
                                        <input type="email" name="recipientEmail" value="${page.hoodServiceReportForm().recipientEmail()}">
                                    </label>
                                </div>

                                <label class="lead-form__field">
                                    Site address
                                    <input type="text" name="siteAddress" value="${page.hoodServiceReportForm().siteAddress()}">
                                </label>

                                <div class="lead-form__row">
                                    <label class="lead-form__field">
                                        Vendor
                                        <input type="text" name="vendorName" value="${page.hoodServiceReportForm().vendorName()}">
                                    </label>

                                    <label class="lead-form__field">
                                        Crew or tech
                                        <input type="text" name="crewOrTechnician" value="${page.hoodServiceReportForm().crewOrTechnician()}">
                                    </label>
                                </div>

                                <div class="lead-form__row">
                                    <label class="lead-form__field">
                                        Work order
                                        <input type="text" name="workOrderReference" value="${page.hoodServiceReportForm().workOrderReference()}">
                                    </label>

                                    <label class="lead-form__field">
                                        Next service date
                                        <input type="date" name="nextServiceDate" value="${page.hoodServiceReportForm().nextServiceDate()}">
                                    </label>
                                </div>

                                <label class="lead-form__field">
                                    Systems serviced
                                    <input type="text" name="systemsServiced" value="${page.hoodServiceReportForm().systemsServiced()}">
                                </label>

                                <label class="lead-form__field">
                                    Work completed
                                    <textarea name="completedWork" rows="5">${page.hoodServiceReportForm().completedWork()}</textarea>
                                </label>

                                <details class="tool-inline-drawer tool-print-hide">
                                    <summary>
                                        <div>
                                            <div class="panel__eyebrow">Optional proof labels</div>
                                            <div class="mobile-drawer-group__title">Add internal file names or local proof photos only if this send needs them</div>
                                        </div>
                                        <span class="mobile-drawer-group__toggle" aria-hidden="true"></span>
                                    </summary>
                                    <div class="tool-inline-drawer__body">
                                        <div class="lead-form__row">
                                            <label class="lead-form__field">
                                                Photo reference (optional)
                                                <input type="text" name="photoReference" placeholder="Optional internal photo set name" value="${page.hoodServiceReportForm().photoReference()}">
                                            </label>

                                            <label class="lead-form__field">
                                                Report reference (optional)
                                                <input type="text" name="reportReference" placeholder="Optional invoice or report number" value="${page.hoodServiceReportForm().reportReference()}">
                                            </label>
                                        </div>
                                        <div class="tool-preset-panel__actions">
                                            <button class="button button--ghost" type="button" data-reference-suggestion-action="photo">Use suggested photo label</button>
                                            <button class="button button--ghost" type="button" data-reference-suggestion-action="report">Use suggested report label</button>
                                        </div>
                                        <p class="panel__note">
                                            Suggested photo label: <strong data-photo-reference-suggestion></strong><br>
                                            Suggested report label: <strong data-report-reference-suggestion></strong>
                                        </p>
                                        <p class="panel__note" data-reference-suggestion-status hidden></p>
                                        <p class="panel__note" style="margin-top: -4px;">Leave these blank if the files are already attached and the customer does not need your internal file names.</p>

                                        <div class="tool-local-upload">
                                            <div>
                                                <div class="tool-preset-panel__title">Local proof photos</div>
                                                <div class="panel__note">Add before and after images from this device only when you want the same browser tab to print them into the PDF closeout.</div>
                                            </div>
                                            <label class="lead-form__field">
                                                Photos for this closeout
                                                <input type="file" accept="image/*" multiple data-photo-upload>
                                            </label>
                                            <div class="tool-preset-panel__actions">
                                                <button class="button button--ghost" type="button" data-photo-action="clear">Clear local photos</button>
                                            </div>
                                            <div class="panel__note tool-photo-upload__status" data-photo-upload-status hidden></div>
                                            <div class="tool-photo-gallery tool-print-hide" data-photo-gallery-editor hidden></div>
                                        </div>
                                    </div>
                                </details>

                                <label class="lead-form__field">
                                    Customer handoff note
                                    <input type="text" name="customerHandoffNote" value="${page.hoodServiceReportForm().customerHandoffNote()}">
                                </label>

                                <label class="lead-form__field">
                                    Follow-up items
                                    <textarea name="followUpItems" rows="4">${page.hoodServiceReportForm().followUpItems()}</textarea>
                                </label>

                                <div class="lead-form__row" data-follow-up-detail-row>
                                    <label class="lead-form__field">
                                        Follow-up owner
                                        <input type="text" name="followUpOwner" value="${page.hoodServiceReportForm().followUpOwner()}">
                                    </label>

                                    <label class="lead-form__field">
                                        Follow-up due date
                                        <input type="date" name="followUpDueDate" value="${page.hoodServiceReportForm().followUpDueDate()}">
                                    </label>
                                </div>

                                <div class="tool-attachment-checklist">
                                    <label class="lead-form__consent">
                                        <input type="checkbox" name="reportAttachmentReady" value="true" checked="${page.hoodServiceReportForm().reportAttachmentReady()}">
                                        <span>Customer-facing report PDF or printout is ready.</span>
                                    </label>
                                    <label class="lead-form__consent">
                                        <input type="checkbox" name="photoSetAttached" value="true" checked="${page.hoodServiceReportForm().photoSetAttached()}">
                                        <span>Photo set is attached.</span>
                                    </label>
                                    <label class="lead-form__consent">
                                        <input type="checkbox" name="reportFileAttached" value="true" checked="${page.hoodServiceReportForm().reportFileAttached()}">
                                        <span>Invoice or internal report file is attached.</span>
                                    </label>
                                    <div data-reference-link-row>
                                        <label class="lead-form__consent">
                                        <input type="checkbox" name="referenceLinkAdded" value="true" checked="${page.hoodServiceReportForm().referenceLinkAdded()}">
                                        <span>Rule link or reference page is added separately when needed.</span>
                                        </label>
                                    </div>
                                </div>

                                <label class="lead-form__consent">
                                    <input type="checkbox" name="includeReferenceLink" value="true" checked="${page.hoodServiceReportForm().includeReferenceLink()}">
                                    <span>Include the city rule link in the customer email only when this account actually wants it.</span>
                                </label>

                                <div class="action-row" style="margin-top: 18px;">
                                    <button class="button button--accent" type="submit">Refresh handoff</button>
                                    <button class="button button--secondary" type="submit" formaction="/tools/hood-service-report.txt" formtarget="_blank">Export current TXT</button>
                                    <button class="button button--secondary" type="submit" formaction="/tools/hood-service-report.csv" formtarget="_blank">Export current CSV</button>
                                    <button class="button button--secondary" type="button" data-tool-action="print_pdf">Print / PDF</button>
                                    @if(page.activeReferenceLink() != null)
                                        <a class="button button--secondary"
                                           href="${page.activeReferenceLink().path()}"
                                           data-tool-action="open_rule_page"
                                           data-tool-target="${page.activeReferenceLink().path()}">Open rule page</a>
                                    @endif
                                </div>
                                <p class="panel__note tool-compose-note">Use browser print on this preview if the office wants a quick PDF version before sending.</p>
                            </form>
                        </section>

                        <article class="tool-report-sheet" id="customer-copy">
                            <header class="tool-report-sheet__header">
                                <div>
                                    <div class="tool-report-sheet__eyebrow">Customer copy</div>
                                    <h3>${page.sampleReportPreview().title()}</h3>
                                    <p class="panel__note" style="margin-top: 8px;">${page.sampleReportPreview().summary()}</p>
                                    @if(page.hoodServiceReportForm() != null)
                                        <div class="tool-report-sheet__meta">
                                            <span><strong>Prepared by:</strong> ${page.hoodServiceReportForm().vendorName()}</span>
                                            <span><strong>Recipient:</strong> ${page.hoodServiceReportForm().recipientName() != null && !page.hoodServiceReportForm().recipientName().isBlank() ? page.hoodServiceReportForm().recipientName() : "Existing thread"}</span>
                                            <span><strong>Rule reference:</strong> ${page.hoodServiceReportForm().includeReferenceLink() ? "Included separately when needed" : "Not included in this send"}</span>
                                        </div>
                                    @endif
                                </div>
                                <div class="tool-report-sheet__badge">For records</div>
                            </header>

                            <section class="tool-report-sheet__grid">
                                <div>
                                    <div class="tool-report-sheet__section-title">Job header</div>
                                    <ul class="bullet-list" style="margin-top: 12px;">
                                        @for(String item : page.sampleReportPreview().headerFacts())
                                            <li>${item}</li>
                                        @endfor
                                    </ul>
                                </div>

                                <div>
                                    <div class="tool-report-sheet__section-title">Work completed</div>
                                    <ul class="bullet-list" style="margin-top: 12px;">
                                        @for(String item : page.sampleReportPreview().completedItems())
                                            <li>${item}</li>
                                        @endfor
                                    </ul>
                                </div>
                            </section>

                            <section class="tool-report-sheet__grid">
                                <div>
                                    <div class="tool-report-sheet__section-title">Attached proof</div>
                                    <ul class="bullet-list" style="margin-top: 12px;">
                                        @for(String item : page.sampleReportPreview().attachedProof())
                                            <li>${item}</li>
                                        @endfor
                                    </ul>
                                </div>
                                
                                <div>
                                    <div class="tool-report-sheet__section-title">Proof pack to send</div>
                                    <ul class="bullet-list" style="margin-top: 12px;">
                                        @for(String item : page.sampleReportPreview().proofPackItems())
                                            <li>${item}</li>
                                        @endfor
                                    </ul>
                                </div>
                            </section>

                            <section class="tool-report-sheet__photo-section" data-report-photo-section hidden>
                                <div class="tool-report-sheet__section-title">Photo proof</div>
                                <div class="panel__note" style="margin-top: 8px;">Local browser photos included in this closeout preview.</div>
                                <div class="tool-report-photo-grid" data-report-photo-grid></div>
                            </section>

                            <section class="tool-report-sheet__grid">
                                <div>
                                    <div class="tool-report-sheet__section-title">Follow-up items</div>
                                    <ul class="bullet-list" style="margin-top: 12px;">
                                        @for(String item : page.sampleReportPreview().followUpItems())
                                            <li>${item}</li>
                                        @endfor
                                    </ul>
                                </div>

                                @if(page.hoodPacketSummary() != null)
                                    <div>
                                        <div class="tool-report-sheet__section-title">Closeout packet</div>
                                        <ul class="tool-report-packet-list">
                                            @for(PacketItem item : page.hoodPacketSummary().items())
                                                <li data-ready="${item.ready()}">
                                                    <strong>${item.label()}</strong>
                                                    <span>${item.note()}</span>
                                                </li>
                                            @endfor
                                        </ul>
                                    </div>
                                @endif
                            </section>

                            <footer class="tool-report-sheet__footer">
                                <div class="tool-report-sheet__next">${page.sampleReportPreview().nextServiceLine()}</div>
                                <div class="panel__note" style="margin-top: 8px;">${page.sampleReportPreview().customerHandoffLine()}</div>
                                <div class="tool-report-sheet__print-note">This closeout is a customer record of service performed and attached proof. Local rule pages stay separate as reference.</div>
                            </footer>
                        </article>
                    </div>

                    @if(page.sendReadinessPanel() != null || page.hoodPacketSummary() != null || page.hoodAttachmentBundle() != null)
                        <details class="mobile-drawer-group tool-print-hide" data-mobile-collapsible open>
                            <summary>
                                <div>
                                    <div class="panel__eyebrow">Mobile shortcut</div>
                                    <div class="mobile-drawer-group__title">Before send, packet status, and attachment plan</div>
                                </div>
                                <span class="mobile-drawer-group__toggle" aria-hidden="true"></span>
                            </summary>
                            <div class="mobile-drawer-group__body">
                                <section class="tool-ops-grid">
                                    @if(page.sendReadinessPanel() != null)
                                        <article class="panel panel--plain tool-ops-card tool-send-readiness" data-ready="${page.sendReadinessPanel().ready()}">
                                            <div class="panel__eyebrow">${page.sendReadinessPanel().ready() ? "Send status" : "Before send"}</div>
                                            <h3>${page.sendReadinessPanel().title()}</h3>
                                            <p class="panel__note" style="margin-top: 10px;">${page.sendReadinessPanel().summary()}</p>
                                            <ul class="bullet-list" style="margin-top: 14px;">
                                                @for(String item : page.sendReadinessPanel().items())
                                                    <li>${item}</li>
                                                @endfor
                                            </ul>
                                        </article>
                                    @endif

                                    @if(page.hoodPacketSummary() != null)
                                        <article class="panel panel--plain tool-ops-card tool-packet-summary" data-ready="${page.hoodPacketSummary().readyItems() == page.hoodPacketSummary().totalItems()}">
                                            <div class="panel__eyebrow">Packet status</div>
                                            <h3>${page.hoodPacketSummary().title()}</h3>
                                            <p class="panel__note" style="margin-top: 10px;">${page.hoodPacketSummary().summary()}</p>
                                            <div class="tool-packet-summary__count">${page.hoodPacketSummary().readyItems()} / ${page.hoodPacketSummary().totalItems()} packet items ready</div>
                                            <ul class="tool-packet-summary__list">
                                                @for(PacketItem item : page.hoodPacketSummary().items())
                                                    <li data-ready="${item.ready()}">
                                                        <strong>${item.label()}</strong>
                                                        <span>${item.note()}</span>
                                                    </li>
                                                @endfor
                                            </ul>
                                        </article>
                                    @endif

                                    @if(page.hoodAttachmentBundle() != null)
                                        <article class="panel panel--plain tool-ops-card tool-attachment-bundle">
                                            <div class="panel__eyebrow">Attachment plan</div>
                                            <h3>${page.hoodAttachmentBundle().title()}</h3>
                                            <p class="panel__note" style="margin-top: 10px;">${page.hoodAttachmentBundle().summary()}</p>
                                            <ul class="tool-attachment-bundle__list">
                                                @for(AttachmentBundleItem item : page.hoodAttachmentBundle().items())
                                                    <li data-ready="${item.ready()}">
                                                        <strong>${item.label()}</strong>
                                                        <span class="tool-attachment-bundle__filename">${item.suggestedName()}</span>
                                                        <span>${item.note()}</span>
                                                    </li>
                                                @endfor
                                            </ul>
                                        </article>
                                    @endif
                                </section>
                            </div>
                        </details>
                    @endif
                @else
                    <article class="tool-report-sheet">
                        <header class="tool-report-sheet__header">
                            <div>
                                <div class="tool-report-sheet__eyebrow">Customer copy</div>
                                <h3>${page.sampleReportPreview().title()}</h3>
                                <p class="panel__note" style="margin-top: 8px;">${page.sampleReportPreview().summary()}</p>
                            </div>
                            <div class="tool-report-sheet__badge">For records</div>
                        </header>

                        <section class="tool-report-sheet__grid">
                            <div>
                                <div class="tool-report-sheet__section-title">Job header</div>
                                <ul class="bullet-list" style="margin-top: 12px;">
                                    @for(String item : page.sampleReportPreview().headerFacts())
                                        <li>${item}</li>
                                    @endfor
                                </ul>
                            </div>

                            <div>
                                <div class="tool-report-sheet__section-title">Work completed</div>
                                <ul class="bullet-list" style="margin-top: 12px;">
                                    @for(String item : page.sampleReportPreview().completedItems())
                                        <li>${item}</li>
                                    @endfor
                                </ul>
                            </div>
                        </section>

                        <section class="tool-report-sheet__grid">
                            <div>
                                <div class="tool-report-sheet__section-title">Attached proof</div>
                                <ul class="bullet-list" style="margin-top: 12px;">
                                    @for(String item : page.sampleReportPreview().attachedProof())
                                        <li>${item}</li>
                                    @endfor
                                </ul>
                            </div>

                            <div>
                                <div class="tool-report-sheet__section-title">Follow-up items</div>
                                <ul class="bullet-list" style="margin-top: 12px;">
                                    @for(String item : page.sampleReportPreview().followUpItems())
                                        <li>${item}</li>
                                    @endfor
                                </ul>
                            </div>
                        </section>

                        <footer class="tool-report-sheet__footer">
                            <div class="tool-report-sheet__next">${page.sampleReportPreview().nextServiceLine()}</div>
                            <div class="panel__note" style="margin-top: 8px;">${page.sampleReportPreview().customerHandoffLine()}</div>
                        </footer>
                    </article>
                @endif
            </section>
        @endif

        <section class="grid-two tool-print-hide">
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

        @if(!page.sendSections().isEmpty() || !page.languageGuardrails().isEmpty() || !page.deliveryChecklist().isEmpty() || page.emailSubject() != null || !page.emailBodyLines().isEmpty() || !page.vendorWorkflowMoments().isEmpty() || !page.customerOutcomes().isEmpty() || !page.repeatReasons().isEmpty() || !page.referenceAddOnNotes().isEmpty() || page.referenceSnippet() != null)
            <details class="mobile-drawer-group tool-print-hide" data-mobile-collapsible open>
                <summary>
                    <div>
                        <div class="panel__eyebrow">Mobile shortcut</div>
                        <div class="mobile-drawer-group__title">Email copy, send checklist, workflow notes, and rule reference</div>
                    </div>
                    <span class="mobile-drawer-group__toggle" aria-hidden="true"></span>
                </summary>
                <div class="mobile-drawer-group__body">
                    @if(!page.sendSections().isEmpty() || !page.languageGuardrails().isEmpty())
                        <section class="grid-two" style="margin-top: 18px;">
                            @if(!page.sendSections().isEmpty())
                                <article class="panel panel--plain">
                                    <div class="panel__eyebrow">Customer-ready structure</div>
                                    <h2>What to send</h2>
                                    <ul class="bullet-list" style="margin-top: 16px;">
                                        @for(String item : page.sendSections())
                                            <li>${item}</li>
                                        @endfor
                                    </ul>
                                </article>
                            @endif

                            @if(!page.languageGuardrails().isEmpty())
                                <article class="panel panel--plain">
                                    <div class="panel__eyebrow">Language guardrails</div>
                                    <h2>Keep the wording factual</h2>
                                    <ul class="bullet-list" style="margin-top: 16px;">
                                        @for(String item : page.languageGuardrails())
                                            <li>${item}</li>
                                        @endfor
                                    </ul>
                                </article>
                            @endif
                        </section>
                    @endif

                    @if(!page.deliveryChecklist().isEmpty() || page.emailSubject() != null || !page.emailBodyLines().isEmpty())
                        <section class="grid-two" style="margin-top: 18px;">
                            @if(!page.deliveryChecklist().isEmpty())
                                <article class="panel panel--accent">
                                    <div class="panel__eyebrow">Send pack</div>
                                    <h2>Attach these with it</h2>
                                    <ul class="bullet-list" style="margin-top: 16px;">
                                        @for(String item : page.deliveryChecklist())
                                            <li>${item}</li>
                                        @endfor
                                    </ul>
                                </article>
                            @endif

                            @if(page.emailSubject() != null || !page.emailBodyLines().isEmpty())
                                <article class="panel panel--plain" id="ready-email">
                                    <div class="panel__eyebrow">Ready-to-send email</div>
                                    <h2>Use this as your handoff copy</h2>
                                    @if(page.hoodServiceReportForm() != null)
                                        <div class="action-row tool-email-actions" style="margin-top: 16px;">
                                            @if(page.hoodServiceReportForm().recipientEmail() != null && !page.hoodServiceReportForm().recipientEmail().isBlank() && page.emailDraftMailto() != null)
                                                <a class="button button--secondary" href="${page.emailDraftMailto()}" data-tool-action="open_email_draft">Open email draft</a>
                                            @else
                                                <div class="panel__note">Recipient email is blank. Copy the handoff or send it from your existing thread.</div>
                                            @endif
                                            <button class="button button--secondary" type="button" data-copy-target="hood-service-report-subject" data-copy-label="Email subject" data-tool-action="copy_subject">Copy subject</button>
                                            <button class="button button--secondary" type="button" data-copy-target="hood-service-report-body" data-copy-label="Email body" data-tool-action="copy_body">Copy email body</button>
                                        </div>
                                        <div class="panel__note tool-copy-status" data-copy-status hidden></div>
                                        <textarea id="hood-service-report-subject" class="visually-hidden" readonly>${page.emailSubject()}</textarea>
                                        <textarea id="hood-service-report-body" class="visually-hidden" readonly>${String.join("\n", page.emailBodyLines())}</textarea>
                                    @endif
                                    @if(page.emailSubject() != null)
                                        <div class="panel__note" style="margin-top: 16px;"><strong>Subject:</strong> ${page.emailSubject()}</div>
                                    @endif
                                    @if(!page.emailBodyLines().isEmpty())
                                        <div class="panel__note" style="margin-top: 12px; white-space: pre-line;">${String.join("\n", page.emailBodyLines())}</div>
                                    @endif
                                </article>
                            @endif
                        </section>
                    @endif

                    @if(!page.vendorWorkflowMoments().isEmpty() || !page.customerOutcomes().isEmpty())
                        <section class="grid-two" style="margin-top: 18px;">
                            @if(!page.vendorWorkflowMoments().isEmpty())
                                <article class="panel panel--plain">
                                    <div class="panel__eyebrow">Vendor perspective</div>
                                    <h2>Where this fits in the office</h2>
                                    <ul class="bullet-list" style="margin-top: 16px;">
                                        @for(String item : page.vendorWorkflowMoments())
                                            <li>${item}</li>
                                        @endfor
                                    </ul>
                                </article>
                            @endif

                            @if(!page.customerOutcomes().isEmpty())
                                <article class="panel panel--plain">
                                    <div class="panel__eyebrow">Customer perspective</div>
                                    <h2>What the customer gets</h2>
                                    <ul class="bullet-list" style="margin-top: 16px;">
                                        @for(String item : page.customerOutcomes())
                                            <li>${item}</li>
                                        @endfor
                                    </ul>
                                </article>
                            @endif
                        </section>
                    @endif

                    @if(!page.repeatReasons().isEmpty())
                        <section class="panel panel--accent" style="margin-top: 18px;">
                            <div class="panel__eyebrow">Product perspective</div>
                            <h2>Why this gets reused</h2>
                            <ul class="bullet-list" style="margin-top: 16px;">
                                @for(String item : page.repeatReasons())
                                    <li>${item}</li>
                                @endfor
                            </ul>
                        </section>
                    @endif

                    @if(!page.referenceAddOnNotes().isEmpty() || page.referenceSnippet() != null)
                        <section class="grid-two" style="margin-top: 18px;">
                            <article class="panel panel--plain" id="reference-link">
                                <div class="panel__eyebrow">Customer return path</div>
                                <h2>Attach the rule link without weakening the handoff</h2>
                                @if(!page.referenceAddOnNotes().isEmpty())
                                    <ul class="bullet-list" style="margin-top: 16px;">
                                        @for(String item : page.referenceAddOnNotes())
                                            <li>${item}</li>
                                        @endfor
                                    </ul>
                                @endif
                                @if(page.referenceSnippet() != null)
                                    <div class="panel__note" style="margin-top: 16px; white-space: pre-line;"><strong>Reference line:</strong> ${page.referenceSnippet()}</div>
                                @endif
                                @if(page.activeReferenceLink() != null)
                                    <div class="tool-reference-chip">
                                        <strong>${page.activeReferenceLink().label()}</strong>
                                        <div>${page.activeReferenceLink().note()}</div>
                                        <a class="text-link"
                                           href="${page.activeReferenceLink().path()}"
                                           data-tool-action="open_rule_page"
                                           data-tool-target="${page.activeReferenceLink().path()}">${page.activeReferenceLink().absoluteUrl()}</a>
                                    </div>
                                @endif
                            </article>
                        </section>
                    @endif
                </div>
            </details>
        @endif

        @if(page.vendorSetupPanel() != null)
            <section class="panel panel--accent tool-print-hide" style="margin-top: 18px;">
                <div class="panel__eyebrow">Vendor-only setup</div>
                <h2>${page.vendorSetupPanel().title()}</h2>
                <p class="panel__note" style="margin-top: 12px;">${page.vendorSetupPanel().summary()}</p>
                <ul class="bullet-list" style="margin-top: 16px;">
                    @for(String item : page.vendorSetupPanel().includedItems())
                        <li>${item}</li>
                    @endfor
                </ul>
                <div class="action-row" style="margin-top: 18px;">
                    <a class="button button--dark" href="${page.vendorSetupPanel().ctaPath()}">${page.vendorSetupPanel().ctaLabel()}</a>
                </div>
            </section>
        @endif

        <section class="panel tool-print-hide" style="margin-top: 18px;">
            <div class="panel__eyebrow">Related local pages</div>
            <h2>Use the tool after the rule is clear</h2>
            <ul class="link-list" style="margin-top: 16px;">
                @for(RelatedPageLink link : page.relatedLinks())
                    <li><a href="${link.path()}">${link.label()}</a></li>
                @endfor
            </ul>
        </section>
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

    @if(page.hoodServiceReportForm() != null)
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const root = document.querySelector('[data-tool-event-endpoint]');
                const status = document.querySelector('[data-copy-status]');
                const presetStatus = document.querySelector('[data-preset-status]');
                const draftStatus = document.querySelector('[data-draft-status]');
                const photoStatus = document.querySelector('[data-photo-upload-status]');
                const quickStatus = document.querySelector('[data-quick-start-status]');
                const accountPresetStatus = document.querySelector('[data-account-preset-status]');
                const endpoint = root ? root.dataset.toolEventEndpoint : '';
                const city = root ? root.dataset.toolCity : '';
                const presetKey = 'kitchenrulehub.hood-service-report.office-preset.v1';
                const accountPresetKey = 'kitchenrulehub.hood-service-report.customer-setups.v1';
                const draftKey = 'kitchenrulehub.hood-service-report.recent-draft.v1';
                const photoStorageKey = 'kitchenrulehub.hood-service-report.local-photos.v1';
                const photoStatusKey = 'kitchenrulehub.hood-service-report.local-photos.status.v1';
                const presetStatusKey = 'kitchenrulehub.hood-service-report.office-preset.status.v1';
                const draftStatusKey = 'kitchenrulehub.hood-service-report.recent-draft.status.v1';
                const quickStatusKey = 'kitchenrulehub.hood-service-report.quick-status.v1';
                const accountStatusKey = 'kitchenrulehub.hood-service-report.customer-status.v1';
                const referenceSuggestionStatusKey = 'kitchenrulehub.hood-service-report.reference-suggestion.status.v1';
                const maxLocalPhotos = 4;
                let skipNextDraftAutosave = false;
                const toolForm = document.querySelector('form.lead-form');
                const photoInput = document.querySelector('[data-photo-upload]');
                const editorPhotoGallery = document.querySelector('[data-photo-gallery-editor]');
                const reportPhotoSection = document.querySelector('[data-report-photo-section]');
                const reportPhotoGrid = document.querySelector('[data-report-photo-grid]');
                const photoSetAttachedField = document.querySelector('[name="photoSetAttached"]');
                const customerNameField = document.querySelector('[name="customerName"]');
                const siteNameField = document.querySelector('[name="siteName"]');
                const cityField = document.querySelector('[name="city"]');
                const serviceDateField = document.querySelector('[name="serviceDate"]');
                const workOrderField = document.querySelector('[name="workOrderReference"]');
                const photoReferenceField = document.querySelector('[name="photoReference"]');
                const reportReferenceField = document.querySelector('[name="reportReference"]');
                const followUpItemsField = document.querySelector('[name="followUpItems"]');
                const followUpDetailRow = document.querySelector('[data-follow-up-detail-row]');
                const includeReferenceLinkField = document.querySelector('[name="includeReferenceLink"]');
                const referenceLinkRow = document.querySelector('[data-reference-link-row]');
                const photoReferenceSuggestion = document.querySelector('[data-photo-reference-suggestion]');
                const reportReferenceSuggestion = document.querySelector('[data-report-reference-suggestion]');
                const referenceSuggestionStatus = document.querySelector('[data-reference-suggestion-status]');
                const accountPresetNameField = document.querySelector('[data-account-preset-name]');
                const accountPresetSelect = document.querySelector('[data-account-preset-select]');
                const presetFields = [
                    'vendorName',
                    'crewOrTechnician',
                    'systemsServiced',
                    'completedWork',
                    'customerHandoffNote',
                    'followUpOwner',
                    'includeReferenceLink'
                ];
                const accountPresetFields = [
                    'city',
                    'customerName',
                    'siteName',
                    'recipientName',
                    'recipientEmail',
                    'siteAddress',
                    'systemsServiced',
                    'customerHandoffNote',
                    'followUpOwner',
                    'includeReferenceLink'
                ];
                const blankJobFields = [
                    'customerName',
                    'siteName',
                    'recipientName',
                    'recipientEmail',
                    'siteAddress',
                    'vendorName',
                    'crewOrTechnician',
                    'workOrderReference',
                    'systemsServiced',
                    'completedWork',
                    'photoReference',
                    'reportReference',
                    'customerHandoffNote',
                    'followUpItems',
                    'followUpOwner',
                    'followUpDueDate',
                    'reportAttachmentReady',
                    'photoSetAttached',
                    'reportFileAttached',
                    'referenceLinkAdded',
                    'includeReferenceLink'
                ];
                const draftFields = [
                    'city',
                    'serviceDate',
                    'nextServiceDate',
                    'customerName',
                    'siteName',
                    'recipientName',
                    'recipientEmail',
                    'siteAddress',
                    'vendorName',
                    'crewOrTechnician',
                    'workOrderReference',
                    'systemsServiced',
                    'completedWork',
                    'photoReference',
                    'reportReference',
                    'customerHandoffNote',
                    'followUpItems',
                    'followUpOwner',
                    'followUpDueDate',
                    'reportAttachmentReady',
                    'photoSetAttached',
                    'reportFileAttached',
                    'referenceLinkAdded',
                    'includeReferenceLink'
                ];

                function sendToolAction(action, targetPath) {
                    if (!endpoint || !action) {
                        return;
                    }
                    const body = new URLSearchParams();
                    body.set('action', action);
                    if (city) {
                        body.set('city', city);
                    }
                    if (targetPath) {
                        body.set('targetPath', targetPath);
                    }
                    if (navigator.sendBeacon) {
                        navigator.sendBeacon(endpoint, body);
                        return;
                    }
                    fetch(endpoint, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                        },
                        body: body.toString(),
                        keepalive: true
                    }).catch(function () {});
                }

                function fieldValue(name) {
                    const field = document.querySelector('[name="' + name + '"]');
                    if (!field) {
                        return null;
                    }
                    if (field.type === 'checkbox') {
                        return field.checked;
                    }
                    return field.value;
                }

                function setFieldValue(name, value) {
                    const field = document.querySelector('[name="' + name + '"]');
                    if (!field) {
                        return;
                    }
                    if (field.type === 'checkbox') {
                        field.checked = value === true || value === 'true';
                        return;
                    }
                    field.value = value || '';
                }

                function showQuickStatus(message) {
                    if (!quickStatus) {
                        return;
                    }
                    quickStatus.hidden = !message;
                    quickStatus.textContent = message || '';
                }

                function showAccountStatus(message) {
                    if (!accountPresetStatus) {
                        return;
                    }
                    accountPresetStatus.hidden = !message;
                    accountPresetStatus.textContent = message || '';
                }

                function showPresetStatus(message) {
                    if (!presetStatus) {
                        return;
                    }
                    presetStatus.hidden = !message;
                    presetStatus.textContent = message || '';
                }

                function showDraftStatus(message) {
                    if (!draftStatus) {
                        return;
                    }
                    draftStatus.hidden = !message;
                    draftStatus.textContent = message || '';
                }

                function showPhotoStatus(message) {
                    if (!photoStatus) {
                        return;
                    }
                    photoStatus.hidden = !message;
                    photoStatus.textContent = message || '';
                }

                function showReferenceSuggestionStatus(message) {
                    if (!referenceSuggestionStatus) {
                        return;
                    }
                    referenceSuggestionStatus.hidden = !message;
                    referenceSuggestionStatus.textContent = message || '';
                }

                function persistPhotoStatus(message) {
                    try {
                        if (!message) {
                            sessionStorage.removeItem(photoStatusKey);
                            return;
                        }
                        sessionStorage.setItem(photoStatusKey, message);
                    } catch (error) {
                    }
                }

                function readPendingPhotoStatus() {
                    try {
                        const message = sessionStorage.getItem(photoStatusKey);
                        if (!message) {
                            return '';
                        }
                        sessionStorage.removeItem(photoStatusKey);
                        return message;
                    } catch (error) {
                        return '';
                    }
                }

                function persistTransientStatus(storageKey, message) {
                    try {
                        if (!message) {
                            sessionStorage.removeItem(storageKey);
                            return;
                        }
                        sessionStorage.setItem(storageKey, message);
                    } catch (error) {
                    }
                }

                function readTransientStatus(storageKey) {
                    try {
                        const message = sessionStorage.getItem(storageKey);
                        if (!message) {
                            return '';
                        }
                        sessionStorage.removeItem(storageKey);
                        return message;
                    } catch (error) {
                        return '';
                    }
                }

                function saveFields(storageKey, fields) {
                    const payload = {};
                    fields.forEach(function (name) {
                        payload[name] = fieldValue(name);
                    });
                    localStorage.setItem(storageKey, JSON.stringify(payload));
                }

                function readStoredObject(storageKey) {
                    try {
                        const raw = localStorage.getItem(storageKey);
                        if (!raw) {
                            return null;
                        }
                        return JSON.parse(raw);
                    } catch (error) {
                        return null;
                    }
                }

                function applyStoredFields(storageKey, fields) {
                    const payload = readStoredObject(storageKey);
                    if (!payload || typeof payload !== 'object' || Array.isArray(payload)) {
                        return false;
                    }
                    fields.forEach(function (name) {
                        if (Object.prototype.hasOwnProperty.call(payload, name)) {
                            setFieldValue(name, payload[name]);
                        }
                    });
                    return true;
                }

                function readStoredPhotos() {
                    try {
                        const raw = sessionStorage.getItem(photoStorageKey);
                        if (!raw) {
                            return [];
                        }
                        const payload = JSON.parse(raw);
                        if (!Array.isArray(payload)) {
                            return [];
                        }
                        return payload.filter(function (item) {
                            return item && item.name && item.dataUrl;
                        });
                    } catch (error) {
                        return [];
                    }
                }

                function storePhotos(photos) {
                    try {
                        if (!photos.length) {
                            sessionStorage.removeItem(photoStorageKey);
                            return true;
                        }
                        sessionStorage.setItem(photoStorageKey, JSON.stringify(photos));
                        return true;
                    } catch (error) {
                        return false;
                    }
                }

                function clearLocalPhotos(message) {
                    if (photoInput) {
                        photoInput.value = '';
                    }
                    sessionStorage.removeItem(photoStorageKey);
                    if (!message) {
                        persistPhotoStatus('');
                    } else {
                        persistPhotoStatus(message);
                    }
                    if (photoSetAttachedField) {
                        photoSetAttachedField.checked = false;
                    }
                    renderStoredPhotos([]);
                    showPhotoStatus(message || '');
                }

                function buildPhotoCard(photo) {
                    const figure = document.createElement('figure');
                    figure.className = 'tool-photo-card';

                    const image = document.createElement('img');
                    image.src = photo.dataUrl;
                    image.alt = photo.name;
                    figure.appendChild(image);

                    const caption = document.createElement('figcaption');
                    caption.textContent = photo.name;
                    figure.appendChild(caption);
                    return figure;
                }

                function renderStoredPhotos(photos) {
                    if (editorPhotoGallery) {
                        editorPhotoGallery.innerHTML = '';
                        editorPhotoGallery.hidden = photos.length === 0;
                        photos.forEach(function (photo) {
                            editorPhotoGallery.appendChild(buildPhotoCard(photo));
                        });
                    }

                    if (reportPhotoGrid && reportPhotoSection) {
                        reportPhotoGrid.innerHTML = '';
                        reportPhotoSection.hidden = photos.length === 0;
                        photos.forEach(function (photo) {
                            reportPhotoGrid.appendChild(buildPhotoCard(photo));
                        });
                    }
                }

                function photoCountLabel(count) {
                    return count === 1 ? '1 local photo' : count + ' local photos';
                }

                function slugifyReferenceValue(value) {
                    return (value || '')
                            .normalize('NFKD')
                            .replace(/[\u0300-\u036f]/g, '')
                            .toLowerCase()
                            .replace(/[^a-z0-9]+/g, '-')
                            .replace(/(^-+|-+$)/g, '');
                }

                function selectedCityLabel() {
                    if (!cityField) {
                        return 'hood closeout';
                    }
                    const selectedOption = cityField.options[cityField.selectedIndex];
                    return selectedOption ? selectedOption.textContent.trim() : 'hood closeout';
                }

                function referenceSuggestionStem() {
                    const labelSource =
                            (siteNameField && siteNameField.value.trim())
                            || (customerNameField && customerNameField.value.trim())
                            || (selectedCityLabel() + ' hood closeout');
                    const stem = slugifyReferenceValue(labelSource) || 'hood-closeout';
                    const serviceDate = serviceDateField && serviceDateField.value ? serviceDateField.value.trim() : '';
                    return serviceDate ? stem + '-' + serviceDate : stem;
                }

                function suggestedPhotoReferenceValue() {
                    return referenceSuggestionStem() + '-photos';
                }

                function suggestedReportReferenceValue() {
                    const workOrder = workOrderField && workOrderField.value ? slugifyReferenceValue(workOrderField.value) : '';
                    return workOrder || (referenceSuggestionStem() + '-report');
                }

                function refreshReferenceSuggestions() {
                    if (photoReferenceSuggestion) {
                        photoReferenceSuggestion.textContent = suggestedPhotoReferenceValue();
                    }
                    if (reportReferenceSuggestion) {
                        reportReferenceSuggestion.textContent = suggestedReportReferenceValue();
                    }
                }

                function toggleFollowUpDetails() {
                    if (!followUpDetailRow || !followUpItemsField) {
                        return;
                    }
                    const hasFollowUp = followUpItemsField.value.trim().length > 0;
                    followUpDetailRow.hidden = !hasFollowUp;
                    if (!hasFollowUp) {
                        setFieldValue('followUpOwner', '');
                        setFieldValue('followUpDueDate', '');
                    }
                }

                function toggleReferenceLinkDetails() {
                    if (!referenceLinkRow || !includeReferenceLinkField) {
                        return;
                    }
                    const includeReference = includeReferenceLinkField.checked;
                    referenceLinkRow.hidden = !includeReference;
                    if (!includeReference) {
                        setFieldValue('referenceLinkAdded', false);
                    }
                }

                function resetFields(fields) {
                    fields.forEach(function (name) {
                        setFieldValue(name, '');
                    });
                }

                function buildCustomerSetupName(payload) {
                    const customer = payload.customerName ? payload.customerName.trim() : '';
                    const site = payload.siteName ? payload.siteName.trim() : '';
                    if (customer && site && customer !== site) {
                        return customer + ' | ' + site;
                    }
                    if (site) {
                        return site;
                    }
                    if (customer) {
                        return customer;
                    }
                    return 'Saved customer setup';
                }

                function currentCustomerSetupPayload() {
                    const payload = {};
                    accountPresetFields.forEach(function (name) {
                        payload[name] = fieldValue(name);
                    });
                    return payload;
                }

                function readCustomerSetups() {
                    const payload = readStoredObject(accountPresetKey);
                    if (!Array.isArray(payload)) {
                        return [];
                    }
                    return payload.filter(function (item) {
                        return item && item.name && item.data && typeof item.data === 'object';
                    });
                }

                function writeCustomerSetups(setups) {
                    localStorage.setItem(accountPresetKey, JSON.stringify(setups.slice(0, 8)));
                }

                function populateCustomerSetupOptions(selectedName) {
                    if (!accountPresetSelect) {
                        return;
                    }
                    const currentValue = selectedName || accountPresetSelect.value;
                    accountPresetSelect.innerHTML = '';

                    const blankOption = document.createElement('option');
                    blankOption.value = '';
                    blankOption.textContent = 'Choose a saved customer setup';
                    accountPresetSelect.appendChild(blankOption);

                    readCustomerSetups().forEach(function (setup) {
                        const option = document.createElement('option');
                        option.value = setup.name;
                        option.textContent = setup.name;
                        option.selected = setup.name === currentValue;
                        accountPresetSelect.appendChild(option);
                    });
                }

                function shrinkImage(file) {
                    return new Promise(function (resolve, reject) {
                        const reader = new FileReader();
                        reader.onload = function () {
                            const image = new Image();
                            image.onload = function () {
                                const maxDimension = 1600;
                                let width = image.width;
                                let height = image.height;

                                if (width > maxDimension || height > maxDimension) {
                                    const scale = Math.min(maxDimension / width, maxDimension / height);
                                    width = Math.max(1, Math.round(width * scale));
                                    height = Math.max(1, Math.round(height * scale));
                                }

                                const canvas = document.createElement('canvas');
                                canvas.width = width;
                                canvas.height = height;
                                const context = canvas.getContext('2d');
                                if (!context) {
                                    resolve({
                                        name: file.name,
                                        dataUrl: reader.result
                                    });
                                    return;
                                }

                                context.drawImage(image, 0, 0, width, height);
                                resolve({
                                    name: file.name,
                                    dataUrl: canvas.toDataURL('image/jpeg', 0.82)
                                });
                            };
                            image.onerror = function () {
                                reject(new Error('image-load-failed'));
                            };
                            image.src = reader.result;
                        };
                        reader.onerror = function () {
                            reject(new Error('reader-failed'));
                        };
                        reader.readAsDataURL(file);
                    });
                }

                async function handleLocalPhotos(fileList) {
                    const imageFiles = Array.from(fileList || [])
                            .filter(function (file) {
                                return file.type && file.type.indexOf('image/') === 0;
                            })
                            .slice(0, maxLocalPhotos);

                    if (!imageFiles.length) {
                        showPhotoStatus('No image files selected. Add JPG or PNG photos from the device.');
                        return;
                    }

                    try {
                        const photos = await Promise.all(imageFiles.map(shrinkImage));
                        const stored = storePhotos(photos);
                        renderStoredPhotos(photos);
                        if (photoSetAttachedField && !photoSetAttachedField.checked) {
                            photoSetAttachedField.checked = true;
                        }
                        const successMessage =
                                'Saved ' + photoCountLabel(photos.length)
                                + ' in this browser tab. They appear in the customer copy below'
                                + (stored ? ' and the handoff will refresh now.' : ', but this browser could not keep them through refresh.');
                        showPhotoStatus(successMessage);
                        persistPhotoStatus(successMessage);
                        sendToolAction('add_local_photos', '');
                        if (toolForm) {
                            toolForm.requestSubmit();
                        }
                    } catch (error) {
                        showPhotoStatus('Photos could not be processed. Try smaller JPG or PNG files.');
                    }
                }

                document.querySelectorAll('[data-preset-action]').forEach(function (button) {
                    button.addEventListener('click', function () {
                        const action = button.dataset.presetAction;
                        if (action === 'save') {
                            saveFields(presetKey, presetFields);
                            showPresetStatus('Saved office defaults in this browser.');
                            return;
                        }

                        if (action === 'apply') {
                            if (!applyStoredFields(presetKey, presetFields)) {
                                showPresetStatus('No saved office defaults in this browser yet.');
                                return;
                            }
                            toggleReferenceLinkDetails();
                            const message = 'Applied saved office defaults and refreshed the handoff.';
                            persistTransientStatus(presetStatusKey, message);
                            if (toolForm) {
                                toolForm.requestSubmit();
                            } else {
                                showPresetStatus(message);
                            }
                            return;
                        }

                        localStorage.removeItem(presetKey);
                        showPresetStatus('Cleared saved office defaults from this browser.');
                    });
                });

                document.querySelectorAll('[data-draft-action]').forEach(function (button) {
                    button.addEventListener('click', function () {
                        const action = button.dataset.draftAction;
                        if (action === 'save') {
                            saveFields(draftKey, draftFields);
                            showDraftStatus('Saved the current draft in this browser.');
                            return;
                        }

                        if (action === 'restore') {
                            const payload = readStoredObject(draftKey);
                            if (!payload || typeof payload !== 'object' || Array.isArray(payload)) {
                                showDraftStatus('No recent draft saved in this browser yet.');
                                return;
                            }
                            draftFields.forEach(function (name) {
                                setFieldValue(name, payload[name]);
                            });
                            toggleFollowUpDetails();
                            toggleReferenceLinkDetails();
                            const message = 'Restored the recent draft and refreshed the handoff.';
                            persistTransientStatus(draftStatusKey, message);
                            if (toolForm) {
                                toolForm.requestSubmit();
                            } else {
                                showDraftStatus(message);
                            }
                            return;
                        }

                        localStorage.removeItem(draftKey);
                        showDraftStatus('Cleared the recent draft from this browser.');
                    });
                });

                document.querySelectorAll('[data-photo-action]').forEach(function (button) {
                    button.addEventListener('click', function () {
                        clearLocalPhotos('Cleared local photos from this browser tab and refreshed the handoff.');
                        sendToolAction('clear_local_photos', '');
                        if (toolForm) {
                            toolForm.requestSubmit();
                        }
                    });
                });

                document.querySelectorAll('[data-quick-action]').forEach(function (button) {
                    button.addEventListener('click', function () {
                        const action = button.dataset.quickAction;
                        if (action === 'blank') {
                            resetFields(blankJobFields);
                            clearLocalPhotos('');
                            const appliedDefaults = applyStoredFields(presetKey, presetFields);
                            toggleFollowUpDetails();
                            toggleReferenceLinkDetails();
                            const message = appliedDefaults
                                    ? 'Cleared the sample job and reapplied saved office defaults.'
                                    : "Cleared the sample job. Add only this visit's details.";
                            persistTransientStatus(quickStatusKey, message);
                            if (toolForm) {
                                skipNextDraftAutosave = true;
                                toolForm.requestSubmit();
                            } else {
                                showQuickStatus(message);
                            }
                            return;
                        }

                        if (action === 'copy-account') {
                            if (!customerNameField || !customerNameField.value.trim()) {
                                showQuickStatus('Add the restaurant account name first.');
                                return;
                            }
                            setFieldValue('siteName', customerNameField.value.trim());
                            refreshReferenceSuggestions();
                            const message = 'Copied the account name into the location field and refreshed the handoff.';
                            persistTransientStatus(quickStatusKey, message);
                            if (toolForm) {
                                toolForm.requestSubmit();
                            } else {
                                showQuickStatus(message);
                            }
                            return;
                        }

                        setFieldValue('followUpItems', '');
                        setFieldValue('followUpOwner', '');
                        setFieldValue('followUpDueDate', '');
                        toggleFollowUpDetails();
                        const message = 'Cleared follow-up fields for this visit and refreshed the handoff.';
                        persistTransientStatus(quickStatusKey, message);
                        if (toolForm) {
                            toolForm.requestSubmit();
                        } else {
                            showQuickStatus(message);
                        }
                    });
                });

                document.querySelectorAll('[data-reference-suggestion-action]').forEach(function (button) {
                    button.addEventListener('click', function () {
                        const action = button.dataset.referenceSuggestionAction;
                        if (action === 'photo') {
                            setFieldValue('photoReference', suggestedPhotoReferenceValue());
                            const message = 'Applied the suggested photo label and refreshed the handoff.';
                            persistTransientStatus(referenceSuggestionStatusKey, message);
                            if (toolForm) {
                                toolForm.requestSubmit();
                            } else {
                                showReferenceSuggestionStatus(message);
                            }
                            return;
                        }

                        setFieldValue('reportReference', suggestedReportReferenceValue());
                        const message = 'Applied the suggested report label and refreshed the handoff.';
                        persistTransientStatus(referenceSuggestionStatusKey, message);
                        if (toolForm) {
                            toolForm.requestSubmit();
                        } else {
                            showReferenceSuggestionStatus(message);
                        }
                    });
                });

                document.querySelectorAll('[data-account-preset-action]').forEach(function (button) {
                    button.addEventListener('click', function () {
                        const action = button.dataset.accountPresetAction;
                        const setups = readCustomerSetups();
                        const selectedName = accountPresetSelect ? accountPresetSelect.value : '';

                        if (action === 'save') {
                            const payload = currentCustomerSetupPayload();
                            const hasUsefulValue = accountPresetFields.some(function (name) {
                                const value = payload[name];
                                return value === true || (typeof value === 'string' && value.trim().length > 0);
                            });
                            if (!hasUsefulValue) {
                                showAccountStatus('Add at least one customer, contact, or site detail before saving a customer setup.');
                                return;
                            }
                            const setupName = accountPresetNameField && accountPresetNameField.value.trim()
                                    ? accountPresetNameField.value.trim()
                                    : buildCustomerSetupName(payload);
                            const nextSetups = setups.filter(function (item) {
                                return item.name !== setupName;
                            });
                            nextSetups.unshift({
                                name: setupName,
                                data: payload
                            });
                            writeCustomerSetups(nextSetups);
                            if (accountPresetNameField) {
                                accountPresetNameField.value = setupName;
                            }
                            populateCustomerSetupOptions(setupName);
                            showAccountStatus('Saved customer setup in this browser.');
                            return;
                        }

                        if (!selectedName) {
                            showAccountStatus('Choose a saved customer setup first.');
                            return;
                        }

                        if (action === 'apply') {
                            const selectedSetup = setups.find(function (item) {
                                return item.name === selectedName;
                            });
                            if (!selectedSetup) {
                                showAccountStatus('That saved customer setup is no longer available.');
                                populateCustomerSetupOptions('');
                                return;
                            }
                            accountPresetFields.forEach(function (name) {
                                if (Object.prototype.hasOwnProperty.call(selectedSetup.data, name)) {
                                    setFieldValue(name, selectedSetup.data[name]);
                                }
                            });
                            if (accountPresetNameField) {
                                accountPresetNameField.value = selectedSetup.name;
                            }
                            toggleFollowUpDetails();
                            toggleReferenceLinkDetails();
                            const message = 'Applied saved customer setup and refreshed the handoff.';
                            persistTransientStatus(accountStatusKey, message);
                            if (toolForm) {
                                toolForm.requestSubmit();
                            } else {
                                showAccountStatus(message);
                            }
                            return;
                        }

                        const nextSetups = setups.filter(function (item) {
                            return item.name !== selectedName;
                        });
                        writeCustomerSetups(nextSetups);
                        populateCustomerSetupOptions('');
                        if (accountPresetNameField) {
                            accountPresetNameField.value = '';
                        }
                        showAccountStatus('Deleted the saved customer setup from this browser.');
                    });
                });

                if (toolForm) {
                    toolForm.addEventListener('submit', function () {
                        if (skipNextDraftAutosave) {
                            skipNextDraftAutosave = false;
                            return;
                        }
                        saveFields(draftKey, draftFields);
                    });
                }

                if (photoInput) {
                    photoInput.addEventListener('change', function () {
                        handleLocalPhotos(photoInput.files);
                    });
                }

                if (followUpItemsField) {
                    followUpItemsField.addEventListener('input', toggleFollowUpDetails);
                }

                if (includeReferenceLinkField) {
                    includeReferenceLinkField.addEventListener('change', toggleReferenceLinkDetails);
                }

                [customerNameField, siteNameField, serviceDateField, workOrderField, photoReferenceField, reportReferenceField].forEach(function (field) {
                    if (!field) {
                        return;
                    }
                    field.addEventListener('input', refreshReferenceSuggestions);
                });

                if (cityField) {
                    cityField.addEventListener('change', refreshReferenceSuggestions);
                }

                const storedPhotos = readStoredPhotos();
                if (storedPhotos.length) {
                    renderStoredPhotos(storedPhotos);
                    if (photoSetAttachedField && !photoSetAttachedField.checked) {
                        photoSetAttachedField.checked = true;
                    }
                }

                const pendingPhotoStatus = readPendingPhotoStatus();
                if (pendingPhotoStatus) {
                    showPhotoStatus(pendingPhotoStatus);
                } else if (storedPhotos.length) {
                    showPhotoStatus('Restored ' + photoCountLabel(storedPhotos.length) + ' from this browser tab.');
                }

                const pendingPresetStatus = readTransientStatus(presetStatusKey);
                if (pendingPresetStatus) {
                    showPresetStatus(pendingPresetStatus);
                }

                const pendingDraftStatus = readTransientStatus(draftStatusKey);
                if (pendingDraftStatus) {
                    showDraftStatus(pendingDraftStatus);
                }

                populateCustomerSetupOptions('');
                toggleFollowUpDetails();
                toggleReferenceLinkDetails();

                const pendingQuickStatus = readTransientStatus(quickStatusKey);
                if (pendingQuickStatus) {
                    showQuickStatus(pendingQuickStatus);
                }

                const pendingAccountStatus = readTransientStatus(accountStatusKey);
                if (pendingAccountStatus) {
                    showAccountStatus(pendingAccountStatus);
                }

                refreshReferenceSuggestions();

                const pendingReferenceSuggestionStatus = readTransientStatus(referenceSuggestionStatusKey);
                if (pendingReferenceSuggestionStatus) {
                    showReferenceSuggestionStatus(pendingReferenceSuggestionStatus);
                }

                document.querySelectorAll('[data-tool-action]').forEach(function (element) {
                    element.addEventListener('click', async function (event) {
                        const action = element.dataset.toolAction;
                        const targetPath = element.dataset.toolTarget || '';
                        const fieldId = element.dataset.copyTarget;
                        if (fieldId) {
                            const field = document.getElementById(fieldId);
                            if (!field) {
                                return;
                            }
                            try {
                                await navigator.clipboard.writeText(field.value);
                                sendToolAction(action, targetPath);
                                if (status) {
                                    status.hidden = false;
                                    status.textContent = element.dataset.copyLabel + ' copied.';
                                }
                            } catch (error) {
                                if (status) {
                                    status.hidden = false;
                                    status.textContent = 'Clipboard copy failed. Use the TXT export instead.';
                                }
                            }
                            return;
                        }

                        if (action === 'print_pdf') {
                            event.preventDefault();
                            sendToolAction(action, targetPath);
                            window.print();
                            return;
                        }

                        sendToolAction(action, targetPath);
                    });
                });
            });
        </script>
    @endif
`)
