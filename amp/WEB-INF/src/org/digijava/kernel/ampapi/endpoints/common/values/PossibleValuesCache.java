package org.digijava.kernel.ampapi.endpoints.common.values;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;

/**
 * @author Nadejda Mandrescu
 */
public class PossibleValuesCache {
    private Map<String, List<PossibleValue>> cache = new HashMap<>();
    private List<APIField> apiFields;
    
    public PossibleValuesCache() {
    }
    
    public PossibleValuesCache(List<APIField> apiFields) {
        this.apiFields = apiFields;
    }
    
    public List<PossibleValue> getPossibleValues(String fieldPath) {
        if (this.apiFields == null) {
            throw new RuntimeException("No default apiFields configured to call this method");
        }
        return this.getPossibleValues(fieldPath, this.apiFields);
    }
    
    public List<PossibleValue> getPossibleValues(String fieldPath, List<APIField> apiFields) {
        if (!cache.containsKey(fieldPath)) {
            cache.put(fieldPath, PossibleValuesEnumerator.INSTANCE
                    .getPossibleValuesForField(fieldPath, apiFields));
        }
        return cache.get(fieldPath);
    }
}
