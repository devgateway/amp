package org.digijava.module.categorymanager.util;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;


public class CategoryConstants {
    public static final String ACCHAPTER_NAME   = "A.C. Chapter";
    public static final String ACCHAPTER_KEY    = "acchapter";
    public static final String PROCUREMENT_SYSTEM_NAME      = "Procurement System";
    public static final String PROCUREMENT_SYSTEM_KEY       = "procurement_system";
    public static final String REPORTING_SYSTEM_NAME        = "Reporting System";
    public static final String REPORTING_SYSTEM_KEY         = "reporting_system";
    public static final String AUDIT_SYSTEM_NAME            = "Audit System";
    public static final String AUDIT_SYSTEM_KEY             = "audit_system";
    public static final String INSTITUTIONS_NAME            = "Institutions";
    public static final String INSTITUTIONS_KEY             = "institutions";
    public static final String ACCESSION_INSTRUMENT_NAME    = "Accession Instrument";
    public static final String ACCESSION_INSTRUMENT_KEY     = "accessioninstr";
    public static final String DOCUMENT_TYPE_NAME       = "Document type";
    public static final String DOCUMENT_TYPE_KEY        = "docType"; 
    public static final String LOGFRAME_NAME            = "Logframe";
    public static final String LOGFRAME_KEY         = "logframe";
    
    public static final String ACTIVITY_STATUS_NAME     = "Activity Status";
    public static final String ACTIVITY_STATUS_KEY          = "activity_status";
    
    public static final String SSC_ADJUSTMENT_TYPE_KEY = "SSC_adjustment_type";
    
    public static final String PROJECT_IMPLEMENTING_UNIT_NAME = "Project Implementing Unit";
    public static final String PROJECT_IMPLEMENTING_UNIT_KEY = "project_impl_unit";
    
    public static final String IMPLEMENTATION_LEVEL_NAME            = "Implementation Level";
    public static final String IMPLEMENTATION_LEVEL_KEY         = "implementation_level";
    
    public static final String IMPLEMENTATION_LOCATION_NAME         = "Implementation Location";
    public static final String IMPLEMENTATION_LOCATION_KEY              = "implementation_location";
    
    public static final String REFERENCE_DOCS_KEY = "reference_docs";
    
    public static final String TEAM_TYPE_NAME       = "Team Type";
    public static final String TEAM_TYPE_KEY        = "team_type";
    
    public static final String TYPE_OF_ASSISTENCE_NAME      = "Type of Assistence";
    public static final String TYPE_OF_ASSISTENCE_KEY       = "type_of_assistence";
    
    public static final String PLEDGES_TYPES_NAME       = "Pledges Types";
    public static final String PLEDGES_TYPES_KEY        = "pledges_types";
    
    public static final String PLEDGES_NAMES_NAME       = "Pledges Names";
    public static final String PLEDGES_NAMES_KEY        = "pledges_names";
    
    public static final String FINANCING_INSTRUMENT_NAME        = "Financing Instrument";
    public static final String FINANCING_INSTRUMENT_KEY     = "financing_instrument";
    
    public static final String PROGRAM_TYPE_NAME                = "Program Type";
    public static final String PROGRAM_TYPE_KEY             = "program_type";
    
    public static final String DOCUMENT_LANGUAGE_NAME               = "Document Languages";
    public static final String DOCUMENT_LANGUAGE_KEY                = "document_languages";
    
    public static final String FINANCIAL_INSTRUMENT_NAME            ="Financial Instrument";
    public static final String FINANCIAL_INSTRUMENT_KEY         ="financial_instrument";
    
    public static final String MTEF_PROJECTION_NAME             ="MTEF Projection";
    public static final String MTEF_PROJECTION_KEY                  ="mtef_projection";
    
    public static final String FUNDING_STATUS_NAME              ="Funding Status";
    public static final String FUNDING_STATUS_KEY                   ="funding_status";
    
    public static final String PEACE_MARKERS_NAME               ="Peace Markers";
    public static final String PEACE_MARKERS_KEY                    ="procurement_system";
    
