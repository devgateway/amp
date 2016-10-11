/**
 * 
 */
package org.digijava.module.aim.annotations.translation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to mark classes that are translatable
 * @author aartimon@developmentgateway.org
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TranslatableClass {
    String displayName();

}
