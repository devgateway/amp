package org.dgfoundation.amp.nireports.schema;

import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;

import java.util.Map;
import java.util.Set;

/**
 * an {@link IdsAcceptor} which does equality testing on the id (most useful for degenerate dimensions)
 * @author Dolghier Constantin
 *
 */
public class IdentityIdsAcceptor implements IdsAcceptor {
    
    final Map<Integer, Set<Long>> splitterCells;
    
    public IdentityIdsAcceptor(Map<Integer, Set<Long>> splitterCells) {
        this.splitterCells = splitterCells;
    }
    
    @Override
    public boolean isAcceptable(Coordinate cellCoos) {
        return splitterCells.containsKey(cellCoos.level) && splitterCells.get(cellCoos.level).contains(cellCoos.id);
    }
    
    @Override
    public String toString() {
        return String.format("(cells = %s)", this.splitterCells);
    }

}
