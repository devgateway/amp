package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to specify how compute value field from possible values.
 * TODO rename
 * @author Octavian Ciubotaru
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface InterchangeableValue {
    Class<? extends InterchangeableValueProvider> value();
}
