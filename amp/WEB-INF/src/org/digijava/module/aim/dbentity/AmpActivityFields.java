package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.annotations.activityversioning.VersionableCollection;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldSimple;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permissible;
import org.hibernate.Query;
import org.hibernate.Session;

@TranslatableClass (displayName = "Activity Form Field")
public abstract class AmpActivityFields extends Permissible implements Comparable<AmpActivityVersion>, Serializable,
LoggerIdentifiable, Cloneable {

	private static final long serialVersionUID = 1L;

	protected static String [] IMPLEMENTED_ACTIONS=new String[]{GatePermConst.Actions.EDIT};

	@Interchangeable(fieldTitle = "Created By")
	@VersionableFieldSimple(fieldTitle = "Created By", blockSingleChange = true)
	protected AmpTeamMember createdBy;
	
	@Interchangeable(fieldTitle = "Project Impact")
	@VersionableFieldTextEditor(fieldTitle = "Project Impact")
	protected String projectImpact;
	
	@Interchangeable(fieldTitle = "Activity Summary")
	@VersionableFieldTextEditor(fieldTitle = "Activity Summary")
	protected String activitySummary;
	
	@Interchangeable(fieldTitle = "Conditionality")
	@VersionableFieldTextEditor(fieldTitle = "Conditionality")
	protected String conditionality;
	
	@Interchangeable(fieldTitle = "Project Management")
	@VersionableFieldTextEditor(fieldTitle = "Project Management")
	protected String projectManagement;

	@Interchangeable(fieldTitle = "Activity Budget")
	@VersionableFieldSimple(fieldTitle = "Activity Budget")
	protected Integer budget;
	
	@Interchangeable(fieldTitle = "Government Agreement Number")
	@VersionableFieldSimple(fieldTitle = "Government Agreement Number")
	protected String govAgreementNumber;
	
	@Interchangeable(fieldTitle = "Budget Code Project ID")
	@VersionableFieldSimple(fieldTitle = "Budget Code Project ID")
	protected String budgetCodeProjectID;
	
	@Interchangeable(fieldTitle = "Budget Sector")
	@VersionableFieldSimple(fieldTitle = "Budget Sector")
	protected Long budgetsector;
	
	@Interchangeable(fieldTitle = "Budget Organization")
	@VersionableFieldSimple(fieldTitle = "Budget Organization")
	protected Long budgetorganization;
	
	@Interchangeable(fieldTitle = "Budget Department")
	@VersionableFieldSimple(fieldTitle = "Budget Department")
	protected Long budgetdepartment;
	
	@Interchangeable(fieldTitle = "Budget Program")
	@VersionableFieldSimple(fieldTitle = "Budget Program")
	protected Long budgetprogram;

	//protected String govAgreementNumber;

	@Interchangeable(fieldTitle = "Internal ID")
	@PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_ID})
	@VersionableFieldSimple(fieldTitle = "Internal ID", blockSingleChange = true)
	protected Long ampActivityId ;

	@Interchangeable(fieldTitle = "AMP Id")
	@VersionableFieldSimple(fieldTitle = "AMP Id", blockSingleChange = true)
	protected String ampId ;

	@Interchangeable(fieldTitle = "Name")
	@PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_LABEL})
	@VersionableFieldSimple(fieldTitle = "Name", mandatoryForSingleChange = true)
	@TranslatableField
	protected String name ;
	
	@Interchangeable(fieldTitle = "Activity Description")
	@VersionableFieldTextEditor(fieldTitle = "Activity Description")
	protected String description ;

	@Interchangeable(fieldTitle = "Project Comments")
	@VersionableFieldTextEditor(fieldTitle = "Project Comments")
	protected String projectComments ;
	
	@Interchangeable(fieldTitle = "Lessons Learned")
	@VersionableFieldTextEditor(fieldTitle = "Lessons Learned")
	protected String lessonsLearned;
	
	@Interchangeable(fieldTitle = "Objective")
	@VersionableFieldTextEditor(fieldTitle = "Objective")
	protected String objective ;
	
	@Interchangeable(fieldTitle = "Purpose")
	@VersionableFieldTextEditor(fieldTitle = "Purpose")
	protected String purpose;
	
	@Interchangeable(fieldTitle = "Results")
	@VersionableFieldTextEditor(fieldTitle = "Results")
	protected String results;
	
	@Interchangeable(fieldTitle = "Document Space")
	@VersionableFieldSimple(fieldTitle = "Document Space")
	protected String documentSpace;

	@Interchangeable(fieldTitle = "Is Draft?")
	@VersionableFieldSimple(fieldTitle = "Is Draft?")
	protected Boolean draft;

	@Interchangeable(fieldTitle = "Equal Oportunity")
	@VersionableFieldTextEditor(fieldTitle = "Equal Oportunity")
	protected String equalOpportunity;
	
	@Interchangeable(fieldTitle = "Environment")
	@VersionableFieldTextEditor(fieldTitle = "Environment")
	protected String environment;
	
	@Interchangeable(fieldTitle = "Minorities")
	@VersionableFieldTextEditor(fieldTitle = "Minorities")
	protected String minorities;

	@Interchangeable(fieldTitle = "Language")
	@VersionableFieldSimple(fieldTitle = "Language")
	protected String language ;

	@Interchangeable(fieldTitle = "Original Date")
	@VersionableFieldSimple(fieldTitle = "Original Date")
	protected Date originalCompDate;
	
	@Interchangeable(fieldTitle = "Contracting Date")
	@VersionableFieldSimple(fieldTitle = "Contracting Date")
	protected Date contractingDate;
	
	@Interchangeable(fieldTitle = "Disbursement Date")
	@VersionableFieldSimple(fieldTitle = "Disbursement Date")
	protected Date disbursmentsDate;
	
	@Interchangeable(fieldTitle = "Sectors")
	@VersionableCollection(fieldTitle = "Sectors")
	protected Set sectors ;
	
	@Interchangeable(fieldTitle = "Contracts")
	@VersionableCollection(fieldTitle = "Contracts")
	protected Set contracts;
	
	@Interchangeable(fieldTitle = "Locations")
	@VersionableCollection(fieldTitle = "Locations")
	protected Set<AmpActivityLocation> locations ;
	
	@Interchangeable(fieldTitle = "Org. Role")
	@VersionableCollection(fieldTitle = "Org. Role")
	protected Set<AmpOrgRole> orgrole;
	
	@Interchangeable(fieldTitle = "Internal IDs")
	@VersionableCollection(fieldTitle = "Internal IDs")
	protected Set internalIds ;
	
	@Interchangeable(fieldTitle = "Fundings")
	@VersionableCollection(fieldTitle = "Fundings")
	protected Set<AmpFunding> funding ;
	
	@Interchangeable(fieldTitle = "Progress")
	@VersionableCollection(fieldTitle = "Progress")
	protected Set progress;
	
	/**
	 * Old, not used anymore
	 * @deprecated
	 */
	@Deprecated
	protected Set documents ;
	
	@Interchangeable(fieldTitle = "Issues")
	@VersionableCollection(fieldTitle = "Issues")
	protected Set<AmpIssues> issues;

	@Interchangeable(fieldTitle = "Regional Observations")
	@VersionableCollection(fieldTitle = "Regional Observations")
	protected Set<AmpRegionalObservation> regionalObservations;

	@Interchangeable(fieldTitle = "Line Ministry Observations")
	@VersionableCollection(fieldTitle = "Line Ministry Observations")
	protected Set<AmpLineMinistryObservation> lineMinistryObservations;

	@Interchangeable(fieldTitle = "Costs")
	@VersionableCollection(fieldTitle = "Costs")
	protected Set costs;
	
	@Interchangeable(fieldTitle = "Program Description")
	@VersionableFieldTextEditor(fieldTitle = "Program Description")
	protected String programDescription;
	
	@Interchangeable(fieldTitle = "Team")
	@VersionableFieldSimple(fieldTitle = "Team")
	protected AmpTeam team;
	//@VersionableCollection(fieldTitle = "Members")
	protected Set member;

	@Interchangeable(fieldTitle = "Contact Name") //slightly fishy here
	protected String contactName;
	//protected AmpTeamMember updatedBy; !!! Use modifiedBy

	@Interchangeable(fieldTitle = "Fun Amount")
	@VersionableFieldSimple(fieldTitle = "Fun Amount")
	protected Double funAmount;
	
	@Interchangeable(fieldTitle = "Currency Code")
	@VersionableFieldSimple(fieldTitle = "Currency Code")
	protected String currencyCode;
	
	@Interchangeable(fieldTitle = "Fun Date")
	@VersionableFieldSimple(fieldTitle = "Fun Date")
	protected Date funDate;

	/**
	 * 
	 * @deprecated
	 */
	@Deprecated
	protected Set referenceDocs;

	/*
	@Deprecated
	@VersionableCollection(fieldTitle = "Activity Programs")
	protected Set activityPrograms;
	 */

	// use contFirstName and contLastName instead.
	// The field is defunct

	// Donor contact information
	@Interchangeable(fieldTitle = "Contact First Name")
	protected String contFirstName;
	@Interchangeable(fieldTitle = "Contact Last Name")
	protected String contLastName;
	@Interchangeable(fieldTitle = "Email")
	protected String email;
	@Interchangeable(fieldTitle = "Donor Contact Title")
	protected String dnrCntTitle;
	@Interchangeable(fieldTitle = "Donor Contact Organization")
	protected String dnrCntOrganization;
	@Interchangeable(fieldTitle = "Donor Contact Phone Number")
	protected String dnrCntPhoneNumber;
	@Interchangeable(fieldTitle = "Donor Contact Fax Number")
	protected String dnrCntFaxNumber;

	// MOFED contact information
	@Interchangeable(fieldTitle = "MOFED Contact First Name")
	protected String mofedCntFirstName;
	@Interchangeable(fieldTitle = "MOFED Contact Last Name")
	protected String mofedCntLastName;
	@Interchangeable(fieldTitle = "MOFED Contact Email")
	protected String mofedCntEmail;
	@Interchangeable(fieldTitle = "MOFED Contact Title")
	protected String mfdCntTitle;
	@Interchangeable(fieldTitle = "MOFED Contact Organization")
	protected String mfdCntOrganization;
	@Interchangeable(fieldTitle = "MOFED Contact Phone Number")
	protected String mfdCntPhoneNumber;
	@Interchangeable(fieldTitle = "MOFED Contact Fax Number")
	protected String mfdCntFaxNumber;

	// Project Coordinator contact information
	@Interchangeable(fieldTitle = "Project Coordinator First Name")
	protected String prjCoFirstName;
	@Interchangeable(fieldTitle = "Project Coordinator Last Name")
	protected String prjCoLastName;
	@Interchangeable(fieldTitle = "Project Coordinator Email")
	protected String prjCoEmail;
	@Interchangeable(fieldTitle = "Project Coordinator Title")
	protected String prjCoTitle;
	@Interchangeable(fieldTitle = "Project Coordinator Organization")
	protected String prjCoOrganization;
	@Interchangeable(fieldTitle = "Project Coordinator Phone Number")
	protected String prjCoPhoneNumber;
	@Interchangeable(fieldTitle = "Project Coordinator Fax Number")
	protected String prjCoFaxNumber;

	// Sector Ministry contact information
	
	@Interchangeable(fieldTitle = "Sector Ministry Contact First Name")
	protected String secMiCntFirstName;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Last Name")
	protected String secMiCntLastName;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Email")
	protected String secMiCntEmail;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Title")
	protected String secMiCntTitle;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Organization")
	protected String secMiCntOrganization;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Phone Number")
	protected String secMiCntPhoneNumber;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Fax Number")
	protected String secMiCntFaxNumber;

	@Interchangeable(fieldTitle = "Activity Contacts")
	@VersionableCollection(fieldTitle = "Activity Contacts")
	protected Set<AmpActivityContact> activityContacts;


	@Interchangeable(fieldTitle = "Status Reason")
	@VersionableFieldTextEditor(fieldTitle = "Status Reason")
	protected String statusReason;
	
	@Interchangeable(fieldTitle = "Components")
	@VersionableCollection(fieldTitle = "Components")
	protected Set<AmpComponent> components;

	@Interchangeable(fieldTitle = "Structures")
	@VersionableCollection(fieldTitle = "Structures")
	protected Set<AmpStructure> structures;

	@Interchangeable(fieldTitle = "Component Fundings")
	@VersionableCollection(fieldTitle = "Component Fundings")
	protected Set<AmpComponentFunding> componentFundings;


	@Interchangeable(fieldTitle = "Proposed Start Date")
	@VersionableFieldSimple(fieldTitle = "Proposed Start Date")
	protected Date proposedStartDate;

	@Interchangeable(fieldTitle = "Actual Start Date")
	@VersionableFieldSimple(fieldTitle = "Actual Start Date")
	protected Date actualStartDate;
	
	@Interchangeable(fieldTitle = "Proposed Approval Date")
	@VersionableFieldSimple(fieldTitle = "Proposed Approval Date")
	protected Date proposedApprovalDate;
	
	@Interchangeable(fieldTitle = "Actual Approval Date")
	@VersionableFieldSimple(fieldTitle = "Actual Approval Date")
	protected Date actualApprovalDate;
	
	@Interchangeable(fieldTitle = "Actual Completion Date")
	@VersionableFieldSimple(fieldTitle = "Actual Completion Date")
	protected Date actualCompletionDate;
	
	@Interchangeable(fieldTitle = "Proposed Completion Date")
	@VersionableFieldSimple(fieldTitle = "Proposed Completion Date")
	protected Date proposedCompletionDate;

	// This field is defunct

	@Interchangeable(fieldTitle = "Created By")
	@VersionableFieldSimple(fieldTitle = "Created By", blockSingleChange = true)
	protected AmpTeamMember activityCreator;
	
	@Interchangeable(fieldTitle = "Creation Date")
	@VersionableFieldSimple(fieldTitle = "Creation Date", blockSingleChange = true)
	protected Date createdDate;
	
	@Interchangeable(fieldTitle = "Update Date")
	@VersionableFieldSimple(fieldTitle = "Update Date", blockSingleChange = true)
	protected Date updatedDate;

	@Interchangeable(fieldTitle = "Iati Last Update Date")
	@VersionableFieldSimple(fieldTitle = "Iati Last Update Date", blockSingleChange = true)
	protected Date iatiLastUpdatedDate;
	
	
	
	protected AmpTeamMember approvedBy;
	@Interchangeable(fieldTitle = "Approval Date")
	protected Date approvalDate;

	@Interchangeable(fieldTitle = "Regional Fundings")
	@VersionableCollection(fieldTitle = "Regional Fundings")
	protected Set regionalFundings;

	@Interchangeable(fieldTitle = "Approval Status")
	@VersionableFieldSimple(fieldTitle = "Approval Status", blockSingleChange = true)
	protected String approvalStatus;

	// Aid Harmonization Survey Set
	@Interchangeable(fieldTitle = "Surveys")
	@VersionableCollection(fieldTitle = "Surveys")
	protected Set<AmpAhsurvey> survey;
	
	@Interchangeable(fieldTitle = "GPI Surveys")
	@VersionableCollection(fieldTitle = "GPI Surveys")
	protected Set <AmpGPISurvey> gpiSurvey;

	@Interchangeable(fieldTitle = "Line Ministry Rank")
	@VersionableFieldSimple(fieldTitle = "Line Ministry Rank")
	protected Integer lineMinRank;

	protected Collection actRankColl;

	@Interchangeable(fieldTitle = "Archived")
	@VersionableFieldSimple(fieldTitle = "Archived")
	protected Boolean archived;

	@Interchangeable(fieldTitle = "Deleted")
	@VersionableFieldSimple(fieldTitle = "Deleted")
	protected Boolean deleted;
	//for AMP-14784
	@Interchangeable(fieldTitle = "Project Implementation Unit")
	@VersionableFieldSimple(fieldTitle = "Project Implementation Unit")
	protected String projectImplementationUnit;
	@Interchangeable(fieldTitle = "IMAC Approved")
	@VersionableFieldSimple(fieldTitle = "IMAC Approved")
	protected String imacApproved;
	@Interchangeable(fieldTitle = "National Oversight")
	@VersionableFieldSimple(fieldTitle = "National Oversight")	
	protected String nationalOversight;
	@Interchangeable(fieldTitle = "On Budget")
	@VersionableFieldSimple(fieldTitle = "On Budget")	
	protected String onBudget;
	@Interchangeable(fieldTitle = "On Parliament")
	@VersionableFieldSimple(fieldTitle = "On Parliament")	
	protected String onParliament;
	@Interchangeable(fieldTitle = "On Treasury")
	@VersionableFieldSimple(fieldTitle = "On Treasury")	
	protected String onTreasury;
	@Interchangeable(fieldTitle = "National Financial Management")
	@VersionableFieldSimple(fieldTitle = "National Financial Management")	
	protected String nationalFinancialManagement;
	@Interchangeable(fieldTitle = "National Procurement")
	@VersionableFieldSimple(fieldTitle = "National Procurement")	
	protected String nationalProcurement;
	@Interchangeable(fieldTitle = "National Audit")
	@VersionableFieldSimple(fieldTitle = "National Audit")	
	protected String nationalAudit;
	
	

	/**
	 * Indicator connections.
	 * This field contains {@link IndicatorActivity} beans which represent activity-indicator connections 
	 * and contain set of values for this connection.
	 * Please refer to AmpActivity.hbm.xml and IndicatorConnection.hbm.xml for details.
	 */
	@Interchangeable(fieldTitle = "Indicators")
	@VersionableCollection(fieldTitle = "Indicators")
	protected Set<IndicatorActivity> indicators;

	@Interchangeable(fieldTitle = "Activity Documents")
	@VersionableCollection(fieldTitle = "Activity Documents")
	protected Set<AmpActivityDocument> activityDocuments	= null;
	/* Categories */
	@Interchangeable(fieldTitle = "Categories")
	@VersionableCollection(fieldTitle = "Categories")
	protected Set<AmpCategoryValue> categories;

	/*
	 * Tanzania adds
	 */
	@Interchangeable(fieldTitle = "Indirect On Budget")
    @VersionableFieldSimple(fieldTitle = "Indirect On Budget")
    protected Boolean indirectOnBudget;
	@Interchangeable(fieldTitle = "FY")
	@VersionableFieldSimple(fieldTitle = "FY")
	protected String FY;
	@Interchangeable(fieldTitle = "Vote")
	@VersionableFieldSimple(fieldTitle = "Vote")
	protected String vote;
	@Interchangeable(fieldTitle = "Sub Vote")
	@VersionableFieldSimple(fieldTitle = "Sub Vote")
	protected String subVote;
	@Interchangeable(fieldTitle = "Sub Program")
	@VersionableFieldSimple(fieldTitle = "Sub Program")
	protected String subProgram;
	@Interchangeable(fieldTitle = "Project Code")
	@VersionableFieldSimple(fieldTitle = "Project Code")
	protected String projectCode;
	@Interchangeable(fieldTitle = "Ministry Code")
	@VersionableFieldSimple(fieldTitle = "Ministry Code")
	protected String ministryCode;

	
	@Interchangeable(fieldTitle = "CRIS Number")
	@VersionableFieldSimple(fieldTitle = "CRIS Number")
	protected String crisNumber;

	
	@Interchangeable(fieldTitle = "Government Approval Procedures")
	@VersionableFieldSimple(fieldTitle = "Government Approval Procedures")
	protected Boolean governmentApprovalProcedures;

	@Interchangeable(fieldTitle = "Joint Criteria")
	@VersionableFieldSimple(fieldTitle = "Joint Criteria")
	protected Boolean jointCriteria;
	@Interchangeable(fieldTitle = "Humanitarian Aid")
	@VersionableFieldSimple(fieldTitle = "Humanitarian Aid")
	protected Boolean humanitarianAid;

	@Interchangeable(fieldTitle = "Act. Programs")
	@VersionableCollection(fieldTitle = "Act. Programs")
	protected Set actPrograms;
	
	@Interchangeable(fieldTitle = "Act. Budget Structure")
	@VersionableCollection(fieldTitle = "Act. Budget Structure")
	protected Set actBudgetStructure;

	protected boolean createdAsDraft;
	


	/**
	 * Fields for activity versioning.
	 */
	protected AmpActivityGroup ampActivityGroup;
	
	
	protected Date modifiedDate;

	@Interchangeable(fieldTitle = "Modified By")
	@VersionableFieldSimple(fieldTitle = "Modified By")
	protected AmpTeamMember modifiedBy;
	
	protected Boolean mergedActivity;
	protected AmpActivityVersion mergeSource1;
	protected AmpActivityVersion mergeSource2;

	@Interchangeable(fieldTitle = CategoryConstants.FUNDING_SOURCES_NUMBER_NAME)
    @VersionableFieldSimple(fieldTitle = CategoryConstants.FUNDING_SOURCES_NUMBER_NAME)
    protected Integer fundingSourcesNumber;
    
	@Interchangeable(fieldTitle = "Proposed Project Life")
    @VersionableFieldSimple(fieldTitle = "Proposed Project Life")
    protected Integer proposedProjectLife;

	/**
	 * whether this is a PROJECT or a South-South Cooperation
	 */
    protected Long activityType = org.dgfoundation.amp.onepager.util.ActivityUtil.ACTIVITY_TYPE_PROJECT; //default type

    @Interchangeable(fieldTitle = "Annual Project Budgets")
    @VersionableCollection(fieldTitle = "Annual Project Budgets")
	protected Set<AmpAnnualProjectBudget> annualProjectBudgets;
	//protected Set <AmpActivityContact> activityContacts;

	public Boolean getMergedActivity() {
		return mergedActivity;
	}

	public void setMergedActivity(Boolean mergedActivity) {
		this.mergedActivity = mergedActivity;
	}

	public AmpActivityVersion getMergeSource1() {
		return mergeSource1;
	}

	public void setMergeSource1(AmpActivityVersion mergeSource1) {
		this.mergeSource1 = mergeSource1;
	}

	public AmpActivityVersion getMergeSource2() {
		return mergeSource2;
	}

	public void setMergeSource2(AmpActivityVersion mergeSource2) {
		this.mergeSource2 = mergeSource2;
	}

	public Integer getBudget() {
		return budget;
	}

	public void setBudget(Integer budget) {
		this.budget = budget;
	}

	public boolean isCreatedAsDraft() {
		return createdAsDraft;
	}

	public void setCreatedAsDraft(boolean createdAsDraft) {
		this.createdAsDraft = createdAsDraft;
	}

	public Boolean isGovernmentApprovalProcedures() {
		return governmentApprovalProcedures;
	}

	public void setGovernmentApprovalProcedures(
			Boolean governmentApprovalProcedures) {
		this.governmentApprovalProcedures = governmentApprovalProcedures;
	}

	public Boolean isJointCriteria() {
		return jointCriteria;
	}

	public void setJointCriteria(Boolean jointCriteria) {
		this.jointCriteria = jointCriteria;
	}

	public Set<AmpCategoryValue> getCategories() {
		return categories;
	}

	public void setCategories(Set<AmpCategoryValue> categories) {
		this.categories = categories;
	}

	public Set<AmpActivityDocument> getActivityDocuments() {
		return activityDocuments;
	}

	public void setActivityDocuments(Set<AmpActivityDocument> activityDocuments) {
		this.activityDocuments = activityDocuments;
	}

	public Set getCosts() {
		return costs;
	}

	public void setCosts(Set costs) {
		this.costs = costs;
	}

	/**
	 * @return
	 */
	public String getAmpId() {
		return ampId;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public Set<AmpFunding> getFunding() {
		return funding;
	}



	/**
	 * @return
	 */
	public Set getInternalIds() {
		return internalIds;
	}

	/**
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	public Set<AmpActivityLocation> getLocations() {
		return locations;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getObjective() {
		return objective;
	}

	/**
	 * @return
	 */
	public Set<AmpOrgRole> getOrgrole() {
		return orgrole;
	}

	/**
	 * @return
	 */
	public Set getSectors() {
		return sectors;
	}

	public Set<AmpIssues> getIssues() {
		return issues;
	}

	/**
	 * @param string
	 */
	public void setAmpId(String string) {
		ampId = string;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param set
	 */
	public void setFunding(Set set) {
		funding = set;
	}


	/**
	 * @param set
	 */
	public void setInternalIds(Set set) {
		internalIds = set;
	}

	public void setIssues(Set set) {
		issues = set;
	}

	/**
	 * @param string
	 */
	public void setLanguage(String string) {
		language = string;
	}

	/**
	 * @param level
	 */
	//public void setLevel(AmpLevel level) { // TO BE DELETED
	//this.level = level;
	//}

	/**
	 * @param set
	 */
	public void setLocations(Set<AmpActivityLocation> set) {
		locations = set;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setObjective(String string) {
		objective = string;
	}

	/**
	 * @param set
	 */
	public void setOrgrole(Set<AmpOrgRole> set) {
		orgrole = set;
	}


	/**
	 * @param set
	 */
	public void setSectors(Set set) {
		sectors = set;
	}

	/**
	 * @param status
	 */
	//public void setStatus(AmpStatus status) { // TO BE DELETED
	//this.status = status;
	//}



	/**
	 * @return
	 */
	public Long getAmpActivityId() {
		return ampActivityId;
	}

	/**
	 * @param long1
	 */
	public void setAmpActivityId(Long long1) {
		ampActivityId = long1;
	}

	/**
	 * @return
	 */
	public Set getProgress() {
		return progress;
	}

	/**
	 * @return
	 */
	@Deprecated
	public Set getDocuments() {
		return documents;
	}

	/**
	 * @param string
	 */
	public void setProgress(Set progress) {
		this.progress = progress;
	}

	/**
	 * @param string
	 */
	public void setDocuments(Set documents) {
		this.documents = documents;
	}

	public AmpTeam getTeam() {
		return team;
	}

	public void setTeam(AmpTeam team) {
		this.team = team;
	}

	public Set getMember() {
		return member;
	}

	public void setMember(Set member) {
		this.member = member;
	}
	/**
	 * @return
	 */
	public Date getOriginalCompDate() {
		return originalCompDate;
	}

	/**
	 * @param date
	 */
	public void setOriginalCompDate(Date date) {
		originalCompDate = date;
	}






	public int compareTo(AmpActivityVersion act) {
		// if (!(o instanceof AmpActivity)) throw new ClassCastException();
		//
		// AmpActivityVersion act = (AmpActivityVersion) o;

		//Added cos this method is comparing by Names, 
		//but many activities have NULL names and running script to correct records only once is temporary solution.
		String myName=(this.name==null)?"":this.name;
		String hisName=(act.getName()==null)?"":act.getName();
		return (myName.trim().toLowerCase().compareTo(hisName.trim().toLowerCase()));

	}

	public Set<AmpActivityContact> getActivityContacts() {
		return activityContacts;
	}

	public void setActivityContacts(Set<AmpActivityContact> activityContacts) {
		this.activityContacts = activityContacts;
	}

	/**
	 * @return Returns the statusReason.
	 */
	public String getStatusReason() {
		return statusReason;
	}
	/**
	 * @param statusReason
	 *            The statusReason to set.
	 */
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	/**
	 * @return Returns the components.
	 */
	public Set<AmpComponent> getComponents() {
		return components;
	}
	/**
	 * @param components
	 *            The components to set.
	 */
	public void setComponents(Set<AmpComponent> components) {
		this.components = components;
	}
	/**
	 * @return Returns the structures.
	 */
	public Set<AmpStructure> getStructures() {
		return structures;
	}
	/**
	 * @param structures
	 *            The structures to set.
	 */
	public void setStructures(Set<AmpStructure> structures) {
		this.structures = structures;
	}

	/**
	 * @return Returns the actualApprovalDate.
	 */
	public Date getActualApprovalDate() {
		return actualApprovalDate;
	}
	/**
	 * @param actualApprovalDate
	 *            The actualApprovalDate to set.
	 */
	public void setActualApprovalDate(Date actualApprovalDate) {
		this.actualApprovalDate = actualApprovalDate;
	}
	/**
	 * @return Returns the actualCompletionDate.
	 */
	public Date getActualCompletionDate() {
		return actualCompletionDate;
	}
	/**
	 * @param actualCompletionDate
	 *            The actualCompletionDate to set.
	 */
	public void setActualCompletionDate(Date actualCompletionDate) {
		this.actualCompletionDate = actualCompletionDate;
	}
	/**
	 * @return Returns the actualStartDate.
	 */
	public Date getActualStartDate() {
		return actualStartDate;
	}
	/**
	 * @param actualStartDate
	 *            The actualStartDate to set.
	 */
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	/**
	 * @return Returns the proposedApprovalDate.
	 */
	public Date getProposedApprovalDate() {
		return proposedApprovalDate;
	}
	/**
	 * @param proposedApprovalDate
	 *            The proposedApprovalDate to set.
	 */
	public void setProposedApprovalDate(Date proposedApprovalDate) {
		this.proposedApprovalDate = proposedApprovalDate;
	}
	/**
	 * @return Returns the proposedStartDate.
	 */
	public Date getProposedStartDate() {
		return proposedStartDate;
	}
	/**
	 * @param proposedStartDate
	 *            The proposedStartDate to set.
	 */
	public void setProposedStartDate(Date proposedStartDate) {
		this.proposedStartDate = proposedStartDate;
	}
	/**
	 * @return Returns the createdDate.
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate
	 *            The createdDate to set.
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return Returns the updatedDate.
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate
	 *            The updatedDate to set.
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return Returns the contFirstName.
	 */
	public String getContFirstName() {
		return contFirstName;
	}

	/**
	 * @param contFirstName
	 *            The contFirstName to set.
	 */
	public void setContFirstName(String contFirstName) {
		this.contFirstName = contFirstName;
	}

	/**
	 * @return Returns the contLastName.
	 */
	public String getContLastName() {
		return contLastName;
	}

	/**
	 * @param contLastName
	 *            The contLastName to set.
	 */
	public void setContLastName(String contLastName) {
		this.contLastName = contLastName;
	}
	/**
	 * @return Returns the programDescription.
	 */
	public String getProgramDescription() {
		return programDescription;
	}
	/**
	 * @param programDescription
	 *            The programDescription to set.
	 */
	public void setProgramDescription(String programDescription) {
		this.programDescription = programDescription;
	}

	/**
	 * @return Returns the activityCreator.
	 */
	public AmpTeamMember getActivityCreator() {
		return activityCreator;
	}

	/**
	 * @param activityCreator
	 *            The activityCreator to set.
	 */
	public void setActivityCreator(AmpTeamMember activityCreator) {
		this.activityCreator = activityCreator;
	}
	/**
	 * @return Returns the mofedCntEmail.
	 */
	public String getMofedCntEmail() {
		return mofedCntEmail;
	}
	/**
	 * @param mofedCntEmail
	 *            The mofedCntEmail to set.
	 */
	public void setMofedCntEmail(String mofedCntEmail) {
		this.mofedCntEmail = mofedCntEmail;
	}
	/**
	 * @return Returns the mofedCntFirstName.
	 */
	public String getMofedCntFirstName() {
		return mofedCntFirstName;
	}
	/**
	 * @param mofedCntFirstName
	 *            The mofedCntFirstName to set.
	 */
	public void setMofedCntFirstName(String mofedCntFirstName) {
		this.mofedCntFirstName = mofedCntFirstName;
	}
	/**
	 * @return Returns the mofedCntLastName.
	 */
	public String getMofedCntLastName() {
		return mofedCntLastName;
	}
	/**
	 * @param mofedCntLastName
	 *            The mofedCntLastName to set.
	 */
	public void setMofedCntLastName(String mofedCntLastName) {
		this.mofedCntLastName = mofedCntLastName;
	}

	// Commented by Mikheil - in general, Hibernate classes do not need to
	// overrride
	//this method, because it may lead to incorrect functinoality
	/*
	 * public boolean equals(Object obj) { if (obj == null) throw new
	 * NullPointerException();
	 *
	 * if (!(obj instanceof AmpActivity)) throw new ClassCastException();
	 *
	 * AmpActivityVersion act = (AmpActivityVersion) obj; return
	 * this.ampActivityId.equals(act.getAmpActivityId()); }
	 */

	/**
	 * @return Returns the regionalFundings.
	 */
	public Set getRegionalFundings() {
		return regionalFundings;
	}

	/**
	 * @param regionalFundings
	 *            The regionalFundings to set.
	 */
	public void setRegionalFundings(Set regionalFundings) {
		this.regionalFundings = regionalFundings;
	}



	/**
	 * @return Returns the approvalStatus.
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}
	/**
	 * @param approval_status
	 *            The approval_status to set.
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	/**
	 * @return Returns the survey.
	 */
	public Set<AmpAhsurvey> getSurvey() {
		return survey;
	}

	public String getDocumentSpace() {
		return documentSpace;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public Double getFunAmount() {
		return FeaturesUtil.applyThousandsForVisibility(funAmount);
	}

	public Date getFunDate() {
		return funDate;
	}

	/**
	 * @param survey
	 *            The survey to set.
	 */
	public void setSurvey(Set<AmpAhsurvey> survey) {
		this.survey = survey;
	}

	public void setDocumentSpace(String documentSpace) {
		this.documentSpace = documentSpace;
	}

	public void setCurrencyCode(String currenyCode) {
		this.currencyCode = currenyCode;
	}

	public void setFunAmount(Double funAmount) {
		this.funAmount = FeaturesUtil.applyThousandsForEntry(funAmount);
	}

	public void setFunDate(Date funDate) {
		this.funDate = funDate;
	}

	public Integer getLineMinRank() {
		return lineMinRank;
	}

	public void setLineMinRank(Integer lineMinRank) {
		this.lineMinRank = lineMinRank;
	}

	public Collection getActRankColl() {
		return actRankColl;
	}

	public Date getProposedCompletionDate() {
		return proposedCompletionDate;
	}

	public void setActRankColl(Collection actRankColl) {
		this.actRankColl = actRankColl;
	}

	public void setProposedCompletionDate(Date proposedCompletionDate) {
		this.proposedCompletionDate = proposedCompletionDate;
	}

	public Date getContractingDate() {
		return contractingDate;
	}
	public void setContractingDate(Date contractingDate) {
		this.contractingDate = contractingDate;
	}

	public Date getDisbursmentsDate() {
		return disbursmentsDate;
	}

	public void setDisbursmentsDate(Date disbursmentsDate) {
		this.disbursmentsDate = disbursmentsDate;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getDnrCntFaxNumber() {
		return dnrCntFaxNumber;
	}

	public void setDnrCntFaxNumber(String dnrCntFaxNumber) {
		this.dnrCntFaxNumber = dnrCntFaxNumber;
	}

	public String getDnrCntOrganization() {
		return dnrCntOrganization;
	}

	public void setDnrCntOrganization(String dnrCntOrganization) {
		this.dnrCntOrganization = dnrCntOrganization;
	}

	public String getDnrCntPhoneNumber() {
		return dnrCntPhoneNumber;
	}

	public void setDnrCntPhoneNumber(String dnrCntPhoneNumber) {
		this.dnrCntPhoneNumber = dnrCntPhoneNumber;
	}

	public String getDnrCntTitle() {
		return dnrCntTitle;
	}

	public void setDnrCntTitle(String dnrCntTitle) {
		this.dnrCntTitle = dnrCntTitle;
	}

	public String getMfdCntFaxNumber() {
		return mfdCntFaxNumber;
	}

	public void setMfdCntFaxNumber(String mfdCntFaxNumber) {
		this.mfdCntFaxNumber = mfdCntFaxNumber;
	}

	public String getMfdCntOrganization() {
		return mfdCntOrganization;
	}

	public void setMfdCntOrganization(String mfdCntOrganization) {
		this.mfdCntOrganization = mfdCntOrganization;
	}

	public String getMfdCntPhoneNumber() {
		return mfdCntPhoneNumber;
	}

	public void setMfdCntPhoneNumber(String mfdCntPhoneNumber) {
		this.mfdCntPhoneNumber = mfdCntPhoneNumber;
	}

	public String getMfdCntTitle() {
		return mfdCntTitle;
	}

	public void setMfdCntTitle(String mfdCntTitle) {
		this.mfdCntTitle = mfdCntTitle;
	}

	public Object getObjectType() {
		return this.getClass().getName();
	}

	public Object getIdentifier() {
		return this.getAmpActivityId();
	}

	public String getObjectName() {
		return this.getAmpId()+" "+this.getName();
	}
        @Override
        public String getObjectFilteredName() {
		return DbUtil.filter(getObjectName());
	}

	public String getFY() {
		return FY;
	}

	public void setFY(String fy) {
		FY = fy;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getSubProgram() {
		return subProgram;
	}

	public void setSubProgram(String subProgram) {
		this.subProgram = subProgram;
	}

	public String getSubVote() {
		return subVote;
	}

	public void setSubVote(String subVote) {
		this.subVote = subVote;
	}

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	public String getMinistryCode() {
		return ministryCode;
	}

	public void setMinistryCode(String ministryCode) {
		this.ministryCode = ministryCode;
	}

	public AmpTeamMember getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(AmpTeamMember createdBy) {
		this.createdBy = createdBy;
	}

	public String getLessonsLearned() {
		return lessonsLearned;
	}

	public void setLessonsLearned(String lessonsLearned) {
		this.lessonsLearned = lessonsLearned;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getEqualOpportunity() {
		return equalOpportunity;
	}

	public void setEqualOpportunity(String equalOpportunity) {
		this.equalOpportunity = equalOpportunity;
	}

	public String getMinorities() {
		return minorities;
	}

	public void setMinorities(String minorities) {
		this.minorities = minorities;
	}

	@Override
	public String[] getImplementedActions() {
		return IMPLEMENTED_ACTIONS;
	}

	@Override
	public Class getPermissibleCategory() {
		return AmpActivityVersion.class;
	}

	public Set getReferenceDocs() {
		return referenceDocs;
	}

	public Boolean getDraft() {
		return draft;
	}

	public Set getActPrograms() {
		return actPrograms;
	}
	
	public Set getActBudgetStructure(){
		return actBudgetStructure;
	}
	
	public void setActBudgetStructure(Set actBudgetStructure){
		this.actBudgetStructure = actBudgetStructure;
	}

	public void setReferenceDocs(Set referenceDocs) {
		this.referenceDocs = referenceDocs;
	}

	public void setDraft(Boolean draft) {
		this.draft = draft;
	}

	public void setActPrograms(Set actPrograms) {
		this.actPrograms = actPrograms;
	}

	public Boolean getGovernmentApprovalProcedures() {
		return governmentApprovalProcedures;
	}

	public Boolean getJointCriteria() {
		return jointCriteria;
	}

	public Set<IndicatorActivity> getIndicators() {
		return indicators;
	}

	public void setIndicators(Set<IndicatorActivity> indicators) {
		this.indicators = indicators;
	}

	public String getGovAgreementNumber() {
		return govAgreementNumber;
	}

	public void setGovAgreementNumber(String govAgreementNumber) {
		this.govAgreementNumber = govAgreementNumber;
	}


	public String getProjectImpact() {
		return projectImpact;
	}

	public void setProjectImpact(String projectImpact) {
		this.projectImpact = projectImpact;
	}

	public String getActivitySummary() {
		return activitySummary;
	}

	public void setActivitySummary(String activitySummary) {
		this.activitySummary = activitySummary;
	}


	public String getConditionality() {
		return conditionality;
	}

	public void setConditionality(String conditionality) {
		this.conditionality = conditionality;
	}

	public String getProjectManagement() {
		return projectManagement;
	}

	public void setProjectManagement(String projectManagement) {
		this.projectManagement = projectManagement;
	}
	public String getPrjCoFirstName() {
		return prjCoFirstName;
	}

	public void setPrjCoFirstName(String prjCoFirstName) {
		this.prjCoFirstName = prjCoFirstName;
	}

	public String getPrjCoLastName() {
		return prjCoLastName;
	}

	public void setPrjCoLastName(String prjCoLastName) {
		this.prjCoLastName = prjCoLastName;
	}

	public String getPrjCoEmail() {
		return prjCoEmail;
	}

	public void setPrjCoEmail(String prjCoEmail) {
		this.prjCoEmail = prjCoEmail;
	}

	public String getPrjCoTitle() {
		return prjCoTitle;
	}

	public void setPrjCoTitle(String prjCoTitle) {
		this.prjCoTitle = prjCoTitle;
	}

	public String getPrjCoOrganization() {
		return prjCoOrganization;
	}

	public void setPrjCoOrganization(String prjCoOrganization) {
		this.prjCoOrganization = prjCoOrganization;
	}

	public String getPrjCoPhoneNumber() {
		return prjCoPhoneNumber;
	}

	public void setPrjCoPhoneNumber(String prjCoPhoneNumber) {
		this.prjCoPhoneNumber = prjCoPhoneNumber;
	}

	public String getPrjCoFaxNumber() {
		return prjCoFaxNumber;
	}

	public void setPrjCoFaxNumber(String prjCoFaxNumber) {
		this.prjCoFaxNumber = prjCoFaxNumber;
	}

	public String getSecMiCntFirstName() {
		return secMiCntFirstName;
	}

	public void setSecMiCntFirstName(String secMiCntFirstName) {
		this.secMiCntFirstName = secMiCntFirstName;
	}

	public String getSecMiCntLastName() {
		return secMiCntLastName;
	}

	public void setSecMiCntLastName(String secMiCntLastName) {
		this.secMiCntLastName = secMiCntLastName;
	}

	public String getSecMiCntEmail() {
		return secMiCntEmail;
	}

	public void setSecMiCntEmail(String secMiCntEmail) {
		this.secMiCntEmail = secMiCntEmail;
	}

	public String getSecMiCntTitle() {
		return secMiCntTitle;
	}

	public void setSecMiCntTitle(String secMiCntTitle) {
		this.secMiCntTitle = secMiCntTitle;
	}

	public String getSecMiCntOrganization() {
		return secMiCntOrganization;
	}

	public void setSecMiCntOrganization(String secMiCntOrganization) {
		this.secMiCntOrganization = secMiCntOrganization;
	}

	public String getSecMiCntPhoneNumber() {
		return secMiCntPhoneNumber;
	}

	public void setSecMiCntPhoneNumber(String secMiCntPhoneNumber) {
		this.secMiCntPhoneNumber = secMiCntPhoneNumber;
	}

	public String getSecMiCntFaxNumber() {
		return secMiCntFaxNumber;
	}

	public void setSecMiCntFaxNumber(String secMiCntFaxNumber) {
		this.secMiCntFaxNumber = secMiCntFaxNumber;
	}



	public String toString(){
		if(name!=null) return name;
		return super.toString();
	}


	public Boolean isHumanitarianAid( ) {
		return this.humanitarianAid ;
	}

	public void setHumanitarianAid(Boolean humanitarianAid) {
		this.humanitarianAid = humanitarianAid;
	}

	public Boolean getHumanitarianAid() {
		return humanitarianAid;
	}

	public AmpTeamMember getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(AmpTeamMember approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getCrisNumber() {
		return crisNumber;
	}

	public void setCrisNumber(String crisNumber) {
		this.crisNumber = crisNumber;
	}

	public Set getContracts() {
		return contracts;
	}

	public void setContracts(Set contracts) {
		this.contracts = contracts;
	}


	public String getDonors() {
		StringBuilder donors = new StringBuilder();
		if (this.getAmpActivityId() != null){
			Session session = PersistenceManager.getSession();
			String queryString = "select distinct donor from " + AmpFunding.class.getName() + " f inner join f.ampDonorOrgId donor inner join f.ampActivityId act ";
			queryString += " where act.ampActivityId=:activityId";
			Query qry = session.createQuery(queryString).setLong("activityId", this.getAmpActivityId());

			List<AmpOrganisation> organizations = qry.list();
			if (organizations != null && organizations.size() > 1) {
				Collections.sort(organizations, new Comparator<AmpOrganisation>() {
					public int compare(AmpOrganisation o1, AmpOrganisation o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
			}

			for (AmpOrganisation donor : organizations) {
				donors.append(donor.getName());
				donors.append(", ");
			}

			if (donors.length() > 1) {
				// remove last comma
				donors.setLength(donors.length() - 2);
			}
		}
		return donors.toString();
	}

	public String getBudgetCodeProjectID() {
		return budgetCodeProjectID;
	}

	public void setBudgetCodeProjectID(String budgetCodeProjectID) {
		this.budgetCodeProjectID = budgetCodeProjectID;
	}

	public String getProjectComments() {
		return this.projectComments;
	}

	public void setProjectComments(String projectComments) {
		this.projectComments = projectComments;
	}

	public void setComponentFundings(Set<AmpComponentFunding> componentFundings) {
		this.componentFundings = componentFundings;
	}

	public Set<AmpComponentFunding> getComponentFundings() {
		return componentFundings;
	}

	public AmpActivityGroup getAmpActivityGroup() {
		return ampActivityGroup;
	}

	public void setAmpActivityGroup(AmpActivityGroup ampActivityGroup) {
		this.ampActivityGroup = ampActivityGroup;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public AmpTeamMember getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(AmpTeamMember modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the archived
	 */
	public Boolean getArchived() {
		return archived;
	}

	/**
	 * @param archived the archived to set
	 */
	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public static String[] getIMPLEMENTED_ACTIONS() {
		return IMPLEMENTED_ACTIONS;
	}

	public static void setIMPLEMENTED_ACTIONS(String[] implemented_actions) {
		IMPLEMENTED_ACTIONS = implemented_actions;
	}

	public Long getBudgetsector() {
		return budgetsector;
	}

	public void setBudgetsector(Long budgetsector) {
		this.budgetsector = budgetsector;
	}

	public Long getBudgetorganization() {
		return budgetorganization;
	}

	public void setBudgetorganization(Long budgetorganization) {
		this.budgetorganization = budgetorganization;
	}

	public Long getBudgetdepartment() {
		return budgetdepartment;
	}

	public void setBudgetdepartment(Long budgetdepartment) {
		this.budgetdepartment = budgetdepartment;
	}

	public Long getBudgetprogram() {
		return budgetprogram;
	}

	public void setBudgetprogram(Long budgetprogram) {
		this.budgetprogram = budgetprogram;
	}

	public Set<AmpRegionalObservation> getRegionalObservations() {
		return regionalObservations;
	}

	public void setRegionalObservations(Set<AmpRegionalObservation> regionalObservations) {
		this.regionalObservations = regionalObservations;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	public Set<AmpLineMinistryObservation> getLineMinistryObservations() {
		return lineMinistryObservations;
	}

	public void setLineMinistryObservations(
			Set<AmpLineMinistryObservation> lineMinistryObservations) {
		this.lineMinistryObservations = lineMinistryObservations;
	}

    public Boolean getIndirectOnBudget() {
        return indirectOnBudget;
    }

    public void setIndirectOnBudget(Boolean indirectOnBudget) {
        this.indirectOnBudget = indirectOnBudget;
    }

    public Long getActivityType() {
        return activityType;
    }

    public void setActivityType(Long activityType) {
        this.activityType = activityType;
    }

    public Integer getFundingSourcesNumber() {
        return fundingSourcesNumber;
    }

    public void setFundingSourcesNumber(Integer fundingSourcesNumber) {
        this.fundingSourcesNumber = fundingSourcesNumber;
    }

    @Override
	public Object clone() throws CloneNotSupportedException {
		try {
			return (AmpActivityVersion) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	public Set <AmpGPISurvey> getGpiSurvey() {
		return gpiSurvey;
	}

	public void setGpiSurvey(Set <AmpGPISurvey> gpiSurvey) {
		this.gpiSurvey = gpiSurvey;
	}
	
	public Integer getProposedProjectLife() {
		return proposedProjectLife;
	}

	public void setProposedProjectLife(Integer proposedProjectLife) {
		this.proposedProjectLife = proposedProjectLife;
	}

	public Set<AmpAnnualProjectBudget> getAnnualProjectBudgets() {
		return annualProjectBudgets;
	}

	public void setAnnualProjectBudgets(
			Set<AmpAnnualProjectBudget> annualProjectBudgets) {
		this.annualProjectBudgets = annualProjectBudgets;
	}
	public Date getIatiLastUpdatedDate() {
		return iatiLastUpdatedDate;
	}

	public void setIatiLastUpdatedDate(Date iatiLastUpdatedDate) {
		this.iatiLastUpdatedDate = iatiLastUpdatedDate;
	}
    public String getProjectImplementationUnit() {
		return projectImplementationUnit;
	}

	public void setProjectImplementationUnit(String projectImplementationUnit) {
		this.projectImplementationUnit = projectImplementationUnit;
	}

	public String getImacApproved() {
		return imacApproved;
	}

	public void setImacApproved(String imacApproved) {
		this.imacApproved = imacApproved;
	}

	public String getNationalOversight() {
		return nationalOversight;
	}

	public void setNationalOversight(String nationalOversight) {
		this.nationalOversight = nationalOversight;
	}

	public String getOnBudget() {
		return onBudget;
	}

	public void setOnBudget(String onBudget) {
		this.onBudget = onBudget;
	}

	public String getOnParliament() {
		return onParliament;
	}

	public void setOnParliament(String onParliament) {
		this.onParliament = onParliament;
	}

	public String getOnTreasury() {
		return onTreasury;
	}

	public void setOnTreasury(String onTreasury) {
		this.onTreasury = onTreasury;
	}

	public String getNationalFinancialManagement() {
		return nationalFinancialManagement;
	}

	public void setNationalFinancialManagement(String nationalFinancialManagement) {
		this.nationalFinancialManagement = nationalFinancialManagement;
	}

	public String getNationalProcurement() {
		return nationalProcurement;
	}

	public void setNationalProcurement(String nationalProcurement) {
		this.nationalProcurement = nationalProcurement;
	}

	public String getNationalAudit() {
		return nationalAudit;
	}

	public void setNationalAudit(String nationalAudit) {
		this.nationalAudit = nationalAudit;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
