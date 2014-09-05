package org.digijava.kernel.ampapi.mondrian.util;

/**
 * 
 * @author Diego Dimunzio
 * @since July 2014 - Mondrian from-scratch reimplementation
 */
public final class MoConstants {

	//AMP Cubes
	public static String CONNECTION_DS = "jdbc:mondrian:Datasource=java:comp/env/monetDS";
	public static final String SCHEMA_PATH = "WEB-INF/saiku/saiku-repository/AMP.xml";//.replace("/", System.getProperty("file.separator"));
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
	public static final String H_PRIMARY_SECTOR = "Primary Sector";
	public static final String H_PROJECT_TITLE = "Project Title";
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
	
	//Measures
	public static String ACTUAL_COMMITMENTS = "Actual Commitments";
	public static String ACTUAL_DISBURSEMENTS = "Actual Disbursements";
	public static String ACTUAL_EXPENDITURES = "Actual Expenditures";
	public static String PLANNED_COMMITMENTS = "Planned Commitments";
	public static String PLANNED_DISBURSEMENTS = "Planned Disbursements";
	public static String PLANNED_EXPENDITURES = "Planned Expenditures";
	
	
	//Pledges Constant
	public static String PLEDGE_PLEDGES_COMMITMENTS = "Pledges Actual Commitments";
	public static String PLEDGE_PLEDGES_DISBURSEMENTS = "Pledges Actual Disbursements";
	public static String PLEDGE_PLEDGES_COMMITMENTS_GAP = "Commitment Gap";
	
	public static final String DATE_FORMAT = "YYYY-MM-dd";
	public static final String HAS_AMP_PROPERTIES = "HAS_AMP_PROPERTIES";
}
