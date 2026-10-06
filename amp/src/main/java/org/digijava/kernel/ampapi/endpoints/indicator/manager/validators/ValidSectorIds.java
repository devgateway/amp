package org.digijava.kernel.ampapi.endpoints.indicator.manager.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * <p>Ensure that the organisation role used in funding matches organisation roles defined in activity.orgRoles</p>
 *
 * @author Viorel Chihai
 */
@Documented
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SectorIdsValidator.class)
public @interface ValidSectorIds {

    String message() default "shouldn't contain invalid or deleted sector ids";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
