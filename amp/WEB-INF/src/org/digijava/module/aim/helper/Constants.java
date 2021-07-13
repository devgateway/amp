package org.digijava.module.aim.helper ;

import java.awt.Color;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.digijava.module.aim.dbentity.ApprovalStatus;

public final class Constants {

    private Constants() {
    }

    public static class GlobalSettings {
        public static final String YEAR_RANGE_START         = "Year Range Start";
        public static final String NUMBER_OF_YEARS_IN_RANGE = "Number of Years in Range";
        public static final String START_YEAR_DEFAULT_VALUE = "Change Range Default Start Value";
        public static final String END_YEAR_DEFAULT_VALUE       = "Change Range Default End Value";
        public static final String DECIMAL_LOCATION_PERCENTAGES_DIVIDE = "Decimals in Location percentage";
    };


    /**
     * transaction type correspondence can be found in
     * CategAmountColWorker.java look for "switch (tr_type) {"
     *
     */
    public static final int COMMITMENT = 0 ;
    public static final int DISBURSEMENT = 1 ;
    public static final int EXPENDITURE = 2 ;
    public static final int MTEFPROJECTION = 3 ;
    public static final int DISBURSEMENT_ORDER = 4 ;
    public static final int PLEDGES_COMMITMENT = 5;
    public static final int PLEDGES_DISBURSEMENT = 6;
    public static final int PLEDGE = 7;
    public static final int RELEASE_OF_FUNDS = 8 ;
    public static final int ESTIMATED_DONOR_DISBURSEMENT = 9;
    public static final int ARREARS = 10;
    public static final int ANNUAL_PROPOSED_PROJECT_COST = 15;
    
    /**
     * only present in zz_v_ni_donor_funding.xml
     */
    public static final int PROPOSED_PROJECT_AMOUNT_PER_PROJECT = 11;
    
    /**
     * dummy transaction type for the ESRI option "commitments + disbursements"
     */
    public static final int TRANSACTION_TYPE_COMMITMENTS_AND_DISBURSEMENTS = 127;
    
    
    public static final int PLANNED = 0 ;
    public static final int ACTUAL = 1 ;
    public static final int ADJUSTMENT_TYPE_PIPELINE = 2;
    public static final int FROM_YEAR = 1983 ;
    public static final int TO_YEAR = 2005 ;

