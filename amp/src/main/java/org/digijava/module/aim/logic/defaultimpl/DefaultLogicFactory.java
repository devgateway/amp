package org.digijava.module.aim.logic.defaultimpl;

import org.digijava.module.aim.logic.BaseFactory;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class DefaultLogicFactory extends BaseFactory{
    
    {
        commitmentCalculator = new DefaultCommitmentCalculator();
        undisbursmentCalculator = new DefaultUnDisbursmentCalculator();
        donorFundingCalculator = new DefaultDonorFundingCalculator();
        ampARFilterHelper = new DefaulAmpARFilterHelper();
    }


}
