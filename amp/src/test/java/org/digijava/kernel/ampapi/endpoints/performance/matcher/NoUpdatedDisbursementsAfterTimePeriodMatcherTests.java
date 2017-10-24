package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.activity.builder.FundingBuilder;
import org.dgfoundation.amp.activity.builder.TransactionBuilder;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoUpdatedDisbursementsAfterTimePeriodMatcherDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

/**
 * Disbursements (actual or planned) from one or more donor have not been updated in the last 3 months
 * 
 * @author Viorel Chihai
 */
public class NoUpdatedDisbursementsAfterTimePeriodMatcherTests extends PerformanceRuleMatcherTests {
    
    @Before
    public void setUp() {
        super.setUp();
        definition = new NoUpdatedDisbursementsAfterTimePeriodMatcherDefinition();
    }
    
    @Test
    public void testValidation() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", getCriticalLevel());
        
        assertNotNull(definition.createMatcher(rule));
    }

    @Test
    public void testOneDisbursementLastPeriod() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", getMajorLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .getActivity();
        
        assertTrue(match(rule, a));
    }
    
    @Test
    public void testNoDisbursementLastPeriod() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", getMajorLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.COMMITMENT)
                                        .withTransactionDate(new LocalDate(2017, 7, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .getActivity();
        
        assertTrue(match(rule, a));
    }
    
    @Test
    public void testTwoDisbursementLastPeriod() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_DAY, "120", getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2017, 7, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .getActivity();
        
        assertFalse(match(rule, a));
    }
    
    @Test
    public void testTwoFundingsLastPeriod() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_YEAR, "1", getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .addFunding(
                        new FundingBuilder()
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.COMMITMENT)
                                        .withTransactionDate(new LocalDate(2013, 12, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .getActivity();
        
        assertTrue(match(rule, a));
    }

    /**
     * @return
     */
    public AmpPerformanceRule createRule(String timeUnit, String timeAmount, AmpCategoryValue level) {
        
        AmpPerformanceRule rule = new AmpPerformanceRule();
        
        AmpPerformanceRuleAttribute attr1 = new AmpPerformanceRuleAttribute();
        attr1.setName(PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT);
        attr1.setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.TIME_UNIT);
        attr1.setValue(timeUnit);
        AmpPerformanceRuleAttribute attr2 = new AmpPerformanceRuleAttribute();
        attr2.setName(PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT);
        attr2.setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.AMOUNT);
        attr2.setValue(timeAmount);
        
        rule.setAttributes(ImmutableSet.of(attr1, attr2));
        rule.setLevel(level);

        return rule;
    }
    
}
