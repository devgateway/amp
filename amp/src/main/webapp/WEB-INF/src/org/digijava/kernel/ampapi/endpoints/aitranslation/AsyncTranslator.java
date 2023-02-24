package org.digijava.kernel.ampapi.endpoints.aitranslation;

import static org.digijava.module.aim.helper.GlobalSettingsConstants.MACHINE_TRANSLATION_ENABLED;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.dgfoundation.amp.aitranslation.CachedMachineTranslationService;
import org.dgfoundation.amp.aitranslation.GoogleMachineTranslationService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Keeps track of all currently running translation operations.
 *
 * @author Octavian Ciubotaru
 */
public class AsyncTranslator {

    private static final int MAX_CACHE_SIZE = 100;
    private static final int EXPIRE_AFTER_MIN = 10;

    private CachedMachineTranslationService service =
            new CachedMachineTranslationService(new GoogleMachineTranslationService());

    private Cache<String, Future<Map<String, String>>> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(EXPIRE_AFTER_MIN, TimeUnit.MINUTES)
            .build();

    /**
     * Start a translation operation. If there are too many operations running will return null.
     *
     * @param request translation request
     * @return id of the long running operation
     * @throws ApiRuntimeException if Machine Translation is not enabled
     */
    public String translate(TranslationRequest request) {
        boolean mtEnabled = FeaturesUtil.getGlobalSettingValueBoolean(MACHINE_TRANSLATION_ENABLED);

        if (!mtEnabled) {
            throw new ApiRuntimeException(Response.Status.INTERNAL_SERVER_ERROR,
                    ApiError.toError(MachineTranslationErrors.NOT_ENABLED));
        }

        cache.cleanUp();
        if (cache.size() > MAX_CACHE_SIZE) {
            return null;
        } else {
            String id = UUID.randomUUID().toString();
            String srcLang = request.getSourceLanguageCode();
            String destLang = request.getTargetLanguageCode();
            cache.put(id, service.translate(srcLang, destLang, request.getContents()));
            return id;
        }
    }

    /**
     * Retrieve the status of a long running operation. Returns null if the operation does not exist.
     *
     * @param id id of the long running operation
     * @return operation or null
     */
    public TranslationOperation getOperation(String id) {
        Future<Map<String, String>> future = cache.getIfPresent(id);
        if (future == null) {
            return null;
        }

        if (!future.isDone()) {
            return TranslationOperation.inProgress();
        } else {
            cache.invalidate(id);
            if (future.isCancelled()) {
                return TranslationOperation.withError("cancelled");
            } else {
                try {
                    return TranslationOperation.withResult(future.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    return TranslationOperation.withError(e.getCause().getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return TranslationOperation.withError(e.getMessage());
                }
            }
        }
    }
}
