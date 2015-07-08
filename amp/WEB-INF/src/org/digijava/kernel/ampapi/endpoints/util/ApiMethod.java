package org.digijava.kernel.ampapi.endpoints.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;

@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMethod {
	boolean ui();
	String name() default "";
	String id() ;
	String[] columns() default EPConstants.NA;
	FilterType[] filterType() default FilterType.ALL;
	String visibilityCheck() default "";
	String columnGroup() default EPConstants.NA;
	AuthRule[] authTypes() default AuthRule.NONE;
}
