package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.activity.builder.FundingBuilder;
import org.dgfoundation.amp.activity.builder.TransactionBuilder;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoDisbursmentsAfterFundingDateMatcherDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

/**
 * A disbursement happened after (transaction date itself) the project closing date 
 * 
 * @author Viorel Chihai
 */
public class DisbursementsAfterActivityDateMatcherTest extends PerformanceRuleMatcherTest {
    
    @Before
    public void setUp() {
        super.setUp();
        definition = new NoDisbursmentsAfterFundingDateMatcherDefinition();
    }

    @Test
    public void testValidation() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getCriticalLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);
        
        assertTrue(matcher.validate());
    }
    
    @Test
    public void testTwoDisbursementsAfterSelectedDate() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getCriticalLevel());
        
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
                .withActualCompletionDate(new LocalDate(2015, 11, 12).toDate())
                .getActivity();
        
        assertFalse(matcher.match(a));
    }
    
    @Test
    public void testTwoFundingsWithDisbursementsAfterSelectedDate() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_DAY, "1", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getCriticalLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);
        
        AmpActivityVersion act2 = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2016, 12, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2015, 10, 12).toDate())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .withActualCompletionDate(new LocalDate(2016, 11, 12).toDate())
                .getActivity();
        
        assertTrue(matcher.match(act2));
    }
    
    @Test
    public void testTwoFundingsWithDisbursementsBeforeSelectedDate() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_YEAR, "1", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getCriticalLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);
        
        AmpActivityVersion act3 = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2016, 12, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2015, 10, 12).toDate())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .getFunding())
                .withActualCompletionDate(new LocalDate(2017, 11, 12).toDate())
                .getActivity();
        
        assertFalse(matcher.match(act3));
    }
        
    public AmpPerformanceRule createRule(String timeUnit, String timeAmount, String fundingDate, 
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

        rule.setAttributes(Stream.of(attr1, attr2, attr3).collect(Collectors.toSet()));
        rule.setLevel(level);

        return rule;
    }
    
}
