package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import org.digijava.kernel.validation.ConstraintValidator;

/**
 * Used to declare a validator on a field or a class.
 *
 * For fields this annotation must be placed in {@link Interchangeable#interValidators()}.
 * For types it is placed directly on the class.
 *
 * @author Octavian Ciubotaru
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(InterchangeableValidators.class)
public @interface InterchangeableValidator {

    /**
     * Constraint validator implementation class.
     */
    Class<? extends ConstraintValidator> value();

    /**
     * Attributes to pass to the constraint validator in {@link ConstraintValidator#initialize(Map)}.
     *
     * <p>Multiple attributes can be specified. Key/value separator is = and attribute separator is &.</p>
     * <p>Example: <code>key1=value1&key2=value2</code></p>
     */
    String attributes() default "";

    /**
     * Validation groups this constraint validator applies to. When not specified validator is applied with
     * default group.
     */
    Class<?>[] groups() default {};
}
