/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AFFieldsConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.utils.ConstantsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

/**
 * Detects which fields are visible in Activity Form and can be further used, 
 * e.g. in report generation & filtering
 * 
 * @author Alexandru Cartaleanu
 */
public class AFFieldsVisibility extends DataVisibility implements FMSettings {
	protected static final Logger logger = Logger.getLogger(AFFieldsConstants.class);
	
	private static final Set<String> fieldsSet = ConstantsUtil.getConstantsSet(AFFieldsConstants.class);
	
	/**
	 * @return the current set of visible columns
	 */
	synchronized
	public static Set<String> getVisibleFields() {
		return FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_COLUMNS);
	}
	
	protected AFFieldsVisibility() {
	}
	
	@Override
	public Set<String> getEnabledSettings() {
		return getCurrentVisibleData();
	}
	
	@Override
	protected Map<String, Collection<String>> getDependancyMapTypeAny() {
		return anyDependencyMap;
	}
	
	@Override
	protected List<String> getVisibleByDefault() {
		List<String> currentlyVisible = new ArrayList<String>(visibleByDefault);
		for (Entry<String, HardCodedCategoryValue> entry : categoryValueDependency.entrySet()) {
			AmpCategoryValue acv = entry.getValue().getAmpCategoryValueFromDB();
			if (acv != null && acv.isVisible())
				currentlyVisible.add(entry.getKey());	
		}
		return currentlyVisible;
	}
	
	@Override
	protected Set<String> getAllData() {
		return fieldsSet;
	}
	
	@Override
	protected Map<String, String> getDataMap(DataMapType dataMapType) {
		switch(dataMapType) {
		case MODULES: return modulesToAFFieldsMap; 
		case FEATURES: return featuresToColumnsMap;
		case FIELDS: return fieldsToColumnsMap;
		case DEPENDENCY: return dependencyMap;
		default: return null; //shouldn't come here
		}
	}
	
	@Override
	protected Map<String, Collection<String>> getDependancyMapTypeAll() {
		return dependencyTypeAll;
	}
	
	/****************
	 * Visibility related constants
	 ***************/
