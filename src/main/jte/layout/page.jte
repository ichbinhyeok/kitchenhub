@import gg.jte.Content
@import owner.kitchencompliance.model.PageMeta
@import java.time.format.DateTimeFormatter

@param PageMeta meta
@param String ga4MeasurementId
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
    <meta name="theme-color" content="#030813">
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
    <link rel="icon" href="/favicon.svg?v=20260412" type="image/svg+xml">
    <link rel="icon" href="/favicon-32.png?v=20260412" sizes="32x32" type="image/png">
    <link rel="shortcut icon" href="/favicon.ico?v=20260412">
    <link rel="apple-touch-icon" href="/apple-touch-icon.png?v=20260412" sizes="180x180">
    <link rel="manifest" href="/site.webmanifest?v=20260412">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/site.css">
    @if(ga4MeasurementId != null && !ga4MeasurementId.isBlank())
        <script async src="https://www.googletagmanager.com/gtag/js?id=$unsafe{ga4MeasurementId}"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag(){dataLayer.push(arguments);}
            gtag('js', new Date());

            gtag('config', '$unsafe{ga4MeasurementId}');
        </script>
    @endif
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
