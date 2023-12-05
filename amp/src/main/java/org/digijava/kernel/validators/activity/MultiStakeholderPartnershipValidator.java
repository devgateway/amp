package org.digijava.kernel.validators.activity;

import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;

import static org.digijava.kernel.ampapi.endpoints.activity.field.APIFieldUtil.readFieldValueOrDefault;

/**
 * Verify that dependent fields specify a value when a project has multi stakeholder partnership set to true
 *
 * @author Viorel Chihai
 */
public class MultiStakeholderPartnershipValidator extends ConditionalRequiredValidator {

    public static final String MULTI_STAKEHOLDER_PARTNERSHIP_KEY = "multi_stakeholder_partnership";

    private static final String MULTI_STAKEHOLDER_PARTNERSHIP_FIELD_NAME = "multi_stakeholder_partnership";

    public MultiStakeholderPartnershipValidator() {
        super(MULTI_STAKEHOLDER_PARTNERSHIP_KEY);
    }

    @Override
    public boolean isActive(APIField type, Object value) {
        APIField multiStakeholderField = type.getField(MULTI_STAKEHOLDER_PARTNERSHIP_FIELD_NAME);

        return Boolean.TRUE.equals(readFieldValueOrDefault(multiStakeholderField, value, false));
    }
}
