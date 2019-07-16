/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Filter rule that can be of one of {@link FilterType} type
 * @author Nadejda Mandrescu
 */
@JsonSerialize(using = FilterRuleSerializer.class)
public class FilterRule {
    
    /** the value to use as a filter value when filtering for NULLs */
    public static final String NULL_VALUE = "#null#";
    
    /** the value to use as a filter value when filtering booleans for TRUEs */
    public static final String TRUE_VALUE = "1";
    
    /** the value to use as a filter value when filtering booleans for FALSEs */
    public static final String FALSE_VALUE = "2";
    
    public final static Map<String, Long> HARDCODED_VALUES = Collections.unmodifiableMap(new HashMap<String, Long>() {{
        put(NULL_VALUE, null);
        put("true", Long.parseLong(TRUE_VALUE));
        put("false", Long.parseLong(FALSE_VALUE));
    }});
    
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
    /** single value/id filter or null */
    public final String value;
    /** min of the range or null */
    public final String min; 
    /** max of the range or null */
    public final String max;
    /** list of values/id or null */
    public final List<String> values;
    /** list of names for associated values - used by the front end */
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
    public FilterRule(String min, String max, boolean minInclusive, boolean maxInclusive) {
        this(min, max, minInclusive, maxInclusive, true);
    }
    
    public FilterRule(String min, String max, boolean minInclusive, boolean maxInclusive, boolean positive) {
        this(FilterType.RANGE, null, null, min, max, null, null, minInclusive, maxInclusive, null, null, positive);
    }
    
    public FilterRule(String min, String max, String minName, String maxName, 
            boolean minInclusive, boolean maxInclusive) {
        this(FilterType.RANGE, null, null, min, max, minName, maxName, minInclusive, maxInclusive, null, null, true);
    }

    /**
     * List filter rule, e.g. allowed years [2009, 2013, 2016]
     * @param values - list of values or ids to filter by
     * @param valuesInclusive - true if only these values are allowed, false if these values are not allowed
     * @param isIdList - true if this is a list of ids, false if this is a list of values
     */
    public FilterRule(List<String> values, boolean valuesInclusive) {
        this(null, values, valuesInclusive);
    }

    /**
     * List filter rule, e.g. allowed years [2009, 2013, 2016]
     * @param filterName - Name of the field/column/dimension that the ids correspond to
     * @param values - list of values or ids to filter by
     * @param names - list of names corresponding to ids to filter by
     * @param valuesInclusive - true if only this values are allowed, flase if this values are not allowed
     * @param isIdList - true if this is a list of ids, false if this is a list of values
     */
    public FilterRule(List<String> names, List<String> values, boolean valuesInclusive) {
        this(FilterType.VALUES, null, null, null, null, null, null, false, false, values, names, valuesInclusive);
    }
    
    /**
     * Single value filter
     * @param value - value or id to filter by
     * @param valueToInclude - true if this value must be kept, false if it must be excluded
     * @param isId - true if this is an Id, false if this is a value
     */
    public FilterRule(String value, boolean valueToInclude) {
        this(value, null, valueToInclude);
    }
    
    public FilterRule(String value, String name, boolean valueToInclude) {
        this(FilterType.SINGLE_VALUE, value, name, null, null, null, null, true, true, null, null, valueToInclude);
    }
    
    private FilterRule(FilterType filterType, String value, String name, 
            String min, String max, String minName, String maxName, boolean minInclusive, boolean maxInclusive, 
            List<String> values, List<String> names, boolean valuesInclusive) {
        this.filterType = filterType;
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
    
    public static Long parseStr(String v) {
        if (HARDCODED_VALUES.containsKey(v))
            return HARDCODED_VALUES.get(v);
        return Long.valueOf(v);
    }
    
    /**
     * returns this filter rule as a {@link Predicate}
     * @return
     */
    public Predicate<Long> buildPredicate() {
        switch(filterType) {
            case RANGE : {
                Predicate<Long> res = z -> true;
                    if (min != null) res = res.and(z -> z >= Long.parseLong(min));
                    if (max != null) res = res.and(z -> z <= Long.parseLong(max));
                    return maybeNegated(res, this.valuesInclusive);
            }
            case SINGLE_VALUE : 
                return maybeNegated(z -> z.equals(parseStr(value)), this.valuesInclusive);
            
            case VALUES :
                Set<Long> cor = values.stream().map(FilterRule::parseStr).collect(Collectors.toSet());
                return maybeNegated(cor::contains, this.valuesInclusive);
                
            default:
                throw new RuntimeException("unknown filter type: " + filterType);
        }
    }
    
    /**
     * returns this filter rule as a set
     * @return
     */
    public Set<Long> addIds(Set<Long> set) {
        Set<Long> res = set == null ? new HashSet<>() : set;
        switch(filterType) {
            case RANGE : {
                if (min != null && max != null) {
                    for(long i = Long.parseLong(min); i <= Long.parseLong(max); i++) {
                        res.add(i);
                    }
                    return res;
                }
                throw new RuntimeException(String.format("ranges for column filters should be closed on both ends: %s", this));
            }
            case SINGLE_VALUE :
                res.add(parseStr(value));
                return res;
            
            case VALUES :
                res.addAll(values.stream().map(FilterRule::parseStr).collect(Collectors.toSet()));
                return res;
                
            default:
                throw new RuntimeException("unknown filter type: " + filterType);
        }
    }

    @Override
    public String toString() {
        switch(filterType) {
        case RANGE: return "FilterRule=RANGE, " + (minInclusive ? "[" : "(") + min + " : " + max + (maxInclusive ? "]" : ")");
        case SINGLE_VALUE: return "FilterRule=SINGLE_VALUE, value=" + value;
        case VALUES: return "FilterRule=VALUES, values=" + values;
        default: return "FilterRule=N/A";
        }
    }
    
    @Override public Object clone() {
        try {return super.clone();}catch(Exception e){throw new RuntimeException(e);}
    }
    
    
    /**
     * merges a list of rules into a shorter list of rules which would yield the same result.
     * ATM range merging of range not implemented, although it would be a nice algo-muscle flexing 
     * @param initRules
     * @return
     */
    public static List<FilterRule> mergeRules(List<FilterRule> initRules) {
        if (initRules == null || initRules.isEmpty() || initRules.size() == 1)
            return initRules;
        
        List<FilterRule> res = new ArrayList<>();
        Set<String> mergedValues = new HashSet<>();
        for(FilterRule rule:initRules) {
            switch(rule.filterType) {
            case RANGE:
                res.add(rule);
                break;
                
            case SINGLE_VALUE:
                mergedValues.add(rule.value);
                break;
                
            case VALUES:
                mergedValues.addAll(rule.values);
                break;
            }
        }
        if (!mergedValues.isEmpty())
            res.add(new FilterRule(new ArrayList<String>(mergedValues), true));
        return res;
    }
    
    public static<K> Predicate<K> maybeNegated(Predicate<K> in, boolean positive) {
        if (positive) return in;
        return in.negate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilterRule)) {
            return false;
        }
        FilterRule that = (FilterRule) o;
        return minInclusive == that.minInclusive
                && maxInclusive == that.maxInclusive
                && valuesInclusive == that.valuesInclusive
                && filterType == that.filterType
                && Objects.equals(value, that.value)
                && Objects.equals(min, that.min)
                && Objects.equals(max, that.max)
                && Objects.equals(values, that.values)
                && Objects.equals(valueToName, that.valueToName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterType, value, min, max, values, valueToName, minInclusive, maxInclusive,
                valuesInclusive);
    }
}
