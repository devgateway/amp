package org.digijava.kernel.ampapi.endpoints.settings;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.*;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.DbUtil;

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
     *
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
     *
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
     *
     * @return a list of setting definitions
     */
    @GET
    @Path("/public")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForPublicPortal() {
        return Arrays.asList(
                getAmountUnitsField(),
                getCurrencyField());
    }

    /**
     * Returns setting definitions for reports.
     *
     * @return a list of setting definitions
     */
    @GET
    @Path("/reports")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForReports() {
        return Arrays.asList(
                getCurrencyField(),
                getCalendarField(),
                getCalendarCurrenciesField(),
                getReportYearRangeField());
    }

    /**
     * Returns setting definitions for a specific report.
     *
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
     *
     * @return a list of setting definitions
     */
    @GET
    @Path("/tabs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForTabs() {
        return Arrays.asList(
                getCurrencyField(),
                getCalendarField());
    }

    @Override
    public Class getErrorsClass() {
        return SettingsDefinitionsErrors.class;
    }
}