    public static final String DISCREPANCY = "DI";
    public static final Long MOFED_ORG_ID= new Long(12);
    public static final String DEFAULT_CURRENCY = "USD" ;
    //public static final Long ETH_FY = new Long(1) ;
    //public static final Long ETH_CAL = new Long(5) ;
    //public static final Long GREGORIAN = new Long(4) ;
    public static final double USD = 1.0 ;
    public static final String ACTIVE_TAB_COLOR = "white" ;
    public static final String INACTIVE_TAB_COLOR = "aqua" ;
    public static final String ACTIVE_MAIN_TAB_CLASS = "sub-nav-selected" ;
    public static final String INACTIVE_MAIN_TAB_CLASS = "sub-nav" ;
    public static final String ACTIVE_SUB_TAB_CLASS = "sub-nav2-selected" ;
    public static final String INACTIVE_SUB_TAB_CLASS = "sub-nav2" ;
    public static final String ORGANIZATION = "OG" ;
    public static final String REPORTING_AGENCY = "RA" ;
    public static final String FUNDING_AGENCY = "DN" ;
    public static final String IMPLEMENTING_AGENCY = "IA" ;
    public static final String BENEFICIARY_AGENCY = "BA" ;
    public static final String CONTRACTING_AGENCY = "CA" ;
    public static final String REGIONAL_GROUP = "RG" ;
    public static final String SECTOR_GROUP = "SG" ;
    public static final String EXECUTING_AGENCY = "EA";
    public static final String RESPONSIBLE_ORGANISATION = "RO";
    public static final String COMPONENT_FUNDING_ORGANIZATION = "CF";
    public static final String COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION = "CS";
    public static final String CONTRACTOR = "CT";
    public static final String RELATED_INSTITUTIONS = "RL" ;
    public static final int NUM_RECORDS = 10 ;
    public static final String LOAN = "L" ;
    public static final String GRANT = "G" ;
    public static final Long DESKTOP = new Long(1) ;
    public static final Long CALENDAR = new Long(1) ;
    public static final Long CURRENCY = new Long(2) ;
    public static final Long DONORS = new Long(3) ;
    public static final Long REGION = new Long(4) ;
    public static final Long SECTOR = new Long(5) ;
    public static final Long STATUS = new Long(6) ;
    public static final Long YEAR_RANGE = new Long(7) ;
    public static final Long PERSPECTIVE = new Long(8) ;
    public static final Long FINANCING_INSTRUMENT = new Long(9) ;
    public static final Long ACTUAL_PLANNED = new Long(10) ;
    public static final Long STARTDATE_CLOSEDATE = new Long(11) ;
// Report Pages Constants Set
    public static final Long QUARTER_PIPELINE = new Long(9);
    public static final Long QUARTERLYBYSECTOR = new Long(12);
    public static final Long QUARTERLYBYPROJECT = new Long(11);
    public static final Long QUARTERLYMULTIDONOR = new Long(10);
    public static final Long TREND = new Long(8);
    public static final Long PIPELINE = new Long(7);
    public static final Long DATERANGE = new Long(6);
    public static final Long SECTORBYPROJECT = new Long(5);
    public static final Long MULTILATERALDONOR = new Long(3);
    public static final Long PROJECTBYDONOR = new Long(4);
    public static final Long PHYSICALCOMPONENT = new Long(13);
//  public static final Long YEAR_RANGE = new Long(1) ;
    public static final String DIRECT_BUDGET_SUPPORT = "1" ;
    public static final String PROGRAM_SUPPORT = "6" ;
    public static final String PROJECT_SUPPORT = "3" ;
    public static final String OTHER_AID = "4" ;
    public static final Long STATUS_PLANNED = new Long(5) ;

    public static final Integer ORIGINAL = new Integer(0);
    public static final Integer REVISED = new Integer(1);
    public static final Integer CURRENT = new Integer(2);
    
    public static final String USER_WORKSPACES="USER_WORKSPACES";
    //Removed by Govind
    //public static  String COUNTRY_ISO = "";
    //public static  String COUNTRY = "";

    public static final String GLOBALSETTINGS_DATEFORMAT="Default Date Format";
    public static final String GLOBALSETTINGS_COMPUTE_TOTALS="Compute Totals for Activities";
    public static final String GLOBALSETTINGS_MULTISECTORSELECT="Multi-Sector Selecting";
    public static final String GLOBALSETTINGS_INCLUDE_PLANNED="Totals include planned";
    
    //added in burkina
    //moved to GlobalSettingsCOnstants
    //public static final String GLOBALSETTINGS_DEFAULT_EX_RATE_SEPARATOR="Default Exchange Rate Separator";
    
    public static final String GLOBALSETTINGS_ECS="ECS Enabled";
    

    public static final String CURRENCY_RATE_DEAFULT_START_DATE = "25/04/2005";
    public static final String CURRENCY_RATE_DEAFULT_END_DATE = "01/05/2005";

    //public static final String CALENDAR_DATE_FORMAT = "dd/mm/yyyy";
    public static final String CALENDAR_DATE_FORMAT = "dd/MM/yyyy"; //error at finding month amp-1754
    //"yyyy-MM-dd"
    public static final String CALENDAR_DATE_PICKER = "yyyy-MM-dd"; //error at finding month amp-1754
    public static final String SIMPLE_DATE_TIME_FORMAT = "dd MMMMM, yyyy hh:mm aaa";

    public static final int FROM_YEAR_RANGE = 40;//22;
    public static final int TO_YEAR_RANGE = 5;

