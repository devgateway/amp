/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Validates that agreement title field value is not blank
 * 
 * @author Viorel Chihai
 */
public class AgreementTitleValidator extends InputValidator {
    
    private String agreementCodeTitle = "fundings~agreement~title";
    
    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.AGREEMENT_TITLE_REQUIRED;
    }

    @Override
    public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent,
            Map<String, Object> oldFieldParent, JsonBean fieldDescription, String fieldPath) {
        
        String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
        // this validator only validates agreement title
        if (agreementCodeTitle.equals(fieldPath)) {
            // validate the agreement title (not blank)
            String agreementTitle = StringUtils.trim((String) newFieldParent.get(fieldName));
            if (StringUtils.isBlank(agreementTitle)) {
                return false;
            } 
        }

        return true;
    }
}
