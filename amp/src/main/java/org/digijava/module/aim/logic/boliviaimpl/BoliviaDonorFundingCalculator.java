package org.digijava.module.aim.logic.boliviaimpl;


import org.digijava.module.aim.logic.DonorFundingCalculator;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class BoliviaDonorFundingCalculator implements DonorFundingCalculator {


    public DecimalWraper getTotalCommtiments(DecimalWraper planned, DecimalWraper actual, DecimalWraper pipeline) {
        DecimalWraper total = new DecimalWraper();
        total.setValue(planned.getValue().add(actual.getValue()));
        total.setCalculations("plannned=" + planned.getCalculations() + "+ actual =  " + actual.getCalculations() + " pipeline = " + pipeline.getCalculations());
        // TODO Auto-generated method stub
        return total;
    }

    public DecimalWraper getunDisbursementsBalance(DecimalWraper a, DecimalWraper b) {
        // TODO Auto-generated method stub
        DecimalWraper ret = new DecimalWraper();
        ret.setValue(a.getValue().subtract(b.getValue()));
        ret.setCalculations("value is " + a.getCalculations() + " - " + b.getCalculations());
        return ret;
    }

}
