package org.dgfoundation.amp.nireports.amp.dimensions;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.amp.PercentagesCorrector;
import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

/**
 * 
 * a dimension consisting of (amp_category_class[level=0], amp_category_value[level=1]) 
 * @author Dolghier Constantin
 *
 */
public class ComponentsDimension extends SqlSourcedNiDimension {
	public final static ComponentsDimension instance = new ComponentsDimension("comps");
	
	private ComponentsDimension(String name) {
		super(name, "amp_components", Arrays.asList("type", "amp_component_id"));
	}
	
	public final static int LEVEL_COMPONENT_TYPE = 0;
	public final static int LEVEL_COMPONENT = 1;

	@Override
	protected PercentagesCorrector buildPercentagesCorrector(NiDimensionUsage dimUsg) {
		return null;
	}
}
