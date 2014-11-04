/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries.entities;

import java.util.List;

import org.digijava.kernel.ampapi.mondrian.util.MoConstants;

/**
 * Stores information about filter settings to be applied over attributes. <br>
 * E.g. filter Donor Types to display only 'Bilateral' and 'Multilateral' donor types
 * 
 * @author Nadejda Mandrescu
 */
public class MDXFilter {
	public enum MDXFilterType {
		/** A range from 'min' to 'max' */
		RANGE,
		/** Exactly one value */
		SINGLE_VALUE,
		/** List of values */
		VALUES
	};
	/** filter rule type of {@link FilterType}, always not null */
	public final MDXFilterType filterType;
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
	/** true if key property must be used to filter by */
	public final boolean isKey;
	
	/**
	 * A filter by range
	 * @param startRange - bottom limit of the range (e.g. "2000"), or null if no bottom limit
	 * @param startRangeInclusive - true if {@link #startRange} is allowed value
	 * @param endRange - upper limit of the range (e.g. "2014"), or null if no upper limit
	 * @param endRangeInlcusive - true if {@link #endRange} is allowed value
	 * @param property - true if key property should be used during filtering
	 */
	public MDXFilter(String startRange, boolean startRangeInclusive, String endRange, boolean endRangeInclusive, boolean isKey) {
		this(isKey, startRange, endRange, startRangeInclusive, endRangeInclusive, null, false, null, MDXFilterType.RANGE);
	}
	
	/**
	 * A filter by a list of allowed/not allowed values
	 * @param filteredValues - list of values (e.g. "2013", "2014") to filter by (allowedValues=true) or values to exclude (allowedValues=false)
	 * @param allowedValues - if true, then filteredValues are allowed values; <br>
	 * @param property - true if key property should be used during filtering 
	 */
	public MDXFilter(List<String> filteredValues, boolean allowedValues, boolean isKey) {
		this(isKey, null, null, false, false, filteredValues, allowedValues, null, MDXFilterType.VALUES);
	}
	
	/**
	 * Single value MDX Filter, to be used for filtering by single ID, while {@link MDXLevel} for specific name value is encouraged to be used 
	 * @param singleValue
	 * @param isAllowedValue 
	 * @param property - true if key property should be used during filtering
	 */
	public MDXFilter(String singleValue, boolean isAllowedValue, boolean isKey) {
		this(isKey, null, null, false, false, null, isAllowedValue, singleValue, MDXFilterType.SINGLE_VALUE);
	}
	
	private MDXFilter(boolean isKey, String startRange, String endRange, boolean startRangeInclusive, boolean endRangeInclusive,
			List<String>filteredValues, boolean allowedFilteredValues, String singleValue, MDXFilterType filterType) {
		if (isKey && filteredValues != null && filteredValues.size() == 1) {
			singleValue = filteredValues.get(0).toString();
			filteredValues = null;
			filterType = MDXFilterType.SINGLE_VALUE;
		} else if (endRangeInclusive && startRangeInclusive && startRange != null && startRange.equals(endRange)) {
			singleValue = startRange;
			startRange = null;
			allowedFilteredValues = true;
			filterType = MDXFilterType.SINGLE_VALUE;
		} else if (isKey && startRange != null && endRange == null) {
			endRange = String.valueOf(MoConstants.UNDEFINED_KEY - 1);
		}
		this.filterType = filterType;
		this.startRange = startRange;
		this.startRangeInclusive = startRangeInclusive;
		this.endRange = endRange;
		this.endRangeInclusive = endRangeInclusive;
		this.isKey = isKey;
		this.filteredValues = filteredValues;
		this.singleValue = singleValue;
		this.allowedFilteredValues = allowedFilteredValues;
	}
	
	@Override
	public String toString() {
		switch(filterType) {
		case SINGLE_VALUE: return "singleValue = " + singleValue + ", isKey = " + String.valueOf(isKey); 
		case VALUES: return "filteredValues = " + filteredValues + ", isKey = " + String.valueOf(isKey);
		case RANGE: return "range = " + (startRangeInclusive ? "[" : "(") + startRange + ":" + endRange + (endRangeInclusive ? "]" : ")");
		default: return null;//nether the case
		}
	}
}
