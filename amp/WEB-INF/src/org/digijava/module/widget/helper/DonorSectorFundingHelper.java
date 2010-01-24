package org.digijava.module.widget.helper;

import java.math.BigDecimal;
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
	private BigDecimal founding;
	
	public DonorSectorFundingHelper(AmpSector sector){
		this.sector = sector;
        this.founding=BigDecimal.ZERO;
	}

	/**
	 * Adds amount to funding of this sector.
	 * Amount should always be in one currency.
	 * @param amount
	 */
	public void addFunding(BigDecimal amount){
		this.founding=this.founding.add(amount);
	}
	
	public AmpSector getSector() {
		return sector;
	}

	public void setSector(AmpSector sector) {
		this.sector = sector;
	}

	public BigDecimal getFounding() {
		return founding;
	}

	public void setFounding(BigDecimal founding) {
		this.founding = founding;
	}
}
