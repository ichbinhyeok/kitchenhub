package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum InspectionType {

    @JsonProperty("fog")
    FOG,

    @JsonProperty("fire")
    FIRE,

    @JsonProperty("hood")
    HOOD
}
