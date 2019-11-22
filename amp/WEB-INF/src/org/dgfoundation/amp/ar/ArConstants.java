/**
 * ArConstants.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.digijava.module.aim.helper.Constants;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 15, 2006
 *
 */
public final class ArConstants {
    public final static MetaInfo []prefixes=new MetaInfo[] {
        //PLEASE KEEP THE SAME ORDER IN prefixes AND suffixes !!
        
        new MetaInfo(GenericViews.HTML2,"/repository/aim/view/ar/html2/"),
        new MetaInfo(GenericViews.HTML,"/repository/aim/view/ar/html/"),
        new MetaInfo(GenericViews.XLS,"org.dgfoundation.amp.ar.view.xls."),
        new MetaInfo(GenericViews.PDF,"org.dgfoundation.amp.ar.view.pdf."),
        new MetaInfo(GenericViews.PRINT,"/repository/aim/view/ar/print/"),
        new MetaInfo(GenericViews.TREE,"/repository/aim/view/ar/tree/"),
        new MetaInfo(GenericViews.FOLDABLE,"/repository/aim/view/ar/foldable/"),        
        };

    public final static MetaInfo []suffixes=new MetaInfo[] {
        new MetaInfo(GenericViews.HTML2,".jsp"),
        new MetaInfo(GenericViews.HTML,".jsp"),
        new MetaInfo(GenericViews.XLS,"XLS"),
        new MetaInfo(GenericViews.PDF,"PDF"),
        new MetaInfo(GenericViews.PRINT,".jsp"),
        new MetaInfo(GenericViews.TREE,".jsp"),
        new MetaInfo(GenericViews.FOLDABLE,".jsp"),     
        };

    //metainfo categs:
    public final static String ADJUSTMENT_TYPE="Adjustment Type";
    public final static String TRANSACTION_TYPE="Transaction Type";
    public final static String TRANSACTION_DATE="Transaction Date";
    
    public final static String FUNDING_TYPE="Funding Type";
    public final static String TERMS_OF_ASSISTANCE="Type Of Assistance";
    public final static String TERMS_OF_ASSISTANCE_TOTAL="Total";
    public final static String FINANCING_INSTRUMENT="Financing Instrument"; 
    public final static String YEAR="Year";
    public final static String FISCAL_Y="FISCAL_Y";
    public final static String FISCAL_M="FISCAL_M";
    public final static String QUARTER="Quarter";
    public final static String QUARTERS_TOTAL="Total";
    public final static String MONTH="Month";
    public final static String PROPOSED_COST="Proposed Cost";
    public final static String SOURCE_FUNDING="Source Funding";
    
    public final static String PERSPECTIVE="Perspective";

    public final static String RELATED_PROJECTS="Related Projects";
    public final static String DONOR="Donor Agency";
    public final static String MODE_OF_PAYMENT="Mode of Payment";
    public final static String MODE_OF_PAYMENT_TOTAL="Total";
    public final static String FUNDING_STATUS="Funding Status";
    
    public final static String MODE_OF_PAYMENT_UNALLOCATED="Mode of Payment Unallocated";
    
    public final static String DONOR_GROUP="Donor Group";
    public final static String DONOR_TYPE_COL="Donor Type";
    public final static String CAPITAL_PERCENT="Capital Percent";
    
    public final static String AGREEMENT_CODE = "Agreement Code";
    public final static String AGREEMENT_TITLE_CODE = "Agreement Title + Code";
    public final static String DISASTER_RESPONSE_MARKER = "Disaster Response Marker";
    
    public final static String DISABLE_PERCENT = "Disable_Percent";
    //public final static String IS_REAL_DISBURSEMENTS_COLUMN = "Is Real Disbursements";
    
