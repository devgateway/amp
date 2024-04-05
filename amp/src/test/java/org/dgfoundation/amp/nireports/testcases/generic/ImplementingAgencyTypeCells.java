package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ImplementingAgencyTypeCells extends HardcodedCells<PercentageTextCell>{

    public ImplementingAgencyTypeCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<PercentageTextCell> populateCells() {
        return  Arrays.asList(
            cell("activity with contracting agency", "Default", 38, 1.000000),
            cell("activity with directed MTEFs", "Default", 38, 1.000000),
            cell("activity-weird-funding", "Default", 38, 1.000000),
            cell("date-filters-activity", "Default", 38, 1.000000),
            cell("department/division", "Default", 38, 1.000000),
            cell("Eth Water", "Default", 38, 1.000000),
            cell("Pure MTEF Project", "Default", 38, 1.000000),
            cell("Real SSC Activity 1", "Default", 38, 1.000000),
            cell("Test MTEF directed", "Default", 38, 1.000000));
    }

}
