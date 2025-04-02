package org.dgfoundation.amp.nireports.schema;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Function;

import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.DatedCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;

/**
 * types of supported resolutions for specifying the date of a cell
 * @author Dolghier Constantin
 *
 */
public enum TimeRange implements Comparable<TimeRange> {
    NONE(null, cell -> new ComparableValue<>("null", "null")), 
    YEAR(NiReportsEngine.PSEUDOCOLUMN_YEAR, cell -> cell.getTranslatedDate().year), 
    QUARTER(NiReportsEngine.PSEUDOCOLUMN_QUARTER, cell -> cell.getTranslatedDate().quarter), 
    MONTH(NiReportsEngine.PSEUDOCOLUMN_MONTH, cell -> cell.getTranslatedDate().month);
    
    private String entityType;
    private Function<DatedCell, ComparableValue<String>> categorizer;
    
//  public String getCategory() {
//      return category;
//  }
    
    private TimeRange(String entityType, Function<DatedCell, ComparableValue<String>> categorizer) {
        this.entityType = entityType;
        this.categorizer = categorizer;
    }
    
    public Function<DatedCell, ComparableValue<String>> getDateComponentCategorizer() {
        return categorizer;
    }

    /** returns all values a...b, both ends inclusive */
    public static List<TimeRange> getRange(TimeRange a, TimeRange b) {
        if (a.compareTo(b) > 0)
            return Collections.emptyList();
        List<TimeRange> allValues = Arrays.asList(values());
        return allValues.stream().filter(z -> z.compareTo(a) >= 0 && z.compareTo(b) <= 0).collect(Collectors.toList());
    }
    
    public static TimeRange forCriteria(GroupingCriteria crit) {
        switch(crit) {
            case GROUPING_MONTHLY:
                return MONTH;
                
            case GROUPING_QUARTERLY:
                return QUARTER;
                
            case GROUPING_YEARLY:
                return YEAR;
                
            case GROUPING_TOTALS_ONLY:
                return NONE;
                
            default:
                throw new RuntimeException("unsupported GroupingCriteria: " + crit);
        }
    }
    
    public VSplitStrategy asVSplitStrategy(NiReportsEngine engine) {
        return new VSplitStrategy() {

            @Override
            public String getEntityType() {
                return entityType;
            }
            
            @Override
            public ComparableValue<String> categorize(NiCell cell) {
                return categorizer.apply((DatedCell) cell.getCell());
            }
            
            @Override
            public String toString() {
                return String.format("VSplitStrategy for %s", entityType);
            }

            @Override
            public ComparableValue<String> getTotalSubcolumnName() {
                if (TimeRange.this == QUARTER) {
                    String name = engine.schemaSpecificScratchpad.getTimeRangeSubTotalColumnName(engine.spec);
                    if (name != null) {
                        return new ComparableValue<>(name, Integer.MAX_VALUE);
                    }
                }
                return null;
            }
        };

    }
}
