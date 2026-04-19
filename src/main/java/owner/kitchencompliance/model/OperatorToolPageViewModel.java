package owner.kitchencompliance.model;

import java.util.List;

public record OperatorToolPageViewModel(
        PageMeta meta,
        String slug,
        String eyebrow,
        String title,
        String summary,
        List<String> checklist,
        List<DownloadLink> downloads,
        List<RelatedPageLink> relatedLinks,
        List<String> sendSections,
        List<String> languageGuardrails,
        List<String> deliveryChecklist,
        String emailSubject,
        List<String> emailBodyLines,
        List<String> vendorWorkflowMoments,
        List<String> customerOutcomes,
        List<String> repeatReasons,
        SampleReportPreview sampleReportPreview,
        List<String> referenceAddOnNotes,
        String referenceSnippet,
        VendorSetupPanel vendorSetupPanel,
        HoodServiceReportForm hoodServiceReportForm,
        ActiveReferenceLink activeReferenceLink,
        SendReadinessPanel sendReadinessPanel,
        HoodPacketSummary hoodPacketSummary,
        HoodAttachmentBundle hoodAttachmentBundle,
        String emailDraftMailto
) {

    public record DownloadLink(
            String label,
            String path,
            String note
    ) {
    }

    public record SampleReportPreview(
            String title,
            String summary,
            List<String> headerFacts,
            List<String> completedItems,
            List<String> attachedProof,
            List<String> proofPackItems,
            List<String> followUpItems,
            String nextServiceLine,
            String customerHandoffLine
    ) {
    }

    public record VendorSetupPanel(
            String title,
            String summary,
            List<String> includedItems,
            String ctaLabel,
            String ctaPath
    ) {
    }

    public record HoodServiceReportForm(
            String actionPath,
            List<CityOption> cityOptions,
            String selectedCity,
            String serviceDate,
            String nextServiceDate,
            String customerName,
            String siteName,
            String recipientName,
            String recipientEmail,
            String siteAddress,
            String vendorName,
            String crewOrTechnician,
            String workOrderReference,
            String systemsServiced,
            String completedWork,
            String photoReference,
            String reportReference,
            String customerHandoffNote,
            String followUpItems,
            String followUpOwner,
            String followUpDueDate,
            boolean reportAttachmentReady,
            boolean photoSetAttached,
            boolean reportFileAttached,
            boolean referenceLinkAdded,
            boolean includeReferenceLink
    ) {
    }

    public record CityOption(
            String value,
            String label
    ) {
    }

    public record ActiveReferenceLink(
            String label,
            String path,
            String absoluteUrl,
            String note
    ) {
    }

    public record SendReadinessPanel(
            boolean ready,
            String title,
            String summary,
            List<String> items
    ) {
    }

    public record HoodPacketSummary(
            String title,
            String summary,
            int readyItems,
            int totalItems,
            List<PacketItem> items
    ) {
    }

    public record HoodAttachmentBundle(
            String title,
            String summary,
            List<AttachmentBundleItem> items
    ) {
    }

    public record PacketItem(
            String label,
            boolean ready,
            String note
    ) {
    }

    public record AttachmentBundleItem(
            String label,
            boolean ready,
            String suggestedName,
            String note
    ) {
    }
}
