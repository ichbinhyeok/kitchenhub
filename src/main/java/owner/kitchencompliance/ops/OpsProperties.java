package owner.kitchencompliance.ops;

import java.nio.file.Path;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ops")
public record OpsProperties(
        String auditDir,
        boolean freshnessAuditEnabled,
        String freshnessAuditCron,
        String freshnessAuditZone
) {
    public Path auditDirectoryPath() {
        return Path.of(auditDir).toAbsolutePath().normalize();
    }
}
