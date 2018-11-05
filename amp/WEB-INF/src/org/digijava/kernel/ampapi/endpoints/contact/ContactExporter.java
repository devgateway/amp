package org.digijava.kernel.ampapi.endpoints.contact;

import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectExporter;
import org.digijava.module.aim.dbentity.AmpContact;

/**
 * @author Octavian Ciubotaru
 */
public class ContactExporter extends ObjectExporter<AmpContact> {

    public ContactExporter() {
        super(AmpFieldsEnumerator.getPublicContactEnumerator().getContactFields());
    }
}
