package org.dgfoundation.amp.nireports.schema;

import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;

/**
 * an {@link IdsAcceptor} which does equality testing on the id (most useful for degenerate dimensions)
 * @author Dolghier Constantin
 *
 */
public class IdentityIdsAcceptor implements IdsAcceptor {

	final long id;
	public IdentityIdsAcceptor(long id) {
		this.id = id;
	}
	
	@Override
	public boolean isAcceptable(Coordinate cellCoos) {
		return cellCoos.id == this.id;
	}
	
	@Override
	public String toString() {
		return String.format("(id = %d)", this.id);
	}

}
