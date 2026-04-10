package owner.kitchencompliance;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import owner.kitchencompliance.ops.SourceQualityVerificationService;

@SpringBootTest
class SourceQualityVerificationLaunchTests {

    @Autowired
    private SourceQualityVerificationService sourceQualityVerificationService;

    @Test
    void indexedRoutesMeetSourceQualityGateForLaunch() {
        assertThatCode(() -> sourceQualityVerificationService.verifyIndexedRoutes()).doesNotThrowAnyException();
    }
}
