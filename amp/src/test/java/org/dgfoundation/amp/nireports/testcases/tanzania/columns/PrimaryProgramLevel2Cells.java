package org.dgfoundation.amp.nireports.testcases.tanzania.columns;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedCells;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class PrimaryProgramLevel2Cells extends HardcodedCells<PercentageTextCell>{

    public PrimaryProgramLevel2Cells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim, String key) {
        super(activityNames, entityNames, degenerate(dim, key));
    }
    public PrimaryProgramLevel2Cells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<PercentageTextCell> populateCells() {
        return  Arrays.asList(
);
    }

}
