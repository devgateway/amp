package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import javax.validation.ConstraintViolation;

/**
 * @author Nadejda Mandrescu
 */
public interface ConstraintViolationBuilder {

    JsonConstraintViolation build(ConstraintViolation v);

}
