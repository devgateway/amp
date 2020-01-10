package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import static org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils.getLongOrNullOnError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.LocationExtraInfo;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesDAO;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * @author Nadejda Mandrescu
 */
public class LocationPossibleValuesProvider extends AbstractPossibleValuesBaseProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<Object[]> possibleLocations = possibleValuesDAO.getPossibleLocations();
        ListMultimap<Long, PossibleValue> groupedValues = ArrayListMultimap.create();

        Map<Long, Long> locCatToLocId = new HashMap<>();

        for (Object[] item : possibleLocations) {
            Long locId = ((Number) item[PossibleValuesDAO.LOC_ID_POS]).longValue();

            // FIXME should be non-null AMP-25735
            Long locCatId = getLongOrNullOnError((Number) item[PossibleValuesDAO.LOC_CAT_ID_POS]);

            if (locCatId != null) {
                locCatToLocId.put(locCatId, locId);
            }
        }

        for (Object[] item : possibleLocations) {
            Long locId = ((Number) item[PossibleValuesDAO.LOC_ID_POS]).longValue();
            String locCatName = ((String) item[PossibleValuesDAO.LOC_CAT_NAME_POS]);
            Long parentLocCatId = getLongOrNullOnError((Number) item[PossibleValuesDAO.LOC_PARENT_CAT_ID_POS]);
            String parentLocCatName = ((String) item[PossibleValuesDAO.LOC_PARENT_CAT_NAME_POS]);
            Long categoryValueId = getLongOrNullOnError((Number) item[PossibleValuesDAO.LOC_CAT_VAL_ID_POS]);
            String categoryValueName = ((String) item[PossibleValuesDAO.LOC_CAT_VAL_NAME_POS]);
            String iso = ((String) item[PossibleValuesDAO.LOC_ISO]);

            // FIXME remove this filter during AMP-25735
            if (locCatName == null && parentLocCatId == null && parentLocCatName == null
                    && categoryValueId == null && categoryValueName == null) {
                continue;
            }

            Long parentLocId = locCatToLocId.get(parentLocCatId);

            LocationExtraInfo extraInfo = new LocationExtraInfo(parentLocId, parentLocCatName, categoryValueId,
                    categoryValueName, iso);

            Map<String, String> translatedValues = translatorService.translateLabel(locCatName);
            groupedValues.put(parentLocId, new PossibleValue(locId, locCatName, translatedValues, extraInfo));
        }
        return convertToHierarchical(groupedValues);
    }

    @Override
    public boolean isAllowed(Long id) {
        return possibleValuesDAO.isLocationValid(id);
    }

}
