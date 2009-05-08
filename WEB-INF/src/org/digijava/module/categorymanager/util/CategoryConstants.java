package org.digijava.module.categorymanager.util;


public class CategoryConstants {
	public static final String ACCHAPTER_NAME	= "A.C. Chapter";
	public static final String ACCHAPTER_KEY	= "acchapter";
	public static final String ACCESSION_INSTRUMENT_NAME		= "Accession Instrument";
	public static final String ACCESSION_INSTRUMENT_KEY		= "accessioninstr";
	public static final String DOCUMENT_TYPE_NAME		= "Document type";
	public static final String DOCUMENT_TYPE_KEY		= "docType"; 
	public static final String LOGFRAME_NAME			= "Logframe";
	public static final String LOGFRAME_KEY			= "logframe";
	
	public static final String ACTIVITY_STATUS_NAME		= "Activity Status";
	public static final String ACTIVITY_STATUS_KEY			= "activity_status";
	
	public static final String IMPLEMENTATION_LEVEL_NAME			= "Implementation Level";
	public static final String IMPLEMENTATION_LEVEL_KEY			= "implementation_level";
	
	public static final String IMPLEMENTATION_LOCATION_NAME			= "Implementation Location";
	public static final String IMPLEMENTATION_LOCATION_KEY				= "implementation_location";
	
	public static final String REFERENCE_DOCS_KEY = "reference_docs";
	
	public static final String TEAM_TYPE_NAME		= "Team Type";
	public static final String TEAM_TYPE_KEY		= "team_type";
	
	public static final String TYPE_OF_ASSISTENCE_NAME		= "Type of Assistence";
	public static final String TYPE_OF_ASSISTENCE_KEY		= "type_of_assistence";
	
	public static final String FINANCING_INSTRUMENT_NAME		= "Financing Instrument";
	public static final String FINANCING_INSTRUMENT_KEY		= "financing_instrument";
	
	public static final String PROGRAM_TYPE_NAME				= "Program Type";
	public static final String PROGRAM_TYPE_KEY				= "program_type";
	
	public static final String DOCUMENT_LANGUAGE_NAME				= "Document Languages";
	public static final String DOCUMENT_LANGUAGE_KEY				= "document_languages";
	
	public static final String FINANCIAL_INSTRUMENT_NAME			="Financial Instrument";
	public static final String FINANCIAL_INSTRUMENT_KEY			="financial_instrument";
	
	public static final String MTEF_PROJECTION_NAME				="MTEF Projection";
	public static final String MTEF_PROJECTION_KEY					="mtef_projection";
	
	public static final String ACTIVITY_LEVEL_NAME					="Activity Level";
	public static final String ACTIVITY_LEVEL_KEY					="activity_level";

	public static final String PROJECT_CATEGORY_NAME		= "Project Category";
	public static final String PROJECT_CATEGORY_KEY		= "project_category";
	
	public static final String DATA_EXCHANGE_NAME		= "Data Echange Category";
	public static final String DATA_EXCHANGE_KEY		= "data_exchange";
	
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

	public static final long NONE_TYPE		= 0;
	public static final long COUNTRY_TYPE	= 1;
	public static final long REGION_TYPE	= 2;
	
	public static final HardCodedCategoryValue TYPE_OF_ASSITANCE_GRANT	= 
		new CategoryConstants.HardCodedCategoryValue("type_of_assistence","Grant", true);
	
	public static final HardCodedCategoryValue LOGFRAME_OBJECTIVE	= 
		new CategoryConstants.HardCodedCategoryValue("logframe","Objective", true);
	public static final HardCodedCategoryValue LOGFRAME_PURPOSE	= 
		new CategoryConstants.HardCodedCategoryValue("logframe","Purpose", true);
	public static final HardCodedCategoryValue LOGFRAME_RESULTS	= 
		new CategoryConstants.HardCodedCategoryValue("logframe","Results", true);
	
	public static final HardCodedCategoryValue TEAM_TYPE_MULTILATERAL	= 
		new CategoryConstants.HardCodedCategoryValue("team_type","Multilateral", false);
	public static final HardCodedCategoryValue TEAM_TYPE_BILATERAL	= 
		new CategoryConstants.HardCodedCategoryValue("team_type","Bilateral", false);
	
	public static final HardCodedCategoryValue MTEF_PROJECTION_PROJECTION	= 
		new CategoryConstants.HardCodedCategoryValue("mtef_projection","projection", false);
	public static final HardCodedCategoryValue MTEF_PROJECTION_PIPELINE	= 
		new CategoryConstants.HardCodedCategoryValue("mtef_projection","pipeline", false);
	
	public static final HardCodedCategoryValue ACTIVITY_STATUS_PROPOSED	= 
		new CategoryConstants.HardCodedCategoryValue("activity_status","proposed", false);
	
	public static final HardCodedCategoryValue FIN_INSTR_BUDGET_SUPPORT	= 
		new CategoryConstants.HardCodedCategoryValue("financing_instrument","Budget Support", false);
	
	public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_COUNTRY	= 
		new CategoryConstants.HardCodedCategoryValue("implementation_location", "Country", true);
	public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_REGION	= 
		new CategoryConstants.HardCodedCategoryValue("implementation_location", "Region", true);
	public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_ZONE	= 
		new CategoryConstants.HardCodedCategoryValue("implementation_location", "Zone", true);
	public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_DISTRICT	= 
		new CategoryConstants.HardCodedCategoryValue("implementation_location", "District", true);
	
	public static class HardCodedCategoryValue {
		private String valueKey;
		private String categoryKey;
		private boolean protectOnDelete;
		public String getValueKey() {
			return valueKey;
		}
		public String getCategoryKey() {
			return categoryKey;
		}
				
		public boolean isProtectOnDelete() {
			return protectOnDelete;
		}
		private HardCodedCategoryValue(String categoryKey, String valueKey, boolean protectOnDelete) {
			this.categoryKey		= categoryKey;
			this.valueKey			= valueKey;
			this.protectOnDelete	= protectOnDelete;
		}
	}
}