//	private static final String DONOR_ORAGNIZATION = "Donor Organization";
//	private static final String SEARCH_DONOR_ORAGNIZATION = "Search Donor Organization";
//	private static final String IMPLEMENTATION_LOCATION_COUNTRY = "IMPLEMENTATION_LOCATION_COUNTRY";
//	private static final String IMPLEMENTATION_LOCATION_REGION = "IMPLEMENTATION_LOCATION_REGION";
//	private static final String IMPLEMENTATION_LOCATION_ZONE = "IMPLEMENTATION_LOCATION_ZONE";
//	private static final String IMPLEMENTATION_LOCATION_DISTRICT = "IMPLEMENTATION_LOCATION_DISTRICT";
	
	//Note: mappings are manually retrieved, because no certain way exists to map them 
	@SuppressWarnings("serial")
	protected static final Map<String, String> modulesToAFFieldsMap = new HashMap<String, String>() {{
		
		put("Activity Created By", AFFieldsConstants.CREATED_BY);
		put("/Activity Form/Identification/Project Impact", AFFieldsConstants.PROJECT_IMPACT);
		put("/Activity Form/Identification/Activity Summary", AFFieldsConstants.ACTIVITY_SUMMARY);
		put("/Activity Form/Identification/Conditionalities", AFFieldsConstants.CONDITIONALITY);
		put("/Activity Form/Identification/Project Management", AFFieldsConstants.PROJECT_MANAGEMENT);
		put("/Activity Form/Identification/Activity Budget", AFFieldsConstants.ACTIVITY_BUDGET);
		put("/Activity Form/Identification/Government Agreement Number", AFFieldsConstants.GOVERNMENT_AGREEMENT_NUMBER);
		put("/Activity Form/Identification/Budget Code Project ID", AFFieldsConstants.BUDGET_CODE_PROJECT_ID);
		put("/Activity Form/Identification/Budget Classification", AFFieldsConstants.BUDGET_SECTOR);
		put("/Activity Form/Identification/Budget Classification", AFFieldsConstants.BUDGET_ORGANIZATION);//apparently, both are under the same component? maybe budget code?
		put("/Activity Form/Identification/Budget Extras/Sub-Program", AFFieldsConstants.BUDGET_PROGRAM);// maybe sub-program?
		put("always on", AFFieldsConstants.AMP_ACTIVITY_ID); //is it?
		put("always on", AFFieldsConstants.AMP_ID); //is it?
		put("always on", AFFieldsConstants.NAME); //is it?
		put("/Activity Form/Identification/Description", AFFieldsConstants.ACTIVITY_DESCRIPTION);
		put("/Activity Form/Identification/Project Comments", AFFieldsConstants.PROJECT_COMMENTS);
		put("/Activity Form/Identification/Lessons Learned", AFFieldsConstants.LESSONS_LEARNED);
		put("/Activity Form/Identification/Objective", AFFieldsConstants.OBJECTIVE);
		put("/Activity Form/Identification/Purpose", AFFieldsConstants.PURPOSE);
		put("/Activity Form/Identification/Results", AFFieldsConstants.RESULTS);
		put("questionable", AFFieldsConstants.DOCUMENT_SPACE);//unused in code! - indeed 
		put("always on", AFFieldsConstants.IS_DRAFT);//
		put("/Activity Form/Cross Cutting Issues/Equal Opportunity", AFFieldsConstants.EQUAL_OPPORTUNITY);
		put("/Activity Form/Cross Cutting Issues/Environment", AFFieldsConstants.ENVIRONMENT);
		put("/Activity Form/Cross Cutting Issues/Minorities", AFFieldsConstants.MINORITIES);
		put("questionable", AFFieldsConstants.LANGUAGE);//where is it used? 
		put("/Activity Form/Planning/Original Completion Date", AFFieldsConstants.ORIGINAL_DATE);
		put("/Activity Form/Planning/Final Date for Contracting", AFFieldsConstants.CONTRACTING_DATE);
		put("/Activity Form/Planning/Final Date for Disbursements", AFFieldsConstants.DISBURSEMENT_DATE);
		put("/Activity Form/Sectors", AFFieldsConstants.SECTORS);
		put("/Activity Form/Contracts", AFFieldsConstants.CONTRACTS);
		put("/Activity Form/Location", AFFieldsConstants.LOCATIONS);
		put("/Activity Form/Funding/Org Role", AFFieldsConstants.ORGANIZATION_ROLE);
		put("/Activity Form/Activity Internal IDs", AFFieldsConstants.INTERNAL_IDS);
		put("/Activity Form/Funding", AFFieldsConstants.FUNDINGS);
		put("questionable", AFFieldsConstants.PROGRESS); //setter and getter never called
		put("/Activity Form/Issues Section", AFFieldsConstants.ISSUES);//possibly also "Issues" (the latter has a description)
		put("/Activity Form/Regional Observations", AFFieldsConstants.REGIONAL_OBSERVATIONS);//
		put("/Activity Form/Line Ministry Observations/Observation", AFFieldsConstants.LINE_MINISTRY_OBSERVATIONS);
		put("always on", AFFieldsConstants.COSTS);//clonable
		put("/Activity Form/Program/Program Description", AFFieldsConstants.PROGRAM_DESCRIPTION);
		put("always on", AFFieldsConstants.TEAM); //???
		put("/Activity Form/Contacts/Donor Contact Information/contact name", AFFieldsConstants.DONOR_CONTACT_NAME);
		put("/Activity Form/Funding/Proposed Project Cost/Amount", AFFieldsConstants.FUN_AMOUNT); //??? unsure
		put("always on", AFFieldsConstants.CURRENCY_CODE); //??? is it?
		put("always on", AFFieldsConstants.FUN_DATE);///clonable too 
		put("/Activity Form/Contacts/Donor Contact Information/contact first name", AFFieldsConstants.DONOR_CONTACT_FIRST_NAME);
		put("/Activity Form/Contacts/Donor Contact Information/contact first name", AFFieldsConstants.DONOR_CONTACT_LAST_NAME);
		put("/Activity Form/Contacts/Donor Contact Information/Add Contact Email", AFFieldsConstants.DONOR_CONTACT_EMAIL);
		put("/Activity Form/Contacts/Donor Contact Information/Contact Title", AFFieldsConstants.DONOR_CONTACT_TITLE);
		put("/Activity Form/Contacts/Donor Contact Information/Contact Organizations", AFFieldsConstants.DONOR_CONTACT_ORGANIZATION);
		put("/Activity Form/Contacts/Donor Contact Information/Add Contact Phone", AFFieldsConstants.DONOR_CONTACT_PHONE_NUMBER);
		put("/Activity Form/Contacts/Donor Contact Information/Add Contact Fax/Add Contact Fax", AFFieldsConstants.DONOR_CONTACT_FAX_NUMBER);	
		put("/Activity Form/Contacts/Mofed Contact Information/contact first name", AFFieldsConstants.MOFED_CONTACT_FIRST_NAME);
		put("/Activity Form/Contacts/Mofed Contact Information/contact lastname", AFFieldsConstants.MOFED_CONTACT_LAST_NAME);
		put("/Activity Form/Contacts/Mofed Contact Information/Add Contact Email", AFFieldsConstants.MOFED_CONTACT_EMAIL);
		put("/Activity Form/Contacts/Mofed Contact Information/Contact Title", AFFieldsConstants.MOFED_CONTACT_TITLE);
		put("/Activity Form/Contacts/Mofed Contact Information/Add Contact Phone", AFFieldsConstants.MOFED_CONTACT_PHONE_NUMBER);
		put("/Activity Form/Contacts/Mofed Contact Information/Add Contact Fax", AFFieldsConstants.MOFED_CONTACT_FAX_NUMBER);
		put("/Activity Form/Contacts/Project Coordinator Contact Information/contact first name", AFFieldsConstants.PROJECT_COORDINATOR_FIRST_NAME);
		put("/Activity Form/Contacts/Project Coordinator Contact Information/contact lastname", AFFieldsConstants.PROJECT_COORDINATOR_LAST_NAME);
		put("/Activity Form/Contacts/Project Coordinator Contact Information/Add Contact Email", AFFieldsConstants.PROJECT_COORDINATOR_EMAIL);
		put("/Activity Form/Contacts/Project Coordinator Contact Information/Contact Title", AFFieldsConstants.PROJECT_COORDINATOR_TITLE);
		put("/Activity Form/Contacts/Project Coordinator Contact Information/Contact Organizations", AFFieldsConstants.PROJECT_COORDINATOR_ORGANIZATION);
		put("/Activity Form/Contacts/Project Coordinator Contact Information/Add Contact Phone", AFFieldsConstants.PROJECT_COORDINATOR_PHONE_NUMBER);
		put("/Activity Form/Contacts/Project Coordinator Contact Information/Add Contact Fax", AFFieldsConstants.PROJECT_COORDINATOR_FAX_NUMBER);
		put("/Activity Form/Contacts/Sector Ministry Contact Information/contact first name", AFFieldsConstants.SECTOR_MINISTRY_CONTACT_FIRST_NAME);
		put("/Activity Form/Contacts/Sector Ministry Contact Information/contact lastname", AFFieldsConstants.SECTOR_MINISTRY_CONTACT_LAST_NAME);
		put("/Activity Form/Contacts/Sector Ministry Contact Information/Add Contact Email", AFFieldsConstants.SECTOR_MINISTRY_CONTACT_EMAIL);
		put("/Activity Form/Contacts/Sector Ministry Contact Information/Contact Title", AFFieldsConstants.SECTOR_MINISTRY_CONTACT_TITLE);
		put("/Activity Form/Contacts/Sector Ministry Contact Information/Contact Organizations", AFFieldsConstants.SECTOR_MINISTRY_CONTACT_ORGANIZATION);
		put("/Activity Form/Contacts/Sector Ministry Contact Information/Add Contact Phone", AFFieldsConstants.SECTOR_MINISTRY_CONTACT_PHONE_NUMER);
		put("/Activity Form/Contacts/Sector Ministry Contact Information/Add Contact Fax", AFFieldsConstants.SECTOR_MINISTRY_CONTACT_FAX_NUMBER);
		put("/Activity Form/Contacts", AFFieldsConstants.ACTIVITY_CONTACTS);
		put("/Activity Form/Identification/Status Reason", AFFieldsConstants.STATUS_REASON);
		put("/Activity Form/Components", AFFieldsConstants.COMPONENTS);
		put("/Activity Form/Structures", AFFieldsConstants.STRUCTURES);
		put("/Activity Form/Components/Component/Component Information", AFFieldsConstants.COMPONENT_FUNDINGS); // ???
		put("/Activity Form/Planning/Proposed Start Date", AFFieldsConstants.PROPOSED_START_DATE);
		put("/Activity Form/Planning/Actual Start Date", AFFieldsConstants.ACTUAL_START_DATE);
		put("/Activity Form/Planning/Proposed Approval Date", AFFieldsConstants.PROPOSED_APPROVAL_DATE);
		put("/Activity Form/Planning/Actual Approval Date", AFFieldsConstants.ACTUAL_APPROVAL_DATE);
		put("/Activity Form/Planning/Actual Completion Date", AFFieldsConstants.ACTUAL_COMPLETION_DATE);
		put("/Activity Form/Planning/Proposed Completion Date", AFFieldsConstants.PROPOSED_COMPLETION_DATE);
		put("always on", AFFieldsConstants.ACTIVITY_CREATOR); //has to be!
		put("/Activity Form/M&E/Creation Date", AFFieldsConstants.CREATION_DATE);
		put("questionable", AFFieldsConstants.UPDATE_DATE); //creation date has a field, but this one doesn't?
		put("questionable", AFFieldsConstants.IATI_LAST_UPDATE);//not in fm
		put("always on", AFFieldsConstants.APPROVED_BY);
		put("always on", AFFieldsConstants.APPROVAL_DATE);
		put("/Activity Form/Regional Funding", AFFieldsConstants.REGIONAL_FUNDINGS);
		put("always on", AFFieldsConstants.APPROVAL_STATUS);
		put("questionable", AFFieldsConstants.SURVEYS); //????
		put("/Activity Form/GPI", AFFieldsConstants.GPI_SURVEYS);
		put("/Activity Form/Planning/Line Ministry Rank", AFFieldsConstants.LINE_MINISTRY_RANK);
		put("always_on", AFFieldsConstants.ARCHIVED);
		put("always_on", AFFieldsConstants.DELETED);
		put("/Activity Form/Identification/Project Implementing Unit", AFFieldsConstants.PROJECT_IMPLEMENTATION_UNIT);
		put("questionable", AFFieldsConstants.IMAC_APPROVED); //????
		put("questionable", AFFieldsConstants.NATIONAL_OVERSIGHT); //not in fm, what is this?
		put("/Activity Form/Identification/Activity Budget", AFFieldsConstants.ON_BUDGET);// ??? UNSURE
		put("/Activity Form/GPI/GPI Item/GPI Questions List/Is this project on budget and subject to parliamentary scrutiny?", AFFieldsConstants.ON_PARLIAMENT); //???unsure
		put("questionable", AFFieldsConstants.ON_TREASURY); //???unsure
		put("/Activity Form/GPI/GPI Item/GPI Questions List/Does the project use national financial reporting procedures?", AFFieldsConstants.NATIONAL_FINANCIAL_MANAGEMENT);//???unsure
		put("/Activity Form/GPI/GPI Item/GPI Questions List/Does the project use national procurement systems?", AFFieldsConstants.NATIONAL_PROCUREMENT);//??? unsure
		put("/Activity Form/GPI/GPI Item/GPI Questions List/Does the project use national auditing procedures?", AFFieldsConstants.NATIONAL_AUDIT);//??? unsure
		put("/Activity Form/M&E/Add Indicator", AFFieldsConstants.INDICATORS);//??? re-check
		put("/Activity Form/Related Documents", AFFieldsConstants.ACTIVITY_DOCUMENTS);
		put("/Activity Form/Identification/Project Category", AFFieldsConstants.CATEGORIES);
		put("/Activity Form/Identification/Budget Extras/Indirect On Budget", AFFieldsConstants.INDIRECT_ON_BUDGET);
		put("/Activity Form/Identification/Budget Extras/FY", AFFieldsConstants.FY);
		put("/Activity Form/Identification/Budget Extras/Vote", AFFieldsConstants.VOTE);
		put("/Activity Form/Identification/Budget Extras/Sub-Vote", AFFieldsConstants.SUB_VOTE);
		put("/Activity Form/Identification/Budget Extras/Sub-Program", AFFieldsConstants.SUB_PROGRAM);
		put("/Activity Form/Identification/Donor Project Code", AFFieldsConstants.PROJECT_CODE);//???not entirely sure
		put("/Activity Form/Identification/Budget Extras/Ministry Code", AFFieldsConstants.MINISTRY_CODE);
		put("/Activity Form/Identification/Cris Number", AFFieldsConstants.CRIS_NUMBER);
		put("/Activity Form/Identification/Government Approval Procedures", AFFieldsConstants.GOVERNMENT_APPROVAL_PROCEDURES);
		put("/Activity Form/Identification/Joint Criteria", AFFieldsConstants.JOINT_CRITERIA);
		put("/Activity Form/Identification/Humanitarian Aid", AFFieldsConstants.HUMANITARIAN_AID);
		put("/Activity Form/Program", AFFieldsConstants.ACTIVITY_PROGRAMS); //?????
		put("/Activity Form/Budget Structure", AFFieldsConstants.ACTIVITY_BUDDGET_STRUCTURE);
		put("always_on", AFFieldsConstants.MODIFIED_BY);
		put("/Activity Form/Funding/Total Number of Funding Sources", AFFieldsConstants.FUNDING_SOURCES_NUMBER);//there's another one with the same name
		put("/Activity Form/Planning/Proposed Project Life", AFFieldsConstants.PROPOSED_PROJECT_LIFE);
		put("/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost", AFFieldsConstants.ANNUAL_PROJECT_BUDGETS);//??? is it this one?
	}};
	
	@SuppressWarnings("serial")
	public static final Map<String, String> dependencyMap= new HashMap<String, String>() {{
//		put(ColumnConstants.ACTIVITY_ID, ColumnConstants.PROJECT_TITLE);
//		put(ColumnConstants.INTERNAL_USE_ID, ColumnConstants.PROJECT_TITLE);
//		put(ColumnConstants.PRIMARY_SECTOR_ID, ColumnConstants.PRIMARY_SECTOR);
//		put(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, ColumnConstants.PRIMARY_SECTOR);
//		put(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR_ID, ColumnConstants.PRIMARY_SECTOR);
//		put(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.PRIMARY_SECTOR);
//		put(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR_ID, ColumnConstants.PRIMARY_SECTOR);
//		put(ColumnConstants.SECONDARY_SECTOR_ID, ColumnConstants.SECONDARY_SECTOR);
//		put(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, ColumnConstants.SECONDARY_SECTOR);
//		put(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR_ID, ColumnConstants.SECONDARY_SECTOR);
//		put(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.SECONDARY_SECTOR);
//		put(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR_ID, ColumnConstants.SECONDARY_SECTOR);
//		put(ColumnConstants.TERTIARY_SECTOR_ID, ColumnConstants.TERTIARY_SECTOR);
//		put(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR, ColumnConstants.TERTIARY_SECTOR);
//		put(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR_ID, ColumnConstants.TERTIARY_SECTOR);
//		put(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.TERTIARY_SECTOR);
//		put(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR_ID, ColumnConstants.TERTIARY_SECTOR);
//		put(ColumnConstants.AGREEMENT_CLOSE_DATE, ColumnConstants.AGREEMENT_CODE);
//		put(ColumnConstants.AGREEMENT_EFFECTIVE_DATE, ColumnConstants.AGREEMENT_CODE);
//		put(ColumnConstants.AGREEMENT_SIGNATURE_DATE, ColumnConstants.AGREEMENT_CODE);
//		put(ColumnConstants.AGREEMENT_TITLE_CODE, ColumnConstants.AGREEMENT_CODE);
//		put(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_DETAIL, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES);
//		put(ColumnConstants.PRIMARY_PROGRAM_DETAIL, ColumnConstants.PRIMARY_PROGRAM);
//		put(ColumnConstants.SECONDARY_PROGRAM_DETAIL, ColumnConstants.SECONDARY_PROGRAM);
//		put(ColumnConstants.TERTIARY_PROGRAM_DETAIL, ColumnConstants.TERTIARY_PROGRAM);
//		
//		String[][] colPrefixList = new String[][]{
//			{"National Planning Objectives Level", ColumnConstants.NATIONAL_PLANNING_OBJECTIVES},
//			{"Primary Program Level",  ColumnConstants.PRIMARY_PROGRAM},
//			{"Secondary Program Level", ColumnConstants.SECONDARY_PROGRAM},
//			{"Tertiary Program Level", ColumnConstants.TERTIARY_PROGRAM}
//		};
//		for (String[] colPrefix : colPrefixList) {
//			for (int i = 1; i < 9 ; i++) {
//				put(colPrefix[0] + " " + i, colPrefix[1]);
//				put(colPrefix[0] + " " + i + " Id", colPrefix[1]);
//			}
//		}
//		
//		put(ColumnConstants.DONOR_ID, ColumnConstants.DONOR_AGENCY);
//		put(ColumnConstants.DONOR_GROUP, ColumnConstants.DONOR_AGENCY);
//		put(ColumnConstants.DONOR_TYPE, ColumnConstants.DONOR_AGENCY);
//		put(ColumnConstants.DONOR_COMMITMENT_DATE, ColumnConstants.DONOR_AGENCY);
//		put(ColumnConstants.RESPONSIBLE_ORGANIZATION_ID, ColumnConstants.RESPONSIBLE_ORGANIZATION);
//		put(ColumnConstants.RESPONSIBLE_ORGANIZATION_DEPARTMENT_DIVISION, ColumnConstants.RESPONSIBLE_ORGANIZATION);
//		put(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS, ColumnConstants.RESPONSIBLE_ORGANIZATION);
//		put(ColumnConstants.EXECUTING_AGENCY_ID, ColumnConstants.EXECUTING_AGENCY);
//		put(ColumnConstants.EXECUTING_AGENCY_DEPARTMENT_DIVISION, ColumnConstants.EXECUTING_AGENCY);
//		put(ColumnConstants.EXECUTING_AGENCY_GROUPS, ColumnConstants.EXECUTING_AGENCY);
//		put(ColumnConstants.EXECUTING_AGENCY_TYPE, ColumnConstants.EXECUTING_AGENCY);
//		put(ColumnConstants.IMPLEMENTING_AGENCY_DEPARTMENT_DIVISION, ColumnConstants.IMPLEMENTING_AGENCY);
//		put(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, ColumnConstants.IMPLEMENTING_AGENCY);
//		put(ColumnConstants.IMPLEMENTING_AGENCY_ID, ColumnConstants.IMPLEMENTING_AGENCY);
//		put(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, ColumnConstants.IMPLEMENTING_AGENCY);
//		put(ColumnConstants.BENEFICIARY_AGENCY__DEPARTMENT_DIVISION, ColumnConstants.BENEFICIARY_AGENCY);
//		put(ColumnConstants.BENEFICIARY_AGENCY_GROUPS, ColumnConstants.BENEFICIARY_AGENCY);
//		put(ColumnConstants.BENEFICIARY_AGENCY_ID, ColumnConstants.BENEFICIARY_AGENCY);
//		put(ColumnConstants.CONTRACTING_AGENCY_ACRONYM, ColumnConstants.CONTRACTING_AGENCY);
//		put(ColumnConstants.CONTRACTING_AGENCY_DEPARTMENT_DIVISION, ColumnConstants.CONTRACTING_AGENCY);
//		put(ColumnConstants.CONTRACTING_AGENCY_GROUPS, ColumnConstants.CONTRACTING_AGENCY);
//		put(ColumnConstants.CONTRACTING_AGENCY_ID, ColumnConstants.CONTRACTING_AGENCY);
//		put(ColumnConstants.SECTOR_GROUP_DEPARTMENT_DIVISION, ColumnConstants.SECTOR_GROUP);
//		put(ColumnConstants.REGIONAL_GROUP_DEPARTMENT_DIVISION, ColumnConstants.REGIONAL_GROUP);
//		
//		String[][] contacts = new String[][] {
//			{"Donor Contact", ColumnConstants.DONOR_CONTACT_ORGANIZATION}, 
//			{"Sector Ministry Contact", ColumnConstants.SECTOR_MINISTRY_CONTACT_ORGANIZATION},
//			{"Project Coordinator Contact", ColumnConstants.PROJECT_COORDINATOR_CONTACT_ORGANIZATION},
//			{"Implementing/Executing Agency Contact", ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_ORGANIZATION},
//			{"Ministry Of Finance Contact", ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_ORGANIZATION},
//			{"Pledge Contact 1 -", ColumnConstants.PLEDGE_CONTACT_1___MINISTRY},
//			{"Pledge Contact 2 -", ColumnConstants.PLEDGE_CONTACT_2___MINISTRY}
//		};
//		String[] suffixList = new String[] {"Email", "Fax", "Name", "Phone", "Title",
//				//and pledges specific
//				"Address", "Alternate Contact", "Telephone"};
//		for (String[] con : contacts)
//			for (String suffix : suffixList)
//				put(con[0] + " " + suffix, con[1]);
//		
//		put(ColumnConstants.GEOCODE, ColumnConstants.LOCATION);
	}};
	
	@SuppressWarnings("serial")
	protected static final Map<String, Collection<String>> anyDependencyMap= new HashMap<String, Collection<String>>() {{
		/* moving DONOR_AGENCY to always visible list per confirmation from Diego that there won't be any 
		 * country that may not want donors to be listed and everything related to it to be disabled in FM 
		put(ColumnConstants.DONOR_AGENCY, Arrays.asList(DONOR_ORAGNIZATION, SEARCH_DONOR_ORAGNIZATION));
		*/
	}};
	
	@SuppressWarnings("serial")
	protected static final Map<String, String> featuresToColumnsMap = new HashMap<String, String>() {{
//		put("Physical Progress", ColumnConstants.PHYSICAL_PROGRESS);
	}};
	
	@SuppressWarnings("serial")
	protected static final Map<String, String> fieldsToColumnsMap = new HashMap<String, String>() {{
//		put("AMP ID", ColumnConstants.AMP_ID);
//		put("Activity Approved By", ColumnConstants.ACTIVITY_APPROVED_BY);
//		put("Activity Created By", ColumnConstants.ACTIVITY_CREATED_BY);
//		put("Activity Created On", ColumnConstants.ACTIVITY_CREATED_ON);
//		put("Activity Pledges Title", ColumnConstants.ACTIVITY_PLEDGES_TITLE);
//		put("Activity Updated By", ColumnConstants.ACTIVITY_UPDATED_BY);
//		put("Activity Updated On", ColumnConstants.ACTIVITY_UPDATED_ON);
//		put("Actors", ColumnConstants.ACTORS);
//		put("Age of Project (Months)", ColumnConstants.AGE_OF_PROJECT_MONTHS);
//		put("Archived", ColumnConstants.ARCHIVED);
//		put("Average Disbursement Rate", ColumnConstants.AVERAGE_DISBURSEMENT_RATE);
//		put("Average Size of Disbursements", ColumnConstants.AVERAGE_SIZE_OF_DISBURSEMENTS);
//		put("Average Size of Projects", ColumnConstants.AVERAGE_SIZE_OF_PROJECTS);
//		put("Budget Department", ColumnConstants.BUDGET_DEPARTMENT);
//		put("Budget Organization", ColumnConstants.BUDGET_ORGANIZATION);
//		put("Budget Program", ColumnConstants.BUDGET_PROGRAM);
//		put("Budget Sector", ColumnConstants.BUDGET_SECTOR);
//		put("Calculated Project Life", ColumnConstants.CALCULATED_PROJECT_LIFE);
//		put("Capital - Expenditure", ColumnConstants.CAPITAL___EXPENDITURE);
//		put("Component Name", ColumnConstants.COMPONENT_NAME);
//		put("Component description", ColumnConstants.COMPONENT_DESCRIPTION);
//		put("Component Funding Organization", ColumnConstants.COMPONENT_FUNDING_ORGANIZATION);
//		put("Component Type", ColumnConstants.COMPONENT_TYPE);
//		put("Costing Total Contribution", ColumnConstants.COSTING_DONOR);
//		put("Credit/Donation", ColumnConstants.CREDIT_DONATION);
//		put("Cumulative Commitment", ColumnConstants.CUMULATIVE_COMMITMENT);
//		put("Cumulative Disbursement", ColumnConstants.CUMULATIVE_DISBURSEMENT);
//		put("Current Completion Date Comments", ColumnConstants.CURRENT_COMPLETION_DATE_COMMENTS);
//		put("Description of Component Funding", ColumnConstants.DESCRIPTION_OF_COMPONENT_FUNDING);
//		put("Draft", ColumnConstants.DRAFT);
//		put("Execution Rate", ColumnConstants.EXECUTION_RATE);
//		put("Final Date for Disbursements Comments", ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS_COMMENTS);
//		put("Funding end date", ColumnConstants.FUNDING_END_DATE);
//		put("Funding start date", ColumnConstants.FUNDING_START_DATE);
//		put("Grand Total Cost", ColumnConstants.GRAND_TOTAL_COST);
//		put("Measures Taken", ColumnConstants.MEASURES_TAKEN);
//		put("Ministry Of Finance Contact Organization", ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_ORGANIZATION);
//		put("Multi Donor", ColumnConstants.MULTI_DONOR);
//		put("Organizations and Project ID", ColumnConstants.ORGANIZATIONS_AND_PROJECT_ID);
//		put("Overage", ColumnConstants.OVERAGE);
//		put("Overage Project", ColumnConstants.OVERAGE_PROJECT);
//		put("Payment Capital - Recurrent", ColumnConstants.PAYMENT_CAPITAL___RECURRENT);
//		put("Physical progress description", ColumnConstants.PHYSICAL_PROGRESS_DESCRIPTION);
//		put("Physical progress title", ColumnConstants.PHYSICAL_PROGRESS_TITLE);
//		put("Pledges sectors", ColumnConstants.PLEDGES_SECTORS);
//		put("Pledges Aid Modality", ColumnConstants.PLEDGES_AID_MODALITY);
//		put("Pledge Contact 1 - Organization", ColumnConstants.PLEDGE_CONTACT_1___MINISTRY);
//		put("Pledge Contact 2 - Organization", ColumnConstants.PLEDGE_CONTACT_2___MINISTRY);
//		put("Pledge Status", ColumnConstants.PLEDGE_STATUS);
//		put("Pledges Detail Date Range", ColumnConstants.PLEDGES_DETAIL_DATE_RANGE);
//		put("Pledges Detail End Date", ColumnConstants.PLEDGES_DETAIL_END_DATE);
//		put("Pledges Detail Start Date", ColumnConstants.PLEDGES_DETAIL_START_DATE);
//		put("Pledges Districts", ColumnConstants.PLEDGES_DISTRICTS);
//		put("Pledges Donor Group", ColumnConstants.PLEDGES_DONOR_GROUP);
//		put("Pledges National Plan Objectives", ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES);
//		put("Pledges Programs", ColumnConstants.PLEDGES_PROGRAMS);
//		put("Pledges Regions", ColumnConstants.PLEDGES_REGIONS);
//		put("Pledges Secondary Programs", ColumnConstants.PLEDGES_SECONDARY_PROGRAMS);
//		put("Pledges Secondary Sectors", ColumnConstants.PLEDGES_SECONDARY_SECTORS);
//		put("Pledges Sectors", ColumnConstants.PLEDGES_SECTORS);
//		put("Pledges Tertiary Programs", ColumnConstants.PLEDGES_TERTIARY_PROGRAMS);
//		put("Pledges Tertiary Sectors", ColumnConstants.PLEDGES_TERTIARY_SECTORS);
//		put("Pledges Type Of Assistance", ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE);
//		put("Pledges Titles", ColumnConstants.PLEDGES_TITLES);
//		put("Pledges Zones", ColumnConstants.PLEDGES_ZONES);
//		put("Predictability of Funding", ColumnConstants.PREDICTABILITY_OF_FUNDING);
//		put("Project Age Ratio", ColumnConstants.PROJECT_AGE_RATIO);
//		put("Project Description", ColumnConstants.PROJECT_DESCRIPTION);
//		put("Project Period", ColumnConstants.PROJECT_PERIOD);
//		put("Related Projects", ColumnConstants.RELATED_PROJECTS);
//		put("Related Pledges", ColumnConstants.RELATED_PLEDGES);
//		put("Sector Location", ColumnConstants.SECTOR_LOCATION);
//		put("Sector Tag", ColumnConstants.SECTOR_TAG);
//		put("Sector Tag Sub-Sector", ColumnConstants.SECTOR_TAG_SUB_SECTOR);
//		put("Sector Tag Sub-Sub-Sector", ColumnConstants.SECTOR_TAG_SUB_SUB_SECTOR);
//		put("SSC Modalities", ColumnConstants.SSC_MODALITIES);
//		put("Variance Of Commitments", ColumnConstants.VARIANCE_OF_COMMITMENTS);
//		put("Variance Of Disbursements", ColumnConstants.VARIANCE_OF_DISBURSEMENTS);
//		put(ColumnConstants.UNCOMMITTED_CUMULATIVE_BALANCE, ColumnConstants.UNCOMMITTED_CUMULATIVE_BALANCE);
//		put(ColumnConstants.UNDISBURSED_CUMULATIVE_BALANCE, ColumnConstants.UNDISBURSED_CUMULATIVE_BALANCE);
	}};
	
	protected static final List<String> visibleByDefault = Arrays.asList(
//			ColumnConstants.ACTIVITY_COUNT,
//			ColumnConstants.FUNDING_YEAR,
//			ColumnConstants.TEAM,
//			ColumnConstants.DONOR_AGENCY,
//			ColumnConstants.RELATED_PLEDGES,
//			ColumnConstants.RELATED_PROJECTS
	);

	/**
	 * Dependency map between column and category value 
	 */
	@SuppressWarnings("serial")
	protected static final Map<String, HardCodedCategoryValue> categoryValueDependency = new HashMap<String, HardCodedCategoryValue>() {{
//		put(IMPLEMENTATION_LOCATION_COUNTRY, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
//		put(IMPLEMENTATION_LOCATION_REGION, CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
//		put(IMPLEMENTATION_LOCATION_ZONE, CategoryConstants.IMPLEMENTATION_LOCATION_ZONE);
//		put(IMPLEMENTATION_LOCATION_DISTRICT, CategoryConstants.IMPLEMENTATION_LOCATION_DISTRICT);
	}};
	
	
	protected static final Map<String, Collection<String>> dependencyTypeAll = new HashMap<String, Collection<String>>() {{
//		put(ColumnConstants.COUNTRY, Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_COUNTRY));
//		put(ColumnConstants.REGION, Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_REGION));
//		put(ColumnConstants.ZONE, Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_ZONE));
//		put(ColumnConstants.DISTRICT, Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_DISTRICT));
	}};
}
