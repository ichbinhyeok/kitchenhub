package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PageFamily {

    @JsonProperty("fog_rules")
    FOG_RULES,

    @JsonProperty("approved_haulers")
    APPROVED_HAULERS,

    @JsonProperty("hood_requirements")
    HOOD_REQUIREMENTS,

    @JsonProperty("inspection_checklist")
    INSPECTION_CHECKLIST,

    @JsonProperty("provider_finder")
    PROVIDER_FINDER,

    @JsonProperty("operator_tool")
    OPERATOR_TOOL
}
