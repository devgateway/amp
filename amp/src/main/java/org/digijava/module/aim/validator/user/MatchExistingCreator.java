package org.digijava.module.aim.validator.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Verifies that the the creator is not null and matches past value if it is an existing activity
 *
 * @author Nadejda Mandrescu
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = MatchExistingCreatorConstraint.class)
public @interface MatchExistingCreator {

    String message() default "{org.digijava.module.aim.validator.InvalidFieldValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
