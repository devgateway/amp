package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;

public class ApprovalStatusPossibleValuesProvider extends PossibleValuesProvider {

	@Override
	public List<PossibleValue> getPossibleValues() {
		List<PossibleValue> values = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : AmpARFilter.activityStatusToNr.entrySet())
			values.add(new PossibleValue(entry.getValue().toString(), entry.getKey()));
		return values;
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
