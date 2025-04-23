package org.digijava.module.aim.validator.fundings;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * <p>Ensure that the organisation role used in transaction matches organisation roles defined in activity.orgRoles</p>
 *
 * @author Viorel Chihai
 */
@Documented
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransactionOrgRoleValidator.class)
public @interface TransactionOrgRole {

    String message() default "{org.digijava.module.aim.validator.OrgRoleConstraint.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
