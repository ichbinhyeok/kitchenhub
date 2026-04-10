package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum IssueType {

    @JsonProperty("fog_cleaning")
    FOG_CLEANING,

    @JsonProperty("manifest_or_log")
    MANIFEST_OR_LOG,

    @JsonProperty("hood_cleaning")
    HOOD_CLEANING,

    @JsonProperty("inspection_prep")
    INSPECTION_PREP,

    @JsonProperty("vendor_search")
    VENDOR_SEARCH,

    @JsonProperty("operator_utility")
    OPERATOR_UTILITY
}
