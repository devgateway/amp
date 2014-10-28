package org.digijava.kernel.ampapi.endpoints.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

@Retention(RetentionPolicy.RUNTIME)
public @interface  ApiMethod {
	boolean ui();
	String name();
	String column() default EPConstants.NA;
}
