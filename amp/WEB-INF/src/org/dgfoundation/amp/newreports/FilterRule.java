/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.List;

import org.dgfoundation.amp.reports.FilterType;

/**
 * @author Nadejda Mandrescu
 */
public class FilterRule {
	public final FilterType filterType;
	public final String value;
	public final String min; 
	public final String max;
	public final List<String> values;
	public final boolean minInclusive;  
	public final boolean maxInclusive;
	public final boolean valuesInclusive;
	
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
