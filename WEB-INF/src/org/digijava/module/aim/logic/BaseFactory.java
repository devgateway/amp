package org.digijava.module.aim.logic;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public abstract class BaseFactory implements LogicFactory {
	protected AmountCalculator commitmentCalculator;
	protected AmountCalculator undisbursmentCalculator;
	protected DonorFundingCalculator donorFundingCalculator;
	
	public AmountCalculator getCommitmentCalculator() {
		return commitmentCalculator;
	}

	public AmountCalculator getUnDisbursmentCalculator() {
		return undisbursmentCalculator;
	}

	public DonorFundingCalculator getTotalDonorFundingCalculator() {
		return donorFundingCalculator;
	}

}
