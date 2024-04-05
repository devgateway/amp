package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import org.apache.commons.lang3.NotImplementedException;

import javax.validation.ConstraintViolation;
import java.util.function.Function;

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
