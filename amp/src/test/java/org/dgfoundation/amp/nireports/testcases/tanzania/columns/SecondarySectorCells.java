package org.dgfoundation.amp.nireports.testcases.tanzania.columns;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedCells;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class SecondarySectorCells extends HardcodedCells<PercentageTextCell>{

    public SecondarySectorCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim, String key) {
        super(activityNames, entityNames, degenerate(dim, key));
    }
    public SecondarySectorCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<PercentageTextCell> populateCells() {
        return  Arrays.asList(
            cell("10c2da45", "HUMANITARIAN (INCL REFUGEES)", 251, 1.000000),
            cell("18f2f9ea", "ECONOMIC MANAGEMENT", 242, 1.000000),
            cell("2261df82", "EDUCATION", 243, 1.000000),
            cell("286fe2cd", "INFRASTRUCTURE", 260, 1.000000),
            cell("2a208933", "EDUCATION", 243, 1.000000),
            cell("33bfabfb", "EDUCATION", 243, 1.000000),
            cell("36586368", "HUMANITARIAN (INCL REFUGEES)", 251, 1.000000),
            cell("384c9de5", "INNOVATIONS & TECHNOLOGY", 269, 1.000000),
            cell("44a1b3c5", "ECONOMIC MANAGEMENT", 242, 1.000000),
            cell("4d4124c2", "HIV/AIDS", 249, 1.000000),
            cell("564bba7a", "ECONOMIC MANAGEMENT", 242, 1.000000),
            cell("5c8cf187", "ECONOMIC MANAGEMENT", 242, 1.000000),
            cell("97a17073", "ECONOMIC MANAGEMENT", 242, 1.000000),
            cell("9bb58889", "HUMAN RESOURCE DEVELOPMENT", 250, 1.000000),
            cell("a85be77c", "ECONOMIC MANAGEMENT", 242, 1.000000),
            cell("bf1feecf", "ECONOMIC MANAGEMENT", 242, 1.000000),
            cell("c019f494", "EDUCATION", 243, 1.000000),
            cell("cbc1f3f5", "EDUCATION", 243, 1.000000),
            cell("e843b074", "EDUCATION", 243, 1.000000),
            cell("ea4b5435", "EDUCATION", 243, 1.000000),
            cell("eef0156f", "INFRASTRUCTURE", 260, 1.000000),
            cell("f594fda7", "HUMAN RESOURCE DEVELOPMENT", 250, 1.000000));
    }

}
