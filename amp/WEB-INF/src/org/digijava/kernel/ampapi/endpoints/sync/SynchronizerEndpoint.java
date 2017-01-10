package org.digijava.kernel.ampapi.endpoints.sync;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.types.ISO8601TimeStamp;
import org.digijava.kernel.ampapi.endpoints.util.types.ListOfLongs;
import org.digijava.kernel.services.sync.SyncService;
import org.digijava.kernel.services.sync.model.SystemDiff;
import org.digijava.kernel.util.SpringUtil;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.util.TeamUtil;

/**
 * @author Octavian Ciubotaru
 */
@Path("sync")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SynchronizerEndpoint implements ErrorReportingEndpoint {

    private SyncService syncService = SpringUtil.getBean(SyncService.class);

    /**
     * Returns list of objects that were changed in specific period of time.
     * <p>Inputs:<ul>
     * <li><em>user-ids</em> - comma separated list of user ids for which to display changes
     * <li><em>last-sync-time</em> - optional timestamp of last synchronization time in ISO8601 format
     * </ul>
     * <h3>Example request:</h3>?user-ids=1,2,3&last-sync-time=2016-12-27T18:05:30.320+0200
     * <h3>Example response:</h3><pre>
     * {
     *   "timestamp": "2016-12-29T00:00:00.000+0200",
     *   "workspaces": false,
     *   "global-settings": true,
     *   "users": {
     *     "removed": [1],
     *     "saved": [2, 3]
     *   },
     *   "activities": {
     *     "removed": [],
     *     "saved": ["1", "2"]
     *   },
     *   "translations": {
     *     "removed": [],
     *     "saved": [],
     *     "incremental": false
     *   },
     *   "workspace-members": {
     *     "removed": [8],
     *     "saved": [16, 17]
     *   }
     * }
     * </pre></p>
     */
    @GET
    public SystemDiff computeSync(
            @DefaultValue("") @QueryParam("user-ids") ListOfLongs userIds,
            @QueryParam("last-sync-time") ISO8601TimeStamp lastSyncTime) {

        if (userIds.isEmpty()) {
            ApiErrorResponse.reportError(BAD_REQUEST, SynchronizerErrors.NO_USERS_ARE_SPECIFIED);
        }

        return syncService.diff(userIds, lastSyncTime);
    }
    
    /**
    * Provides full workspaces definitions
    * <p>
    * Each workspace definition includes non-null/non-empty properties.
    * 
    * <h3>Sample Request:</h3><pre>GET /rest/sync/workspaces?management=false&private=false</pre>
    * <h3>Sample Response:</h3><pre>
    * [  {
    *     "id": 16,
    *     "name": {
    *       "en": null,
    *       "fr": "Training Workspace"
    *     },
    *     "description": {
    *       "en": null,
    *       "fr": "Training Workspace"
    *     },
    *     "workspace-group": "Donor",
    *     "add-activity": true,
    *     "is-computed": true,
    *     "hide-draft": false,
    *     "is-cross-team-validation": false,
    *     "use-filter": true,
    *     "access-type": "Team",
    *     "permission-strategy": "Full Access",
    *     "workspace-filters": {
    *       "amountinthousand": 0,
    *       "calendarType": "1",
    *       "customusegroupings": true,
    *       "selectedActivityPledgesSettings": -1,
    *       "searchMode": "0",
    *       "workspaceonly": false,
    *       "decimalseparator": ",",
    *       "renderStartYear": 2000,
    *       "sortByAsc": true,
    *       "maximumFractionDigits": 0,
    *       "showArchived": false,
    *       "computedYear": 2015,
    *       "renderEndYear": 2015,
    *       "workspaces": [1, 2, 3, 8, 9, 11, 12, 15],
    *       "justSearch": false,
    *       "groupingsize": 3
    *     }
    *   },
    *   ...
    * ]</pre> 
    * @param includeManagement must be set to true to include management workspace into the result
    * @param includePrivate must be set to true to include private workspace into the result
    * @return the list of workspaces definitions according to selected filters 
    */
    @GET
    @Path("/workspaces")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "", ui = false, authTypes = {AuthRule.AUTHENTICATED})
    public List<AmpTeam> getWorkspaces(@DefaultValue("false") @QueryParam("management") Boolean includeManagement,
            @DefaultValue("false") @QueryParam("private") Boolean includePrivate) {
        return TeamUtil.getAllTeams(includeManagement, includePrivate);
    }

    @Override
    public Class getErrorsClass() {
        return SynchronizerErrors.class;
    }
}
