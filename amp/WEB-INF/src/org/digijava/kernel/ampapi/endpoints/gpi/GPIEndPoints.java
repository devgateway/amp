package org.digijava.kernel.ampapi.endpoints.gpi;


import java.util.List;

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
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;


@Path("gpi")
@Api("gpi")
public class GPIEndPoints implements ErrorReportingEndpoint {

    @GET
    @Path("/aid-on-budget")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getAidOnBudgetList", ui = false)
    @ApiOperation(
            value = "Retrieve the list of aid on budget objects.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>data</b><dd> - list of aid on budget objects\n"
                    + "<dt><b>totalRecords</b><dd> - total number of aid on budget records\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "\"data\" : [{\n"
                    + " \"id\" : 5,\n"
                    + "  \"donorId\" : 1113,\n"
                    + "         \"currencyCode\" : \"ETB\",\n"
                    + "         \"amount\" : 7888.0,\n"
                    + "         \"indicatorDate\" : \"2017-04-21\"\n"
                    + "     }, {\n"
                    + "         \"id\" : 1,\n"
                    + "         \"donorId\" : 1370,\n"
                    + "         \"currencyCode\" : \"CHF\",\n"
                    + "         \"amount\" : 4777.0,\n"
                    + "         \"indicatorDate\" : \"2017-04-07\"\n"
                    + "     }, {\n"
                    + "         \"id\" : 4,\n"
                    + "         \"donorId\" : 1333,\n"
                    + "         \"currencyCode\" : \"USD\",\n"
                    + "         \"amount\" : 7888.0,\n"
                    + "         \"indicatorDate\" : \"2017-04-07\"\n"
                    + "     }, {\n"
                    + "         \"id\" : 2,\n"
                    + "         \"donorId\" : 1443,\n"
                    + "         \"currencyCode\" : \"XUA\",\n"
                    + "         \"amount\" : 88328.0,\n"
                    + "         \"indicatorDate\" : \"2017-04-06\"\n"
                    + "     }, {\n"
                    + "         \"id\" : 3,\n"
                    + "         \"donorId\" : 1370,\n"
                    + "         \"currencyCode\" : \"XUA\",\n"
                    + "         \"amount\" : 6999.0,\n"
                    + "         \"indicatorDate\" : \"2017-04-06\"\n"
                    + "     }\n"
                    + " ],\n"
                    + " \"totalRecords\" : 5  \n"
                    + " } \n"
                    + "</pre>")
    public JsonBean getAidOnBudgetList(@QueryParam("offset") Integer offset,
            @ApiParam("maximum number of records to return") @QueryParam("count") Integer count,
            @ApiParam("field that will be used for sorting") @QueryParam("orderby") String orderBy,
            @ApiParam("asc or desc order") @QueryParam("sort") String sort) {
        return GPIDataService.getAidOnBudgetList(offset, count, orderBy, sort);
    }

    @GET
    @Path("/aid-on-budget/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getAidOnBudgetById", ui = false)
    @ApiOperation(
            value = "Retrieve aid on budget object by id.",
            notes = "<dl>\n"
                    + "</br>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>id</b><dd> - id of the returned object\n"
                    + "<dt><b>donorId</b><dd> - id of a donor agency\n"
                    + "<dt><b>currencyCode</b><dd> - currency code e.g USD\n"
                    + "<dt><b>amount</b><dd> - aid amount\n"
                    + "<dt><b>indicatorDate</b><dd> - date\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "    \"id\" : 5,\n"
                    + "     \"donorId\" : 1113,\n"
                    + "     \"currencyCode\" : \"ETB\",\n"
                    + "     \"amount\" : 7888.0,\n"
                    + "     \"indicatorDate\" : \"2017-04-21\"\n"
                    + " }\n"
                    + "</pre>")
    public JsonBean getAidOnBudgetById(@PathParam("id") long id) {
        return GPIDataService.getAidOnBudgetById(id);
    }

