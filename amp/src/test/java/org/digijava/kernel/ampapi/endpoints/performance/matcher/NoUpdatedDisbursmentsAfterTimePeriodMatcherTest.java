package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import static org.junit.Assert.assertTrue;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.activity.builder.FundingBuilder;
import org.dgfoundation.amp.activity.builder.TransactionBuilder;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoUpdatedDisbursmentsAfterTimePeriodMatcherDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

/**
 * Disbursements (actual or planned) from one or more donor have not been updated in the last 3 months
 * 
 * @author Viorel Chihai
 */
public class NoUpdatedDisbursmentsAfterTimePeriodMatcherTest extends PerformanceRuleMatcherTest {
    
    @Before
    public void setUp() {
        super.setUp();
        definition = new NoUpdatedDisbursmentsAfterTimePeriodMatcherDefinition();
    }
    
    @Test
    public void testValidation() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", getCriticalLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);

        assertTrue(matcher.validate());
    }

    @Test
    public void testOneDisbursementLastPeriod() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", getMajorLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .getActivity();
        
        assertTrue(matcher.match(a));
    }
    
    @Test
    public void testNoDisbursementLastPeriod() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", getMajorLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.COMMITMENT)
                                        .withTransactionDate(new LocalDate(2017, 7, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .getActivity();
        
        assertTrue(matcher.match(a));
    }
    
    @Test
    public void testTwoDisbursementLastPeriod() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_DAY, "30", getCriticalLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2014, 12, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .getActivity();
        
        assertTrue(matcher.match(a));
    }
    
    @Test
    public void testTwoFundingsLastPeriod() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_YEAR, "1", getCriticalLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);
        
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
        
        assertTrue(matcher.match(a));
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
        
        rule.setAttributes(Stream.of(attr1, attr2).collect(Collectors.toSet()));
        rule.setLevel(level);

        return rule;
    }
    
}
