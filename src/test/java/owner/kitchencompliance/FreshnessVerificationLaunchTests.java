package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import owner.kitchencompliance.ops.FreshnessVerificationService;

@SpringBootTest
class FreshnessVerificationLaunchTests {

    @Autowired
    private FreshnessVerificationService freshnessVerificationService;

    @Test
    void indexedRoutesRemainFreshForLaunch() {
        assertThatCode(() -> freshnessVerificationService.verifyIndexedRoutes()).doesNotThrowAnyException();
    }
}
