package org.digijava.module.aim.annotations.activityversioning;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this annotation to mark which fields will be used to compare 2 versions
 * of the same activity.
 * 
 * @author Gabriel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface VersionableFieldSimple {

	/**
	 * Represents the string the user will see in the jsp, please use English
	 * only.
	 */
	String fieldTitle() default "Empty String";
}
