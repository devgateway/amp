package org.digijava.module.aim.annotations.interchange;

import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.kernel.ampapi.discriminators.StringDiscriminatorConfigurer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InterchangeableDiscriminator {

    /**
     * Discriminator field path. Used during serialization to obtain the discriminator value.
     * <p>The value is converted to string using {@link Object#toString()} and is expected to be non-null.
     */
    String discriminatorField() default "";

    /** list of settings based on discriminator field options. The option value must be configured at discriminatorOption */
    Interchangeable[] settings() default {};

    /**
     * Used during deserialization to restore the discriminator value.
     */
    Class<? extends DiscriminationConfigurer> configurer() default StringDiscriminatorConfigurer.class;
}