    public static final Long STATUS_NAME = new Long(1) ;
    public static final Long DONOR_NAME = new Long(2) ;
    public static final Long ACTUAL_START_DATE = new Long(3) ;
    public static final Long ACTIVITY_NAME = new Long(4) ;
    public static final Long TERM_ASSIST_NAME = new Long(5) ;
    public static final Long LEVEL_NAME = new Long(6) ;
    public static final Long ACTUAL_COMPLETION_DATE = new Long(7) ;
    public static final Long SECTOR_NAME = new Long(8) ;
    public static final Long REGION_NAME = new Long(9) ;
    public static final Long FUNDING_INSTRUMENT = new Long(10) ;
    public static final Long OBJECTIVE = new Long(11) ;
    public static final Long AMP_ID = new Long(12) ;
    public static final Long CONTACT_NAME = new Long(13) ;
    public static final Long DESCRIPTION = new Long(14) ;
    public static final Long TOTAL_COMMITMENT = new Long(15) ;
    public static final Long TOTAL_DISBURSEMENT = new Long(16) ;
    //humbly added by Mihai
    public static final Long COMPONENT_NAME = new Long(17);


    public static final String REGIONAL_FUNDING_PAGE_CODE = "RFS";
        public static final String COSTING_PAGE_CODE = "Cost";
    public static final String CALENDAR_FILTER = "Calendar";
    public static final String CURRENCY_FILTER = "Currency";
    public static final String DONOR_FILTER = "Donor";
    public static final String SECTOR_FILTER = "Sector";
    public static final String STATUS_FILTER = "Status";
    public static final String PERSPECTIVE_FILTER = "Perspective";
    public static final String ACTIVITY_RISK_FILTER = "Activity Risk";
        public static final String YEAR_RANGE_FILTER = "Year Range";


    public static final String ANNUAL = "A" ;
    public static final String QUARTERLY = "Q" ;

    //proudly added by Alex Gartner
    public static final String ACTIVITY_STATUS_PLANNED          = "Planned";
    public static final String ACTIVITY_STATUS_ONGOING          = "Ongoing";
    public static final String ACTIVITY_STATUS_COMPLETED        = "Completed";
    public static final String ACTIVITY_STATUS_CANCELLED        = "Cancelled / Suspended";
    @Deprecated
    public static final String ACTIVITY_STATUS_PROPOSED         = "Proposed";
    public static final String ACTIVITY_STATUS_CONSIDERED       = "Considered";
    
    public static final Set<ApprovalStatus> ACTIVITY_NEEDS_APPROVAL_STATUS_SET = ImmutableSet.of(
            ApprovalStatus.CREATED,
            ApprovalStatus.STARTED,
            ApprovalStatus.EDITED,
            ApprovalStatus.REJECTED);

    public static final String ACTIVITY_NEEDS_APPROVAL_STATUS = ACTIVITY_NEEDS_APPROVAL_STATUS_SET.stream()
            .map(z -> StringUtils.wrap(z.getDbName(), "'"))
            .collect(Collectors.joining(", "));



    //humbly added by Mihai
    public static final Long DONOR_FUNDING = new Long(1);
    public static final Long COMPONENT_FUNDING = new Long(2);
    public static final Long REGION_FUNDING = new Long(3);

    public static final String TEAM_ID = "TID";
    
    public static final String TEAM_Lead = "teamLead";
    public static final String TEAM_Head = "teamHead";

    public static final String SESSION_LIST = "sessionList";
    public static final String EDIT_ACT_LIST = "editActivityList";
    public static final String USER_ACT_LIST = "userActivityList";
    public static final String TS_ACT_LIST = "timestampActivityList";

    public static final long MAX_TIME_LIMIT = 10*60*1000;

    public static final String ME_IND_VAL_BASE_ID = "base";
    public static final String ME_IND_VAL_ACTUAL_ID = "actual";
    public static final String ME_IND_VAL_TARGET_ID = "target";

    public static final String ACTIVITY_PERFORMANCE_CHART_TITLE = "Activity - Performance";
    public static final String ACTIVITY_RISK_CHART_TITLE = "Activity - Risk";
    public static final String PORTFOLIO_PERFORMANCE_CHART_TITLE = "Portfolio - Performance";
    public static final String PORTFOLIO_RISK_CHART_TITLE = "Portfolio - Risk";

