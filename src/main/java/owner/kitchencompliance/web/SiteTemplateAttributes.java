package owner.kitchencompliance.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SiteTemplateAttributes {

    private final SiteProperties siteProperties;

    public SiteTemplateAttributes(SiteProperties siteProperties) {
        this.siteProperties = siteProperties;
    }

    @ModelAttribute("ga4MeasurementId")
    public String ga4MeasurementId() {
        return siteProperties.ga4MeasurementId();
    }
}
