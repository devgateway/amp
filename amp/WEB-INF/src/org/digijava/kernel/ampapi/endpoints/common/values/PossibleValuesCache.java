package org.digijava.kernel.ampapi.endpoints.common.values;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;

/**
 * @author Nadejda Mandrescu
 */
public class PossibleValuesCache {
    private Map<String, List<PossibleValue>> cache = new HashMap<>();
    private Map<String, Set<Long>> cacheIds = new HashMap<>();
    private List<APIField> apiFields;
    private PossibleValuesEnumerator pvEnumerator;

    public PossibleValuesCache(PossibleValuesEnumerator pvEnumerator) {
        this.pvEnumerator = pvEnumerator;
    }

    public PossibleValuesCache(PossibleValuesEnumerator pvEnumerator, List<APIField> apiFields) {
        this.apiFields = apiFields;
        this.pvEnumerator = pvEnumerator;
    }

    public List<PossibleValue> getPossibleValues(String fieldPath, String commonFieldPath) {
        if (this.apiFields == null) {
            throw new RuntimeException("No default apiFields configured to call this method");
        }
        return this.getPossibleValues(fieldPath, commonFieldPath, this.apiFields);
    }

    public List<PossibleValue> getPossibleValues(String fieldPath, String commonFieldPath, List<APIField> apiFields) {
        commonFieldPath = commonFieldPath == null ? fieldPath : commonFieldPath;
        if (!cache.containsKey(commonFieldPath)) {
            cache.put(commonFieldPath, pvEnumerator.getPossibleValuesForField(fieldPath, apiFields));
        }
        return cache.get(commonFieldPath);
    }

    public boolean hasPossibleValues(String fieldPath, String commonFieldPath) {
        List<PossibleValue> pvs = this.getPossibleValues(fieldPath, commonFieldPath);
        return pvs != null && pvs.size() > 0;
    }

    // TODO it would be nice if possible values could be extended to retrieve one single possible value by id.
    // this will reduce this operation from O(n) to O(log N) or O(1)
    // reason: fields can repeat and may have thousands of possible values
    public boolean isAllowed(Long id, String fieldPath, String commonFieldPath) {
        String actualFieldPath = commonFieldPath == null ? fieldPath : commonFieldPath;
        Set<Long> allowedIds = cacheIds.computeIfAbsent(actualFieldPath,
                key -> collectPossibleValueIds(new HashSet<Long>(), getPossibleValues(fieldPath, commonFieldPath)));
        return allowedIds.contains(id);
    }

    private Set<Long> collectPossibleValueIds(Set<Long> ids, List<PossibleValue> pvs) {
        ids.addAll(pvs.stream().map(pv -> {
            if (pv.getChildren() != null) {
                collectPossibleValueIds(ids, pv.getChildren());
            }
            return pv.getId();
        }).collect(Collectors.toList()));
        return ids;
    }
}
