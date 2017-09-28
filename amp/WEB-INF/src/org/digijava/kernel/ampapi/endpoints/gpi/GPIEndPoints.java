package org.digijava.kernel.ampapi.endpoints.gpi;


import java.util.List;

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

import org.dgfoundation.amp.gpi.reports.GPIDonorActivityDocument;
import org.dgfoundation.amp.gpi.reports.GPIRemark;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.util.FeaturesUtil;


@Path("gpi")
public class GPIEndPoints implements ErrorReportingEndpoint {

    /**
     * Retrieves a list of aid on budget objects </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>data</b><dd> - list of aid on budget objects
     * <dt><b>totalRecords</b><dd> - total number of aid on budget records
     * </dl></br></br>
     * 
     * <h3>Sample Output:</h3>
     * <pre>
     * 
     * {
     * "data" : [{
     *  "id" : 5,
     *   "donorId" : 1113,
     *          "currencyCode" : "ETB",
     *          "amount" : 7888.0,
     *          "indicatorDate" : "2017-04-21"
     *      }, {
     *          "id" : 1,
     *          "donorId" : 1370,
     *          "currencyCode" : "CHF",
     *          "amount" : 4777.0,
     *          "indicatorDate" : "2017-04-07"
     *      }, {
     *          "id" : 4,
     *          "donorId" : 1333,
     *          "currencyCode" : "USD",
     *          "amount" : 7888.0,
     *          "indicatorDate" : "2017-04-07"
     *      }, {
     *          "id" : 2,
     *          "donorId" : 1443,
     *          "currencyCode" : "XUA",
     *          "amount" : 88328.0,
     *          "indicatorDate" : "2017-04-06"
     *      }, {
     *          "id" : 3,
     *          "donorId" : 1370,
     *          "currencyCode" : "XUA",
     *          "amount" : 6999.0,
     *          "indicatorDate" : "2017-04-06"
     *      }
     *  ],
     *  "totalRecords" : 5  
     *  } 
     * </pre>
     * @param offset
     * @param count maximum number of records to return
     * @param orderBy field that will be used for sorting
     * @param sort asc or desc order
     * @return A json object with a list of aid on budget and paging information
     */
    @GET
    @Path("/aid-on-budget")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getAidOnBudgetList", ui = false)
    public JsonBean getAidOnBudgetList(@QueryParam("offset") Integer offset, @QueryParam("count") Integer count,
            @QueryParam("orderby") String orderBy, @QueryParam("sort") String sort) {
        return GPIDataService.getAidOnBudgetList(offset, count, orderBy, sort);
    }

    /**
     * Retrieves object aid on budget by id
     * </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>id</b><dd> - id of the returned object
     * <dt><b>donorId</b><dd> - id of a donor agency
     * <dt><b>currencyCode</b><dd> - currency code e.g USD
     * <dt><b>amount</b><dd> - aid amount
     * <dt><b>indicatorDate</b><dd> - date
     * </dl></br></br>
     * 
     * <h3>Sample Output:</h3>
     * <pre>
     * {
     *     "id" : 5,
     *      "donorId" : 1113,
     *      "currencyCode" : "ETB",
     *      "amount" : 7888.0,
     *      "indicatorDate" : "2017-04-21"
     *  }
     * </pre>
     * @param id the ID that will be used to query the database
     * @return a json object of the found aid on budget object
     */
    @GET
    @Path("/aid-on-budget/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getAidOnBudgetById", ui = false)
    public JsonBean getAidOnBudgetById(@PathParam("id") long id) {
        return GPIDataService.getAidOnBudgetById(id);
    }

