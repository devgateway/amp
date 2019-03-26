package org.digijava.kernel.ampapi.endpoints.contact;

import org.digijava.kernel.ampapi.endpoints.common.values.providers.CategoryValuePossibleValuesProvider;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author Octavian Ciubotaru
 */
public class ContactTitlePossibleValuesProvider extends CategoryValuePossibleValuesProvider {

    public ContactTitlePossibleValuesProvider() {
        super(CategoryConstants.CONTACT_TITLE_KEY);
    }
}
