package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dgfoundation.amp.gpi.reports.GPIDonorActivityDocument;
import org.dgfoundation.amp.gpi.reports.GPIRemark;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.reports.saiku.export.AMPReportExportConstants;
import org.digijava.kernel.ampapi.endpoints.dto.DateConversionResult;
import org.digijava.kernel.ampapi.endpoints.dto.Org;
import org.digijava.kernel.ampapi.endpoints.dto.SaveResult;
import org.digijava.kernel.ampapi.endpoints.dto.YearsForCalendar;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.dto.ResultPage;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.digijava.module.aim.dbentity.AmpGPINiDonorNotes;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;


@Path("gpi")
@Api("gpi")
public class GPIEndPoints implements ErrorReportingEndpoint {

    @GET
    @Path("/aid-on-budget")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getAidOnBudgetList", ui = false)
    @ApiOperation("List aid on budget objects.")
    public ResultPage<AmpGPINiAidOnBudget> getAidOnBudgetList(@QueryParam("offset") Integer offset,
            @ApiParam("maximum number of records to return") @QueryParam("count") Integer count,
            @ApiParam("field that will be used for sorting") @QueryParam("orderby") String orderBy,
            @ApiParam("asc or desc order") @QueryParam("sort") String sort) {
        return GPIDataService.getAidOnBudgetList(offset, count, orderBy, sort);
    }

    @GET
    @Path("/aid-on-budget/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getAidOnBudgetById", ui = false)
    @ApiOperation("Get aid on budget object by id.")
    public AmpGPINiAidOnBudget getAidOnBudgetById(@PathParam("id") long id) {
        return GPIDataService.getAidOnBudgetById(id);
    }

    @POST
    @Path("/aid-on-budget")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveAidOnBudget", ui = false)
    @ApiOperation("Create or updated aid on budget object.")
    public SaveResult<AmpGPINiAidOnBudget> saveAidOnBudget(AmpGPINiAidOnBudget aidOnBudget) {
        ValidationUtils.requireValid(aidOnBudget);
        return GPIDataService.saveAidOnBudget(aidOnBudget);
    }

    @POST
    @Path("/aid-on-budget/save-all")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveAllEdits", ui = false)
    @ApiOperation("Save a list of aid on budget objects.")
    public List<SaveResult<AmpGPINiAidOnBudget>> saveAllEdits(List<AmpGPINiAidOnBudget> aidOnBudgetList) {
        ValidationUtils.requireValid(aidOnBudgetList);
        return GPIDataService.saveAidOnBudget(aidOnBudgetList);
    }

