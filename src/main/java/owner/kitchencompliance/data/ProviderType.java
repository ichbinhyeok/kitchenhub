package owner.kitchencompliance.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProviderType {

    @JsonProperty("grease_hauler")
    GREASE_HAULER,

    @JsonProperty("grease_trap_service")
    GREASE_TRAP_SERVICE,

    @JsonProperty("hood_cleaner")
    HOOD_CLEANER,

    @JsonProperty("suppression_vendor")
    SUPPRESSION_VENDOR
}
