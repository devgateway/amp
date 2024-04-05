package org.digijava.module.aim.validator;

import com.google.common.collect.ImmutableSet;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.validator.fundings.FundingOrgRole;
import org.digijava.module.aim.validator.fundings.FundingOrgRoleValidator;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.digijava.module.aim.validator.ConstraintMatchers.hasViolation;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

/**
 * @author Viorel Chihai
 */
public class FundingOrgRoleValidatorTest extends AbstractActivityValidatorTest<FundingOrgRoleValidator> {

    private AmpRole donorRole =
            newRole(1L, Constants.ROLE_CODE_DONOR, "Donor");

    private AmpRole implementingAgencyRole =
            newRole(2L, Constants.ROLE_CODE_IMPLEMENTING_AGENCY, "Implementing Agency");

    private AmpRole executingAgencyRole =
            newRole(3L, Constants.ROLE_CODE_EXECUTING_AGENCY, "Executing Agency");
    
    private AmpOrganisation org1 = newOrganisation(1L, "Org 1");
    private AmpOrganisation org2 = newOrganisation(2L, "Org 2");
    private AmpOrganisation org3 = newOrganisation(3L, "Org 3");
    
    @Test
    public void testNotAppliedInHibernate() {
        AmpActivity activity = new AmpActivity();
        
        Set<ConstraintViolation<AmpActivity>> violations = getValidator().validate(activity);
        
        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullOrgRolesAndFundings() {
        AmpActivity activity = new AmpActivity();

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testEmptyOrgRolesAndFundings() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of());
        activity.setFunding(ImmutableSet.of());

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullOrgRole() {
        AmpActivity activity = new AmpActivity();
        activity.setFunding(ImmutableSet.of(newFunding(org1, implementingAgencyRole)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(fundingOrgRoleViolation()));
    }

    @Test
    public void testEmptyOrgRole() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of());
        activity.setFunding(ImmutableSet.of(newFunding(org1, implementingAgencyRole)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(fundingOrgRoleViolation()));
    }
    
    @Test
    public void testEmptyFunding() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(org1, donorRole)));
        activity.setFunding(ImmutableSet.of());
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, emptyIterable());
    }
    
    @Test
    public void testNullValues() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(null, null)));
        activity.setFunding(ImmutableSet.of(newFunding(null, null)));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidOrgRoles() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(org2, implementingAgencyRole)));
        activity.setFunding(ImmutableSet.of(newFunding(org2, implementingAgencyRole)));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
    
        assertThat(violations, emptyIterable());
    }
    
    @Test
    public void testInvalidOrg() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(org1, implementingAgencyRole)));
        activity.setFunding(ImmutableSet.of(newFunding(org2, implementingAgencyRole)));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, contains(fundingOrgRoleViolation()));
    }
    
    @Test
    public void testInvalidRole() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(org1, donorRole)));
        activity.setFunding(ImmutableSet.of(newFunding(org1, implementingAgencyRole)));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, contains(fundingOrgRoleViolation()));
    }
    
    @Test
    public void testInvalidOrgRoleInFundings() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(
                newOrgRole(org1, donorRole),
                newOrgRole(org1, implementingAgencyRole)));
        
        activity.setFunding(ImmutableSet.of(newFunding(org1, executingAgencyRole)));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, contains(fundingOrgRoleViolation()));
    }
    
    @Test
    public void testMixedInvalidOrgRoleInFundings() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(
                newOrgRole(org1, donorRole),
                newOrgRole(org1, implementingAgencyRole)));
        
        activity.setFunding(ImmutableSet.of(
                newFunding(org2, donorRole),
                newFunding(org1, executingAgencyRole)));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, contains(fundingOrgRoleViolation()));
    }
    
    @Test
    public void testValidOrgRoleInFundings() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(
                newOrgRole(org1, donorRole),
                newOrgRole(org1, implementingAgencyRole),
                newOrgRole(org2, executingAgencyRole),
                newOrgRole(org3, donorRole)));
        
        activity.setFunding(ImmutableSet.of(
                newFunding(org1, donorRole),
                newFunding(org2, executingAgencyRole),
                newFunding(org3, donorRole)));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, emptyIterable());
    }
    
    /**
     * Matcher for an activity funding org role constraint violation.
     */
    private Matcher<ConstraintViolation> fundingOrgRoleViolation() {
        return hasViolation(FundingOrgRole.class);
    }
    
    @Override
    public Set<ConstraintViolation<AmpActivity>> validateForAPI(AmpActivity activity) {
        setActivityInContext(activity);
        return super.validateForAPI(activity);
    }

    private AmpRole newRole(Long id, String code, String name) {
        AmpRole role = new AmpRole();
        role.setAmpRoleId(id);
        role.setRoleCode(code);
        role.setName(name);
        
        return role;
    }
    
    private AmpOrganisation newOrganisation(Long id, String name) {
        AmpOrganisation organisation = new AmpOrganisation();
        organisation.setAmpOrgId(id);
        organisation.setName(name);
        
        return organisation;
    }

    private AmpOrgRole newOrgRole(AmpOrganisation org, AmpRole role) {
        AmpOrgRole orgRole = new AmpOrgRole();
        orgRole.setOrganisation(org);
        orgRole.setRole(role);
        
        return orgRole;
    }
    
    private AmpFunding newFunding(AmpOrganisation org, AmpRole role) {
        AmpFunding funding = new AmpFunding();
        funding.setAmpDonorOrgId(org);
        funding.setSourceRole(role);
        
        return funding;
    }
    
}
