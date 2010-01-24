package org.dgfoundation.amp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ProperyDescription {

	String description();
	PropertyDescPosition position();
	boolean showOnlyIfValueIsTrue();
	boolean hiddenValue();
	
}
