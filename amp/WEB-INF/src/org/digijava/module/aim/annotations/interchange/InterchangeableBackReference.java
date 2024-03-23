package org.digijava.module.aim.annotations.interchange;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark fields that references parent (owner) object. On import such fields will be set automatically.
 * Very similar to {@link JsonBackReference}.
 *
 * <p>This is useful for restoring bi-directional associations in Hibernate entities.</p>
 *
 * @author Octavian Ciubotaru
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface InterchangeableBackReference {
}
