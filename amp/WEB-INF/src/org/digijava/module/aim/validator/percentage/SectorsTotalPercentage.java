package org.digijava.module.aim.validator.percentage;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * <p>Ensure that percentages sum up to 100 for all activity programs. Validation is done on per program setting basis,
 * i.e. percentages are summed, validated and reported for each program setting separately.</p>
 *
 * <p>Note on usage: This validator does not depend on FM rules. API uses a more strict validation than Activity Form.
 * For API this validator is always enforced while in AF it could be disabled via FM. Adding this validator to
 * Default group can cause validation errors in Hibernate. Make sure this validator is not added to Default group
 * unless AF also enforces this validator unconditionally.</p>
 *
 * @author Octavian Ciubotaru
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SectorsTotalPercentageValidator.class)
public @interface SectorsTotalPercentage {

    String message() default "{org.digijava.module.aim.validator.PercentageConstraint.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
