package org.digijava.kernel.ampapi.endpoints.sync;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.types.ISO8601TimeStamp;
import org.digijava.kernel.services.sync.SyncService;
import org.digijava.kernel.services.sync.model.SystemDiff;
import org.digijava.kernel.services.sync.model.Translation;
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
     * <p>For initial sync caller must send only user ids. For subsequent syncs caller must send all fields.</p>
     * Input:<ul>
     * <li><b>user-ids</b> - will sync activities visible to the users from this list, mandatory
     * <li><b>last-sync-time</b> - time since last sync in ISO8601 format, optional
     * <li><b>amp-ids</b> - list of amp ids of activities currently known to the client, optional
     * </ul>
     *
     * <h3>Example request:</h3>
     * <pre>
     * {
     *   "user-ids": [ 7 ],
     *   "last-sync-time": "2016-06-01T01:00:00.999+0000",
     *   "amp-ids": [ "8723268703", "8723268706", "8723228497", "8723210713" ]
     * }
     * </pre>
     * <h3>Example response:</h3><pre>
     * {
     *   "timestamp": "2016-12-29T00:00:00.000+0200",
     *   "workspaces": false,
     *   "workspace-settings": false,
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
     * </pre>
     */
    @POST
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "computeSync", ui = false)
    public SystemDiff computeSync(SyncRequest syncRequest) {

        if (syncRequest.getUserIds() == null || syncRequest.getUserIds().isEmpty()) {
            ApiErrorResponse.reportError(BAD_REQUEST, SynchronizerErrors.NO_USERS_ARE_SPECIFIED);
        }

        return syncService.diff(syncRequest);
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
    *     "is-private": "false",
    *     "permission-strategy": "Full Access",
    *     "workspace-filters": {
    *       "selectedActivityPledgesSettings": -1,
    *       "searchMode": "0",
    *       "workspaceonly": false,
    *       "showArchived": false,
    *       "computedYear": 2015,
    *       "workspaces": [1, 2, 3, 8, 9, 11, 12, 15],
    *       "justSearch": false
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

    /**
     * Returns translations that were changed since last sync.
     *
     * For initial sync last-sync-time must be omitted. In this case it will return all translations. For incremental
     * updates clients should specify last-sync-time returned by last call to GET /sync.
     * <p>
     * If a translation to a requested language is missing then it will also be missing in response. There is no
     * fallback mechanism that returns translation from default or English language.
     * <p>
     * Response body is map of translation grouped by labels and locale code.
     *
     * <h3>Sample Request:</h3>
     * GET /rest/translations/translate?translations=en|it
     * <p>
     * Body:
     * <pre>
     * ["User", "Password"]
     * </pre>
     *
     * <h3>Sample Response:</h3>
     * <pre>
     * {
     *   "User": {
     *     "en": "user",
     *     "it": "utente"
     *   },
     *   "Password": {
     *     "en": "Password"
     *   }
     * }
     * </pre>
     * @param lastSyncTime optional timestamp of last synchronization time in ISO8601 format
     * @implicitParam translations|string|query|false|||||false|pipe separated list of language codes
     */
    @GET
    @Path("/translations")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Map<String, Map<String, String>> getTranslationsToSync(
            @QueryParam("last-sync-time") ISO8601TimeStamp lastSyncTime) {
        List<Translation> translations = syncService.getTranslationsToSync(lastSyncTime);
        return TranslationUtil.groupByLabelAndLocale(translations);
    }

    @Override
    public Class getErrorsClass() {
        return SynchronizerErrors.class;
    }
}
