package org.digijava.module.gis.helper;

import org.digijava.module.aim.dbentity.AmpSector;

public class DonorSectorFundingHelper {
	private AmpSector sector;
	private double founding;
	
	public DonorSectorFundingHelper(AmpSector sector){
		this.sector = sector;
	}

	public void addFunding(double amount){
		this.founding+=amount;
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
