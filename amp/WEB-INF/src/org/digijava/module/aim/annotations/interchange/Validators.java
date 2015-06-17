package org.digijava.module.aim.annotations.interchange;

public @interface Validators {
	String unique () default "";
	String maxSize () default "";
	String percentage () default "";
	String minSize () default "";

}
