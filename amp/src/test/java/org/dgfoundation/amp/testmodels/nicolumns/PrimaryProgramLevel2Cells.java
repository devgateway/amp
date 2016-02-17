package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;


public class PrimaryProgramLevel2Cells extends HardcodedCells<PercentageTextCell>{

	public PrimaryProgramLevel2Cells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			cell("Activity with both MTEFs and Act.Comms", "", 1.000000),
			cell("Activity with primary_tertiary_program", "", 0.500000),
			cell("Activity with primary_tertiary_program", "", 0.500000),
			cell("activity with directed MTEFs", "", 1.000000),
			cell("activity with funded components", "", 1.000000),
			cell("activity with many MTEFs", "", 1.000000),
			cell("activity with pipeline MTEFs and act. disb", "", 1.000000),
			cell("activity with primary_program", "", 1.000000),
			cell("activity_with_disaster_response", "", 1.000000),
			cell("execution rate activity", "", 1.000000),
			cell("with weird currencies", "", 0.330000),
			cell("with weird currencies", "", 0.670000)
);
	}

}
