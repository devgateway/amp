package org.dgfoundation.amp.nireports.testcases.drc.columns;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedCells;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ModeOfPaymentCells extends HardcodedCells<TextCell>{

    public ModeOfPaymentCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim, String key) {
        super(activityNames, entityNames, degenerate(dim, key));
    }
    public ModeOfPaymentCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<TextCell> populateCells() {
        return  Arrays.asList(
);
    }

}
