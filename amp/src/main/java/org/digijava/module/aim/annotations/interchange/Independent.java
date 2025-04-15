package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark the boundary of the object tree which is owned by the root object.
 * For example agreements are mapped via {@link Interchangeable} as is they are part of the activity when they are not.
 *
 * This information is used initialize object correctly during deserialization. If a value is specified in json then
 * it will be replaced with a fresh new object. Otherwise existing object reference is kept as is.
 *
 * Works only for fields where type is object.
 *
 * @author Octavian Ciubotaru
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Independent {
}
