/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Processes and stores filters
 * Note: for now in the context of v1 filters implementation, only some additional filters
 * 
 * @author Nadejda Mandrescu
 */
public class FiltersProcessor {
    
    private Optional<AmpReportFilters> filters;
    private JsonBean filtersConfig;
    
    public FiltersProcessor(JsonBean filtersConfig, AmpReportFilters filters) {
        this.filtersConfig = filtersConfig;
        this.filters = Optional.ofNullable(filters);
    }
    
    public AmpReportFilters getFilters() {
        readComputedYear();
        
        return filters.orElse(null);
    }
    
    private void readComputedYear() {
        if (filtersConfig.any().containsKey(FiltersConstants.COMPUTED_YEAR)) {
            String computedYearId = filtersConfig.getString(FiltersConstants.COMPUTED_YEAR);
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
