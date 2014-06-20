package org.digijava.module.dataExchange.util;

import java.util.HashMap;
import java.util.Map;

public class DataExchangeConstants {

//	public static String ADJUSTMENT_TYPE_ACTUAL = "Actual";
//	public static String ADJUSTMENT_TYPE_PLANNED = "Planned";
	
	public static String ORG_ROLE_REPORTING_AGENCY = "Reporting Agency";
	public static String ORG_ROLE_FUNDING_AGENCY = "Funding Agency";
	public static String ORG_ROLE_IMPLEMENTING_AGENCY = "Implementing Agency";
	public static String ORG_ROLE_BENEFICIARY_AGENCY = "Beneficiary Agency";
	public static String ORG_ROLE_CONTRACTING_AGENCY = "Contracting Agency";
	public static String ORG_ROLE_REGIONAL_GROUP = "Regional Group";
	public static String ORG_ROLE_SECTOR_GROUP = "Sector Group";
	public static String ORG_ROLE_EXECUTING_AGENCY= "Executing Agency";
	public static String ORG_ROLE_RESPONSIBLE_ORGANIZATION = "Responsible Organization";
	public static String ORG_ROLE_CONTRACTOR = "Contractor";
	public static String ORG_ROLE_RELEATED_INSTITUTIONS = "Releated Institutions";
	
	
	public static String LOCATION_TYPE_COUNTRY = "Country";
	public static String LOCATION_TYPE_REGION = "Region";
	public static String LOCATION_TYPE_ZONE = "Zone";
	public static String LOCATION_TYPE_WOREDA = "Woreda";
	public static String LOCATION_TYPE_DEPARTMENT = "Department";
	public static String LOCATION_TYPE_DISTRICT = "District";
	
	public static final String MISSING_ORGANIZATION = "Missing organizations";
	public static final String MISSING_SECTOR = "Missing sectors";
	public static final String MISSING_MTEF = "Missing mtef projections";
	public static final String MISSING_PROGRAM = "Missing programs";
	public static final String MISSING_STATUS = "Missing status";
	public static final String SECTOR_PERCENTAGE = "Sector percentage";
	public static final String PROGRAM_PERCENTAGE = "Program percentage";
	public static final String MISSING_IMPLEMENTATION_LOCATION = "Missing implementation location";
	public static final String MISSING_IMPLEMENTATION_LEVEL = "Missing implementation level";
	public static final String MISSING_FINANCING_INSTRUMENT = "Missing financing instrument";
	public static final String MISSING_TYPE_OF_ASSISTANCE = "Missing type of assistance";
	public static final String MISSING_CURRENCY = "Missing currency";
	public static final String MISSING_LOCATION = "Missing location";
	
	public static final String ACTIVITY_LOG = "Activity log";
	
	public static final String TEXT = "text";
	
	public static final String COLUMN_KEY_TITLE = "title";
	public static final String COLUMN_KEY_ID = "id";
	public static final String COLUMN_KEY_AMPID = "ampid";
	public static final String COLUMN_KEY_PTIP = "ptip";
	
	public static final String IATI_ORGANIZATION_IDENTIFIER = "OrganisationIdentifier";
	public static final String IATI_DOCUMENT_CATEGORY = "DocumentCategory";
	public static final String IATI_COLLABORATION_TYPE = "Collaboration Type";
	public static final String IATI_ORGANIZATION_TYPE = "Organization Type";
	public static final String IATI_ORGANIZATION = "Organization";
	public static final String IATI_LOCATION = "Location";
	public static final String IATI_ACTIVITY_STATUS = "Activity Status";
	public static final String IATI_VOCABULARY_CODE = "Vocabulary Code";
	public static final String AMP_VOCABULARY_CODE = "Sector Scheme";
	public static final String IATI_SECTOR = "Sector";
	//type of assistance
	public static final String IATI_FINANCE_TYPE = "Finance Type";
	public static final String IATI_FINANCE_TYPE_CATEGORY = "Finance Type (category)";
	//financing instrument
	public static final String IATI_AID_TYPE = "Aid Type";
	public static final String IMPLEMENTATION_LEVEL_TYPE = "Implementation Level";
	//mode of payment
	public static final String IATI_DISBURSEMENT_CHANNEL = "Disbursement Channel";
	public static final String TEST = "test";
	public static final String IATI_ACTIVITY = "Activity";

    public static final Map <String, String> AmpToIATI = new HashMap<String, String>()
    {
        {
            put(IATI_ORGANIZATION_TYPE, "OrganisationType");
            put(IATI_ACTIVITY_STATUS, "ActivityStatus");
            put(IATI_VOCABULARY_CODE, "Vocabulary");
            put(IATI_AID_TYPE, "AidTypeFlag");
            put(IATI_DISBURSEMENT_CHANNEL, "DisbursementChannel");
        }
    };


    /* All IATI Code types
     */
    public enum IatiCodeTypeEnum{
    	None("None"),
    	ActivityDateType("ActivityDateType"),
    	ActivityScope("ActivityScope"),
    	ActivityStatus("ActivityStatus"),
    	AidType("AidType"),
    	AidTypeFlag("AidTypeFlag"),
    	BudgetIdentifier("BudgetIdentifier"),
    	BudgetIdentifierVocabulary("BudgetIdentifierVocabulary"), 
    	BudgetType("BudgetType"),
    	CollaborationType("CollaborationType"),
    	ConditionType("ConditionType"),
    	ContactType("ContactType"),
    	DescriptionType("DescriptionType"),
    	DisbursementChannel("DisbursementChannel"),
    	DocumentCategoryCategory("DocumentCategory-category"),
    	DocumentCategory("DocumentCategory"),
    	FileFormat("FileFormat"),
    	FinanceTypeCategory("FinanceType-category"),
    	GazetteerAgency("GazetteerAgency"),
    	GeographicalPrecision("GeographicalPrecision"),
    	IndicatorMeasure("IndicatorMeasure"),
    	LoanRepaymentPeriod("LoanRepaymentPeriod"),
    	LoanRepaymentType("LoanRepaymentType"),
    	LocationType("LocationType"),
    	OrganisationIdentifier("OrganisationIdentifier"),
    	OrganisationRole("OrganisationRole"),
    	OrganisationType("OrganisationType"),
    	PolicyMarker("PolicyMarker"),
    	PublisherType("PublisherType"),
    	RegionVocabulary("RegionVocabulary"),
    	RelatedActivityType("RelatedActivityType"),
    	ResultType("ResultType"),
    	Sector("Sector"),
    	TiedStatus("TiedStatus"),
    	TransactionType("TransactionType"),
    	VerificationStatus("VerificationStatus"),
    	Vocabulary("Vocabulary");
    	
    	private String value;
    	IatiCodeTypeEnum(String value) {
    		this.value = value;
    	}
    	@Override
    	public String toString(){
    		return this.value;
    	}
    	
    	public static IatiCodeTypeEnum getValueOf(String value) {
    		for (IatiCodeTypeEnum typeName:IatiCodeTypeEnum.values()) {
    			if (typeName.toString().equals(value)) {
    				return typeName;
    			}
    		}
    		return None;
    	}
    };

}
