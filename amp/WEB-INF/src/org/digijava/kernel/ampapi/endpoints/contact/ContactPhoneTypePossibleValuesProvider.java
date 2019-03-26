package org.digijava.kernel.ampapi.endpoints.contact;

import org.digijava.kernel.ampapi.endpoints.common.values.providers.CategoryValuePossibleValuesProvider;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author Octavian Ciubotaru
 */
public class ContactPhoneTypePossibleValuesProvider extends CategoryValuePossibleValuesProvider {

    public ContactPhoneTypePossibleValuesProvider() {
        super(CategoryConstants.CONTACT_PHONE_TYPE_KEY);
    }
}
