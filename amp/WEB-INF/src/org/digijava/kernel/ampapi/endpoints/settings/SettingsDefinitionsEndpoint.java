package org.digijava.kernel.ampapi.endpoints.settings;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getCalendarCurrenciesField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getCalendarField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getCurrencyField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getFundingTypeField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getReportAmountFormatField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getReportYearRangeField;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.DbUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

/**
 * This endpoint returns settings definitions for each module.
 * Settings definitions were designed to be consumed by Settings Widget. <p>See also {@link SettingField}.</p>
 *
 * @author Octavian Ciubotaru
 */
@Path("settings-definitions")
public class SettingsDefinitionsEndpoint implements ErrorReportingEndpoint {

    /**
     * Returns setting definitions for dashboards.
     * <p>Returns definitions for the following settings: currency, calendar, calendar currencies, funding type.</p>
     * <h3>Sample Output:</h3>
     * <pre>
     * [
     *   {
     *     "type": "OPTIONS",
     *     "id": "currency-code",
     *     "name": "currency",
     *     "value": {
     *       "multi": false,
     *       "defaultId": "USD",
     *       "options": [
     *         {
     *           "id": "AUD",
     *           "name": "Australian Dollar",
     *           "value": "AUD"
     *         },
     *         {
     *           "id": "CAD",
     *           "name": "Canadian Dollar",
     *           "value": "CAD"
     *         },
     *         ... other options
     *       ]
     *     }
     *   },
     *   ... other fields
     * ]
     * </pre>
     * @return a list of setting definitions
     */
    @GET
    @Path("/dashboards")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForDashboards() {
        return Arrays.asList(
                getCurrencyField(),
                getCalendarField(),
                getCalendarCurrenciesField(),
                getFundingTypeField());
    }

    /**
     * Returns setting definitions for GIS.
     * <p>Returns definitions for the following settings: currency, calendar, calendar currencies, funding type.</p>
     * <h3>Sample Output:</h3>
     * <pre>
     * [
     *   {
     *     "type": "OPTIONS",
     *     "id": "currency-code",
     *     "name": "currency",
     *     "value": {
     *       "multi": false,
     *       "defaultId": "USD",
     *       "options": [
     *         {
     *           "id": "AUD",
     *           "name": "Australian Dollar",
     *           "value": "AUD"
     *         },
     *         {
     *           "id": "CAD",
     *           "name": "Canadian Dollar",
     *           "value": "CAD"
     *         },
     *         ... other options
     *       ]
     *     }
     *   },
     *   ... other fields
     * ]
     * </pre>
     * @return a list of setting definitions
     */
    @GET
    @Path("/gis")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForGisModule() {
        return Arrays.asList(
                getCurrencyField(),
                getCalendarField(),
                getCalendarCurrenciesField(),
                getFundingTypeField());
    }

    /**
     * Returns setting definitions for public portal.
     * <p>Returns definitions for the following settings: amount units, currency.</p>
     * <h3>Sample Output:</h3>
     * <pre>
     * [
     *   {
     *     "type": "OPTIONS",
     *     "id": "number-divider",
     *     "name": "Amount units",
     *     "value": {
     *       "multi": false,
     *       "defaultId": "0",
     *       "options": [
     *         {
     *           "id": "0",
     *           "name": "Amounts are in units",
     *           "value": "1"
     *         },
     *         {
     *           "id": "1",
     *           "name": "Amounts are in thousands (000)",
     *           "value": "1000"
     *         },
     *         ... other options
     *       ]
     *     }
     *   },
     *   ... other fields
     * ]
     * </pre>
     *
     * @return a list of setting definitions
     */
    @GET
    @Path("/public")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForPublicPortal() {
        return Arrays.asList(
                getReportAmountFormatField(),
                getCurrencyField());
    }

    /**
     * Returns setting definitions for reports.
     * <p>Returns definitions for the following settings: currency, calendar, calendar currencies, year range.</p>
     * <h3>Sample Output:</h3>
     * <pre>
     * [
     *   {
     *     "type": "OPTIONS",
     *     "id": "currency-code",
     *     "name": "currency",
     *     "value": {
     *       "multi": false,
     *       "defaultId": "USD",
     *       "options": [
     *         {
     *           "id": "AUD",
     *           "name": "Australian Dollar",
     *           "value": "AUD"
     *         },
     *         {
     *           "id": "CAD",
     *           "name": "Canadian Dollar",
     *           "value": "CAD"
     *         },
     *         ... other options
     *       ]
     *     }
     *   },
     *   ... other fields
     * ]
     * </pre>
     * @return a list of setting definitions
     */
    @GET
    @Path("/reports")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForReports() {
        return Arrays.asList(
                //getCurrencyField(),
                //getCalendarField(),
                //getCalendarCurrenciesField(),
                getReportYearRangeField());
    }

    /**
     * Returns setting definitions for a specific report.
     * <p>Returns definitions for the following settings: currency, calendar, calendar currencies, year range.</p>
     * <h3>Sample Output:</h3>
     * <pre>
     * [
     *   {
     *     "type": "OPTIONS",
     *     "id": "currency-code",
     *     "name": "currency",
     *     "value": {
     *       "multi": false,
     *       "defaultId": "USD",
     *       "options": [
     *         {
     *           "id": "AUD",
     *           "name": "Australian Dollar",
     *           "value": "AUD"
     *         },
     *         {
     *           "id": "CAD",
     *           "name": "Canadian Dollar",
     *           "value": "CAD"
     *         },
     *         ... other options
     *       ]
     *     }
     *   },
     *   ... other fields
     * ]
     * </pre>
     * @param reportId Report Id
     * @return a list of setting definitions
     */
    @GET
    @Path("/report/{report_id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForReport(@PathParam("report_id") Long reportId) {
        AmpReports ampReport = DbUtil.getAmpReport(reportId);
        if (ampReport == null) {
            ApiErrorResponse.reportError(BAD_REQUEST, SettingsDefinitionsErrors.REPORT_NOT_FOUND);
        }

        ReportSpecification spec = AmpReportsToReportSpecification.convert(ampReport);
        return Arrays.asList(
                getCurrencyField(),
                getCalendarField(),
                getCalendarCurrenciesField(),
                getReportYearRangeField(spec));
    }

    /**
     * Returns setting definitions for tabs.
     * <p>Returns definitions for the following settings: currency, calendar.</p>
     * <h3>Sample Output:</h3>
     * <pre>
     * [
     *   {
     *     "type": "OPTIONS",
     *     "id": "currency-code",
     *     "name": "currency",
     *     "value": {
     *       "multi": false,
     *       "defaultId": "USD",
     *       "options": [
     *         {
     *           "id": "AUD",
     *           "name": "Australian Dollar",
     *           "value": "AUD"
     *         },
     *         {
     *           "id": "CAD",
     *           "name": "Canadian Dollar",
     *           "value": "CAD"
     *         },
     *         ... other options
     *       ]
     *     }
     *   },
     *   ... other fields
     * ]
     * </pre>
     * @return a list of setting definitions
     */
    @GET
    @Path("/tabs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForTabs() {
        return Arrays.asList(
                getCurrencyField(),
                getCalendarField(),
                getCalendarCurrenciesField());
    }

    @Override
    public Class getErrorsClass() {
        return SettingsDefinitionsErrors.class;
    }
}
