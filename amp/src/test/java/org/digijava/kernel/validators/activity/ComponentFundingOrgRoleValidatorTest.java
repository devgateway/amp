package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
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
import org.digijava.kernel.persistence.InMemoryOrganisationManager;
import org.digijava.kernel.persistence.InMemoryRoleManager;
import org.digijava.kernel.validation.ConstraintViolation;
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
        AmpActivityVersion activity = new ActivityBuilder().getActivity();

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testOneUndeclaredOrg() {
        InMemoryOrganisationManager organisationManager = InMemoryOrganisationManager.getInstance();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(organisationManager.getWorldBank());

        assertThat(getViolations(activityField, activity), contains(violationFor(COMM_FIELD_PATH, 1L)));
    }

    @Test
    public void testOneUndeclaredOrgInMultipleTransactions() {
        InMemoryOrganisationManager organisationManager = InMemoryOrganisationManager.getInstance();

        AmpActivityVersion activity = new ActivityBuilder()
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(organisationManager.getWorldBank()).addFunding()
                        .buildFunding(Constants.COMMITMENT).withOrg(organisationManager.getWorldBank()).addFunding()
                        .addComponent()
                .getActivity();

        Set<ConstraintViolation> violations = getViolations(activityField, activity);
        assertThat(violations, contains(violationFor(COMM_FIELD_PATH, 1L)));
    }

    @Test
    public void testOneUndeclaredOrgMultiplePaths() {
        InMemoryOrganisationManager organisationManager = InMemoryOrganisationManager.getInstance();

        AmpActivityVersion activity = new ActivityBuilder()
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(organisationManager.getWorldBank()).addFunding()
                        .buildFunding(Constants.DISBURSEMENT).withOrg(organisationManager.getWorldBank()).addFunding()
                .addComponent()
                .getActivity();

        Set<ConstraintViolation> violations = getViolations(activityField, activity);
        assertThat(violations, containsInAnyOrder(
                violationFor(COMM_FIELD_PATH, 1L),
                violationFor(DISB_FIELD_PATH, 1L)));
    }

    @Test
    public void testTwoOrgsNotDeclared() {
        InMemoryOrganisationManager organisationManager = InMemoryOrganisationManager.getInstance();

        AmpActivityVersion activity = new ActivityBuilder()
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(organisationManager.getWorldBank()).addFunding()
                        .buildFunding(Constants.DISBURSEMENT).withOrg(organisationManager.getBelgium()).addFunding()
                        .addComponent()
                .getActivity();

        Set<ConstraintViolation> violations = getViolations(activityField, activity);
        assertThat(violations, containsInAnyOrder(
                violationFor(COMM_FIELD_PATH, 1L),
                violationFor(DISB_FIELD_PATH, 3L)));
    }

    @Test
    public void testDeclaredOrg() {
        InMemoryOrganisationManager organisationManager = InMemoryOrganisationManager.getInstance();
        InMemoryRoleManager roleManager = InMemoryRoleManager.getInstance();

        AmpActivityVersion activity = new ActivityBuilder()
                .addOrgRole(roleManager.getDonorRole(), organisationManager.getWorldBank(), 100f)
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(organisationManager.getWorldBank()).addFunding()
                        .addComponent()
                .getActivity();

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testDisabledComponents() {
        InMemoryOrganisationManager organisationManager = InMemoryOrganisationManager.getInstance();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(organisationManager.getWorldBank());

        APIField activityField = ValidatorUtil.getMetaData(ImmutableSet.of("/Activity Form/Components"));

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testDisabledCommitments() {
        InMemoryOrganisationManager organisationManager = InMemoryOrganisationManager.getInstance();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(organisationManager.getWorldBank());

        APIField activityField = ValidatorUtil.getMetaData(
                ImmutableSet.of("/Activity Form/Components/Component/Components Commitments/Commitment Table"));

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testDisabledComponentOrg() {
        InMemoryOrganisationManager organisationManager = InMemoryOrganisationManager.getInstance();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(organisationManager.getWorldBank());

        APIField activityField = ValidatorUtil.getMetaData(ImmutableSet.of(
                "/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Organization"));

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testAllTransactionTypes() {
        InMemoryOrganisationManager organisationManager = InMemoryOrganisationManager.getInstance();
        InMemoryRoleManager roleManager = InMemoryRoleManager.getInstance();

        AmpActivityVersion activity = new ActivityBuilder()
                .addOrgRole(roleManager.getDonorRole(), organisationManager.getWorldBank(), 100f)
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(organisationManager.getWorldBank()).addFunding()
                        .buildFunding(Constants.DISBURSEMENT).withOrg(organisationManager.getWorldBank()).addFunding()
                        .buildFunding(Constants.EXPENDITURE).withOrg(organisationManager.getWorldBank()).addFunding()
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
        Set<ConstraintViolation> violations = ActivityValidatorUtil.validate(activityField, activity);
        return filter(violations, ComponentFundingOrgRoleValidator.class);
    }
}
