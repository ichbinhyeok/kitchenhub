package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SourceTier {

    @JsonProperty("tier_1")
    TIER_1("Tier 1"),

    @JsonProperty("tier_2")
    TIER_2("Tier 2"),

    @JsonProperty("tier_3")
    TIER_3("Tier 3"),

    @JsonProperty("tier_4")
    TIER_4("Tier 4");

    private final String label;

    SourceTier(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
