package org.digijava.kernel.ampapi.endpoints.sync;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.util.types.ISO8601TimeStamp;
import org.digijava.kernel.ampapi.endpoints.util.types.ListOfLongs;
import org.digijava.kernel.services.sync.model.SystemDiff;
import org.digijava.kernel.services.sync.SyncService;
import org.digijava.kernel.util.SpringUtil;

/**
 * @author Octavian Ciubotaru
 */
@Path("sync")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SynchronizerEndpoint implements ErrorReportingEndpoint {

    private SyncService syncService = SpringUtil.getBean(SyncService.class);

    @GET
    public SystemDiff computeSync(
            @DefaultValue("") @QueryParam("user-ids") ListOfLongs userIds,
            @QueryParam("last-sync-time") ISO8601TimeStamp lastSyncTime) {

        if (userIds.isEmpty()) {
            ApiErrorResponse.reportError(BAD_REQUEST, SynchronizerErrors.NO_USERS_ARE_SPECIFIED);
        }

        return syncService.diff(userIds, lastSyncTime);
    }

    @Override
    public Class getErrorsClass() {
        return SynchronizerErrors.class;
    }
}
