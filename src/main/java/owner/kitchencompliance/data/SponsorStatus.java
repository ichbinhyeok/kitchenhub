package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SponsorStatus {

    @JsonProperty("prospect")
    PROSPECT,

    @JsonProperty("active")
    ACTIVE,

    @JsonProperty("hold")
    HOLD
}