    public static final String BUDGET_STRUCTURE_NAME_OPERATIONS = "Operations";
    public static final String BUDGET_STRUCTURE_NAME_CAPITAL = "Capital";
    public static final String BUDGET_STRUCTURE_NAME_SALARIES = "Salaries";

    public static final int CHART_HEIGHT = 350;
    public static final int CHART_WIDTH = 400;

    public static final int PORTFOLIO_CHART_HEIGHT = 400;
    public static final int PORFOLIO_CHART_WIDTH = 730;

    public static final String ACCESS_TYPE_TEAM = "Team";
    public static final String ACCESS_TYPE_MNGMT = "Management";

    // M&E Indicator Risk Ratings
    public static final byte HIGHLY_SATISFACTORY = 3;
    public static final byte VERY_SATISFACTORY = 2;
    public static final byte SATISFACTORY = 1;
    public static final byte UNSATISFACTORY = -1;
    public static final byte VERY_UNSATISFACTORY = -2;
    public static final byte HIGHLY_UNSATISFACTORY = -3;

    // Risk chart colors for risk ratings
    public static final Color HIGHLY_SATISFACTORY_CLR = Color.GREEN;
    public static final Color VERY_SATISFACTORY_CLR = Color.BLUE;
    public static final Color SATISFACTORY_CLR = Color.CYAN;
    public static final Color UNSATISFACTORY_CLR = new Color(0,0,83); // dark blue
    public static final Color VERY_UNSATISFACTORY_CLR = Color.ORANGE;
    public static final Color HIGHLY_UNSATISFACTORY_CLR = Color.RED;

    // Performance chart colors
    public static final Color BASE_VAL_CLR = Color.BLUE;
    public static final Color ACTUAL_VAL_CLR = Color.GREEN;
    public static final Color TARGET_VAL_CLR = Color.RED;

    // For AMP feature turn ON/OFF, following are the feature codes.
    public static final String ME_FEATURE = "ME"; //* M & E Feature code*
    public static final String PI_FEATURE = "PI"; //* Paris Indicators feature code
    public static final String AA_FEATURE = "AA"; //* Activity Approval feature code
    public static final String CL_FEATURE = "CL"; //* Calendar feature code
    public static final String DC_FEATURE = "DC"; // Documents feature code
    public static final String SC_FEATURE = "SC"; // Scenarios feature code
    public static final String MS_FEATURE = "MS"; //* Multi Sector Manager code
    public static final String LB_FEATURE = "LB"; //* Logframe Builder code
    public static final String SA_FEATURE = "SA"; //* Standard AMP Form Fields code


    public static final String DEF_FLAG_EXIST = "defFlagExist";
    public static final String DSKTP_FLTR_CHANGED = "dsktpFltrChanged";


    //   Application attributes names
    public static final String GLOBAL_SETTINGS = "globalSettings";

    // Session attributes names

    public static final String CURRENT_USER = "currentUser";
    public static final String CURRENT_MEMBER = "currentMember";
    public static final String FILTER_PARAMS = "filterParams";

    public static final String NUM_OF_LANGUAGES = "numOfLanguages";

    public static final String AMP_PROJECTS = "ampProjects";
    public static final String DIRTY_ACTIVITY_LIST = "dirtyActivityList";
    public static final String TYPE_MNGMT = "Management";
    public static final String TYPE_TEAM = "Team";
    public static final String SSC_WORKSPACE_PREFIX = "SSC_";

    public static final String DEFAULT_TEAM_REPORT      = "defaultTeamReport";
    
    /**
     * the AmpReport instance linked to the currently-opened tab
     */
    public static final String CURRENT_TAB_REPORT   = "current_tab_report";
    
