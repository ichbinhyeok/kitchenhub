package owner.kitchencompliance.model;

import java.util.List;
import owner.kitchencompliance.data.RouteTemplate;

public record LocalPageViewModel(
        PageMeta meta,
        RouteTemplate template,
        String sourcePath,
        String cityEntryPath,
        String canonicalPath,
        String kicker,
        String title,
        String summary,
        String authorityName,
        String authorityTypeLabel,
        String city,
        String state,
        String officialHeading,
        List<String> officialRequirements,
        String recordsHeading,
        List<String> onSiteRecords,
        String riskHeading,
        List<String> failureReasons,
        String actionsHeading,
        List<String> nextActions,
        String noteTitle,
        String noteBody,
        String officialListStatement,
        String providerModeSummary,
        CallToAction callToAction,
        LeadCapturePanel operatorLeadPanel,
        LeadCapturePanel sponsorPanel,
        SubmissionNotice submissionNotice,
        CityVerdict cityVerdict,
        ProviderRoutingDecision routingDecision,
        List<ProviderCard> providers,
        List<RelatedPageLink> relatedLinks,
        List<RelatedPageLink> operatorToolLinks,
        List<SourceAttribution> sources,
        String governanceHeading,
        String governanceBody,
        String trustBannerTitle,
        String trustBannerBody,
        List<String> verificationChecklist,
        boolean prioritizeOperatorLeadPanel
) {
}
