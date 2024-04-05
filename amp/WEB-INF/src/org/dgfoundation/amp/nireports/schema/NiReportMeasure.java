package org.dgfoundation.amp.nireports.schema;

import org.dgfoundation.amp.nireports.Cell;

import java.util.Collections;
import java.util.Map;

/**
 * a class which holds complete info for NiReports so that those know how to process them (please see {@link NiReportColumn}). <br />
 * A NiReportMeasure is very much a regular NiReportColumn, but with some engine-supported niceties: <ul>
 * <li>measures are split by temporal columns</li>
 * <li>measures support dependency checking</li>
 * <li>others???</li>
 * </ul>
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportMeasure<K extends Cell> extends NiReportedEntity<K> {
            
    protected NiReportMeasure(String name, Behaviour<?> behaviour, String description) {
        super(name, behaviour, description);
    }
    
    /**
     * returns the map of measures which should be fetched before this measure's {@link #fetch(NiReportContext)} function will be called
     * the value of an item is: <ul>
     * <li><strong>true</strong> if the measure should be run as part of the report upto output (where it would be culled)</li>
     * <li><strong>false</strong> if the measure should just be fetched</li>
     * </ul>
     * @return
     */
    public Map<String, Boolean> getPrecursorMeasures() {
        return Collections.emptyMap();
    }
        
    @Override public String toString() {
        return String.format("measdef: <%s>", name);
    }
}