    public static final String MY_REPORTS       = "myReports";
    public static final String LAST_VIEWED_REPORTS          = "lastViewedReports"; // BoundedList
    public static final String MY_ACTIVE_TABS   = "myActiveTabs";
    public static final String MY_TABS          = "myTabs";
    public static final String MY_TASKS = "myTasks";
    public static final String MY_MESSAGES = "myMessages";
    public static final String MY_LINKS = "myLinks";
    //public static final String MOST_RECENT_RESOURCES_UUIDS = "recentResources_uuid"; // BoundedList of most-recently-visited-resources
    public static final String MOST_RECENT_RESOURCES = "recentResources"; // BoundedList of most-recently-visited-resources
    public static final Integer MAX_MOST_RECENT_RESOURCES = 5;
    public static final String MY_TEAM_MEMBERS = "myTeamMembers";
    public static final String MY_DOCUMENTS = "myDocuments";
    public static final String MY_LAST_VERSIONS = "lastVersions";
    
    
    public static final String INDICATOR_NAMES = "indicatorNames";

    public static final byte SORT_FIELD_PROJECT = 1;
    public static final byte SORT_FIELD_AMPID = 2;
    public static final byte SORT_FIELD_DONOR = 3;
    public static final byte SORT_FIELD_AMOUNT = 4;

    public static final boolean SORT_ORDER_ASC = true;

    public static final String DESKTOP_SETTINGS_CHANGED = "desktopSettingsChanged";

    public static final String AIM_MODULE_KEY = "aim";

    public static final String EVENT_EDIT = "edit";
    public static final String EVENT_ADD = "add";
    public static final String EVENT_SHOW_ADD = "showAdd";
    public static final String EVENT_SHOW_EDIT = "showEdit";

    public static final String DESKTOP_PG_CODE = "DTP";
    public static final String FINANCIAL_PG_CODE = "FP";
        public static final String FINANCING_INSTRUMENT_FILTER = "Financing Instrument";
        public static final String TYPE_ASSISTANCE_FILTER = "Type of Assistance";
        public static final String ORGANIZATION_FILTER = "Organization";


    public static final String ROLE_CODE_DONOR                      = "DN";
    public static final String ROLE_CODE_IMPLEMENTING_AGENCY        = "IA";
    public static final String ROLE_CODE_REPORTING_AGENCY           = "RA";
    public static final String ROLE_CODE_BENEFICIARY_AGENCY         = "BA";
    public static final String ROLE_CODE_EXECUTING_AGENCY           = "EA";
    public static final String ROLE_CODE_RESPONSIBLE_ORG            = "RO";
    public static final String ROLE_CODE_CONTRACTING_AGENCY         = "CA";
    public static final String ROLE_CODE_REGIONAL_GROUP             = "RG";
    public static final String ROLE_CODE_SECTOR_GROUP               = "SG";
    public static final String ROLE_CODE_COMPONENT_FUNDING_ORGANIZATION = "CF";
    public static final String ROLE_CODE_COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION = "CS";

    //global settings
    public static final String GLOBAL_BUDGET_FILTER = "Public View Budget Filter";
    public static final String GLOBAL_PUBLIC_VIEW = "Public View";
    public static final String GLOBAL_DEFAULT_COUNTRY = "Default Country";
    public static final String GLOBAL_SHOW_COMPONENT_FUNDING_BY_YEAR = "Show Component Funding by Year";
    public static final String STRONG_PASSWORD = "Strong password";


    public static final String GLOBAL_DEFAULT_SECTOR_SCHEME="Default Sector Scheme";

    public static final String ONLY_PREVIEW="onlyPreview";
    public static final String MY_REPORTS_PER_PAGE = "myReportsPerPage";

    public static final String AMP_SERVLET_CONTEXT="ampServletContext";
       
    public static final String   FEATURE_MANAGER_VISIBILITY_TREE_UPDATED="Your changes have been saved." ;
    
    //Lucene Index
    public static final String LUCENE_INDEX = "luceneIndex";
    public static final String  TEAM_LEAD_ALREADY_EXISTS = "The already has a team leader. You should remove the actual team leader first";
    //Reports Export to Excel indent
    public static final String excelIndexString = "  "; //TABS do not show in Windows

