package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.activity.builder.FundingBuilder;
import org.dgfoundation.amp.activity.builder.OrganisationBuilder;
import org.dgfoundation.amp.activity.builder.TransactionBuilder;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoDisbursementsAfterFundingDateMatcherDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

/**
 * A period have passed since the project signature date and still no disbursement from donor
 * 
 * @author Viorel Chihai
 */
public class NoDisbursementsAfterFundingDateMatcherTests extends PerformanceRuleMatcherTests {
    
    @Before
    public void setUp() {
        super.setUp();
        definition = new NoDisbursementsAfterFundingDateMatcherDefinition();
    }
    
    @Test
    public void testValidation() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getCriticalLevel());
        
        assertNotNull(definition.createMatcher(rule));
    }
    
    @Test
    public void testNoFundingClassificationDate() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor2")
                                        .getOrganisation())
                                .getFunding())
                .getActivity();
        
        assertNull(findPerformanceIssue(rule, a));
    }

    @Test
    public void testOneDisbursementBeforeFundingClassificationDate() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(DateTime.now().minusDays(3).toDate())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor1")
                                        .getOrganisation())
                                .getFunding())
                .getActivity();
        
        assertNull(findPerformanceIssue(rule, a));
    }
    
    @Test
    public void testNoDisbursementsNoFundingDate() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", 
                PerformanceRuleConstants.FUNDING_EFFECTIVE_DATE, getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2016, 12, 12).toDate())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor1")
                                        .getOrganisation())
                                .getFunding())
                .getActivity();
        
        assertNull(findPerformanceIssue(rule, a));
    }
    
    @Test
    public void testTwoDisbursementAfterFundingClassificationDate() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_YEAR, "1", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(DateTime.now().minusMonths(3).toDate())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2018, 12, 12).toDate())
                                        .getTransaction())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2014, 12, 12).toDate())
                                        .getTransaction())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor1")
                                        .getOrganisation())
                                .getFunding())
                .getActivity();
        
        assertNull(findPerformanceIssue(rule, a));
    }
    
    @Test
    public void testNoDisbursement() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_DAY, "20", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getMinorLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2016, 11, 11).toDate())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.COMMITMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.COMMITMENT)
                                        .withTransactionDate(new LocalDate(2014, 12, 12).toDate())
                                        .getTransaction())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor1")
                                        .getOrganisation())
                                .getFunding())
                .getActivity();
        
        assertNotNull(findPerformanceIssue(rule, a));
    }
    
    @Test
    public void testWithDisbursement() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_DAY, "30", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getMinorLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2016, 11, 11).toDate())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.COMMITMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2016, 12, 12).toDate())
                                        .getTransaction())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor1")
                                        .getOrganisation())
                                .getFunding())
                .getActivity();
        
        assertNull(findPerformanceIssue(rule, a));
    }
    
    @Test
    public void testTwoFundingsBeforeFundingClassificationDate() {
       
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2016, 11, 13).toDate())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor1")
                                        .getOrganisation())
                                .getFunding())
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2014, 11, 13).toDate())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.COMMITMENT)
                                        .withTransactionDate(new LocalDate(2013, 12, 12).toDate())
                                        .getTransaction())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor2")
                                        .getOrganisation())
                                .getFunding())
                .getActivity();
        
        assertEquals(findPerformanceIssue(rule, a).getDonors().size(), 1);
    }
    
    public void testTwoFundingsBeforeFundingEffectiveDate() {
        
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "1", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, getCriticalLevel());
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2016, 06, 30).toDate())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2017, 12, 31).toDate())
                                        .getTransaction())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor1")
                                        .getOrganisation())
                                .getFunding())
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2014, 11, 13).toDate())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.DISBURSEMENT)
                                        .withTransactionDate(new LocalDate(2015, 12, 12).toDate())
                                        .getTransaction())
                                .addTransaction(new TransactionBuilder()
                                        .withTransactionType(Constants.COMMITMENT)
                                        .withTransactionDate(new LocalDate(2017, 6, 30).toDate())
                                        .getTransaction())
                                .withDonor(new OrganisationBuilder()
                                        .withOrganisationName("Donor2")
                                        .getOrganisation())
                                .getFunding())
                .getActivity();
        
        assertNull(findPerformanceIssue(rule, a));
    }

    /**
     * @return
     */
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
        
        rule.setAttributes(ImmutableSet.of(attr1, attr2, attr3));
        rule.setLevel(level);

        return rule;
    }
    
}
