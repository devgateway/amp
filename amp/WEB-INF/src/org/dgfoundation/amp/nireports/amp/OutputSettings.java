/**
 * 
 */
package org.dgfoundation.amp.nireports.amp;

import java.util.HashSet;
import java.util.Set;


/**
 * Stores custom output settings and information
 * 
 * @author Nadejda Mandrescu
 */
public class OutputSettings {
	
	/** columns for which to provide ids/values map (expensive operation) */
	public final HashSet<String> idsValuesColumns;
	
	public OutputSettings(Set<String> idsValuesColumns) {
		this.idsValuesColumns = idsValuesColumns == null ? new HashSet<>() : new HashSet<>(idsValuesColumns);
	}
}
