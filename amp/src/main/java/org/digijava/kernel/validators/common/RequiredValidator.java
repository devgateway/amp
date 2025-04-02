package org.digijava.kernel.validators.common;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;

/**
 * @author Octavian Ciubotaru
 */
public class RequiredValidator implements ConstraintValidator {

    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        if ((type.isTranslatable() != null && type.isTranslatable())
                || type.getTranslationType() == TranslationSettings.TranslationType.TEXT) {
            String lang = context.getTranslatedValueContext().getLang();
            value = context.getTranslatedValueContext().getValue(lang);
        }

        if (value == null) {
            return false;
        } else if (value instanceof CharSequence) {
            return StringUtils.isNotBlank((CharSequence) value);
        } else if (value instanceof Collection) {
            return !((Collection) value).isEmpty();
        } else if (value instanceof MultilingualContent) {
            return StringUtils.isNotBlank(((MultilingualContent) value).getText());
        } else {
            return true;
        }
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ValidationErrors.FIELD_REQUIRED;
    }
}
