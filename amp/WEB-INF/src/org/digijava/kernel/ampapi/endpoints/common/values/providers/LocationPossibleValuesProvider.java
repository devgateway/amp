package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.digijava.kernel.ampapi.endpoints.activity.LocationExtraInfo;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesDAO;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;

import java.util.List;
import java.util.Map;

import static org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils.getLongOrNullOnError;

/**
 * @author Nadejda Mandrescu
 */
public class LocationPossibleValuesProvider extends AbstractPossibleValuesBaseProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<Object[]> possibleLocations = possibleValuesDAO.getPossibleLocations();
        ListMultimap<Long, PossibleValue> groupedValues = ArrayListMultimap.create();

        for (Object[] item : possibleLocations) {
            // FIXME should be non-null AMP-25735
            Long locCatId = getLongOrNullOnError(item[PossibleValuesDAO.LOC_CAT_ID_POS]);
            String locCatName = ((String) item[PossibleValuesDAO.LOC_CAT_NAME_POS]);
            Long parentLocCatId = getLongOrNullOnError(item[PossibleValuesDAO.LOC_PARENT_CAT_ID_POS]);
            String parentLocCatName = ((String) item[PossibleValuesDAO.LOC_PARENT_CAT_NAME_POS]);
            Long categoryValueId = getLongOrNullOnError(item[PossibleValuesDAO.LOC_CAT_VAL_ID_POS]);
            String categoryValueName = ((String) item[PossibleValuesDAO.LOC_CAT_VAL_NAME_POS]);
            String iso = ((String) item[PossibleValuesDAO.LOC_ISO]);
            Long oldLocId = getLongOrNullOnError(item[PossibleValuesDAO.LOC_OLD_ID]);

            // FIXME remove this filter during AMP-25735
            if (locCatName == null && parentLocCatId == null && parentLocCatName == null
                    && categoryValueId == null && categoryValueName == null) {
                continue;
            }

            LocationExtraInfo extraInfo = new LocationExtraInfo(parentLocCatId, parentLocCatName, categoryValueId,
                    categoryValueName, iso, oldLocId);

            Map<String, String> translatedValues = translatorService.translateLabel(locCatName);
            groupedValues.put(parentLocCatId,
                    new PossibleValue(locCatId, locCatName, translatedValues, extraInfo));
        }
        return convertToHierarchical(groupedValues);
    }

    @Override
    public boolean isAllowed(Long id) {
        return possibleValuesDAO.isLocationValid(id);
    }

}
