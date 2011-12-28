package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Identifiable;

public class AmpIndicatorRiskRatings implements Identifiable {
	private Long ampIndRiskRatingsId;
	private String ratingName;
	private String translatedRatingName;
	private int ratingValue;
	
	public Long getAmpIndRiskRatingsId() {
		return ampIndRiskRatingsId;
	}
	public void setAmpIndRiskRatingsId(Long ampIndRiskRatingsId) {
		this.ampIndRiskRatingsId = ampIndRiskRatingsId;
	}
	public String getRatingName() {
		return ratingName;
	}
	public void setRatingName(String ratingName) {
		this.ratingName = ratingName;
	}
	public int getRatingValue() {
		return ratingValue;
	}
	public void setRatingValue(int ratingValue) {
		this.ratingValue = ratingValue;
	}
	public Object getIdentifier() {
		return ampIndRiskRatingsId;
	}

	public String toString() {
		return ratingName;
	}
	public String getTranslatedRatingName() {
		return translatedRatingName;
	}
	public void setTranslatedRatingName(String translatedRatingName) {
		this.translatedRatingName = translatedRatingName;
	}
}
