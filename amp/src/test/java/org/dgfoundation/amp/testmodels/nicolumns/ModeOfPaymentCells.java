package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;


public class ModeOfPaymentCells extends HardcodedCells<TextCell>{

	public ModeOfPaymentCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity with both MTEFs and Act.Comms", "Direct payment"),
			createCell("Activity with primary_tertiary_program", "Cash"),
			createCell("Eth Water", "Direct payment"),
			createCell("SSC Project 1", "Direct payment"),
			createCell("SSC Project 2", "Direct payment"),
			createCell("SubNational no percentages", "Direct payment"),
			createCell("Test MTEF directed", "Cash"),
			createCell("activity with directed MTEFs", "Direct payment"),
			createCell("activity with directed MTEFs", "Non-Cash"),
			createCell("activity with pipeline MTEFs and act. disb", "Direct payment"),
			createCell("activity_with_disaster_response", "Direct payment"),
			createCell("crazy funding 1", "Cash"),
			createCell("crazy funding 1", "Direct payment"),
			createCell("mtef activity 2", "Direct payment"),
			createCell("ptc activity 1", "Reimbursable"),
			createCell("ptc activity 2", "No Information"),
			createCell("with weird currencies", "Direct payment")
);
	}

}
