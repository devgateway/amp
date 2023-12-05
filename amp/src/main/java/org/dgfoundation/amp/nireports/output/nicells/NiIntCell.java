package org.dgfoundation.amp.nireports.output.nicells;

import org.dgfoundation.amp.nireports.runtime.CellColumn;

/**
 * a {@link NiOutCell} holding an integer (for example, a computed entity holding counts or ids)
 * @author Dolghier Constantin
 *
 */
public class NiIntCell extends NiOutCell {
    
    public final long value;
    
    /**
     * the main entityId. In case the cell holds a multitude of them, an arbitrary one of them will be stored. Values <= 0 mean "no entity" 
     */
    public final long entityId; 
            
    public NiIntCell(long value, long entityId) {
        this.value = value;
        this.entityId = entityId;
    }
        
    @Override
    public int compareTo(Object o) {
        NiIntCell ntc = (NiIntCell) o;
        return Long.compare(this.value, ntc.value);
    }

    @Override
    public String getDisplayedValue() {
        return Long.toString(value);
    }

    @Override
    public <K> K accept(CellVisitor<K> visitor, CellColumn niCellColumn) {
        return visitor.visit(this, niCellColumn);
    }

}
