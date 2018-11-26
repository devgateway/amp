package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields annotated with {@code @PossibleValueValue} will be used as value in possible values API. Field type must be
 * {@link String}.
 * <p>In case value must be computed as a formula, use {@link InterchangeableValue}.
 *
 * @author Octavian Ciubotaru
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface PossibleValueValue {
}
