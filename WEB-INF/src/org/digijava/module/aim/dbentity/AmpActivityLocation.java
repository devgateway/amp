package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.helper.ActivityItem;
import org.digijava.module.translation.entity.AdvancedTrnItem;

/**
 * Connection between Activity and Sector.
 * This class initially was added to add percentage for Bolivia. AMP-2250
 * @author Irakli Kobiashvili
 *
 */
public class AmpActivityLocation implements Comparable{
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

	@Override
	public int compareTo(Object o) {
		if (!(o instanceof AmpActivityLocation)) {
            throw new IllegalArgumentException("AmpActivityLocation class object can be only compared against the same class instance");
        }
		AmpActivityLocation other = (AmpActivityLocation)o;
        String myName=(getLocation().getName()==null)?"":getLocation().getName();
		String hisName=(other.getLocation().getName()==null)?"":other.getLocation().getName();
		return (myName.trim().toLowerCase().compareTo(hisName.trim().toLowerCase()));
    }
}
