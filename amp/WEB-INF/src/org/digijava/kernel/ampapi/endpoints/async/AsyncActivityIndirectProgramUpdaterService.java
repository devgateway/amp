package org.digijava.kernel.ampapi.endpoints.async;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.onepager.util.IndirectProgramUpdater;
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

public class AsyncActivityIndirectProgramUpdaterService {
    public static final String PROGRAM_UPDATER_KEY = "ASYNC_ACTIVITY_INDIRECT_PROGRAM_UPDATER_KEY";
    private static AsyncActivityIndirectProgramUpdaterService instance;

    public static AsyncActivityIndirectProgramUpdaterService getInstance() {
        if (instance == null) {
            instance = new AsyncActivityIndirectProgramUpdaterService();
        }
        return instance;
    }

    public void updateIndirectPrograms(String key) {

        AsyncResultCacher.addAsyncResultsData(key, new AsyncResult(key));

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        executor.execute(() -> {
            final List<Long> ampActivityIds = new ArrayList<>();
            PersistenceManager.inTransaction(() -> {
                TLSUtils.populateMockTlsUtils();

                PersistenceManager.getSession().doWork(conn -> {
                    String query = "select distinct amp_activity_id "
                            + "      from amp_program_settings aps, "
                            + "           amp_activity_program ap "
                            + "      where default_hierarchy = "
                            + "            (select settingsvalue::BIGINT "
                            + "             from amp_global_settings "
                            + "             where settingsname = 'NPD Default Primary Program') "
                            + "        and ap.program_setting = aps.amp_program_settings_id "
                            + "        and exists( "
                            + "              select 1 "
                            + "              from amp_activity a, "
                            + "                   amp_activities_categoryvalues aacv, "
                            + "                   amp_category_class acc, "
                            + "                   amp_category_value acv, "
                            + "                   amp_global_settings gs "
                            + "              where a.amp_activity_id = aacv.amp_activity_id "
                            + "                and acv.amp_category_class_id = acc.id "
                            + "                and acc.keyname = 'activity_status' "
                            + "                and acv.id = aacv.amp_categoryvalue_id "
                            + "                and gs.settingsvalue::BIGINT = acv.id "
                            + "                and gs.settingsname = 'Closed activity status' "
                            + "                and ap.amp_activity_id = a.amp_activity_id"
                            + "          )";
                    ampActivityIds.addAll(SQLUtils.fetchLongs(conn, query));
                });
            });
            final List<ApiErrorMessage> errors = new ArrayList<>();
            Map<String, Object> details = new ConcurrentHashMap<>();

            for (Long ampActivityId : ampActivityIds) {
                try {
                    (new IndirectProgramUpdater()).updateIndirectProgramMapping(ampActivityId);
                    details.put(ampActivityId + "", "Activity Updated Updated");
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
