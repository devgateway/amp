package org.dgfoundation.amp.nireports.testcases.tanzania;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.testcases.generic.AbstractFundingColumn;

import java.util.List;
import java.util.Map;

/**
 * the Tanzania class reading funding cells from the binary dump residing in file funding.gz
 * @author Dolghier Constantin
 *
 */
public class TanzaniaFundingCells extends AbstractFundingColumn {

    public TanzaniaFundingCells(Map<String, Long> activityNames) {
        super(activityNames, TanzaniaReportsTestSchema.catsDimension, TanzaniaReportsTestSchema.DONOR_DIM_USG, new TanzaniaHardcodedFundingNames());
    }
    
    @Override
    protected List<CategAmountCell> populateCells() {
        return decodeCells(this.getClass().getResourceAsStream("funding.gz"));
    }
    
}
