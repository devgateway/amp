/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @author aartimon@dginternational.org
 * @since Apr 27, 2011
 */
@TranslatableClass (displayName = "Activity Version")
@Entity
@Table(name = "amp_activity_version")
@Cacheable
@DynamicUpdate
public class AmpActivityVersion extends AmpActivityFields implements Versionable{
    
    /**
     * 
     * NOTE:
     *    All new fields should be added in {@link AmpActivityFields}
     *    
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ACTIVITY_VERSION_seq")
    @SequenceGenerator(name = "AMP_ACTIVITY_VERSION_seq", sequenceName = "AMP_ACTIVITY_VERSION_seq", allocationSize = 1)
    @Column(name = "amp_activity_id")
    private Long ampActivityId;

    @Column(name = "amp_id")
    private String ampId;

    @Column(name = "name")
    private String name;

    @Column(name = "gov_agreement_number")
    private String govAgreementNumber;

    @Column(name = "budget_code_project_id")
    private String budgetCodeProjectID;

    @Column(name = "budget_sector")
    private Long budgetsector;

    @Column(name = "budget_organization")
    private Long budgetorganization;

    @Column(name = "budget_department")
    private Long budgetdepartment;

    @Column(name = "budget_program")
    private Long budgetprogram;

    @Column(name = "budget")
    private Integer budget;

    @Column(name = "iati_identifier")
    private String iatiIdentifier;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "lessons_learned", columnDefinition = "text")
    private String lessonsLearned;

    @Column(name = "status_other_info")
    private String statusOtherInfo;

    @Column(name = "project_category_other_info")
    private String projectCategoryOtherInfo;

    @Column(name = "modalities_other_info")
    private String modalitiesOtherInfo;

    @Column(name = "objectives", columnDefinition = "text")
    private String objective;

    @Column(name = "results", columnDefinition = "text")
    private String results;

    @Column(name = "purpose", columnDefinition = "text")
    private String purpose;

    @Column(name = "projectComments", columnDefinition = "text")
    private String projectComments;

    @Column(name = "project_impact", columnDefinition = "text")
    private String projectImpact;

    @Column(name = "activity_summary", columnDefinition = "text")
    private String activitySummary;

    @Column(name = "conditionality", columnDefinition = "text")
    private String conditionality;

    @Column(name = "project_management", columnDefinition = "text")
    private String projectManagement;

    @Column(name = "document_space")
    private String documentSpace;

    @Column(name = "language")
    private String language;

    @Column(name = "equalOpportunity")
    private String equalOpportunity;

    @Column(name = "environment")
    private String environment;

    @Column(name = "minorities")
    private String minorities;

    @Column(name = "draft")
    private Boolean draft;

    @Column(name = "change_type")
    private String changeType;

    @Column(name = "last_imported_at")
    private Date lastImportedAt;

    @ManyToOne
    @JoinColumn(name = "last_imported_by")
    private User lastImportedBy;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "created_as_draft")
    private boolean createdAsDraft;

    @Column(name = "original_comp_date")
    private Date originalCompDate;

    @Column(name = "contracting_date")
    private Date contractingDate;

    @Column(name = "disbursments_date")
    private Date disbursmentsDate;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "status_reason", columnDefinition = "text")
    private String statusReason;

    @Column(name = "proposed_start_date")
    private Date proposedStartDate;

    @Column(name = "actual_start_date")
    private Date actualStartDate;

    @Column(name = "proposed_approval_date")
    private Date proposedApprovalDate;

    @Column(name = "actual_approval_date")
    private Date actualApprovalDate;

    @Column(name = "actual_completion_date")
    private Date actualCompletionDate;

    @Column(name = "proposed_completion_date")
    private Date proposedCompletionDate;

    @Column(name = "date_created")
    private Date createdDate;

    @Column(name = "date_updated")
    private Date updatedDate;

    @Column(name = "iati_last_update_date")
    private Date iatiLastUpdatedDate;

    @Column(name = "program_description", columnDefinition = "text")
    private String programDescription;

    @Column(name = "indirect_on_budget")
    private Boolean indirectOnBudget;

    @Column(name = "FY")
    private String FY;

    @Column(name = "vote")
    private String vote;

    @Column(name = "subVote")
    private String subVote;

    @Column(name = "subProgram")
    private String subProgram;

    @Column(name = "project_code")
    private String projectCode;

    @Column(name = "ministry_code")
    private String ministryCode;

    @Column(name = "cris_number")
    private String crisNumber;

    @Column(name = "governmentApprovalProcedures")
    private Boolean governmentApprovalProcedures;

    @Column(name = "jointCriteria")
    private Boolean jointCriteria;

    @Column(name = "humanitarianAid")
    private Boolean humanitarianAid;

    @Column(name = "archived")
    private Boolean archived;

    @Column(name = "multi_stakeholder_partnership")
    private Boolean multiStakeholderPartnership;

    @Column(name = "multi_stakeholder_partners")
    private String multiStakeholderPartners;

    @ManyToOne
    @JoinColumn(name = "amp_team_id")
    private AmpTeam team;

    @ManyToOne
    @JoinColumn(name = "activity_creator", referencedColumnName = "amp_team_mem_id")
    private AmpTeamMember activityCreator;

    @Column(name = "approval_status")
    @Type(type = "org.digijava.module.aim.dbentity.NamedEnumType", parameters = {
            @org.hibernate.annotations.Parameter(name = "enumClass", value = "org.digijava.module.aim.dbentity.ApprovalStatus"),
            @org.hibernate.annotations.Parameter(name = "valueProperty", value = "dbName")
    })
    private ApprovalStatus approvalStatus;

    @Column(name = "funding_sources_number")
    private Integer fundingSourcesNumber;

    @Column(name = "line_min_rank")
    private Integer lineMinRank;

    @ManyToOne
    @JoinColumn(name = "amp_activity_group_id")
    private AmpActivityGroup ampActivityGroup;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @ManyToOne
    @JoinColumn(name = "modified_by", referencedColumnName = "amp_team_mem_id")
    private AmpTeamMember modifiedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "AMP_ACTIVITY_EFFECTIVENESS_INDICATOR_OPTIONS",
            joinColumns = @JoinColumn(name = "amp_activity_id"),
            inverseJoinColumns = @JoinColumn(name = "amp_indicator_option_id"))
    private Set<AmpAidEffectivenessIndicatorOption> selectedEffectivenessIndicatorOptions;

    @ManyToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "AMP_ACTIVITIES_CATEGORYVALUES",
            joinColumns = @JoinColumn(name = "amp_activity_id"),
            inverseJoinColumns = @JoinColumn(name = "amp_categoryvalue_id"))
    private Set<AmpCategoryValue> categories;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpActivityDocument> activityDocuments;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpOrgRole> orgrole;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpActivitySector> sectors;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<IPAContract> contracts;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpActivityLocation> locations;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpActivityInternalId> internalIds;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpFunding> funding;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpComponent> components;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpStructure> structures;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_MEMBER_ACTIVITIES",
            joinColumns = @JoinColumn(name = "amp_activity_id"),
            inverseJoinColumns = @JoinColumn(name = "amp_team_mem_id"))
    private Set<AmpTeamMember> member;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpIssues> issues;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpRegionalObservation> regionalObservations;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpLineMinistryObservation> lineMinistryObservations;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpRegionalFunding> regionalFundings;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpAhsurvey> survey;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpGPISurvey> gpiSurvey;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpAnnualProjectBudget> annualProjectBudgets;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpActivityProgram> actPrograms;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpActivityBudgetStructure> actBudgetStructure;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<IndicatorActivity> indicators;

    @ManyToOne
    @JoinColumn(name = "approvedBy", referencedColumnName = "amp_team_mem_id")
    private AmpTeamMember approvedBy;

    @Column(name = "approvalDate")
    private Date approvalDate;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<AmpActivityContact> activityContacts;

    @Column(name = "merged_activity")
    private Boolean mergedActivity;

    @ManyToOne
    @JoinColumn(name = "merge_source1")
    private AmpActivityVersion mergeSource1;

    @ManyToOne
    @JoinColumn(name = "merge_source2")
    private AmpActivityVersion mergeSource2;

    @Column(name = "activity_type")
    private Long activityType;

    @Column(name = "proposed_project_life")
    private Integer proposedProjectLife;

    @OneToMany(mappedBy = "ampActivityVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpFundingAmount> costAmounts;
    @Transient
    protected String rejectMessage;
    

    public AmpActivityVersion() {
    }

    public AmpActivityVersion(Long ampActivityId, String name, Date updatedDate, AmpTeamMember modifiedBy, String ampid) {
        this.ampActivityId=ampActivityId;
        this.name=name;
        //this.budget=budget;
        this.updatedDate=updatedDate;
        this.modifiedBy = modifiedBy;
        this.ampId=ampid;
    }

    public AmpActivityVersion(Long ampActivityId, String name, String ampid) {
        this.ampActivityId=ampActivityId;
        this.name=name;
        this.ampId=ampid;
    }

    /* Note, archived should be Boolean to support null values */
    public AmpActivityVersion(Long ampActivityId, String name, String ampid, Boolean archived) {
        this.ampActivityId=ampActivityId;
        this.name=name;
        this.ampId=ampid;
        this.archived = archived;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Object getValue() {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Output getOutput() {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws Exception {
        throw new AssertionError("Not implemented");
    }
    
    public static String sqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpActivityVersion.class, "name").getSQLFunctionCall(idSource);
    }

    public static String hqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpActivityVersion.class, "name").getSQLFunctionCall(idSource + ".ampActivityId");
    }
    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }

    public void addLocation(AmpActivityLocation activityLocation) {
        activityLocation.setActivity(this);
        getLocations().add(activityLocation);
    }
}
