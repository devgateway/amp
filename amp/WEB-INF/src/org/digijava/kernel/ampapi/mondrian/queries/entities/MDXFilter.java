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
	/** single value filter */
	public final String singleValue;
	/** null or property name to filter by */
	public final String property;
	
	/**
	 * A filter by
	 * @param startRange - bottom limit of the range (e.g. "2000"), or null if no bottom limit
	 * @param startRangeInclusive - true if {@link #startRange} is allowed value
	 * @param endRange - upper limit of the range (e.g. "2014"), or null if no upper limit
	 * @param endRangeInlcusive - true if {@link #endRange} is allowed value
	 * @param property - can be null, if specified, then this property value will be used during filtering
	 */
	public MDXFilter(String startRange, boolean startRangeInclusive, String endRange, boolean endRangeInclusive, String property) {
		this(property, startRange, endRange, startRangeInclusive, endRangeInclusive, null, false, null);
	}
	
	/**
	 * A filter by a list of allowed/not allowed values
	 * @param filteredValues - list of values (e.g. "2013", "2014") to filter by (allowedValues=true) or values to exclude (allowedValues=false)
	 * @param allowedValues - if true, then filteredValues are allowed values; <br>
	 * @param property - can be null, if specified, then this property value will be used during filtering 
	 */
	public MDXFilter(List<String> filteredValues, boolean allowedValues, String property) {
		this(property, null, null, false, false, filteredValues, allowedValues, null);
	}
	
	/**
	 * Single value MDX Filter, to be used for filtering by single ID, while {@link MDXLevel} for specific name value is encouraged to be used 
	 * @param singleValue
	 * @param isAllowedValue 
	 * @param property - can be null, if specified, then this property value will be used during filtering
	 */
	public MDXFilter(String singleValue, boolean isAllowedValue, String property) {
		this(property, null, null, false, false, null, isAllowedValue, singleValue);
	}
	
	private MDXFilter(String property, String startRange, String endRange, boolean startRangeInclusive, boolean endRangeInclusive, 
			List<String>filteredValues, boolean allowedFilteredValues, String singleValue) {
		this.startRange = startRange;
		this.endRange = endRange;
		this.startRangeInclusive = startRangeInclusive;
		this.endRangeInclusive = endRangeInclusive;
		this.filteredValues = filteredValues;
		this.property = property;
		if (property != null && filteredValues != null && filteredValues.size() == 1) {
			singleValue = filteredValues.get(0).toString(); 
		} else if (endRangeInclusive && startRangeInclusive && startRange.equals(endRange)) {
			singleValue = startRange; 
			allowedFilteredValues = true;
		}  
		this.singleValue = singleValue;
		this.allowedFilteredValues = allowedFilteredValues;
	}	
}
