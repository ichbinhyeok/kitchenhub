package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import owner.kitchencompliance.data.SourceRecord;
import owner.kitchencompliance.data.SourceScopeType;
import owner.kitchencompliance.data.SourceTier;
import owner.kitchencompliance.ops.SourceFreshnessAssessment;
import owner.kitchencompliance.ops.SourceFreshnessService;

class SourceFreshnessServiceTests {

    @Test
    void assessMarksSourcesStaleOnceTheReviewDatePasses() {
        SourceFreshnessService sourceFreshnessService = new SourceFreshnessService(
                Clock.fixed(Instant.parse("2026-04-07T00:00:00Z"), ZoneOffset.UTC)
        );
        SourceRecord freshSource = source("fresh", LocalDate.of(2026, 4, 7), LocalDate.of(2026, 4, 8));
        SourceRecord staleSource = source("stale", LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 6));

        List<SourceFreshnessAssessment> assessments = sourceFreshnessService.assess(List.of(freshSource, staleSource));

        assertThat(assessments).extracting(SourceFreshnessAssessment::sourceId, SourceFreshnessAssessment::fresh)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple("fresh", true),
                        org.assertj.core.groups.Tuple.tuple("stale", false)
                );
        assertThat(assessments.get(1).statusMessage()).contains("Stale on 2026-04-07");
    }

    private SourceRecord source(String id, LocalDate verifiedOn, LocalDate nextReviewOn) {
        return new SourceRecord(
                id,
                SourceScopeType.FOG_RULE,
                "scope",
                SourceTier.TIER_1,
                "Agency",
                "Title " + id,
                "https://example.com/" + id,
                "summary",
                verifiedOn,
                nextReviewOn
        );
    }
}
