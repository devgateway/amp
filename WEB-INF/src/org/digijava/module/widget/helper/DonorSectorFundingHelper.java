package org.digijava.module.widget.helper;

import org.digijava.module.aim.dbentity.AmpSector;

/**
 * Helper for sector funding calculations.
 * It allso supports adding amount to already exisiting funding by method {@link #addFunding(double)}
 * So this should represent sector funding in one particular currency.
 * @author Irakli Kobiashvili
 *
 */
public class DonorSectorFundingHelper {
	private AmpSector sector;
	private double founding;
    private double disbFunding;

    public double getDisbFunding() {
        return disbFunding;
    }

    public void setDisbFunding(double disbFunding) {
        this.disbFunding = disbFunding;
    }


	public DonorSectorFundingHelper(AmpSector sector){
		this.sector = sector;
	}

	/**
	 * Adds amount to funding of this sector.
	 * Amount should always be in one currency.
	 * @param amount
	 */
	public void addFunding(double amount){
		this.founding+=amount;
	}

    /**
	 * Adds amount to funding of this sector.
	 * Amount should always be in one currency.
	 * @param amount
	 */
	public void addDisbFunding(double amount){
		this.disbFunding+=amount;
	}
	
	public AmpSector getSector() {
		return sector;
	}

	public void setSector(AmpSector sector) {
		this.sector = sector;
	}

	public Double getFounding() {
		return new Double(founding);
	}

	public void setFounding(Double founding) {
		this.founding = founding.doubleValue();
	}
}
