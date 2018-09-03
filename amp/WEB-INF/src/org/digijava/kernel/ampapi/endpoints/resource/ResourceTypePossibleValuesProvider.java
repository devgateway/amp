package org.digijava.kernel.ampapi.endpoints.resource;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;

import com.google.common.collect.ImmutableMap;

public class ResourceTypePossibleValuesProvider extends PossibleValuesProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<PossibleValue> values = new ArrayList<>();
        for (String type : ResourceEPConstants.RESOURCE_TYPES) {
            values.add(new PossibleValue(type, type, ImmutableMap.of()));
        }
        return values;
    }

    @Override
    public Object toJsonOutput(Object object) {
        return object;
    }

    @Override
    public Long getIdOf(Object value) {
        return null;
    }

    @Override
    public Object toAmpFormat(Object obj) {
        return obj;
    }

}