    @POST
    @Path("/aid-on-budget")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveAidOnBudget", ui = false)
    @ApiOperation(
            value = "Save aid on budget object to the database.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>data</b><dd> - saved aid on budget object\n"
                    + "<dt><b>result</b><dd> - result string that indicates if the save was successful or not - "
                    + "SAVED/SAVE_FAILED\n"
                    + "<dt><b>errors</b><dd> - an array of error objects for all the errors that occurred while "
                    + "saving\n"
                    + "</dl></br></br> \n"
                    + "<h3>Sample Input:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "   \"id\" : 5,\n"
                    + "   \"donorId\" : 1113,\n"
                    + "   \"currencyCode\" : \"ETB\",\n"
                    + "   \"amount\" : 7888.0,\n"
                    + "   \"indicatorDate\" : \"2017-04-21\"\n"
                    + "}\n"
                    + "</pre>\n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "   \"data\" : {\n"
                    + "                \"id\" : 5,\n"
                    + "                 \"donorId\" : 1113,\n"
                    + "                  \"currencyCode\" : \"ETB\",\n"
                    + "                  \"amount\" : 7888.0,\n"
                    + "                  \"indicatorDate\" : \"2017-04-21\"\n"
                    + "},\n"
                    + "    \"result\" : \"SAVED\",\n"
                    + "     \"errors\" : []\n"
                    + "}\n"
                    + "</pre>")
    public JsonBean saveAidOnBudget(
            @ApiParam("json data that will be used to create or update an aid on budget row") JsonBean aidOnBudget) {
        return GPIDataService.saveAidOnBudget(aidOnBudget);
    }

