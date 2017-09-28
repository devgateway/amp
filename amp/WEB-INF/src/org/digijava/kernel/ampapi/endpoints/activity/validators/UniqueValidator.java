/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Validates that unique values are provided when within a list required to have
 * unique values
 * 
 * @author Nadejda Mandrescu
 */
public class UniqueValidator extends InputValidator {

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.FIELD_UNQUE_VALUES;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
        boolean isValid = true;
        String fieldName = fieldDescription.getFieldName();
        String uniqueField = fieldDescription.getUniqueConstraint();
        if (StringUtils.isNotBlank(uniqueField)) {
            // get Collection with values to be unique
            Collection<Map<String, Object>> fieldValue = (Collection<Map<String, Object>>) newFieldParent.get(fieldName);
            
            //Set that will hold the values that need to be different between each other
            Set<Object> idValuesSet = new HashSet<Object>();
            if (fieldValue != null && fieldValue.size() > 1) {
                Integer totalElements = populateUniqueValues(fieldValue, idValuesSet, uniqueField);
                
                //if the set contains less elements, then we have some repeated values
                if (idValuesSet.size() < totalElements) {
                    isValid = false;
                }
            }

        }

        return isValid;
    }

    /**
     * Populates a Set of unique id values from the Collection <JsonBean>
     * containing the fields and returns the number of unique id values.
     * 
     * @param fieldValue    the Collection <JsonBean> with a list of fields from which the
     * @param idValuesSet   the Set to be populated with unique id values
     * @param fieldPathToId String path of the parent.
     * @return number of unique id values
     */
    private Integer populateUniqueValues(Collection<Map<String, Object>> fieldValues, Set<Object> idValuesSet, String fieldPathToId) {
        Integer totalElements = 0;
        for (Map<String, Object> child : fieldValues) {
            if (child.get(fieldPathToId) != null) {
                totalElements++;
                idValuesSet.add(child.get(fieldPathToId));
            }
        }
        
        return totalElements;
    }
}