    /**
     * saves an aid on budget object to the database
     * </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>data</b><dd> - saved aid on budget object
     * <dt><b>result</b><dd> - result string that indicates if the save was successful or not - SAVED/SAVE_FAILED
     * <dt><b>errors</b><dd> - an array of error objects for all the errors that occurred while saving
     * </dl></br></br> 
     * <h3>Sample Input:</h3>
     * <pre>
     * {
     *    "id" : 5,
     *    "donorId" : 1113,
     *    "currencyCode" : "ETB",
     *    "amount" : 7888.0,
     *    "indicatorDate" : "2017-04-21"
     * }
     * </pre>
     * <h3>Sample Output:</h3>
     * <pre>
     * {
     *    "data" : {
     *                 "id" : 5,
     *                  "donorId" : 1113,
     *                   "currencyCode" : "ETB",
     *                   "amount" : 7888.0,
     *                   "indicatorDate" : "2017-04-21"
     * },
     *     "result" : "SAVED",
     *      "errors" : []
     * }
     * </pre>
     * @param aidOnBudget json data that will be used to create or update an aid on budget row
     * @return json object of the created or updated aid on budget object. Also returns any error messages that occur
     */
    @POST
    @Path("/aid-on-budget")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveAidOnBudget", ui = false)
    public JsonBean saveAidOnBudget(JsonBean aidOnBudget) {
        return GPIDataService.saveAidOnBudget(aidOnBudget);
    }

    /**
     * Saves a list of aid on budget objects to the database
     * </br>
     * <dl>
     * </br>
     * Returns a list of JSON objects. The JSON objects holds information regarding:
     * <dt><b>data</b><dd> - saved aid on budget object
     * <dt><b>result</b><dd> - result string that indicates if the save was successful or not - SAVED/SAVE_FAILED
     * <dt><b>errors</b><dd> - an array of error objects for all the errors that occurred while saving
     * </dl></br></br> 
     * <h3>Sample Input:</h3>
     * <pre>
     * [{
     *    "id" : 5,
     *    "donorId" : 1113,
     *    "currencyCode" : "ETB",
     *    "amount" : 7888.0,
     *    "indicatorDate" : "2017-04-21"
     * },
     * {
     *    "donorId" : 1114,
     *    "currencyCode" : "ETB",
     *    "amount" : 9000,
     *    "indicatorDate" : "2017-01-21"
     * }]
     * </pre>
     * <h3>Sample Output:</h3>
     * <pre>
     * [{
     *    "data" : {
     *                 "id" : 5,
     *                  "donorId" : 1113,
     *                   "currencyCode" : "ETB",
     *                   "amount" : 7888.0,
     *                   "indicatorDate" : "2017-04-21"
     * },
     *     "result" : "SAVED",
     *     "errors" : []
     * },
     * {
     *    "data" : {
     *                 "id" : 6,
     *                  "donorId" : 1114,
     *                   "currencyCode" : "ETB",
     *                   "amount" : 9000,
     *                   "indicatorDate" : "2017-01-21"
     * },
     *     "result" : "SAVED",
     *     "errors" : []
     * }]
     * </pre>
     * @param aidOnBudgetList - list of aid on budget objects that will be saved
     * @return list of aid on budget objects that were saved. Also returns any server side validation errors
     */
    @POST
    @Path("/aid-on-budget/save-all")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveAllEdits", ui = false)
    public List<JsonBean> saveAllEdits(List<JsonBean> aidOnBudgetList) {
        return GPIDataService.saveAidOnBudget(aidOnBudgetList);
    }

