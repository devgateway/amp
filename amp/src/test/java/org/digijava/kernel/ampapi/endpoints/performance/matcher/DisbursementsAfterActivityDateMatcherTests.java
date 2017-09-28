package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.activity.builder.FundingBuilder;
import org.dgfoundation.amp.activity.builder.TransactionBuilder;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.DisbursementsAfterActivityDateMatcherDefinition;
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
 * A disbursement happened after (transaction date itself) the project closing date 
 * 
 * @author Viorel Chihai
 */
public class DisbursementsAfterActivityDateMatcherTests extends PerformanceRuleMatcherTests {
    
    @Before
    public void setUp() {
        super.setUp();
        definition = new DisbursementsAfterActivityDateMatcherDefinition();
    }

    @Test
    public void testValidation() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.ACTIVITY_COMPLETION_DATE, getCriticalLevel());
        
        assertNotNull(definition.createMatcher(rule));
    }
    
    @Test
    public void testTwoDisbursementsAfterSelectedDate() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.ACTIVITY_COMPLETION_DATE, getCriticalLevel());
        
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
                .withActualCompletionDate(new LocalDate(2011, 11, 12).toDate())
                .getActivity();
        
        assertTrue(match(rule, a));
    }
    
    @Test
    public void testTwoFundingsWithDisbursementsAfterSelectedDate() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.ACTIVITY_COMPLETION_DATE, getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
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
        
        assertTrue(match(rule, a));
    }
    
    @Test
    public void testTwoFundingsWithDisbursementsBeforeSelectedDate() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.ACTIVITY_COMPLETION_DATE, getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
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
                .withActualCompletionDate(new LocalDate(2016, 12, 13).toDate())
                .getActivity();
        
        assertFalse(match(rule, a));
    }
        
    public AmpPerformanceRule createRule(String activityDate, AmpCategoryValue level) {

        AmpPerformanceRule rule = new AmpPerformanceRule();

        AmpPerformanceRuleAttribute attr1 = new AmpPerformanceRuleAttribute();
        attr1.setName(PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_DATE);
        attr1.setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.ACTIVITY_DATE);
        attr1.setValue(activityDate);

        rule.setAttributes(ImmutableSet.of(attr1));
        rule.setLevel(level);

        return rule;
    }
    
}
