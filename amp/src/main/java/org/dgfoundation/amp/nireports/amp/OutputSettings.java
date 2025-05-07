/**
 * 
 */
package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.runtime.CellColumn;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Stores custom output settings and information
 * 
 * @author Nadejda Mandrescu
 */
public class OutputSettings {
    
    /** columns for which to provide ids/values map (expensive operation) */
    protected final Set<String> idsValuesColumns;
    
    /**
     * Output settings
     * @param idsValuesColumns columns for which to provide ids/values map
     */
    public OutputSettings(Set<String> idsValuesColumns) {
        this.idsValuesColumns = Collections.unmodifiableSet(idsValuesColumns == null ? new HashSet<>() : new HashSet<>(idsValuesColumns));
    }
    
    public boolean needsIdsValues(CellColumn cc) {
        return cc != null && cc.entity != null && idsValuesColumns.contains(cc.entity.name);
    }
}
