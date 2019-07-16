package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.kernel.ampapi.endpoints.contact.AmpCategoryPossibleValuesProvider;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author Viorel Chihai
 */
public class DocumentTypePossibleValuesProvider extends AmpCategoryPossibleValuesProvider {

    public DocumentTypePossibleValuesProvider() {
        super(CategoryConstants.DOCUMENT_TYPE_KEY);
    }
}
