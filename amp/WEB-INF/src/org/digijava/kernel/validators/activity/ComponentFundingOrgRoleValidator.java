package org.digijava.kernel.validators.activity;

import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIFieldUtil;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;

import java.util.*;

import static java.util.Collections.emptyList;

/**
 * @author Octavian Ciubotaru
 */
public class ComponentFundingOrgRoleValidator implements ConstraintValidator {

    public static final String ATTR_ORG_ID = "orgId";

    public static final String ORGANIZATION_PRESENT_KEY = "organization_present";

    private static final String COMPONENT_ORGANIZATION_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.COMPONENT_ORGANIZATION);

    private static final String COMPONENTS_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.COMPONENTS);

    private static final String ORGROLE_INTERNAL_FIELD_NAME = "orgrole";

    private static final String FUNDING_INTERNAL_FIELD_NAME = "fundings";

    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {

        APIField componentsField = type.getField(COMPONENTS_FIELD_NAME);
        if (componentsField == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        Set<Long> declaredOrgIds = new HashSet<>();

        for (APIField orgRoleField : type.getFieldsForFieldNameInternal(ORGROLE_INTERNAL_FIELD_NAME)) {
            List<AmpOrgRole> orgRoles = APIFieldUtil.readFieldValueOrDefault(orgRoleField, value, emptyList());
            orgRoles.forEach(orgRole -> declaredOrgIds.add(orgRole.getOrganisation().getAmpOrgId()));
        }

        Collection<AmpComponent> components = componentsField.getFieldAccessor().get(value);
        List<APIField> fundingFields = componentsField.getFieldsForFieldNameInternal(FUNDING_INTERNAL_FIELD_NAME);

        boolean valid = true;

        for (AmpComponent component : components) {
            for (APIField fundingField : fundingFields) {
                List<AmpComponentFunding> fundingList =
                        APIFieldUtil.readFieldValueOrDefault(fundingField, component, emptyList());
                for (AmpComponentFunding funding : fundingList) {
                    APIField orgField = fundingField.getField(COMPONENT_ORGANIZATION_FIELD_NAME);
                    AmpOrganisation org = APIFieldUtil.readFieldValueOrDefault(orgField, funding, null);
                    if (org != null && !declaredOrgIds.contains(org.getAmpOrgId())) {

                        context.buildConstraintViolation(ValidationErrors.ORGANIZATION_NOT_DECLARED)
                                .addPropertyNode(componentsField.getFieldName())
                                .addPropertyNode(fundingField.getFieldName())
                                .addPropertyNode(orgField.getFieldName())
                                .addAttribute(ATTR_ORG_ID, org.getAmpOrgId())
                                .addConstraintViolation();

                        valid = false;
                    }
                }
            }
        }

        return valid;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ValidationErrors.ORGANIZATION_NOT_DECLARED;
    }
}
