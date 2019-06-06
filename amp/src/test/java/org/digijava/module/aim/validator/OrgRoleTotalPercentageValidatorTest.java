package org.digijava.module.aim.validator;

import static org.digijava.module.aim.validator.ConstraintMatchers.inIterableNode;
import static org.digijava.module.aim.validator.ConstraintMatchers.inIterableNodeAtKey;
import static org.digijava.module.aim.validator.ConstraintMatchers.nodeAtKey;
import static org.digijava.module.aim.validator.ConstraintMatchers.propertyNode;
import static org.digijava.module.aim.validator.ConstraintMatchers.violationWithPath;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.validator.percentage.OrgRoleTotalPercentage;
import org.digijava.module.aim.validator.percentage.OrgRoleTotalPercentageValidator;
import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class OrgRoleTotalPercentageValidatorTest
extends AbstractActivityValidatorTest<OrgRoleTotalPercentageValidator> {

    private AmpRole donorRole =
            newRole(1L, Constants.ROLE_CODE_DONOR, "Donor");

    private AmpRole implementingAgencyRole =
            newRole(2L, Constants.ROLE_CODE_IMPLEMENTING_AGENCY, "Implementing Agency");

    private AmpRole executingAgencyRole =
            newRole(3L, Constants.ROLE_CODE_EXECUTING_AGENCY, "Executing Agency");

    @Test
    public void testNotAppliedInHibernate() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(implementingAgencyRole, 33f)));

        Set<ConstraintViolation<AmpActivity>> violations = getValidator().validate(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullOrgRoles() {
        AmpActivity activity = new AmpActivity();

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testEmptyOrgRoles() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of());

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullPercentage() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(implementingAgencyRole, null)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(orgRolePercentageViolation(implementingAgencyRole)));
    }

    @Test
    public void testNullRole() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(null, 13f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(orgRolePercentageViolation(null)));
    }

    @Test
    public void testValidPercentageForOneNonDonorSingle() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(implementingAgencyRole, 100f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidPercentageForOneNonDonorMultiple() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(
                newOrgRole(implementingAgencyRole, 33.33f),
                newOrgRole(implementingAgencyRole, 33.33f),
                newOrgRole(implementingAgencyRole, 33.34f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testPercentageForDonorIsIgnored() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(
                newOrgRole(donorRole, 33f),
                newOrgRole(donorRole, 33f),
                newOrgRole(donorRole, null)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidPercentageForOneNonDonor() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(implementingAgencyRole, 99f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(orgRolePercentageViolation(implementingAgencyRole)));
    }

    @Test
    public void testInvalidPercentageForManyNonDonors() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(
                newOrgRole(implementingAgencyRole, 99f),
                newOrgRole(executingAgencyRole, 50f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations,
                containsInAnyOrder(
                        orgRolePercentageViolation(implementingAgencyRole),
                        orgRolePercentageViolation(executingAgencyRole)));
    }

    @Test
    public void testMixedPercentages() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(
                newOrgRole(implementingAgencyRole, 100f),
                newOrgRole(executingAgencyRole, 50f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(orgRolePercentageViolation(executingAgencyRole)));
    }

    /**
     * Matcher for an activity org role percentage constraint violation.
     */
    private Matcher<ConstraintViolation> orgRolePercentageViolation(AmpRole implementingAgencyRole) {
        return violationWithPath(OrgRoleTotalPercentage.class,
                ImmutableList.of(propertyNode("orgrole"), inIterableNodeAtKey("percentage", implementingAgencyRole)));
    }

    private AmpRole newRole(Long id, String code, String name) {
        AmpRole role = new AmpRole();
        role.setAmpRoleId(id);
        role.setRoleCode(code);
        role.setName(name);
        return role;
    }

    private AmpOrgRole newOrgRole(AmpRole role, Float percentage) {
        AmpOrgRole orgRole = new AmpOrgRole();
        orgRole.setRole(role);
        orgRole.setPercentage(percentage);
        return orgRole;
    }
}
