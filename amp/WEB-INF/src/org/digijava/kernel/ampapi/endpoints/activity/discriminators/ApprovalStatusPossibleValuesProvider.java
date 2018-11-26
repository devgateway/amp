package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.aim.dbentity.ApprovalStatus;

public class ApprovalStatusPossibleValuesProvider extends PossibleValuesProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<PossibleValue> values = new ArrayList<>();
        for (ApprovalStatus status : AmpARFilter.activityStatus) {
            values.add(new PossibleValue(status.getId().longValue(), status.getDbName(), ImmutableMap.of()));
        }
        return values;
    }

    @Override
    public Object toJsonOutput(Object object) {
        return object;
    }

    @Override
    public Long getIdOf(Object value) {
        return ((ApprovalStatus) value).getId().longValue();
    }

    @Override
    public Object toAmpFormat(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return ApprovalStatus.fromId((Integer) obj);
        }
        throw new IllegalArgumentException("Unknown approval status: " + obj);
    }
}
