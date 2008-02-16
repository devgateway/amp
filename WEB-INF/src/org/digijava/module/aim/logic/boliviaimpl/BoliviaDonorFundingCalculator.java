package org.digijava.module.aim.logic.boliviaimpl;

import java.util.Collection;
import java.util.Iterator;

import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.YearlyInfo;
import org.digijava.module.aim.logic.DonorFundingCalculator;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class BoliviaDonorFundingCalculator implements DonorFundingCalculator {

	public double getTotalDonorFunding(Collection<YearlyInfo> c, FilterParams fp) {
		double total = 0;
		Iterator<YearlyInfo> iter = c.iterator();
		
		while ( iter.hasNext() )	{
			YearlyInfo yf = iter.next();
			
			total += yf.getActualAmount();

			
			if (fp.getTransactionType() != Constants.DISBURSEMENT){
				double temp = yf.getPlannedAmount();
				total += temp;
			}
			
		}		
		return total;
	}

}
