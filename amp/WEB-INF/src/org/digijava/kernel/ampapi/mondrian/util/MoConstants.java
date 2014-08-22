package org.digijava.kernel.ampapi.mondrian.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Diego Dimunzio
 * @since March 12 2009 - AMP 1.13
 */
public final class MoConstants {

	//AMP Cubes
	public static String CONNECTION_DS = "jdbc:mondrian:Datasource=java:comp/env/ampDS";
	public static final String SCHEMA_PATH = "WEB-INF/queries/AMP.xml".replace("/", System.getProperty("file.separator"));
	public static final String FUNDING_CUBE_NAME = "Donor Funding";
	public static final String DEFAULT_CUBE_NAME = FUNDING_CUBE_NAME;
	
	//Query
	public static final String QUERY_NAME_KEY = "QUERY_NAME_KEY";
	public static final String DEFAULT_QUERY_NAME = "Anonymous";
	public static final String COLUMNS = "COLUMNS";
	public static final String ROWS = "ROWS";
	public static final String FILTER = "FILTER";
	public static final String MEASURE = "Measure";
	public static final String MEASURES = "Measures";
	public static final String MEMBERS = "Members";
	public static final String MEMBER = "MEMBER";
	public static final String CURRENT_MEMBER = "CurrentMember";
	public static final String FIRST_CHILD = "FirstChild";
	public static final String LAST_CHILD = "LastChild";
	public static final String PROPERTIES = "Properties";
	public static final String MEMBER_NAME = "name";
	public static final String FUNC_CROSS_JOIN = "CrossJoin";
	public static final String FUNC_CROSS_JOIN_FORMAT = "CrossJoin(%s, %s)";
	public static final String FUNC_HIERARCHIZE = "Hierarchize";
	public static final String FUNC_INTERSECT = "Intersect";
	public static final String FUNC_UNION = "Union";
	public static final String FUNC_UNION_FORMAT = "Union(%s, %s)";
	public static final String FUNC_ORDER = "Order";
	public static final String FUNC_FILTER = "Filter";
	public static final String FUNC_CAST = "Cast";
	//Activity
	public static String AMP_ACTIVITY_TABLE = "(\\bamp_activity\\b)";
	public static String CACHED_ACTIVITY_TABLE = "cached_amp_activity";
	//Dimensions
	public static final String PROJECT_TITLE = "Project Title"; //TODO: why cube def is split into separate dimensions for this info coming from same physical table
	public static final String APPROVAL_STATUS = "Approval Status"; //TODO: why cube def is split into separate dimensions for this info coming from same physical table
	public static final String AMP_ID = "AMP ID"; //TODO: why cube def is split into separate dimensions for this info coming from same physical table
	public static final String DATES = "Dates";
	public static final String LOCATION = "Location"; 
	public static final String PRIMARY_SECTOR = "Primary Sector";
	public static final String SECONDARY_SECTOR = "Secondary Sector";
	public static final String TERTIARY_SECTOR = "Tertiary Sector";
	public static final String PRIMARY_PROGRAMS = "Primary Program";
	public static final String SECONDARY_PROGRAMS = "Secondary Program";
	public static final String TERTIARY_PROGRAMS = "Tertiary Program";
	public static final String NATIONAL_OBJECTIVES = "National Objectives";
	public static final String DONOR_AGENCY = "Donor Agency";
	public static final String IMPLEMENTING_AGENCY = "Implementing Agency";
	public static final String EXECUTING_AGENCY = "Executing Agency";
	public static final String BENEFICIARY_AGENCY = "Beneficiary Agency";
	public static final String RESPONSIBLE_AGENCY = "Responsible Organization";
	//Hierarchies
	public static final String H_DATES = "Dates";
	//public static final String H_DATES_DUPLICATE = "DatesDuplicate";
	public static final String H_YEAR = "Year";
	public static final String H_QUARTER = "Quarter";
	public static final String H_MONTH = "Month";
	public static final String H_ORGANIZATIONS = "Organization Hierarchy";
	public static final String H_ORG_TYPE_NAME = "Organization Type Name";
	public static final String H_ORG_GROUP_NAME = "Organization Group Name";
	public static final String H_ORG_NAME = "All Orgs";
	//public static final String H_ORG_DUPLICATE = "Organization Hierarchy Duplicate";
	public static final String H_LOCATIONS = "Locations";
	//public static final String H_LOCATIONS_DUPLICATE= "LocationsDuplicate";
	//Attributes/Levels
	public static final String ATTR_PROJECT_TITLE = "Project Title";
	public static final String ATTR_APPROVAL_STATUS = "Approval Status";
	public static final String ATTR_AMP_ID = "AMP ID";
	public static final String ATTR_STATUS_NAME = "Status";
	public static final String ATTR_YEAR = "Year";
	public static final String ATTR_QUARTER = "Quarter";
	public static final String ATTR_MONTH = "Month";
	public static final String ATTR_DATE = "Date";
	public static final String ATTR_DAY = "Day";
	public static final String ATTR_ALL_DATES = "All Periods";
	public static final String ATTR_COUNTRY_NAME = "Country Name";
	public static final String ATTR_REGION_NAME = "Region Name";
	public static final String ATTR_ZONE_NAME = "Zone Name";
	public static final String ATTR_DISTRICT_NAME = "District Name";
	public static final String ATTR_LOCATION_NAME = "Location";
	public static final String ATTR_PRIMARY_SECTOR_NAME = "Primary Sector";
	public static final String ATTR_PRIMARY_SECTOR_SUB_SECTOR_NAME = "Primary Sector Sub-sector";
	public static final String ATTR_PRIMARY_SECTOR_SUB_SUB_SECTOR_NAME = "Primary Sector Sub-sub-sector";
	public static final String ATTR_SECONDARY_SECTOR_NAME = "Secondary Sector";
	public static final String ATTR_SECONDARY_SUB_SECTOR_NAME = "Secondary Sector Sub-sector";
	public static final String ATTR_SECONDARY_SUB_SUB_SECTOR_NAME = "Secondary Sector Sub-sub-sector";
	public static final String ATTR_TETRIARY_SECTOR = "Tertiary Sector";
	public static final String ATTR_TETRIARY_SUB_SECTOR = "Tertiary Sector Sub-sector";
	public static final String ATTR_TETRIARY_SUB_SUB_SECTOR = "Tertiary Sector Sub-sub-sector";
	public static final String ATTR_ORG_TYPE_NAME = "Organization Type Name";
	public static final String ATTR_ORG_GROUP_NAME = "Organization Group Name";
	public static final String ATTR_ORG_NAME = "Organization Name";
	public static final String ATTR_ORG_CODE = "Organization Code";
	public static final String ATTR_PROGRAM_LEVEL_0_NAME = "Program Level 0 Name";
	public static final String ATTR_PROGRAM_LEVEL_1_NAME = "Program Level 1 Name";
	public static final String ATTR_PROGRAM_LEVEL_2_NAME = "Program Level 2 Name";
	public static final String ATTR_PROGRAM_LEVEL_3_NAME = "Program Level 3 Name";
	public static final String ATTR_PROGRAM_LEVEL_4_NAME = "Program Level 4 Name";
	public static final String ATTR_PROGRAM_LEVEL_5_NAME = "Program Level 5 Name";
	public static final String ATTR_PROGRAM_LEVEL_6_NAME = "Program Level 6 Name";
	public static final String ATTR_PROGRAM_LEVEL_7_NAME = "Program Level 7 Name";
	public static final String ATTR_PROGRAM_LEVEL_8_NAME = "Program Level 8 Name";
	//properties
	public static final String P_KEY = "Key";
	public static final String P_YEAR = "year";
	public static final String P_DATE = "date";
	public static final String P_COUNTRY_ID = "countryId";
	public static final String P_REGION_ID = "regionId";
	public static final String P_ZONE_ID = "zoneId";
	public static final String P_DISTRIC_ID = "districtId";
	public static final String P_LOCATION_ID = "locationId";
	public static final String P_ORG_TYPE_ID = "orgTypeId";
	public static final String P_ORG_GROUP_ID = "orgGroupId";
	public static final String P_ORG_ID = "orgId";
	public static final String P_PRIMARY_SECTOR = "primarySectorId";
	public static final String P_PRIMARY_SECTOR_SUB_SECTOR = "primarySectorSubSectorId";
	public static final String P_PRIMARY_SECTOR_SUB_SUB_SECTOR = "primarySectorSubSubSectorId";
	public static final Set<String> AMP_SCHEMA_PROPERTIES = new HashSet<String>() {{
		add(P_KEY); add(P_YEAR); add(P_DATE); add(P_COUNTRY_ID); add(P_REGION_ID); add(P_ZONE_ID); add(P_DISTRIC_ID); add(P_LOCATION_ID);
		add(P_ORG_TYPE_ID); add(P_ORG_GROUP_ID); add(P_ORG_ID); add(P_PRIMARY_SECTOR); add(P_PRIMARY_SECTOR_SUB_SECTOR); add(P_PRIMARY_SECTOR_SUB_SUB_SECTOR);
	}};
	
