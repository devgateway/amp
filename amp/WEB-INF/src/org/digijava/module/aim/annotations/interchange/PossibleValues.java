package org.digijava.module.aim.annotations.interchange;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Octavian Ciubotaru
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface PossibleValues {

    Class<? extends PossibleValuesProvider> value();
}
