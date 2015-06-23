package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Interchangeable {
	String fieldTitle();
	String fmPath() default "";
	boolean multipleValues() default false;
	boolean exportable() default true;
	boolean importable() default true;
	String required () default "_NONE_";
	boolean recursive() default false;
}
