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

    /**
     * If true, then this field will not be changed in the comparison window for
     * another's version value.
     */
    boolean blockSingleChange() default false;

    /**
     * If true, then this field will have to have a value when merging two
     * versions.
     */
    boolean mandatoryForSingleChange() default false;
}