    //Error Priorities
    public static final int AMP_ERROR_LEVEL_UNSTABLE = -10; //highest priority
    public static final int AMP_ERROR_LEVEL_CRITICAL = -5;
        //errors bellow 0 are severe will get sent right away to the server
        //and require a new user session or server restart
    public static final int AMP_ERROR_LEVEL_ERROR = 0;
    public static final int AMP_ERROR_LEVEL_WARNING = 5;    //lowest priority
    //END Error Priorities
    
    //   NEVER CHANGE THIS!! THE UNIFIED JNDI DATASOURCE ALIAS, VALID ONLY INSIDE THIS WAR 
    //   USE THIS NAME FOR LOOKUPS: java:comp/env/ampDS 
    //   SEE jboss-web.xml
    public static final String UNIFIED_JNDI_ALIAS="java:comp/env/ampDS";

    public static final String JNP_URL="jnp://localhost:1099";
    
    public static final int NUM_OF_CHARS_IMPUTATION = 22;
    
    public static final int NUM_OF_CHARS_CODE_CHAPITRE = 11;
    
    //org type values
    public static final String ORG_TYPE_GOVERNMENTAL = "GOVERNMENTAL";
    public static final String ORG_TYPE_NGO = "NGO";
    public static final String ORG_TYPE_REGIONAL = "REGIONAL";
    public static final String ORG_TYPE_FUND = "FUND";
    
    //organization information
    public static final int ORG_INFO_TYPE_ANNUAL_BUDGET_ADMIN= 1;
    public static final int ORG_INFO_TYPE_ANNUAL_BUDGET_PROGRAM = 2;

    // org profile
    public static final int EXPORT_OPTION_CHART_ONLY = 0 ;
    public static final int EXPORT_OPTION_DATA_SOURCE_ONLY = 1 ;
    public static final int EXPORT_OPTION_CHART_DATA_SOURCE= 2 ;
    public static final int EXPORT_OPTION_NONE=3;
    
    public static final int EXPORT_TO_WORD = 0 ;
    public static final int EXPORT_TO_PDF= 1 ;

    //contact information 
    public static final String DONOR_CONTACT = "DONOR_CONT";
    public static final String MOFED_CONTACT = "MOFED_CONT";
    public static final String SECTOR_MINISTRY_CONTACT = "SECTOR_MINISTRY_CONT";
    public static final String PROJECT_COORDINATOR_CONTACT = "PROJ_COORDINATOR_CONT";
    public static final String IMPLEMENTING_EXECUTING_AGENCY_CONTACT= "IMPL_EXEC_AGENCY_CONT";
    //contact parameter names
    public static final String CONTACT_PROPERTY_NAME_EMAIL="contact email";
    public static final String CONTACT_PROPERTY_NAME_PHONE="contact phone";
    public static final String CONTACT_PROPERTY_NAME_FAX="contact fax";
    //contact information pagination elements
    public static final int CONTACTS_PER_PAGE=15;
    public static final int PAGES_TO_SHOW=5;
    //Pagination for Reports and Tabs
     public static final int DRAFRT_GO_TO_DESKTOP=1;
     public static final int DRAFRT_STAY_TO_ACTIVITY_PAGE=2;
     public static final int MAX_REPORTS_IN_SESSION=5;     
     //Action type loggers
     public static final String LOGIN_ACTION="login";
     public static final String SENT_REMINDER="sentReminder";
     public static final String UNASSIGNED_ACTIVITY_LIST="unassignedActivityList";


    public static final String COMPARATOR_TRANSACTION_DATE_DESC = "1";
    public static final String COMPARATOR_TRANSACTION_DATE_ASC = "2";
    public static final String COMPARATOR_REPORTING_DATE_DESC = "3";
    public static final String COMPARATOR_REPORTING_DATE_ASC = "4";


    public static final String AUTOMATIC_VALIDATION_JOB_CLASS_NAME =
            "org.digijava.module.message.jobs.ActivityAutomaticValidationJob";

    public static final String PROJECT_VALIDATION_ON = "On";
    public static final String PROJECT_VALIDATION_OFF = "validationOff";
    public static final String PROJECT_VALIDATION_FOR_ALL_EDITS = "allEdits";
    public static final String PROJECT_VALIDATION_FOR_NEW_ONLY = "newOnly";

}
