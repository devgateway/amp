/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.List;

/**
 * @author Nadejda Mandrescu
 */
public class FilterRule {
	public enum FilterType {
		/** A range from 'min' to 'max' value */
		RANGE,
		SINGLE_VALUE,
		VALUES
	};
	public final FilterType filterType;
	public final String value;
	public final String min; 
	public final String max;
	public final List<String> values;
	public final boolean minInclusive;  
	public final boolean maxInclusive;
	public final boolean valuesInclusive;
	
	/**
	 * Range filter rule to apply over some data
	 * @param min - minimum value of the range, or null if no minimum limit
	 * @param max - maximum value of the range, or null if no maximum limit
	 * @param minInclusive - if true and 'min' is specified, then 'min' is an inclusive limit of the range  
	 * @param maxInclusive - if true and 'max' is specified, then 'max' is an inclusive limit of the range 
	 */
	public FilterRule(String min, String max, boolean minInclusive, boolean maxInclusive) {
		this(FilterType.RANGE, null, min, max, minInclusive, maxInclusive, null, false);
	}

	public FilterRule(List<String> values, boolean valuesInclusive) {
		this(FilterType.VALUES, null, null, null, false, false, values, valuesInclusive);
	}
	
	public FilterRule(String value) {
		this(FilterType.SINGLE_VALUE, value, null, null, true, true, null, false);
	}

	private FilterRule(FilterType filterType, String value, String min, String max, boolean minInclusive, boolean maxInclusive, 
			List<String> values, boolean valuesInclusive) {
		this.filterType = filterType;
		this.value = value;
		this.min = min;
		this.max = max;
		this.minInclusive = minInclusive;
		this.maxInclusive = maxInclusive;
		this.values = values;
		this.valuesInclusive = valuesInclusive;
	}
}
