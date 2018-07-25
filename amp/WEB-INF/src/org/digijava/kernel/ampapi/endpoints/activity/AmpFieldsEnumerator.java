package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;

/**
 * TODO enumerated fields are good candidate for caching, they only change when FM entries change!
 *
 * @author Octavian Ciubotaru
 */
public final class AmpFieldsEnumerator {

    public static final FieldsEnumerator PUBLIC_ENUMERATOR;

    /**
     * Private enumerator main scope is to restore back references to the activity.
     *
     * It adds new extra fields that point to parent activity and then through
     * {@link ActivityImporter#setupNotImportableField(java.lang.Object, java.lang.reflect.Field)}
     * restores the reference.
     *
     * TODO rethink & make it work for other back references too!
     */
    public static final FieldsEnumerator PRIVATE_ENUMERATOR;

    // TODO remove these enumerators
    public static final FieldsEnumerator PUBLIC_CONTACT_ENUMERATOR;
    public static final FieldsEnumerator PRIVATE_CONTACT_ENUMERATOR;

    static {
        AmpFieldInfoProvider provider = new AmpFieldInfoProvider(AmpActivityVersion.class);
       
        AMPFMService fmService = new AMPFMService();
        PUBLIC_ENUMERATOR = new FieldsEnumerator(provider, fmService, AMPTranslatorService.INSTANCE, false);
        PRIVATE_ENUMERATOR = new FieldsEnumerator(provider, fmService, AMPTranslatorService.INSTANCE, true);
        
        AmpFieldInfoProvider contactFieldInfoProvider = new AmpFieldInfoProvider(AmpContact.class);
        PUBLIC_CONTACT_ENUMERATOR = 
                new FieldsEnumerator(contactFieldInfoProvider, fmService, AMPTranslatorService.INSTANCE, false);
        PRIVATE_CONTACT_ENUMERATOR = 
                new FieldsEnumerator(contactFieldInfoProvider, fmService, AMPTranslatorService.INSTANCE, true);
    }

    private AmpFieldsEnumerator() {
    }
}
