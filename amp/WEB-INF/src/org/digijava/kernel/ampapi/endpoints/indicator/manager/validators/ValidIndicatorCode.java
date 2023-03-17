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
@Constraint(validatedBy = IndicatorCodeValidator.class)
public @interface ValidIndicatorCode {
    String message() default "already exists or is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
