package org.digijava.kernel.ampapi.endpoints.util;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This class has three responsibilities:
 * <ol>
 * <li>Expose endpoint methods using id & name properties
 * <li>Authorize calls to endpoint methods using authTypes property
 * <li>Expose filters using the rest of the properties
 * </ol>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMethod {

    /**
     * Whenever to display this filter in UI.
     *
     * <p>Note: Used in filters only.</p>
     */
    boolean ui() default false;

    /**
     * Translatable method name.
     */
    String name() default "";

    /**
     * Method identifier, as opposed to translatable name.
     */
    String id();

    /**
     * Link this filter to a list of columns.
     *
     * <p>Note: Used in filters only.</p>
     */
    String[] columns() default EPConstants.NA;

    /**
     * Specified where this filter can be used.
     *
     * <p>Note: Used in filters only.</p>
     */
    FilterType[] filterType() default FilterType.ALL;

    /**
     * In which tab to display this filter.
     *
     * <p>Note: Used in filters only.</p>
     */
    String tab() default EPConstants.TAB_UNASSIGNED;

    /**
     * Method name in same class to be called to determine visibility of this method. The signature must be:
     * public boolean methodName()
     *
     * <p>Note: Used in filters only.</p>
     */
    String visibilityCheck() default "";

    /**
     * Authorization rules that must be applied to this method.
     *
     * <p>Fail-closed default: when left empty, the method still requires an authenticated session
     * (equivalent to {@link AuthRule#AUTHENTICATED}). Use {@link AuthRule#PUBLIC} explicitly to allow
     * anonymous access.</p>
     */
    AuthRule[] authTypes() default {};
}
