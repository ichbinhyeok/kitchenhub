@import owner.kitchencompliance.model.LeadCapturePanel

@param String sourcePath
@param LeadCapturePanel panel

<form class="lead-form" action="${panel.submitPath()}" method="post">
    <input type="hidden" name="source" value="${sourcePath}">
    <input type="hidden" name="formLoadedAt" value="${System.currentTimeMillis()}">

    <div aria-hidden="true" style="position:absolute;left:-10000px;top:auto;width:1px;height:1px;overflow:hidden;">
        <label>
            <span>Website</span>
            <input type="text" name="website" tabindex="-1" autocomplete="off" value="">
        </label>
    </div>

    <label class="lead-form__field">
        <span>Contact name</span>
        <input type="text" name="contactName" required>
    </label>

    <label class="lead-form__field">
        <span>${panel.sponsor() ? "Company name" : "Restaurant or business name"}</span>
        <input type="text" name="businessName" required>
    </label>

    <div class="lead-form__row">
        <label class="lead-form__field">
            <span>Email</span>
            <input type="email" name="email" required>
        </label>
        <label class="lead-form__field">
            <span>Phone</span>
            <input type="text" name="phone">
        </label>
    </div>

    @if(panel.sponsor())
        <label class="lead-form__field">
            <span>Coverage note</span>
            <textarea name="coverageNote" rows="2" placeholder="Markets, category, or slot interest"></textarea>
        </label>
    @endif

    <label class="lead-form__field">
        <span>${panel.sponsor() ? "Anything we should know about your placement request?" : "What service gap needs to be closed?"}</span>
        <textarea name="notes" rows="3" placeholder="${panel.sponsor() ? "Inventory depth, category, or timing" : "Inspection date, missing paperwork, or service urgency"}"></textarea>
    </label>

    <label class="lead-form__consent">
        <input type="checkbox" name="routingConsent" value="true" required>
        <span>${panel.consentLabel()}</span>
    </label>

    <button class="button button--submit" type="submit">${panel.submitLabel()}</button>
</form>
