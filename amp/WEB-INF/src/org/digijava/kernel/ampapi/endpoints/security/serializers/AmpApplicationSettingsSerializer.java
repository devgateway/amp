package org.digijava.kernel.ampapi.endpoints.security.serializers;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;

/**
 * @author Octavian Ciubotaru
 */
public class AmpApplicationSettingsSerializer extends JsonSerializer<AmpApplicationSettings> {

    @Override
    public void serialize(AmpApplicationSettings value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();

        writeField("id", value.getAmpAppSettingsId(), jgen, provider);
        writeField("workspace-id", value.getTeam() != null ? value.getTeam().getAmpTeamId() : null, jgen, provider);
        writeField("default-records-per-page", value.getDefaultRecordsPerPage(), jgen, provider);
        writeField("report-start-year", value.getReportStartYear(), jgen, provider);
        writeField("report-end-year", value.getReportEndYear(), jgen, provider);
        writeField("currency", getCurrency(value), jgen, provider);
        writeField("fiscal-calendar", getFiscalYear(value), jgen, provider);
        writeField("language", value.getLanguage(), jgen, provider);
        writeField("validation", value.getValidation(), jgen, provider);
        writeField("show-all-countries", value.getShowAllCountries(), jgen, provider);
        writeField("default-team-report", getDefaultTeamReport(value), jgen, provider);
        writeField("default-reports-per-page", value.getDefaultReportsPerPage(), jgen, provider);
        writeField("allow-add-team-res", value.getAllowAddTeamRes(), jgen, provider);
        writeField("allow-share-team-res", value.getAllowShareTeamRes(), jgen, provider);
        writeField("allow-publishing-resources", value.getAllowPublishingResources(), jgen, provider);
        writeField("number-of-pages-to-display", value.getNumberOfPagesToDisplay(), jgen, provider);

        jgen.writeEndObject();
    }

    public Long getDefaultTeamReport(AmpApplicationSettings value) {
        return value.getDefaultTeamReport() != null ? value.getDefaultTeamReport().getAmpReportId() : null;
    }

    public Long getFiscalYear(AmpApplicationSettings value) {
        return value.getFiscalCalendar() != null ? value.getFiscalCalendar().getAmpFiscalCalId() : null;
    }

    public Object getCurrency(AmpApplicationSettings value) {
        return value.getCurrency() != null ? value.getCurrency().getCurrencyCode() : null;
    }

    private void writeField(String fieldName, Object value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        provider.defaultSerializeField(fieldName, value, jgen);
    }
}
