package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.List;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.translation.util.ContentTranslationUtil;

/**
 * The Amp Aid Effectiveness Indicator domain class
 */
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "AMP_AID_EFFECTIVENESS_INDICATOR")
@TranslatableClass (displayName = "Aid Effectiveness Indicator")
public class AmpAidEffectivenessIndicator implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_aid_effectiveness_indicator_seq_generator")
    @SequenceGenerator(name = "amp_aid_effectiveness_indicator_seq_generator", sequenceName = "AMP_AID_EFFECTIVENESS_INDICATOR_seq", allocationSize = 1)
    @Column(name = "amp_indicator_id")
    private Long ampIndicatorId;

    @Column(name = "amp_indicator_name")
    @TranslatableField

    private String ampIndicatorName;

    @Column(name = "tooltip_text", columnDefinition = "text")
    @TranslatableField

    private String tooltipText;

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "MANDATORY")
    private boolean mandatory;

    @Column(name = "INDICATOR_TYPE")
    private Integer indicatorType;

    @OneToMany(mappedBy = "indicator", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "idx")
    private List<AmpAidEffectivenessIndicatorOption> options;




    public enum IndicatorType {
        DROPDOWN_LIST, SELECT_LIST  // 0 - dropdown list, 1 - selectbox list
    }

    public Long getAmpIndicatorId() {
        return ampIndicatorId;
    }

    public void setAmpIndicatorId(Long ampIndicatorId) {
        this.ampIndicatorId = ampIndicatorId == 0 ? null : ampIndicatorId;
    }

    public String getAmpIndicatorName() {
        return ampIndicatorName;
    }

    public void setAmpIndicatorName(String ampIndicatorName) {
        this.ampIndicatorName = ampIndicatorName;
    }

    public String getTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public int getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(int indicatorType) {
        this.indicatorType = indicatorType;
    }

    public List<AmpAidEffectivenessIndicatorOption> getOptions() {
        return options;
    }

    public void setOptions(List<AmpAidEffectivenessIndicatorOption> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AmpAidEffectivenessIndicator)) return false;

        AmpAidEffectivenessIndicator indicator = (AmpAidEffectivenessIndicator) o;

        if (ampIndicatorId != null ? !ampIndicatorId.equals(indicator.ampIndicatorId) : indicator.ampIndicatorId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ampIndicatorId != null ? ampIndicatorId.hashCode() : 0;
    }
    
    public String getFmName() {
        String fmName = ampIndicatorName;
        
         if(ContentTranslationUtil.multilingualIsEnabled() && ampIndicatorId != null) {
            String baseLanguage = ContentTranslationUtil.getBaseLanguage();
            fmName = ContentTranslationUtil.loadFieldTranslationInLocale(AmpAidEffectivenessIndicator.class.getName(), ampIndicatorId, "ampIndicatorName", baseLanguage);
         }
        
        return fmName;
    }
}
