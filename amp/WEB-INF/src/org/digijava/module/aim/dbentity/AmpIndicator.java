package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@TranslatableClass (displayName = "Indicator")
public class AmpIndicator implements Serializable, Identifiable
{
    
    //IATI-check: to be ignored
    private static final long serialVersionUID = 1L;
//  @Interchangeable(fieldTitle="Indicator ID")
    private Long indicatorId;
//  @Interchangeable(fieldTitle="Default Indicator")
    private boolean defaultInd;
//  @Interchangeable(fieldTitle="Name",fmPath="/Activity Form/M&E/Name")
    @TranslatableField
    private String name;
//  @Interchangeable(fieldTitle="Code",fmPath="/Activity Form/M&E/Code")
    private String code;
//  @Interchangeable(fieldTitle="Type")
    private String type;
//  @Interchangeable(fieldTitle="Creation Date",fmPath="/Activity Form/M&E/Creation Date")
    private Date creationDate;
//  @Interchangeable(fieldTitle="Category")
    private int category;
    @TranslatableField
//    @Interchangeable(fieldTitle="Description",fmPath="/Activity Form/M&E/Indicator Description")
    private String description;
//    @Interchangeable(fieldTitle="Sectors", pickIdOnly=true)
    @Validators (unique="/Activity Form/M&E/uniqueSectorsValidator")
    private Set<AmpSector> sectors;
//    @Interchangeable(fieldTitle="Comments")

    private Set<AmpTheme> programs;

    private String comments;
//    @Interchangeable(fieldTitle="Unit")
    private String unit;
    /**
     * Indicator connections with activities.
     * Elements in this set contains activity and values assigned to this activity-indicator connections.
     * Please look carefully in IndicatorConnection.hbm.xml before changing anything, because {@link IndicatorActivity} is subclass of {@link IndicatorConnection}
     * @see IndicatorConnection
     */
    
    private Set<IndicatorActivity> valuesActivity;

    /**
     * Indicator connections with themes.
     * Elements in this set contains theme and values assigned to this theme-indicator connections.
     * Please look carefully in IndicatorConnection.hbm.xml before changing anything, because {@link IndicatorTheme} is subclass of {@link IndicatorConnection}
     * @see IndicatorConnection
     */
    
    private Set<IndicatorTheme> valuesTheme;

    private Set<AmpIndicatorGlobalValue> indicatorValues;

    @Interchangeable(fieldTitle="Indicator Category")
    private AmpCategoryValue indicatorsCategory;

    @Interchangeable(fieldTitle="Risk")
    private AmpIndicatorRiskRatings risk;

    private Date startDate;

    private Date endDate;


    public Long getIndicatorId() {
        return indicatorId;
    }
    public void setIndicatorId(Long indicatorId) {
        this.indicatorId = indicatorId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<AmpSector> getSectors() {
        return sectors;
    }

    public void setSectors(Set<AmpSector> sectors) {
        this.sectors = sectors;
    }

    public Set<AmpTheme> getPrograms() {
        return programs;
    }

    public void setPrograms(Set<AmpTheme> programs) {
        this.programs = programs;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isDefaultInd() {
        return defaultInd;
    }
    public void setDefaultInd(boolean defaultInd) {
        this.defaultInd = defaultInd;
    }

    public AmpCategoryValue getIndicatorsCategory() {
        return indicatorsCategory;
    }
    public void setIndicatorsCategory(AmpCategoryValue indicatorsCategory) {
        this.indicatorsCategory = indicatorsCategory;
    }
    public AmpIndicatorRiskRatings getRisk() {
        return risk;
    }
    public void setRisk(AmpIndicatorRiskRatings risk) {
        this.risk = risk;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public Set<IndicatorActivity> getValuesActivity() {
        return valuesActivity;
    }
    public void setValuesActivity(Set<IndicatorActivity> valuesActivity) {
        this.valuesActivity = valuesActivity;
    }
    public Set<IndicatorTheme> getValuesTheme() {
        return valuesTheme;
    }

    public String getUnit() {
        return unit;
    }

    public void setValuesTheme(Set<IndicatorTheme> valuesTheme) {
        this.valuesTheme = valuesTheme;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public Object getIdentifier() {
        return indicatorId;
    }

    public Set<AmpIndicatorGlobalValue> getIndicatorValues() {
        return indicatorValues;
    }

    public void setIndicatorValues(final Set<AmpIndicatorGlobalValue> indicatorValues) {
        this.indicatorValues = indicatorValues;
    }

    public AmpIndicatorGlobalValue getBaseValue() {
        return indicatorValues.stream()
                .filter(AmpIndicatorGlobalValue::isBaseValue)
                .findFirst()
                .orElse(null);
    }

    public AmpIndicatorGlobalValue getTargetValue() {
        return indicatorValues.stream()
                .filter(AmpIndicatorGlobalValue::isTargetValue)
                .findFirst()
                .orElse(null);
    }
}
