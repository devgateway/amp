package org.digijava.module.aim.annotations.interchange;

import org.digijava.kernel.validation.ConstraintValidator;

import java.lang.annotation.*;
import java.util.Map;

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


    /**
     * Validator is active only when corresponding FM entry is enabled.
     */
    String fmPath() default "";

    /**
     * Enable validator only for specific discriminator branches.
     */
    String[] discriminatorOptions() default {"*"};
}
