package org.digijava.kernel.ampapi.endpoints.settings;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getCalendarCurrenciesField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getCalendarField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getCurrencyField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getEnabledProgramField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getFundingTypeField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getReportAmountFormatField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getReportAmountUnits;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getReportYearRangeField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getResourceManagerSettings;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.converters.AmpReportsToReportSpecification;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.reports.ReportErrors;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.ampapi.endpoints.util.PublicConstants;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.DbUtil;

/**
 * This endpoint returns settings definitions for each module. Settings
 * definitions were designed to be consumed by Settings Widget.
 * <p>
 * See also {@link SettingField}.
 * </p>
 *
 * @author Octavian Ciubotaru
 */
@Path("settings-definitions")
@Api("settings-definitions")
public class SettingsDefinitionsEndpoint {

    @GET
    @Path("/dashboards")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Setting definitions for dashboards",
            notes = "Returns definitions for the following settings: currency, calendar, calendar currencies, "
                    + "funding type.")
    public final List<SettingField> getSettingDefinitionsForDashboards() {
        return Arrays.asList(getCurrencyField(true), getCalendarField(), getCalendarCurrenciesField(),
                getFundingTypeField(new LinkedHashSet<>(GisConstants.FUNDING_TYPES)), getEnabledProgramField());
    }

    @GET
    @Path("/gis")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Setting definitions for GIS",
            notes = "Returns definitions for the following settings: currency, calendar, calendar currencies, "
                    + "funding type.")
    public final List<SettingField> getSettingDefinitionsForGisModule() {
        return Arrays.asList(getCurrencyField(true), getCalendarField(), getCalendarCurrenciesField(),
                getFundingTypeField(new LinkedHashSet<>(GisConstants.FUNDING_TYPES)));
    }

    @GET
    @Path("/public")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Setting definitions for public portal",
            notes = "Returns definitions for the following settings: amount units, currency, funding type.")
    public final List<SettingField> getSettingDefinitionsForPublicPortal() {
        return Arrays.asList(getReportAmountFormatField(), getCurrencyField(true), getFundingTypeField(new
                LinkedHashSet<>(PublicConstants.FUNDING_TYPES)));
    }

    @GET
    @Path("/reports")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Setting definitions for reports",
            notes = "Returns definitions for the following settings: currency, calendar, calendar currencies, "
                    + "year range.")
    public final List<SettingField> getSettingDefinitionsForReports() {
        return Arrays.asList(getCurrencyField(true), getCalendarField(), getCalendarCurrenciesField(),
                getReportYearRangeField(), getReportAmountUnits());
    }

    @GET
    @Path("/report/{report_id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Setting definitions for a specific report",
            notes = "Returns definitions for the following settings: currency, calendar, calendar currencies, "
                    + "year range.")
    public final List<SettingField> getSettingDefinitionsForReport(@PathParam("report_id") Long reportId) {
        AmpReports ampReport = DbUtil.getAmpReport(reportId);
        if (ampReport == null) {
            ApiErrorResponseService.reportError(BAD_REQUEST, ReportErrors.REPORT_NOT_FOUND);
        }

        ReportSpecification spec = AmpReportsToReportSpecification.convert(ampReport);
        return Arrays.asList(getCurrencyField(true), getCalendarField(), getCalendarCurrenciesField(),
                getReportYearRangeField(spec));
    }

    @GET
    @Path("/tabs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Setting definitions for tabs",
            notes = "Returns definitions for the following settings: currency, calendar, calendar currencies.")
    public final List<SettingField> getSettingDefinitionsForTabs() {
        return Arrays.asList(getCurrencyField(true), getCalendarField(), getCalendarCurrenciesField());
    }

    @GET
    @Path("/resource-manager")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Setting definitions for Resource Manager",
            notes = "Returns definitions for the following settings: resource manager.")
    public final List<SettingField> getSettingDefinitionsForResourceManager() {
        return getResourceManagerSettings();
    }

    @GET
    @Path("/gpi-data")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Setting definitions for gpi data entry",
            notes = "Returns definitions for the following settings: currency (does not include deflated currencies).")
    public final List<SettingField> getSettingDefinitionsForGPIData() {
        return Arrays.asList(getCurrencyField(false));
    }

    @GET
    @Path("/gpi-reports")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Setting definitions for GPI Reports",
            notes = "Returns definitions for the following settings: currency, calendar.")
    public final List<SettingField> getSettingDefinitionsForGPIReports() {
        return Arrays.asList(getCurrencyField(true), getCalendarField(), getCalendarCurrenciesField());
    }

}
