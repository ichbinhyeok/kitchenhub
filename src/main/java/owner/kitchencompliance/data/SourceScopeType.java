package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SourceScopeType {

    @JsonProperty("authority")
    AUTHORITY,

    @JsonProperty("fog_rule")
    FOG_RULE,

    @JsonProperty("hood_rule")
    HOOD_RULE,

    @JsonProperty("inspection_prep")
    INSPECTION_PREP
}
