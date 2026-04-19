@import owner.kitchencompliance.model.PageMeta

@param PageMeta meta

<header class="site-header">
    <div class="site-header__inner">
        <a class="brand" href="/">
            <span>
                <span class="brand__wordmark">KitchenRuleHub</span>
                <span class="brand__sub">Local rules from official sources</span>
            </span>
        </a>

        <nav class="top-nav" aria-label="Primary">
            <a class="top-nav__link${!meta.canonicalUrl().contains("/guides/") && !meta.canonicalUrl().contains("/tools/") && !meta.canonicalUrl().contains("/admin") && !meta.canonicalUrl().contains("/authorities") && !meta.canonicalUrl().contains("/for-vendors") ? " is-active" : ""}" href="/">
                Find Your City
            </a>
            <a class="top-nav__link${meta.canonicalUrl().contains("/for-vendors") ? " is-active" : ""}" href="/for-vendors">
                For Vendors
            </a>
            <a class="top-nav__link${meta.canonicalUrl().contains("/authorities") ? " is-active" : ""}" href="/authorities">
                Browse Authorities
            </a>
            <a class="top-nav__link${meta.canonicalUrl().contains("/guides/") ? " is-active" : ""}" href="/guides/fog-vs-grease-trap-cleaning">
                Practical Guides
            </a>
            <a class="top-nav__link${meta.canonicalUrl().contains("/tools/") ? " is-active" : ""}" href="/tools/grease-log">
                Operator Tools
            </a>
        </nav>
    </div>
</header>
