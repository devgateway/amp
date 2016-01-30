package org.dgfoundation.amp.nireports.runtime;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * a (probably caching) builder of {@link IdsAcceptor} 
 * @author Dolghier Constantin
 *
 */
public interface IdsAcceptorsBuilder {
	public IdsAcceptor buildAcceptor(NiDimensionUsage dimUsage, List<NiDimension.Coordinate> coos);
	
	public default IdsAcceptor buildAcceptor(NiDimensionUsage dimUsage, NiDimension.Coordinate coo) {
		return buildAcceptor(dimUsage, Arrays.asList(coo));
	}
}
