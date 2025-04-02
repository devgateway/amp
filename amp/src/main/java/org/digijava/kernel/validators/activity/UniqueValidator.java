package org.digijava.kernel.validators.activity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;
import org.digijava.module.aim.util.Identifiable;

/**
 * Validates either a list of objects or a list of primitives for uniqueness.
 *
 * In case of a list of objects 'field' attribute is required to determine
 *
 * @author Octavian Ciubotaru
 */
public class UniqueValidator implements ConstraintValidator {

    // only when validating list of objects
    private String subFieldName;

    @Override
    public void initialize(Map<String, String> arguments) {
        subFieldName = arguments.get("field");
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        Collection coll = (Collection) value;

        if (coll == null) {
            return true;
        }

        Set<Object> knownValues = new HashSet<>();

        if (type.getApiType().isAListOfObjects()) {
            APIField subField = type.getField(subFieldName);

            for (Object item : coll) {
                // value is either a primitive or Identifiable!
                Object uniqueValue = subField.getFieldAccessor().get(item);

                if (uniqueValue instanceof Identifiable) {
                    uniqueValue = ((Identifiable) uniqueValue).getIdentifier();
                }

                if (uniqueValue != null) {
                    if (!InterchangeUtils.isSimpleType(uniqueValue.getClass())) {
                        throw new RuntimeException("Unsupported type: " + uniqueValue.getClass());
                    }

                    boolean added = knownValues.add(uniqueValue);
                    if (!added) {
                        return false;
                    }
                }
            }
        } else if (type.getApiType().isAListOfPrimitives()) {
            for (Object item : coll) {
                if (!InterchangeUtils.isSimpleType(item.getClass())) {
                    throw new RuntimeException("Unsupported type: " + item.getClass());
                }
                boolean added = knownValues.add(item);
                if (!added) {
                    return false;
                }
            }
        } else {
            throw new RuntimeException("Unsupported type: " + type.getApiType());
        }

        return true;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ValidationErrors.FIELD_UNQUE_VALUES;
    }
}
