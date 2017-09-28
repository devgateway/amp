/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityFields;

/**
 * Validates that field value is allowed
 * 
 * @author Nadejda Mandrescu
 */
public class ValueValidator extends InputValidator {

    protected boolean isValidLength = true;
    protected boolean isValidPercentage = true;
    @Override
    public ApiErrorMessage getErrorMessage() {
        if (!isValidLength)
            return ActivityErrors.FIELD_INVALID_LENGTH;
        if (!isValidPercentage)
            return ActivityErrors.FIELD_INVALID_PERCENTAGE;
        return ActivityErrors.FIELD_INVALID_VALUE;
    }

    @Override
    public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent, 
            Map<String, Object> oldFieldParent, JsonBean fieldDescription, String fieldPath) {
        
        boolean importable = (boolean) fieldDescription.get(ActivityEPConstants.IMPORTABLE);
        // input type, allowed input will be verified before, so nothing check here 
        if (!importable)
            return true;
        
        //temporary debug
        if (!isValidLength(newFieldParent, fieldDescription))
            return false;
        if (!isValidPercentage(newFieldParent, fieldDescription)) 
            return false;
        
        List<JsonBean> possibleValues = importer.getPossibleValuesForFieldCached(fieldPath, AmpActivityFields.class, null);
        
        if (possibleValues.size() != 0) {
            Object value = newFieldParent.get(fieldDescription.getString(ActivityEPConstants.FIELD_NAME));
            
            if (value != null) {
                boolean idOnly = Boolean.TRUE.equals(fieldDescription.get(ActivityEPConstants.ID_ONLY));
                // convert to string the ids to avoid long-integer comparison
                value = idOnly ? value.toString() : value;
                
                for (JsonBean option: possibleValues) {
                    if (idOnly) {
                        if (value.equals(option.getString(ActivityEPConstants.ID)))
                            return true;
                    } else {
                        if (value.equals(option.get(ActivityEPConstants.VALUE)))
                            return true;                        
                    }
                }
                // wrong value configured if it is not found in allowed options 
                return false;
            }
        }
        // nothing failed so far? then we are good to go
        return true;
    }
    



    private boolean isValidPercentage(Map<String, Object> newFieldParent,
            JsonBean fieldDescription) {
        this.isValidPercentage  = true;
        if (fieldDescription.get(ActivityEPConstants.PERCENTAGE) == null)
            return true; //this doesn't contain a percentage-based field

        //attempt to get the number out of this one
        Double val = InterchangeUtils.getDoubleFromJsonNumber(newFieldParent.get(fieldDescription.get(ActivityEPConstants.FIELD_NAME)));
        if (val == null || val < ActivityEPConstants.EPSILON || val - 100.0 > ActivityEPConstants.EPSILON) {
            this.isValidPercentage = false;
            return false;
        }
        
        return true;
    }

    protected boolean isValidLength(Map<String, Object> newFieldParent, JsonBean fieldDescription) {
        isValidLength = true;
        Integer maxLength = (Integer) fieldDescription.get(ActivityEPConstants.FIELD_LENGTH); 
        if (maxLength != null) {
            Object obj = newFieldParent.get(fieldDescription.getString(ActivityEPConstants.FIELD_NAME));
            if (obj != null) {
                if (!Boolean.TRUE.equals(fieldDescription.get(ActivityEPConstants.TRANSLATABLE))) {
                    isValidLength = isValidLength(obj, maxLength);
                } else if (Map.class.isAssignableFrom(obj.getClass())) {
                    for (Object trn : ((Map) obj).values()) {
                        if (!isValidLength(trn, maxLength)) {
                            isValidLength = false;
                            break;
                        }
                    }
                    // translatable means its input must be a map, otherwise invalid input was provided, so we cannot say it's invalid
                }
            }
        }
        return isValidLength;
    }
    
    protected boolean isValidLength(Object obj, Integer maxLength) {
        if (obj == null)
            return true;
        if (String.class.isAssignableFrom(obj.getClass())){
            if (maxLength < ((String) obj).length())
                return false;
        }
        return true;
    }
}
