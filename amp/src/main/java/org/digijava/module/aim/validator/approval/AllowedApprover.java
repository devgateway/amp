package org.digijava.module.aim.validator.approval;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Validates the approver right to approve
 *
 * @author Nadejda Mandrescu
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = AllowedApproverConstraint.class)
public @interface AllowedApprover {

    String message() default "{org.digijava.module.aim.validator.InvalidFieldValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