    public static final String PEACEBUILDING_GOALS_NAME             ="Peacebuilding and Statebuilding Goals";
    public static final String PEACEBUILDING_GOALS_KEY                  ="reporting_system";
    
    public static final String MODE_OF_PAYMENT_NAME             ="Mode of Payment";
    public static final String MODE_OF_PAYMENT_KEY                  ="mode_of_payment";
    
    public static final String ACTIVITY_LEVEL_NAME                  ="Activity Level";
    public static final String ACTIVITY_LEVEL_KEY                   ="activity_level";

    public static final String PROJECT_CATEGORY_NAME        = "Project Category";
    public static final String PROJECT_CATEGORY_KEY     = "project_category";
    
    public static final String DATA_EXCHANGE_NAME       = "Data Echange Category";
    public static final String DATA_EXCHANGE_KEY        = "data_exchange";
    
    public static final String ACTIVITY_BUDGET_NAME         = "Activity Budget";
    public static final String ACTIVITY_BUDGET_KEY          = "activity_budget";
    
    public static final String PROJECT_AMOUNT_NAME          = "Amount";
    public static final String PROJECT_AMOUNT_KEY           = "funAmount";
    
    public static final String PROPOSE_PRJC_DATE_NAME       = "Date";
    public static final String PROPOSE_PRJC_DATE_KEY        = "funDate";

    public static final String ADJUSTMENT_TYPE_NAME         = "Adjustment Type";
    public static final String ADJUSTMENT_TYPE_KEY          = "adjustment_type";

    public static final String TRANSACTION_TYPE_NAME        = "Transaction Type";
    public static final String TRANSACTION_TYPE_KEY         = "transaction_type";

    public static final String WORKSPACE_PREFIX_NAME        = "Workspace Prefix";
    public static final String WORKSPACE_PREFIX_KEY         = "workspace_prefix";
    //categ for SSC
    public static final String FUNDING_SOURCES_NUMBER_NAME = "Total Number of Funding Sources";
    public static final String FUNDING_SOURCES_NUMBER_KEY = "total_number_of_funding_sources";
    //categ for SSC
    public static final String MODALITIES_NAME              = "Modalities";
    public static final String MODALITIES_KEY               = "modalities";
    //categ for SSC
    public static final String TYPE_OF_COOPERATION_NAME     = "Type of Cooperation";
    public static final String TYPE_OF_COOPERATION_KEY      = "type_of_cooperation";
    //categ for SSC
    public static final String TYPE_OF_IMPLEMENTATION_NAME  = "Type of Implementation";
    public static final String TYPE_OF_IMPLEMENTATION_KEY   = "type_of_implementation";

    public static final String REPORT_CATEGORY_NAME                 ="Report Category";
    public static final String REPORT_CATEGORY_KEY                  ="report_category";
    
    public static final String CONCESSIONALITY_LEVEL_NAME   = "Concessionality Level";
    public static final String CONCESSIONALITY_LEVEL_KEY    = "concessionality_level";
    
    
    //--- IPA Contracting Step 13
    
    //Activity Category
    public static final String IPA_ACTIVITY_CATEGORY_NAME="IPA Activity Category";
    public static final String IPA_ACTIVITY_CATEGORY_KEY="ipa_act_cat";
    //IPA Status
    public static final String IPA_STATUS_NAME="IPA Status";
    public static final String IPA_STATUS_KEY="ipa_cat_stat";
    //Contracting Type
    public static final String IPA_TYPE_NAME="IPA Type";
    public static final String IPA_TYPE_KEY="ipa_type";
    //Activity Type
    public static final String IPA_ACTIVITY_TYPE_NAME="IPA Activity Type";
    public static final String IPA_ACTIVITY_TYPE_KEY="ipa_activity_type";
    //---

    // activity indicator risk
    public static final String EVENT_TYPE_NAME = "Event Type";
    public static final String EVENT_TYPE_KEY = "event_type";
    public static final String EVENT_COLOR_NAME = "Event Color";
    public static final String EVENT_COLOR_KEY = "event_color";
    
