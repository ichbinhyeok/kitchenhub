package owner.kitchencompliance.web;

import java.nio.file.Path;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.leads")
public record LeadCaptureProperties(
        boolean enabled,
        String logDir
) {
    public Path logDirectoryPath() {
        return Path.of(logDir).toAbsolutePath().normalize();
    }

    public Path logFilePath() {
        return logDirectoryPath().resolve("lead-intake.csv");
    }
}
