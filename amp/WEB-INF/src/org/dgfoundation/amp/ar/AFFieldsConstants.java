/**
 * 
 */
package org.dgfoundation.amp.ar;

import org.digijava.module.aim.annotations.activityversioning.VersionableFieldSimple;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Constants for Column Names. <br>
 * Values are pulled from amp_columns.columnname and several are defined manually.
 * @author Nadejda Mandrescu
 *
 */
public class AFFieldsConstants {
//	@VersionableFieldSimple(fieldTitle = "Created By", blockSingleChange = true)
//	protected AmpTeamMember createdBy;

	public final static String CREATED_BY = "Created By";
	public final static String PROJECT_IMPACT = "Project Impact";
	public final static String ACTIVITY_SUMMARY = "Activity Summary";
	public final static String CONDITIONALITY = "Conditionality";
	public final static String PROJECT_MANAGEMENT = "Project Management"; 
	public final static String ACTIVITY_BUDGET = "Activity Budget";
	public final static String GOVERNMENT_AGREEMENT_NUMBER = "Government Agreement Number";
	public final static String BUDGET_CODE_PROJECT_ID = "Budget Code Project ID";
	public final static String BUDGET_SECTOR = "Budget Sector";
	public final static String BUDGET_ORGANIZATION = "Budget Organization";
	public final static String BUDGET_PROGRAM = "Budget Program";
	public final static String AMP_ACTIVITY_ID = "AMP Activity ID";
	public final static String AMP_ID = "AMP Id";
	public final static String NAME = "Name";
	public final static String ACTIVITY_DESCRIPTION = "Activity Description";
	public final static String PROJECT_COMMENTS = "Project Comments";
	public final static String LESSONS_LEARNED = "Lessons Learned";
	public final static String OBJECTIVE = "Objective";
	public final static String PURPOSE = "Purpose";
	public final static String RESULTS = "Results";
	public final static String DOCUMENT_SPACE = "Document Space";
	public final static String IS_DRAFT = "Is Draft?";
	public final static String EQUAL_OPPORTUNITY = "Equal Oportunity";
	public final static String ENVIRONMENT = "Environment";
	public final static String MINORITIES = "Minorities";
	public final static String LANGUAGE = "Language";
	public final static String ORIGINAL_DATE = "Original Date";
	public final static String CONTRACTING_DATE = "Contracting Date";
	public final static String DISBURSEMENT_DATE = "Disbursement Date";
	public final static String SECTORS = "Sectors";
	public final static String CONTRACTS = "Contracts";
	public final static String LOCATIONS = "Locations";
	public final static String ORGANIZATION_ROLE = "Org. Role";
	public final static String INTERNAL_IDS = "Internal IDs";
	public final static String FUNDINGS = "Fundings";
	public final static String PROGRESS = "Progress";
	public final static String ISSUES = "Issues";
	public final static String REGIONAL_OBSERVATIONS = "Regional Observations";
	public final static String LINE_MINISTRY_OBSERVATIONS = "Line Ministry Observations";
	public final static String COSTS = "Costs";
	public final static String PROGRAM_DESCRIPTION = "Program Description";
	public final static String TEAM = "Team";
	public final static String DONOR_CONTACT_NAME = "Contact Name";
	public final static String FUN_AMOUNT = "Fun Amount";
	public final static String CURRENCY_CODE = "Currency Code";
	public final static String FUN_DATE = "Fun Date";
	public final static String DONOR_CONTACT_FIRST_NAME = "Contact First Name"; 
	public final static String DONOR_CONTACT_LAST_NAME = "Contact Last Name";
	public final static String DONOR_CONTACT_EMAIL = "Email";
	public final static String DONOR_CONTACT_TITLE = "Donor Contact Title";
	public final static String DONOR_CONTACT_ORGANIZATION = "Donor Contact Organization";
	public final static String DONOR_CONTACT_PHONE_NUMBER = "Donor Contact Phone Number";
	public final static String DONOR_CONTACT_FAX_NUMBER = "Donor Contact Fax Number";
	public final static String MOFED_CONTACT_FIRST_NAME = "MOFED Contact First Name"; 
	public final static String MOFED_CONTACT_LAST_NAME = "MOFED Contact Last Name";
	public final static String MOFED_CONTACT_EMAIL = "MOFED Contact Email";
	public final static String MOFED_CONTACT_TITLE = "MOFED Contact Title";
	public final static String MOFED_CONTACT_PHONE_NUMBER = "MOFED Contact Phone Number";
	public final static String MOFED_CONTACT_FAX_NUMBER = "MOFED Contact Fax Number";
	public final static String PROJECT_COORDINATOR_FIRST_NAME = "Project Coordinator First Name"; 
	public final static String PROJECT_COORDINATOR_LAST_NAME = "Project Coordinator Last Name";
	public final static String PROJECT_COORDINATOR_EMAIL = "Project Coordinator Email";
	public final static String PROJECT_COORDINATOR_TITLE = "Project Coordinator Title";
	public final static String PROJECT_COORDINATOR_ORGANIZATION = "Project Coordinator Organizaation";
	public final static String PROJECT_COORDINATOR_PHONE_NUMBER = "Project Coordinator Phone Number";
	public final static String PROJECT_COORDINATOR_FAX_NUMBER = "Project Coordinator Fax Number";
	public final static String SECTOR_MINISTRY_CONTACT_FIRST_NAME = "Sector Ministry Contact First Name";
	public final static String SECTOR_MINISTRY_CONTACT_LAST_NAME = "Sector Ministry Contact Last Name";
	public final static String SECTOR_MINISTRY_CONTACT_EMAIL = "Sector Ministry Contact Email";
	public final static String SECTOR_MINISTRY_CONTACT_TITLE = "Sector Ministry Contact Title";
	public final static String SECTOR_MINISTRY_CONTACT_ORGANIZATION = "Sector Ministry Contact Organization";
	public final static String SECTOR_MINISTRY_CONTACT_PHONE_NUMER = "Sector Ministry Contact Phone Number";
	public final static String SECTOR_MINISTRY_CONTACT_FAX_NUMBER = "Sector Ministry Contact Fax Number";
	public final static String ACTIVITY_CONTACTS = "Activity Contacts";
	public final static String STATUS_REASON = "Status Reason";
	public final static String COMPONENTS = "Components";
	public final static String STRUCTURES = "Structures";
	public final static String COMPONENT_FUNDINGS = "Component Fundings";
	public final static String PROPOSED_START_DATE = "Proposed Start Date";
	public final static String ACTUAL_START_DATE = "Actual Start Date";
	public final static String PROPOSED_APPROVAL_DATE = "Proposed Approval Date";
	public final static String ACTUAL_APPROVAL_DATE = "Actual Approval Date";
	public final static String ACTUAL_COMPLETION_DATE = "Actual Completion Date";
	public final static String PROPOSED_COMPLETION_DATE = "Proposed Completion Date";
	public final static String ACTIVITY_CREATOR = "Created By"; //??!??!??!??!
	public final static String CREATION_DATE = "Creation Date";
	public final static String UPDATE_DATE = "Update Date";
	public final static String IATI_LAST_UPDATE = "Iati Last Update Date";
	public final static String APPROVED_BY = "Approved By";
	public final static String APPROVAL_DATE = "Approval Date";
	public final static String REGIONAL_FUNDINGS = "Regional Fundings";
	public final static String APPROVAL_STATUS = "Approval Status";
	public final static String SURVEYS = "Surveys";
	public final static String GPI_SURVEYS = "GPI Surveys";
	public final static String LINE_MINISTRY_RANK = "Line Ministry Rank";
	public final static String ARCHIVED = "Archived";
	public final static String DELETED = "Deleted";
	public final static String PROJECT_IMPLEMENTATION_UNIT = "Project Implementation Unit";
	public final static String IMAC_APPROVED = "IMAC Approved";
	public final static String NATIONAL_OVERSIGHT = "National Oversight";
	public final static String ON_BUDGET = "On Budget";
	public final static String ON_PARLIAMENT = "On Parliament";
	public final static String ON_TREASURY = "On Treasury";
	public final static String NATIONAL_FINANCIAL_MANAGEMENT = "National Financial Management";
	public final static String NATIONAL_PROCUREMENT = "National Procurement";
	public final static String NATIONAL_AUDIT = "National Audit";
	public final static String INDICATORS = "Indicators";
	public final static String ACTIVITY_DOCUMENTS = "Activity Documents"; //LOL HOW IS THIS GONNA BE EXPORTED
	public final static String CATEGORIES = "Categories";
	public final static String INDIRECT_ON_BUDGET = "Indirect On Budget";
	public final static String FY = "FY";
	public final static String VOTE = "Vote";
	public final static String SUB_VOTE = "Sub Vote";
	public final static String SUB_PROGRAM = "Sub Program"; 
	public final static String PROJECT_CODE = "Project Code";
	public final static String MINISTRY_CODE = "Ministry Code";
	public final static String CRIS_NUMBER = "CRIS Number";
	public final static String GOVERNMENT_APPROVAL_PROCEDURES = "Government Approval Procedures";
	public final static String JOINT_CRITERIA = "Joint Criteria";
	public final static String HUMANITARIAN_AID = "Humanitarian Aid";
	public final static String ACTIVITY_PROGRAMS = "Act. Programs";
	public final static String ACTIVITY_BUDDGET_STRUCTURE = "Act. Budget Structure";
	public final static String MODIFIED_BY = "Modified By";
	public final static String FUNDING_SOURCES_NUMBER = "Total Number of Funding Sources";
	public final static String PROPOSED_PROJECT_LIFE = "Proposed Project Life";
	public final static String ANNUAL_PROJECT_BUDGETS = "Annual Project Budgets";
}
