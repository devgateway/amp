package org.dgfoundation.amp.nireports.output.nicells;

import org.dgfoundation.amp.nireports.runtime.CellColumn;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.dgfoundation.amp.algo.AmpCollections.orderedListWrapper;

/**
 * a {@link NiOutCell} holding a set of dates
 * @author Dolghier Constantin
 *
 */
public class NiDateCell extends NiOutCell {
    /**
     * the main entityId. In case the cell holds a multitude of them, an arbitrary one of them will be stored. Values <= 0 mean "no entity" 
     */
    public final long entityId; 
    
    /**
     * never null for date cells
     */
    public final Map<Long, LocalDate> entitiesIdsValues;
    
    public final List<LocalDate> values;
    
    @SuppressWarnings("rawtypes")
    public final Comparable comparableToken;
    
    public NiDateCell(List<LocalDate> values, long entityId, Map<Long, LocalDate> entitiesIdsValues) {
        this.entityId = entityId;
        this.entitiesIdsValues = entitiesIdsValues;
        this.values = values;
        this.comparableToken = orderedListWrapper(values);
    }
        
    @Override
    public int compareTo(Object o) {
        NiDateCell ntc = (NiDateCell) o;
        return this.comparableToken.compareTo(ntc.comparableToken);
    }

    @Override
    public String getDisplayedValue() {
        return values.isEmpty() ? "" : values.toString();
    }

    @Override
    public <K> K accept(CellVisitor<K> visitor, CellColumn niCellColumn) {
        return visitor.visit(this, niCellColumn);
    }

}
