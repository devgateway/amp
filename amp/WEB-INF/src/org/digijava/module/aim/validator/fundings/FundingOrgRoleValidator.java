package org.digijava.module.aim.validator.fundings;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.tuple.Pair;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.validator.ActivityValidationContext;
import org.springframework.util.CollectionUtils;

/**
 * @author Viorel Chihai
 */
public class FundingOrgRoleValidator implements ConstraintValidator<FundingOrgRole, Set<AmpFunding>> {
    
    @Override
    public void initialize(FundingOrgRole constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(Set<AmpFunding> fundings, ConstraintValidatorContext context) {
        
        if (CollectionUtils.isEmpty(fundings)) {
            return true;
        }
        
        ActivityValidationContext avc = ActivityValidationContext.getOrThrow();
        AmpActivityFields a = avc.getNewActivity();
        
        Set<Pair<Long, Long>> orgRoles = a.getOrgrole().stream()
                .filter(orgRole -> orgRole.getOrganisation() != null && orgRole.getRole() != null)
                .map(orgRole -> Pair.of(orgRole.getOrganisation().getAmpOrgId(), orgRole.getRole().getAmpRoleId()))
                .collect(Collectors.toSet());
        
        Set<Pair<Long, Long>> orgRolesUsedInFundings = fundings.stream()
                .filter(f -> f.getAmpDonorOrgId() != null && f.getSourceRole() != null)
                .map(f -> Pair.of(f.getAmpDonorOrgId().getAmpOrgId(), f.getSourceRole().getAmpRoleId()))
                .collect(Collectors.toSet());
    
        orgRolesUsedInFundings.removeAll(orgRoles);
        
        return orgRolesUsedInFundings.isEmpty();
    }
    
}
