package org.digijava.module.aim.logic;

import java.util.Collection;

import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.YearlyInfo;
import org.digijava.module.aim.util.DecimalWraper;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public interface DonorFundingCalculator {
	
    DecimalWraper getTotalDonorFunding(Collection<YearlyInfo> c, FilterParams fp);
    
     DecimalWraper getTotalCommtiments(Long activityId, String currCode);
     
     DecimalWraper getTotalCommtiments(DecimalWraper planned, DecimalWraper actual, DecimalWraper pipeline);
     
     DecimalWraper getunDisbursementsBalance(DecimalWraper a, DecimalWraper b);

}
