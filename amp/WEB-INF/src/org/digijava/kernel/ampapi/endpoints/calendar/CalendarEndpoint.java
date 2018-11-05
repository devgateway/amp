package org.digijava.kernel.ampapi.endpoints.calendar;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.CalendarUtil;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

/**
 * @author Viorel Chihai
 */
@Path("calendar")
@Api("calendar")
public class CalendarEndpoint implements ErrorReportingEndpoint {

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getContact", ui = false)
    @ApiOperation("Retrieve calendars.")
    public List<AmpFiscalCalendar> getCalendars(@QueryParam("id") List<Long> id) {
        return CalendarUtil.getCalendars(id);
    }

    @Override
    public Class getErrorsClass() {
        return CalendarEndpoint.class;
    }
    
}
