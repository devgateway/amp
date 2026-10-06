package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Whether this field is an ID for the db entity where it takes residence.
 * The ID itself is picked from the getIdentifier() method of the entity,
 * which has to be marked as "implements <Identifiable>".
 *
 * @author Octavian Ciubotaru
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface PossibleValueId {
}
