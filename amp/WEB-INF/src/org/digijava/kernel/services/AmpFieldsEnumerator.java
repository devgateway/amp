package org.digijava.kernel.services;

import java.util.function.Function;

import org.digijava.kernel.ampapi.endpoints.activity.AMPFMService;
import org.digijava.kernel.ampapi.endpoints.activity.AllowMultipleProgramsPredicate;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.AmpFieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.CachingFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.services.sync.SyncDAO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Octavian Ciubotaru
 */
@Component
public final class AmpFieldsEnumerator implements InitializingBean {

    private static CachingFieldsEnumerator enumerator;

    @Autowired
    private SyncDAO syncDAO;

    public static CachingFieldsEnumerator getEnumerator() {
        return enumerator;
    }

    @Override
    public void afterPropertiesSet() {
        AmpFieldInfoProvider fieldProvider = new AmpFieldInfoProvider();
        Function<String, Boolean> allowMultiplePrograms = new AllowMultipleProgramsPredicate();

        AMPFMService fmService = new AMPFMService();
        enumerator = new CachingFieldsEnumerator(syncDAO,
                new FieldsEnumerator(fieldProvider, fmService, AMPTranslatorService.INSTANCE, allowMultiplePrograms));
    }
}
