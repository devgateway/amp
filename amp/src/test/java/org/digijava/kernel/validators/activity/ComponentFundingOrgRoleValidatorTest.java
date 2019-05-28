package org.digijava.kernel.validators.activity;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.digijava.kernel.ampapi.endpoints.activity.FMService;
import org.digijava.kernel.ampapi.endpoints.activity.TestFMService;
import org.digijava.kernel.ampapi.endpoints.activity.TestFieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.Path;
import org.digijava.kernel.validation.Validator;
import org.digijava.module.aim.dbentity.AmpActivityFields;
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

    private static APIField defaultMetaModel;

    @BeforeClass
    public static void setup() {
        defaultMetaModel = getMetaModel(new TestFMService());
    }

    @Test
    public void testNothingToValidate() {
        AmpActivityVersion activity = new AmpActivityVersion();

        assertThat(getViolations(defaultMetaModel, activity), emptyIterable());
    }

    @Test
    public void testOneUndeclaredOrg() {
        HardcodedOrgs orgs = new HardcodedOrgs();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(orgs.getWorldBank());

        assertThat(getViolations(defaultMetaModel, activity), contains(violationFor(1L)));
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

        Set<ConstraintViolation> violations = getViolations(defaultMetaModel, activity);
        assertThat(violations, contains(violationFor(1L)));
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

        Set<ConstraintViolation> violations = getViolations(defaultMetaModel, activity);
        assertThat(violations, containsInAnyOrder(
                violationFor(1L, path("components~commitments~component_organization")),
                violationFor(1L, path("components~disbursements~component_organization"))));
    }

    private Matcher<Path> path(String path) {
        return hasToString(path);
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

        Set<ConstraintViolation> violations = getViolations(defaultMetaModel, activity);
        assertThat(violations, containsInAnyOrder(
                violationFor(1L),
                violationFor(3L)));
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

        assertThat(getViolations(defaultMetaModel, activity), emptyIterable());
    }

    @Test
    public void testDisabledComponents() {
        HardcodedOrgs orgs = new HardcodedOrgs();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(orgs.getWorldBank());

        TestFMService fmService = new TestFMService(ImmutableSet.of("/Activity Form/Components"));
        APIField activityField = getMetaModel(fmService);

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testDisabledCommitments() {
        HardcodedOrgs orgs = new HardcodedOrgs();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(orgs.getWorldBank());

        TestFMService fmService = new TestFMService(
                ImmutableSet.of("/Activity Form/Components/Component/Components Commitments/Commitment Table"));

        APIField activityField = getMetaModel(fmService);

        assertThat(getViolations(activityField, activity), emptyIterable());
    }

    @Test
    public void testDisabledComponentOrg() {
        HardcodedOrgs orgs = new HardcodedOrgs();
        AmpActivityVersion activity = activityWithOneUndeclaredOrg(orgs.getWorldBank());

        TestFMService fmService = new TestFMService(ImmutableSet.of(
                "/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Organization"));

        APIField activityField = getMetaModel(fmService);

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

        assertThat(getViolations(defaultMetaModel, activity), emptyIterable());
    }

    private AmpActivityVersion activityWithOneUndeclaredOrg(AmpOrganisation org) {
        return new ActivityBuilder()
                .buildComponent()
                        .buildFunding(Constants.COMMITMENT).withOrg(org).addFunding()
                        .addComponent()
                .getActivity();
    }

    /**
     * Matches constraint violations generated by {@link ComponentFundingOrgRoleValidator}. Path is not verified.
     * @param orgId offending organization id
     * @return
     */
    private Matcher<ConstraintViolation> violationFor(Long orgId) {
        return violationFor(orgId, anything());
    }

    /**
     * Matches constraint violations generated by {@link ComponentFundingOrgRoleValidator}.
     * @param orgId offending organization id
     * @param pathMatcher matcher for path
     * @return constraint violation matcher
     */
    private Matcher<ConstraintViolation> violationFor(Long orgId, Matcher pathMatcher) {
        return allOf(
                isA(ConstraintViolation.class),
                hasProperty("constraintDescriptor",
                        hasProperty("constraintValidatorClass", equalTo(ComponentFundingOrgRoleValidator.class))),
                hasProperty("attributes", hasEntry(ComponentFundingOrgRoleValidator.ATTR_ORG_ID, orgId)),
                hasProperty("path", pathMatcher));
    }

    private Set<ConstraintViolation> getViolations(APIField activityField, AmpActivityVersion activity) {
        Validator validator = new Validator();
        return validator.validate(activityField, activity);
    }

    private static APIField getMetaModel(FMService fmService) {
        TestTranslatorService translatorService = new TestTranslatorService();
        TestFieldInfoProvider provider = new TestFieldInfoProvider();

        FieldsEnumerator fieldsEnumerator = new FieldsEnumerator(provider, fmService, translatorService, name -> true);

        return fieldsEnumerator.getMetaModel(AmpActivityFields.class);
    }
}
