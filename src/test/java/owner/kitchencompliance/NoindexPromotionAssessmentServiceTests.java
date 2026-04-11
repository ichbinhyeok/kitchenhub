package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import owner.kitchencompliance.data.RouteRecord;
import owner.kitchencompliance.data.RouteTemplate;
import owner.kitchencompliance.ops.NoindexPromotionAssessmentService;

@SpringBootTest
class NoindexPromotionAssessmentServiceTests {

    @Autowired
    private NoindexPromotionAssessmentService noindexPromotionAssessmentService;

    @Test
    void syntheticNoindexFinderGetsPromotionChecklistAndReviewDate() {
        RouteRecord route = new RouteRecord(
                "/nc/charlotte/find-grease-service",
                RouteTemplate.FIND_GREASE_SERVICE,
                "nc",
                "charlotte",
                "charlotte-water-flow-free",
                "charlotte-nc-kitchen-compliance",
                "/nc/charlotte/find-grease-service",
                false,
                "Keep finder noindex until promotion conditions are complete.",
                null,
                null,
                LocalDate.of(2026, 4, 21),
                OffsetDateTime.parse("2026-04-07T15:00:00+09:00")
        );

        var assessment = noindexPromotionAssessmentService.assess(route);

        assertThat(assessment.cityLabel()).isEqualTo("Charlotte, NC");
        assertThat(assessment.status().label()).isEqualTo("Ready to promote");
        assertThat(assessment.readyToPromote()).isTrue();
        assertThat(assessment.nextReviewOn()).isEqualTo(LocalDate.of(2026, 4, 21));
        assertThat(assessment.checklist()).anySatisfy(step ->
                assertThat(step).contains("Review the route on or before 2026-04-21"));
        assertThat(assessment.renderableProviders()).isGreaterThanOrEqualTo(3);
    }
}
