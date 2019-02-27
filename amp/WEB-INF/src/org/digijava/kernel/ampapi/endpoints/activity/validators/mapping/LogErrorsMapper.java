package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import java.util.function.Function;

import javax.validation.ConstraintViolation;

import org.apache.log4j.Logger;

/**
 * Does not actually map, only write errors to log.
 *
 * @author Octavian Ciubotaru
 */
public class LogErrorsMapper implements Function<ConstraintViolation, JsonConstraintViolation> {

    private Logger log = Logger.getLogger(LogErrorsMapper.class);

    @Override
    public JsonConstraintViolation apply(ConstraintViolation constraintViolation) {
        log.warn("The following Bean Validation violation was not mapped onto json violation: " + constraintViolation);
        return null;
    }
}
