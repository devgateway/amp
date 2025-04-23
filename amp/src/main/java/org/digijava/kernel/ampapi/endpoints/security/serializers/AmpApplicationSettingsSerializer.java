package org.digijava.kernel.ampapi.endpoints.security.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.StringProperty;
import org.digijava.kernel.ampapi.swagger.converters.ModelDescriber;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.helper.Constants;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Octavian Ciubotaru
 */
public class AmpApplicationSettingsSerializer extends JsonSerializer<AmpApplicationSettings>
        implements ModelDescriber {

    public static final int EXAMPLE_REC_PER_PAGE = 100;
    public static final int EXAMPLE_START_YEAR = 2015;
    public static final int EXAMPLE_END_YEAR = 2017;

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

    @Override
    public Model describe() {
        ModelImpl model = new ModelImpl();

        model.name("AmpApplicationSettings");

        model.addProperty("id", new LongProperty());
        model.addProperty("workspace-id", new LongProperty());
        model.addProperty("default-records-per-page", new IntegerProperty().example(EXAMPLE_REC_PER_PAGE));
        model.addProperty("report-start-year", new IntegerProperty().example(EXAMPLE_START_YEAR)
                .description("in reports, display year columns starting from this year"));
        model.addProperty("report-end-year", new IntegerProperty().example(EXAMPLE_END_YEAR)
                .description("in reports, display year columns up to this year"));
        model.addProperty("currency", new StringProperty().example("USD"));
        model.addProperty("fiscal-calendar", new LongProperty());
        model.addProperty("language", new StringProperty().example("en"));
        model.addProperty("validation", new StringProperty()._enum(
                Arrays.asList(Constants.PROJECT_VALIDATION_OFF, Constants.PROJECT_VALIDATION_FOR_ALL_EDITS)));
        model.addProperty("show-all-countries", new BooleanProperty().example(false));
        model.addProperty("default-team-report", new LongProperty().example(null));
        model.addProperty("default-reports-per-page", new IntegerProperty());
        model.addProperty("allow-add-team-res", new IntegerProperty().example(1)
                .description("documents adding policy (1-3)"));
        model.addProperty("allow-share-team-res", new IntegerProperty().example(1)
                .description("documents sharing policy (1-2)"));
        model.addProperty("allow-publishing-resources", new IntegerProperty().example(1)
                .description("documents sharing policy (1-3)"));
        model.addProperty("number-of-pages-to-display", new IntegerProperty().example(null));

        return model;
    }
}