	public static String ALL_PRIMARY_SECTOR = "All Primary Sectors";
	public static String ALL_SECONDARY_SECTOR = "All Secondary Sectors";
	public static String ALL_ACTIVITIES = "All Activities";
	public static String ALL_PERIODS = "All Periods";
	public static String ALL_REGIONS = "All Regions";
	public static String ALL_PROGRAMS = "All Programs";
	public static String ALL_STATUS = "All Status";
	public static String ALL_DONOR = "All Donors";
	public static String ALL_DONOR_TYPES = "All Donors Types";
	public static String ALL_DONOR_GROUP = "All Donors Group";
	public static String ALL_FINANCING_INTRUMENT= "All Financing Instruments";
	public static String ALL_TERMS_OF_ASSISTANCE= "All Terms of Assistance";
	public static String ALL_SUB_SECTORS = "All Sub-Sectors";
	public static String ALL_SUB_SUB_SECTORS = "All Sub-Sub-Sectors";
	public static String ALL_CURRENCIES = "All Currencies";
	
	//Measures
	public static String RAW_ACTUAL_COMMITMENTS = "Raw Actual Commitments";
	public static String RAW_ACTUAL_DISBURSEMENTS = "Raw Actual Disbursements";
	public static String RAW_ACTUAL_EXPENDITURES = "Raw Actual Expenditures";
	public static String RAW_PLANNED_COMMITMENTS = "Raw Planned Commitments";
	public static String RAW_PLANNED_DISBURSEMENTS = "Raw Planned Disbursements";
	public static String RAW_PLANNED_EXPENDITURES = "Raw Planned Expenditures";
	public static String ACTUAL_COMMITMENTS = "Actual Commitments";
	public static String ACTUAL_DISBURSEMENTS = "Actual Disbursements";
	public static String ACTUAL_EXPENDITURES = "Actual Expenditures";
	public static String PLANNED_COMMITMENTS = "Planned Commitments";
	public static String PLANNED_DISBURSEMENTS = "Planned Disbursements";
	public static String PLANNED_EXPENDITURES = "Planned Expenditures";
	public static String ACTIVITY_COUNT = "Activity Count";
	
	
	//Pledges Constant
	public static String PLEDGES_MEASURE = "Pledges Total";
	public static String PLEDGE_TITTLE = "Tilte";
	public static String PLEDGE_ALL_TITTLE = "All Titles";
	public static String PLEDGE_TYPE_OF_ASSINETANCE = "Type of Assistance";
	public static String PLEDGE_ALL_TYPE_OF_ASSINETANCE = "All Type of Assistance";
	public static String PLEDGE_AID_MODALITY = "Aid Modality";
	public static String PLEDGE_ALL_AID_MODALITY = "All Aid Modality";
	public static String PLEDGE_PLEDGES_DATES = "Pledges Dates";
	public static String PLEDGE_ALL_PLEDGES_DATES = "All Pledges Dates";
	public static String PLEDGE_ALL_PLEDGES_TYPES = "All Pledges Types";
	public static String PLEDGE_PLEDGES_TYPES = "Pledges Types";
	public static String PLEDGE_PLEDGES_CONTACT_NAME = "Contact Name";
	public static String PLEDGE_PLEDGES_CONTACT_EMAIL = "Contact Email";
	public static String PLEDGE_PLEDGES_COMMITMENTS = "Pledges Actual Commitments";
	public static String PLEDGE_PLEDGES_DISBURSEMENTS = "Pledges Actual Disbursements";
	public static String PLEDGE_PLEDGES_COMMITMENTS_GAP = "Commitment Gap";
	
