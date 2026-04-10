package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AuthorityType {

    @JsonProperty("utility")
    UTILITY("Utility"),

    @JsonProperty("fire_ahj")
    FIRE_AHJ("Fire AHJ"),

    @JsonProperty("city_department")
    CITY_DEPARTMENT("City department");

    private final String label;

    AuthorityType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
