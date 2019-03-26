package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.kernel.ampapi.endpoints.common.values.providers.CategoryValuePossibleValuesProvider;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author Viorel Chihai
 */
public class DocumentTypePossibleValuesProvider extends CategoryValuePossibleValuesProvider {

    public DocumentTypePossibleValuesProvider() {
        super(CategoryConstants.DOCUMENT_TYPE_KEY);
    }
}
