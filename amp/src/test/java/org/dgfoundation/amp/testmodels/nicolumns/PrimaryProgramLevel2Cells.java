package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class PrimaryProgramLevel2Cells extends HardcodedCells<PercentageTextCell>{

	public PrimaryProgramLevel2Cells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity with both MTEFs and Act.Comms", "", 1.000000),
			createCell("Activity with primary_tertiary_program", "", 0.500000),
			createCell("Activity with primary_tertiary_program", "", 0.500000),
			createCell("activity with directed MTEFs", "", 1.000000),
			createCell("activity with funded components", "", 1.000000),
			createCell("activity with many MTEFs", "", 1.000000),
			createCell("activity with pipeline MTEFs and act. disb", "", 1.000000),
			createCell("activity with primary_program", "", 1.000000),
			createCell("activity_with_disaster_response", "", 1.000000),
			createCell("execution rate activity", "", 1.000000),
			createCell("with weird currencies", "", 0.330000),
			createCell("with weird currencies", "", 0.670000)
);
	}

}
