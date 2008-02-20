package org.digijava.module.aim.logic;

import java.util.Collection;

import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.YearlyInfo;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public interface DonorFundingCalculator {
	double getTotalDonorFunding(Collection<YearlyInfo> c, FilterParams fp);
}
