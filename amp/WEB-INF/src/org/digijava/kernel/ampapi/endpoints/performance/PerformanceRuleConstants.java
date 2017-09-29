package org.digijava.kernel.ampapi.endpoints.performance;

public final class PerformanceRuleConstants {

    public static final int DEFAULT_RECORDS_PER_PAGE = 10;
    public static final int DEFAULT_START_PAGE = 0;
    
    public static final String TIME_UNIT_DAY = "day";
    public static final String TIME_UNIT_MONTH = "month";
    public static final String TIME_UNIT_YEAR = "year";
    
    public static final String ACTIVITY_COMPLETION_DATE = "activityCompletionDate";
    public static final String ACTIVITY_ACTUAL_APPROVAL_DATE = "activityActualApprovalDate";
    public static final String ACTIVITY_PROPOSED_START_DATE = "activityProposedStartDate";
    public static final String ACTIVITY_ORIGINAL_COMPLETING_DATE = "activityOriginalCompletingDate";
    public static final String ACTIVITY_ACTUAL_START_DATE = "activityActualStartDate";
    public static final String ACTIVITY_CONTRACTING_DATE = "activityContractingDate";
    public static final String ACTIVITY_DISBURSEMENTS_DATE = "activityDisbursementsDate";

    public static final String FUNDING_CLASSIFICATION_DATE = "fundingClassificationDate";
    public static final String FUNDING_EFFECTIVE_DATE = "fundingEffectiveDate";
    public static final String FUNDING_CLOSING_DATE = "fundingClosingDate";
    
    public static final String ATTRIBUTE_TIME_AMOUNT = "timeAmount";
    public static final String ATTRIBUTE_TIME_UNIT = "timeUnit";
    public static final String ATTRIBUTE_ACTIVITY_STATUS = "activityStatus";
    public static final String ATTRIBUTE_ACTIVITY_DATE = "activityDate";
    public static final String ATTRIBUTE_FUNDING_DATE = "fundingDate";
    
    public static final String JSON_ATTRIBUTE_TYPE_CLASS_NAME = "type-class-name";
    public static final String JSON_ATTRIBUTE_POSSIBLE_VALUES = "possible-values";
    public static final String JSON_ATTRIBUTE_TRANSLATED_LABEL = "translated-label";
    
    public static final String HAS_ENABLED_PERFORMANCE_RULES = "hasEnabledPerformanceRules";

    private PerformanceRuleConstants() { }

}
