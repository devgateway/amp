package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import static org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils.getLongOrNullOnError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesDAO;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author Octavian Ciubotaru
 */
public class RegionPossibleValuesProvider extends AbstractPossibleValuesBaseProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<Object[]> possibleLocations = possibleValuesDAO.getPossibleLocations();

        List<PossibleValue> result = new ArrayList<>();

        for (Object[] item : possibleLocations) {
            Long locCatId = getLongOrNullOnError(item[PossibleValuesDAO.LOC_CAT_ID_POS]);
            String locCatName = ((String) item[PossibleValuesDAO.LOC_CAT_NAME_POS]);
            String categoryValueName = ((String) item[PossibleValuesDAO.LOC_CAT_VAL_NAME_POS]);

            if (locCatName == null
                    && categoryValueName == null
                    || !categoryValueName.equals(CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1.getValueKey())) {
                continue;
            }

            Map<String, String> translatedValues = translatorService.translateLabel(locCatName);
            result.add(new PossibleValue(locCatId, locCatName, translatedValues));
        }

        return result;
    }

    @Override
    public boolean isAllowed(Long id) {
        return possibleValuesDAO.isLocationValid(id);
    }

}
