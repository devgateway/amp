package org.dgfoundation.amp.newreports;

import java.util.Map;

/**
 * a {@link ReportCell} whose cells are identifiable in the output (e.g. are linked with an entity other than ampActivityId)
 * @author Dolghier Constantin
 *
 */
public abstract class IdentifiedReportCell extends ReportCell {
    
    /**
     * the main entityId. In case the cell holds a multitude of them, an arbitrary one of them will be stored. Values <= 0 mean "no entity" 
     */
    public final long entityId;
    
    /**
     * Map<entityId, displayedValue> for the cells embedded within this cell. Might be null if this option has been disabled during report output stages for this column (maps are expensive)
     */
    public final Map<Long, String> entitiesIdsValues;
    
    protected IdentifiedReportCell(Comparable<?> value, String displayedValue, long entityId, Map<Long, String> entitiesIdsValues) {
        super(value, displayedValue);
        this.entityId = entityId;
        this.entitiesIdsValues = entitiesIdsValues;
    }
}
