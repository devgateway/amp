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

    @POST
    @Path("event")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "saveDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
    public JsonBean saveDataFreezeEvent(DataFreezeEvent dataFreezeEvent) {
        return DataFreezeService.saveDataFreezeEvent(dataFreezeEvent);
    }

    @DELETE
    @Path("event/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "deleteDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
    public void deleteDataFreezeEvent(@PathParam("id") long id) {
        DataFreezeService.deleteDataFreezeEvent(id);
    }

    @GET
    @Path("event/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "fetchDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
    public AmpDataFreezeSettings fetchDataFreezeEvent(@PathParam("id") long id) {
        return DataFreezeService.fetchOneDataFreezeEvent(id);
    }

    @GET
    @Path("event/list")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "fetchDataFreezeEventList", authTypes = { AuthRule.IN_ADMIN })
    public Page<DataFreezeEvent> fetchDataFreezeEventList(@QueryParam("offset") Integer offset,
            @QueryParam("count") Integer count, @QueryParam("orderby") String orderBy,
            @QueryParam("sort") String sort) {
        return DataFreezeService.fetchDataFreezeEventList(offset, count, orderBy, sort);
    }

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