    /**
     *  Deletes the aid on budget row on the database
     * </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding the result of the delete action:
     * <dt><b>result</b><dd> - result string that indicates if the delete was successful
     * </dl></br></br> 
     * <h3>Sample Output:</h3>
     * <pre>
     * {
     *    "result" : "deleted"
     * }
     * </pre>
     * @param id identifier to query for aid on budget to delete
     * @return JSON object containing the result
     */
    @DELETE
    @Path("/aid-on-budget/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "deleteAidOnBudgetById", ui = false)
    public JsonBean deleteAidOnBudgetById(@PathParam("id") long id) {
        return GPIDataService.deleteAidOnBudgetById(id);
    }

    /**
     * saves a donorNotes object to the database
     * </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>data</b><dd> - saved donorNotes object
     * <dt><b>result</b><dd> - result string that indicates if the save was successful or not - SAVED/SAVE_FAILED
     * <dt><b>errors</b><dd> - an array of error objects for all the errors that occurred while saving
     * </dl></br></br> 
     * <h3>Sample Input:</h3>
     * <pre>
     * {
     *      "id" : 1,
     *      "donorId" : 1061,
     *      "notes" : "Sample note 1",
     *      "notesDate" : "2017-04-14"
     *  }
     * </pre>
     * <h3>Sample Output:</h3>
     * <pre>
     * {
     *    "data" : {
     *                 "id" : 1,
     *                 "donorId" : 1061,
     *                 "notes" : "Sample note 1",
     *                 "notesDate" : "2017-04-14"                  
     * },
     *     "result" : "SAVED",
     *     "errors" : []
     * }
     * </pre>
     * @param donorNotes json representation of the donorNotes object to be saved
     * @return json object containing the donorNotes object that and any server side validation errors
     */
    @POST
    @Path("/donor-notes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveDonorNotes", ui = false)
    public JsonBean saveDonorNotes(JsonBean donorNotes) {
        return GPIDataService.saveDonorNotes(donorNotes);
    }

    /**
     * saves a list donorNotes objects to the database
     * </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>data</b><dd> - saved donorNotes object
     * <dt><b>result</b><dd> - result string that indicates if the save was successful or not - SAVED/SAVE_FAILED
     * <dt><b>errors</b><dd> - an array of error objects for all the errors that occurred while saving
     * </dl></br></br> 
     * <h3>Sample Input:</h3>
     * <pre>
     * [{
     *      "id" : 1,
     *      "donorId" : 1061,
     *      "notes" : "Sample note 1",
     *      "notesDate" : "2017-04-14"
     * },
     * {
     *      "donorId" : 1062,
     *      "notes" : "Sample note 2",
     *      "notesDate" : "2017-04-14"
     * }]
     * </pre>
     * <h3>Sample Output:</h3>
     * <pre>
     * [{
     *    "data" : {
     *                 "id" : 1,
     *                 "donorId" : 1061,
     *                 "notes" : "Sample note 1",
     *                 "notesDate" : "2017-04-14"                
     * },
     *     "result" : "SAVED",
     *     "errors" : []
     * },
     *  {
     *    "data" : {
     *                 "id" : 2,
     *                 "donorId" : 1062,
     *                 "notes" : "Sample note 2",
     *                 "notesDate" : "2017-04-14"
     * },
     *     "result" : "SAVED",
     *     "errors" : []
     * }
     * ]
     * </pre>
     * @param donorNotes list of donorNotes objects
     * @return list of donorNotes objects that were saved. Also returns any server side validation errors
     */
    @POST
    @Path("/donor-notes/save-all")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "saveAllDonorNotes", ui = false)
    public List<JsonBean> saveAllDonorNotes(List<JsonBean> donorNotes) {
        return GPIDataService.saveDonorNotes(donorNotes);
    }

    /**
     * Retrieves a list of donor notes
     * </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding:
     * <dt><b>data</b><dd> - list of donor notes objects
     * <dt><b>totalRecords</b><dd> - total number of donor notes records
     * </dl></br></br> 
     * <h3>Sample Output:</h3>
     * <pre>
     * {
     *    "data" :[{
     *                 "id" : 1,
     *                 "donorId" : 1061,
     *                 "notes" : "Sample note 1",
     *                 "notesDate" : "2017-04-14"                
     *     },  
     *     {
     *                 "id" : 2,
     *                 "donorId" : 1062,
     *                 "notes" : "Sample note 2",
     *                 "notesDate" : "2017-04-14"
     *    }],
     *    "totalRecords" : 2     
     * }     
     * </pre>
     * @param offset first element in the list
     * @param count maximum number of records to return
     * @param orderBy field that will be used for sorting
     * @param sort asc or desc order
     * @return a json object containing a list of donors notes and paging information
     */
    @GET
    @Path("/donor-notes/{indicatorCode}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getDonorNotesList", ui = false)
    public JsonBean getDonorNotesList(@PathParam("indicatorCode") String indicatorCode, @QueryParam("offset") Integer offset, @QueryParam("count") Integer count,
            @QueryParam("orderby") String orderBy, @QueryParam("sort") String sort) {
        return GPIDataService.getDonorNotesList(offset, count, orderBy, sort, indicatorCode);
    }

