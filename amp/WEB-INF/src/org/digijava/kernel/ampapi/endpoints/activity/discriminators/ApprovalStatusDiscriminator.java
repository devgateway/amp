package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.activity.FieldsDiscriminator;

public class ApprovalStatusDiscriminator extends FieldsDiscriminator {

	@Override
	public Map<String, Object> getPossibleValues() {
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		for (Map.Entry<String, Integer> entry : AmpARFilter.activityStatusToNr.entrySet())
			valuesMap.put(entry.getValue().toString(), entry.getKey());
		return valuesMap;
	}

	@Override
	public Object toJsonOutput(Object obj) {
		return obj;
	}

}
