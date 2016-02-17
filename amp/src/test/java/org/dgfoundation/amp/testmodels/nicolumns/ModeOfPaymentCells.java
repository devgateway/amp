package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;


public class ModeOfPaymentCells extends HardcodedCells<TextCell>{

	public ModeOfPaymentCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim) {
		super(activityNames, entityNames, degenerate(dim, "mode_of_payment"));
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			cell("Activity with both MTEFs and Act.Comms", "Direct payment"),
			cell("Activity with primary_tertiary_program", "Cash"),
			cell("Eth Water", "Direct payment"),
			cell("SSC Project 1", "Direct payment"),
			cell("SSC Project 2", "Direct payment"),
			cell("SubNational no percentages", "Direct payment"),
			cell("Test MTEF directed", "Cash"),
			cell("activity with directed MTEFs", "Direct payment"),
			cell("activity with directed MTEFs", "Non-Cash"),
			cell("activity with pipeline MTEFs and act. disb", "Direct payment"),
			cell("activity_with_disaster_response", "Direct payment"),
			cell("crazy funding 1", "Cash"),
			cell("crazy funding 1", "Direct payment"),
			cell("mtef activity 2", "Direct payment"),
			cell("ptc activity 1", "Reimbursable"),
			cell("ptc activity 2", "No Information"),
			cell("with weird currencies", "Direct payment")
);
	}

}
