package org.digijava.kernel.ampapi.endpoints.activity.values;

import com.google.common.collect.ImmutableMap;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.aim.dbentity.ApprovalStatus;

import java.util.ArrayList;
import java.util.List;

public class ApprovalStatusPossibleValuesProvider implements PossibleValuesProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<PossibleValue> values = new ArrayList<>();
        for (ApprovalStatus status : AmpARFilter.ACTIVITY_STATUS) {
            values.add(new PossibleValue(status.getId().longValue(), status.getDbName(), ImmutableMap.of()));
        }
        return values;
    }

    @Override
    public boolean isAllowed(Long id) {
        return id != null && ApprovalStatus.isValid(id.intValue());
    }
}
