package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.util.Identifiable;

public class AmpIndicatorRiskRatings implements Identifiable, Serializable {
    //IATI-check: to be ignored
    
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

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + (this.ampIndRiskRatingsId != null ? this.ampIndRiskRatingsId.hashCode() : 0);
            return hash;
        }

    
        @Override
    public boolean equals (Object o) {
        if(!(o instanceof AmpIndicatorRiskRatings)) return false;
        AmpIndicatorRiskRatings a = (AmpIndicatorRiskRatings) o;
        if ( a == null )
            return false;
        return this.ampIndRiskRatingsId.equals( a.getAmpIndRiskRatingsId() );
    }

}