    public final static String COMPONENT_NAME = "Component Name";
    public final static String COMPONENT_TYPE_S = "Component Type";
    public final static String RECIPIENT_NAME = "Recipient Name";
    public final static String RECIPIENT_ROLE_NAME = "Recipient Role Name";
    public final static String RECIPIENT_ROLE_CODE = "Recipient Role Code";
    public final static String SOURCE_ROLE_CODE = "Source Role Code";
    public final static String SOURCE_ROLE_NAME = "Source Role Name";

    public final static String UNALLOCATED="Unallocated";
    
    //report type
    public final static int DONOR_TYPE = 1;
    public final static int COMPONENT_TYPE = 2;
    public final static int REGIONAL_TYPE = 3;
    public final static int CONTRIBUTION_TYPE = 4;
    public final static int PLEDGES_TYPE = 5;
    public final static int GPI_TYPE = 6;
    public final static Set<Integer> LEGAL_REPORT_TYPES = Collections.unmodifiableSet(new HashSet<Integer>(Arrays.asList(
            DONOR_TYPE, COMPONENT_TYPE, REGIONAL_TYPE, CONTRIBUTION_TYPE, PLEDGES_TYPE, GPI_TYPE
            )));
    
    //metainfo values:
    public final static String COMMITMENT = "Commitments";
    public final static String DISBURSEMENT = "Disbursements";
    public final static String EXPENDITURE = "Expenditures";
    public final static String RELEASE_OF_FUNDS = "Release of Funds";
    public final static String ESTIMATED_DISBURSEMENTS = "Estimated Disbursements";
    public final static String ANNUAL_PROPOSED_PROJECT_COST = "Annual Proposed Project Cost";
    public final static String PLEDGES_COMMITMENT = "Pledges Commitments";
    public final static String PLEDGES_DISBURSEMENT = "Pledges Disbursements";
    public final static String ARREARS = "Arrears";
    public final static String PLEDGE = "Pledge";
    public final static String FUNDING_TYPE_COMMITMENT_GAP = "Commitment Gap";
    public final static String PLEDGES_METADATA_NAME = "Pledges ";

