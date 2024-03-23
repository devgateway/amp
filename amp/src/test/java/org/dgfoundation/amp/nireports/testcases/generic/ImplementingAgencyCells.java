package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ImplementingAgencyCells extends HardcodedCells<PercentageTextCell>{

    public ImplementingAgencyCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<PercentageTextCell> populateCells() {
        return  Arrays.asList(
            cell("activity with contracting agency", "72 Local Public Administrations from RM", 21378, 1.000000),
            cell("activity with directed MTEFs", "Finland", 21698, 0.330000),
            cell("activity with directed MTEFs", "UNDP", 21695, 0.670000),
            cell("activity-weird-funding", "UNDP", 21695, 0.550000),
            cell("activity-weird-funding", "Water Org", 21701, 0.450000),
            cell("date-filters-activity", "Finland", 21698, 1.000000),
            cell("department/division", "Ministry of Finance", 21699, 1.000000),
            cell("Eth Water", "Ministry of Economy", 21700, 0.500000),
            cell("Eth Water", "Ministry of Finance", 21699, 0.500000),
            cell("Pure MTEF Project", "Ministry of Finance", 21699, 1.000000),
            cell("Real SSC Activity 1", "Ministry of Finance", 21699, 0.500000),
            cell("Real SSC Activity 1", "UNDP", 21695, 0.500000),
            cell("Test MTEF directed", "USAID", 21696, 1.000000));
    }

}
