package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InterchangeableDiscriminator {
    /** child field name that will act as a discriminator 
     * 
     * if this field is empty, means discrimination is done at the level of the current field
     * and it's a simple type (String, Long/Integer, Float/Double, Boolean)
     * 
     * */
    String discriminatorField() default "";
    
//    /** 
//     * indicates that this field does not have descendants, which means: 
//     * 1) discriminatorField is obsolete (recommended to be put to be the name of the field itself)
//     * 2) it's a simple type (String, Long/Integer, Float/Double, Boolean)
//     */
//    boolean finalField() default false;
    
    /** list of settings based on discriminator field options. The option value must be configured at discriminatorOption */
    Interchangeable[] settings() default {};

    /** we can also have a method, like Discriminators.sector that will fill in the settings structure at runtime*/
    String method() default "";
    /**
     * class for obtaining possible values for a field that, for some reason, isn't directly mappable to an entity
     * and a custom way of getting said values has to be performed
     */
    String discriminatorClass() default "";
}
