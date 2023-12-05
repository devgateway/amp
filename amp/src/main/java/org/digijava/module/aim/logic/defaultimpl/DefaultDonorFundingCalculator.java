package org.digijava.module.aim.logic.defaultimpl;

import org.digijava.module.aim.logic.DonorFundingCalculator;
import org.digijava.module.aim.util.DecimalWraper;

import java.math.BigDecimal;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class DefaultDonorFundingCalculator implements DonorFundingCalculator {

    public DecimalWraper getTotalCommtiments(DecimalWraper planned, DecimalWraper actual, DecimalWraper pipeline) {
        DecimalWraper value = new DecimalWraper();
        value.setValue(new BigDecimal(actual.doubleValue() + pipeline.doubleValue()));
        return value;
    }

    public DecimalWraper getunDisbursementsBalance(DecimalWraper a,
        DecimalWraper b) {
        DecimalWraper ret=new DecimalWraper();
        ret.setValue(a.getValue().subtract(b.getValue()));
        ret.setCalculations("value is "+a.getCalculations()+" - "+b.getCalculations());
        return ret;
    }


}

