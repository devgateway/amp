package org.digijava.kernel.ampapi.endpoints.contact;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * @author Octavian Ciubotaru
 */
public abstract class AmpCategoryPossibleValuesProvider extends PossibleValuesProvider {

    private final String categoryClassKey;

    public AmpCategoryPossibleValuesProvider(String categoryClassKey) {
        this.categoryClassKey = categoryClassKey;
    }

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        return CategoryManagerUtil.getAmpCategoryValueCollectionByKey(categoryClassKey)
                .stream()
                .map(cv -> toPossibleValue(cv, translatorService))
                .collect(toList());
    }

    private PossibleValue toPossibleValue(AmpCategoryValue categoryValue, TranslatorService translatorService) {
        return new PossibleValue(categoryValue.getId(), categoryValue.getValue(),
                translatorService.translateLabel(categoryValue.getValue()));
    }

    @Override
    public Object toJsonOutput(Object value) {
        return value;
    }

    @Override
    public Long getIdOf(Object value) {
        if (value != null && value instanceof AmpCategoryValue) {
            return ((AmpCategoryValue) value).getId();
        }
        
        return null;
    }

    @Override
    public Object toAmpFormat(Object obj) {
        return CategoryManagerUtil.getAmpCategoryValueFromDb(((Number) obj).longValue());
    }
}
