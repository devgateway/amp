package org.dgfoundation.amp.nireports.testcases;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ID acceptors array for a cell which log the calls done to them
 * @author Dolghier Constantin
 *
 */
public class LoggingIdsAcceptors {
    
    final Map<NiDimensionUsage, IdsAcceptor> acceptors;
    final List<String> calls = new ArrayList<>();
    
    public LoggingIdsAcceptors(Cell cell) {
        this.acceptors = cell.getCoordinates().entrySet().stream().collect(Collectors.toMap(z -> z.getKey(), z -> new MyIdsAcceptor(z.getKey(), z.getValue())));
    }
    
    public Map<NiDimensionUsage, IdsAcceptor> getAcceptors() {
        return Collections.unmodifiableMap(acceptors);
    }

    public List<String> getCalls() {
        return Collections.unmodifiableList(calls);
    }
    
    class MyIdsAcceptor implements IdsAcceptor {

        final NiDimensionUsage dimUsg;
        final IdsAcceptor idsAcceptor;
        final Coordinate coo;
        
        public MyIdsAcceptor(NiDimensionUsage dimUsg, Coordinate coo) {
            this.dimUsg = dimUsg;
            this.idsAcceptor = dimUsg.dimension.getDimensionData().getCachingIdsAcceptor(Arrays.asList(coo));
            this.coo = coo;
        }
        
        @Override
        public boolean isAcceptable(Coordinate cellCoos) {
            boolean res = idsAcceptor.isAcceptable(cellCoos);
            calls.add(String.format("%s: %s -> %s", dimUsg, cellCoos, res));
            return res;
        }
        
    }
}
