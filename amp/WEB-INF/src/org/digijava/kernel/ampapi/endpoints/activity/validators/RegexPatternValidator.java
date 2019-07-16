/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import clover.org.apache.commons.lang.StringUtils;

/**
 * Validates that the value against the regex
 * 
 * @author Viorel Chihai
 */
public class RegexPatternValidator extends InputValidator {

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.FIELD_INVALID_VALUE;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
        boolean isValid = true;
        
        String regexPattern = fieldDescription.getRegexPattern();
        String fieldName = fieldDescription.getFieldName();
        Object fieldValue = newFieldParent.get(fieldName);
        if (fieldValue != null && StringUtils.isNotBlank(regexPattern)) {
            isValid = match(regexPattern, fieldValue);
        }
        
        return isValid;
    }

    /**
     * Match the string against the regular expression
     * 
     * @param regexPattern, the regular expression pattern
     * @param fieldValue, the Object to check
     * @return true if the value matches the regex pattern
     */
    private boolean match(String regexPattern, Object fieldValue) {
        String value = (String) fieldValue;
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(value);
        
        return matcher.matches();
    }

}
