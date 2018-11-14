package org.digijava.kernel.ampapi.endpoints.datafreeze;


import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;

@Path("data-freeze")
@Api("data-freeze")
public class DataFreezeEndPoints implements ErrorReportingEndpoint {

    @POST
    @Path("event")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "saveDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation(
            value = "Saves a data freeze object to the database",
            notes = "The returned JSON object holds information regarding:\n"
                    + "\n"
                    + "Field|Description\n"
                    + "---|---\n"
                    + "data|the saved data freeze eevent\n"
                    + "result|result string that indicates if the save was successful or not "
                    + "SAVE_SUCCESSFUL/SAVE_FAILED\n"
                    + "errors|an array of error objects for all the errors that occurred while saving\n"
                    + "\n"
                    + "### Sample Output\n"
                    + "```\n"
                    + "{\n"
                    + "      \"data\" : {\n"
                    + "      \"id\" : 30,\n"
                    + "      \"enabled\" : true,\n"
                    + "      \"gracePeriod\" : 30,\n"
                    + "      \"freezingDate\" : \"2017-09-01\",\n"
                    + "      \"openPeriodStart\" : \"2017-10-01\",\n"
                    + "      \"openPeriodEnd\" : \"2017-10-31\",\n"
                    + "      \"freezeOption\" : \"ENTIRE_ACTIVITY\",\n"
                    + "      \"filters\" : null,\n"
                    + "      \"sendNotification\" : false,\n"
                    + "      \"count\" : 88,\n"
                    + "      \"notificationDays\" : null,\n"
                    + "       \"cid\" : 1\n"
                    + " },\n"
                    + "    \"result\" : \"SAVE_SUCCESSFUL\",\n"
                    + "    \"errors\" : []\n"
                    + "}\n"
                    + "```")
    public JsonBean saveDataFreezeEvent(DataFreezeEvent dataFreezeEvent) {
        return DataFreezeService.saveDataFreezeEvent(dataFreezeEvent);
    }

    @DELETE
    @Path("event/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "deleteDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Deletes an data freeze event")
    public void deleteDataFreezeEvent(
            @ApiParam("unique identifier used to find the data freeze event in the database")
            @PathParam("id") long id) {
        DataFreezeService.deleteDataFreezeEvent(id);
    }

    @GET
    @Path("event/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "fetchDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Retrieves a data freeze event object")
    public AmpDataFreezeSettings fetchDataFreezeEvent(
            @ApiParam("unique identifier used to find the data freeze event in the database")
            @PathParam("id") long id) {
        return DataFreezeService.fetchOneDataFreezeEvent(id);
    }

    @GET
    @Path("event/list")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "fetchDataFreezeEventList", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Retrieves a list of data freeze event objects")
    public Page<DataFreezeEvent> fetchDataFreezeEventList(
            @ApiParam("first element in list") @QueryParam("offset") Integer offset,
            @ApiParam("maximum number of records to return") @QueryParam("count") Integer count,
            @ApiParam("field that will be used for sorting") @QueryParam("orderby") String orderBy,
            @ApiParam("asc or desc order") @QueryParam("sort") String sort) {
        return DataFreezeService.fetchDataFreezeEventList(offset, count, orderBy, sort);
    }
    
    @GET
    @Path("event/list-frozen-activities")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "fetchFrozenActivities", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation(
            value = "Gets an object containing the freeze date of the latest freeze event and number of "
                    + "activities affected the event.",
            notes = "### Sample output\n"
                    + "```\n"
                    + "{\n"
                    + "    \"freezingDate\" : 2017-08-30,\n"
                    + "    \"freezingCount\" : 345\n"
                    + "}\n"
                    + "```")
    public JsonBean fetchFrozenActivities() {
        return DataFreezeService.getFronzeActivitiesInformation();
    }
   
    @POST
    @Path("event/unfreeze-all")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "unfreezeAll", authTypes = { AuthRule.IN_ADMIN })
    @ApiOperation("Disables all freeze events")
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
