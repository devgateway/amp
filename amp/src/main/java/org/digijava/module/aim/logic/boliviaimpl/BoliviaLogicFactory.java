package org.digijava.module.aim.logic.boliviaimpl;

import org.digijava.module.aim.logic.BaseFactory;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class BoliviaLogicFactory extends BaseFactory {  
    {
        commitmentCalculator = new BoliviaCommitmentCalculator();
        undisbursmentCalculator = new BoliviaUnDisbursmentCalculator();
        donorFundingCalculator = new BoliviaDonorFundingCalculator();
        ampARFilterHelper = new BoliviaAmpARFilterHelper();
    }

}
