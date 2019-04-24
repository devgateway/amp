package org.digijava.module.aim.validator.approval;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


/**
 * Verifies that the approval status is in line with the activity draft status.
 *
 * @author Nadejda Mandrescu
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = ApprovalStatusConstraint.class)
public @interface AllowedApprovalStatus {

    String message() default "{org.digijava.module.aim.validator.InvalidFieldValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
