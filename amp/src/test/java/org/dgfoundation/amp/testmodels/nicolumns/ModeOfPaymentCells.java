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
			cell("Activity with both MTEFs and Act.Comms", "Direct payment", 2094),
			cell("activity with directed MTEFs", "Direct payment", 2094),
			cell("activity with directed MTEFs", "Non-Cash", 2096),
			cell("activity with pipeline MTEFs and act. disb", "Direct payment", 2094),
			cell("Activity with primary_tertiary_program", "Cash", 2093),
			cell("activity_with_disaster_response", "Direct payment", 2094),
			cell("crazy funding 1", "Cash", 2093),
			cell("crazy funding 1", "Direct payment", 2094),
			cell("Eth Water", "Direct payment", 2094),
			cell("mtef activity 2", "Direct payment", 2094),
			cell("ptc activity 1", "Reimbursable", 2097),
			cell("ptc activity 2", "No Information", 2095),
			cell("SSC Project 1", "Direct payment", 2094),
			cell("SSC Project 2", "Direct payment", 2094),
			cell("SubNational no percentages", "Direct payment", 2094),
			cell("Test MTEF directed", "Cash", 2093),
			cell("with weird currencies", "Direct payment", 2094)
);
	}

}
