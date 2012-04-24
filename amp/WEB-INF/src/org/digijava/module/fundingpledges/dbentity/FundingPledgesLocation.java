package org.digijava.module.fundingpledges.dbentity;

import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;

public class FundingPledgesLocation {

	private Long id;
	private FundingPledges pledgeid;
	private AmpCategoryValueLocations location;
	private Float locationpercentage;
	
	public Long getId() {
		return id;
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
	
	public Float getLocationpercentage() {
		return locationpercentage;
	}
	public AmpCategoryValueLocations getLocation() {
		return location;
	}
	public void setLocation(AmpCategoryValueLocations location) {
		this.location = location;
	}
	public void setLocationpercentage(Float locationpercentage) {
		this.locationpercentage = locationpercentage;
	}
	
}
