package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import javax.validation.ConstraintViolation;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;

/**
 * @author Nadejda Mandrescu
 */
public class MatchExistingCreatorViolationBuilder  implements ConstraintViolationBuilder {

    @Override
    public JsonConstraintViolation build(ConstraintViolation v) {
        String fieldPath = FieldMap.underscorify(ActivityFieldsConstants.CREATED_BY);
        return new JsonConstraintViolation(fieldPath, ActivityErrors.FIELD_INVALID_VALUE);
    }

}
