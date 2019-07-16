package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import java.util.function.Function;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Default implementation throws NotImplementedException.
 *
 * @author Octavian Ciubotaru
 */
public class DefaultErrorsMapper implements Function<ConstraintViolation, JsonConstraintViolation> {

    @Override
    public JsonConstraintViolation apply(ConstraintViolation constraintViolation) {
        throw new NotImplementedException(
                "The following Bean Validation violation was not mapped onto json violation: " + constraintViolation);
    }
}
