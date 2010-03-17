package org.digijava.module.aim.ar.impexp.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TransformerAnn {
	String expTransformerFactoryClass();
	String impTransformerFactoryClass();
	String overwrittenPropertyName() default "";
}
