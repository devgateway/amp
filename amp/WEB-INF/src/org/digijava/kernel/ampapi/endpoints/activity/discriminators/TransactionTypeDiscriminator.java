package org.digijava.kernel.ampapi.endpoints.activity.discriminators;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.FieldsDiscriminator;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class TransactionTypeDiscriminator extends FieldsDiscriminator {
	
	@Override
	public Map<String, Object> getPossibleValues() {
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		for (Map.Entry<String, Integer> entry : getTransactionTypeMap().entrySet())
			valuesMap.put(entry.getValue().toString(), entry.getKey());
		return valuesMap;
	}
	
	@Override
	public Object toJsonOutput(Object obj) {
		Integer intValue = (Integer) obj;
		for (Map.Entry<String, Integer> entry : getTransactionTypeMap().entrySet())
			if (entry.getValue().equals(intValue))
				return entry.getKey();
		return null;
	}

    private Map<String, Integer> getTransactionTypeMap() {
        Map<String, Integer> valuesMap = new HashMap<String, Integer>();

        AmpCategoryClass categoryClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.TRANSACTION_TYPE_KEY);
        List<AmpCategoryValue> possibleValues = categoryClass.getPossibleValues();
        if (possibleValues != null) {
            for (AmpCategoryValue transactionType : possibleValues) {
                if (transactionType.isVisible()) {
                    valuesMap.put(transactionType.getValue(), transactionType.getIndex());
                }
            }
        }

        return valuesMap;
    }

	@Override
	public Long getIdOf(Object value) {
		return Long.decode(value.toString());
	}

	@Override
	public Object toAmpFormat(Object obj) {
		return obj;
	}

}
