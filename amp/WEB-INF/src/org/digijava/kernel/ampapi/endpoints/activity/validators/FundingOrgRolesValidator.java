package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.util.OrganisationUtil;

/**
 * Check if organization roles used in activity.funding and activity.funding.fundingDetails match organization roles
 * defined in activity.orgRoles. {@link org.digijava.module.aim.dbentity.AmpActivityFields}
 *
 * @author Octavian Ciubotaru
 */
public class FundingOrgRolesValidator extends InputValidator {

    private final static String ORG_ROLE_ORG = "organization";

    private final static String FUNDING = "fundings";
    private static final String SRC_ORG = "donor_organization_id";
    private static final String SRC_ROLE = "source_role";

    private final static String FUNDING_DETAILS = "funding_details";
    private static final String DST_ORG = "recipient_organization";
    private static final String DST_ROLE = "recipient_role";

    private final static String FUNDING_PATH = FUNDING;
    private final static String FUNDING_DETAILS_PATH = FUNDING_PATH + "~" + FUNDING_DETAILS;

    private Set<Pair<Long, Long>> orgRoleDefinitions;

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {

        if (fieldPath.equals(FUNDING)) {
            return areOrgRolesValid(importer, newFieldParent, FUNDING, SRC_ORG, SRC_ROLE);
        }

        if (fieldPath.equals(FUNDING_DETAILS_PATH)) {
            return areOrgRolesValid(importer, newFieldParent, FUNDING_DETAILS, DST_ORG, DST_ROLE);
        }

        return true;
    }

    /**
     * Check if all objects from collection use only allowed/defined organization roles.
     */
    private boolean areOrgRolesValid(ObjectImporter importer, Map<String, Object> root, String collectionField,
                                     String orgField, String roleField) {
        if (orgRoleDefinitions == null) {
            JsonBean activity = importer.getNewJson();
            orgRoleDefinitions = getOrgRoleDefinitions(activity);
        }

        Object collObj = root.get(collectionField);
        if (collObj != null && collObj instanceof Collection) {
            Collection coll = (Collection) collObj;
            for (Object obj : coll) {
                if (obj != null && obj instanceof Map) {
                    Map objAsMap = (Map) obj;

                    Pair<Long, Long> srcOrgRole = getOrgRole(objAsMap, orgField, roleField);

                    if (srcOrgRole != null && !orgRoleDefinitions.contains(srcOrgRole)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Get defined organization roles for activity.
     */
    private Set<Pair<Long, Long>> getOrgRoleDefinitions(JsonBean activity) {
        Set<Pair<Long, Long>> orgRoles = new HashSet<>();

        Map<String, Long> roleIdsByCode = getOrgRoleIdsByCode();

        List<String> orgRoleFields = InterchangeUtils.discriminatedFieldsByFieldTitle.get(ActivityFieldsConstants.ORG_ROLE);

        for (String field : orgRoleFields) {
            Object orgRolesObj = InterchangeUtils.getFieldValuesFromJsonActivity(activity, field);
            if (orgRolesObj != null && orgRolesObj instanceof Collection) {
                Collection<?> orgRolesColl = (Collection<?>) orgRolesObj;
                for (Object orgRoleObj : orgRolesColl) {
                    if (orgRoleObj != null && orgRoleObj instanceof Map) {
                        Map orgRoleMap = (Map) orgRoleObj;

                        Long orgId = getLong(orgRoleMap.get(ORG_ROLE_ORG));
                        String roleCode = ActivityFieldsConstants.ORG_ROLE_CODES.get(field);
                        Long roleId = roleIdsByCode.get(roleCode);

                        if (orgId != null) {
                            orgRoles.add(Pair.of(orgId, roleId));
                        }
                    }
                }
            }
        }

        return orgRoles;
    }

    private Map<String, Long> getOrgRoleIdsByCode() {
        Map<String, Long> roles = new HashMap<>();
        for (AmpRole role : OrganisationUtil.getOrgRoles()) {
            roles.put(role.getRoleCode(), role.getAmpRoleId());
        }
        return roles;
    }

    /**
     * Retrieve a pair of ids representing an AmpOrgRole relation.
     * Returns a pair only if both ids are longs and are present, otherwise returns null.
     */
    private Pair<Long, Long> getOrgRole(Map map, String orgField, String roleField) {
        Long org = getLong(map.get(orgField));
        Long role = getLong(map.get(roleField));
        if (org != null && role != null) {
            return Pair.of(org, role);
        } else {
            return null;
        }
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.ORGANIZATION_ROLE_PAIR_NOT_DECLARED;
    }
}
