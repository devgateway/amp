package org.digijava.kernel.ampapi.endpoints.indicator.manager.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @author Timothy Mugo
 */
@Documented
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProgramSchemeIdValidator.class)
public @interface ValidProgramSchemeId {
    String message() default "shouldn't contain invalid or deleted program ids";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
