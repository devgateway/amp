package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTheme;

import java.util.Collection;

public class NewIndicatorForm
    extends ActionForm {
    private Long id;
    private String name;
    private String description;
    private String code;
    private String date;
    private Integer category;
    private String type;
    private String action;
    private String keyword;
    private String prgStatus;
    private String prjStatus;

    private Collection<AmpTheme> programsCol;
    private Collection<AmpActivity> ActivitiesCol;
    private Long selectedProgramId;
    private Collection<LabelValueBean> selectedPrograms;
    private Long selectedActivityId;
    private Collection<LabelValueBean> selectedActivities;
    private Long selActivitySector[];
    private Long themeId;
    private boolean sectorReset;
    private Long sectorScheme;
    private Collection sectorSchemes;
    private Collection parentSectors;
    private Collection childSectorsLevel1;
    private Collection childSectorsLevel2;
    private Long sector;
    private Long subsectorLevel1;
    private Long subsectorLevel2;
    private Collection activitySectors;
    private Long selectedActivity[];

    public Collection getSectorSchemes() {
        return sectorSchemes;
    }

    public void setSectorSchemes(Collection sectorSchemes) {
        this.sectorSchemes = sectorSchemes;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public NewIndicatorForm() {
    }

    public Integer getCategory() {
        return category;
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getKeyword() {
        return keyword;
    }

    public Long getSelectedActivityId() {
        return selectedActivityId;
    }

    public Long getSelectedProgramId() {
        return selectedProgramId;
    }

    public String getAction() {
        return action;
    }

    public Collection getSelectedPrograms() {
        return selectedPrograms;
    }

    public Collection getProgramsCol() {
        return programsCol;
    }

    public Collection getSelectedActivities() {
        return selectedActivities;
    }

    public Collection getActivitiesCol() {
        return ActivitiesCol;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setSelectedPrograms(Collection selectedPrograms) {
        this.selectedPrograms = selectedPrograms;
    }

    public void setSelectedProgramId(Long selectedProgramId) {
        this.selectedProgramId = selectedProgramId;
    }

    public void setSelectedActivityId(Long selectedActivityId) {
        this.selectedActivityId = selectedActivityId;
    }

    public void setSelectedActivities(Collection selectedActivities) {
        this.selectedActivities = selectedActivities;
    }

    public void setProgramsCol(Collection programsCol) {
        this.programsCol = programsCol;
    }

    public void setActivitiesCol(Collection ActivitiesCol) {
        this.ActivitiesCol = ActivitiesCol;
    }


    
    public void reset(){
        this.id=null;
        this.name=null;
        this.description=null;
        this.code=null;
        this.date=null;
        this.category=null;
        this.type=null;
        this.action=null;
        this.keyword=null;
        this.programsCol=null;
        this.ActivitiesCol=null;
        this.selectedProgramId=null;
        this.selectedPrograms=null;
        this.selectedActivityId=null;
        this.selectedActivities=null;
        this.selActivitySector=null;
        this.activitySectors = null;
        this. sectorSchemes=null;
    }
    
    public void resetsector(){
        this.sector = new Long(-1);
        this.subsectorLevel1 = new Long(-1);
        this.subsectorLevel2 = new Long(-1);
        this.sectorScheme = new Long(-1);
        this.parentSectors = null;
        this.childSectorsLevel1 = null;
        this.childSectorsLevel2 = null;
    }
    
    

    public Long[] getSelActivitySector() {
        return selActivitySector;
    }

    public void setSelActivitySector(Long[] selActivitySector) {
        this.selActivitySector = selActivitySector;
    }

    public boolean isSectorReset() {
        return sectorReset;
    }

    public void setSectorReset(boolean sectorReset) {
        this.sectorReset = sectorReset;
    }

    public Long getSectorScheme() {
        return sectorScheme;
    }

    public void setSectorScheme(Long sectorScheme) {
        this.sectorScheme = sectorScheme;
    }

    public Collection getParentSectors() {
        return parentSectors;
    }

    public void setParentSectors(Collection parentSectors) {
        this.parentSectors = parentSectors;
    }

    public Collection getChildSectorsLevel1() {
        return childSectorsLevel1;
    }

    public void setChildSectorsLevel1(Collection childSectorsLevel1) {
        this.childSectorsLevel1 = childSectorsLevel1;
    }

    public Collection getChildSectorsLevel2() {
        return childSectorsLevel2;
    }

    public void setChildSectorsLevel2(Collection childSectorsLevel2) {
        this.childSectorsLevel2 = childSectorsLevel2;
    }

    public Long getSector() {
        return sector;
    }

    public void setSector(Long sector) {
        this.sector = sector;
    }

    public Long getSubsectorLevel1() {
        return subsectorLevel1;
    }

    public void setSubsectorLevel1(Long subsectorLevel1) {
        this.subsectorLevel1 = subsectorLevel1;
    }

    public Long getSubsectorLevel2() {
        return subsectorLevel2;
    }

    public void setSubsectorLevel2(Long subsectorLevel2) {
        this.subsectorLevel2 = subsectorLevel2;
    }

    public Collection getActivitySectors() {
        return activitySectors;
    }

    public void setActivitySectors(Collection activitySectors) {
        this.activitySectors = activitySectors;
    }

    public String getPrgStatus() {
        return prgStatus;
    }

    public void setPrgStatus(String prgStatus) {
        this.prgStatus = prgStatus;
    }

    public String getPrjStatus() {
        return prjStatus;
    }

    public void setPrjStatus(String prjStatus) {
        this.prjStatus = prjStatus;
    }

    public Long[] getSelectedActivity() {
        return selectedActivity;
    }

    public void setSelectedActivity(Long[] selectedActivity) {
        this.selectedActivity = selectedActivity;
    }

    
}
