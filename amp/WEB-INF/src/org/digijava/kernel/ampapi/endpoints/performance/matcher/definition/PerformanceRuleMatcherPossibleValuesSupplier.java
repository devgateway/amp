package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * 
 * @author Viorel Chihai
 *
 */
public final class PerformanceRuleMatcherPossibleValuesSupplier {
    
    private static PerformanceRuleMatcherPossibleValuesSupplier instance;
    
    private Function<PerformanceRuleAttributeType, List<PerformanceRuleAttributeOption>> supplier;

    private PerformanceRuleMatcherPossibleValuesSupplier() {

    }

    public static List<PerformanceRuleAttributeOption> getDefaultPerformanceRuleAttributePossibleValues(
            PerformanceRuleAttributeType type) {
        switch (type) {
            case TIME_UNIT:
                return getPeriodPossibleValues();
            case ACTIVITY_STATUS:
                return getActivityStatusPossibleValues();
            case ACTIVITY_DATE:
                return getActivityDatePossibleValues();
            case FUNDING_DATE:
                return getFundingDatePossibleValues();
            default:
                return Collections.emptyList();
        }
    }

    public static List<PerformanceRuleAttributeOption> getActivityStatusPossibleValues() {
        return CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY).stream()
                .map(acv -> new PerformanceRuleAttributeOption(acv.getLabel()))
                .collect(Collectors.toList());
    }

    public static List<PerformanceRuleAttributeOption> getPeriodPossibleValues() {
        return Arrays.asList(
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.TIME_UNIT_DAY),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.TIME_UNIT_MONTH),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.TIME_UNIT_YEAR));
    }

    public static List<PerformanceRuleAttributeOption> getActivityDatePossibleValues() {
        return Arrays.asList(
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_COMPLETION_DATE, 
                        "Actual Completion Date", "/Activity Form/Planning/Actual Completion Date"),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_ACTUAL_APPROVAL_DATE, 
                        "Actual Approval Date", "/Activity Form/Planning/Actual Approval Date"),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_PROPOSED_START_DATE, 
                        "Proposed Start Date", "/Activity Form/Planning/Proposed Start Date"),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_ORIGINAL_COMPLETING_DATE, 
                        "Original Completion Date", "/Activity Form/Planning/Original Completion Date"),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_ACTUAL_START_DATE, 
                        "Actual Start Date", "/Activity Form/Planning/Actual Start Date"),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_CONTRACTING_DATE, 
                        "Contracting Date", "/Activity Form/Planning/Final Date for Contracting"),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_DISBURSEMENTS_DATE, 
                        "Disbursement Date", "/Activity Form/Planning/Final Date for Disbursements"));
    }

    public static List<PerformanceRuleAttributeOption> getFundingDatePossibleValues() {
        return Arrays.asList(
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, 
                "Funding Classification Date", 
                "/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Classification Date"),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.FUNDING_EFFECTIVE_DATE, 
                "Effective Funding Date", 
                "/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Effective Funding Date"),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.FUNDING_CLOSING_DATE, 
                "Funding Closing Date",
                "/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Closing Date"));
    }

    public static PerformanceRuleMatcherPossibleValuesSupplier getInstance() {
        if (instance == null) {
            instance = new PerformanceRuleMatcherPossibleValuesSupplier();
        }
        
        return instance;
    }
    
    public void setSupplier(Function<PerformanceRuleAttributeType, List<PerformanceRuleAttributeOption>> supplier) {
        this.supplier = supplier;
    }
    
    public Function<PerformanceRuleAttributeType, List<PerformanceRuleAttributeOption>> getSupplier() {
        if (supplier == null) {
            supplier = PerformanceRuleMatcherPossibleValuesSupplier::getDefaultPerformanceRuleAttributePossibleValues;
        }
        
        return supplier;
    }
}
