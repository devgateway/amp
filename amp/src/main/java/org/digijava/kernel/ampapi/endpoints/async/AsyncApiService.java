package org.digijava.kernel.ampapi.endpoints.async;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityImportRules;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ApiContext;
import org.digijava.kernel.ampapi.endpoints.activity.dto.SwaggerActivity;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.AMP_ID_FIELD_NAME;

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
        final ApiContext apiContext = AsyncUtil.buildApiContext();
    
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

}
