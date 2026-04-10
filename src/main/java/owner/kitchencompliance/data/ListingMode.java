package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ListingMode {

    @JsonProperty("public")
    PUBLIC,

    @JsonProperty("sponsor_only")
    SPONSOR_ONLY
}
