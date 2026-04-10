package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum VerificationStatus {

    @JsonProperty("verified")
    VERIFIED,

    @JsonProperty("monitoring")
    MONITORING,

    @JsonProperty("stale")
    STALE
}
