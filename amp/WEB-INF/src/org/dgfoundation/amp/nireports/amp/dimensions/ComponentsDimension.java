package org.dgfoundation.amp.nireports.amp.dimensions;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

/**
 * 
 * an <i>amp_components</i>-backed dimension consisting of (amp_component_type[level=0], amp_category_id[level=1]) 
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
}
