@import owner.kitchencompliance.model.LocalPageViewModel

@param LocalPageViewModel page

@template.layout.page(meta = page.meta(), content = @`
    @template.layout.localShell(page)
`)
