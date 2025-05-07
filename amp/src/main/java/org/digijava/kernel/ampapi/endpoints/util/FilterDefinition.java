package org.digijava.kernel.ampapi.endpoints.util;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This class is used for exposing filters using the rest of the properties
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterDefinition {

    /**
     * Whenever to display this filter in UI.
     */
    boolean ui() default true;

    /**
     * Link this filter to a list of columns.
     */
    String[] columns() default EPConstants.NA;

    /**
     * Specified the type of the filter
     */
    FilterFieldType fieldType() default FilterFieldType.TREE;
    
    /**
     * Specified the data type of the filter (text|date)
     */
    FilterDataType dataType() default FilterDataType.TEXT;
    
    /**
     * Specified where this filter can be used.
     */
    FilterComponentType[] componentType() default FilterComponentType.ALL;
    
    /**
     * Specified the report type where the filter is visible (D|P|ALL)
     */
    FilterReportType reportType() default FilterReportType.DONOR;
    
    /**
     * In which tab to display this filter.
     */
    String tab() default EPConstants.TAB_UNASSIGNED;
    
    /**
    * Method name in same class to be called to determine visibility of this method. The signature must be:
    * 
    * public boolean methodName()
    */
    String visibilityCheck() default "";
    
    /**
     * Specifies if multiple values can be selected for a filter
     */
    boolean multiple() default true;
    
}
