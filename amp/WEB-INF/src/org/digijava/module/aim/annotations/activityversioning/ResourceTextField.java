package org.digijava.module.aim.annotations.activityversioning;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceTextField {

    String fieldTitle();
    
    String translationsField();
    
}
