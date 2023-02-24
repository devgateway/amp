package org.dgfoundation.amp.nireports.runtime;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * a (probably caching) builder of {@link IdsAcceptor} 
 * @author Dolghier Constantin
 *
 */
public interface IdsAcceptorsBuilder {
    public IdsAcceptor buildAcceptor(NiDimensionUsage dimUsage, List<NiDimension.Coordinate> coos);
    
    public default IdsAcceptor buildAcceptor(NiDimensionUsage dimUsage, NiDimension.Coordinate coo) {
        IdsAcceptor underlying = buildAcceptor(dimUsage, Arrays.asList(coo)); //undefined are friends forever
        if (coo.id == ColumnReportData.UNALLOCATED_ID)
            return new UnallocatedWrappingAcceptor(underlying);
        else
            return underlying;
    }

    /**
     * a thin wrapper around an {@link IdsAcceptor} which returns true for undefined cells
     * @author Dolghier Constantin
     *
     */
    class UnallocatedWrappingAcceptor implements IdsAcceptor {

        final IdsAcceptor underlying;
        public UnallocatedWrappingAcceptor(IdsAcceptor underlying) {
            this.underlying = underlying;
        }
        
        @Override
        public boolean isAcceptable(Coordinate cellCoos) {
            if (cellCoos.id == ColumnReportData.UNALLOCATED_ID)
                return true;
            return false;
        }
        
        @Override
        public String toString() {
            return String.format("UNDEFINED wrapper for %s", underlying);
        }
    }
}
