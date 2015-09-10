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
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.utils.ConstantsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

/**
 * Detects which columns are visible in Activity Form and can be further used, 
 * e.g. in report generation & filtering
 * 
 * @author Nadejda Mandrescu
 */
public class ColumnsVisibility extends DataVisibility implements FMSettings {
	protected static final Logger logger = Logger.getLogger(ColumnsVisibility.class);
	
	private static final Set<String> columnsSet = ConstantsUtil.getConstantsSet(ColumnConstants.class);
	
	/**
	 * @return the current set of visible columns
	 */
	synchronized
	public static Set<String> getVisibleColumns() {
		return FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_COLUMNS);
	}
	
	protected ColumnsVisibility() {
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
		return columnsSet;
	}
	
	@Override
	protected Map<String, String> getDataMap(DataMapType dataMapType) {
		switch(dataMapType) {
		case MODULES: return modulesToColumnsMap; 
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
	private static final String DONOR_ORAGNIZATION = "Donor Organization";
	private static final String SEARCH_DONOR_ORAGNIZATION = "Search Donor Organization";
	private static final String IMPLEMENTATION_LOCATION_COUNTRY = "IMPLEMENTATION_LOCATION_COUNTRY";
	private static final String IMPLEMENTATION_LOCATION_REGION = "IMPLEMENTATION_LOCATION_REGION";
	private static final String IMPLEMENTATION_LOCATION_ZONE = "IMPLEMENTATION_LOCATION_ZONE";
	private static final String IMPLEMENTATION_LOCATION_DISTRICT = "IMPLEMENTATION_LOCATION_DISTRICT";
	
	//Note: mappings are manually retrieved, because no certain way exists to map them 
	@SuppressWarnings("serial")
	protected static final Map<String, String> modulesToColumnsMap = new HashMap<String, String>() {{
		put("Activity Approval Process", ColumnConstants.APPROVAL_STATUS);
		put("/Activity Form/Contacts/Donor Contact Information", ColumnConstants.DONOR_CONTACT_ORGANIZATION);
		put("/Activity Form/Contacts/Sector Ministry Contact Information", ColumnConstants.SECTOR_MINISTRY_CONTACT_ORGANIZATION);
		put("/Activity Form/Contacts/Project Coordinator Contact Information", ColumnConstants.PROJECT_COORDINATOR_CONTACT_ORGANIZATION);
		put("/Activity Form/Contacts/Implementing Executing Agency Contact Information", ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_ORGANIZATION);
		put("/Activity Form/Contacts/Ministry Of Finance Contact Organization", ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_ORGANIZATION);
		put("/Activity Form/Budget Structure", ColumnConstants.BUDGET_STRUCTURE);
		put("/Activity Form/Cross Cutting Issues/Environment", ColumnConstants.ENVIRONMENT);
		put("/Activity Form/Cross Cutting Issues/Equal Opportunity", ColumnConstants.EQUAL_OPPORTUNITY);
		put("/Activity Form/Cross Cutting Issues/Minorities", ColumnConstants.MINORITIES);
		put("/Activity Form/Identification/A.C. Chapter", ColumnConstants.AC_CHAPTER);
		put("/Activity Form/Identification/Accession Instrument", ColumnConstants.ACCESSION_INSTRUMENT);
		put("/Activity Form/Identification/Activity Budget", ColumnConstants.ON_OFF_TREASURY_BUDGET);
		put("/Activity Form/Identification/Activity Status", ColumnConstants.STATUS);
		put("/Activity Form/Identification/Audit System", ColumnConstants.AUDIT_SYSTEM);
		put("/Activity Form/Identification/Budget Code Project ID", ColumnConstants.BUDGET_CODE_PROJECT_ID); 
		put("/Activity Form/Identification/Budget Extras/FY", ColumnConstants.FY);
		put("/Activity Form/Identification/Budget Extras/Indirect On Budget", ColumnConstants.INDIRECT_ON_BUDGET); 
		put("/Activity Form/Identification/Budget Extras/Ministry Code", ColumnConstants.MINISTRY_CODE);
		put("/Activity Form/Identification/Budget Extras/Project Code", ColumnConstants.PROJECT_CODE);
		put("/Activity Form/Identification/Budget Extras/Sub-Vote", ColumnConstants.SUB_VOTE);
		put("/Activity Form/Identification/Budget Extras/Sub-Program", ColumnConstants.SUB_PROGRAM);
		put("/Activity Form/Identification/Budget Extras/Vote", ColumnConstants.VOTE);
		put("/Activity Form/Identification/Cris Number", ColumnConstants.CRIS_NUMBER);
		put("/Activity Form/Identification/Financial Instrument", ColumnConstants.FINANCIAL_INSTRUMENT); 
		put("/Activity Form/Identification/Government Agreement Number", ColumnConstants.GOVERNMENT_AGREEMENT_NUMBER);
		put("/Activity Form/Identification/Government Approval Procedures", ColumnConstants.GOVERNMENT_APPROVAL_PROCEDURES);
		put("/Activity Form/Identification/Joint Criteria", ColumnConstants.JOINT_CRITERIA);
		put("/Activity Form/Identification/Institutions", ColumnConstants.INSTITUTIONS);
		put("/Activity Form/Identification/Procurement System", ColumnConstants.PROCUREMENT_SYSTEM);
		put("/Activity Form/Identification/Project Category", ColumnConstants.PROJECT_CATEGORY);
		put("/Activity Form/Identification/Project Comments", ColumnConstants.PROJECT_COMMENTS);
		put("/Activity Form/Identification/Project Impact", ColumnConstants.PROJECT_IMPACT);
		put("/Activity Form/Identification/Project Implementing Unit", ColumnConstants.PROJECT_IMPLEMENTING_UNIT);
		put("/Activity Form/Identification/Project Title", ColumnConstants.PROJECT_TITLE);
		put("/Activity Form/Identification/Purpose", ColumnConstants.PURPOSE);
		put("/Activity Form/Identification/Humanitarian Aid", ColumnConstants.HUMANITARIAN_AID);
		put("/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Disaster Response", ColumnConstants.DISASTER_RESPONSE_MARKER);
		put("/Activity Form/Identification/Objective", ColumnConstants.OBJECTIVE);
		put("/Activity Form/Identification/Reporting System", ColumnConstants.REPORTING_SYSTEM);
		put("/Activity Form/Identification/Results", ColumnConstants.RESULTS);
		put("/Activity Form/Funding/Overview Section/Type of Cooperation", ColumnConstants.TYPE_OF_COOPERATION);
		put("/Activity Form/Funding/Overview Section/Type of Implementation", ColumnConstants.TYPE_OF_IMPLEMENTATION);
		put("/Activity Form/Issues Section", ColumnConstants.ISSUES);
		put("/Activity Form/Issues Section/Issue/Measure/Actor", ColumnConstants.ISSUES___MEASURES___ACTORS);
		put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement", ColumnConstants.AGREEMENT_CODE);
		put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Financing Instrument", ColumnConstants.FINANCING_INSTRUMENT);
		put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Classification Date", ColumnConstants.FUNDING_CLASSIFICATION_DATE);
		put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Organization Id", ColumnConstants.FUNDING_ORGANIZATION_ID);
		put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Status", ColumnConstants.FUNDING_STATUS);
		put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Mode of Payment", ColumnConstants.MODE_OF_PAYMENT);
		put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Type of Assistence", ColumnConstants.TYPE_OF_ASSISTANCE);
		put("/Activity Form/Funding/Modalities", ColumnConstants.PLEDGES_AID_MODALITY);
		put("/Activity Form/Funding/Proposed Project Cost", ColumnConstants.PROPOSED_PROJECT_AMOUNT);
		put("/Activity Form/Location/Implementation Level", ColumnConstants.IMPLEMENTATION_LEVEL);
		put("/Activity Form/Location/Locations", ColumnConstants.LOCATION);
		put("/Activity Form/Planning/Actual Approval Date", ColumnConstants.ACTUAL_APPROVAL_DATE);
		put("/Activity Form/Planning/Actual Completion Date", ColumnConstants.ACTUAL_COMPLETION_DATE);
		put("/Activity Form/Planning/Actual Start Date", ColumnConstants.ACTUAL_START_DATE);
		put("/Activity Form/Planning/Final Date for Contracting", ColumnConstants.FINAL_DATE_FOR_CONTRACTING);
		put("/Activity Form/Planning/Final Date for Disbursements", ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS);
		put("/Activity Form/Planning/Proposed Approval Date", ColumnConstants.PROPOSED_APPROVAL_DATE);
		put("/Activity Form/Planning/Proposed Completion Date", ColumnConstants.PROPOSED_COMPLETION_DATE);
		put("/Activity Form/Planning/Proposed Project Life", ColumnConstants.PROPOSED_PROJECT_LIFE);
		put("/Activity Form/Planning/Proposed Start Date", ColumnConstants.PROPOSED_START_DATE);
		put("/Activity Form/Planning/Original Completion Date", ColumnConstants.ORIGINAL_COMPLETION_DATE);
		put("/Activity Form/Program/National Plan Objective", ColumnConstants.NATIONAL_PLANNING_OBJECTIVES);
		put("/Activity Form/Program/Primary Programs", ColumnConstants.PRIMARY_PROGRAM);
		put("/Activity Form/Program/Program Description", ColumnConstants.PROGRAM_DESCRIPTION);
		put("/Activity Form/Program/Secondary Programs", ColumnConstants.SECONDARY_PROGRAM);
		put("/Activity Form/Program/Tertiary Programs", ColumnConstants.TERTIARY_PROGRAM);
		put("/Activity Form/Regional Observations", ColumnConstants.REGIONAL_OBSERVATIONS);
		put("/Activity Form/Organizations/Beneficiary Agency", ColumnConstants.BENEFICIARY_AGENCY);
		put("/Activity Form/Organizations/Contracting Agency", ColumnConstants.CONTRACTING_AGENCY);
		put("/Activity Form/Organizations/Donor Organization", DONOR_ORAGNIZATION);
		put("/Activity Form/Funding/Search Funding Organizations", SEARCH_DONOR_ORAGNIZATION);
		put("/Activity Form/Organizations/Executing Agency", ColumnConstants.EXECUTING_AGENCY);
		put("/Activity Form/Organizations/Implementing Agency", ColumnConstants.IMPLEMENTING_AGENCY);
		put("/Activity Form/Organizations/Regional Group", ColumnConstants.REGIONAL_GROUP);
		put("/Activity Form/Organizations/Responsible Organization", ColumnConstants.RESPONSIBLE_ORGANIZATION);
		put("/Activity Form/Organizations/Sector Group", ColumnConstants.SECTOR_GROUP);
		put("/Activity Form/Sectors/Primary Sectors", ColumnConstants.PRIMARY_SECTOR);
		put("/Activity Form/Sectors/Secondary Sectors", ColumnConstants.SECONDARY_SECTOR);
		put("/Activity Form/Sectors/Tertiary Sectors", ColumnConstants.TERTIARY_SECTOR);
		put("/Activity Form/Structures", ColumnConstants.STRUCTURES_COLUMN);
	}};
	
	@SuppressWarnings("serial")
	public static final Map<String, String> dependencyMap= new HashMap<String, String>() {{
		put(ColumnConstants.ACTIVITY_ID, ColumnConstants.PROJECT_TITLE);
		put(ColumnConstants.INTERNAL_USE_ID, ColumnConstants.PROJECT_TITLE);
		put(ColumnConstants.PRIMARY_SECTOR_ID, ColumnConstants.PRIMARY_SECTOR);
		put(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, ColumnConstants.PRIMARY_SECTOR);
		put(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR_ID, ColumnConstants.PRIMARY_SECTOR);
		put(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.PRIMARY_SECTOR);
		put(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR_ID, ColumnConstants.PRIMARY_SECTOR);
		put(ColumnConstants.SECONDARY_SECTOR_ID, ColumnConstants.SECONDARY_SECTOR);
		put(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, ColumnConstants.SECONDARY_SECTOR);
		put(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR_ID, ColumnConstants.SECONDARY_SECTOR);
		put(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.SECONDARY_SECTOR);
		put(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR_ID, ColumnConstants.SECONDARY_SECTOR);
		put(ColumnConstants.TERTIARY_SECTOR_ID, ColumnConstants.TERTIARY_SECTOR);
		put(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR, ColumnConstants.TERTIARY_SECTOR);
		put(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR_ID, ColumnConstants.TERTIARY_SECTOR);
		put(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.TERTIARY_SECTOR);
		put(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR_ID, ColumnConstants.TERTIARY_SECTOR);
		put(ColumnConstants.AGREEMENT_CLOSE_DATE, ColumnConstants.AGREEMENT_CODE);
		put(ColumnConstants.AGREEMENT_EFFECTIVE_DATE, ColumnConstants.AGREEMENT_CODE);
		put(ColumnConstants.AGREEMENT_SIGNATURE_DATE, ColumnConstants.AGREEMENT_CODE);
		put(ColumnConstants.AGREEMENT_TITLE_CODE, ColumnConstants.AGREEMENT_CODE);
		put(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_DETAIL, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES);
		put(ColumnConstants.PRIMARY_PROGRAM_DETAIL, ColumnConstants.PRIMARY_PROGRAM);
		put(ColumnConstants.SECONDARY_PROGRAM_DETAIL, ColumnConstants.SECONDARY_PROGRAM);
		put(ColumnConstants.TERTIARY_PROGRAM_DETAIL, ColumnConstants.TERTIARY_PROGRAM);
		
		String[][] colPrefixList = new String[][]{
			{"National Planning Objectives Level", ColumnConstants.NATIONAL_PLANNING_OBJECTIVES},
			{"Primary Program Level",  ColumnConstants.PRIMARY_PROGRAM},
			{"Secondary Program Level", ColumnConstants.SECONDARY_PROGRAM},
			{"Tertiary Program Level", ColumnConstants.TERTIARY_PROGRAM}
		};
		for (String[] colPrefix : colPrefixList) {
			for (int i = 1; i < 9 ; i++) {
				put(colPrefix[0] + " " + i, colPrefix[1]);
				put(colPrefix[0] + " " + i + " Id", colPrefix[1]);
			}
		}
		
		put(ColumnConstants.DONOR_ID, ColumnConstants.DONOR_AGENCY);
		put(ColumnConstants.DONOR_GROUP, ColumnConstants.DONOR_AGENCY);
		put(ColumnConstants.DONOR_TYPE, ColumnConstants.DONOR_AGENCY);
		put(ColumnConstants.DONOR_COMMITMENT_DATE, ColumnConstants.DONOR_AGENCY);
		put(ColumnConstants.RESPONSIBLE_ORGANIZATION_ID, ColumnConstants.RESPONSIBLE_ORGANIZATION);
		put(ColumnConstants.RESPONSIBLE_ORGANIZATION_DEPARTMENT_DIVISION, ColumnConstants.RESPONSIBLE_ORGANIZATION);
		put(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS, ColumnConstants.RESPONSIBLE_ORGANIZATION);
		put(ColumnConstants.EXECUTING_AGENCY_ID, ColumnConstants.EXECUTING_AGENCY);
		put(ColumnConstants.EXECUTING_AGENCY_DEPARTMENT_DIVISION, ColumnConstants.EXECUTING_AGENCY);
		put(ColumnConstants.EXECUTING_AGENCY_GROUPS, ColumnConstants.EXECUTING_AGENCY);
		put(ColumnConstants.EXECUTING_AGENCY_TYPE, ColumnConstants.EXECUTING_AGENCY);
		put(ColumnConstants.IMPLEMENTING_AGENCY_DEPARTMENT_DIVISION, ColumnConstants.IMPLEMENTING_AGENCY);
		put(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, ColumnConstants.IMPLEMENTING_AGENCY);
		put(ColumnConstants.IMPLEMENTING_AGENCY_ID, ColumnConstants.IMPLEMENTING_AGENCY);
		put(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, ColumnConstants.IMPLEMENTING_AGENCY);
		put(ColumnConstants.BENEFICIARY_AGENCY__DEPARTMENT_DIVISION, ColumnConstants.BENEFICIARY_AGENCY);
		put(ColumnConstants.BENEFICIARY_AGENCY_GROUPS, ColumnConstants.BENEFICIARY_AGENCY);
		put(ColumnConstants.BENEFICIARY_AGENCY_ID, ColumnConstants.BENEFICIARY_AGENCY);
		put(ColumnConstants.CONTRACTING_AGENCY_ACRONYM, ColumnConstants.CONTRACTING_AGENCY);
		put(ColumnConstants.CONTRACTING_AGENCY_DEPARTMENT_DIVISION, ColumnConstants.CONTRACTING_AGENCY);
		put(ColumnConstants.CONTRACTING_AGENCY_GROUPS, ColumnConstants.CONTRACTING_AGENCY);
		put(ColumnConstants.CONTRACTING_AGENCY_ID, ColumnConstants.CONTRACTING_AGENCY);
		put(ColumnConstants.SECTOR_GROUP_DEPARTMENT_DIVISION, ColumnConstants.SECTOR_GROUP);
		put(ColumnConstants.REGIONAL_GROUP_DEPARTMENT_DIVISION, ColumnConstants.REGIONAL_GROUP);
		
		String[][] contacts = new String[][] {
			{"Donor Contact", ColumnConstants.DONOR_CONTACT_ORGANIZATION}, 
			{"Sector Ministry Contact", ColumnConstants.SECTOR_MINISTRY_CONTACT_ORGANIZATION},
			{"Project Coordinator Contact", ColumnConstants.PROJECT_COORDINATOR_CONTACT_ORGANIZATION},
			{"Implementing/Executing Agency Contact", ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_ORGANIZATION},
			{"Ministry Of Finance Contact", ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_ORGANIZATION},
			{"Pledge Contact 1 -", ColumnConstants.PLEDGE_CONTACT_1___MINISTRY},
			{"Pledge Contact 2 -", ColumnConstants.PLEDGE_CONTACT_2___MINISTRY}
		};
		String[] suffixList = new String[] {"Email", "Fax", "Name", "Phone", "Title",
				//and pledges specific
				"Address", "Alternate Contact", "Telephone"};
		for (String[] con : contacts)
			for (String suffix : suffixList)
				put(con[0] + " " + suffix, con[1]);
		
		put(ColumnConstants.GEOCODE, ColumnConstants.LOCATION);
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
		put("Physical Progress", ColumnConstants.PHYSICAL_PROGRESS);
	}};
	
	@SuppressWarnings("serial")
	protected static final Map<String, String> fieldsToColumnsMap = new HashMap<String, String>() {{
		put("AMP ID", ColumnConstants.AMP_ID);
		put("Activity Approved By", ColumnConstants.ACTIVITY_APPROVED_BY);
		put("Activity Created By", ColumnConstants.ACTIVITY_CREATED_BY);
		put("Activity Created On", ColumnConstants.ACTIVITY_CREATED_ON);
		put("Activity Pledges Title", ColumnConstants.ACTIVITY_PLEDGES_TITLE);
		put("Activity Updated By", ColumnConstants.ACTIVITY_UPDATED_BY);
		put("Activity Updated On", ColumnConstants.ACTIVITY_UPDATED_ON);
		put("Actors", ColumnConstants.ACTORS);
		put("Age of Project (Months)", ColumnConstants.AGE_OF_PROJECT_MONTHS);
		put("Archived", ColumnConstants.ARCHIVED);
		put("Average Disbursement Rate", ColumnConstants.AVERAGE_DISBURSEMENT_RATE);
		put("Average Size of Disbursements", ColumnConstants.AVERAGE_SIZE_OF_DISBURSEMENTS);
		put("Average Size of Projects", ColumnConstants.AVERAGE_SIZE_OF_PROJECTS);
		put("Budget Department", ColumnConstants.BUDGET_DEPARTMENT);
		put("Budget Organization", ColumnConstants.BUDGET_ORGANIZATION);
		put("Budget Program", ColumnConstants.BUDGET_PROGRAM);
		put("Budget Sector", ColumnConstants.BUDGET_SECTOR);
		put("Calculated Project Life", ColumnConstants.CALCULATED_PROJECT_LIFE);
		put("Capital - Expenditure", ColumnConstants.CAPITAL___EXPENDITURE);
		put("Component Name", ColumnConstants.COMPONENT_NAME);
		put("Component description", ColumnConstants.COMPONENT_DESCRIPTION);
		put("Component Funding Organization", ColumnConstants.COMPONENT_FUNDING_ORGANIZATION);
		put("Component Type", ColumnConstants.COMPONENT_TYPE);
		put("Costing Total Contribution", ColumnConstants.COSTING_DONOR);
		put("Credit/Donation", ColumnConstants.CREDIT_DONATION);
		put("Cumulative Commitment", ColumnConstants.CUMULATIVE_COMMITMENT);
		put("Cumulative Disbursement", ColumnConstants.CUMULATIVE_DISBURSEMENT);
		put("Current Completion Date Comments", ColumnConstants.CURRENT_COMPLETION_DATE_COMMENTS);
		put("Description of Component Funding", ColumnConstants.DESCRIPTION_OF_COMPONENT_FUNDING);
		put("Draft", ColumnConstants.DRAFT);
		put("Execution Rate", ColumnConstants.EXECUTION_RATE);
		put("Final Date for Disbursements Comments", ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS_COMMENTS);
		put("Funding end date", ColumnConstants.FUNDING_END_DATE);
		put("Funding start date", ColumnConstants.FUNDING_START_DATE);
		put("Grand Total Cost", ColumnConstants.GRAND_TOTAL_COST);
		put("Measures Taken", ColumnConstants.MEASURES_TAKEN);
		put("Ministry Of Finance Contact Organization", ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_ORGANIZATION);
		put("Multi Donor", ColumnConstants.MULTI_DONOR);
		put("Organizations and Project ID", ColumnConstants.ORGANIZATIONS_AND_PROJECT_ID);
		put("Overage", ColumnConstants.OVERAGE);
		put("Overage Project", ColumnConstants.OVERAGE_PROJECT);
		put("Payment Capital - Recurrent", ColumnConstants.PAYMENT_CAPITAL___RECURRENT);
		put("Physical progress description", ColumnConstants.PHYSICAL_PROGRESS_DESCRIPTION);
		put("Physical progress title", ColumnConstants.PHYSICAL_PROGRESS_TITLE);
		put("Pledges sectors", ColumnConstants.PLEDGES_SECTORS);
		put("Pledges Aid Modality", ColumnConstants.PLEDGES_AID_MODALITY);
		put("Pledge Contact 1 - Organization", ColumnConstants.PLEDGE_CONTACT_1___MINISTRY);
		put("Pledge Contact 2 - Organization", ColumnConstants.PLEDGE_CONTACT_2___MINISTRY);
		put("Pledge Status", ColumnConstants.PLEDGE_STATUS);
		put("Pledges Detail Date Range", ColumnConstants.PLEDGES_DETAIL_DATE_RANGE);
		put("Pledges Detail End Date", ColumnConstants.PLEDGES_DETAIL_END_DATE);
		put("Pledges Detail Start Date", ColumnConstants.PLEDGES_DETAIL_START_DATE);
		put("Pledges Districts", ColumnConstants.PLEDGES_DISTRICTS);
		put("Pledges Donor Group", ColumnConstants.PLEDGES_DONOR_GROUP);
		put("Pledges National Plan Objectives", ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES);
		put("Pledges Programs", ColumnConstants.PLEDGES_PROGRAMS);
		put("Pledges Regions", ColumnConstants.PLEDGES_REGIONS);
		put("Pledges Secondary Programs", ColumnConstants.PLEDGES_SECONDARY_PROGRAMS);
		put("Pledges Secondary Sectors", ColumnConstants.PLEDGES_SECONDARY_SECTORS);
		put("Pledges Sectors", ColumnConstants.PLEDGES_SECTORS);
		put("Pledges Tertiary Programs", ColumnConstants.PLEDGES_TERTIARY_PROGRAMS);
		put("Pledges Tertiary Sectors", ColumnConstants.PLEDGES_TERTIARY_SECTORS);
		put("Pledges Type Of Assistance", ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE);
		put("Pledges Titles", ColumnConstants.PLEDGES_TITLES);
		put("Pledges Zones", ColumnConstants.PLEDGES_ZONES);
		put("Predictability of Funding", ColumnConstants.PREDICTABILITY_OF_FUNDING);
		put("Project Age Ratio", ColumnConstants.PROJECT_AGE_RATIO);
		put("Project Description", ColumnConstants.PROJECT_DESCRIPTION);
		put("Project Period", ColumnConstants.PROJECT_PERIOD);
		put("Related Projects", ColumnConstants.RELATED_PROJECTS);
		put("Related Pledges", ColumnConstants.RELATED_PLEDGES);
		put("Sector Location", ColumnConstants.SECTOR_LOCATION);
		put("Sector Tag", ColumnConstants.SECTOR_TAG);
		put("Sector Tag Sub-Sector", ColumnConstants.SECTOR_TAG_SUB_SECTOR);
		put("Sector Tag Sub-Sub-Sector", ColumnConstants.SECTOR_TAG_SUB_SUB_SECTOR);
		put("SSC Modalities", ColumnConstants.SSC_MODALITIES);
		put("Variance Of Commitments", ColumnConstants.VARIANCE_OF_COMMITMENTS);
		put("Variance Of Disbursements", ColumnConstants.VARIANCE_OF_DISBURSEMENTS);
		put(ColumnConstants.UNCOMMITTED_CUMULATIVE_BALANCE, ColumnConstants.UNCOMMITTED_CUMULATIVE_BALANCE);
		put(ColumnConstants.UNDISBURSED_CUMULATIVE_BALANCE, ColumnConstants.UNDISBURSED_CUMULATIVE_BALANCE);
		put("Disaster Response Marker", ColumnConstants.DISASTER_RESPONSE_MARKER);
	}};
	
	protected static final List<String> visibleByDefault = Arrays.asList(
			ColumnConstants.ACTIVITY_COUNT,
			ColumnConstants.FUNDING_YEAR,
			ColumnConstants.TEAM_ID,
			ColumnConstants.TEAM,
			ColumnConstants.DONOR_AGENCY,
			ColumnConstants.RELATED_PLEDGES,
			ColumnConstants.RELATED_PROJECTS
	);

	/**
	 * Dependency map between column and category value 
	 */
	@SuppressWarnings("serial")
	protected static final Map<String, HardCodedCategoryValue> categoryValueDependency = new HashMap<String, HardCodedCategoryValue>() {{
		put(IMPLEMENTATION_LOCATION_COUNTRY, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
		put(IMPLEMENTATION_LOCATION_REGION, CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
		put(IMPLEMENTATION_LOCATION_ZONE, CategoryConstants.IMPLEMENTATION_LOCATION_ZONE);
		put(IMPLEMENTATION_LOCATION_DISTRICT, CategoryConstants.IMPLEMENTATION_LOCATION_DISTRICT);
	}};
	
	
	protected static final Map<String, Collection<String>> dependencyTypeAll = new HashMap<String, Collection<String>>() {{
		put(ColumnConstants.COUNTRY, Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_COUNTRY));
		put(ColumnConstants.REGION, Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_REGION));
		put(ColumnConstants.ZONE, Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_ZONE));
		put(ColumnConstants.DISTRICT, Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_DISTRICT));
	}};
}
