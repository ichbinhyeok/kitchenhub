@import owner.kitchencompliance.model.LocalPageViewModel

@param LocalPageViewModel page
@param String ga4MeasurementId

@template.layout.page(meta = page.meta(), ga4MeasurementId = ga4MeasurementId, content = @`
    @template.layout.localShell(page)
`)
