/*
 * AmpTeam.java @Author Priyajith C Created: 13-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.ar.dbentity.AmpTeamFilterData;
import org.dgfoundation.amp.ar.dbentity.FilterDataSetInterface;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.kernel.ampapi.endpoints.serializers.AmpTeamSerializer;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.NameableOrIdentifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import javax.persistence.*;

@Entity
@Table(name = "AMP_TEAM")
@TranslatableClass (displayName = "Team")
@JsonSerialize(using = AmpTeamSerializer.class)
public class AmpTeam  implements Serializable, Comparable, Identifiable, FilterDataSetInterface<AmpTeamFilterData>,
                                    NameableOrIdentifiable {
    private static final Logger logger = Logger.getLogger(AmpTeam.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_team_seq_generator")
    @SequenceGenerator(name = "amp_team_seq_generator", sequenceName = "AMP_TEAM_seq", allocationSize = 1)
    @Column(name = "amp_team_id")
    @PossibleValueId

    private Long ampTeamId;

    @Column(name = "name")
    @TranslatableField
    @PossibleValueValue
    private String name;

    @Column(name = "access_type")
    private String accessType;

    @Column(name = "description")
    @TranslatableField

    private String description;

    @Column(name = "add_activity")
    private Boolean addActivity;

    @Column(name = "isolated")
    private Boolean isolated;

    @Column(name = "computation")
    private Boolean computation;

    @Column(name = "hide_draft")
    private Boolean hideDraftActivities;

    @Column(name = "use_filter")
    private Boolean useFilter;

    @Column(name = "cross_team_validation")
    private Boolean crossTeamValidation;

    @ManyToOne
    @JoinColumn(name = "team_lead_id", referencedColumnName = "amp_team_mem_id")
    private AmpTeamMember teamLead;

    @ManyToOne
    @JoinColumn(name = "parent_team_id")
    private AmpTeam parentTeamId;

    @ManyToOne
    @JoinColumn(name = "related_team_id")
    private AmpTeam relatedTeamId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_TEAM_ACTIVITIES",
            joinColumns = @JoinColumn(name = "amp_team_id"),
            inverseJoinColumns = @JoinColumn(name = "amp_activity_id"))
    private Set<AmpActivityVersion> activityList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_TEAM_ORGS",
            joinColumns = @JoinColumn(name = "amp_team_id"),
            inverseJoinColumns = @JoinColumn(name = "amp_org_id"))
    private Set<AmpOrganisation> organizations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_categoryvalue_id")
    private AmpCategoryValue workspaceGroup;

    @Column(name = "permission_strategy")
    private String permissionStrategy;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpTeamFilterData> filterDataSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fm_template")
    private AmpTemplatesVisibility fmTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_prefix_categoryvalue_id")
    private AmpCategoryValue workspacePrefix;




 @Transient

    private Collection childrenWorkspaces;
    @Transient
    private Boolean crossteamvalidation;

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
}
