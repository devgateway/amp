package org.digijava.module.aim.dbentity;

/**
 * Connection between Activity and Sector.
 * This class initially was added to add percentage for Bolivia. AMP-2250
 * @author Irakli Kobiashvili
 *
 */
public class AmpActivityLocation {
	private Long id;
	private AmpActivity activity;
	private AmpLocation location;
	private Float locationPercentage;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AmpActivity getActivity() {
		return activity;
	}
	public void setActivity(AmpActivity activity) {
		this.activity = activity;
	}
	public AmpLocation getLocation() {
		return location;
	}
	public void setLocation(AmpLocation location) {
		this.location = location;
	}
	public Float getLocationPercentage() {
		return locationPercentage;
	}
	public void setLocationPercentage(Float locationPercentage) {
		this.locationPercentage = locationPercentage;
	}

}
