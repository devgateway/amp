package org.digijava.kernel.ampapi.endpoints.activity.values;

import java.util.ArrayList;
import java.util.List;

import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;

import com.google.common.collect.ImmutableMap;

public class FiscalYearPossibleValuesProvider implements PossibleValuesProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<PossibleValue> values = new ArrayList<>();
        
        for (String year : ActivityUtil.getFiscalYearsRange()) {
            values.add(new PossibleValue(Long.parseLong(year), year, ImmutableMap.of()));
        }
        
        return values;
    }

    @Override
    public boolean isAllowed(Long id) {
        return id != null && ActivityUtil.isFiscalYearInRange(id.intValue());
    }
}
