package org.dgfoundation.amp.testmodels.dimensions;

import static org.dgfoundation.amp.testmodels.dimensions.HNDNode.element;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.testmodels.TestModelConstants;


public class OrganizationsTestDimension extends HardcodedNiDimension {

	public OrganizationsTestDimension(String name, int depth) {
		super(name, depth);
	}

	public final static OrganizationsTestDimension instance = new OrganizationsTestDimension("Organizations dimension", TestModelConstants.ORGS_DIMENSION_DEPTH);

	@Override
	protected List<HNDNode> buildHardcodedElements() {
		return Arrays.asList(
				element(38, "Default", 
						element(17, "Default Group", 
							element(21378, "72 Local Public Administrations from RM" ), 
							element(21698, "Finland" ) ), 
						element(18, "European", 
							element(21694, "Norway" ) ), 
						element(19, "American", 
							element(21696, "USAID" ), 
							element(21701, "Water Org" ), 
							element(21702, "Water Foundation" ) ), 
						element(20, "International", 
							element(21697, "World Bank" ), 
							element(21695, "UNDP" ) ), 
						element(21, "National", 
							element(21699, "Ministry of Finance" ), 
							element(21700, "Ministry of Economy" ) ) ));
	}
}