    public static final String INDICATOR_LAYER_TYPE_NAME = "Indicator Layer Type"; 
    public static final String INDICATOR_LAYER_TYPE_KEY = "indicator_layer_type";
    
    public static final String PERFORMANCE_ALERT_LEVEL_NAME = "Performance Alert Level"; 
    public static final String PERFORMANCE_ALERT_LEVEL_KEY = "performance_alert_level";

    public static final long NONE_TYPE      = 0;
    public static final long COUNTRY_TYPE   = 1;
    public static final long REGION_TYPE    = 2;
    
    public static final HardCodedCategoryValue TYPE_OF_ASSITANCE_GRANT  = 
        new CategoryConstants.HardCodedCategoryValue("type_of_assistence","Grant", true);
    public static final HardCodedCategoryValue TYPE_OF_ASSISTANCE_LOAN  = 
            new CategoryConstants.HardCodedCategoryValue("type_of_assistence","Loan", true);
    
    public static final HardCodedCategoryValue LOGFRAME_OBJECTIVE   = 
        new CategoryConstants.HardCodedCategoryValue("logframe","Objective", true);
    public static final HardCodedCategoryValue LOGFRAME_PURPOSE = 
        new CategoryConstants.HardCodedCategoryValue("logframe","Purpose", true);
    public static final HardCodedCategoryValue LOGFRAME_RESULTS = 
        new CategoryConstants.HardCodedCategoryValue("logframe","Results", true);
    
    
    public static final HardCodedCategoryValue MTEF_PROJECTION_PROJECTION   = 
        new CategoryConstants.HardCodedCategoryValue("mtef_projection","projection", true);
    public static final HardCodedCategoryValue MTEF_PROJECTION_PIPELINE = 
        new CategoryConstants.HardCodedCategoryValue("mtef_projection","pipeline", false);
    
    public static final HardCodedCategoryValue ACTIVITY_STATUS_PROPOSED = 
        new CategoryConstants.HardCodedCategoryValue("activity_status","proposed", false);
    
    public static final HardCodedCategoryValue FIN_INSTR_BUDGET_SUPPORT = 
        new CategoryConstants.HardCodedCategoryValue("financing_instrument","Budget Support", false);
    
    public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_ADM_LEVEL_0 =
        new CategoryConstants.HardCodedCategoryValue("implementation_location", "Administrative Level 0", true);
    public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_ADM_LEVEL_1 =
        new CategoryConstants.HardCodedCategoryValue("implementation_location", "Administrative Level 1", true);
    public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_ADM_LEVEL_2 =
        new CategoryConstants.HardCodedCategoryValue("implementation_location", "Administrative Level 2", true);
    public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_ADM_LEVEL_3 =
        new CategoryConstants.HardCodedCategoryValue("implementation_location", "Administrative Level 3", true);
    public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_ADM_LEVEL_4 =
        new CategoryConstants.HardCodedCategoryValue("implementation_location", "Administrative Level 4", true);
    public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_ALL  =
            new CategoryConstants.HardCodedCategoryValue("implementation_location", "All", true);
    
    public static final HardCodedCategoryValue IMPLEMENTATION_LEVEL_INTERNATIONAL = 
         new CategoryConstants.HardCodedCategoryValue("implementation_level", "International", true);
    public static final HardCodedCategoryValue IMPLEMENTATION_LEVEL_NATIONAL = 
         new CategoryConstants.HardCodedCategoryValue("implementation_level", "National", true);
    public static final HardCodedCategoryValue IMPLEMENTATION_LEVEL_REGIONAL = 
         new CategoryConstants.HardCodedCategoryValue("implementation_level", "Regional", true);

     public static final HardCodedCategoryValue ACTIVITY_BUDGET_ON = 
         new CategoryConstants.HardCodedCategoryValue("activity_budget", "On Budget", true);
     public static final HardCodedCategoryValue ACTIVITY_BUDGET_OFF = 
         new CategoryConstants.HardCodedCategoryValue("activity_budget", "Off Budget", true);
     public static final HardCodedCategoryValue ACTIVITY_TREASURY = 
             new CategoryConstants.HardCodedCategoryValue("activity_budget", "Treasury", true);

