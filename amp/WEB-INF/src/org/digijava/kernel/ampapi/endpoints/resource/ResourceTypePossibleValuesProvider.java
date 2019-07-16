package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.kernel.ampapi.endpoints.contact.AmpCategoryPossibleValuesProvider;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author Viorel Chihai
 */
public class ResourceTypePossibleValuesProvider extends AmpCategoryPossibleValuesProvider {

    public ResourceTypePossibleValuesProvider() {
        super(CategoryConstants.DOCUMENT_TYPE_KEY);
    }
}
