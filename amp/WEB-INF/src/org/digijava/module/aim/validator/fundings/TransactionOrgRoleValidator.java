package org.digijava.module.aim.validator.fundings;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.tuple.Pair;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.validator.ActivityValidationContext;
import org.springframework.util.CollectionUtils;

/**
 * @author Viorel Chihai
 */
public class TransactionOrgRoleValidator implements ConstraintValidator<TransactionOrgRole, Set<AmpFundingDetail>> {
    
    @Override
    public void initialize(TransactionOrgRole constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(Set<AmpFundingDetail> transactions, ConstraintValidatorContext context) {
        
        if (CollectionUtils.isEmpty(transactions)) {
            return true;
        }
    
        context.disableDefaultConstraintViolation();
        
        ActivityValidationContext avc = ActivityValidationContext.getOrThrow();
        AmpActivityFields a = avc.getNewActivity();
        
        Set<Pair<Long, Long>> orgRoles = a.getOrgrole().stream()
                .filter(orgRole -> orgRole.getOrganisation() != null && orgRole.getRole() != null)
                .map(orgRole -> Pair.of(orgRole.getOrganisation().getAmpOrgId(), orgRole.getRole().getAmpRoleId()))
                .collect(Collectors.toSet());
        
        boolean valid = true;
        for (AmpFundingDetail fd : transactions) {
            Long orgId = fd.getRecipientOrg() != null ? fd.getRecipientOrg().getAmpOrgId() : null;
            Long roleId = fd.getRecipientRole() != null ? fd.getRecipientRole().getAmpRoleId() : null;
            
            if (orgId != null && roleId != null) {
                Pair<Long, Long> pair = Pair.of(orgId, roleId);
                if (!orgRoles.contains(pair)) {
                    addConstraintViolationForTransaction(context, fd.getTransactionType());
                    valid = false;
                }
            }
        }
        
        return valid;
    }
    
    /**
     * Build and add the constraint violation.
     *
     * @param context     context in which the constraint is evaluated
     * @param transactionType transaction type
     */
    public void addConstraintViolationForTransaction(ConstraintValidatorContext context, Integer transactionType) {
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("transaction")
                .inIterable()
                .atKey(transactionType)
                .addConstraintViolation();
    }
}
