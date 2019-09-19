package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.DiscriminatedFieldAccessor.unwrapSingleElement;
import static org.digijava.kernel.ampapi.endpoints.activity.field.APIFieldUtil.readFieldValueOrDefault;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * Part 1. Implementation location checks.
 * Implementation location depends on implementation level. Implementation location cannot be specified unless
 * implementation level is specified. When implementation level is specified, then implementation location must
 * match one of the allowed values.
 *
 * Part 2. Location checks.
 * Locations depend on implementation level. Cannot specify locations unless implementation level was specified.
 * Locations can be of different administrative levels as allowed by implementation level.
 *
 * @author Octavian Ciubotaru
 */
public class ImplementationLevelValidator implements ConstraintValidator {

    public static final String ATTR_LOC_ID = "locId";

    public static final String IMPLEMENTATION_LEVEL_PRESENT_KEY = "implementation_level_present";
    public static final String IMPLEMENTATION_LOCATION_PRESENT_KEY = "implementation_location_present";
    public static final String IMPLEMENTATION_LEVEL_VALID_KEY = "implementation_level_valid";
    public static final String IMPLEMENTATION_LOCATION_VALID_KEY = "implementation_location_valid";

    private static final String IMPL_LEVEL_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.IMPLEMENTATION_LEVEL);

    private static final String IMPL_LOC_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.IMPLEMENTATION_LOCATION);

    private static final String LOCATIONS_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.LOCATIONS);

    private static final String LOCATION_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.Locations.LOCATION);

    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        APIField implLevelField = type.getField(IMPL_LEVEL_FIELD_NAME);
        AmpCategoryValue implLevel =
                unwrapSingleElement(readFieldValueOrDefault(implLevelField, value, ImmutableList.of()));

        return isImplLocationValid(type, value, context, implLevel)
                & areLocationsValid(type, value, context, implLevel);
    }

    private boolean areLocationsValid(APIField type, Object value, ConstraintValidatorContext context,
            AmpCategoryValue implLevel) {

        boolean valid = true;

        APIField locsField = type.getField(LOCATIONS_FIELD_NAME);
        Collection<AmpActivityLocation> actLocs = readFieldValueOrDefault(locsField, value, ImmutableList.of());

        if (implLevel == null && !actLocs.isEmpty()) {
            context.buildConstraintViolation(ValidationErrors.IMPLEMENTATION_LEVEL_NOT_SPECIFIED)
                    .addPropertyNode(LOCATIONS_FIELD_NAME)
                    .addConstraintViolation();
            return false;
        }

        for (AmpActivityLocation activityLocation : actLocs) {

            if (activityLocation.getLocation() == null) {
                continue;
            }

            Long implLevelId = implLevel.getId();

            boolean found = activityLocation.getLocation()
                    .getLocation()
                    .getParentCategoryValue()
                    .getUsedValues()
                    .stream()
                    .map(AmpCategoryValue::getId)
                    .anyMatch(implLevelId::equals);

            if (!found) {
                valid = false;

                context.buildConstraintViolation(ValidationErrors.DOESNT_MATCH_IMPLEMENTATION_LEVEL)
                        .addPropertyNode(LOCATIONS_FIELD_NAME)
                        .addPropertyNode(LOCATION_FIELD_NAME)
                        .addAttribute(ATTR_LOC_ID, activityLocation.getLocation().getAmpLocationId())
                        .addConstraintViolation();
            }
        }

        return valid;
    }

    private boolean isImplLocationValid(APIField type, Object value, ConstraintValidatorContext context,
            AmpCategoryValue implLevel) {
        APIField implLocationField = type.getField(IMPL_LOC_FIELD_NAME);
        AmpCategoryValue implLocation =
                unwrapSingleElement(readFieldValueOrDefault(implLocationField, value, ImmutableList.of()));

        if (implLocation != null) {
            if (implLevel != null) {
                Long implLevelId = implLevel.getId();
                boolean found = implLocation.getUsedValues().stream()
                        .map(AmpCategoryValue::getId)
                        .anyMatch(implLevelId::equals);
                if (!found) {
                    context.buildConstraintViolation(ValidationErrors.DOESNT_MATCH_IMPLEMENTATION_LEVEL)
                            .addPropertyNode(IMPL_LOC_FIELD_NAME)
                            .addConstraintViolation();
                }
                return found;
            } else {
                context.buildConstraintViolation(ValidationErrors.IMPLEMENTATION_LEVEL_NOT_SPECIFIED)
                        .addPropertyNode(IMPL_LOC_FIELD_NAME)
                        .addConstraintViolation();
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        throw new UnsupportedOperationException();
    }
}
