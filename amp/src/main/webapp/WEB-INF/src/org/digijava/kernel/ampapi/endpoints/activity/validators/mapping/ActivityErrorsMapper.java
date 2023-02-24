package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Path;

import com.google.common.collect.ImmutableMap;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.validator.approval.AllowedApprovalStatus;
import org.digijava.module.aim.validator.approval.AllowedApprover;
import org.digijava.module.aim.validator.contact.PrimaryContact;
import org.digijava.module.aim.validator.fundings.TransactionOrgRole;
import org.digijava.module.aim.validator.fundings.FundingOrgRole;
import org.digijava.module.aim.validator.user.MatchExistingCreator;

/**
 * Map activity errors onto json structure.
 *
 * @author Octavian Ciubotaru
 */
public class ActivityErrorsMapper implements Function<ConstraintViolation, JsonConstraintViolation> {

    @PostConstruct
    private void validateConfig() {
        constraintToViolation.forEach((c, v) -> {
            if (!Annotation.class.isAssignableFrom(c)
                    || ConstraintViolationBuilder.class.isAssignableFrom(v)) {
                throw new RuntimeException("Invalid mapping from " + c.getName() + " to " + v.getName());
            }
        });
    }

    private Map<String, String> contactToJsonPath = ImmutableMap.<String, String>builder()
            .put(Constants.DONOR_CONTACT, FieldMap.underscorify(ActivityFieldsConstants.DONOR_CONTACT))
            .put(Constants.MOFED_CONTACT, FieldMap.underscorify(ActivityFieldsConstants.MOFED_CONTACT))
            .put(Constants.PROJECT_COORDINATOR_CONTACT,
                    FieldMap.underscorify(ActivityFieldsConstants.PROJECT_COORDINATOR_CONTACT))
            .put(Constants.SECTOR_MINISTRY_CONTACT,
                    FieldMap.underscorify(ActivityFieldsConstants.SECTOR_MINISTRY_CONTACT))
            .put(Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT,
                    FieldMap.underscorify(ActivityFieldsConstants.IMPL_EXECUTING_AGENCY_CONTACT))
            .build();

    private Map<Class<?>, Class<?>> constraintToViolation = ImmutableMap.<Class<?>, Class<?>>builder()
            // customize if anything extra needed or use some common default below
            // e.g. .put(AllowedApprovalStatus.class, ApprovalStatusViolationBuilder.class)
            .put(FundingOrgRole.class, OrgRoleViolationBuilder.class)
            .put(TransactionOrgRole.class, OrgRoleViolationBuilder.class)
            .build();

    private Map<Class<?>, String> violationsWithGenericInvalidFieldBuilder = ImmutableMap.<Class<?>, String>builder()
            .put(AllowedApprovalStatus.class, FieldMap.underscorify(ActivityFieldsConstants.APPROVAL_STATUS))
            .put(AllowedApprover.class, FieldMap.underscorify(ActivityFieldsConstants.APPROVED_BY))
            .put(MatchExistingCreator.class, FieldMap.underscorify(ActivityFieldsConstants.CREATED_BY))
            .build();

    private ConstraintViolationBuilder getViolationBuilder(ConstraintViolation v) {
        Class<? extends Annotation> aClass = v.getConstraintDescriptor().getAnnotation().annotationType();
        String fieldPath = violationsWithGenericInvalidFieldBuilder.get(aClass);
        if (fieldPath != null) {
            return new InvalidFieldViolationBuilder(fieldPath);
        }
        return null;
    }

    @Override
    public JsonConstraintViolation apply(ConstraintViolation v) {
        if (isPrimaryContactConstraint(v)) {
            String contactType = getContactType(v);
            String jsonPath = contactToJsonPath.get(contactType);
            if (jsonPath == null) {
                throw new RuntimeException("Cannot find json path for activity contact " + contactType);
            }
            
            return new JsonConstraintViolation(jsonPath, ValidationErrors.UNIQUE_PRIMARY_CONTACT);
        }
    
        Class<?> cvbc = constraintToViolation.get(v.getConstraintDescriptor().getAnnotation().annotationType());
        if (cvbc != null) {
            try {
                ConstraintViolationBuilder cvb = ((Class<? extends ConstraintViolationBuilder>) cvbc).newInstance();
                return cvb.build(v);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Could not generate the contraint violation json object");
            }
        }
        ConstraintViolationBuilder cvb = getViolationBuilder(v);
        if (cvb != null) {
            return cvb.build(v);
        }

        throw new RuntimeException("Cannot map constraint violation onto json object. Violation: " + v);
    }

    private String getContactType(ConstraintViolation v) {
        return (String) getSecondNodeKey(v);
    }
    
    private Object getSecondNodeKey(ConstraintViolation v) {
        Iterator<Path.Node> iterator = v.getPropertyPath().iterator();
        iterator.next();
        Path.Node percentageNode = iterator.next();
        return percentageNode.getKey();
    }
    
    private boolean isPrimaryContactConstraint(ConstraintViolation violation) {
        return violation.getConstraintDescriptor().getAnnotation() instanceof PrimaryContact;
    }
}
