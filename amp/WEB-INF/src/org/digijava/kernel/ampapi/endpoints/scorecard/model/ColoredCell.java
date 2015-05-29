package org.digijava.kernel.ampapi.endpoints.scorecard.model;

public class ColoredCell {

	public static enum Colors {
		GREEN, RED, GRAY, YELLOW
	};

	private Colors color;
	private Long donorId;
	private Quarter quarter;
	private Integer amountOfUpdatedActivites = 0;

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

	public Integer getAmountOfUpdatedActivites() {
		return amountOfUpdatedActivites;
	}

	public void setAmountOfUpdatedActivites(Integer amountOfUpdatedActivites) {
		this.amountOfUpdatedActivites = amountOfUpdatedActivites;
	}

}
