package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RouteTemplate {

    @JsonProperty("fog-rules")
    FOG_RULES("fog-rules", PageFamily.FOG_RULES),

    @JsonProperty("approved-haulers")
    APPROVED_HAULERS("approved-haulers", PageFamily.APPROVED_HAULERS),

    @JsonProperty("hood-requirements")
    HOOD_REQUIREMENTS("hood-requirements", PageFamily.HOOD_REQUIREMENTS),

    @JsonProperty("inspection-checklist")
    INSPECTION_CHECKLIST("inspection-checklist", PageFamily.INSPECTION_CHECKLIST),

    @JsonProperty("find-grease-service")
    FIND_GREASE_SERVICE("provider-finder", PageFamily.PROVIDER_FINDER),

    @JsonProperty("find-hood-cleaner")
    FIND_HOOD_CLEANER("provider-finder", PageFamily.PROVIDER_FINDER);

    private final String viewName;
    private final PageFamily pageFamily;

    RouteTemplate(String viewName, PageFamily pageFamily) {
        this.viewName = viewName;
        this.pageFamily = pageFamily;
    }

    public String viewName() {
        return viewName;
    }

    public PageFamily pageFamily() {
        return pageFamily;
    }
}
