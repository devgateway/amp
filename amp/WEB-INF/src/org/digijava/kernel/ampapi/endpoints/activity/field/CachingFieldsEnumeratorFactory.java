package org.digijava.kernel.ampapi.endpoints.activity.field;

import org.digijava.kernel.ampapi.endpoints.activity.AMPFeatureManagerService;
import org.digijava.kernel.ampapi.endpoints.activity.AllowMultipleProgramsPredicate;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.services.sync.SyncDAO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author Viorel Chihai
 */
public class CachingFieldsEnumeratorFactory {

    private SyncDAO syncDAO;

    private Map<Long, CachingFieldsEnumerator> cache = new ConcurrentHashMap<>();

    public CachingFieldsEnumeratorFactory(SyncDAO syncDAO) {
        this.syncDAO = syncDAO;
    }

    public void buildDefaultEnumerator() {
        cache.put(-1L, buildEnumerator(new FMVisibility()));
    }

    public CachingFieldsEnumerator getDefaultEnumerator() {
        return cache.get(-1L);
    }

    public CachingFieldsEnumerator getEnumerator(Long templateId) {
        CachingFieldsEnumerator cacheFieldsEnumerator = buildEnumerator(new FMVisibility(templateId));
        CachingFieldsEnumerator result = cache.putIfAbsent(templateId, cacheFieldsEnumerator);
        if (result == null) {
            result = cache.putIfAbsent(templateId, cacheFieldsEnumerator);
        }
        return result;
    }

    private CachingFieldsEnumerator buildEnumerator(FMVisibility fmVisibility) {
        AmpFieldInfoProvider fieldProvider = new AmpFieldInfoProvider();
        AMPFeatureManagerService fmService = new AMPFeatureManagerService(fmVisibility);
        Function<String, Boolean> allowMultiplePrograms = new AllowMultipleProgramsPredicate();

        return new CachingFieldsEnumerator(syncDAO,
                new FieldsEnumerator(fieldProvider, fmService, AMPTranslatorService.INSTANCE, allowMultiplePrograms));
    }
}
