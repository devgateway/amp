package org.dgfoundation.amp.nireports.testcases;


import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedCells;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedColumn;

import java.util.Map;

/**
 * funding fetcher used in HardcodedReportsTestSchema
 * @author acartaleanu
 *
 */
public class TestFundingFetcher extends HardcodedColumn<CategAmountCell>{
    
    public TestFundingFetcher(Map<String, Long> activityNames, HardcodedCells<CategAmountCell> funding ) {
        super("funding", funding, null, TrivialMeasureBehaviour.getInstance());
    }

//  public TestFundingFetcher(Map<String, Long> activityNames ) {
//      this(activityNames, new FundingCells(activityNames));
//  }
    
}
