package owner.kitchencompliance.ops;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class OpsConfiguration {

    @Bean
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }
}
