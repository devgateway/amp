package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Octavian Ciubotaru
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface InterchangeableValue {
    Class<? extends InterchangeableValueProvider> value();
}
