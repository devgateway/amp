package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;


public class FundingStatusCells extends HardcodedCells<TextCell>{

	public FundingStatusCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
);
	}

}
