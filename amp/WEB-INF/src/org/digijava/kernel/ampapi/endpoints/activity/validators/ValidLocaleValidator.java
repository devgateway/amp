package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Verifies if the string fields contains valid languages in translations
 * 
 * @author Viorel Chihai
 */
public class ValidLocaleValidator extends InputValidator {

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.LOCALE_INVALID;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
            APIField fieldDescription, String fieldPath) {
    
        String fieldName = fieldDescription.getFieldName();
        FieldType fieldType = fieldDescription.getApiType().getFieldType();
        Object item = newFieldParent.get(fieldName);
    
        if (item == null) {
            return true;
        }
        
        Collection<String> allowedLangCodes = importer.getTrnSettings().getAllowedLangCodes();
        if (fieldType == FieldType.STRING && fieldDescription.isTranslatable()) {
            if (Map.class.isAssignableFrom(item.getClass())) {
                Map<String, Object> map = (Map<String, Object>) item;
                for (Map.Entry<String, Object> castedEntry : map.entrySet()) {
                    if (!allowedLangCodes.contains(castedEntry.getKey())) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
}
