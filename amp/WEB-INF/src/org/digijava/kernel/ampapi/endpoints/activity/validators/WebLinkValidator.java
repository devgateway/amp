package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.UrlValidator;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceErrors;

/**
 * Validates the URL of a web link
 * 
 * @author Viorel Chihai
 */
public class WebLinkValidator extends InputValidator {
    
    private String webLinkCodePath = "web_link";

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ResourceErrors.FIELD_INVALID_URL_VALUE;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
            APIField fieldDescription, String fieldPath) {
        boolean isValid = true;
        String fieldName = fieldDescription.getFieldName();
        // this validator only validates web link
        if (webLinkCodePath.equals(fieldPath)) {
            String webLink = StringUtils.trim((String) newFieldParent.get(fieldName));
            if (StringUtils.isBlank(webLink)) {
                return true;
            } else {
                UrlValidator urlValidator = new UrlValidator();
                isValid = urlValidator.isValid(webLink);
            }
        }

        return isValid;
    }
}
