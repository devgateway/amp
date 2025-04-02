package org.digijava.module.aim.annotations.activityversioning;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VersionableFieldTextEditor {

    /**
     * Represents the string the user will see in the jsp, please use English
     * only.
     */
    String fieldTitle() default "Empty String";
}
