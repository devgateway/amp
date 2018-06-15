/*
 * AmpTeam.java @Author Priyajith C Created: 13-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.ar.dbentity.AmpTeamFilterData;
import org.dgfoundation.amp.ar.dbentity.FilterDataSetInterface;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.kernel.ampapi.endpoints.serializers.AmpTeamSerializer;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.NameableOrIdentifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

@TranslatableClass (displayName = "Team")
@JsonSerialize(using = AmpTeamSerializer.class)
public class AmpTeam  implements Serializable, Comparable, Identifiable, /*Versionable,*/ FilterDataSetInterface<AmpTeamFilterData>,  
                                    NameableOrIdentifiable {
    private static final Logger logger = Logger.getLogger(AmpTeam.class);
    @Interchangeable(fieldTitle = "id", id = true)
    private Long ampTeamId;
    @TranslatableField
    @Interchangeable(fieldTitle = "name", value = true)
    private String name;
    
    private Boolean addActivity;
    private Boolean isolated;//called thus because 'private' is a reserved keyword in Java
    private Boolean computation;
    private Boolean hideDraftActivities;
    private Boolean useFilter;
    @TranslatableField
    private String description;

    private AmpTeamMember teamLead; // Denotes the Team Leader

    //private String type;          // Whether Bilateral or Multilateral
    

    

    private AmpTeam parentTeamId;
    
    private Collection childrenWorkspaces;
    
    private String accessType;      // Management or Team
    

    private AmpTeam relatedTeamId;  // a donor team referring a mofed team
    private Set<AmpActivityVersion> activityList;       // activities assigned to donor team
    
    private Set organizations;      // activities assigned to donor team
    private NpdSettings npdSettings;
    
    private AmpCategoryValue workspaceGroup;
    
    private String permissionStrategy;
    
    private Set<AmpTeamFilterData> filterDataSet;

    private AmpTemplatesVisibility fmTemplate;
    private AmpCategoryValue workspacePrefix;
    private Boolean crossteamvalidation;
    private AmpSummaryNotificationSettings sumaryNotificationSettings;



    //Global function to initialize the team filters inside the session
    public static AmpARFilter initializeTeamFiltersSession(AmpTeamMember member, HttpServletRequest request, HttpSession session){
        //Initialize Team Filter
        AmpTeam ampTeam = member.getAmpTeam();
        AmpARFilter af = new AmpARFilter();
        
        /**
         *  AmpARFilter.FILTER_SECTION_ALL, null - parameters were added on merge, might not be right
         */
        //af.readRequestData(request, AmpARFilter.FILTER_SECTION_ALL, null);
        af.fillWithDefaultsSettings();
        af.fillWithDefaultsFilter(null);
        
        if (ampTeam.getFilterDataSet()!=null && ampTeam.getFilterDataSet().size()>0 ){
            af = FilterUtil.buildFilter(ampTeam, null);
        }

        /* The prepare function needs to have the filter (af) already populated */
        /**
         * on merge - prepare is gone :)
        try {
            FilterUtil.prepare(request, af, false,new Long(ArConstants.DONOR_TYPE));
        } catch (Exception e) {
            logger.error("Error while preparing filter:", e);
        }
        */
        af.generateFilterQuery(request, true);
        session.setAttribute(ArConstants.TEAM_FILTER, af);
        return af;
    }
    
    @Override
    public AmpFilterData newAmpFilterData(FilterDataSetInterface filterRelObj,
            String propertyName, String propertyClassName,
            String elementClassName, String value) {
        return new AmpTeamFilterData(filterRelObj, propertyName, propertyClassName, elementClassName, value);
    }
    
    @Override
    public Set<AmpTeamFilterData> getFilterDataSet() {
        return filterDataSet;
    }
    
    @Override
    public void setFilterDataSet(Set<AmpTeamFilterData> filterDataSet) {
        this.filterDataSet = filterDataSet;
    }

    public String getPermissionStrategy() {
        return permissionStrategy;
    }

    public void setPermissionStrategy(String permissionStrategy) {
        this.permissionStrategy = permissionStrategy;
    }

    public NpdSettings getNpdSettings() {
        return npdSettings;
    }

    public void setNpdSettings(NpdSettings npdSettings) {
        this.npdSettings = npdSettings;
    }

    /**
     * @return ampTeamId
     */
    public Long getAmpTeamId() {
        return ampTeamId;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return teamLeadId
     */
    public AmpTeamMember getTeamLead() {
        return teamLead;
    }

    /**
     * @param ampTeamId
     */
    public void setAmpTeamId(Long ampTeamId) {
        this.ampTeamId = ampTeamId;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param teamLeadId
     */
    public void setTeamLead(AmpTeamMember teamLead) {
        this.teamLead = teamLead;
    }

    /**
     * @return Returns the parent team, for some reason mislabeled as parentTeamId
     * TODO: no it doesn't. returns AmpTeam
     */
    public AmpTeam getParentTeamId() {
        return parentTeamId;
    }

    /**
     * @param parentTeamId
     *            The parentTeamId to set.
     */
    public void setParentTeamId(AmpTeam parentTeamId) {
        this.parentTeamId = parentTeamId;
    }

    /**
     * @return Returns the accessType.
     */
    public String getAccessType() {
        return accessType;
    }

    /**
     * @param accessType
     *            The accessType to set.
     */
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
    
    /**
     * @return Returns the activityList.
     */
    public Set<AmpActivityVersion> getActivityList() {
        return activityList;
    }
    /**
     * @param activityList The activityList to set.
     */
    public void setActivityList(Set<AmpActivityVersion> activityList) {
        this.activityList = activityList;
    }
    /**
     * @return Returns the relatedTeam.
     */
    public AmpTeam getRelatedTeamId() {
        return relatedTeamId;
    }
    /**
     * @param relatedTeam The relatedTeam to set.
     */
    public void setRelatedTeamId(AmpTeam relatedTeam) {
        this.relatedTeamId = relatedTeam;
    }


    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        return ampTeamId.compareTo(((AmpTeam)o).getAmpTeamId());
    }
    
    public String toString() {
        return name;
    }

    public Object getIdentifier() {
        return this.getAmpTeamId();
    }

    public Set getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set organizations) {
        this.organizations = organizations;
    }

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

    public Collection getChildrenWorkspaces() {
        return childrenWorkspaces;
    }

    public void setChildrenWorkspaces(Collection childrenWorkspaces) {
        this.childrenWorkspaces = childrenWorkspaces;
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


    public Boolean getUseFilter() {
        return useFilter;
    }

    public void setUseFilter(Boolean useFilter) {
        this.useFilter = useFilter;
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

    public static String hqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpTeam.class, "name").getSQLFunctionCall(idSource + ".ampTeamId");
    }
    
    public boolean isSSCWorkspace () {
        return this.getWorkspacePrefix() != null && "SSC_".equals(this.getWorkspacePrefix().getValue());
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
        if (isolated == null) {
            this.isolated = false;
        } else {
            this.isolated = isolated;
        }
    }

    public AmpSummaryNotificationSettings getSumaryNotificationSettings() {
        return sumaryNotificationSettings;
    }

    public void setSumaryNotificationSettings(AmpSummaryNotificationSettings sumaryNotificationSettings) {
        this.sumaryNotificationSettings = sumaryNotificationSettings;
    }
}
