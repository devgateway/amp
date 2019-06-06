package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.validators.activity.ValidatorMatchers.containsInAnyOrder;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.Validator;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class ComponentFundingOrgRoleValidatorTest {

    private static final String COMM_FIELD_PATH = "components~commitments~component_organization";
    private static final String DISB_FIELD_PATH = "components~disbursements~component_organization";

    private static APIField activityField;

    @BeforeClass
    public static void setUp() {
        activityField = ValidatorUtil.getMetaData();
    }

    @Test
    public void testNothingToValidate() {
        AmpActivityVersion activity = new AmpActivityVersion();

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testOneUndeclaredOrg() {
        HardcodedOrgs orgs = new HardcodedOrgs();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(orgs.getWorldBank());

        assertThat(getViolations(activityField, activity), contains(violationFor(COMM_FIELD_PATH, 1L)));
    }

    @Test
    public void testOneUndeclaredOrgInMultipleTransactions() {
        HardcodedOrgs orgs = new HardcodedOrgs();

        AmpActivityVersion activity = new ActivityBuilder()
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(orgs.getWorldBank()).addFunding()
                        .buildFunding(Constants.COMMITMENT).withOrg(orgs.getWorldBank()).addFunding()
                        .addComponent()
                .getActivity();

        Set<ConstraintViolation> violations = getViolations(activityField, activity);
        assertThat(violations, contains(violationFor(COMM_FIELD_PATH, 1L)));
    }

    @Test
    public void testOneUndeclaredOrgMultiplePaths() {
        HardcodedOrgs orgs = new HardcodedOrgs();

        AmpActivityVersion activity = new ActivityBuilder()
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(orgs.getWorldBank()).addFunding()
                        .buildFunding(Constants.DISBURSEMENT).withOrg(orgs.getWorldBank()).addFunding()
                .addComponent()
                .getActivity();

        Set<ConstraintViolation> violations = getViolations(activityField, activity);
        assertThat(violations, containsInAnyOrder(
                violationFor(COMM_FIELD_PATH, 1L),
                violationFor(DISB_FIELD_PATH, 1L)));
    }

    @Test
    public void testTwoOrgsNotDeclared() {
        HardcodedOrgs orgs = new HardcodedOrgs();

        AmpActivityVersion activity = new ActivityBuilder()
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(orgs.getWorldBank()).addFunding()
                        .buildFunding(Constants.DISBURSEMENT).withOrg(orgs.getBelgium()).addFunding()
                        .addComponent()
                .getActivity();

        Set<ConstraintViolation> violations = getViolations(activityField, activity);
        assertThat(violations, containsInAnyOrder(
                violationFor(COMM_FIELD_PATH, 1L),
                violationFor(DISB_FIELD_PATH, 3L)));
    }

    @Test
    public void testDeclaredOrg() {
        HardcodedOrgs orgs = new HardcodedOrgs();
        HardcodedRoles roles = new HardcodedRoles();

        AmpActivityVersion activity = new ActivityBuilder()
                .addOrgRole(roles.getDonorRole(), orgs.getWorldBank())
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(orgs.getWorldBank()).addFunding()
                        .addComponent()
                .getActivity();

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testDisabledComponents() {
        HardcodedOrgs orgs = new HardcodedOrgs();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(orgs.getWorldBank());

        APIField activityField = ValidatorUtil.getMetaData(ImmutableSet.of("/Activity Form/Components"));

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testDisabledCommitments() {
        HardcodedOrgs orgs = new HardcodedOrgs();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(orgs.getWorldBank());

        APIField activityField = ValidatorUtil.getMetaData(
                ImmutableSet.of("/Activity Form/Components/Component/Components Commitments/Commitment Table"));

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testDisabledComponentOrg() {
        HardcodedOrgs orgs = new HardcodedOrgs();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(orgs.getWorldBank());

        APIField activityField = ValidatorUtil.getMetaData(ImmutableSet.of(
                "/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Organization"));

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testAllTransactionTypes() {
        HardcodedOrgs orgs = new HardcodedOrgs();
        HardcodedRoles roles = new HardcodedRoles();

        AmpActivityVersion activity = new ActivityBuilder()
                .addOrgRole(roles.getDonorRole(), orgs.getWorldBank())
                .addOrgRole(roles.getDonorRole(), orgs.getUsaid())
                .addOrgRole(roles.getDonorRole(), orgs.getBelgium())
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(orgs.getWorldBank()).addFunding()
                        .buildFunding(Constants.DISBURSEMENT).withOrg(orgs.getUsaid()).addFunding()
                        .buildFunding(Constants.EXPENDITURE).withOrg(orgs.getBelgium()).addFunding()
                        .addComponent()
                .getActivity();

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    private AmpActivityVersion activityWithOneUndeclaredOrg(AmpOrganisation org) {
        return new ActivityBuilder()
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(org).addFunding()
                        .addComponent()
                .getActivity();
    }

    /**
     * Matches constraint violations generated by {@link ComponentFundingOrgRoleValidator}.
     *
     * @param path field path
     * @param orgId offending organization id
     * @return constraint violation matcher
     */
    private Matcher<ConstraintViolation> violationFor(String path, Long orgId) {
        return ValidatorMatchers.violationFor(ComponentFundingOrgRoleValidator.class,
                path,
                hasEntry(ComponentFundingOrgRoleValidator.ATTR_ORG_ID, orgId),
                ActivityErrors.ORGANIZATION_NOT_DECLARED);
    }

    private Set<ConstraintViolation> getViolations(APIField activityField, AmpActivityVersion activity) {
        Validator validator = new Validator();
        return validator.validate(activityField, activity);
    }
}