    @POST
    @Path("/aid-on-budget/save-all")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveAllEdits", ui = false)
    @ApiOperation(
            value = "Save a list of aid on budget objects to the database.",
            notes = "<dl>\n"
                    + "Returns a list of JSON objects. The JSON objects holds information regarding:\n"
                    + "<dt><b>data</b><dd> - saved aid on budget object\n"
                    + "<dt><b>result</b><dd> - result string that indicates if the save was successful or not - "
                    + "SAVED/SAVE_FAILED\n"
                    + "<dt><b>errors</b><dd> - an array of error objects for all the errors that occurred while "
                    + "saving\n"
                    + "</dl></br></br> \n"
                    + "<h3>Sample Input:</h3>\n"
                    + "<pre>\n"
                    + "[{\n"
                    + "   \"id\" : 5,\n"
                    + "   \"donorId\" : 1113,\n"
                    + "   \"currencyCode\" : \"ETB\",\n"
                    + "   \"amount\" : 7888.0,\n"
                    + "   \"indicatorDate\" : \"2017-04-21\"\n"
                    + "},\n"
                    + "{\n"
                    + "   \"donorId\" : 1114,\n"
                    + "   \"currencyCode\" : \"ETB\",\n"
                    + "   \"amount\" : 9000,\n"
                    + "   \"indicatorDate\" : \"2017-01-21\"\n"
                    + "}]\n"
                    + "</pre>\n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "[{\n"
                    + "   \"data\" : {\n"
                    + "                \"id\" : 5,\n"
                    + "                 \"donorId\" : 1113,\n"
                    + "                  \"currencyCode\" : \"ETB\",\n"
                    + "                  \"amount\" : 7888.0,\n"
                    + "                  \"indicatorDate\" : \"2017-04-21\"\n"
                    + "},\n"
                    + "    \"result\" : \"SAVED\",\n"
                    + "    \"errors\" : []\n"
                    + "},\n"
                    + "{\n"
                    + "   \"data\" : {\n"
                    + "                \"id\" : 6,\n"
                    + "                 \"donorId\" : 1114,\n"
                    + "                  \"currencyCode\" : \"ETB\",\n"
                    + "                  \"amount\" : 9000,\n"
                    + "                  \"indicatorDate\" : \"2017-01-21\"\n"
                    + "},\n"
                    + "    \"result\" : \"SAVED\",\n"
                    + "    \"errors\" : []\n"
                    + "}]")
    public List<JsonBean> saveAllEdits(
            @ApiParam("list of aid on budget objects that will be saved") List<JsonBean> aidOnBudgetList) {
        return GPIDataService.saveAidOnBudget(aidOnBudgetList);
    }

    @DELETE
    @Path("/aid-on-budget/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "deleteAidOnBudgetById", ui = false)
    @ApiOperation(
            value = "Delete aid on budget object in the database.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding the result of the delete action:\n"
                    + "<dt><b>result</b><dd> - result string that indicates if the delete was successful\n"
                    + "</dl></br></br> \n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "   \"result\" : \"deleted\"\n"
                    + "}\n"
                    + "</pre>")
    public JsonBean deleteAidOnBudgetById(@PathParam("id") long id) {
        return GPIDataService.deleteAidOnBudgetById(id);
    }

    @POST
    @Path("/donor-notes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveDonorNotes", ui = false)
    @ApiOperation(
            value = "Save donor notes objects to the database.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>data</b><dd> - saved donorNotes object\n"
                    + "<dt><b>result</b><dd> - result string that indicates if the save was successful or not - "
                    + "SAVED/SAVE_FAILED\n"
                    + "<dt><b>errors</b><dd> - an array of error objects for all the errors that occurred while "
                    + "saving\n"
                    + "</dl></br></br> \n"
                    + "<h3>Sample Input:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "     \"id\" : 1,\n"
                    + "     \"donorId\" : 1061,\n"
                    + "     \"notes\" : \"Sample note 1\",\n"
                    + "     \"notesDate\" : \"2017-04-14\"\n"
                    + " }\n"
                    + "</pre>\n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "   \"data\" : {\n"
                    + "                \"id\" : 1,\n"
                    + "                \"donorId\" : 1061,\n"
                    + "                \"notes\" : \"Sample note 1\",\n"
                    + "                \"notesDate\" : \"2017-04-14\"                  \n"
                    + "},\n"
                    + "    \"result\" : \"SAVED\",\n"
                    + "    \"errors\" : []\n"
                    + "}\n"
                    + "</pre>")
    public JsonBean saveDonorNotes(
            @ApiParam("json representation of the donorNotes object to be saved") JsonBean donorNotes) {
        return GPIDataService.saveDonorNotes(donorNotes);
    }

    @POST
    @Path("/donor-notes/save-all")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveAllDonorNotes", ui = false)
    @ApiOperation(
            value = "Save a list donorNotes objects to the database.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>data</b><dd> - saved donorNotes object\n"
                    + "<dt><b>result</b><dd> - result string that indicates if the save was successful or not - "
                    + "SAVED/SAVE_FAILED\n"
                    + "<dt><b>errors</b><dd> - an array of error objects for all the errors that occurred while "
                    + "saving\n"
                    + "</dl></br></br> \n"
                    + "<h3>Sample Input:</h3>\n"
                    + "<pre>\n"
                    + "[{\n"
                    + "     \"id\" : 1,\n"
                    + "     \"donorId\" : 1061,\n"
                    + "     \"notes\" : \"Sample note 1\",\n"
                    + "     \"notesDate\" : \"2017-04-14\"\n"
                    + "},\n"
                    + "{\n"
                    + "     \"donorId\" : 1062,\n"
                    + "     \"notes\" : \"Sample note 2\",\n"
                    + "     \"notesDate\" : \"2017-04-14\"\n"
                    + "}]\n"
                    + "</pre>\n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "[{\n"
                    + "   \"data\" : {\n"
                    + "                \"id\" : 1,\n"
                    + "                \"donorId\" : 1061,\n"
                    + "                \"notes\" : \"Sample note 1\",\n"
                    + "                \"notesDate\" : \"2017-04-14\"                \n"
                    + "},\n"
                    + "    \"result\" : \"SAVED\",\n"
                    + "    \"errors\" : []\n"
                    + "},\n"
                    + " {\n"
                    + "   \"data\" : {\n"
                    + "                \"id\" : 2,\n"
                    + "                \"donorId\" : 1062,\n"
                    + "                \"notes\" : \"Sample note 2\",\n"
                    + "                \"notesDate\" : \"2017-04-14\"\n"
                    + "},\n"
                    + "    \"result\" : \"SAVED\",\n"
                    + "    \"errors\" : []\n"
                    + "}\n"
                    + "]\n"
                    + "</pre>")
    public List<JsonBean> saveAllDonorNotes(
            @ApiParam("list of donorNotes objects") List<JsonBean> donorNotes) {
        return GPIDataService.saveDonorNotes(donorNotes);
    }

    @GET
    @Path("/donor-notes/{indicatorCode}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getDonorNotesList", ui = false)
    @ApiOperation(
            value = "Retrieve a list of donor notes.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>data</b><dd> - list of donor notes objects\n"
                    + "<dt><b>totalRecords</b><dd> - total number of donor notes records\n"
                    + "</dl></br></br> \n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "   \"data\" :[{\n"
                    + "                \"id\" : 1,\n"
                    + "                \"donorId\" : 1061,\n"
                    + "                \"notes\" : \"Sample note 1\",\n"
                    + "                \"notesDate\" : \"2017-04-14\"\n"
                    + "    },  \n"
                    + "    {\n"
                    + "                \"id\" : 2,\n"
                    + "                \"donorId\" : 1062,\n"
                    + "                \"notes\" : \"Sample note 2\",\n"
                    + "                \"notesDate\" : \"2017-04-14\"\n"
                    + "   }],\n"
                    + "   \"totalRecords\" : 2     \n"
                    + "}\n"
                    + "</pre>")
    public JsonBean getDonorNotesList(@PathParam("indicatorCode") String indicatorCode,
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
    @ApiOperation(
            value = "Delete the donor note object from the database.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding the result of the delete action:\n"
                    + "<dt><b>result</b><dd> - result string that indicates if the delete was successful\n"
                    + "</dl></br></br> \n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "   \"result\" : \"deleted\"\n"
                    + "}\n"
                    + "</pre>")
    public JsonBean deleteDonorNotesById(@PathParam("id") long id) {
        return GPIDataService.deleteDonorNotesById(id);
    }

    @GET
    @Path("/users-verified-orgs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getUsersVerifiedOrganizations", ui = false)
    @ApiOperation(
            value = "Retrieve a list of verified organizations associated with the logged in user.",
            notes = "<dl>\n"
                    + "An array of JSON objects. The JSON objects contain the following fields:\n"
                    + "<dt><b>id</b><dd> - database id of the organization\n"
                    + "<dt><b>name</b><dd> - name of the organisation\n"
                    + "</dl></br></br> \n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "[{\n"
                    + "   \"id\" : 3443,\n"
                    + "   \"name\" : \"African Development Bank\"\n"
                    + "},\n"
                    + "{\n"
                    + "    \"id\" : 3444,\n"
                    + "    \"name\" : \"World Bank Group\"\n"
                    + "}\n"
                    + "]\n"
                    + "</pre>")
    public List<JsonBean> getUsersVerifiedOrganizations() {
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
    @ApiOperation(
            value = "Retrieve gpi report visibility for each indicator.",
            notes = "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + " \"1\": true,\n"
                    + " \"6\": true,\n"
                    + " \"5a\": true,\n"
                    + " \"5b\": true,\n"
                    + " \"9b\": true\n"
                    + "}\n"
                    + "</pre>")
    public JsonBean getGPIReportVisibilityConfiguration() {
        JsonBean visibilityConfiguration = new JsonBean();
        visibilityConfiguration.set(GPIReportConstants.REPORT_1, FeaturesUtil.isVisibleFeature(GPIReportConstants.REPORT_1_GFM_NAME) );
        visibilityConfiguration.set(GPIReportConstants.REPORT_5a, FeaturesUtil.isVisibleFeature(GPIReportConstants.REPORT_5a_GFM_NAME) );
        visibilityConfiguration.set(GPIReportConstants.REPORT_5b, FeaturesUtil.isVisibleFeature(GPIReportConstants.REPORT_5b_GFM_NAME) );
        visibilityConfiguration.set(GPIReportConstants.REPORT_6, FeaturesUtil.isVisibleFeature(GPIReportConstants.REPORT_6_GFM_NAME) );
        visibilityConfiguration.set(GPIReportConstants.REPORT_9b, FeaturesUtil.isVisibleFeature(GPIReportConstants.REPORT_9b_GFM_NAME) );
        return visibilityConfiguration;
    }
    
    @GET
    @Path("/report/years/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getYears", ui = false)
    public List<JsonBean> getYears() {       
         return GPIDataService.getYears();
    }

    @GET
    @Path("/report/donors/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getDonors", ui = false)
    public List<JsonBean> getDonors() {       
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
    @ApiOperation(
            value = "Convert a date from one calendar to another",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>converted-date</b><dd> - the converted date in the format yyyy-MM-dd\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "  \"converted-date\": \"2006-11-20\"\n"
                    + "} \n"
                    + "</pre>")
    public JsonBean getConvertedDate(@QueryParam("fromCalId") Long fromCalId, @QueryParam("toCalId") Long toCalId,
            @ApiParam("sourceDate in the format yyyy-MM-dd") @QueryParam("date") String date) {
        
        JsonBean convertedDateMap = new JsonBean();
        convertedDateMap.set("converted-date", GPIDataService.getConvertedDate(fromCalId, toCalId, date));
        
        return convertedDateMap;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<GPIErrors> getErrorsClass() {
        return GPIErrors.class;
    }
}
