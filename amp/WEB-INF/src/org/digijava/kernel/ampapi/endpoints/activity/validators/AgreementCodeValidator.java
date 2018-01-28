/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.digijava.module.aim.util.DbUtil;

/**
 * Validates that agreement code field value is unique across AMP
 * 
 * @author Viorel Chihai
 */
public class AgreementCodeValidator extends InputValidator {
    
    private boolean missingCode = false;
    private String agreementCodePath = "fundings~agreement~code";

    @Override
    public ApiErrorMessage getErrorMessage() {
        if (missingCode) {
            return ActivityErrors.AGREEMENT_CODE_REQUIRED;
        }
        
        return ActivityErrors.AGREEMENT_CODE_UNIQUE;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
        boolean isValid = true;
        String fieldName = fieldDescription.getFieldName();
        // this validator only validates agreement code
        if (agreementCodePath.equals(fieldPath)) {
            // validate the agreement code
            String agreementCode = StringUtils.trim((String) newFieldParent.get(fieldName));
            if (StringUtils.isBlank(agreementCode)) {
                isValid = false;
                missingCode = true;
            } else {
                List<AmpAgreement> agreements = DbUtil.getAgreementsByCode(agreementCode);
                isValid = agreements == null || agreements.size() < 1;
            }
        }

        return isValid;
    }
}
