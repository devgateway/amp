package org.dgfoundation.amp.nireports.schema;

import java.util.Set;

import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;

/**
 * An interface instances of which are created by the schema at early {@link NiReportsEngine} initialisation time 
 * by calling {@link NiReportsSchema#generateScratchpad(NiReportsEngine)}. Aside from some basic functionalities which are requires from all of its implementors,
 * the scratchpad is supposed to be a schema-specific extension of {@link NiReportsEngine} containing data, global state and utility methods 
 * @author Dolghier Constantin
 *
 */
public interface SchemaSpecificScratchpad extends AutoCloseable {
    
    /** 
     * @return the precision with which to run the internal calculations. Should be at least 2 orders of magnitude better than the format used for displaying
     */
    public NiPrecisionSetting getPrecisionSetting();
    
    /**
     * 
     * @return the Calendar to use in a report in case {@link ReportSpecification} does not specify one
     */
    public CachingCalendarConverter buildCalendarConverter();
    
    public CalendarConverter getDefaultCalendar();
    
    public default CalendarConverter buildUnderlyingCalendarConverter(ReportSpecification spec) {
        return spec.getSettings() != null && spec.getSettings().getCalendar() != null ? 
                spec.getSettings().getCalendar() : getDefaultCalendar();
    }
    
    /**
     * returns the ids to use for fetching a given column. By default returns the engine-wide {@link NiReportsEngine#getMainIds()}, 
     * but offers schemas the ability to override it in arbitrary ways
     * @param engine the context of the asking report
     * @param col the column to be fetched
     * @return
     */
    public default Set<Long> getMainIds(NiReportsEngine engine, NiReportColumn<?> col) {
        return engine.getMainIds();
    }

    /**
     * returns the localised name for column used for time range sub totals. if sub totals are not needed then null
     * must be returned.
     * @param reportSpecification report specification
     * @return localized column name
     */
    public String getTimeRangeSubTotalColumnName(ReportSpecification reportSpecification);
}
