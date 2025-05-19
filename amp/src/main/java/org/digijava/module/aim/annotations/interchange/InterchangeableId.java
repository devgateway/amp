package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Used to annotate the field that can be used to identify correctly object within a collection. This information
 * is needed in order to match old and new objects during json deserialization.</p>
 *
 * <p>Field type must implement correctly hash code and equals methods.</p>
 *
 * @author Octavian Ciubotaru
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface InterchangeableId {
}
