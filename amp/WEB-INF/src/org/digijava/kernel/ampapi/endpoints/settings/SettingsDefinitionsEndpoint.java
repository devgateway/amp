package org.digijava.kernel.ampapi.endpoints.settings;

import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getReportYearRangeField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getCurrencyField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getCalendarField;
import static org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils.getFundingTypeField;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.DbUtil;

/**
 * @author Octavian Ciubotaru
 */
@Path("settings-definitions")
public class SettingsDefinitionsEndpoint {

    @GET
    @Path("/dashboards")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForDashboards() {
        return Arrays.asList(getCurrencyField(), getCalendarField());
    }

    @GET
    @Path("/gis")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForGisModule() {
        return Arrays.asList(getCurrencyField(), getCalendarField(), getFundingTypeField());
    }

    @GET
    @Path("/public")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForPublicPortal() {
        return Collections.emptyList(); // TODO return correct settings
    }

    @GET
    @Path("/reports")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForReports() {
        return Arrays.asList(getCurrencyField(), getCalendarField(), getReportYearRangeField());
    }

    @GET
    @Path("/report/{report_id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final List<SettingField> getSettingDefinitionsForReport(@PathParam("report_id") Long reportId) {
        AmpReports ampReport = DbUtil.getAmpReport(reportId);
        ReportSpecification spec = AmpReportsToReportSpecification.convert(ampReport);
        return Arrays.asList(getCurrencyField(), getCalendarField(), getReportYearRangeField(spec));
    }
}
