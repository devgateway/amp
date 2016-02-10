package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class PrimaryProgramCells extends HardcodedCells<PercentageTextCell>{

	public PrimaryProgramCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity with both MTEFs and Act.Comms", "Subprogram p1", 1.000000),
			createCell("Activity with primary_tertiary_program", "Subprogram p1", 0.500000),
			createCell("Activity with primary_tertiary_program", "Subprogram p1.b", 0.500000),
			createCell("activity with directed MTEFs", "Subprogram p1", 1.000000),
			createCell("activity with funded components", "Subprogram p1", 1.000000),
			createCell("activity with many MTEFs", "Subprogram p1", 1.000000),
			createCell("activity with pipeline MTEFs and act. disb", "Subprogram p1.b", 1.000000),
			createCell("activity with primary_program", "", 1.000000),
			createCell("activity_with_disaster_response", "Subprogram p1.b", 1.000000),
			createCell("execution rate activity", "Subprogram p1", 1.000000),
			createCell("with weird currencies", "Subprogram p1", 0.670000),
			createCell("with weird currencies", "Subprogram p1.b", 0.330000)
);
	}

}
