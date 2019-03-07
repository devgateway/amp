package org.digijava.kernel.services;

import java.util.function.Function;

import org.digijava.kernel.ampapi.endpoints.activity.AMPFMService;
import org.digijava.kernel.ampapi.endpoints.activity.AllowMultipleProgramsPredicate;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
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

    private static CachingFieldsEnumerator enumerator;

    // TODO remove this enumerator
    private static CachingFieldsEnumerator contactEnumerator;

    @Autowired
    private SyncDAO syncDAO;

    public static CachingFieldsEnumerator getEnumerator() {
        return enumerator;
    }

    public static CachingFieldsEnumerator getContactEnumerator() {
        return contactEnumerator;
    }

    @Override
    public void afterPropertiesSet() {
        AmpFieldInfoProvider provider = new AmpFieldInfoProvider(AmpActivityVersion.class);

        Function<String, Boolean> allowMultiplePrograms = new AllowMultipleProgramsPredicate();

        String iatiIdentifierFieldName = InterchangeUtils.getAmpIatiIdentifierFieldName();

        AMPFMService fmService = new AMPFMService();
        enumerator = new CachingFieldsEnumerator(syncDAO,
                new ActivityFieldsEnumerator(provider, fmService, AMPTranslatorService.INSTANCE,
                        allowMultiplePrograms, iatiIdentifierFieldName));

        AmpFieldInfoProvider contactFieldInfoProvider = new AmpFieldInfoProvider(AmpContact.class);
        contactEnumerator = new CachingFieldsEnumerator(syncDAO,
                new FieldsEnumerator(contactFieldInfoProvider, fmService, AMPTranslatorService.INSTANCE,
                        allowMultiplePrograms));
    }
}
