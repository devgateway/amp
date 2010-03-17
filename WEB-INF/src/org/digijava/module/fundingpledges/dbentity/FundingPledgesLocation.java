package org.digijava.module.fundingpledges.dbentity;

import org.digijava.module.aim.dbentity.AmpLocation;

public class FundingPledgesLocation {

	private Long id;
	private FundingPledges pledgeid;
	private AmpLocation location;
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
	public AmpLocation getLocation() {
		return location;
	}
	public void setLocation(AmpLocation location) {
		this.location = location;
	}
	public Float getLocationpercentage() {
		return locationpercentage;
	}
	public void setLocationpercentage(Float locationpercentage) {
		this.locationpercentage = locationpercentage;
	}
	
}
