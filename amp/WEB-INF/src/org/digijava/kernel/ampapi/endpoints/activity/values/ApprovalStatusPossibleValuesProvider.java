package org.digijava.kernel.ampapi.endpoints.activity.values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;

public class ApprovalStatusPossibleValuesProvider extends PossibleValuesProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<PossibleValue> values = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : AmpARFilter.activityStatusToNr.entrySet())
            values.add(new PossibleValue(entry.getKey(), entry.getKey(), ImmutableMap.of()));
        return values;
    }
}
