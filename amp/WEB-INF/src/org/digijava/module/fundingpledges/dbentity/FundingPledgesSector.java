package org.digijava.module.fundingpledges.dbentity;

import org.digijava.module.aim.dbentity.AmpSector;

public class FundingPledgesSector {

	private Long id;
	private FundingPledges pledgeid;
	private AmpSector sector;
	private Float sectorpercentage;
	
	public Long getId() {
		return id;
	}
	public Float getSectorpercentage() {
		return sectorpercentage;
	}
	public void setSectorpercentage(Float sectorpercentage) {
		this.sectorpercentage = sectorpercentage;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public FundingPledges getPledgeid() {
		return pledgeid;
	}
	public void setPledgeid(FundingPledges pledgeid) {
		this.pledgeid = pledgeid;
	}
	
	public AmpSector getSector() {
		return sector;
	}
	public void setSector(AmpSector sector) {
		this.sector = sector;
	}
}
