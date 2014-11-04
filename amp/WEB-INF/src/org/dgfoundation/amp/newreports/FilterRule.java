/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Filter rule that can be of one of {@link FilterType} type
 * @author Nadejda Mandrescu
 */
public class FilterRule {
	/** 
	 * Possible types of rules: a range filter (of values/ids), a single value filter (value/id), a list filter (of values/ids) <br>
	 * Options: <br>
	 * {@link #RANGE} <br>
	 * {@link #SINGLE_VALUE} <br>
	 * {@link #VALUES} <br>
	 */
	public enum FilterType {
		/** A range from 'min' to 'max' */
		RANGE,
		/** Exactly one value */
		SINGLE_VALUE,
		/** List of values */
		VALUES
	};
	/** filter rule type of {@link FilterType}, always not null */
	public final FilterType filterType;
	/** true if values configured in the filter are for ids or for actual values */
	public final boolean isIdFilter;
	/** single value/id filter or null */
	public final String value;
	/** min of the range or null */
	public final String min; 
	/** max of the range or null */
	public final String max;
	/** list of values/id or null */
	public final List<String> values;
	/** list of names for associated values */
	public final Map<String, String> valueToName = new HashMap<String, String>();
	/** true if 'min' must be an inclusive limit of the range */
	public final boolean minInclusive;  
	/** true if 'max' must be an inclusive limit of the range */
	public final boolean maxInclusive;
	/** true if list of values/ids is a list of allowed values, false if this is a list of not allowed values/ids */
	public final boolean valuesInclusive;
	
	/**
	 * Range filter rule to apply over some data, e.g. >= 2000 or between 2010 and 2014
	 * @param min - minimum value of the range, or null if no minimum limit
	 * @param max - maximum value of the range, or null if no maximum limit
	 * @param minInclusive - if true and 'min' is specified, then 'min' is an inclusive limit of the range  
	 * @param maxInclusive - if true and 'max' is specified, then 'max' is an inclusive limit of the range
	 * @param isIdRange - whether (min,max) is a range of ids or a range of values
	 */
	public FilterRule(String min, String max, boolean minInclusive, boolean maxInclusive, boolean isIdRange) {
		this(min, max, null, null, minInclusive, maxInclusive, isIdRange);
	}
	
	public FilterRule(String min, String max, String minName, String maxName, 
			boolean minInclusive, boolean maxInclusive, boolean isIdRange) {
		this(FilterType.RANGE, isIdRange, null, null, min, max, minName, maxName, minInclusive, maxInclusive, null, null, false);
	}

	/**
	 * List filter rule, e.g. allowed years [2009, 2013, 2016]
	 * @param values - list of values or ids to filter by
	 * @param valuesInclusive - true if only this values are allowed, flase if this values are not allowed
	 * @param isIdList - true if this is a list of ids, false if this is a list of values
	 */
	public FilterRule(List<String> values, boolean valuesInclusive, boolean isIdList) {
		this(null, values, valuesInclusive, isIdList);
	}

	/**
	 * List filter rule, e.g. allowed years [2009, 2013, 2016]
	 * @param filterName - Name of the field/column/dimension that the ids correspond to
	 * @param values - list of values or ids to filter by
	 * @param names - list of names corresponding to ids to filter by
	 * @param valuesInclusive - true if only this values are allowed, flase if this values are not allowed
	 * @param isIdList - true if this is a list of ids, false if this is a list of values
	 */
	public FilterRule(List<String> names, List<String> values, boolean valuesInclusive, boolean isIdList) {
		this(FilterType.VALUES, isIdList, null, null, null, null, null, null, false, false, values, names, valuesInclusive);
	}
	
	/**
	 * Single value filter
	 * @param value - value or id to filter by
	 * @param valueToInclude - true if this value must be kept, false if it must be excluded
	 * @param isId - true if this is an Id, false if this is a value
	 */
	public FilterRule(String value, boolean valueToInclude, boolean isId) {
		this(value, null, valueToInclude, isId);
	}
	
	public FilterRule(String value, String name, boolean valueToInclude, boolean isId) {
		this(FilterType.SINGLE_VALUE, isId, value, name, null, null, null, null, true, true, null, null, valueToInclude);
	}

	private FilterRule(FilterType filterType, boolean isIdFilter, String value, String name, 
			String min, String max, String minName, String maxName, boolean minInclusive, boolean maxInclusive, 
			List<String> values, List<String> names, boolean valuesInclusive) {
		this.filterType = filterType;
		this.isIdFilter = isIdFilter;
		this.value = value;
		this.min = min;
		this.max = max;
		this.minInclusive = minInclusive;
		this.maxInclusive = maxInclusive;
		this.values = values;
		this.valuesInclusive = valuesInclusive;
		configureValueToName(name, minName, maxName, names);
	}
	
	private void configureValueToName(String name, String minName, String maxName, List<String> names) {
		switch(filterType) {
		case RANGE:
			if (min != null)
				valueToName.put(min, minName);
			if (max != null)
				valueToName.put(max, maxName);
			break;
		case SINGLE_VALUE:
			valueToName.put(value, name);
			break;
		case VALUES:
			if (values!= null && names != null) {
				Iterator<String> valuesIter = values.iterator();
				Iterator<String> namesIter = names.iterator();
				while (valuesIter.hasNext() && namesIter.hasNext()) 
					valueToName.put(valuesIter.next(), namesIter.next());
			}
			break;
		}
	}
	
	@Override
	public String toString() {
		switch(filterType) {
		case RANGE: return "FilterRule=RANGE, " + (minInclusive ? "[" : "(") + min + " : " + max + (maxInclusive ? "]" : ")") + ", isIdFilter=" + isIdFilter;
		case SINGLE_VALUE: return "FilterRule=SINGLE_VALUE, value=" + value + ", isIdFilter=" + isIdFilter;
		case VALUES: return "FilterRule=VALUES, values=" + values + ", isIdFilter=" + isIdFilter;
		default: return "FilterRule=N/A";
		}
		
	}
}
