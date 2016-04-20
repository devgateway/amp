package org.dgfoundation.amp.testmodels.nicolumns;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class ImplementingAgencyTypeCells extends HardcodedCells<PercentageTextCell>{

	public ImplementingAgencyTypeCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
		cell("activity with contracting agency", "Default", 38, 1.000000),
		cell("activity with directed MTEFs", "Default", 38, 1.000000),
		cell("date-filters-activity", "Default", 38, 1.000000),
		cell("Eth Water", "Default", 38, 1.000000),
		cell("Pure MTEF Project", "Default", 38, 1.000000),
		cell("Test MTEF directed", "Default", 38, 1.000000)
);
	}

}
