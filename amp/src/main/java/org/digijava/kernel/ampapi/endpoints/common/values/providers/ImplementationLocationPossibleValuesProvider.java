package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import org.digijava.kernel.ampapi.endpoints.activity.ImplementationLocationExtraInfo;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author Nadejda Mandrescu
 */
public class ImplementationLocationPossibleValuesProvider implements PossibleValuesProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        String key = CategoryConstants.IMPLEMENTATION_LOCATION_KEY;
        List<AmpCategoryValue> collectionByKey = CategoryManagerUtil.getAllAcceptableValuesForACVClass(key, null);
        return collectionByKey.stream()
                .filter(AmpCategoryValue::isVisible)
                .map(locCategory -> getImplementationLocationValue(locCategory, translatorService))
                .collect(toList());
    }

    private PossibleValue getImplementationLocationValue(AmpCategoryValue locCategory,
            TranslatorService translatorService) {
        Long id = locCategory.getId();
        String value = locCategory.getValue();
        Map<String, String> translatedValues = translatorService.translateLabel(value);

        List<Long> implementationLevels = locCategory.getUsedValues().stream()
                .map(AmpCategoryValue::getId)
                .collect(toList());
        ImplementationLocationExtraInfo extraInfo = new ImplementationLocationExtraInfo(locCategory.getIndex(),
                implementationLevels);

        return new PossibleValue(id, value, translatedValues, extraInfo);
    }

    @Override
    public boolean isAllowed(Long id) {
        return CategoryManagerUtil.isExitingAmpCategoryValue(CategoryConstants.IMPLEMENTATION_LOCATION_KEY, id, true);
    }

}
