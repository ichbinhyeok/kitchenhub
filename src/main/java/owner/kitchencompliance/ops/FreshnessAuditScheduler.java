package owner.kitchencompliance.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FreshnessAuditScheduler {

    private static final Logger log = LoggerFactory.getLogger(FreshnessAuditScheduler.class);

    private final OpsProperties properties;
    private final OpsAuditService opsAuditService;

    public FreshnessAuditScheduler(OpsProperties properties, OpsAuditService opsAuditService) {
        this.properties = properties;
        this.opsAuditService = opsAuditService;
    }

    @Scheduled(cron = "${app.ops.freshness-audit-cron:0 15 6 * * *}", zone = "${app.ops.freshness-audit-zone:UTC}")
    public void runScheduledAudit() {
        if (!properties.freshnessAuditEnabled()) {
            return;
        }
        opsAuditService.writeSnapshot();
        log.info("Wrote ops snapshots to {}", properties.auditDirectoryPath());
    }
}