    public final static Map<String, String> USER_FRIENDLY_ROLE_CODES = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("IA", "IMPL");
        put("EA", "EXEC");
        put("BA", "BENF");
    }});

    public final static Map<String, Integer> TRANSACTION_TYPE_NAME_TO_ID = new LinkedHashMap<String, Integer>() {{
        put(COMMITMENT, Constants.COMMITMENT);
        put(DISBURSEMENT, Constants.DISBURSEMENT);
        put(EXPENDITURE, Constants.EXPENDITURE);
        put(RELEASE_OF_FUNDS, Constants.RELEASE_OF_FUNDS);
        put(ESTIMATED_DISBURSEMENTS, Constants.ESTIMATED_DONOR_DISBURSEMENT);
        put(PLEDGE, Constants.PLEDGE);
        put(DISBURSEMENT_ORDERS, Constants.DISBURSEMENT_ORDER);
        put(MTEF_PROJECTION, Constants.MTEFPROJECTION);
        put(PLEDGES_COMMITMENT, Constants.PLEDGES_COMMITMENT);
        put(ANNUAL_PROPOSED_PROJECT_COST, Constants.ANNUAL_PROPOSED_PROJECT_COST);
        put(PLEDGES_DISBURSEMENT, Constants.PLEDGES_DISBURSEMENT);
        put(ARREARS, Constants.ARREARS);
    }};
    
    public final static Map<Integer, String> TRANSACTION_ID_TO_TYPE_NAME = MapUtils.invertMap(TRANSACTION_TYPE_NAME_TO_ID);
    
    public final static Map<String, Integer> SSC_TRANSACTION_TYPE_NAME_TO_ID = new LinkedHashMap<String, Integer>() {{
        put(COMMITMENT, Constants.COMMITMENT);
        put(DISBURSEMENT, Constants.DISBURSEMENT);
    }};
    
    public final static int MIN_SUPPORTED_YEAR = 1970;
    public final static int MAX_SUPPORTED_YEAR = 2050;

    //public final static String PLEDGES_TOTAL_PLEDGED="Total Pledged";
    public final static java.sql.Date PLEDGE_FAKE_YEAR = new java.sql.Date(0);
    
    /**
     * fixed date format used for unformatted input/output
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    public static final Integer UNDEFINED_KEY = 999999999;
    public static final String BOOLEAN_TRUE_KEY = "2";
    public static final String BOOLEAN_FALSE_KEY = "1";
        
    //Computed Field Constants
    //
    public final static String MAX_ACTUAL_COMMITMENT="MAX_ACTUAL_COMMITMENT";
    public final static String MAX_ACTUAL_DISBURSEMENT="MAX_ACTUAL_DISBURSEMENT";
    
    public final static String MAX_PLANNED_COMMITMENT="MAX_PLANNED_COMMITMENT";
    public final static String MAX_PLANNED_DISBURSEMENT="MAX_PLANNED_DISBURSEMENT";
    
    public final static String MIN_ACTUAL_COMMITMENT="MIN_ACTUAL_COMMITMENT";
    public final static String MIN_ACTUAL_DISBURSEMENT="MIN_ACTUAL_DISBURSEMENT";
    
    public final static String MIN_PLANNED_COMMITMENT="MIN_PLANNED_COMMITMENT";
    public final static String MIN_PLANNED_DISBURSEMENT="MIN_PLANNED_DISBURSEMENT";
    public final static String COUNT_PROJECTS="COUNT_PROJECTS";
    
    //funding variable  names
    public final static String ACTUAL_COMMITMENT="ACTUAL_COMMITMENT";
    public final static String ACTUAL_DISBURSEMENT="ACTUAL_DISBURSEMENT";
    public final static String ACTUAL_EXPENDITURET="ACTUAL_EXPENDITURET";
    public final static String PLANNED_COMMITMENT="PLANNED_COMMITMENT";
    public final static String PLANNED_DISBURSEMENT="PLANNED_DISBURSEMENT";
    public final static String PLANNED_EXPENDITURE="PLANNED_EXPENDITURE";
    
    
    public final static String TOTAL_ACTUAL_COMMITMENT="TOTAL_ACTUAL_COMMITMENT";
    public final static String TOTAL_ACTUAL_DISBURSEMENT="TOTAL_ACTUAL_DISBURSEMENT";

    public final static String TOTAL_PLANNED_COMMITMENT="TOTAL_PLANNED_COMMITMENT";
    public final static String TOTAL_PLANNED_DISBURSEMENT="TOTAL_PLANNED_DISBURSEMENT";
    
    public final static String ACTUAL_COMMITMENT_FILTERED="ACTUAL_COMMITMENT_FILTERED";
    public final static String ACTUAL_DISBURSEMENT_FILTERED="ACTUAL_DISBURSEMENT_FILTERED";
    public final static String PLANNED_COMMITMENT_FILTERED="PLANNED_COMMITMENT_FILTERED";
    public final static String PLANNED_DISBURSEMENT_FILTERED="PLANNED_DISBURSEMENT_FILTERED";
  
    
    public final static String ACTUAL_COMMITMENT_COUNT="ACTUAL_COMMITMENT_COUNT";
    public final static String ACTUAL_DISBURSEMENT_COUNT="ACTUAL_DISBURSEMENT_COUNT";
    
    public final static String PLANNED_COMMITMENT_COUNT="PLANNED_COMMITMENT_COUNT";
    public final static String PLANNED_DISBURSEMENT_COUNT="PLANNED_DISBURSEMENT_COUNT";
    
    public final static String PLEDGED_TOTAL="PLEDGE_TOTAL";
    public final static String TOTAL_PLEDGE_ACTIVITY_ACTUAL_COMMITMENT = "TOTAL_PLEDGE_ACTIVITY_ACTUAL_COMMITMENT";
    
    //dates variable names
    public final static String ACTUAL_START_DATE_VALUE = "ACTUAL_START_DATE_VALUE";
    public final static String ACTUAL_APPROVAL_DATE_VALUE = "ACTUAL_APPROVAL_DATE_VALUE";
    //public final static String ACTIVITY_APPROVAL_DATE_VALUE = "ACTIVITY_APPROVAL_DATE_VALUE";
    public final static String PROPOSED_START_DATE_VALUE = "PROPOSED_START_DATE_VALUE";
    public final static String ACTUAL_COMPLETION_DATE_VALUE = "ACTUAL_COMPLETION_DATE_VALUE";
    public final static String PROPOSED_COMPLETION_DATE_VALUE = "PROPOSED_COMPLETION_DATE_VALUE";
    public final static String CURRENT_DATE_VALUE="CURRENT_DATE_VALUE";
    public final static String ORIGINAL_COMPLETION_DATE_VALUE = "ORIGINAL_COMPLETION_DATE_VALUE";
    //end computed filds 
    
    public final static String PLANNED="Planned";
    public final static String ACTUAL="Actual";
    public final static String PIPELINE="Pipeline";
    
    //created columns
    public final static String COLUMN_TOTAL="Total Costs";
    public final static String COLUMN_CONTRIBUTION_TOTAL="Total Contributions";
    
    public final static String COLUMN_RAW_DATA="RAW DATA";
    public final static String COLUMN_FUNDING="Funding";
    public final static String COLUMN_PROPOSED_COST="Proposed Project Cost";
    public final static String COSTING_GRAND_TOTAL="Grand Total";
    public final static String MTEF_COLUMN = "MTEF";
    
    public final static String COLUMN_ANY_SECTOR="Sector";
    public final static String COLUMN_SUB_SECTOR="Sub-Sector";
    
    public final static String ROLE_NAME_DONOR_AGENCY = "Donor Agency";
    public final static String ROLE_NAME_EXECUTING_AGENCY = "Executing Agency";
    public final static String ROLE_NAME_IMPLEMENTING_AGENCY = "Implementing Agency";
    public final static String ROLE_NAME_BENEFICIARY_AGENCY = "Beneficiary Agency";
    
    public final static String IS_AN_MTEF_FUNDING = "Is MTEF Funding";
    
    
    public final static String COLUMN_SECTOR_TAG="Sector Tag";
    
    public final static List<String> COLUMN_ANY_RELATED_ORGS=Arrays.asList("Beneficiary Agency","Contracting Agency",
                            "Executing Agency","Implementing Agency","Regional Group","Responsible Organization","Sector Group");

    public final static Map<String, String> COLUMN_ROLE_CODES = Collections.unmodifiableMap(new HashMap<String, String>()
            {{
                put(ROLE_NAME_DONOR_AGENCY, Constants.ROLE_CODE_DONOR);
                put(ROLE_NAME_EXECUTING_AGENCY, Constants.ROLE_CODE_EXECUTING_AGENCY);
                put(ROLE_NAME_IMPLEMENTING_AGENCY, Constants.ROLE_CODE_IMPLEMENTING_AGENCY);
                put(ROLE_NAME_BENEFICIARY_AGENCY, Constants.ROLE_CODE_BENEFICIARY_AGENCY);
            }});

    public final static Set<String> COLUMNS_LINKED_WITH_FLOW_ROLES = Collections.unmodifiableSet(new HashSet<String>()
            {{
                addAll(COLUMN_ROLE_CODES.keySet());
                add(ColumnConstants.DONOR_GROUP); add(ColumnConstants.DONOR_TYPE);
                add(ColumnConstants.EXECUTING_AGENCY_GROUPS); add(ColumnConstants.EXECUTING_AGENCY_TYPE);
                add(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS); add(ColumnConstants.IMPLEMENTING_AGENCY_TYPE);
                add(ColumnConstants.BENEFICIARY_AGENCY_GROUPS); // no BENF type column, looks like
                add(ColumnConstants.CONTRACTING_AGENCY); add(ColumnConstants.CONTRACTING_AGENCY_GROUPS);
                add(ColumnConstants.RESPONSIBLE_ORGANIZATION); add(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS);
                add(ColumnConstants.REGIONAL_GROUP); add(ColumnConstants.REGIONAL_GROUP_GROUP);
                add(ColumnConstants.SECTOR_GROUP); add(ColumnConstants.SECTOR_GROUP_GROUP);
            }});
    public final static Map<String, String> TRANSACTION_TYPE_TO_DIRECTED_TRANSACTION_VALUE = new HashMap<String, String>() {{
        put(ArConstants.COMMITMENT, ArConstants.TRANSACTION_REAL_COMMITMENT_TYPE);
        put(ArConstants.DISBURSEMENT, ArConstants.TRANSACTION_REAL_DISBURSEMENT_TYPE);
        put(ArConstants.MTEF_PROJECTION, ArConstants.TRANSACTION_REAL_MTEF_TYPE);
    }};
    
    public final static Map<String, String> DIRECTED_MEASURE_TO_DIRECTED_TRANSACTION_VALUE = new HashMap<String, String>() {{
        put(ArConstants.REAL_DISBURSEMENTS, ArConstants.TRANSACTION_REAL_DISBURSEMENT_TYPE);
        put(ArConstants.REAL_COMMITMENTS, ArConstants.TRANSACTION_REAL_COMMITMENT_TYPE);
        put(ArConstants.REAL_MTEFS, ArConstants.TRANSACTION_REAL_MTEF_TYPE);
    }};
    
    public final static Map<String, String> NONDIRECTED_MEASURE_TO_DIRECTED_MEASURE = new HashMap<String, String>() {{
        put(ArConstants.ACTUAL_COMMITMENTS, ArConstants.REAL_COMMITMENTS);
        put(ArConstants.ACTUAL_DISBURSEMENTS, ArConstants.REAL_DISBURSEMENTS);
        put(ArConstants.ACTUAL_MTEF_PROJECTION, ArConstants.REAL_MTEFS);
    }};
    
    public final static Map<String, String> DIRECTED_MEASURE_TO_NONDIRECTED_MEASURE = new HashMap<String, String>() {{
        put(ArConstants.REAL_COMMITMENTS, ArConstants.ACTUAL_COMMITMENTS);
        put(ArConstants.REAL_DISBURSEMENTS, ArConstants.ACTUAL_DISBURSEMENTS);
        put(ArConstants.REAL_MTEFS, ArConstants.ACTUAL_MTEF_PROJECTION);
    }};

    
    public final static Map<String, String> NONDIRECTED_MEASURE_TO_TRANSACTION = new HashMap<String, String>() {{
        put(ArConstants.ACTUAL_COMMITMENTS, ArConstants.COMMITMENT);
        put(ArConstants.ACTUAL_DISBURSEMENTS, ArConstants.DISBURSEMENT);
        put(ArConstants.MTEF_PROJECTION, ArConstants.MTEF_PROJECTION);
    }};
    
    public static final String COLUMN_LOC_ADM_LEVEL_0 = "Administrative Level 0";
    public static final String COLUMN_LOC_ADM_LEVEL_1 = "Administrative Level 1";
    public static final String COLUMN_LOC_ADM_LEVEL_2 = "Administrative Level 2";
    public static final String COLUMN_LOC_ADM_LEVEL_3 = "Administrative Level 3";

    public static final String COLUMN_PLEDGE_LOC_ADM_LEVEL_1 = "Pledges Administrative Level 1";
    public static final String COLUMN_PLEDGE_LOC_ADM_LEVEL_2 = "Pledges Administrative Level 2";
    public static final String COLUMN_PLEDGE_LOC_ADM_LEVEL_3 = "Pledges Administrative Level 3";
    
    public final static List<String> LOCATION_COLUMNS_LIST = Arrays.asList(new String[]{
            ArConstants.COLUMN_LOC_ADM_LEVEL_1, ArConstants.COLUMN_PLEDGE_LOC_ADM_LEVEL_1,
            ArConstants.COLUMN_LOC_ADM_LEVEL_2, ArConstants.COLUMN_PLEDGE_LOC_ADM_LEVEL_2,
            ArConstants.COLUMN_LOC_ADM_LEVEL_3, ArConstants.COLUMN_PLEDGE_LOC_ADM_LEVEL_3});
    
    public final static Set<String> LOCATION_COLUMNS = new HashSet<String>(LOCATION_COLUMNS_LIST);
    
    public final static Set<String> SECTOR_COLUMNS = new HashSet<String>(
            Arrays.asList(new String[]{
                    "Primary Sector", "Primary Sector Sub-Sector", "Primary Sector Sub-Sub-Sector",
                    "Secondary Sector Sub-Sector", "Secondary Sector", "Secondary Sector Sub-Sub-Sector",
                    "Tertiary Sector", "Tertiary Sector Sub-Sector", "Tertiary Sector Sub-Sub-Sector",
                    "Sector Tag", "Sector Tag Sub-Sector", "Sector Tag Sub-Sub-Sector",
                    "Pledges Secondary Sectors", "Pledges sectors", "Pledges Tertiary Sectors"
            } 
            ));
    
    public final static List<String> PROGRAMS_COLUMNS   = 
        Arrays.asList(new String[]{"National Planning Objectives Level 1",
                "National Planning Objectives Level 2",
                "National Planning Objectives Level 3",
                "National Planning Objectives Level 4",
                "National Planning Objectives Level 5",
                "National Planning Objectives Level 6",
                "National Planning Objectives Level 7",
                "National Planning Objectives Level 8"});
    
    
    public final static String COLUMN_SECTOR_GROUP="Sector Group";
    
    public final static String COLUMN_ANY_NATPROG                   = "National Planning Objectives";
    public final static String COLUMN_ANY_PRIMARYPROG           = "Primary Program";
    public final static String COLUMN_ANY_SECONDARYPROG = "Secondary Program";
    
    public static final String COLUMN_CAPITAL_EXPENDITRURE  =   "Capital Expenditure";
    public final static String COLUMN_ACTUAL_DISB_CAPITAL_RECURRENT="Payment Capital - Recurrent";
    
    //additional measures
    public static final String GRAND_TOTAL_ACTUAL_COMMITMENTS = "GRAND_TOTAL_ACTUAL_COMMITMENTS";
    public final static String UNDISBURSED_BALANCE="Undisbursed Balance";
    public final static String ACTUAL_COMMITMENTS="Actual Commitments";
    public final static String ACTUAL_DISBURSEMENTS = "Actual Disbursements";
    public final static String ACTUAL_MTEF_PROJECTION = "Actual MTEF Projections";
    public final static String MTEF_PROJECTION = "MTEF Projections";
    public final static String PLANNED_COMMITMENTS = "Planned Commitments";
    public final static String PLANNED_DISBURSEMENTS = "Planned Disbursements";
    
    public final static String REAL_DISBURSEMENTS = "Real Disbursements";
    public final static String REAL_COMMITMENTS = "Real Commitments";
    public final static String REAL_MTEFS = "Real MTEFs";
    
    public final static String UNCOMMITTED_BALANCE="Uncommitted Balance";
    public final static String TOTAL_COMMITMENTS="Total Commitments";
    //public final static String TOTAL_PERCENTAGE_OF_TOTAL_DISBURSEMENTS="Percentage Of Total Disbursements";
    public final static String EXECUTION_RATE = "Execution Rate";
    
//maldives only:
//  public final static String SECTOR_PERCENTAGE="Sector Percentage";
    
    public final static String PERCENTAGE="Percentage";
    
    public final static String TRANSACTION_REAL_COMMITMENT_TYPE = "Real Commitment Type";
    public final static String TRANSACTION_REAL_DISBURSEMENT_TYPE = "Real Disbursement Type";
    public final static String TRANSACTION_REAL_MTEF_TYPE = "Real MTEF Type";
    
    public final static String TRANSACTION_DN_EXEC = userFriendlyNameOfRole(Constants.FUNDING_AGENCY) + "-" + userFriendlyNameOfRole(Constants.EXECUTING_AGENCY);
    public final static String TRANSACTION_EXEC_IMPL = userFriendlyNameOfRole(Constants.EXECUTING_AGENCY) + "-" + userFriendlyNameOfRole(Constants.IMPLEMENTING_AGENCY);
    public final static String TRANSACTION_IMPL_BENF = userFriendlyNameOfRole(Constants.IMPLEMENTING_AGENCY) + "-" + userFriendlyNameOfRole(Constants.BENEFICIARY_AGENCY);
    
    //draft in title
    public final static String DRAFT="DRAFT";
    public final static String STATUS="STATUS";
    //bolivia:
//  public final static String LOCATION_PERCENTAGE="Location Percentage";
//  public final static String COMPONENTE_PERCENTAGE="Componente Percentage";
    
//  public final static String EXECUTING_AGENCY_PERCENTAGE="Eexecuting Agency Percentage";
    
    
    //burkina
//  public final static String PROGRAM_PERCENTAGE="Program Percentage";
    
    
    
//  public final static String NPO_PERCENTAGE="National Planning Objectives Percentage";
    //hierarchysorter
    public final static String HIERARCHY_SORTER_TITLE="Title";
    //public final static String SORT_ASCENDING="sortAscending";
    
    public static final String VIEW_PROPOSED_COST="v_proposed_cost";
    public static final String VIEW_REVISED_COST="v_revised_project_cost";
    public static final String VIEW_COST="v_costs"; 
    public static final String VIEW_DONOR_FUNDING="v_donor_funding";
    public static final String VIEW_COMPONENT_FUNDING="v_component_funding";
    public static final String VIEW_REGIONAL_FUNDING="v_regional_funding";
    public static final String VIEW_CONTRIBUTION_FUNDING="v_contribution_funding";
    public static final String VIEW_PLEDGES_FUNDING="v_pledges_funding_st";
    
    
    //public static final String PLEDGES_REPORT="pledgereport";
    
    //Columns order names
    public static final String PLEDGES_COLUMNS="Pledges Columns";
    public static final String PLEDGES_CONTACTS_1 ="Pledge Contact 1";
    public static final String PLEDGES_CONTACTS_2 ="Pledge Contact 2";
    
    //reportsFilter
    
    //public final static String REPORTS_Z_FILTER="ReportsFilter";
    public final static String TEAM_FILTER="TeamFilter";

    public final static String COMPUTE_ON_YEAR="ComputeOnYear";

    //the currency in use
    // public final static String SELECTED_CURRENCY="SelectedCurrency";

    public static final String DISBURSEMENT_ORDERS = "Disbursement Orders";
    
    public static final String INITIALIZE_FILTER_FROM_DB    = "Initialize filter from db";

    public static final String COLUMN_PROJECT_TITLE                 = "Project Title";
    public static final String COLUMN_AMP_ID                        = "AMP ID";
//  public static final String COLUMN_CUMULATIVE_COMMITMENT         = "Cumulative Commitment";
//  public static final String COLUMN_CUMULATIVE_DISBURSEMENT       = "Cumulative Disbursement";
//  public static final String COLUMN_UNDISB_CUMULATIVE_BALANCE     = "Undisbursed Cumulative Balance";
//  public static final String COLUMN_UNCOMM_CUMULATIVE_BALANCE = "Uncommitted Cumulative Balance";

    public static final String SUM_OFF_RESULTS = "SUM_OFF_RESULTS";

    public static final String TOTAL_PRIOR_ACTUAL_DISBURSEMENT = "TOTAL_PRIOR_ACTUAL_DISBURSEMENT";
    public static final String TOTAL_ACTUAL_DISBURSEMENT_LAST_CLOSED_MONTH = "TOTAL_ACTUAL_DISBURSEMENT_LAST_CLOSED_MONTH";
    
    public final static String CUMULATED_DISBURSEMENT_SELECTED_YEAR= "CUMULATED_DISBURSEMENT_SELECTED_YEAR";
    public final static String TOTAL_PLANNED_DISBURSEMENT_SELECTED_YEAR= "TOTAL_PLANNED_DISBURSEMENT_SELECTED_YEAR";
    
    public final static String EXCHANGE_RATES_CACHE="EXCHANGE_RATES_CACHE";
    public static final String VIEW_PUBLIC_PREFIX = "cached_";
    
    public final static List<SyntheticColumnsMeta> syntheticColumns = Arrays.asList(
            new SyntheticColumnsMeta("Planned Disbursements - Capital", new CapitalCellGenerator(ArConstants.CAPITAL_PERCENT, "Planned Disbursements - Capital","Planned Disbursements")),
            new SyntheticColumnsMeta("Planned Disbursements - Expenditure", new CapitalExpenditureCellGenerator(ArConstants.CAPITAL_PERCENT, "Planned Disbursements - Expenditure","Planned Disbursements")),
            new SyntheticColumnsMeta("Planned Disbursements", new CapitalSplitTotalsCellGenerator(ArConstants.CAPITAL_PERCENT, "Planned Disbursements","Planned Disbursements")),
            new SyntheticColumnsMeta("Actual Disbursements - Capital", new CapitalCellGenerator(ArConstants.CAPITAL_PERCENT, "Actual Disbursements - Capital","Actual Disbursements")),
            new SyntheticColumnsMeta("Actual Disbursements - Recurrent", new ActualDisbRecurrentCellGenerator(ArConstants.MODE_OF_PAYMENT, "Actual Disbursements - Recurrent","Actual Disbursements")),
            new SyntheticColumnsMeta("Actual Disbursements", new ActualDisbSplitCapRecTotalsCellGenerator(ArConstants.MODE_OF_PAYMENT, "Actual Disbursements","Actual Disbursements")),
            new SyntheticColumnsMeta("Commitment Gap", new CommitmentGapCellGenerator())
    ) ;

    public static final String ACTIVITY_ID = "ACTIVITY_ID";

    public static final String ACTIVITY_PLEDGES_TITLE_NAME = "Activity Pledges Title";

    public static final String COLUMN_ANY_PRIMARY_PROGRAM_LEVEL = "Primary Program Level";

    public final static String COLUMN_ANY_SECONDARY_PROGRAM_LEVEL="Secondary Program Level";
    
    public static class SyntheticColumnsMeta {
        String columnName;
        SyntheticCellGenerator generator;
        
        
        public SyntheticColumnsMeta(String columnName,
                SyntheticCellGenerator generator) {
            super();
            this.columnName = columnName;
            this.generator = generator;
        }
        /**
         * @return the columnName
         */
        public String getColumnName() {
            return columnName;
        }
        /**
         * @param columnName the columnName to set
         */
        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }
        /**
         * @return the generator
         */
        public SyntheticCellGenerator getGenerator() {
            return generator;
        }
        /**
         * @param generator the generator to set
         */
        public void setGenerator(SyntheticCellGenerator generator) {
            this.generator = generator;
        }
        
        @Override
        public String toString()
        {
            return String.format("%s -> %s", this.columnName, this.generator.toString());
        }
        
    }
    
    public final static String userFriendlyNameOfRole(String src) {
        String res = USER_FRIENDLY_ROLE_CODES.get(src);
        if (res != null) return res;
        return src;
    }
}
