package org.dgfoundation.amp.testmodels;



import java.util.Map;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;
import org.dgfoundation.amp.testmodels.nicolumns.FundingCells;
import org.dgfoundation.amp.testmodels.nicolumns.HardcodedColumn;

/**
 * funding fetcher used in HardcodedReportsTestSchema
 * @author acartaleanu
 *
 */
public class TestFundingFetcher extends HardcodedColumn<CategAmountCell>{

	public TestFundingFetcher(Map<String, Long> activityNames ) {
		super("funding", new FundingCells(activityNames, activityNames), null, TrivialMeasureBehaviour.getInstance());
	}
	
}
