package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;

/**
 * @author Octavian Ciubotaru
 */
public class ComponentFundingOrgsValidator extends InputValidator {

    public static final String ORGANIZATION = "organization";
    public static final String COMP_FUND_ORGANIZATION = "components~funding~component_organization";

    private Set<Long> orgIds;

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
            Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {

        if (fieldPath.equals(COMP_FUND_ORGANIZATION)) {
            return isValid(importer.getNewJson(), newFieldParent.get(fieldDescription.getFieldName()));
        }

        return true;
    }

    public boolean isValid(JsonBean activityJson, Object value) {
        if (orgIds == null) {
            orgIds = getOrgIds(activityJson);
        }
        Long orgId = getLong(value);
        
        return orgId == null || orgIds.contains(orgId);
    }

    /**
     * Get defined organizations for activity.
     */
    private Set<Long> getOrgIds(JsonBean activity) {
        Set<Long> orgIds = new HashSet<>();

        List<String> orgRoleFields = InterchangeUtils.discriminatedFieldsByFieldTitle.get(ActivityFieldsConstants.ORG_ROLE);

        for (String field : orgRoleFields) {
            Object orgRolesObj = InterchangeUtils.getFieldValuesFromJsonActivity(activity, field);
            if (orgRolesObj != null && orgRolesObj instanceof Collection) {
                Collection<?> orgRolesColl = (Collection<?>) orgRolesObj;
                for (Object orgRoleObj : orgRolesColl) {
                    if (orgRoleObj != null && orgRoleObj instanceof Map) {
                        Map orgRoleMap = (Map) orgRoleObj;
                        Long orgId = getLong(orgRoleMap.get(ORGANIZATION));
                        if (orgId != null) {
                            orgIds.add(orgId);
                        }
                    }
                }
            }
        }

        return orgIds;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.ORGANIZATION_NOT_DECLARED;
    }
}