	//Months
	public static String MONTH_JANUARY = "january";
	public static String MONTH_FEBRUARY = "february";
	public static String MONTH_MARCH = "march";
	public static String MONTH_APRIL = "april";
	public static String MONTH_MAY = "may";
	public static String MONTH_JUNE = "june";
	public static String MONTH_JULY = "july";
	public static String MONTH_AGOUST = "august";
	public static String MONTH_SEPTEMBER = "september";
	public static String MONTH_OCTOBER = "october";
	public static String MONTH_NOVEMBER = "november";
	public static String MONTH_DECEMBER = "december";

	//OLD CUBE refs - to be removed
	public static String OLD_ACTIVITY = "Activity";
	public static String OLD_STATUS = "Status";
	public static String OLD_DONOR = "Donor";
	public static String OLD_DONOR_TYPES = "Donor Types";
	public static String OLD_DONOR_GROUP = "Donor Group";
	public static String OLD_REGIONS = "Regions";
	public static String OLD_DONOR_DATES = "Donor Dates";
	
	public static String OLD_NATIONAL_PROGRAM= "National Program";
	public static String OLD_SECTORS= "Sectors";
	public static String OLD_SUB_SECTORS = "Primary Sector Sub-Sectors";
	public static String OLD_SUB_SUB_SECTORS = "Primary Sector Sub-Sub-Sectors";
	public static String OLD_SEC_SUB_SECTORS = "Secondary Sector Sub-Sectors";
	public static String OLD_SEC_SUB_SUB_SECTORS = "Secondary Sector Sub-Sub-Sectors";
	public static String OLD_CURRENCY = "currency";
	public static String OLD_FINANCING_INTRUMENT= "Financing Instrument";
	public static String OLD_TERMS_OF_ASSISTANCE= "Terms of Assistance";

	public static final String DATE_FORMAT = "YYYY-MM-dd";
	public static final String HAS_AMP_PROPERTIES = "HAS_AMP_PROPERTIES";
	
}
