package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import java.util.HashSet;
import java.util.Set;

public class ColoredCell {

	public static enum Colors {
		GREEN, RED, GRAY, YELLOW
	};

	private Colors color;
	private Long donorId;
	private Quarter quarter;
	//updated activities on the current quarter
	private Set <String> updatedActivites = new HashSet <String> ();
	
	//updated activities on the grace period of the next quarter
	private Set <String> updatedActivitiesOnGracePeriod = new HashSet <String> ();;

	public ColoredCell (Colors color) {
		this.color = color;
	}
	
	public ColoredCell () {
		this.color = Colors.RED;
	}
	public Colors getColor() {
		return color;
	}

	public void setColor(Colors color) {
		this.color = color;
	}

	public Long getDonorId() {
		return donorId;
	}

	public void setDonorId(Long donorId) {
		this.donorId = donorId;
	}

	public Quarter getQuarter() {
		return quarter;
	}

	public void setQuarter(Quarter quarter) {
		this.quarter = quarter;
	}

	public Set<String> getUpdatedActivites() {
		return updatedActivites;
	}

	public void setUpdatedActivites(Set<String> updatedActivites) {
		this.updatedActivites = updatedActivites;
	}

	public Set<String> getUpdatedActivitiesOnGracePeriod() {
		return updatedActivitiesOnGracePeriod;
	}

	public void setUpdatedActivitiesOnGracePeriod(Set<String> updatedActivitiesOnGracePeriod) {
		this.updatedActivitiesOnGracePeriod = updatedActivitiesOnGracePeriod;
	}

	

}
