@import gg.jte.Content
@import owner.kitchencompliance.model.PageMeta
@import java.time.format.DateTimeFormatter

@param PageMeta meta
@param Content content

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${meta.title()}</title>
    <meta name="description" content="${meta.description()}">
    <meta name="robots" content="${meta.robots()}">
    <meta name="application-name" content="KitchenRuleHub">
    <meta property="og:title" content="${meta.title()}">
    <meta property="og:description" content="${meta.description()}">
    <meta property="og:url" content="${meta.canonicalUrl()}">
    <meta property="og:site_name" content="KitchenRuleHub">
    <meta property="og:type" content="website">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="${meta.title()}">
    <meta name="twitter:description" content="${meta.description()}">
    @if(meta.lastVerified() != null)
        <meta name="last-modified" content="${DateTimeFormatter.ISO_LOCAL_DATE.format(meta.lastVerified())}">
    @endif
    <link rel="canonical" href="${meta.canonicalUrl()}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/site.css">
    @if(meta.structuredDataJson() != null)
        <script type="application/ld+json">$unsafe{meta.structuredDataJson()}</script>
    @endif
</head>
<body>
    @template.layout.header(meta)
    ${content}
    @template.layout.footer()
</body>
</html>
