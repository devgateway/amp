package org.dgfoundation.amp.testmodels.nicolumns;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class ImplementingAgencyGroupsCells extends HardcodedCells<PercentageTextCell>{

	public ImplementingAgencyGroupsCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
		cell("activity with contracting agency", "Default Group", 17, 1.000000),
		cell("activity with directed MTEFs", "Default Group", 17, 0.330000),
		cell("activity with directed MTEFs", "International", 20, 0.670000),
		cell("date-filters-activity", "Default Group", 17, 1.000000),
		cell("Eth Water", "National", 21, 1.000000),
		cell("Pure MTEF Project", "National", 21, 1.000000),
		cell("Test MTEF directed", "American", 19, 1.000000)
);
	}

}
