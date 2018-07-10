/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;

/**
 * Validates the presence of multiple primary contacts of a certain type
 * 
 * @author Viorel Chihai
 */
public class PrimaryContactValidator extends InputValidator {
    
    public static Set<String> CONTACT_TYPE_FIELDS = new HashSet<String>() {{
        add(InterchangeUtils.underscorify(ActivityFieldsConstants.DONOR_CONTACT));
        add(InterchangeUtils.underscorify(ActivityFieldsConstants.PROJECT_COORDINATOR_CONTACT));
        add(InterchangeUtils.underscorify(ActivityFieldsConstants.SECTOR_MINISTRY_CONTACT));
        add(InterchangeUtils.underscorify(ActivityFieldsConstants.MOFED_CONTACT));
        add(InterchangeUtils.underscorify(ActivityFieldsConstants.IMPL_EXECUTING_AGENCY_CONTACT));
    }};
    
    public PrimaryContactValidator() {
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.UNIQUE_PRIMARY_CONTACT;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
        
        boolean isValid = true;
        String fieldName = fieldDescription.getFieldName();
        
        if (CONTACT_TYPE_FIELDS.contains(fieldName)) {
            Collection<Map<String, Object>> fieldValues = (Collection<Map<String, Object>>) newFieldParent.get(fieldName);

            if (fieldValues != null && fieldValues.size() > 1) {
                String primaryContactFieldName = InterchangeUtils.underscorify(ActivityFieldsConstants.PRIMARY_CONTACT);
                int primaryContactCnt = 0;
                
                for (Map<String, Object> child : fieldValues) {
                    String childInput = String.valueOf(child.get(primaryContactFieldName));
                    
                    if (Boolean.valueOf(childInput)) {
                        primaryContactCnt++;
                    }
                }
                if (primaryContactCnt > 1) {
                    isValid = false;
                }
            }
        }

        return isValid;
    }
}
