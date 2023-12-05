package org.digijava.module.aim.validator.contact;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * <p>Ensure that there is only one primary contact in activity contact type collection</p>
 *
 * @author Viorel Chihai
 */
@Documented
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PrimaryContactValidator.class)
public @interface PrimaryContact {

    String message() default "{org.digijava.module.aim.validator.PrimaryContactConstraint.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
