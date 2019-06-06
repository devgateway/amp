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
import javax.ws.rs.core.Response.Status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.exception.AmpWebApplicationException;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.types.ISO8601TimeStamp;
import org.digijava.kernel.services.sync.SyncService;
import org.digijava.kernel.services.sync.model.ExchangeRatesDiff;
import org.digijava.kernel.services.sync.model.SystemDiff;
import org.digijava.kernel.services.sync.model.Translation;
import org.digijava.kernel.util.SpringUtil;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.util.TeamUtil;

/**
 * @author Octavian Ciubotaru
 */
@Path("sync")
@Api("sync")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SynchronizerEndpoint implements ErrorReportingEndpoint {

    private SyncService syncService = SpringUtil.getBean(SyncService.class);

    @POST
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE}, id = "computeSync", ui = false)
    @ApiOperation(
            value = "Returns list of objects that were changed in specific period of time.",
            notes = "For initial sync caller must send only user ids. For subsequent syncs caller must send all "
                    + "fields.")
    public SystemDiff computeSync(SyncRequest syncRequest) {

        if (syncRequest.getUserIds() == null || syncRequest.getUserIds().isEmpty()) {
            ApiErrorResponseService.reportError(BAD_REQUEST, SynchronizerErrors.NO_USERS_ARE_SPECIFIED);
        }

        return syncService.diff(syncRequest);
    }

    @GET
    @Path("/workspaces")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "", ui = false, authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE})
    @ApiOperation(
            value = "Provides full workspaces definitions",
            notes = "Each workspace definition includes non-null/non-empty properties.")
    public List<AmpTeam> getWorkspaces(
            @ApiParam("must be set to true to include management workspace into the result")
            @DefaultValue("false") @QueryParam("management") Boolean includeManagement,
            @ApiParam("must be set to true to include private workspace into the result")
            @DefaultValue("false") @QueryParam("private") Boolean includePrivate) {
        return TeamUtil.getAllTeams(includeManagement, includePrivate);
    }

    @GET
    @Path("/translations")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "", ui = false, authTypes = {AuthRule.AMP_OFFLINE})
    @ApiOperation(
            value = "Returns translations that were changed since last sync.",
            notes = "For initial sync last-sync-time must be omitted. In this case it will return all translations. "
                    + "For incremental updates clients should specify last-sync-time returned by last call to "
                    + "GET /sync.\n\n"
                    + "If a translation to a requested language is missing then it will also be missing in response. "
                    + "There is no fallback mechanism that returns translation from default or English language.\n\n"
                    + "Response body is map of translation grouped by labels and locale code.")
    public Map<String, Map<String, String>> getTranslationsToSync(
            @QueryParam("last-sync-time") ISO8601TimeStamp lastSyncTime) {
        List<Translation> translations = syncService.getTranslationsToSync(lastSyncTime);
        return TranslationUtil.groupByLabelAndLocale(translations);
    }

    @GET
    @Path("/exchange-rates")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "exchange-rates", ui = false, authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE})
    @ApiOperation(
            value = "Returns exchange rates that changed since last sync.",
            notes = "For first time synchronization please use GET /rest/currency/exchange-rates.\n\n"
                    + "Result will contain the dates for which exchange rates were changed. Changed includes: "
                    + "deletions, additions, modifications. For correct sync, all locally stored exchange rates for "
                    + "specified days must be deleted and then replaced with new rates. Rates for a day will include "
                    + "all currency pairs, even if only one of them has changed.")
    public ExchangeRatesDiff getExchangeRatesToSync(
            @ApiParam(required = true, example = "2016-06-01T01:00:00.999+0000")
            @QueryParam("last-sync-time") ISO8601TimeStamp lastSyncTime) {
        if (lastSyncTime == null) {
            throw new AmpWebApplicationException(Status.BAD_REQUEST, SynchronizerErrors.LAST_SYNC_TIME_REQUIRED);
        }
        return syncService.getChangedExchangeRates(lastSyncTime);
    }

    @Override
    public Class getErrorsClass() {
        return SynchronizerErrors.class;
    }

}
