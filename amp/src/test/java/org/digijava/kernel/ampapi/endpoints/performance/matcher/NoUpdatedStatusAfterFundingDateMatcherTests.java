package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.activity.builder.*;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoUpdatedStatusAfterFundingDateMatcherDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 3 months went by after the contract signature date and the project status was not modified from planned to ongoing
 * 
 * @author Viorel Chihai
 */
public class NoUpdatedStatusAfterFundingDateMatcherTests extends PerformanceRuleMatcherTests {
    
    @Before
    public void setUp() {
        super.setUp();
        definition = new NoUpdatedStatusAfterFundingDateMatcherDefinition();
    }
    
    @Test
    public void testValidation() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "3", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, Constants.ACTIVITY_STATUS_ONGOING, 
                getCriticalLevel());
        
        assertNotNull(definition.createMatcher(rule));
    }

    @Test
    public void testPlannedStatusAfterClasificationDate() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "3", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, Constants.ACTIVITY_STATUS_ONGOING, 
                getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2016, 12, 12).toDate())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor1")
                                        .getOrganisation())
                                .getFunding())
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new DateTime().plusMonths(4).toDate())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor2")
                                        .getOrganisation())
                                .getFunding())
                .addCategoryValue(
                        new CategoryValueBuilder()
                                .withLabel(Constants.ACTIVITY_STATUS_PLANNED)
                                .withCategoryClass(
                                        new CategoryClassBuilder()
                                        .withKey(CategoryConstants.ACTIVITY_STATUS_KEY)
                                        .getCategoryClass())
                                .getCategoryValue())
                .getActivity();
        
        assertEquals(findPerformanceIssue(rule, a).getDonors().size(), 1);
    }
    
    @Test
    public void testUpdatedActivityStatus() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "3", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, Constants.ACTIVITY_STATUS_ONGOING, 
                getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2015, 10, 12).toDate())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor1")
                                        .getOrganisation())
                                .getFunding())
                .addCategoryValue(
                        new CategoryValueBuilder()
                                .withLabel(Constants.ACTIVITY_STATUS_ONGOING)
                                .withCategoryClass(
                                        new CategoryClassBuilder()
                                        .withKey(CategoryConstants.ACTIVITY_STATUS_KEY)
                                        .getCategoryClass())
                                .getCategoryValue())
                .getActivity();
        
        assertNull(findPerformanceIssue(rule, a));
    }
    
    @Test
    public void testNotUpdatedActivitySatus() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "3", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, Constants.ACTIVITY_STATUS_ONGOING, 
                getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2017, 3, 13).toDate())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor1")
                                        .getOrganisation())
                                .getFunding())
                .addCategoryValue(
                        new CategoryValueBuilder()
                                .withLabel(Constants.ACTIVITY_STATUS_PLANNED)
                                .withCategoryClass(
                                        new CategoryClassBuilder()
                                        .withKey(CategoryConstants.ACTIVITY_STATUS_KEY)
                                        .getCategoryClass())
                                .getCategoryValue())
                .getActivity();
        
        assertEquals(findPerformanceIssue(rule, a).getDonors().size(), 1);
    }
    
    @Test
    public void testActivityStatusUpdatedWihtoutTimePeriod() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "3", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, Constants.ACTIVITY_STATUS_ONGOING, 
                getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2017, 7, 13).toDate())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor2")
                                        .getOrganisation())
                                .getFunding())
                .addCategoryValue(
                        new CategoryValueBuilder()
                                .withLabel(Constants.ACTIVITY_STATUS_ONGOING)
                                .withCategoryClass(
                                        new CategoryClassBuilder()
                                        .withKey(CategoryConstants.ACTIVITY_STATUS_KEY)
                                        .getCategoryClass())
                                .getCategoryValue())
                .getActivity();
        
        assertNull(findPerformanceIssue(rule, a));
    }

    /**
     * @return
     */
    public AmpPerformanceRule createRule(String timeUnit, String timeAmount, String fundingDate, String activityStatus, 
            AmpCategoryValue level) {
        
        AmpPerformanceRule rule = new AmpPerformanceRule();

        AmpPerformanceRuleAttribute attr1 = new AmpPerformanceRuleAttribute();
        attr1.setName(PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT);
        attr1.setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.TIME_UNIT);
        attr1.setValue(timeUnit);
        AmpPerformanceRuleAttribute attr2 = new AmpPerformanceRuleAttribute();
        attr2.setName(PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT);
        attr2.setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.AMOUNT);
        attr2.setValue(timeAmount);
        AmpPerformanceRuleAttribute attr3 = new AmpPerformanceRuleAttribute();
        attr3.setName(PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE);
        attr3.setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.FUNDING_DATE);
        attr3.setValue(fundingDate);
        AmpPerformanceRuleAttribute attr4 = new AmpPerformanceRuleAttribute();
        attr4.setName(PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_STATUS);
        attr4.setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.ACTIVITY_STATUS);
        attr4.setValue(activityStatus);

        rule.setAttributes(ImmutableSet.of(attr1, attr2, attr3, attr4));
        rule.setLevel(level);

        return rule;
    }
    
}
