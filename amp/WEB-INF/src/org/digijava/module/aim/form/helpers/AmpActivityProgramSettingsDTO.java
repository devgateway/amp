package org.digijava.module.aim.form.helpers;
import org.digijava.module.aim.dbentity.AmpTheme;

public class AmpActivityProgramSettingsDTO {
    private AmpTheme defaultHierarchy;

    private Long defaultHierarchyId;

    private boolean allowMultiple;

    private Long ampProgramSettingsId;

    private String name;

    private String startDate;

    private String endDate;


    public AmpActivityProgramSettingsDTO() {}

    public AmpTheme getDefaultHierarchy() {
        return defaultHierarchy;
    }

    public void setDefaultHierarchy(AmpTheme defaultHierarchy) {
        this.defaultHierarchy = defaultHierarchy;
    }

    public boolean isAllowMultiple() {
        return allowMultiple;
    }

    public void setAllowMultiple(boolean allowMultiple) {
        this.allowMultiple = allowMultiple;
    }

    public Long getAmpProgramSettingsId() {
        return ampProgramSettingsId;
    }

    public void setAmpProgramSettingsId(Long ampProgramSettingsId) {
        this.ampProgramSettingsId = ampProgramSettingsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getDefaultHierarchyId() {
        return defaultHierarchyId;
    }

    public void setDefaultHierarchyId(Long defaultHierarchyId) {
        this.defaultHierarchyId = defaultHierarchyId;
    }
}
