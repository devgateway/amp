/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

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
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
        
        String fieldName = fieldDescription.getFieldName();
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
