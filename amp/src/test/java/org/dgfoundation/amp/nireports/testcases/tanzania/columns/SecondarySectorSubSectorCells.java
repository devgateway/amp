package org.dgfoundation.amp.nireports.testcases.tanzania.columns;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedCells;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class SecondarySectorSubSectorCells extends HardcodedCells<PercentageTextCell>{

    public SecondarySectorSubSectorCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim, String key) {
        super(activityNames, entityNames, degenerate(dim, key));
    }
    public SecondarySectorSubSectorCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<PercentageTextCell> populateCells() {
        return  Arrays.asList(
            cell("10c2da45", "", -251, 1.000000),
            cell("18f2f9ea", "", -242, 1.000000),
            cell("2261df82", "", -243, 1.000000),
            cell("286fe2cd", "", -260, 1.000000),
            cell("2a208933", "", -243, 1.000000),
            cell("33bfabfb", "", -243, 1.000000),
            cell("36586368", "", -251, 1.000000),
            cell("384c9de5", "", -269, 1.000000),
            cell("44a1b3c5", "", -242, 1.000000),
            cell("4d4124c2", "", -249, 1.000000),
            cell("564bba7a", "", -242, 1.000000),
            cell("5c8cf187", "", -242, 1.000000),
            cell("97a17073", "", -242, 1.000000),
            cell("9bb58889", "", -250, 1.000000),
            cell("a85be77c", "", -242, 1.000000),
            cell("bf1feecf", "", -242, 1.000000),
            cell("c019f494", "", -243, 1.000000),
            cell("cbc1f3f5", "", -243, 1.000000),
            cell("e843b074", "", -243, 1.000000),
            cell("ea4b5435", "", -243, 1.000000),
            cell("eef0156f", "", -260, 1.000000),
            cell("f594fda7", "", -250, 1.000000));
    }

}
