/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.util.ActivityVersionUtil;

/**
 * Validates amp_activity_id and amp_id to be valid
 * 
 * @author Nadejda Mandrescu
 */
public class AmpActivityIdValidator extends InputValidator {
    private boolean isOldActivityId = false;

    @Override
    public ApiErrorMessage getErrorMessage() {
        if (isOldActivityId) {
            return ActivityErrors.UPDATE_ID_IS_OLD;
        }
        return ActivityErrors.FIELD_INVALID_VALUE;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
        // REFACTOR: let's define a flag (count = 2) once both amp_activity_id and amp_id are verified to immediately skip this validator
        boolean isValid = true;
        String fieldName = fieldDescription.getFieldName();
        ActivityImporter activityImporter = (ActivityImporter) importer;
        
        // verify amp_activity_id, that is our main reference 
        if (ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME.equals(fieldPath)) {
            String internalId = String.valueOf(newFieldParent.get(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME));
            Long ampActivityId = NumberUtils.isNumber(internalId) ? Long.valueOf(internalId) : null; 
            if (activityImporter.isUpdate()) {
                Long latestAmpActivityId = ampActivityId == null ? null :  
                        ActivityVersionUtil.getLastVersionForVersion(Long.valueOf(ampActivityId.toString()));
                // if this is an update and we cannot match the id, then we report it as invalid
                if (activityImporter.getOldActivity() == null || latestAmpActivityId == null
                        || !latestAmpActivityId.equals(activityImporter.getOldActivity().getAmpActivityId())) {
                    isValid = false;
                    isOldActivityId = activityImporter.getOldActivity() != null;
                    if (isOldActivityId) {
                        activityImporter.setLatestActivityId(latestAmpActivityId);
                    }
                }
            } else if ((ampActivityId != null)) {
                    isValid = false;
            }
            
        // verify amp_id
        } else if (InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ID).equals(fieldName)) {
            String ampId = (String) newFieldParent.get(InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ID));
            if (activityImporter.isUpdate()) {
                if (activityImporter.getOldActivity() == null
                        || !activityImporter.getOldActivity().getAmpId().equals(ampId)) {
                    isValid = false;
                }
            } else {
                if (ampId != null) {
                    isValid = false;
                }
            }
        }
        return isValid;
    }
}
