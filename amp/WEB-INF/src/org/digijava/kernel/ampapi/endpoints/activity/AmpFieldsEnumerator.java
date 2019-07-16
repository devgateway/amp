package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;

/**
 * @author Octavian Ciubotaru
 */
public final class AmpFieldsEnumerator {

    public static final FieldsEnumerator PUBLIC_ENUMERATOR;
    public static final FieldsEnumerator PRIVATE_ENUMERATOR;
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
