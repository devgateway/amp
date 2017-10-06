package org.digijava.module.aim.helper;

import java.util.Collection;
import java.util.Set;

public class AmpPrgIndicator
{
    private Long indicatorId;
    private String name;
    private String code;
    private String type;
    private String creationDate;
    private int category;
    private boolean npIndicator;
    private String description;
    private Collection<AmpPrgIndicatorValue> prgIndicatorValues;
    private Long sector[];
    private Set sectors;
    private Collection<AmpIndSectors> indSectores;
    private Set activity;
    private Long selectedActivityId;
    private boolean defaultInd;
    private boolean prjStatus;
    private boolean prgStatus;
    private Collection selectedActivity;
    /**
     * @return Returns the category.
     */
    public int getCategory() {
        return category;
    }
    /**
     * @param category The category to set.
     */
    public void setCategory(int category) {
        this.category = category;
    }
    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the indicatorId.
     */
    public Long getIndicatorId() {
        return indicatorId;
    }
    /**
     * @param indicatorId The indicatorId to set.
     */
    public void setIndicatorId(Long indicatorId) {
        this.indicatorId = indicatorId;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the npIndicator.
     */
    public boolean isNpIndicator() {
        return npIndicator;
    }
    /**
     * @param npIndicator The npIndicator to set.
     */
    public void setNpIndicator(boolean npIndicator) {
        this.npIndicator = npIndicator;
    }
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return Returns the prgIndicatorValues.
     */
    public Collection getPrgIndicatorValues() {
        return prgIndicatorValues;
    }
    /**
     * @param prgIndicatorValues The prgIndicatorValues to set.
     */
    public void setPrgIndicatorValues(Collection prgIndicatorValues) {
        this.prgIndicatorValues = prgIndicatorValues;
    }
    /**
     * @return Returns the creationDate.
     */
    public String getCreationDate() {
        return creationDate;
    }
    /**
     * @param creationDate The creationDate to set.
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    public Long getSector()[] {
        return sector;
    }
    public void setSector(Long sector[]) {
        this.sector = sector;
    }
    public Set getSectors() {
        return sectors;
    }
    
    public void setSectors(Set sectors) {
        this.sectors = sectors;
    }
    public Collection<AmpIndSectors> getIndSectores() {
        return indSectores;
    }
    public void setIndSectores(Collection<AmpIndSectors> indSectores) {
        this.indSectores = indSectores;
    }
    public Set getActivity() {
        return activity;
    }
    public void setActivity(Set activity) {
        this.activity = activity;
    }
    public Long getSelectedActivityId() {
        return selectedActivityId;
    }
    public void setSelectedActivityId(Long selectedActivityId) {
        this.selectedActivityId = selectedActivityId;
    }
    public boolean isDefaultInd() {
        return defaultInd;
    }
    public void setDefaultInd(boolean defaultInd) {
        this.defaultInd = defaultInd;
    }
    public boolean isPrjStatus() {
        return prjStatus;
    }
    public void setPrjStatus(boolean prjStatus) {
        this.prjStatus = prjStatus;
    }
    public boolean isPrgStatus() {
        return prgStatus;
    }
    public void setPrgStatus(boolean prgStatus) {
        this.prgStatus = prgStatus;
    }
    public Collection getSelectedActivity() {
        return selectedActivity;
    }
    public void setSelectedActivity(Collection selectedActivity) {
        this.selectedActivity = selectedActivity;
    }
}
