package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import java.util.Map;

import javax.validation.ConstraintViolation;

import com.google.common.collect.ImmutableMap;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.validator.fundings.FundingOrgRole;
import org.digijava.module.aim.validator.fundings.TransactionOrgRole;

public class OrgRoleViolationBuilder implements ConstraintViolationBuilder {
    
    private Map<Integer, String> transactionToJsonPath = ImmutableMap.<Integer, String>builder()
            .put(new Integer(Constants.COMMITMENT), FieldMap.underscorify(ArConstants.COMMITMENT))
            .put(new Integer(Constants.DISBURSEMENT), FieldMap.underscorify(ArConstants.DISBURSEMENT))
            .put(new Integer(Constants.ARREARS), FieldMap.underscorify(ArConstants.ARREARS))
            .put(new Integer(Constants.DISBURSEMENT_ORDER), FieldMap.underscorify(ArConstants.DISBURSEMENT_ORDERS))
            .put(new Integer(Constants.ESTIMATED_DONOR_DISBURSEMENT),
                    FieldMap.underscorify(ArConstants.ESTIMATED_DISBURSEMENTS))
            .put(new Integer(Constants.RELEASE_OF_FUNDS), FieldMap.underscorify(ArConstants.RELEASE_OF_FUNDS))
            .put(new Integer(Constants.EXPENDITURE), FieldMap.underscorify(ArConstants.EXPENDITURE))
            .build();
    
    @Override
    public JsonConstraintViolation build(ConstraintViolation v) {
        String jsonPath = FieldMap.underscorify(ActivityFieldsConstants.FUNDINGS);
        
        if (isFundingOrgRoleConstraint(v)) {
            return new JsonConstraintViolation(jsonPath, ValidationErrors.ORGANIZATION_ROLE_PAIR_NOT_DECLARED);
        }
    
        if (isTransactionOrgRoleConstraint(v)) {
            Integer transactionType = getTransactionType(v);
            jsonPath = String.format("%s~%s", jsonPath, transactionToJsonPath.get(transactionType));
            
            if (jsonPath == null) {
                throw new RuntimeException("Cannot find json path for transaction " + transactionType);
            }
        }
        
        return new JsonConstraintViolation(jsonPath, ValidationErrors.ORGANIZATION_ROLE_PAIR_NOT_DECLARED);
    }
    
    private boolean isFundingOrgRoleConstraint(ConstraintViolation violation) {
        return violation.getConstraintDescriptor().getAnnotation() instanceof FundingOrgRole;
    }
    
    private boolean isTransactionOrgRoleConstraint(ConstraintViolation violation) {
        return violation.getConstraintDescriptor().getAnnotation() instanceof TransactionOrgRole;
    }
    
    private Integer getTransactionType(ConstraintViolation v) {
        return (Integer) getThirdNodeKey(v);
    }
}
