@import owner.kitchencompliance.model.PageMeta

@param PageMeta meta

<header class="site-header">
    <div class="site-header__inner">
        <a class="brand" href="/">
            <span>
                <span class="brand__wordmark">KitchenRuleHub</span>
                <span class="brand__sub">Inspection-ready next steps by city</span>
            </span>
        </a>

        <nav class="top-nav" aria-label="Primary">
            <div class="top-nav__group">
                <a class="top-nav__link${!meta.canonicalUrl().contains("/guides/") && !meta.canonicalUrl().contains("/tools/") && !meta.canonicalUrl().contains("/admin") ? " is-active" : ""}" href="/">
                    Start
                </a>
                <a class="top-nav__link" href="/#active-cities">
                    Cities
                </a>
                <a class="top-nav__link${meta.canonicalUrl().contains("/guides/") ? " is-active" : ""}" href="/guides/fog-vs-grease-trap-cleaning">
                    Guides
                </a>
                <a class="top-nav__link${meta.canonicalUrl().contains("/tools/") ? " is-active" : ""}" href="/tools/grease-log">
                    Worksheets
                </a>
            </div>
        </nav>
    </div>
</header>