     public static final HardCodedCategoryValue ADJUSTMENT_TYPE_ACTUAL = 
         new CategoryConstants.HardCodedCategoryValue("adjustment_type", "Actual", true);
     
     public static final HardCodedCategoryValue ADJUSTMENT_TYPE_PLANNED = 
         new CategoryConstants.HardCodedCategoryValue("adjustment_type", "Planned", true);
     
     public static final HardCodedCategoryValue ADJUSTMENT_TYPE_PIPELINE = 
             new CategoryConstants.HardCodedCategoryValue("adjustment_type", "Pipeline", false);
     
     public static final HardCodedCategoryValue ADJUSTMENT_TYPE_ODA_SSC = 
             new CategoryConstants.HardCodedCategoryValue(SSC_ADJUSTMENT_TYPE_KEY, "Official Development Aid", true);
     
     public static final HardCodedCategoryValue ADJUSTMENT_TYPE_BILATERAL_SSC = 
             new CategoryConstants.HardCodedCategoryValue(SSC_ADJUSTMENT_TYPE_KEY, "Bilateral SSC", true);
     
     public static final HardCodedCategoryValue ADJUSTMENT_TYPE_TRIANGULAR_SSC = 
             new CategoryConstants.HardCodedCategoryValue(SSC_ADJUSTMENT_TYPE_KEY, "Triangular SSC", true);

     public static final HardCodedCategoryValue ADJUSTMENT_TYPE_OFFICIAL_DEV_AID =
            new CategoryConstants.HardCodedCategoryValue(SSC_ADJUSTMENT_TYPE_KEY, "Official Development Aid", true);
     
     public static final HardCodedCategoryValue MODE_OF_PAYMENT_SALARIES_WAGES = 
             new CategoryConstants.HardCodedCategoryValue("mode_of_payment", "Salaries and Wages", false);
     public static final HardCodedCategoryValue MODE_OF_PAYMENT_GOODS_SERVICES = 
             new CategoryConstants.HardCodedCategoryValue("mode_of_payment", "Goods or Services", false);
     public static final HardCodedCategoryValue MODE_OF_PAYMENT_MINOR_CAPITAL = 
             new CategoryConstants.HardCodedCategoryValue("mode_of_payment", "Minor Capital", false);
     public static final HardCodedCategoryValue MODE_OF_PAYMENT_CAPITAL_DEVELOPMENT = 
             new CategoryConstants.HardCodedCategoryValue("mode_of_payment", "Capital Development", false);
     
     public static final HardCodedCategoryValue EXPENDITURE_CLASS_CAPITAL_EXPENDITURE = 
             new CategoryConstants.HardCodedCategoryValue("expenditure_class", "Capital Expenditure", false);
     public static final HardCodedCategoryValue EXPENDITURE_CLASS_COMPENSATION_SALARIES = 
             new CategoryConstants.HardCodedCategoryValue("expenditure_class", "Compensation / Salaries", false);
     public static final HardCodedCategoryValue EXPENDITURE_CLASS_GOODS_AND_SERVICES = 
             new CategoryConstants.HardCodedCategoryValue("expenditure_class", "Goods and Services", false);
     public static final HardCodedCategoryValue EXPENDITURE_CLASS_OTHERS = 
             new CategoryConstants.HardCodedCategoryValue("expenditure_class", "Others", false);
     
     public static final HardCodedCategoryValue INDICATOR_LAYER_TYPE_PER_CAPITA = 
             new CategoryConstants.HardCodedCategoryValue("indicator_layer_type", "Per Capita", true);
     public static final HardCodedCategoryValue INDICATOR_LAYER_TYPE_POPULATION_RATIO = 
             new CategoryConstants.HardCodedCategoryValue("indicator_layer_type", "Ratio (% of Total Population)", true);
     public static final HardCodedCategoryValue INDICATOR_LAYER_TYPE_COUNT = 
             new CategoryConstants.HardCodedCategoryValue("indicator_layer_type", "Count", true);
     