    /**
     * Deletes the donor notes object from the database
     * </br>
     * <dl>
     * </br>
     * The JSON object holds information regarding the result of the delete action:
     * <dt><b>result</b><dd> - result string that indicates if the delete was successful
     * </dl></br></br> 
     * <h3>Sample Output:</h3>
     * <pre>
     * {
     *    "result" : "deleted"
     * }
     * </pre>
     * @param id identifier to query for object to delete
     * @return JSON object containing the result
     */
    @DELETE
    @Path("/donor-notes/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "deleteDonorNotesId", ui = false)
    public JsonBean deleteDonorNotesById(@PathParam("id") long id) {
        return GPIDataService.deleteDonorNotesById(id);
    }

    /**
     * Retrieves a list of verified organizations associated with the logged in user
     * </br>
     * <dl>
     * </br>
     * An array of JSON objects. The JSON objects contain the following fields:
     * <dt><b>id</b><dd> - database id of the organization
     * <dt><b>name</b><dd> - name of the organisation
     * </dl></br></br> 
     * <h3>Sample Output:</h3>
     * <pre>
     *[{
     *    "id" : 3443,
     *    "name" : "African Development Bank"
     * },{
     *     "id" : 3444,
     *     "name" : "World Bank Group"
     * }
     *]
     * </pre>
     * @return list of verified organizations associated with the logged in user
     */
    @GET
    @Path("/users-verified-orgs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getUsersVerifiedOrganizations", ui = false)
    public List<JsonBean> getUsersVerifiedOrganizations() {
        return GPIDataService.getUsersVerifiedOrganizations();
    }
    
    /**
     * Retrieves the gpi report for specified indicator.
     * 
     * @param indicatorCode
     * @param formParams
     * @return
     */
    @POST
    @Path("/report/{indicatorCode}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getGpiReport", ui = false)
    public GPIReport getGPIReport(@PathParam("indicatorCode") String indicatorCode, JsonBean formParams) {
        return GPIReportService.getInstance().getGPIReport(indicatorCode, formParams);
    }
    
    /**
     * 
     * @param indicatorCode
     * @param formParams
     * @return response containing the XLSX file of the GPI Report
     */
    @POST
    @Path("/report/export/xls/{indicatorCode}")
    @Produces({"application/vnd.ms-excel" })
    public final Response exportXlsGPIReport(@PathParam("indicatorCode") String indicatorCode, @FormParam("formParams") String formParams) {        
        return GPIReportService.getInstance().exportGPIReport(indicatorCode, JsonBean.getJsonBeanFromString(formParams), GPIReportConstants.XLSX);
    }
    
    /**
     * 
     * @param indicatorCode
     * @param formParams
     * @return response containing the PDF file of the GPI Report
     */
    @POST
    @Path("/report/export/pdf/{indicatorCode}")
    @Produces({"application/pdf" })
    public final Response exportPdfGPIReport(@PathParam("indicatorCode") String indicatorCode, @FormParam("formParams") String formParams) {
        return GPIReportService.getInstance().exportGPIReport(indicatorCode, JsonBean.getJsonBeanFromString(formParams), GPIReportConstants.PDF);
    }
    
    /**
     * Retrieves the remarks for the specified indicator code, donor agency/group and between the specified dates.
     * 
     * @param indicatorCode
     * @param donorIds - list of donors
     * @param donorType (donor-agency|donor-group)
     * @param from
     * @param to
     * 
     * @return
     */
    @GET
    @Path("/report/remarks/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getGpiReport", ui = false)
    public List<GPIRemark> getGPIRemarks(@QueryParam("indicatorCode") String indicatorCode,
            @QueryParam("donorId") List<Long> donorIds, @QueryParam("donorType") String donorType, @QueryParam("from") Long from,
            @QueryParam("to") Long to) {
        
        return GPIDataService.getGPIRemarks(indicatorCode, donorIds, donorType, from, to);
    }
    
    /**
     * Retrieves the documents for the specified donor agency/group and activity.
     * 
     * @param activitydonors - list of donors with activities
     * 
     * @return list of activity donors with documents
     */
    @POST
    @Path("/report/documents/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getSupportiveDocuments", ui = false)
    public List<GPIDonorActivityDocument> getSupportiveDocuments(List<GPIDonorActivityDocument> activityDonors) {

        return GPIDataService.getGPIDocuments(activityDonors);
    }

    @GET
    @Path("/report/visibility/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = { AuthRule.IN_WORKSPACE }, id = "getGPIReportVisibility", ui = false)
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<GPIErrors> getErrorsClass() {
        return GPIErrors.class;
    }
}
