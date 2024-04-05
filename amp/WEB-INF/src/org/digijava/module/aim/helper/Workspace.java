/*
 * Workspace.java @Author Priyajith C Created: 07-Apr-2005
 */

package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.util.Collection;

public class Workspace {
    private String id;
    private String name;
    private Collection childOrgs;
    private String description;
    private String teamCategory;
    //private String type;
    private AmpCategoryValue type;
    private String workspaceType;
    private AmpCategoryValue workspaceGroup;
    private Long relatedTeam;
    private Long parentTeamId;
    private String parentTeamName;
    private boolean hasActivities;
    private boolean hasMembers;
    private Collection childWorkspaces;
    
    private Boolean hideDraftActivities;
    private Boolean addActivity;
    private Boolean computation;
    private Boolean useFilter;

    private AmpTemplatesVisibility fmTemplate;
    private AmpCategoryValue workspacePrefix;
    
    private Boolean crossteamvalidation;
    private Boolean isolated;
    private Boolean sendSummaryChangesApprover;
    private Boolean sendSummaryChangesManager;
    public Boolean getAddActivity() {
        return addActivity;
    }

    public void setAddActivity(Boolean addActivity) {
        this.addActivity = addActivity;
    }

    public Boolean getComputation() {
        return computation;
    }

    public void setComputation(Boolean computation) {
        this.computation = computation;
    }

    public Boolean getUseFilter() {
        return useFilter;
    }

    public void setUseFilter(Boolean useFilter) {
        this.useFilter = useFilter;
    }

    public Workspace() {
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
     * @return Returns the hasActivities.
     */
    public boolean isHasActivities() {
        return hasActivities;
    }
    /**
     * @param hasActivities The hasActivities to set.
     */
    public void setHasActivities(boolean hasActivities) {
        this.hasActivities = hasActivities;
    }
    /**
     * @return Returns the hasMembers.
     */
    public boolean isHasMembers() {
        return hasMembers;
    }
    /**
     * @param hasMembers The hasMembers to set.
     */
    public void setHasMembers(boolean hasMembers) {
        this.hasMembers = hasMembers;
    }
    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
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
     * @return Returns the teamCategory.
     */
    public String getTeamCategory() {
        return teamCategory;
    }
    /**
     * @param teamCategory The teamCategory to set.
     */
    public void setTeamCategory(String teamCategory) {
        this.teamCategory = teamCategory;
    }
    /**
     * @return Returns the relatedTeam.
     */
    public Long getRelatedTeam() {
        return relatedTeam;
    }
    /**
     * @param relatedTeam The relatedTeam to set.
     */
    public void setRelatedTeam(Long relatedTeam) {
        this.relatedTeam = relatedTeam;
    }
    /**
     * @return Returns the workspaceType.
     */
    public String getWorkspaceType() {
        return workspaceType;
    }
    /**
     * @param workspaceType The workspaceType to set.
     */
    public void setWorkspaceType(String workspaceType) {
        this.workspaceType = workspaceType;
    }
    /**
     * @return Returns the childWorkspaces.
     */
    public Collection getChildWorkspaces() {
        return childWorkspaces;
    }
    /**
     * @param childWorkspaces The childWorkspaces to set.
     */
    public void setChildWorkspaces(Collection childWorkspaces) {
        this.childWorkspaces = childWorkspaces;
    }

    public AmpCategoryValue getType() {
        return type;
    }

    public void setType(AmpCategoryValue type) {
        this.type = type;
    }

    public Collection getChildOrgs() {
        return childOrgs;
    }

    public void setChildOrgs(Collection childOrgs) {
        this.childOrgs = childOrgs;
    }

    /**
     * @return the hideDraftActivities
     */
    public Boolean getHideDraftActivities() {
        return hideDraftActivities;
    }

    /**
     * @param hideDraftActivities the hideDraftActivities to set
     */
    public void setHideDraftActivities(Boolean hideDraftActivities) {
        this.hideDraftActivities = hideDraftActivities;
    }

    public void setWorkspaceGroup(AmpCategoryValue workspaceGroup) {
        this.workspaceGroup = workspaceGroup;
    }

    public AmpCategoryValue getWorkspaceGroup() {
        return workspaceGroup;
    }

    public AmpTemplatesVisibility getFmTemplate() {
        return fmTemplate;
    }

    public void setFmTemplate(AmpTemplatesVisibility fmTemplate) {
        this.fmTemplate = fmTemplate;
    }

    public AmpCategoryValue getWorkspacePrefix() {
        return workspacePrefix;
    }

    public void setWorkspacePrefix(AmpCategoryValue workspacePrefix) {
        this.workspacePrefix = workspacePrefix;
    }

    public Boolean getCrossteamvalidation() {
        return crossteamvalidation;
    }

    public void setCrossteamvalidation(Boolean crossteamvalidation) {
        this.crossteamvalidation = crossteamvalidation;
    }

    public Boolean getIsolated() {
        return isolated;
    }

    public void setIsolated(Boolean isolated) {
        this.isolated = isolated;
    }

    public Boolean getSendSummaryChangesApprover() {
        return sendSummaryChangesApprover;
    }

    public void setSendSummaryChangesApprover(Boolean sendSummaryChangesApprover) {
        this.sendSummaryChangesApprover = sendSummaryChangesApprover;
    }

    public Boolean getSendSummaryChangesManager() {
        return sendSummaryChangesManager;
    }

    public void setSendSummaryChangesManager(Boolean sendSummaryChangesManager) {
        this.sendSummaryChangesManager = sendSummaryChangesManager;
    }

    public Long getParentTeamId() {
        return parentTeamId;
    }

    public void setParentTeamId(Long parentTeamId) {
        this.parentTeamId = parentTeamId;
    }

    public String getParentTeamName() {
        return parentTeamName;
    }

    public void setParentTeamName(String parentTeamName) {
        this.parentTeamName = parentTeamName;
    }

   
}
