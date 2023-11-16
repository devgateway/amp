package org.digijava.module.aim.validator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.validator.fundings.TransactionOrgRole;
import org.digijava.module.aim.validator.fundings.TransactionOrgRoleValidator;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.digijava.module.aim.validator.ConstraintMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Viorel Chihai
 */
public class TransactionOrgRoleValidatorTest extends AbstractActivityValidatorTest<TransactionOrgRoleValidator> {

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
    public void testNullValues() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(null, null)));
    
        AmpFunding funding = newFunding(null, null);
        funding.setFundingDetails(ImmutableSet.of(newTransaction(Constants.COMMITMENT, null, null)));
    
        activity.setFunding(ImmutableSet.of(funding));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, emptyIterable());
    }
    
    
    @Test
    public void testNullOrgRole() {
        AmpActivity activity = new AmpActivity();
        activity.setFunding(ImmutableSet.of(newFunding(org1, implementingAgencyRole)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
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
    public void testEmptyOrgRole() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of());
        
        AmpFunding funding = newFunding(org1, implementingAgencyRole);
        funding.setFundingDetails(ImmutableSet.of(newTransaction(Constants.COMMITMENT, org2, implementingAgencyRole)));
        
        activity.setFunding(ImmutableSet.of(funding));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
    
        assertThat(violations, contains(transactionOrgRoleViolation(Integer.valueOf(Constants.COMMITMENT))));
    }
    
    @Test
    public void testInvalidOrgRole() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(org1, implementingAgencyRole)));
        
        AmpFunding funding = newFunding(org1, implementingAgencyRole);
        funding.setFundingDetails(ImmutableSet.of(newTransaction(Constants.DISBURSEMENT, org2, implementingAgencyRole)));
        
        activity.setFunding(ImmutableSet.of(funding));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, contains(transactionOrgRoleViolation(Integer.valueOf(Constants.DISBURSEMENT))));
    }
    
    @Test
    public void testValidOrgRole() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(newOrgRole(org1, donorRole)));
        
        AmpFunding funding = newFunding(org1, implementingAgencyRole);
        funding.setFundingDetails(ImmutableSet.of(newTransaction(Constants.DISBURSEMENT, org1, donorRole)));
        
        activity.setFunding(ImmutableSet.of(funding));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, emptyIterable());
    }
    
    @Test
    public void testInvalidMultipleTransactions() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(
                newOrgRole(org1, implementingAgencyRole),
                newOrgRole(org2, executingAgencyRole)));
        
        AmpFunding funding1 = newFunding(org1, implementingAgencyRole);
        funding1.setFundingDetails(ImmutableSet.of(newTransaction(Constants.COMMITMENT, org1, donorRole)));
    
        AmpFunding funding2 = newFunding(org2, executingAgencyRole);
        funding2.setFundingDetails(ImmutableSet.of(newTransaction(Constants.DISBURSEMENT, org2, implementingAgencyRole)));
        
        activity.setFunding(ImmutableSet.of(funding1, funding2));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, containsInAnyOrder(transactionOrgRoleViolation(Integer.valueOf(Constants.COMMITMENT)),
                transactionOrgRoleViolation(Integer.valueOf(Constants.DISBURSEMENT))));
    }
    
    @Test
    public void testValidMultipleOrgRole() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(
                newOrgRole(org1, donorRole),
                newOrgRole(org2, executingAgencyRole)));
        
        AmpFunding funding1 = newFunding(org1, implementingAgencyRole);
        funding1.setFundingDetails(ImmutableSet.of(newTransaction(Constants.DISBURSEMENT, org1, donorRole)));
        
        AmpFunding funding2 = newFunding(org2, executingAgencyRole);
        funding2.setFundingDetails(ImmutableSet.of(newTransaction(Constants.DISBURSEMENT, org2, executingAgencyRole)));
        
        activity.setFunding(ImmutableSet.of(funding1, funding2));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, emptyIterable());
    }
    
    @Test
    public void testValidMultipleOrgRoleNull() {
        AmpActivity activity = new AmpActivity();
        activity.setOrgrole(ImmutableSet.of(
                newOrgRole(org1, donorRole),
                newOrgRole(org3, executingAgencyRole)));
        
        AmpFunding funding1 = newFunding(org1, implementingAgencyRole);
        funding1.setFundingDetails(ImmutableSet.of(newTransaction(Constants.DISBURSEMENT, org1, donorRole)));
        
        AmpFunding funding2 = newFunding(org2, executingAgencyRole);
        funding2.setFundingDetails(ImmutableSet.of(newTransaction(Constants.DISBURSEMENT, null, null)));
        
        activity.setFunding(ImmutableSet.of(funding1, funding2));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, emptyIterable());
    }
    
    /**
     * Matcher for an activity transaction recipient org role constraint violation.
     */
    private Matcher<ConstraintViolation> transactionOrgRoleViolation(Integer transactionType) {
        return violationWithPath(TransactionOrgRole.class,
                ImmutableList.of(propertyNode("funding"), inIterableNode("fundingDetails"),
                        inIterableNodeAtKey("transaction", transactionType)));
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
    
    private AmpFundingDetail newTransaction(int transactionType, AmpOrganisation org, AmpRole role) {
        AmpFundingDetail transaction = new AmpFundingDetail();
        transaction.setTransactionType(transactionType);
        transaction.setRecipientOrg(org);
        transaction.setRecipientRole(role);
        
        return transaction;
    }
    
}
