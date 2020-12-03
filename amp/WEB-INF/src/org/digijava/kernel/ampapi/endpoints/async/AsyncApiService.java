package org.digijava.kernel.ampapi.endpoints.async;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.AMP_ID_FIELD_NAME;
import static org.digijava.module.aim.util.FeaturesUtil.AMP_TREE_VISIBILITY_ATTR;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;

import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImportRules;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ApiContext;
import org.digijava.kernel.ampapi.endpoints.activity.dto.SwaggerActivity;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.ampapi.filters.ClientMode;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.util.TeamUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author Viorel Chihai
 */
public class AsyncApiService {
    
    private static AsyncApiService instance;
    
    public static AsyncApiService getInstance() {
        if (instance == null) {
            instance = new AsyncApiService();
        }
        
        return instance;
    }
    
    public void importActivities(ActivityImportRules rules, String resultId, List<SwaggerActivity> activitiesJson,
                                 URI baseUri) {
        final ApiContext apiContext = buildApiContext();
    
        if (AsyncResultCacher.getAsyncResult(resultId) == null) {
            AsyncResultCacher.addAsyncResultsData(resultId, new AsyncResult(resultId));
        }
    
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        executor.execute(() -> PersistenceManager.inTransaction(() -> {
            TLSUtils.populateMockTlsUtilsWithApiContext(apiContext);
            for (SwaggerActivity act : activitiesJson) {
                boolean toUpdate = act.getMap().containsKey(AMP_ID_FIELD_NAME);
                AsyncResultCacher.getAsyncResult(resultId)
                        .getResults()
                        .add(ActivityInterchangeUtils.importActivity(act.getMap(), toUpdate, rules,
                                baseUri + "activity"));
            }
            AsyncResultCacher.getAsyncResult(resultId).setStatus(AsyncStatus.DONE);
        }));
    
        executor.shutdown();
    }
    
    @NotNull
    public ApiContext buildApiContext() {
        ApiContext apiContext = new ApiContext();
        HttpServletRequest request = TLSUtils.getRequest();
        apiContext.setSessionServletContext(request.getSession().getServletContext());
        apiContext.setTeamMember(TeamUtil.getCurrentMember());
        apiContext.setUser(TeamUtil.getCurrentUser());
        apiContext.setAmpTreeVisibility((AmpTreeVisibility) request.getSession()
                .getAttribute(AMP_TREE_VISIBILITY_ATTR));
        apiContext.setSite(TLSUtils.getSite());
        apiContext.setAmpTreeVisibilityModificationDate((Date) request.getSession()
                .getAttribute("ampTreeVisibilityModificationDate"));
        apiContext.setRequestURL(request.getRequestURL());
        apiContext.setRootPath(TLSUtils.getRequest().getServletContext().getRealPath("/"));
    
        ClientMode clientMode = null;
        
        if (AmpClientModeHolder.isOfflineClient()) {
            clientMode = ClientMode.AMP_OFFLINE;
        } else if (AmpClientModeHolder.isIatiImporterClient()) {
            clientMode = ClientMode.IATI_IMPORTER;
        }
        
        apiContext.setClientMode(clientMode);
        
        return apiContext;
    }
}
