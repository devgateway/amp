package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InterchangeableDiscriminator {
    /** child field name that will act as a discriminator */
    String discriminatorField();
    /** list of settings based on discriminator field options. The option value must be configured at discriminatorOption */
    Interchangeable[] settings() default {};
    /** list of validators for each sub-category**/
    Validators [] validators() default {};
    /** we can also have a method, like Discriminators.sector that will fill in the settings structure at runtime*/
    String method() default "";
    /**
     * class for obtaining possible values for a field that, for some reason, isn't directly mappable to an entity
     * and a custom way of getting said values has to be performed
     */
    String discriminatorClass() default "";
}