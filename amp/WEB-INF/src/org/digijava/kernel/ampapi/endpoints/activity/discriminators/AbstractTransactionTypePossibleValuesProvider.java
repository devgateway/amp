package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * @author Octavian Ciubotaru
 */
public class AbstractTransactionTypePossibleValuesProvider extends PossibleValuesProvider {

    private boolean filterByValues;
    private List<String> values;

    public AbstractTransactionTypePossibleValuesProvider() {
        this(false, Collections.emptyList());
    }

    public AbstractTransactionTypePossibleValuesProvider(List<String> values) {
        this(true, values);
    }

    private AbstractTransactionTypePossibleValuesProvider(boolean filterByValues, List<String> values) {
        this.filterByValues = filterByValues;
        this.values = values;
    }

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<PossibleValue> values = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : getTransactionTypeMap().entrySet()) {
            values.add(new PossibleValue(entry.getValue().longValue(), entry.getKey(),
                    translatorService.translateLabel(entry.getKey())));
        }
        return values;
    }

    @Override
    public Object toJsonOutput(Object obj) {
        Integer intValue = (Integer) obj;
        for (Map.Entry<String, Integer> entry : getTransactionTypeMap().entrySet()) {
            if (entry.getValue().equals(intValue)) {
                return entry.getKey();
            }
        }

        return null;
    }

    private Map<String, Integer> getTransactionTypeMap() {
        Map<String, Integer> valuesMap = new HashMap<String, Integer>();

        AmpCategoryClass categoryClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.TRANSACTION_TYPE_KEY);
        List<AmpCategoryValue> possibleValues = categoryClass.getPossibleValues();
        if (possibleValues != null) {
            for (AmpCategoryValue transactionType : possibleValues) {
                // put only those values that are not disabled/deleted in the category manager and are presented in the
                // TRANSACTION_TYPE_NAME_TO_ID map
                if (transactionType.isVisible()
                        && ArConstants.TRANSACTION_TYPE_NAME_TO_ID.get(transactionType.getValue()) != null
                        && isIncluded(transactionType)) {
                    valuesMap.put(transactionType.getValue(), ArConstants.TRANSACTION_TYPE_NAME_TO_ID.get(transactionType.getValue()));
                }
            }
        }

        return valuesMap;
    }

    private boolean isIncluded(AmpCategoryValue categoryValue) {
        return !filterByValues || values.contains(categoryValue.getValue());
    }

    @Override
    public Long getIdOf(Object value) {
        return Long.decode(value.toString());
    }

    @Override
    public Object toAmpFormat(Object obj) {
        return obj;
    }
}
