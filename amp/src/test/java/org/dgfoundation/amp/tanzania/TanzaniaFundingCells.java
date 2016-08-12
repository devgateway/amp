package org.dgfoundation.amp.tanzania;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dgfoundation.amp.testmodels.nicolumns.AbstractFundingColumn;

import org.dgfoundation.amp.nireports.CategAmountCell;


public class TanzaniaFundingCells extends AbstractFundingColumn {

	public TanzaniaFundingCells(Map<String, Long> activityNames) {
		super(activityNames, TanzaniaReportsTestSchema.catsDimension, TanzaniaReportsTestSchema.DONOR_DIM_USG, new TanzaniaHardcodedFundingNames());
	}
	
	@Override
	protected List<CategAmountCell> populateCells() {
		/*			amount                         , activity title                          , year, month        , pledge                        , transaction_type              , agreement                     , recipient_org                 , recipient_role                , source_role                   , adjustment_type               , donor_org                     , funding_status                , mode_of_payment               , terms_assist                  , financing_instrument          */
return Arrays.asList(
			cell("121000.000000"     , "f5d0a35b"                                                  , 2015, "April"      , null                          , null                          , null                          , null                          , null                          , "DN"                          , "Actual"                      , "ACTIONAID  LBG"              , null                          , null                          , "Grant"                       , "Sector Budget Support (SBS)" , "2016-04-12"),
			cell("1580000.000000"    , "f5d0a35b"                                                  , 2015, "April"      , null                          , "commitment"                  , null                          , null                          , null                          , "DN"                          , "Actual"                      , "ACTIONAID  LBG"              , null                          , null                          , "Grant"                       , "Sector Budget Support (SBS)" , "2016-04-12")
);
	}

}
