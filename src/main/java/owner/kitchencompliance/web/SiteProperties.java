package owner.kitchencompliance.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.site")
public record SiteProperties(
        String baseUrl,
        String title,
        String defaultState,
        String ga4MeasurementId
) {
}
