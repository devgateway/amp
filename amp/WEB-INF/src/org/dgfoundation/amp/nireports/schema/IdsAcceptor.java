package org.dgfoundation.amp.nireports.schema;

import mondrian.olap.MondrianDef.DimensionUsage;

/**
 * a dimensionalities resolver which decides whether a (level, id) coo is on the same straight line as an another (level, id) coo. At the point of this function being called, they are both part of the same {@link DimensionUsage}
 * @author Dolghier Constantin
 *
 */
public interface IdsAcceptor {
	public boolean isAcceptable(NiDimension.Coordinate cellCoos);
}
