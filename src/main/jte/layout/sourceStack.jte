@import java.time.LocalDate
@import java.time.format.DateTimeFormatter
@import java.util.List
@import owner.kitchencompliance.model.SourceAttribution

@param LocalDate lastVerified
@param List<SourceAttribution> sources

<section class="panel source-stack">
    <div class="panel__eyebrow">Source stack</div>
    <h2>Authority-backed sources</h2>
    @if(lastVerified != null)
        <p class="source-stack__date">
            Last verified:
            <strong>${DateTimeFormatter.ISO_LOCAL_DATE.format(lastVerified)}</strong>
        </p>
    @endif
    <ol class="source-list">
        @for(SourceAttribution source : sources)
            <li class="source-list__item">
                <div class="source-list__title">
                    <a href="${source.url()}" rel="noopener noreferrer" target="_blank">${source.title()}</a>
                </div>
                <p class="source-list__meta">
                    <span>${source.agency()}</span>
                    <span>|</span>
                    <span>${source.tierLabel()}</span>
                    <span>|</span>
                    <span>${DateTimeFormatter.ISO_LOCAL_DATE.format(source.verifiedOn())}</span>
                </p>
                <p>${source.summary()}</p>
            </li>
        @endfor
    </ol>
</section>
