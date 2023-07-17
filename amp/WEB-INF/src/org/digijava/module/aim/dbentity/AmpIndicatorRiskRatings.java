package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.util.Identifiable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_INDICATOR_RISK_RATINGS")
public class AmpIndicatorRiskRatings implements Identifiable, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_indicator_risk_ratings_seq_generator")
    @SequenceGenerator(name = "amp_indicator_risk_ratings_seq_generator", sequenceName = "AMP_INDICATOR_RISK_RATINGS_seq", allocationSize = 1)
    @Column(name = "amp_ind_risk_ratings_id")
    private Long ampIndRiskRatingsId;

    @Column(name = "rating_name")
    private String ratingName;

    @Column(name = "rating_value")
    private Integer ratingValue;
    @Transient
    private String translatedRatingName;

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
