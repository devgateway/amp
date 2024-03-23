package org.dgfoundation.amp.nireports.output.nicells;

import org.dgfoundation.amp.nireports.runtime.CellColumn;

import java.util.Map;

/**
 * a cell which holds a text
 * @author Dolghier Constantin
 *
 */
public class NiTextCell extends NiOutCell {
    public final String text;
    
    /**
     * tthe main entityId. In case the cell holds a multitude of them, an arbitrary one of them will be stored. Values <= 0 mean "no entity" 
     */
    public final long entityId; 
    
    /**
     * Might be null if this option has been disabled during report output stages for this column (maps are expensive)
     */
    public final Map<Long, String> entitiesIdsValues;
    
    public NiTextCell(String text, long entityId, Map<Long, String> entitiesIdsValues) {
        this.text = text;
        this.entityId = entityId;
        this.entitiesIdsValues = entitiesIdsValues;
    }
    
    @Override
    public int compareTo(Object o) {
        NiTextCell ntc = (NiTextCell) o;
        int delta = this.text.compareToIgnoreCase(ntc.text);
        if (delta == 0)
            delta = this.text.compareTo(ntc.text);
        return delta;
    }

    @Override
    public String getDisplayedValue() {
        return text;
    }

    @Override
    public <K> K accept(CellVisitor<K> visitor, CellColumn niCellColumn) {
        return visitor.visit(this, niCellColumn);
    }

    public final static NiTextCell EMPTY = new NiTextCell("", -1, null);
}
