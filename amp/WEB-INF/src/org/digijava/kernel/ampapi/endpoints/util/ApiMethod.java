package org.digijava.kernel.ampapi.endpoints.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

@Retention(RetentionPolicy.RUNTIME)
public @interface  ApiMethod {
	boolean ui();
	String name() default "";
	String id() ;
	String column() default EPConstants.NA;
	FilterType [] filterType() default FilterType.ALL;
}
