package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.ampapi.endpoints.activity.FieldsDiscriminator;

public class ApprovalStatusDiscriminator extends FieldsDiscriminator {

    @Override
    public Map<Object, Object> getPossibleValues() {
        Map<Object, Object> valuesMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : AmpARFilter.activityStatusToNr.entrySet())
            valuesMap.put(entry.getValue().toString(), entry.getKey());
        return valuesMap;
    }

    @Override
    public Object toJsonOutput( Object object) {
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
