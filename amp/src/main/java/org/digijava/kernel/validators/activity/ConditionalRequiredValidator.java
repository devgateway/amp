package org.digijava.kernel.validators.activity;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;

/**
 * Validate fields marked with specific dependency to be required under certain conditions. Only fields that
 * match the 'required' attribute will be taken in account. In other words, if this validator was initialized
 * with 'required=Y' then only fields that have {@link APIField#getDependencyRequired()} also 'Y' will be validated.
 *
 * Validator is active only when {@link ConditionalRequiredValidator#isActive(APIField, Object)} returns true.
 *
 * Dependency name used to mark required fields must be specified via constructor.
 *
 * Translatable fields are not supported yet.
 *
 * Note: will validate only the fields of the validated object. This validator does not recurse.
 *
 * @author Octavian Ciubotaru
 */
public abstract class ConditionalRequiredValidator implements ConstraintValidator {

    private String requiredFieldDependency;
    private String requiredStatus;

    public ConditionalRequiredValidator(String requiredFieldDependency) {
        this.requiredFieldDependency = requiredFieldDependency;
    }

    /**
     * 'required' argument must be specified.
     *
     * @param arguments arguments for the constraint validator
     */
    @Override
    public void initialize(Map<String, String> arguments) {
        this.requiredStatus = Objects.requireNonNull(arguments.get("required"));
    }

    @Override
    public final boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        if (!isActive(type, value)) {
            return true;
        }

        boolean valid = true;

        context.disableDefaultConstraintViolation();

        for (APIField field : type.getFieldsWithDependency(requiredFieldDependency)) {
            if (requiredStatus.equals(field.getDependencyRequired())) {

                if (field.getApiType().getFieldType() == FieldType.STRING
                        && field.getTranslationType() != TranslationSettings.TranslationType.NONE) {
                    throw new RuntimeException("Translatable fields are not supported yet.");
                }

                if (isEmptyValue(field.getFieldAccessor().get(value))) {
                    context.buildConstraintViolation(ValidationErrors.FIELD_REQUIRED)
                            .addPropertyNode(field.getFieldName())
                            .addConstraintViolation();

                    valid = false;
                }
            }
        }

        return valid;
    }

    /**
     * Returns true when the validator should be active.
     *
     * @param type type of the value
     * @param value value being validated
     * @return whenever the validator is active or not
     */
    public abstract boolean isActive(APIField type, Object value);

    private boolean isEmptyValue(Object value) {
        return value == null
                || (value instanceof Collection && ((Collection) value).isEmpty())
                || (value instanceof String && ((String) value).isEmpty());
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return null;
    }
}
