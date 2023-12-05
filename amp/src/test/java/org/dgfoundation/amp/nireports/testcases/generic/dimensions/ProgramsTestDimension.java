package org.dgfoundation.amp.nireports.testcases.generic.dimensions;

import org.dgfoundation.amp.nireports.testcases.HNDNode;
import org.dgfoundation.amp.nireports.testcases.HardcodedNiDimension;
import org.dgfoundation.amp.nireports.testcases.TestModelConstants;

import java.util.Arrays;
import java.util.List;

import static org.dgfoundation.amp.nireports.testcases.HNDNode.element;



public class ProgramsTestDimension extends HardcodedNiDimension {

    public final static ProgramsTestDimension instance = new ProgramsTestDimension("progs", TestModelConstants.PROGRAMS_DIMENSION_DEPTH);
    
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
