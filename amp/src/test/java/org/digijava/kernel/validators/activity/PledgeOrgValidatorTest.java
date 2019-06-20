package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.digijava.kernel.validators.activity.ValidatorMatchers.containsInAnyOrder;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.activity.builder.FundingBuilder;
import org.dgfoundation.amp.activity.builder.TransactionBuilder;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class PledgeOrgValidatorTest {

    private static APIField activityField;
    private static FundingPledges usaidPledge;
    private static HardcodedOrgs orgs;
    private static HardcodedCategoryValues categoryValues;

    @BeforeClass
    public static void setUp() {
        activityField = ValidatorUtil.getMetaData();

        orgs = new HardcodedOrgs();
        categoryValues = new HardcodedCategoryValues();

        usaidPledge = new FundingPledges();
        usaidPledge.setId(100L);
        usaidPledge.setOrganizationGroup(orgs.getUsaidGroup());
    }

    @Test
    public void testNullDonor() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new AmpFunding())
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testDonorMatches() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder()
                        .withDonor(orgs.getUsaid())
                        .withTypeOfAssistance(categoryValues.getTypeOfAssistanceValues().getGrant())
                        .withFinancingInstrument(categoryValues.getFinancingInstruments().getDebtRelief())
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .withPledge(usaidPledge)
                                .getTransaction())
                        .getFunding())
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testDonorDoesNotMatch() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder()
                        .withDonor(orgs.getWorldBank())
                        .withTypeOfAssistance(categoryValues.getTypeOfAssistanceValues().getGrant())
                        .withFinancingInstrument(categoryValues.getFinancingInstruments().getDebtRelief())
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .withPledge(usaidPledge)
                                .getTransaction())
                        .getFunding())
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(violation(
                orgs.getWorldBank().getOrgGrpId().getAmpOrgGrpId(),
                usaidPledge.getId())));
    }

    @Test
    public void testDonorDoesNotMatchOneViolation() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder()
                        .withDonor(orgs.getWorldBank())
                        .withTypeOfAssistance(categoryValues.getTypeOfAssistanceValues().getGrant())
                        .withFinancingInstrument(categoryValues.getFinancingInstruments().getDebtRelief())
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .withPledge(usaidPledge)
                                .getTransaction())
                        .getFunding())
                .addFunding(new FundingBuilder()
                        .withDonor(orgs.getWorldBank())
                        .withTypeOfAssistance(categoryValues.getTypeOfAssistanceValues().getGrant())
                        .withFinancingInstrument(categoryValues.getFinancingInstruments().getDebtRelief())
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .withPledge(usaidPledge)
                                .getTransaction())
                        .getFunding())
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(violation(
                orgs.getWorldBank().getOrgGrpId().getAmpOrgGrpId(),
                usaidPledge.getId())));
    }

    @Test
    public void testDonorDoesNotMatchTwoViolations() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder()
                        .withDonor(orgs.getWorldBank())
                        .withTypeOfAssistance(categoryValues.getTypeOfAssistanceValues().getGrant())
                        .withFinancingInstrument(categoryValues.getFinancingInstruments().getDebtRelief())
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .withPledge(usaidPledge)
                                .getTransaction())
                        .getFunding())
                .addFunding(new FundingBuilder()
                        .withDonor(orgs.getBelgium())
                        .withTypeOfAssistance(categoryValues.getTypeOfAssistanceValues().getGrant())
                        .withFinancingInstrument(categoryValues.getFinancingInstruments().getDebtRelief())
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .withPledge(usaidPledge)
                                .getTransaction())
                        .getFunding())
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, containsInAnyOrder(
                violation(
                        orgs.getWbGroup().getAmpOrgGrpId(),
                        usaidPledge.getId()),
                violation(
                        orgs.getBelgiumGroup().getAmpOrgGrpId(),
                        usaidPledge.getId())));
    }

    @Test
    public void testDisabledPledges() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder()
                        .withDonor(orgs.getWorldBank())
                        .withTypeOfAssistance(categoryValues.getTypeOfAssistanceValues().getGrant())
                        .withFinancingInstrument(categoryValues.getFinancingInstruments().getDebtRelief())
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .withPledge(usaidPledge)
                                .getTransaction())
                        .getFunding())
                .getActivity();

        Set<String> hiddenFm = ImmutableSet.of(
                "/Activity Form/Funding/Funding Group/Funding Item/Commitments/Commitments Table/Pledges");
        Set<ConstraintViolation> violations = getConstraintViolations(ValidatorUtil.getMetaData(hiddenFm), activity);

        assertThat(violations, emptyIterable());
    }

    private Matcher<ConstraintViolation> violation(Long donorOrgGroupId, Object fundingPledgeId) {
        Matcher attributesMatcher = allOf(
                hasEntry(PledgeOrgValidator.DONOR_ORG_GROUP_ID, donorOrgGroupId),
                hasEntry(PledgeOrgValidator.FUNDING_PLEDGE_ID, fundingPledgeId));

        return ValidatorMatchers.violationFor(PledgeOrgValidator.class,
                "fundings~commitments~pledge",
                attributesMatcher,
                ActivityErrors.FUNDING_PLEDGE_ORG_GROUP_MISMATCH);
    }

    private Set<ConstraintViolation> getConstraintViolations(AmpActivityVersion activity) {
        return getConstraintViolations(activityField, activity);
    }

    private Set<ConstraintViolation> getConstraintViolations(APIField type, AmpActivityVersion activity) {
        Set<ConstraintViolation> violations = ActivityValidatorUtil.validate(type, activity);
        return filter(violations, PledgeOrgValidator.class);
    }
}