    //Org. Manager : Staff Information type
     public static final String ORGANIZATION_STAFF_INFO_NAME="Staff Information Type";
     public static final String ORGANIZATION_STAFF_INFO_KEY="staff_information_type";
     
    // organization budget Information type
    public static final String ORGANIZATION_BUDGET_INFO_NAME = "NGO Budget Type";
    public static final String ORGANIZATION_BUDGET_INFO_KEY = "ngo_budget_type";
    
    public static final String CONTACT_TITLE_NAME = "Contact Title";
    public static final String CONTACT_TITLE_KEY = "contact_title";

    public static final String CONTACT_PHONE_TYPE_NAME = "Phone Type";
    public static final String CONTACT_PHONE_TYPE_KEY = "contact_phone_type";


    public static final String RESOURCE_TYPE_COUNTRY_ANALYTIC_REPORT_KEY = "Country Analytic Report (Paris Indicator)";

    public static final String WORKSPACE_GROUP_KEY  = "workspace_group";

    public static final String PROJECT_IMPLEMENTATION_MODE_NAME     = "Project Implementation Mode";
    public static final String PROJECT_IMPLEMENTATION_MODE_KEY          = "project_implementation_mode";
    
    public static final String EXPENDITURE_CLASS_NAME = "Expenditure Class";
    public static final String EXPENDITURE_CLASS_KEY = "expenditure_class";
    
    public static final String GIS_STRUCTURES_COLOR_CODING_NAME = "GIS Structures Color Coding";
    public static final String GIS_STRUCTURES_COLOR_CODING_KEY = "gis_structures_color_coding";
    
    public static class HardCodedCategoryValue
    {
        private String valueKey;
        private String categoryKey;
        private boolean protectOnDelete;
        
        private Long databaseAcvlId = null;
        
        public String getValueKey() {
            return valueKey;
        }
        public String getCategoryKey() {
            return categoryKey;
        }
                
        public boolean isProtectOnDelete() {
            return protectOnDelete;
        }
        
        public HardCodedCategoryValue(String categoryKey, String valueKey, boolean protectOnDelete) {
            this.categoryKey        = categoryKey;
            this.valueKey           = valueKey;
            this.protectOnDelete    = protectOnDelete;
        }
        
        public boolean existsInDatabase()
        {
            return getAmpCategoryValueFromDB() != null;
        }
        
        
        public boolean isActiveInDatabase () 
        {
          AmpCategoryValue value = getAmpCategoryValueFromDB ();
          return (value != null && value.isVisible());
        }
        
        /**
         * returns null if does not exist in database
         * @return
         */
        public Long getIdInDatabase()
        {
            AmpCategoryValue val = getAmpCategoryValueFromDB();
            return val == null ? null : val.getId();
        }

        /**
         * returns the database equivalent of a hardcoded value
         * @return null if none found
         */
        public AmpCategoryValue getAmpCategoryValueFromDB()
        {
            if (databaseAcvlId == null){
                AmpCategoryClass c = CategoryManagerUtil.loadAmpCategoryClassByKey(this.getCategoryKey());
                if (c != null){
                    for(AmpCategoryValue categoryValue : c.getPossibleValues())
                        if (categoryValue != null && categoryValue.getValue().equalsIgnoreCase(this.getValueKey())) {
                            databaseAcvlId = categoryValue.getId();
                            break;
                        }
                }
            }
            if (databaseAcvlId == null)
                return null;
            return (AmpCategoryValue) PersistenceManager.getSession().get(AmpCategoryValue.class, databaseAcvlId);
        }
        
        public boolean equalsCategoryValue(AmpCategoryValue value)
        {
            return value != null && value.getAmpCategoryClass().getKeyName().equals(this.getCategoryKey()) && value.getValue().equals(this.getValueKey());
        }

    }
}

