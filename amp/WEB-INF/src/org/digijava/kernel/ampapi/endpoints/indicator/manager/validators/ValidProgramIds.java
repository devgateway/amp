package org.digijava.kernel.ampapi.endpoints.indicator.manager.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 *
 * @author Viorel Chihai
 */
@Documented
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProgramIdsValidator.class)
public @interface ValidProgramIds {

    String message() default "shouldn't contain invalid program ids";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
