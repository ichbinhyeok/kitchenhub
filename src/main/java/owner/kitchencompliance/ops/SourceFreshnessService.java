package owner.kitchencompliance.ops;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import owner.kitchencompliance.data.SourceRecord;

@Service
public class SourceFreshnessService {

    private final Clock clock;

    public SourceFreshnessService(Clock clock) {
        this.clock = clock;
    }

    public boolean allFresh(List<SourceRecord> sources) {
        return assess(sources).stream().allMatch(SourceFreshnessAssessment::fresh);
    }

    public List<SourceFreshnessAssessment> assess(List<SourceRecord> sources) {
        return sources.stream().map(this::assess).toList();
    }

    public SourceFreshnessAssessment assess(SourceRecord source) {
        LocalDate today = LocalDate.now(clock);
        boolean fresh = !source.nextReviewOn().isBefore(today);
        String statusMessage = fresh
                ? "Fresh through " + source.nextReviewOn()
                : "Stale on " + today + "; next review was due " + source.nextReviewOn();
        return new SourceFreshnessAssessment(
                source.sourceId(),
                source.title(),
                source.verifiedOn(),
                source.nextReviewOn(),
                fresh,
                statusMessage
        );
    }
}
