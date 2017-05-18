/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Optional;

import org.dgfoundation.amp.newreports.AmpReportFilters;

/**
 * Processes and stores filters
 * Note: for now in the context of v1 filters implementation, only some additional filters
 * 
 * @author Nadejda Mandrescu
 */
public class FiltersProcessor {
    
    private Optional<AmpReportFilters> filters;
    private Map<String, Object> filterMap;
    
    public FiltersProcessor(Map<String, Object> filterMap, AmpReportFilters filters) {
        this.filterMap = filterMap;
        this.filters = Optional.ofNullable(filters);
    }
    
    public AmpReportFilters getFilters() {
        readComputedYear();
        
        return filters.orElse(null);
    }
    
    private void readComputedYear() {
        if (filterMap != null && filterMap.containsKey(FiltersConstants.COMPUTED_YEAR)) {
            String computedYearId = filterMap.get(FiltersConstants.COMPUTED_YEAR).toString();
            Integer computedYear = null;
            if (FiltersConstants.CURRENT.equals(computedYearId)) {
                computedYear = new GregorianCalendar().get(Calendar.YEAR);
            } else {
                computedYear = Integer.parseInt(computedYearId); 
            }
            getOrCreateFilters().setComputedYear(computedYear);
        }
    }
    
    private AmpReportFilters getOrCreateFilters() {
        return (AmpReportFilters) filters.orElse(new AmpReportFilters());
    }

}
