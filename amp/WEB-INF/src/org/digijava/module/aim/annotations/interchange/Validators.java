package org.digijava.module.aim.annotations.interchange;

public @interface Validators {
    
    String unique() default "";
    
    String percentage() default "";
    
    String treeCollection() default "";

}
