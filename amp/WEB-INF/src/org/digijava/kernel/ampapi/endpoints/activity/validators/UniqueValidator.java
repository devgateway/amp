package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
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
        String fieldName = fieldDescription.getFieldName();
        String uniqueField = fieldDescription.getUniqueConstraint();
        if (StringUtils.isNotBlank(uniqueField)) {
            Collection<?> newValues = (Collection<?>) newFieldParent.get(fieldName);
            if (newValues != null) {
                Set<Object> uniqueValues = new HashSet<>();
                if (fieldDescription.getApiType().isSimpleItemType()) {
                    uniqueValues.addAll(newValues);
                } else {
                    uniqueValues.addAll(((Collection<Map<String, Object>>) newValues).stream()
                            .map(e -> e.get(uniqueField)).collect(Collectors.toList()));
                }
                uniqueValues.remove(null);
                return uniqueValues.size() == newValues.size();
            }
        }
        return true;
    }
    
}
