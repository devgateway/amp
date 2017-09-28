package org.dgfoundation.amp.nireports.testcases.drc;

import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.testcases.generic.AbstractFundingColumn;


public class DRCFundingCells extends AbstractFundingColumn {

    public DRCFundingCells(Map<String, Long> activityNames) {
        super(activityNames, DRCReportsTestSchema.catsDimension, DRCReportsTestSchema.DONOR_DIM_USG, new DRCHardcodedFundingNames());
    }
    
    @Override
    protected List<CategAmountCell> populateCells() {
        return decodeCells(this.getClass().getResourceAsStream("drc-funding.gz"));
    }
    
}
