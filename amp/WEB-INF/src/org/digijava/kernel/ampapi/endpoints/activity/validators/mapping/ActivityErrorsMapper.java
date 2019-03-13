package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.validator.percentage.LocationTotalPercentage;
import org.digijava.module.aim.validator.percentage.OrgRoleTotalPercentage;
import org.digijava.module.aim.validator.percentage.ProgramTotalPercentage;
import org.digijava.module.aim.validator.percentage.SectorsTotalPercentage;

/**
 * Map activity errors onto json structure.
 *
 * @author Octavian Ciubotaru
 */
public class ActivityErrorsMapper implements Function<ConstraintViolation, JsonConstraintViolation> {

    private Map<String, String> roleCodeToJsonPath = ImmutableBiMap.<String, String>builder()
            .putAll(ActivityFieldsConstants.ORG_ROLE_CODES)
            .build()
            .inverse();

    private Map<String, String> programToJsonPath = ImmutableMap.<String, String>builder()
            .put(ProgramUtil.PRIMARY_PROGRAM,
                    FieldMap.underscorify(ActivityFieldsConstants.PRIMARY_PROGRAMS))
            .put(ProgramUtil.SECONDARY_PROGRAM,
                    FieldMap.underscorify(ActivityFieldsConstants.SECONDARY_PROGRAMS))
            .put(ProgramUtil.TERTIARY_PROGRAM,
                    FieldMap.underscorify(ActivityFieldsConstants.TERTIARY_PROGRAMS))
            .put(ProgramUtil.NATIONAL_PLAN_OBJECTIVE,
                    FieldMap.underscorify(ActivityFieldsConstants.NATIONAL_PLAN_OBJECTIVE))
            .build();

    private Map<String, String> sectorToJsonPath = ImmutableMap.<String, String>builder()
            .put(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME,
                    FieldMap.underscorify(ActivityFieldsConstants.PRIMARY_SECTORS))
            .put(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME,
                    FieldMap.underscorify(ActivityFieldsConstants.SECONDARY_SECTORS))
            .put(AmpClassificationConfiguration.TERTIARY_CLASSIFICATION_CONFIGURATION_NAME,
                    FieldMap.underscorify(ActivityFieldsConstants.TERTIARY_SECTORS))
            .put(AmpClassificationConfiguration.TAG_CLASSIFICATION_CONFIGURATION_NAME,
                    FieldMap.underscorify(ActivityFieldsConstants.TAG_SECTORS))
            .build();

    @Override
    public JsonConstraintViolation apply(ConstraintViolation v) {
        if (isOrgRolePercentageConstraint(v)) {
            AmpRole role = getAmpRole(v);

            String jsonPath = roleCodeToJsonPath.get(role.getRoleCode());
            if (jsonPath == null) {
                throw new RuntimeException("Cannot find json path for organization role code " + role);
            }

            return new JsonConstraintViolation(jsonPath, ActivityErrors.FIELD_PERCENTAGE_SUM_BAD);
        }

        if (isProgramPercentageConstraint(v)) {
            AmpActivityProgramSettings programSettings = getActivityProgramSettings(v);

            String jsonPath = programToJsonPath.get(programSettings.getName());
            if (jsonPath == null) {
                throw new RuntimeException("Cannot find json path for activity program setting " + programSettings);
            }

            return new JsonConstraintViolation(jsonPath, ActivityErrors.FIELD_PERCENTAGE_SUM_BAD);
        }

        if (isSectorPercentageConstraint(v)) {
            AmpClassificationConfiguration classificationConfiguration = getSectorSettings(v);

            String jsonPath = sectorToJsonPath.get(classificationConfiguration.getName());
            if (jsonPath == null) {
                throw new RuntimeException("Cannot find json path for classification configuration "
                        + classificationConfiguration);
            }

            return new JsonConstraintViolation(jsonPath, ActivityErrors.FIELD_PERCENTAGE_SUM_BAD);
        }

        if (isLocationPercentageConstraint(v)) {
            return new JsonConstraintViolation(FieldMap.underscorify(ActivityFieldsConstants.LOCATIONS),
                    ActivityErrors.FIELD_PERCENTAGE_SUM_BAD);
        }

        throw new RuntimeException("Cannot map constraint violation onto json object. Violation: " + v);
    }

    private AmpRole getAmpRole(ConstraintViolation v) {
        return (AmpRole) getSecondNodeKey(v);
    }

    private AmpActivityProgramSettings getActivityProgramSettings(ConstraintViolation v) {
        return (AmpActivityProgramSettings) getSecondNodeKey(v);
    }

    private AmpClassificationConfiguration getSectorSettings(ConstraintViolation v) {
        return (AmpClassificationConfiguration) getSecondNodeKey(v);
    }

    private Object getSecondNodeKey(ConstraintViolation v) {
        Iterator<Path.Node> iterator = v.getPropertyPath().iterator();
        iterator.next();
        Path.Node percentageNode = iterator.next();
        return percentageNode.getKey();
    }

    private boolean isOrgRolePercentageConstraint(ConstraintViolation violation) {
        return violation.getConstraintDescriptor().getAnnotation() instanceof OrgRoleTotalPercentage;
    }

    private boolean isProgramPercentageConstraint(ConstraintViolation violation) {
        return violation.getConstraintDescriptor().getAnnotation() instanceof ProgramTotalPercentage;
    }

    private boolean isSectorPercentageConstraint(ConstraintViolation violation) {
        return violation.getConstraintDescriptor().getAnnotation() instanceof SectorsTotalPercentage;
    }

    private boolean isLocationPercentageConstraint(ConstraintViolation violation) {
        return violation.getConstraintDescriptor().getAnnotation() instanceof LocationTotalPercentage;
    }
}
