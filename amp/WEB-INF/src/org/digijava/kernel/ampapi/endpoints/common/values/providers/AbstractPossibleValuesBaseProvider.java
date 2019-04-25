package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesDAO;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;

import com.google.common.collect.ListMultimap;

/**
 * @author Nadejda Mandrescu
 */
public abstract class AbstractPossibleValuesBaseProvider implements PossibleValuesProvider {
    protected PossibleValuesDAO possibleValuesDAO;

    public void setPossibleValuesDAO(PossibleValuesDAO possibleValuesDAO) {
        this.possibleValuesDAO = possibleValuesDAO;
    }

    protected List<PossibleValue> convertToHierarchical(ListMultimap<Long, PossibleValue> groupedValues) {
        return convertToHierarchical(groupedValues.get(null), groupedValues);
    }

    protected List<PossibleValue> convertToHierarchical(List<PossibleValue> flatValues,
            ListMultimap<Long, PossibleValue> groupedValues) {
        List<PossibleValue> hierarchicalValues = new ArrayList<>();
        for (PossibleValue possibleValue : flatValues) {
            Long id = (Long) possibleValue.getId();
            List<PossibleValue> children = convertToHierarchical(groupedValues.get(id), groupedValues);
            hierarchicalValues.add(possibleValue.withChildren(children));
        }
        return hierarchicalValues;
    }

}
