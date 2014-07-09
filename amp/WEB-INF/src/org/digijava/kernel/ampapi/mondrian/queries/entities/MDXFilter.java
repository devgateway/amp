/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries.entities;

import java.util.List;

/**
 * Stores information about filter settings to be applied over attributes. <br>
 * E.g. filter Donor Types to display only 'Bilateral' and 'Multilateral' donor types
 * 
 * @author Nadejda Mandrescu
 */
public class MDXFilter {
	/** bottom limit of the range, or null if no bottom limit */
	public final String startRange;
	/** upper limit of the range, or null if no upper limit */
	public final String endRange;
	/** true if startRange is allowed value */
	public final boolean startRangeInclusive;
	/** true if endRange is allowed value */
	public final boolean endRangeInclusive;
	/** list of values to filter by (if {@link #allowedFilteredValues}=true) or values to exclude (if {@link #allowedValues}=false) */
	public final List<String> filteredValues;
	/** if true, then {@link #filteredValues} are allowed values; <br>
	 * if false, then {@link #filteredValues} are values to exclude */
	public final boolean allowedFilteredValues;
	
	/**
	 * A filter by
	 * @param startRange - bottom limit of the range (e.g. "2000"), or null if no bottom limit
	 * @param startRangeInclusive - true if {@link #startRange} is allowed value
	 * @param endRange - upper limit of the range (e.g. "2014"), or null if no upper limit
	 * @param endRangeInlcusive - true if {@link #endRange} is allowed value
	 */
	public MDXFilter(String startRange, boolean startRangeInclusive, String endRange, boolean endRangeInclusive) {
		this(startRange, endRange, startRangeInclusive, endRangeInclusive, null, false);
	}
	
	/**
	 * A filter by a list of allowed/not allowed values
	 * @param filteredValues - list of values (e.g. "2013", "2014") to filter by (allowedValues=true) or values to exclude (allowedValues=false)
	 * @param allowedValues - if true, then filteredValues are allowed values; <br>
	 * if false, then filteredValues are values to exclude 
	 */
	public MDXFilter(List<String> filteredValues, boolean allowedValues) {
		this(null, null, false, false, filteredValues, allowedValues);
	}
	
	private MDXFilter(String startRange, String endRange, boolean startRangeInclusive, boolean endRangeInclusive, 
			List<String>filteredValues, boolean allowedFilteredValues) {
		this.startRange = startRange;
		this.endRange = endRange;
		this.startRangeInclusive = startRangeInclusive;
		this.endRangeInclusive = endRangeInclusive;
		this.filteredValues = filteredValues;
		this.allowedFilteredValues = allowedFilteredValues;
	}	
}
