package org.digijava.module.aim.logic;


/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public interface LogicFactory {
    AmountCalculator getCommitmentCalculator();
    AmountCalculator getUnDisbursmentCalculator();
    DonorFundingCalculator getTotalDonorFundingCalculator();
    AmpARFilterHelper getAmpARFilterHelper();
}
