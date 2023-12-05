package org.dgfoundation.amp.nireports.schema;

import java.util.function.Predicate;


/**
 * a dimensionalities resolver which decides whether a (level, id) coo is on the same straight line as an another (level, id) coo. At the point of this function being called, they are both part of the same {@link DimensionUsage}
 * @author Dolghier Constantin
 *
 */
public interface IdsAcceptor extends Predicate<NiDimension.Coordinate> {
    public boolean isAcceptable(NiDimension.Coordinate cellCoos);
    
    @Override
    public default boolean test(NiDimension.Coordinate cellCoos) {
        return isAcceptable(cellCoos);
    }
}
