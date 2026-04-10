package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ApprovedHaulerMode {

    @JsonProperty("official_list")
    OFFICIAL_LIST,

    @JsonProperty("operator_must_verify")
    OPERATOR_MUST_VERIFY,

    @JsonProperty("unclear")
    UNCLEAR
}
