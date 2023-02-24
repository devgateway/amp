package org.digijava.kernel.validators.activity;

import static java.util.stream.Collectors.toSet;
import static org.digijava.kernel.ampapi.endpoints.activity.field.APIFieldUtil.readFieldValueOrDefault;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;

/**
 * Location chosen in regional funding must be selected in locations section.
 *
 * @author Octavian Ciubotaru
 */
public class RegionLocationValidator implements ConstraintValidator {

    public static final String ATTR_LOC_IDS = "locIds";

    private static final String REGIONAL_COMMITMENTS_FIELD_NAME =
            FieldMap.underscorify(ArConstants.REGIONAL_COMMITMENTS);

    private static final String REGIONAL_DISBURSEMENTS_FIELD_NAME =
            FieldMap.underscorify(ArConstants.REGIONAL_DISBURSEMENTS);

    private static final String REGIONAL_EXPENDITURES_FIELD_NAME =
            FieldMap.underscorify(ArConstants.REGIONAL_EXPENDITURES);

    private static final String REGIONAL_LOCATION_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.RegionalFunding.LOCATION);

    private static final Set<String> REGIONAL_FUNDING_FIELD_NAMES = ImmutableSet.of(
            REGIONAL_COMMITMENTS_FIELD_NAME,
            REGIONAL_DISBURSEMENTS_FIELD_NAME,
            REGIONAL_EXPENDITURES_FIELD_NAME);

    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        APIField locsField = type.getField(ActivityEPConstants.LOCATIONS_FIELD_NAME);
        Collection<AmpActivityLocation> actLocs = readFieldValueOrDefault(locsField, value, ImmutableList.of());

        Set<AmpCategoryValueLocations> selectedLocations = actLocs.stream()
                .map(AmpActivityLocation::getLocation)
                .filter(Objects::nonNull)
                .collect(toSet());

        boolean valid = true;

        for (String fundingFieldName : REGIONAL_FUNDING_FIELD_NAMES) {
            APIField fundingField = type.getField(fundingFieldName);
            Collection<AmpRegionalFunding> funding = readFieldValueOrDefault(fundingField, value, ImmutableList.of());

            Set<Long> ids = funding.stream()
                    .map(AmpRegionalFunding::getRegionLocation)
                    .filter(l -> l != null && !selectedLocations.contains(l))
                    .map(AmpCategoryValueLocations::getId)
                    .collect(toSet());

            if (!ids.isEmpty()) {
                context.buildConstraintViolation(ValidationErrors.LOCATION_NOT_DECLARED)
                        .addPropertyNode(fundingFieldName)
                        .addPropertyNode(REGIONAL_LOCATION_FIELD_NAME)
                        .addAttribute(ATTR_LOC_IDS, ids)
                        .addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        throw new UnsupportedOperationException();
    }
}
