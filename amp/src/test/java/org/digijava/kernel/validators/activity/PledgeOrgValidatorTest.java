package org.digijava.kernel.validators.activity;

import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.activity.builder.FundingBuilder;
import org.dgfoundation.amp.activity.builder.TransactionBuilder;
import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.persistence.InMemoryCategoryValuesManager;
import org.digijava.kernel.persistence.InMemoryOrganisationManager;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Set;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.digijava.kernel.validators.activity.ValidatorMatchers.containsInAnyOrder;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Octavian Ciubotaru
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FeaturesUtil.class})
public class PledgeOrgValidatorTest {

    private static APIField activityField;
    private static FundingPledges usaidPledge;
    private static InMemoryOrganisationManager organisationManager;
    private static InMemoryCategoryValuesManager categoryValues;

    @Before
    public void setUp() {
        TransactionUtil.setUpWorkspaceEmptyPrefixes();
        PowerMockito.mockStatic(FeaturesUtil.class);
        activityField = ValidatorUtil.getMetaData();

        organisationManager = InMemoryOrganisationManager.getInstance();
        categoryValues = InMemoryCategoryValuesManager.getInstance();

        usaidPledge = new FundingPledges();
        usaidPledge.setId(100L);
        usaidPledge.setOrganizationGroup(organisationManager.getUsaidGroup());
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
                        .withDonor(organisationManager.getUsaid())
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
                        .withDonor(organisationManager.getWorldBank())
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
                organisationManager.getWorldBank().getOrgGrpId().getAmpOrgGrpId(),
                usaidPledge.getId())));
    }

    @Test
    public void testDonorDoesNotMatchOneViolation() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder()
                        .withDonor(organisationManager.getWorldBank())
                        .withTypeOfAssistance(categoryValues.getTypeOfAssistanceValues().getGrant())
                        .withFinancingInstrument(categoryValues.getFinancingInstruments().getDebtRelief())
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .withPledge(usaidPledge)
                                .getTransaction())
                        .getFunding())
                .addFunding(new FundingBuilder()
                        .withDonor(organisationManager.getWorldBank())
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
                organisationManager.getWorldBank().getOrgGrpId().getAmpOrgGrpId(),
                usaidPledge.getId())));
    }

    @Test
    public void testDonorDoesNotMatchTwoViolations() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder()
                        .withDonor(organisationManager.getWorldBank())
                        .withTypeOfAssistance(categoryValues.getTypeOfAssistanceValues().getGrant())
                        .withFinancingInstrument(categoryValues.getFinancingInstruments().getDebtRelief())
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .withPledge(usaidPledge)
                                .getTransaction())
                        .getFunding())
                .addFunding(new FundingBuilder()
                        .withDonor(organisationManager.getBelgium())
                        .withTypeOfAssistance(categoryValues.getTypeOfAssistanceValues().getGrant())
                        .withFinancingInstrument(categoryValues.getFinancingInstruments().getDebtRelief())
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .withPledge(usaidPledge)
                                .getTransaction())
                        .getFunding())
                .getActivity();
        mockValidation();
        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, containsInAnyOrder(
                violation(
                        organisationManager.getWbGroup().getAmpOrgGrpId(),
                        usaidPledge.getId()),
                violation(
                        organisationManager.getBelgiumGroup().getAmpOrgGrpId(),
                        usaidPledge.getId())));
    }

    private void mockValidation() {
        when(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM)).thenReturn(null);
    }

    @Test
    public void testDisabledPledges() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder()
                        .withDonor(organisationManager.getWorldBank())
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
