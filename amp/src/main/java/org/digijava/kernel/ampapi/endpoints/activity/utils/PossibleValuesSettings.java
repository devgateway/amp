/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.utils;


/**
 * Stores different settings for the possible values
 *  
 * @author Nadejda Mandrescu
 */
public class PossibleValuesSettings {
    public final PossibleValuesFilter filter;
    public final boolean includeReferences;
    
    public PossibleValuesSettings(PossibleValuesFilter filter, boolean includeReferences) {
        this.filter = filter;
        this.includeReferences = includeReferences;
    }
}
