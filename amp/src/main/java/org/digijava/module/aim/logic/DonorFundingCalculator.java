package org.digijava.module.aim.logic;

import java.util.Collection;

import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.util.DecimalWraper;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public interface DonorFundingCalculator {
     
     DecimalWraper getTotalCommtiments(DecimalWraper planned, DecimalWraper actual, DecimalWraper pipeline);
     
     DecimalWraper getunDisbursementsBalance(DecimalWraper a, DecimalWraper b);

}
