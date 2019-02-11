package org.digijava.kernel.services;

import java.lang.reflect.Field;
import java.util.function.Function;

import org.digijava.kernel.ampapi.endpoints.activity.AMPFMService;
import org.digijava.kernel.ampapi.endpoints.activity.AllowMultipleProgramsPredicate;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.ActivityFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.field.AmpFieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.CachingFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.services.sync.SyncDAO;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Octavian Ciubotaru
 */
@Component
public final class AmpFieldsEnumerator implements InitializingBean {

    private static CachingFieldsEnumerator publicEnumerator;
    private static CachingFieldsEnumerator privateEnumerator;

    // TODO remove these enumerators
    private static CachingFieldsEnumerator publicContactEnumerator;
    private static CachingFieldsEnumerator privateContactEnumerator;

    @Autowired
    private SyncDAO syncDAO;

    public static CachingFieldsEnumerator getPublicEnumerator() {
        return publicEnumerator;
    }

    /**
     * Private enumerator main scope is to restore back references to the activity.
     *
     * It adds new extra fields that point to parent activity and then through
     * {@link ObjectImporter#setupNotImportableField(Object, Field)}
     * restores the reference.
     *
     * TODO rethink & make it work for other back references too!
     */
    public static CachingFieldsEnumerator getPrivateEnumerator() {
        return privateEnumerator;
    }

    public static CachingFieldsEnumerator getPublicContactEnumerator() {
        return publicContactEnumerator;
    }

    public static CachingFieldsEnumerator getPrivateContactEnumerator() {
        return privateContactEnumerator;
    }

    @Override
    public void afterPropertiesSet() {
        AmpFieldInfoProvider provider = new AmpFieldInfoProvider(AmpActivityVersion.class);

        Function<String, Boolean> allowMultiplePrograms = new AllowMultipleProgramsPredicate();

        String iatiIdentifierFieldName = InterchangeUtils.getAmpIatiIdentifierFieldName();

        AMPFMService fmService = new AMPFMService();
        publicEnumerator = new CachingFieldsEnumerator(syncDAO,
                new ActivityFieldsEnumerator(provider, fmService, AMPTranslatorService.INSTANCE,
                        false, allowMultiplePrograms, iatiIdentifierFieldName));
        privateEnumerator = new CachingFieldsEnumerator(syncDAO,
                new ActivityFieldsEnumerator(provider, fmService, AMPTranslatorService.INSTANCE,
                        true, allowMultiplePrograms, iatiIdentifierFieldName));

        AmpFieldInfoProvider contactFieldInfoProvider = new AmpFieldInfoProvider(AmpContact.class);
        publicContactEnumerator = new CachingFieldsEnumerator(syncDAO,
                new FieldsEnumerator(contactFieldInfoProvider, fmService, AMPTranslatorService.INSTANCE,
                        false, allowMultiplePrograms));
        privateContactEnumerator = new CachingFieldsEnumerator(syncDAO,
                new FieldsEnumerator(contactFieldInfoProvider, fmService, AMPTranslatorService.INSTANCE,
                        true, allowMultiplePrograms));
    }
}
