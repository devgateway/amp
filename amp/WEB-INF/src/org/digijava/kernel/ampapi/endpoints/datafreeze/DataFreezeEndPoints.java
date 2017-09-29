package org.digijava.kernel.ampapi.endpoints.datafreeze;


import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;

@Path("data-freeze")
public class DataFreezeEndPoints implements ErrorReportingEndpoint {

    /**
     * Saves a data freeze object to the database </br>
     * <dl>
     * </br>
     * The returned JSON object holds information regarding:
     * <dt><b>data</b>
     * <dd>- the saved data freeze eevent
     * <dt><b>result</b>
     * <dd>- result string that indicates if the save was successful or not -
     * SAVE_SUCCESSFUL/SAVE_FAILED
     * <dt><b>errors</b>
     * <dd>- an array of error objects for all the errors that occurred while
     * saving
     * </dl>
     * </br>
     * </br>
     * <h3>Sample Input:</h3>
     * 
     * <pre>
     * {
     *      "enabled" : true,
     *      "cid" : 1,
     *      "freezingDate" : "2017-09-01",
     *      "gracePeriod" : 30,
     *      "openPeriodStart" : "2017-10-01",
     *      "openPeriodEnd" : "2017-10-31",
     *      "freezeOption" : "ENTIRE_ACTIVITY",
     *      "sendNotification" : false,
     *      "notificationDays" : ""
     * }
     * </pre>
     * 
     * <h3>Sample Output:</h3>
     * 
     * <pre>
     * {
     *       "data" : {
     *       "id" : 30,
     *       "enabled" : true,
     *       "gracePeriod" : 30,
     *       "freezingDate" : "2017-09-01",
     *       "openPeriodStart" : "2017-10-01",
     *       "openPeriodEnd" : "2017-10-31",
     *       "freezeOption" : "ENTIRE_ACTIVITY",
     *       "filters" : null,
     *       "sendNotification" : false,
     *       "count" : 88,
     *       "notificationDays" : null,
     *        "cid" : 1
     *  },
     *     "result" : "SAVE_SUCCESSFUL",
     *     "errors" : []
     * }
     * </pre>
     * 
     * @param dataFreezeEvent
     *            - data freeze event object
     * @return
     */
    @POST
    @Path("event")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "saveDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
    public JsonBean saveDataFreezeEvent(DataFreezeEvent dataFreezeEvent) {
        return DataFreezeService.saveDataFreezeEvent(dataFreezeEvent);
    }

    /**
     * Deletes an data freeze event
     * @param id unique identifier used to find the data freeze event in the database
     */
    @DELETE
    @Path("event/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "deleteDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
    public void deleteDataFreezeEvent(@PathParam("id") long id) {
        DataFreezeService.deleteDataFreezeEvent(id);
    }

    /**
     * Retrieves a data freeze event object
     *<h3>Sample Output:</h3>
     * <pre>
     * {
     *      "ampDataFreezeSettingsId" : 31,
     *      "enabled" : true,
     *      "executed" : false,
     *      "gracePeriod" : 20,
     *      "freezingDate" : 1512075600000,
     *      "openPeriodStart" : 1517432400000,
     *      "openPeriodEnd" : 1519765200000,
     *      "sendNotification" : true,
     *      "freezeOption" : "FUNDING",
     *      "notificationDays" : 8,
     *      "filters" : null
     * }
     * </pre>
     * @param id unique identifier used to find the data freeze event in the database
     * @return
     */
    @GET
    @Path("event/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "fetchDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
    public AmpDataFreezeSettings fetchDataFreezeEvent(@PathParam("id") long id) {
        return DataFreezeService.fetchOneDataFreezeEvent(id);
    }

    /**
     * Retrieves a list of data freeze event objects
     * <h3>Sample Output:</h3>
     * <pre>
     * {
     *     "totalRecords" : 2,
     *     "data" : [{
     *                   "id" : 31,
     *                    "cid" : null,
     *                    "enabled" : true,
     *                    "gracePeriod" : 20,
     *                    "freezingDate" : "2017-12-01",
     *                    "openPeriodStart" : "2018-02-01",
     *                    "openPeriodEnd" : "2018-02-28",
     *                    "sendNotification" : true,
     *                    "freezeOption" : "FUNDING",
     *                    "filters" : null,
     *                    "count" : 88,
     *                    "notificationDays" : 8,
     *                    "executed" : false
     *               }, {
     *                     "id" : 30,
     *                     "cid" : null,
     *                     "enabled" : true,
     *                     "gracePeriod" : 30,
     *                     "freezingDate" : "2017-09-01",
     *                     "openPeriodStart" : "2017-10-01",
     *                     "openPeriodEnd" : "2017-10-31",
     *                     "sendNotification" : false,
     *                     "freezeOption" : "ENTIRE_ACTIVITY",
     *                     "filters" : null,
     *                     "count" : 88,
     *                     "notificationDays" : null,
     *                     "executed" : false
     *               }
     *               ]
     *  }
     * </pre>
     * @param offset first element in list
     * @param count maximum number of records to return
     * @param orderBy field that will be used for sorting
     * @param sort asc or desc order
     * @return
     */
    @GET
    @Path("event/list")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "fetchDataFreezeEventList", authTypes = { AuthRule.IN_ADMIN })
    public Page<DataFreezeEvent> fetchDataFreezeEventList(@QueryParam("offset") Integer offset,
            @QueryParam("count") Integer count, @QueryParam("orderby") String orderBy,
            @QueryParam("sort") String sort) {
        return DataFreezeService.fetchDataFreezeEventList(offset, count, orderBy, sort);
    }
    
    /**
     * Gets an object containing the freeze date of the latest freeze event 
     * and number of activities affected the event.
     * <h3>Sample Output:</h3>
     * <pre>
     *  {
     *      "freezingDate" : 2017-08-30,
     *      "freezingCount" : 345
     *  }
     * </pre>
     * @return
     */
    @GET
    @Path("event/list-frozen-activities")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "fetchFrozenActivities", authTypes = { AuthRule.IN_ADMIN })
    public JsonBean fetchFrozenActivities() {
        return DataFreezeService.getFronzeActivitiesInformation();
    }
   
    /**
    * Disables all freeze events 
    */
    @POST
    @Path("event/unfreeze-all")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "unfreezeAll", authTypes = { AuthRule.IN_ADMIN })
    public void unfreezeAll(JsonBean data) {
        DataFreezeService.unfreezeAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getErrorsClass() {
        return DataFreezeEndPoints.class;
    }

}
