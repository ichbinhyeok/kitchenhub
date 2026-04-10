package owner.kitchencompliance.data;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SeedRegistry {

    private static final String CLASSPATH_PREFIX = "classpath*:";

    private final Map<String, AuthorityRecord> authoritiesById;
    private final Map<String, CityComplianceProfile> profilesById;
    private final Map<String, FogRuleRecord> fogRulesByProfileId;
    private final Map<String, HoodRuleRecord> hoodRulesByProfileId;
    private final Map<String, InspectionPrepRecord> inspectionRecordsByProfileId;
    private final Map<String, RouteRecord> routesByPath;
    private final Map<String, SourceRecord> sourcesById;
    private final Map<String, ProviderRecord> providersById;
    private final List<ProviderRecord> providers;

    public SeedRegistry(ResourcePatternResolver resolver, ObjectMapper objectMapper, Validator validator) {
        List<AuthorityRecord> authorities = loadDirectory(
                resolver, objectMapper, validator, "data/authorities/*.json", AuthorityRecord.class);
        List<CityComplianceProfile> profiles = loadDirectory(
                resolver, objectMapper, validator, "data/city-compliance-profiles/*.json", CityComplianceProfile.class);
        List<FogRuleRecord> fogRules = loadDirectory(
                resolver, objectMapper, validator, "data/fog-rules/*.json", FogRuleRecord.class);
        List<HoodRuleRecord> hoodRules = loadDirectory(
                resolver, objectMapper, validator, "data/hood-rules/*.json", HoodRuleRecord.class);
        List<InspectionPrepRecord> inspectionPrepRecords = loadDirectory(
                resolver, objectMapper, validator, "data/inspection-prep/*.json", InspectionPrepRecord.class);
        List<ProviderRecord> providerRecords = loadDirectory(
                resolver, objectMapper, validator, "data/providers/*.json", ProviderRecord.class);
        List<RouteRecord> routeRecords = loadDirectory(
                resolver, objectMapper, validator, "data/routes/*.json", RouteRecord.class);
        List<SourceRecord> sourceRecords = loadDirectory(
                resolver, objectMapper, validator, "data/sources/*.json", SourceRecord.class);

        this.authoritiesById = indexBy(authorities, AuthorityRecord::authorityId);
        this.profilesById = indexBy(profiles, CityComplianceProfile::profileId);
        this.fogRulesByProfileId = fogRules.stream()
                .collect(Collectors.toUnmodifiableMap(this::profileKey, Function.identity()));
        this.hoodRulesByProfileId = hoodRules.stream()
                .collect(Collectors.toUnmodifiableMap(this::profileKey, Function.identity()));
        this.inspectionRecordsByProfileId = inspectionPrepRecords.stream()
                .collect(Collectors.toUnmodifiableMap(this::profileKey, Function.identity()));
        this.routesByPath = routeRecords.stream()
                .collect(Collectors.toUnmodifiableMap(RouteRecord::path, Function.identity()));
        this.sourcesById = indexBy(sourceRecords, SourceRecord::sourceId);
        this.providersById = indexBy(providerRecords, ProviderRecord::providerId);
        this.providers = List.copyOf(providerRecords);

        validateCrossReferences();
    }

    public Map<String, AuthorityRecord> authoritiesById() {
        return authoritiesById;
    }

    public CityComplianceProfile profile(String profileId) {
        return required(profilesById, profileId, "profile");
    }

    public List<CityComplianceProfile> profiles() {
        return profilesById.values().stream().toList();
    }

    public AuthorityRecord authority(String authorityId) {
        return required(authoritiesById, authorityId, "authority");
    }

    public FogRuleRecord fogRule(String profileId) {
        return required(fogRulesByProfileId, cityKey(profile(profileId)), "fog rule");
    }

    public HoodRuleRecord hoodRule(String profileId) {
        return required(hoodRulesByProfileId, cityKey(profile(profileId)), "hood rule");
    }

    public InspectionPrepRecord inspectionPrep(String profileId) {
        return required(inspectionRecordsByProfileId, cityKey(profile(profileId)), "inspection prep");
    }

    public RouteRecord route(String path) {
        return required(routesByPath, path, "route");
    }

    public List<RouteRecord> routes() {
        return routesByPath.values().stream().toList();
    }

    public RouteRecord routeFor(String profileId, RouteTemplate template) {
        return routesByPath.values().stream()
                .filter(route -> route.profileId().equals(profileId))
                .filter(route -> route.template() == template)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Missing route for profile " + profileId + " and template " + template));
    }

    public List<ProviderRecord> providersFor(String profileId, ProviderType providerType) {
        return providers.stream()
                .filter(provider -> provider.providerType() == providerType)
                .filter(provider -> provider.coverageTargets().contains(profileId))
                .toList();
    }

    public ProviderRecord provider(String providerId) {
        return required(providersById, providerId, "provider");
    }

    public List<SourceRecord> sourcesFor(RouteRecord route) {
        return switch (route.template()) {
            case FOG_RULES, APPROVED_HAULERS, FIND_GREASE_SERVICE ->
                    sourceRecordsForIds(fogRule(route.profileId()).sourceRefs());
            case HOOD_REQUIREMENTS, FIND_HOOD_CLEANER ->
                    sourceRecordsForIds(hoodRule(route.profileId()).sourceRefs());
            case INSPECTION_CHECKLIST ->
                    sourceRecordsForIds(inspectionPrep(route.profileId()).sourceRefs());
        };
    }

    public LocalDate lastVerifiedFor(RouteRecord route) {
        return switch (route.template()) {
            case FOG_RULES, APPROVED_HAULERS, FIND_GREASE_SERVICE -> fogRule(route.profileId()).lastVerified();
            case HOOD_REQUIREMENTS, FIND_HOOD_CLEANER -> hoodRule(route.profileId()).lastVerified();
            case INSPECTION_CHECKLIST -> latestVerifiedFromSources(inspectionPrep(route.profileId()).sourceRefs());
        };
    }

    private LocalDate latestVerifiedFromSources(List<String> sourceIds) {
        return sourceRecordsForIds(sourceIds).stream()
                .map(SourceRecord::verifiedOn)
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalStateException("Expected at least one source."));
    }

    private List<SourceRecord> sourceRecordsForIds(List<String> sourceIds) {
        return sourceIds.stream()
                .map(sourceId -> required(sourcesById, sourceId, "source"))
                .toList();
    }

    private void validateCrossReferences() {
        profilesById.values().forEach(profile -> {
            authority(profile.fogAuthorityId());
            authority(profile.hoodAuthorityId());
        });

        fogRulesByProfileId.values().forEach(rule -> {
            authority(rule.authorityId());
            validateSourceRefs(rule.sourceRefs());
        });

        hoodRulesByProfileId.values().forEach(rule -> {
            authority(rule.authorityId());
            validateSourceRefs(rule.sourceRefs());
        });

        inspectionRecordsByProfileId.values().forEach(record -> validateSourceRefs(record.sourceRefs()));

        providers.forEach(provider -> provider.coverageTargets().forEach(profileId -> profile(profileId)));

        routesByPath.values().forEach(route -> {
            profile(route.profileId());
            authority(route.authorityId());
            if (!route.path().equals(route.canonicalPath())) {
                throw new IllegalStateException("Route canonical path must match path for v1: " + route.path());
            }
            if (route.indexable()) {
                if (sourcesFor(route).isEmpty()) {
                    throw new IllegalStateException("Indexed route is missing source coverage: " + route.path());
                }
                Objects.requireNonNull(lastVerifiedFor(route), "Indexed route is missing last verified date.");
            }
        });
    }

    private void validateSourceRefs(List<String> sourceRefs) {
        sourceRefs.forEach(sourceId -> required(sourcesById, sourceId, "source"));
    }

    private String profileKey(FogRuleRecord rule) {
        return cityKey(rule.state(), rule.city());
    }

    private String profileKey(HoodRuleRecord rule) {
        return cityKey(rule.state(), rule.city());
    }

    private String profileKey(InspectionPrepRecord record) {
        return cityKey(record.state(), record.city());
    }

    private String cityKey(String state, String city) {
        return state.toLowerCase() + "-" + city.toLowerCase();
    }

    private String cityKey(CityComplianceProfile profile) {
        return cityKey(profile.state(), profile.city());
    }

    private <T> List<T> loadDirectory(
            ResourcePatternResolver resolver,
            ObjectMapper objectMapper,
            Validator validator,
            String pattern,
            Class<T> type
    ) {
        try {
            Resource[] resources = resolver.getResources(CLASSPATH_PREFIX + pattern);
            Arrays.sort(resources, Comparator.comparing(resource -> resource.getFilename() == null ? "" : resource.getFilename()));
            List<T> loaded = new ArrayList<>();
            for (Resource resource : resources) {
                try (InputStream inputStream = resource.getInputStream()) {
                    T value = objectMapper.readValue(inputStream, type);
                    validateBean(value, validator, resource.getFilename());
                    loaded.add(value);
                }
            }
            return List.copyOf(loaded);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load seed data from " + pattern, ex);
        }
    }

    private <T> void validateBean(T value, Validator validator, String filename) {
        Set<ConstraintViolation<T>> violations = validator.validate(value);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .sorted()
                    .collect(Collectors.joining(", "));
            throw new IllegalStateException("Invalid seed record in " + filename + ": " + message);
        }
    }

    private <K, V> Map<K, V> indexBy(List<V> values, Function<V, K> idExtractor) {
        Map<K, V> indexed = new LinkedHashMap<>();
        for (V value : values) {
            K key = idExtractor.apply(value);
            if (indexed.putIfAbsent(key, value) != null) {
                throw new IllegalStateException("Duplicate seed key detected: " + key);
            }
        }
        return Map.copyOf(indexed);
    }

    private <K, V> V required(Map<K, V> values, K key, String label) {
        V value = values.get(key);
        if (value == null) {
            throw new IllegalStateException("Missing " + label + " for key " + key);
        }
        return value;
    }
}