    @DELETE
    @Path("/aid-on-budget/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "deleteAidOnBudgetById", ui = false)
    @ApiOperation("Delete aid on budget object in the database.")
    public void deleteAidOnBudgetById(@PathParam("id") long id) {
        GPIDataService.deleteAidOnBudgetById(id);
    }

    @POST
    @Path("/donor-notes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveDonorNotes", ui = false)
    @ApiOperation("Save donor notes objects to the database.")
    public SaveResult<AmpGPINiDonorNotes> saveDonorNotes(
            @ApiParam("json representation of the donorNotes object to be saved") AmpGPINiDonorNotes donorNotes) {
        ValidationUtils.requireValid(donorNotes);
        return GPIDataService.saveDonorNotes(donorNotes);
    }

    @POST
    @Path("/donor-notes/save-all")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveAllDonorNotes", ui = false)
    @ApiOperation("Save a list donorNotes objects to the database.")
    public List<SaveResult<AmpGPINiDonorNotes>> saveAllDonorNotes(
            @ApiParam("list of donorNotes objects") List<AmpGPINiDonorNotes> donorNotes) {
        ValidationUtils.requireValid(donorNotes);
        return GPIDataService.saveDonorNotes(donorNotes);
    }

    @GET
    @Path("/donor-notes/{indicatorCode}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getDonorNotesList", ui = false)
    @ApiOperation("Retrieve a list of donor notes.")
    public ResultPage<AmpGPINiDonorNotes> getDonorNotesList(@PathParam("indicatorCode") String indicatorCode,
            @ApiParam("first element in the list") @QueryParam("offset") Integer offset,
            @ApiParam("maximum number of records to return") @QueryParam("count") Integer count,
            @ApiParam("field that will be used for sorting") @QueryParam("orderby") String orderBy,
            @ApiParam("asc or desc order") @QueryParam("sort") String sort) {
        return GPIDataService.getDonorNotesList(offset, count, orderBy, sort, indicatorCode);
    }

    @DELETE
    @Path("/donor-notes/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "deleteDonorNotesId", ui = false)
    @ApiOperation("Delete the donor note object from the database.")
    public void deleteDonorNotesById(@PathParam("id") long id) {
        GPIDataService.deleteDonorNotesById(id);
    }

    @GET
    @Path("/users-verified-orgs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getUsersVerifiedOrganizations", ui = false)
    @ApiOperation("Retrieve a list of verified organizations associated with the logged in user.")
    public List<Org> getUsersVerifiedOrganizations() {
        return GPIDataService.getUsersVerifiedOrganizations();
    }

    @POST
    @Path("/report/{indicatorCode}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getGpiReport", ui = false)
    @ApiOperation("Retrieve gpi report for the specified indicator.")
    public GPIReport getGPIReport(
            @ApiParam(allowableValues = "1,5a,5b,6,9")
            @PathParam("indicatorCode") String indicatorCode,
            GpiFormParameters formParams) {
        return GPIReportService.getInstance().getGPIReport(indicatorCode, formParams);
    }

    @POST
    @Path("/report/export/xls/{indicatorCode}")
    @Produces({"application/vnd.ms-excel" })
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation("Retrieve gpi report in XLSX format.")
    public final Response exportXlsGPIReport(
            @ApiParam(allowableValues = "1,5a,5b,6,9") @PathParam("indicatorCode") String indicatorCode,
            @ApiParam("Stringified body parameter as documented in POST /data/saikureport/{report_id}")
            @FormParam("formParams") GpiFormParameters formParams) {
        return GPIReportService.getInstance().exportGPIReport(indicatorCode, formParams, AMPReportExportConstants.XLSX);
    }

    @POST
    @Path("/report/export/pdf/{indicatorCode}")
    @Produces({"application/pdf" })
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation("Retrieve gpi report in PDF format.")
    public final Response exportPdfGPIReport(
            @ApiParam(allowableValues = "1,5a,5b,6,9") @PathParam("indicatorCode") String indicatorCode,
            @ApiParam("Stringified body parameter as documented in POST /data/saikureport/{report_id}")
            @FormParam("formParams") GpiFormParameters formParams) {
        return GPIReportService.getInstance().exportGPIReport(indicatorCode, formParams, AMPReportExportConstants.PDF);
    }

    @GET
    @Path("/report/remarks/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getGpiReport", ui = false)
    @ApiOperation(
            "Retrieve remarks for the specified indicator code, donor agency/group and between the specified dates.")
    public List<GPIRemark> getGPIRemarks(
            @ApiParam(allowableValues = "1,5a,5b,6,9") @QueryParam("indicatorCode") String indicatorCode,
            @QueryParam("donorId") List<Long> donorIds,
            @ApiParam(allowableValues = "donor-agency,donor-group") @QueryParam("donorType") String donorType,
            @ApiParam("Julian date number") @QueryParam("from") Long from,
            @ApiParam("Julian date number") @QueryParam("to") Long to) {
        
        return GPIDataService.getGPIRemarks(indicatorCode, donorIds, donorType, from, to);
    }

    @POST
    @Path("/report/documents/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getSupportiveDocuments", ui = false)
    @ApiOperation(
            value = "Retrieve documents for the specified donor agency/group and activity.",
            notes = "Supportive documents endpoint is used for retrieving URLS of the survey requested by the "
                    + "parameters donor ID + activity ID.")
    public List<GPIDonorActivityDocument> getSupportiveDocuments(List<GPIDonorActivityDocument> activityDonors) {

        return GPIDataService.getGPIDocuments(activityDonors);
    }

    @GET
    @Path("/report/visibility/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getGPIReportVisibility", ui = false)
    @ApiOperation("Retrieve gpi report visibility for each indicator.")
    public Map<String, Boolean> getGPIReportVisibilityConfiguration() {
        Map<String, Boolean> ret = new HashMap<>();
        ret.put(GPIReportConstants.REPORT_1, FeaturesUtil.isVisibleFeature(GPIReportConstants.REPORT_1_GFM_NAME));
        ret.put(GPIReportConstants.REPORT_5a, FeaturesUtil.isVisibleFeature(GPIReportConstants.REPORT_5a_GFM_NAME));
        ret.put(GPIReportConstants.REPORT_5b, FeaturesUtil.isVisibleFeature(GPIReportConstants.REPORT_5b_GFM_NAME));
        ret.put(GPIReportConstants.REPORT_6, FeaturesUtil.isVisibleFeature(GPIReportConstants.REPORT_6_GFM_NAME));
        ret.put(GPIReportConstants.REPORT_9b, FeaturesUtil.isVisibleFeature(GPIReportConstants.REPORT_9b_GFM_NAME));
        return ret;
    }

    @GET
    @Path("/report/years/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getYears", ui = false)
    @ApiOperation("List years for all known calendars.")
    public List<YearsForCalendar> getYears() {
         return GPIDataService.getYears();
    }

    @GET
    @Path("/report/donors/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getDonors", ui = false)
    @ApiOperation("List all donors.")
    public List<Org> getDonors() {
        return GPIDataService.getDonors(); 
    }
    
    @GET
    @Path("/report/calendars/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getCalendars", ui = false)
    public List<AmpFiscalCalendar> getCalendars() {
        return FiscalCalendarUtil.getAllAmpFiscalCalendars();
    }

    @GET
    @Path("/calendar/convert/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getConvertedDate", ui = false)
    @ApiOperation("Convert a date from one calendar to another")
    public DateConversionResult getConvertedDate(
            @QueryParam("fromCalId") Long fromCalId,
            @QueryParam("toCalId") Long toCalId,
            @ApiParam("sourceDate in the format yyyy-MM-dd") @QueryParam("date") String date) {
        return new DateConversionResult(GPIDataService.getConvertedDate(fromCalId, toCalId, date));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<GPIErrors> getErrorsClass() {
        return GPIErrors.class;
    }
}
