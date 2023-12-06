package org.digijava.kernel.ampapi.endpoints.async;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.onepager.util.IndirectSectorUpdater;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.GenericErrors;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class AsyncActivityIndirectSectorUpdaterService {
    public static final String SECTOR_UPDATER_KEY = "ASYNC_ACTIVITY_INDIRECT_SECTOR_UPDATER_KEY";
    private static AsyncActivityIndirectSectorUpdaterService instance;
    public static AsyncActivityIndirectSectorUpdaterService getInstance() {
        if (instance == null) {
            instance = new AsyncActivityIndirectSectorUpdaterService();
        }
        return instance;
    }

    public void updateIndirectSectors(String key) {

        AsyncResultCacher.addAsyncResultsData(key, new AsyncResult(key));

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        executor.execute(() -> {
            final List<Long> ampActivityIds = new ArrayList<>();
            PersistenceManager.inTransaction(() -> {
                TLSUtils.populateMockTlsUtils();

                PersistenceManager.getSession().doWork(conn -> {

                    String query =  " SELECT DISTINCT actSec.amp_activity_id " +
                                    " FROM amp_sector_mapping secMap " +
                                    " JOIN amp_activity_sector actSec ON secMap.src_sector_id = actSec.amp_sector_id " +
                                    " WHERE EXISTS( " +
                                    "   SELECT 1 " +
                                    "   FROM amp_activity a, " +
                                    "       amp_activities_categoryvalues aacv, " +
                                    "       amp_category_class acc, " +
                                    "       amp_category_value acv, " +
                                    "       amp_global_settings gs " +
                                    "   WHERE a.amp_activity_id = aacv.amp_activity_id " +
                                    "       AND acv.amp_category_class_id = acc.id " +
                                    "       AND acc.keyname = 'activity_status' " +
                                    "       AND acv.id = aacv.amp_categoryvalue_id " +
                                    "       AND gs.settingsvalue::BIGINT = acv.id " +
                                    "       AND gs.settingsname = 'Closed activity status' " +
                                    "       AND actSec.amp_activity_id = a.amp_activity_id )";
                    ampActivityIds.addAll(SQLUtils.fetchLongs(conn, query));
                });
            });
            final List<ApiErrorMessage> errors = new ArrayList<>();
            Map<String, Object> details = new ConcurrentHashMap<>();

            for (Long ampActivityId : ampActivityIds) {
                try {
                    (new IndirectSectorUpdater()).updateIndirectSectorMapping(ampActivityId);
                    details.put(ampActivityId + "", "Activity Updated");
                } catch (Exception e) {
                    errors.add(GenericErrors.INTERNAL_ERROR.withDetails(ampActivityId
                            + "failed with error" + e.toString()));
                }
            }
            AsyncResultCacher.getAsyncResult(key).getResults().add(new JsonApiResponse(ApiError.toError(errors),
                    details));
            AsyncResultCacher.getAsyncResult(key).setStatus(AsyncStatus.DONE);
        });
        executor.shutdown();
    }
}
