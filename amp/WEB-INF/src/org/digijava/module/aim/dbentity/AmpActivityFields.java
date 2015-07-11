package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.activityversioning.VersionableCollection;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldSimple;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
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

	protected AmpTeamMember createdBy;
	
	@Interchangeable(fieldTitle = "Project Impact",fmPath="/Activity Form/Identification/Project Impact")
	@VersionableFieldTextEditor(fieldTitle = "Project Impact")
	protected String projectImpact;
	
	@Interchangeable(fieldTitle = "Activity Summary",fmPath="/Activity Form/Identification/Activity Summary")
	@VersionableFieldTextEditor(fieldTitle = "Activity Summary")
	protected String activitySummary;
	
	@Interchangeable(fieldTitle = "Conditionalities",fmPath="/Activity Form/Identification/Conditionalities")
	@VersionableFieldTextEditor(fieldTitle = "Conditionality")
	protected String conditionality;
	
	@Interchangeable(fieldTitle = "Project Management",fmPath="/Activity Form/Identification/Project Management")
	@VersionableFieldTextEditor(fieldTitle = "Project Management")
	protected String projectManagement;

	
	//getter and setter never used
//	@Interchangeable(fieldTitle = "Activity Budget",fmPath="/Activity Form/Identification/Activity Budget", required="/Activity Form/Identification/Required Validator for Activity Budget")
	@VersionableFieldSimple(fieldTitle = "Activity Budget")
	protected Integer budget;
	
	@Interchangeable(fieldTitle = "Government Agreement Number",fmPath="/Activity Form/Identification/Government Agreement Number")
	@VersionableFieldSimple(fieldTitle = "Government Agreement Number")
	protected String govAgreementNumber;
	
	@Interchangeable(fieldTitle = "Budget Code Project ID",fmPath="/Activity Form/Identification/Budget Code Project ID")
	@VersionableFieldSimple(fieldTitle = "Budget Code Project ID")
	protected String budgetCodeProjectID;
	
	//getter and setter never used
	@Interchangeable(fieldTitle = "Budget Sector",fmPath="/Activity Form/Identification/Budget Classification")
	@VersionableFieldSimple(fieldTitle = "Budget Sector")
	protected Long budgetsector;
	
	//getter and setter never used
	@Interchangeable(fieldTitle = "Budget Organization",fmPath="/Activity Form/Identification/Budget Classification")
	@VersionableFieldSimple(fieldTitle = "Budget Organization")
	protected Long budgetorganization;
	
	@Interchangeable(fieldTitle = "Budget Department",fmPath="/Activity Form/Identification/Budget Classification")
	@VersionableFieldSimple(fieldTitle = "Budget Department")
	protected Long budgetdepartment;
	
	
	//getter and setter never used
	@Interchangeable(fieldTitle = "Budget Program",fmPath="/Activity Form/Identification/Budget Classification")
	@VersionableFieldSimple(fieldTitle = "Budget Program")
	protected Long budgetprogram;

	//protected String govAgreementNumber;

	@Interchangeable(fieldTitle = ActivityFieldsConstants.AMP_ACTIVITY_ID, importable=false)
	@PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_ID})
	@VersionableFieldSimple(fieldTitle = "Internal ID", blockSingleChange = true)
	protected Long ampActivityId ;

	@Interchangeable(fieldTitle = ActivityFieldsConstants.AMP_ID, required="_ALWAYS_" ,importable=false)
	@VersionableFieldSimple(fieldTitle = "AMP Id", blockSingleChange = true)
	protected String ampId ;


	@Interchangeable(fieldTitle = ActivityFieldsConstants.PROJECT_TITLE,fmPath="/Activity Form/Identification/Project Title", required = "_ALWAYS_")
	@PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_LABEL})
	@VersionableFieldSimple(fieldTitle = "Name", mandatoryForSingleChange = true)
	@TranslatableField
	protected String name ;
	
	@Interchangeable(fieldTitle = "Activity Description",fmPath="/Activity Form/Identification/Description")
	@VersionableFieldTextEditor(fieldTitle = "Activity Description")
	protected String description ;

	@Interchangeable(fieldTitle = "Project Comments",fmPath="/Activity Form/Identification/Project Comments")
	@VersionableFieldTextEditor(fieldTitle = "Project Comments")
	protected String projectComments ;
	
	@Interchangeable(fieldTitle = "Lessons Learned",fmPath="/Activity Form/Identification/Lessons Learned")
	@VersionableFieldTextEditor(fieldTitle = "Lessons Learned")
	protected String lessonsLearned;
	
	@Interchangeable(fieldTitle = "Objective",fmPath="/Activity Form/Identification/Objective")
	@VersionableFieldTextEditor(fieldTitle = "Objective")
	protected String objective ;
	
	@Interchangeable(fieldTitle = "Purpose",fmPath="/Activity Form/Identification/Purpose")
	@VersionableFieldTextEditor(fieldTitle = "Purpose")
	protected String purpose;
	
	@Interchangeable(fieldTitle = "Results",fmPath="/Activity Form/Identification/Results")
	@VersionableFieldTextEditor(fieldTitle = "Results")
	protected String results;
	
	@Interchangeable(fieldTitle = "Document Space")
	@VersionableFieldSimple(fieldTitle = "Document Space")
	protected String documentSpace;


	@Interchangeable(fieldTitle = ActivityFieldsConstants.IS_DRAFT, required="_ALWAYS_",importable=false)
	@VersionableFieldSimple(fieldTitle = "Is Draft?")
	protected Boolean draft;

    @VersionableFieldSimple(fieldTitle = ActivityFieldsConstants.CHANGE_TYPE)
    protected String changeType;

	@Interchangeable(fieldTitle = "Equal Oportunity",fmPath="/Activity Form/Cross Cutting Issues/Equal Opportunity")
	@VersionableFieldTextEditor(fieldTitle = "Equal Oportunity")
	protected String equalOpportunity;
	
	@Interchangeable(fieldTitle = "Environment",fmPath="/Activity Form/Cross Cutting Issues/Environment")
	@VersionableFieldTextEditor(fieldTitle = "Environment")
	protected String environment;
	
	@Interchangeable(fieldTitle = "Minorities",fmPath="/Activity Form/Cross Cutting Issues/Minorities")
	@VersionableFieldTextEditor(fieldTitle = "Minorities")
	protected String minorities;

	@VersionableFieldSimple(fieldTitle = "Language")
	protected String language ;

	@Interchangeable(fieldTitle = "Original Date",fmPath="/Activity Form/Planning/Original Completion Date", required="/Activity Form/Planning/Required Validator for Original Completion Date")
	@VersionableFieldSimple(fieldTitle = "Original Date")
	protected Date originalCompDate;
	
	@Interchangeable(fieldTitle = "Contracting Date",fmPath="/Activity Form/Planning/Final Date for Contracting")
	@VersionableFieldSimple(fieldTitle = "Contracting Date")
	protected Date contractingDate;
	
	@Interchangeable(fieldTitle = "Disbursement Date",fmPath="/Activity Form/Planning/Final Date for Disbursements")
	@VersionableFieldSimple(fieldTitle = "Disbursement Date")
	protected Date disbursmentsDate;
	
	@Interchangeable(fieldTitle = "Sectors", fmPath = "/Activity Form/Sectors")
	@VersionableCollection(fieldTitle = "Sectors")
	@InterchangeableDiscriminator(discriminatorField = "classificationConfig.name", settings = {
			@Interchangeable(fieldTitle = "Primary Sectors", discriminatorOption = "Primary", fmPath = "/Activity Form/Sectors/Primary Sectors"),
			@Interchangeable(fieldTitle = "Secondary Sectors", discriminatorOption = "Secondary", fmPath = "/Activity Form/Sectors/Secondary Sectors"),
			@Interchangeable(fieldTitle = "Tertiary Sectors", discriminatorOption = "Tertiary", fmPath = "/Activity Form/Sectors/Tertiary Sectors")}, validators = {
			@Validators(minSize = "/Activity Form/Sectors/Primary Sectors/minSizeSectorsValidator", percentage = "/Activity Form/Sectors/Primary Sectors/sectorPercentageTotal", unique = "/Activity Form/Sectors/Primary Sectors/uniqueSectorsValidator"),
			@Validators(minSize = "/Activity Form/Sectors/Secondary Sectors/minSizeSectorsValidator", percentage = "/Activity Form/Sectors/Secondary Sectors/sectorPercentageTotal", unique = "/Activity Form/Sectors/Secondary Sectors/uniqueSectorsValidator"),
			@Validators(minSize = "/Activity Form/Sectors/Tertiary Sectors/minSizeSectorsValidator", percentage = "/Activity Form/Sectors/Tertiary Sectors/sectorPercentageTotal", unique = "/Activity Form/Sectors/Tertiary Sectors/uniqueSectorsValidator")})
	protected Set <AmpActivitySector> sectors ;

	@Interchangeable(fieldTitle = "Contracts",fmPath="/Activity Form/Contracts")
	@VersionableCollection(fieldTitle = "Contracts")
	protected Set<IPAContract> contracts;
	
	@Interchangeable(fieldTitle = "Locations",fmPath="/Activity Form/Location",required="/Activity Form/Location/Locations/Location required validator")
	@Validators (unique="/Activity Form/Location/Locations/uniqueLocationsValidator")
	@VersionableCollection(fieldTitle = "Locations")
	protected Set<AmpActivityLocation> locations ;
	
	@Interchangeable(fieldTitle = "Org. Role",fmPath="/Activity Form/Funding/Org Role")
	@VersionableCollection(fieldTitle = "Org. Role")
	protected Set<AmpOrgRole> orgrole;
	
	@Interchangeable(fieldTitle = "Organizations Project IDs",fmPath="/Activity Form/Activity Internal IDs")
	@VersionableCollection(fieldTitle = "Internal IDs")
	protected Set<AmpActivityInternalId> internalIds ;
	
	@Interchangeable(fieldTitle = "Fundings",fmPath="/Activity Form/Funding")
	@VersionableCollection(fieldTitle = "Fundings")
	protected Set<AmpFunding> funding ;
	
	//TODO show this field?
	/*seems obsolete*/
	@VersionableCollection(fieldTitle = "Progress")
	protected Set progress;
	
	/**
	 * Old, not used anymore
	 * @deprecated
	 */
	@Deprecated
	protected Set documents ;
	
	@Interchangeable(fieldTitle = "Issues",fmPath="/Activity Form/Issues Section")
	@VersionableCollection(fieldTitle = "Issues")
	protected Set<AmpIssues> issues;

	@Interchangeable(fieldTitle = "Regional Observations",fmPath="/Activity Form/Regional Observations")
	@VersionableCollection(fieldTitle = "Regional Observations")
	protected Set<AmpRegionalObservation> regionalObservations;

	@Interchangeable(fieldTitle = "Line Ministry Observations",fmPath="/Activity Form/Line Ministry Observations")
	@VersionableCollection(fieldTitle = "Line Ministry Observations")
	protected Set<AmpLineMinistryObservation> lineMinistryObservations;

	//seems obsolete
	@VersionableCollection(fieldTitle = "Costs")
	protected Set costs;
	
	@Interchangeable(fieldTitle = "Program Description",fmPath="/Activity Form/Program/Program Description")
	@VersionableFieldTextEditor(fieldTitle = "Program Description")
	protected String programDescription;
	
	@VersionableFieldSimple(fieldTitle = "Team")
	protected AmpTeam team;
	//@VersionableCollection(fieldTitle = "Members")
	protected Set member;

	//TODO never used. What we do on IATI?
	@Interchangeable(fieldTitle = "Contact Name") //slightly fishy here
	protected String contactName;
	//protected AmpTeamMember updatedBy; !!! Use modifiedBy

	@Interchangeable(fieldTitle = "Fun Amount",fmPath="/Activity Form/Funding/Proposed Project Cost/Amount")
	@VersionableFieldSimple(fieldTitle = "Fun Amount")
	protected Double funAmount;
	
	@Interchangeable(fieldTitle = "Currency Code",fmPath="/Activity Form/Funding/Proposed Project Cost/Amount")
	@VersionableFieldSimple(fieldTitle = "Currency Code")
	protected String currencyCode;
	
	@Interchangeable(fieldTitle = "Fun Date",fmPath="/Activity Form/Funding/Proposed Project Cost/Date")
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
	@Interchangeable(fieldTitle = "Contact First Name",fmPath="/Activity Form/Contacts/Donor Contact Information/contact first name")
	protected String contFirstName;
	@Interchangeable(fieldTitle = "Contact Last Name",fmPath="/Activity Form/Contacts/Donor Contact Information/contact lastname")
	protected String contLastName;
	@Interchangeable(fieldTitle = "Email",fmPath="/Activity Form/Contacts/Donor Contact Information/Add Contact Email/Add Contact Email")
	protected String email;
	@Interchangeable(fieldTitle = "Donor Contact Title",fmPath="/Activity Form/Contacts/Donor Contact Information/Contact Title")
	protected String dnrCntTitle;
	@Interchangeable(fieldTitle = "Donor Contact Organization",fmPath="/Activity Form/Contacts/Donor Contact Information/Contact Organizations")
	protected String dnrCntOrganization;
	@Interchangeable(fieldTitle = "Donor Contact Phone Number",fmPath="/Activity Form/Contacts/Donor Contact Information/Add Contact Phone")
	protected String dnrCntPhoneNumber;
	@Interchangeable(fieldTitle = "Donor Contact Fax Number",fmPath="/Activity Form/Contacts/Donor Contact Information/Add Contact Fax/Add Contact Fax")
	protected String dnrCntFaxNumber;

	// MOFED contact information
	@Interchangeable(fieldTitle = "MOFED Contact First Name",fmPath="/Activity Form/Contacts/Mofed Contact Information/contact first name")
	protected String mofedCntFirstName;
	@Interchangeable(fieldTitle = "MOFED Contact Last Name",fmPath="/Activity Form/Contacts/Mofed Contact Information/contact lastname")
	protected String mofedCntLastName;
	@Interchangeable(fieldTitle = "MOFED Contact Email",fmPath="/Activity Form/Contacts/Mofed Contact Information/Add Contact Email/Add Contact Email")
	protected String mofedCntEmail;
	@Interchangeable(fieldTitle = "MOFED Contact Title",fmPath="/Activity Form/Contacts/Mofed Contact Information/Contact Title")
	protected String mfdCntTitle;
	@Interchangeable(fieldTitle = "MOFED Contact Organization",fmPath="/Activity Form/Contacts/Mofed Contact Information/Contact Organizations")
	protected String mfdCntOrganization;
	@Interchangeable(fieldTitle = "MOFED Contact Phone Number",fmPath="/Activity Form/Contacts/Mofed Contact Information/Add Contact Phone")
	protected String mfdCntPhoneNumber;
	@Interchangeable(fieldTitle = "MOFED Contact Fax Number",fmPath="/Activity Form/Contacts/Mofed Contact Information/Add Contact Fax")
	protected String mfdCntFaxNumber;

	// Project Coordinator contact information
	@Interchangeable(fieldTitle = "Project Coordinator First Name",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/contact first name")
	protected String prjCoFirstName;
	@Interchangeable(fieldTitle = "Project Coordinator Last Name",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/contact lastname")
	protected String prjCoLastName;
	@Interchangeable(fieldTitle = "Project Coordinator Email",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/Add Contact Email/Add Contact Email")
	protected String prjCoEmail;
	@Interchangeable(fieldTitle = "Project Coordinator Title",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/Contact Title")
	protected String prjCoTitle;
	@Interchangeable(fieldTitle = "Project Coordinator Organization",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/Contact Organizations")
	protected String prjCoOrganization;
	@Interchangeable(fieldTitle = "Project Coordinator Phone Number",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/Add Contact Phone")
	protected String prjCoPhoneNumber;
	@Interchangeable(fieldTitle = "Project Coordinator Fax Number",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/Add Contact Fax")
	protected String prjCoFaxNumber;

	// Sector Ministry contact information
	
	@Interchangeable(fieldTitle = "Sector Ministry Contact First Name",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/contact first name")
	protected String secMiCntFirstName;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Last Name",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/contact lastname")
	protected String secMiCntLastName;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Email",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/Add Contact Email/Add Contact Email")
	protected String secMiCntEmail;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Title",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/Contact Title")
	protected String secMiCntTitle;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Organization",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/Contact Organizations")
	protected String secMiCntOrganization;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Phone Number",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/Add Contact Phone")
	protected String secMiCntPhoneNumber;
	@Interchangeable(fieldTitle = "Sector Ministry Contact Fax Number",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/Add Contact Fax")
	protected String secMiCntFaxNumber;

	@Interchangeable(fieldTitle = "Activity Contacts",fmPath="/Activity Form/Contacts")
	@VersionableCollection(fieldTitle = "Activity Contacts")
	protected Set<AmpActivityContact> activityContacts;

	@Interchangeable(fieldTitle = "Status Reason",fmPath="/Activity Form/Identification/Status Reason")
	@VersionableFieldTextEditor(fieldTitle = "Status Reason")
	protected String statusReason;
	
	@Interchangeable(fieldTitle = "Components",fmPath="/Activity Form/Components")
	@VersionableCollection(fieldTitle = "Components")
	protected Set<AmpComponent> components;

	@Interchangeable(fieldTitle = "Structures",fmPath="/Activity Form/Structures")
	@VersionableCollection(fieldTitle = "Structures")
	protected Set<AmpStructure> structures;

	//TODO can be Component Commintment, Disbursement or Expenditures
	@Interchangeable(fieldTitle = "Component Fundings",fmPath="/Activity Form/Components")
	@VersionableCollection(fieldTitle = "Component Fundings")
//	DOES NOT NEED A DISCRIMINATOR! IT'S REACHABLE VIA AMPCATEGORYVALUES INSIDE AMPCOMPONENTFUNDING
//ALSO, IS NOT A PREDEFINED LIST  
//	@InterchangeableDiscriminator(
//	        discriminatorField = "transactionType",
//	        settings = {
//	 @Interchangeable(fieldTitle ="Components Commitments",discriminatorOption = "0", fmPath="/Activity Form/Components/Component/Components Commitments"),
//	 @Interchangeable(fieldTitle ="Components Disbursements",discriminatorOption = "1", fmPath="/Activity Form/Components/Component/Components Disbursements"),
//	 @Interchangeable(fieldTitle ="Components Expenditures",discriminatorOption = "2", fmPath="/Activity Form/Components/Component/Components Expenditures"),
//	 }
//	)
	protected Set<AmpComponentFunding> componentFundings;


	@Interchangeable(fieldTitle = "Proposed Start Date",fmPath="/Activity Form/Planning/Proposed Start Date")
	@VersionableFieldSimple(fieldTitle = "Proposed Start Date")
	protected Date proposedStartDate;

	@Interchangeable(fieldTitle = "Actual Start Date",fmPath="/Activity Form/Planning/Actual Start Date")
	@VersionableFieldSimple(fieldTitle = "Actual Start Date")
	protected Date actualStartDate;
	
	@Interchangeable(fieldTitle = "Proposed Approval Date",fmPath="/Activity Form/Planning/Proposed Approval Date")
	@VersionableFieldSimple(fieldTitle = "Proposed Approval Date")
	protected Date proposedApprovalDate;
	
	@Interchangeable(fieldTitle = "Actual Approval Date",fmPath="/Activity Form/Planning/Actual Approval Date")
	@VersionableFieldSimple(fieldTitle = "Actual Approval Date")
	protected Date actualApprovalDate;
	
	@Interchangeable(fieldTitle = "Actual Completion Date",fmPath="/Activity Form/Planning/Actual Completion Date")
	@VersionableFieldSimple(fieldTitle = "Actual Completion Date")
	protected Date actualCompletionDate;
	
	@Interchangeable(fieldTitle = "Proposed Completion Date",fmPath="/Activity Form/Planning/Proposed Completion Date")
	@VersionableFieldSimple(fieldTitle = "Proposed Completion Date")
	protected Date proposedCompletionDate;



//	@Interchangeable(fieldTitle = "Created By")
	@VersionableFieldSimple(fieldTitle = "Created By", blockSingleChange = true)
	protected AmpTeamMember activityCreator;
	
	@Interchangeable(fieldTitle = ActivityFieldsConstants.CREATED_DATE, importable=false)
	@VersionableFieldSimple(fieldTitle = "Creation Date", blockSingleChange = true)
	protected Date createdDate;
	
	@Interchangeable(fieldTitle = ActivityFieldsConstants.UPDATE_DATE, importable=false)
	@VersionableFieldSimple(fieldTitle = "Update Date", blockSingleChange = true)
	protected Date updatedDate;

	@Interchangeable(fieldTitle = "Iati Last Update Date", importable=false)
	@VersionableFieldSimple(fieldTitle = "Iati Last Update Date", blockSingleChange = true)
	protected Date iatiLastUpdatedDate;
	
	
	
	protected AmpTeamMember approvedBy;
	
	@Interchangeable(fieldTitle = "Approval Date", importable=false)
	protected Date approvalDate;

	@Interchangeable(fieldTitle = "Regional Fundings",fmPath="/Activity Form/Regional Funding")
	@VersionableCollection(fieldTitle = "Regional Fundings")
	protected Set <AmpRegionalFunding> regionalFundings;

	@Interchangeable(fieldTitle = "Approval Status",importable=false)
	@InterchangeableDiscriminator(discriminatorField="approvalStatus", 
		discriminatorClass="org.digijava.kernel.ampapi.endpoints.activity.discriminators.ApprovalStatusDiscriminator")
	@VersionableFieldSimple(fieldTitle = "Approval Status", blockSingleChange = true)
	protected String approvalStatus;

	// Aid Harmonization Survey Set
	// @Interchangeable(fieldTitle = "Surveys",fmPath="/Activity Form/Paris Indicators")
	// @VersionableCollection(fieldTitle = "Surveys")
	protected Set<AmpAhsurvey> survey;
	
	// @Interchangeable(fieldTitle = "GPI Surveys",fmPath="/Activity Form/GPI")
	// @VersionableCollection(fieldTitle = "GPI Surveys")
	protected Set <AmpGPISurvey> gpiSurvey;

	@Interchangeable(fieldTitle = "Line Ministry Rank",fmPath="/Activity Form/Planning/Line Ministry Rank")
	@VersionableFieldSimple(fieldTitle = "Line Ministry Rank")
	protected Integer lineMinRank;

	protected Collection actRankColl;

	@Interchangeable(fieldTitle = "Archived", importable=false)
	@VersionableFieldSimple(fieldTitle = "Archived")
	protected Boolean archived;

	@Interchangeable(fieldTitle = "Deleted", importable=false)
	@VersionableFieldSimple(fieldTitle = "Deleted")
	protected Boolean deleted;
	//for AMP-14784
	@Interchangeable(fieldTitle = "Project Implementation Unit",fmPath="/Activity Form/Aid Effectivenes/Project uses parallel project implementation unit")
	@VersionableFieldSimple(fieldTitle = "Project Implementation Unit")
	protected String projectImplementationUnit;
	@Interchangeable(fieldTitle = "IMAC Approved",fmPath="/Activity Form/Aid Effectivenes/Project has been approved by IMAC")
	@VersionableFieldSimple(fieldTitle = "IMAC Approved")
	protected String imacApproved;
	@Interchangeable(fieldTitle = "National Oversight",fmPath="/Activity Form/Aid Effectivenes/Government is meber of project steering committee")
	@VersionableFieldSimple(fieldTitle = "National Oversight")	
	protected String nationalOversight;
	@Interchangeable(fieldTitle =  "On Budget",fmPath="/Activity Form/Aid Effectivenes/Project is on budget")
	@VersionableFieldSimple(fieldTitle = "On Budget")	
	protected String onBudget;
	@Interchangeable(fieldTitle = "On Parliament",fmPath="/Activity Form/Aid Effectivenes/Project is on parliament")
	@VersionableFieldSimple(fieldTitle = "On Parliament")	
	protected String onParliament;
	@Interchangeable(fieldTitle = "On Treasury",fmPath="/Activity Form/Aid Effectivenes/Project disburses directly into the Goverment single treasury account")
	@VersionableFieldSimple(fieldTitle = "On Treasury")	
	protected String onTreasury;
	@Interchangeable(fieldTitle = "National Financial Management",fmPath="/Activity Form/Aid Effectivenes/Project uses national financial management systems")
	@VersionableFieldSimple(fieldTitle = "National Financial Management")	
	protected String nationalFinancialManagement;
	@Interchangeable(fieldTitle = "National Procurement",fmPath="/Activity Form/Aid Effectivenes/Project uses national procurement systems")
	@VersionableFieldSimple(fieldTitle = "National Procurement")	
	protected String nationalProcurement;
	@Interchangeable(fieldTitle = "National Audit",fmPath="/Activity Form/Aid Effectivenes/Project uses national audit systems")
	@VersionableFieldSimple(fieldTitle = "National Audit")	
	protected String nationalAudit;
	
	

	/**
	 * Indicator connections.
	 * This field contains {@link IndicatorActivity} beans which represent activity-indicator connections 
	 * and contain set of values for this connection.
	 * Please refer to AmpActivity.hbm.xml and IndicatorConnection.hbm.xml for details.
	 */
	@Interchangeable(fieldTitle = "Indicators",fmPath="/Activity Form/M&E")
	@VersionableCollection(fieldTitle = "Indicators")
	@Validators (unique="/Activity Form/M&E/Unique MEs Validator")
	protected Set<IndicatorActivity> indicators;

	@Interchangeable(fieldTitle = "Activity Documents",fmPath="/Activity Form/Related Documents")
	@VersionableCollection(fieldTitle = "Activity Documents")
	protected Set<AmpActivityDocument> activityDocuments	= null;
	
	
	
	
	/* Categories */
	@Interchangeable(fieldTitle = "Categories")
	@InterchangeableDiscriminator(discriminatorField="categories", 
	settings = {
		@Interchangeable(fieldTitle = "Status", discriminatorOption = CategoryConstants.ACTIVITY_STATUS_KEY),
		@Interchangeable(fieldTitle = "Type of Cooperation", discriminatorOption = /*"SSC_" + */CategoryConstants.TYPE_OF_COOPERATION_KEY),
		@Interchangeable(fieldTitle = "Type of Implementation", discriminatorOption = /*"SSC_" + */CategoryConstants.TYPE_OF_IMPLEMENTATION_KEY),
		@Interchangeable(fieldTitle = "Modalities", discriminatorOption = /*"SSC_" + */CategoryConstants.MODALITIES_KEY),
		@Interchangeable(fieldTitle = "A.C. Chapter", discriminatorOption = CategoryConstants.ACCHAPTER_KEY), 
		@Interchangeable(fieldTitle = "Activity Budget", discriminatorOption = CategoryConstants.ACTIVITY_BUDGET_KEY), 
		@Interchangeable(fieldTitle = "Procurement System", discriminatorOption = CategoryConstants.PROCUREMENT_SYSTEM_KEY),
		@Interchangeable(fieldTitle = "Reporting System", discriminatorOption = CategoryConstants.REPORTING_SYSTEM_KEY), 
		@Interchangeable(fieldTitle = "Audit System", discriminatorOption = CategoryConstants.AUDIT_SYSTEM_KEY),
		@Interchangeable(fieldTitle = "Institutions", discriminatorOption = CategoryConstants.INSTITUTIONS_KEY),
		@Interchangeable(fieldTitle = "Project Implementing Unit", discriminatorOption = CategoryConstants.PROJECT_IMPLEMENTING_UNIT_KEY),
		@Interchangeable(fieldTitle = "Accession Instrument", discriminatorOption = CategoryConstants.ACCESSION_INSTRUMENT_KEY),
		@Interchangeable(fieldTitle = "Project Category", discriminatorOption = CategoryConstants.PROJECT_CATEGORY_KEY),
		@Interchangeable(fieldTitle = "Implementation Level", discriminatorOption = CategoryConstants.IMPLEMENTATION_LEVEL_KEY),
		@Interchangeable(fieldTitle = "Implementation Location", discriminatorOption = CategoryConstants.IMPLEMENTATION_LOCATION_KEY)
		
	})
	@VersionableCollection(fieldTitle = "Categories")
	protected Set<AmpCategoryValue> categories;

	/*
	 * Tanzania adds
	 */
	@Interchangeable(fieldTitle = "Indirect On Budget",fmPath="/Activity Form/Identification/Budget Extras/Indirect On Budget")
    @VersionableFieldSimple(fieldTitle = "Indirect On Budget")
    protected Boolean indirectOnBudget;
	@Interchangeable(fieldTitle = "FY",fmPath="/Activity Form/Identification/Budget Extras/FY")
	@VersionableFieldSimple(fieldTitle = "FY")
	protected String FY;
	@Interchangeable(fieldTitle = "Vote",fmPath="/Activity Form/Identification/Budget Extras/Vote")
	@VersionableFieldSimple(fieldTitle = "Vote")
	protected String vote;
	@Interchangeable(fieldTitle = "Sub Vote",fmPath="/Activity Form/Identification/Budget Extras/Sub-Vote")
	@VersionableFieldSimple(fieldTitle = "Sub Vote")
	protected String subVote;
	@Interchangeable(fieldTitle = "Sub Program",fmPath="/Activity Form/Identification/Budget Extras/Sub-Program")
	@VersionableFieldSimple(fieldTitle = "Sub Program")
	protected String subProgram;
	/* 
	 * AMP-20423: For Import/Export we need it always visible 
	 * */
	@Interchangeable(fieldTitle = ActivityFieldsConstants.PROJECT_CODE)
			//AMP-20423: disabling fmPath="/Activity Form/Identification/Budget Extras/Project Code")
	@VersionableFieldSimple(fieldTitle = "Project Code")
	protected String projectCode;
	@Interchangeable(fieldTitle = "Ministry Code",fmPath="/Activity Form/Identification/Budget Extras/Ministry Code")
	@VersionableFieldSimple(fieldTitle = "Ministry Code")
	protected String ministryCode;

	
	@Interchangeable(fieldTitle = "CRIS Number",fmPath="/Activity Form/Identification/Cris Number")
	@VersionableFieldSimple(fieldTitle = "CRIS Number")
	protected String crisNumber;

	
	@Interchangeable(fieldTitle = "Government Approval Procedures",fmPath="/Activity Form/Identification/Government Approval Procedures")
	@VersionableFieldSimple(fieldTitle = "Government Approval Procedures")
	protected Boolean governmentApprovalProcedures;

	@Interchangeable(fieldTitle = "Joint Criteria",fmPath="/Activity Form/Identification/Joint Criteria")
	@VersionableFieldSimple(fieldTitle = "Joint Criteria")
	protected Boolean jointCriteria;
	@Interchangeable(fieldTitle = "Humanitarian Aid",fmPath="/Activity Form/Identification/Humanitarian Aid")
	@VersionableFieldSimple(fieldTitle = "Humanitarian Aid")
	protected Boolean humanitarianAid;

	//Can be Primary, Secondary,Tertiary or National Plan Objective
	@Interchangeable(fieldTitle = "Act. Programs",fmPath="/Activity Form/Program")
	@VersionableCollection(fieldTitle = "Act. Programs")
	@InterchangeableDiscriminator(discriminatorField = "programSetting.name", settings = {
			@Interchangeable(fieldTitle = "National Plan Objective", discriminatorOption = "National Plan Objective", fmPath = "/Activity Form/Program/National Plan Objective"),
			@Interchangeable(fieldTitle = "Primary Programs", discriminatorOption = "Primary Program", fmPath = "/Activity Form/Program/Primary Programs"),
			@Interchangeable(fieldTitle = "Secondary Programs", discriminatorOption = "Secondary Program", fmPath = "/Activity Form/Program/Secondary Programs"),
			@Interchangeable(fieldTitle = "Tertiary Programs", discriminatorOption = "Tertiary Program", fmPath = "/Activity Form/Program/Tertiary Programs") }, validators = {
			@Validators(maxSize = "/Activity Form/Program/National Plan Objective/max Size Program Validator", minSize = "/Activity Form/Program/National Plan Objective/minSizeProgramValidator", unique = "/Activity Form/Program/National Plan Objective/uniqueProgramsValidator", percentage = "/Activity Form/Program/National Plan Objective/programPercentageTotal"),
			@Validators(maxSize = "/Activity Form/Program/Primary Programs/max Size Program Validator", minSize = "/Activity Form/Program/Primary Programs/minSizeProgramValidator", unique = "/Activity Form/Program/Primary Programs/uniqueProgramsValidator", percentage = "/Activity Form/Program/Primary Programs/programPercentageTotal"),
			@Validators(maxSize = "/Activity Form/Program/Secondary Programs/max Size Program Validator", minSize = "/Activity Form/Program/Secondary Programs/minSizeProgramValidator", unique = "/Activity Form/Program/Secondary Programs/uniqueProgramsValidator", percentage = "/Activity Form/Program/Secondary Programs/programPercentageTotal"),
			@Validators(maxSize = "/Activity Form/Program/Tertiary Programs/max Size Program Validator", minSize = "/Activity Form/Program/Tertiary Programs/minSizeProgramValidator", unique = "/Activity Form/Program/Tertiary Programs/uniqueProgramsValidator", percentage = "/Activity Form/Program/Tertiary Programs/programPercentageTotal"), })
	protected Set <AmpActivityProgram> actPrograms;
	
	//UPDATE_IT_AFTER
//	@Interchangeable(fieldTitle = "Act. Budget Structure",fmPath="/Activity Form/Budget Structure")
	//@Validators (unique ="/Activity Form/Budget Structure/Budget Structure/uniqueProgramsValidator", minSize="/Activity Form/Budget Structure/Budget Structure/minSizeProgramValidator")
	@VersionableCollection(fieldTitle = "Act. Budget Structure")
	protected Set <AmpActivityBudgetStructure> actBudgetStructure;

	protected boolean createdAsDraft;
	


	/**
	 * Fields for activity versioning.
	 */
	protected AmpActivityGroup ampActivityGroup;
	
	
	protected Date modifiedDate;

//	@Interchangeable(fieldTitle = "Modified By")
	@VersionableFieldSimple(fieldTitle = "Modified By")
	protected AmpTeamMember modifiedBy;
	
	protected Boolean mergedActivity;
	protected AmpActivityVersion mergeSource1;
	protected AmpActivityVersion mergeSource2;

	@Interchangeable(fieldTitle = CategoryConstants.FUNDING_SOURCES_NUMBER_NAME,fmPath="/Activity Form/Funding/Total Number of Funding Sources")
    @VersionableFieldSimple(fieldTitle = CategoryConstants.FUNDING_SOURCES_NUMBER_NAME)
    protected Integer fundingSourcesNumber;
    
	@Interchangeable(fieldTitle = "Proposed Project Life",fmPath="/Activity Form/Planning/Proposed Project Life")
    @VersionableFieldSimple(fieldTitle = "Proposed Project Life")
    protected Integer proposedProjectLife;

	/**
	 * whether this is a PROJECT or a South-South Cooperation
	 */
    protected Long activityType = org.dgfoundation.amp.onepager.util.ActivityUtil.ACTIVITY_TYPE_PROJECT; //default type

    @Interchangeable(fieldTitle = "Annual Project Budgets",fmPath="/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost")
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
	public Set<AmpActivityInternalId> getInternalIds() {
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
	public Set <AmpActivitySector> getSectors() {
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
	public void setSectors(Set <AmpActivitySector> set) {
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
	public Set <AmpRegionalFunding> getRegionalFundings() {
		return regionalFundings;
	}

	/**
	 * @param regionalFundings
	 *            The regionalFundings to set.
	 */
	public void setRegionalFundings(Set <AmpRegionalFunding> regionalFundings) {
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

	public Set <AmpActivityProgram> getActPrograms() {
		return actPrograms;
	}
	
	public Set <AmpActivityBudgetStructure> getActBudgetStructure(){
		return actBudgetStructure;
	}
	
	public void setActBudgetStructure(Set <AmpActivityBudgetStructure> actBudgetStructure){
		this.actBudgetStructure = actBudgetStructure;
	}

	public void setReferenceDocs(Set referenceDocs) {
		this.referenceDocs = referenceDocs;
	}

	public void setDraft(Boolean draft) {
		this.draft = draft;
	}

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

	public void setActPrograms(Set <AmpActivityProgram> actPrograms) {
		this.actPrograms = actPrograms;
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

	public Set<IPAContract> getContracts() {
		return contracts;
	}

	public void setContracts(Set<IPAContract> contracts) {
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
