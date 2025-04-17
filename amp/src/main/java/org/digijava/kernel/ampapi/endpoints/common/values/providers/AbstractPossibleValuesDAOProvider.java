package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;

import java.util.List;
import java.util.Map;

/**
 * @author Nadejda Mandrescu
 */
public abstract class AbstractPossibleValuesDAOProvider extends AbstractPossibleValuesBaseProvider
    implements DiscriminatedPossibleValuesProvider {
    protected String discriminatorValue;
    protected boolean isCheckDeleted;

    public AbstractPossibleValuesDAOProvider(String discriminatorValue, boolean isCheckDeleted) {
        this.discriminatorValue = discriminatorValue;
        this.isCheckDeleted = isCheckDeleted;
    }

    protected abstract List<Object[]> getDAOItems();

    protected abstract Object getExtraInfo(Object[] items);

    @Override
    public String getDiscriminatorValue() {
        return this.discriminatorValue;
    }

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<Object[]> items = getDAOItems();
        return setProperties(items, translatorService);
    }

    protected List<PossibleValue> setProperties(List<Object[]> objColList, TranslatorService translatorService) {
        ListMultimap<Long, PossibleValue> groupedValues = ArrayListMultimap.create();
        for (Object[] item : objColList) {
            Long id = ((Number) (item[0])).longValue();
            String value = ((String) (item[1]));
            boolean itemGood = !isCheckDeleted || Boolean.FALSE.equals((Boolean) (item[2]));
            Long parentId = (!isCheckDeleted && item.length > 2) ? (Long) item[2] : null;
            Object extraInfo = getExtraInfo(item);
            if (itemGood) {
                Map<String, String> translatedValues = translatorService.translateLabel(value);
                PossibleValue possibleValue = new PossibleValue(id, value, translatedValues, extraInfo);
                groupedValues.put(parentId, possibleValue);
            }
        }

        return convertToHierarchical(groupedValues);
    }

}
