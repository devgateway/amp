package org.digijava.module.aim.logic.boliviaimpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.YearlyInfo;
import org.digijava.module.aim.logic.DonorFundingCalculator;
import org.digijava.module.aim.util.DecimalWraper;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class BoliviaDonorFundingCalculator implements DonorFundingCalculator {

	public DecimalWraper getTotalDonorFunding(Collection<YearlyInfo> c, FilterParams fp) {
		DecimalWraper total=new DecimalWraper();
		Iterator<YearlyInfo> iter = c.iterator();
		while ( iter.hasNext() )	{
			YearlyInfo yf = iter.next();
			
			if (total.getValue()==null){ 
				total.setValue(new BigDecimal(yf.getActualAmount()));
				total.setCalculations(yf.getWrapedActual());
			}
			else{
				total.setValue(new BigDecimal(yf.getActualAmount()).add(total.getValue()));
				total.setCalculations(total.getCalculations() + " + " + yf.getWrapedActual());
			}
			//total += yf.getActualAmount();

			if (fp.getTransactionType() != Constants.DISBURSEMENT){
				DecimalWraper temp=new DecimalWraper();
				temp.setValue(new BigDecimal( yf.getPlannedAmount()));
				total.setValue(total.getValue().add(temp.getValue()));
				total.setCalculations(total.getCalculations() + " + " + temp.getCalculations());
			}
			
		}		
		return total;
	}

}
