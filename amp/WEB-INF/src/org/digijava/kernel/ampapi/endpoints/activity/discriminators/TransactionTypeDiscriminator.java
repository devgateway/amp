package org.digijava.kernel.ampapi.endpoints.activity.discriminators;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.activity.FieldsDiscriminator;

public class TransactionTypeDiscriminator extends FieldsDiscriminator {
	
//	public TransactionTypeDiscriminator(Object value) {
//		super(value);
//	}




	@Override
	public Map<String, Object> getPossibleValues() {
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		for (Map.Entry<String, Integer> entry : ArConstants.TRANSACTION_TYPE_NAME_TO_ID.entrySet())
			valuesMap.put(entry.getValue().toString(), entry.getKey());
		return valuesMap;
	}
	
	
	
	
	@Override
	public Object toJsonOutput(Object obj) {
		Integer intValue = (Integer) obj;
		for (Map.Entry<String, Integer> entry : ArConstants.TRANSACTION_TYPE_NAME_TO_ID.entrySet())
			if (entry.getValue().equals(intValue))
				return entry.getKey();
		return null;
	}




	@Override
	public Long getIdOf(Object value) {
		return Long.decode(value.toString());
	}




	@Override
	public Object toAmpFormat(Object obj) {
		return obj;
//		return ArConstants.TRANSACTION_TYPE_NAME_TO_ID.get(obj);
	}


}
