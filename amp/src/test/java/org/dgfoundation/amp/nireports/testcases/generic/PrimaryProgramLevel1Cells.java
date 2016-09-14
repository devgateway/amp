package org.dgfoundation.amp.nireports.testcases.generic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;


public class PrimaryProgramLevel1Cells extends HardcodedCells<PercentageTextCell> {

	public PrimaryProgramLevel1Cells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			cell("Activity with both MTEFs and Act.Comms", "Subprogram p1", 2, 1.000000),
			cell("activity with directed MTEFs", "Subprogram p1", 2, 1.000000),
			cell("activity with funded components", "Subprogram p1", 2, 1.000000),
			cell("activity with many MTEFs", "Subprogram p1", 2, 1.000000),
			cell("activity with pipeline MTEFs and act. disb", "Subprogram p1.b", 3, 1.000000),
			cell("activity with primary_program", "", -1, 1.000000),
			cell("Activity with primary_tertiary_program", "Subprogram p1", 2, 0.500000),
			cell("Activity with primary_tertiary_program", "Subprogram p1.b", 3, 0.500000),
			cell("activity_with_disaster_response", "Subprogram p1.b", 3, 1.000000),
			cell("execution rate activity", "Subprogram p1", 2, 1.000000),
			cell("with weird currencies", "Subprogram p1", 2, 0.670000),
			cell("with weird currencies", "Subprogram p1.b", 3, 0.330000));
	}

}
