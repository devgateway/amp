package org.digijava.kernel.ampapi.endpoints.resource;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;

import java.util.ArrayList;
import java.util.List;

public class ResourceTypePossibleValuesProvider implements PossibleValuesProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<PossibleValue> values = new ArrayList<>();
        for (ResourceType type : ResourceType.values()) {
            values.add(new PossibleValue(type.getId().longValue(), type.getName(), ImmutableMap.of()));
        }
        return values;
    }

    @Override
    public boolean isAllowed(Long id) {
        return ResourceType.isValid(id.intValue());
    }
}
