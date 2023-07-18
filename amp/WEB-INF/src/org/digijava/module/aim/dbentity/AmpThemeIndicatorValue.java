package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.aim.util.FeaturesUtil;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "AMP_THEME_INDICATOR_VALUE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Deprecated
public class AmpThemeIndicatorValue implements Comparable, Serializable 
{

     @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_theme_ind_val_seq_gen")
    @SequenceGenerator(name = "amp_theme_ind_val_seq_gen", sequenceName = "AMP_THEME_IND_ID_VAL_seq", allocationSize = 1)
    @Column(name = "amp_theme_ind_val_id")
    private Long ampThemeIndValId;

    @Column(name = "value_type")
    private Integer valueType;

    @Column(name = "value_amount")
    private Double valueAmount;

    @Column(name = "creation_date")
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_indicator_id")
    private AmpThemeIndicators themeIndicatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indicator_id")
 
    /**
     * @return Returns the ampThemeIndValId.
     */
    public Long getAmpThemeIndValId() {
        return ampThemeIndValId;
    }
    /**
     * @param ampThemeIndValId The ampThemeIndValId to set.
     */
    public void setAmpThemeIndValId(Long ampThemeIndValId) {
        this.ampThemeIndValId = ampThemeIndValId;
    }
    /**
     * @return Returns the creationDate.
     */
    public Date getCreationDate() {
        return creationDate;
    }
    /**
     * @param creationDate The creationDate to set.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    /**
     * @return Returns the themeIndicatorId.
     */
    public AmpThemeIndicators getThemeIndicatorId() {
        return themeIndicatorId;
    }
    /**
     * @param themeIndicatorId The themeIndicatorId to set.
     */
    public void setThemeIndicatorId(AmpThemeIndicators themeIndicatorId) {
        this.themeIndicatorId = themeIndicatorId;
    }
    /**
     * @return Returns the valueAmount.
     */
    public Double getValueAmount() {
        return FeaturesUtil.applyThousandsForVisibility(valueAmount);
    }
    /**
     * @param valueAmount The valueAmount to set.
     */
    public void setValueAmount(Double valueAmount) {
        this.valueAmount = FeaturesUtil.applyThousandsForEntry(valueAmount);
    }
    /**
     * @return Returns the valueType.
     */
    public int getValueType() {
        return valueType;
    }
    /**
     * @param valueType The valueType to set.
     */
    public void setValueType(int valueType) {
        this.valueType = valueType;
    }
    public int compareTo(Object obj) {
        AmpThemeIndicatorValue other = (AmpThemeIndicatorValue) obj;
        return this.getCreationDate().compareTo(other.getCreationDate());
    }
    public AmpIndicator getIndicatorId() {
        return indicatorId;
    }
    public void setIndicatorId(AmpIndicator indicatorId) {
        this.indicatorId = indicatorId;
    }
}






