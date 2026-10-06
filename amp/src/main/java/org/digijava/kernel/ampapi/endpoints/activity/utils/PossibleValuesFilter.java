/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.utils;

/**
 * Stores possible value filters
 * 
 * @author Nadejda Mandrescu
 */
public class PossibleValuesFilter {
    public final PossibleValuesFilter otherFilter;
    public final String currentFilter;
    public final boolean boothRequired;
    
    public PossibleValuesFilter(String currentFilter) {
        this(currentFilter, null, false);
    }
    
    public PossibleValuesFilter(String currentFilter, PossibleValuesFilter otherFilter, boolean boothRequired) {
        this.boothRequired = boothRequired;
        this.currentFilter = currentFilter;
        this.otherFilter = otherFilter;
    }
    
    @Override
    public String toString() {
        return " (" + currentFilter + ") " + otherFilter == null ? "" : (boothRequired ? "and (" : "or (") + otherFilter + ") ";
    }

}
