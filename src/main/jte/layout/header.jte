@import owner.kitchencompliance.model.PageMeta

@param PageMeta meta

<header class="site-header">
    <div class="site-header__inner">
        <a class="brand" href="/">
            <span>
                <span class="brand__wordmark">KitchenComplianceHub</span>
                <span class="brand__sub">Official municipal data</span>
            </span>
        </a>

        <nav class="top-nav" aria-label="Primary">
            <a class="top-nav__link${!meta.canonicalUrl().contains("/guides/") && !meta.canonicalUrl().contains("/tools/") && !meta.canonicalUrl().contains("/admin") ? " is-active" : ""}" href="/">
                Find Your City
            </a>
            <a class="top-nav__link${meta.canonicalUrl().contains("/guides/") ? " is-active" : ""}" href="/guides/fog-vs-grease-trap-cleaning">
                Evergreen Guides
            </a>
            <a class="top-nav__link${meta.canonicalUrl().contains("/tools/") ? " is-active" : ""}" href="/tools/grease-log">
                Operator Tools
            </a>
            <a class="button button--dark" href="/admin">Sign In</a>
        </nav>
    </div>
</header>
