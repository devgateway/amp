package org.dgfoundation.amp.nireports.testcases.tanzania.dimensions;

import org.dgfoundation.amp.nireports.testcases.HNDNode;
import org.dgfoundation.amp.nireports.testcases.HardcodedNiDimension;

import java.util.Arrays;
import java.util.List;

import static org.dgfoundation.amp.nireports.testcases.HNDNode.element;

public class ProgramsTestDimension extends HardcodedNiDimension {

    public ProgramsTestDimension(String name, int depth) {
        super(name, depth);
    }

    public final static ProgramsTestDimension instance = new ProgramsTestDimension("progs", 4);

    @Override
    protected List<HNDNode> buildHardcodedElements() {
        return Arrays.asList(
        element(1, "National Plan", 
            element(8, "MKUKUTA", 
                element(10, "Cluster 1: Growth and reduction of income  poverty ( MKUKUTA)", 
                    element(16, "Goal 1: Ensuring sound economic management" ) ), 
                element(11, "Cluster 2: Improved quality of life and social well-being ( MKUKUTA)" ), 
                element(12, "Cluster 3: Governance and accountability ( MKUKUTA)" ), 
                element(17, "Cluster 4: Macro, Resource Allocation and Public Financial Management ( MKUKUTA)" ) ), 
            element(9, "MKUZA", 
                element(13, "Cluster 1: Growth and reduction of income poverty ( MKUZA)" ), 
                element(14, "Cluster 2: Improved quality of life and social well-being ( MKUZA)" ), 
                element(15, "Cluster 3: Governance and accountability ( MKUZA)" ) ) ), 
        element(18, "MarinaProgram", 
            element(19, "SubProgram1" ) ));
    }
}

