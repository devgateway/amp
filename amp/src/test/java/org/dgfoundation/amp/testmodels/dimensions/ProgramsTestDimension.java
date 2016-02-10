package org.dgfoundation.amp.testmodels.dimensions;

import static org.dgfoundation.amp.testmodels.dimensions.HNDNode.element;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.testmodels.TestModelConstants;



public class ProgramsTestDimension extends HardcodedNiDimension {

	public final static ProgramsTestDimension instance = new ProgramsTestDimension("Programs test dimension", TestModelConstants.PROGRAMS_DIMENSION_DEPTH);
	
	public ProgramsTestDimension(String name, int depth) {
		super(name, depth);
	}

	@Override
	protected List<HNDNode> buildHardcodedElements() {
		return Arrays.asList(
				element(1, "Program #1", 
						element(2, "Subprogram p1" ), 
						element(3, "Subprogram p1.b" ) ), 
					element(4, "Older Program", 
						element(5, "OP1 name", 
							element(6, "OP11 name", 
								element(7, "OP111 name" ), 
								element(8, "OP112 name" ) ) ), 
						element(9, "OP2 name" ) ));
	}

}
