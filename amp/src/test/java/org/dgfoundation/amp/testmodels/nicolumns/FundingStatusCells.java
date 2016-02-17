package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;


public class FundingStatusCells extends HardcodedCells<TextCell>{

	public FundingStatusCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim) {
		super(activityNames, entityNames, degenerate(dim, "funding_status"));
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
);
	}

}
