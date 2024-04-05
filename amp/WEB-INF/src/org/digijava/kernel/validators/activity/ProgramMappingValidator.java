package org.digijava.kernel.validators.activity;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.digijava.kernel.ampapi.endpoints.activity.field.APIFieldUtil.readFieldValueOrDefault;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.MAPPING_SOURCE_PROGRAM;
import static org.digijava.module.aim.util.ProgramUtil.*;

/**
 * Ensure that the mapped programs matches if does exist direct program mapping
 *
 * @author Viorel Chihai
 */
public class ProgramMappingValidator implements ConstraintValidator {

    public static final String ATTR_PROGRAM_ID = "programId";

    private static final String PROGRAM_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.Programs.PROGRAM);

    public static final Map<String, String> SETTINGS_NAME_TO_ACTIVITY_FIELD_MAP = new HashMap<String, String>() {{
        put(NATIONAL_PLAN_OBJECTIVE, FieldMap.underscorify(ActivityFieldsConstants.NATIONAL_PLAN_OBJECTIVE));
        put(PRIMARY_PROGRAM, FieldMap.underscorify(ActivityFieldsConstants.PRIMARY_PROGRAMS));
        put(SECONDARY_PROGRAM, FieldMap.underscorify(ActivityFieldsConstants.SECONDARY_PROGRAMS));
        put(TERTIARY_PROGRAM, FieldMap.underscorify(ActivityFieldsConstants.TERTIARY_PROGRAMS));
    }};


    @Override
    public void initialize(Map<String, String> arguments) {

    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        return areDirectProgramMappingsValid(type, value, context);
    }

    private boolean areDirectProgramMappingsValid(APIField type, Object value,
                                                  ConstraintValidatorContext context) {

        List<AmpActivityProgram> dstPrograms = getDestMappingPrograms(type, value);
        if (dstPrograms == null || dstPrograms.isEmpty()) {
            return true;
        }

        List<AmpActivityProgram> srcPrograms = getSourceMappingPrograms(type, value);
        if (srcPrograms == null || srcPrograms.isEmpty()) {
            context.buildConstraintViolation(ValidationErrors.PROGRAM_MAPPING_DOESNT_MATCH)
                    .addPropertyNode(getSourceMappingProgramFieldName())
                    .addConstraintViolation();
            return false;
        }

        boolean valid = true;

        Map<AmpTheme, Set<AmpTheme>> mapping = ProgramUtil.loadProgramMappings();
        List<AmpTheme> srcThemes = srcPrograms.stream()
                .map(AmpActivityProgram::getProgram)
                .collect(Collectors.toList());

        Set<AmpTheme> allowedMappedPrograms = srcThemes.stream()
                .filter(p -> mapping.containsKey(p))
                .map(p -> getProgramsIncludingAncestors(mapping.get(p)))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        for (AmpActivityProgram dstProgram : dstPrograms) {
            if (!allowedMappedPrograms.contains(dstProgram.getProgram())) {
                valid = false;

                context.buildConstraintViolation(ValidationErrors.FIELD_SOURCE_PROGRAM_NOT_SPECIFIED)
                        .addPropertyNode(getSourceMappingProgramFieldName())
                        .addPropertyNode(PROGRAM_FIELD_NAME)
                        .addConstraintViolation();

                context.buildConstraintViolation(ValidationErrors.FIELD_SOURCE_PROGRAM_NOT_SPECIFIED)
                        .addPropertyNode(getDestMappingProgramFieldName())
                        .addPropertyNode(PROGRAM_FIELD_NAME)
                        .addAttribute(ATTR_PROGRAM_ID, dstProgram.getProgram().getAmpThemeId())
                        .addConstraintViolation();
            }
        }

        return valid;
    }

    private List<AmpActivityProgram> getDestMappingPrograms(APIField type, Object value) {
        return getMappingPrograms(type, value, getDestMappingProgramFieldName());
    }

    private List<AmpActivityProgram> getSourceMappingPrograms(APIField type, Object value) {
       return getMappingPrograms(type, value, getSourceMappingProgramFieldName());
    }

    private List<AmpActivityProgram> getMappingPrograms(APIField type, Object value, String fieldName) {
        if (StringUtils.isNotBlank(fieldName)) {
            APIField sourceMappingField = type.getField(fieldName);
            return readFieldValueOrDefault(sourceMappingField, value, null);
        }

        return null;
    }

    private String getSourceMappingProgramFieldName() {
        return getMappingProgramFieldName(MAPPING_SOURCE_PROGRAM);
    }

    private String getDestMappingProgramFieldName() {
        return getMappingProgramFieldName(MAPPING_DESTINATION_PROGRAM);
    }

    private String getMappingProgramFieldName(final String gsName) {
        String mappingProgramFieldName = null;
        String rootProgramId = FeaturesUtil.getGlobalSettingValue(gsName);
        if (StringUtils.isNotBlank(rootProgramId)) {
            AmpTheme rootProgram = ProgramUtil.getThemeById(Long.valueOf(rootProgramId));
            AmpActivityProgramSettings s = rootProgram.getProgramSettings().stream().findAny().orElse(null);
            if (s != null) {
                String settingsName = s.getName();
                mappingProgramFieldName = SETTINGS_NAME_TO_ACTIVITY_FIELD_MAP.get(settingsName);
            }
        }
        return mappingProgramFieldName;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ValidationErrors.FIELD_SOURCE_PROGRAM_NOT_SPECIFIED;
    }

}
