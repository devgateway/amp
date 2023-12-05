package org.digijava.module.aim.dbentity;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorAccessType;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@TranslatableClass (displayName = "IndicatorLayer")
public class AmpIndicatorLayer implements Serializable, Comparable <AmpIndicatorLayer>, LoggerIdentifiable {
    
    private Long id;
    @TranslatableField
    private String name;
    @VersionableFieldTextEditor
    private String description;
    private Set <AmpIndicatorColor> colorRamp;
    private Long numberOfClasses;
    private AmpCategoryValue admLevel;
    private Set <AmpLocationIndicatorValue> indicatorValues;
    private Set <AmpIndicatorWorkspace> sharedWorkspaces;
    @TranslatableField
    private String unit;
    private AmpCategoryValue indicatorType;
    private Boolean population;
    private IndicatorAccessType accessType;
    private Date createdOn;
    private Date updatedOn;
    private AmpTeamMember createdBy;
    private Boolean zeroCategoryEnabled;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNumberOfClasses() {
        return numberOfClasses;
    }

    public void setNumberOfClasses(Long numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }
    
    @Override
    public int compareTo(AmpIndicatorLayer o) {
        return id.compareTo(o.getId());
    }

    public Object getObjectType() {
        return this.getClass().getName();
    }
    @Override
    public String getObjectFilteredName() {
        return DbUtil.filter(getObjectName());
    }

    public Object getIdentifier() {
        return this.getId();
    }

    public String getObjectName() {
        return this.getId()+" "+this.getName();
    }

    public AmpCategoryValue getAdmLevel() {
        return admLevel;
    }

    public void setAdmLevel(AmpCategoryValue admLevel) {
        this.admLevel = admLevel;
    }

    public Set<AmpIndicatorColor> getColorRamp() {
        return colorRamp;
    }

    public void setColorRamp(Set<AmpIndicatorColor> colorRamp) {
        this.colorRamp = colorRamp;
    }

    public Set<AmpLocationIndicatorValue> getIndicatorValues() {
        return indicatorValues;
    }


    public void setIndicatorValues(Set<AmpLocationIndicatorValue> indicatorValues) {
        this.indicatorValues = indicatorValues;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public IndicatorAccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(IndicatorAccessType accessType) {
        this.accessType = accessType;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public AmpTeamMember getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AmpTeamMember createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn=updatedOn;
    }

    public Set<AmpIndicatorWorkspace> getSharedWorkspaces() {
        return sharedWorkspaces;
    }

    public void setSharedWorkspaces(Set<AmpIndicatorWorkspace> sharedWorkspaces) {
        this.sharedWorkspaces = sharedWorkspaces;
    }

    /**
     * @return the indicatorType
     */
    public AmpCategoryValue getIndicatorType() {
        return indicatorType;
    }

    /**
     * @param indicatorType the indicatorType to set
     */
    public void setIndicatorType(AmpCategoryValue indicatorType) {
        this.indicatorType = indicatorType;
    }

    /**
     * @return the population
     */
    public Boolean isPopulation() {
        return population;
    }

    /**
     * @param population the population to set
     */
    public void setPopulation(Boolean population) {
        this.population = population;
    }

    public Boolean getZeroCategoryEnabled() {
        return zeroCategoryEnabled;
    }

    public void setZeroCategoryEnabled(Boolean zeroCategoryEnabled) {
        this.zeroCategoryEnabled = zeroCategoryEnabled;
    }

   
}
