package org.digijava.kernel.ampapi.endpoints.common.values;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.NotImplementedException;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;

/**
 * @author Nadejda Mandrescu
 */
public class PossibleValuesCache {
    private Map<String, List<PossibleValue>> cache = new HashMap<>();
    private Map<String, Set<Long>> cacheIds = new HashMap<>();
    private List<APIField> apiFields;
    private PossibleValuesEnumerator pvEnumerator;
    private PossibleValueSource pvSource;

    public PossibleValuesCache(PossibleValuesEnumerator pvEnumerator, PossibleValueSource pvSource) {
        this (pvEnumerator, null, pvSource);
    }

    public PossibleValuesCache(PossibleValuesEnumerator pvEnumerator, List<APIField> apiFields) {
        this (pvEnumerator, apiFields, PossibleValueSource.POSSIBLE_VALUES_CACHE);
    }

    public PossibleValuesCache(PossibleValuesEnumerator pvEnumerator, List<APIField> apiFields,
            PossibleValueSource pvSource) {
        this.apiFields = apiFields;
        this.pvEnumerator = pvEnumerator;
        this.pvSource = pvSource;
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

    public boolean isAllowed(Long id, String fieldPath, String commonFieldPath) {
        switch (pvSource) {
        case POSSIBLE_VALUES_CACHE:
            return isAllowedUsePossibleValuesCache(id, fieldPath, commonFieldPath);
        case POSSIBLE_VALUES_IDS_CACHE:
            return isAllowedUseIdsCache(id, fieldPath, commonFieldPath);
        case DIRECT_SOURCE:
            return isAllowedUseDirectSource(id, fieldPath);
        default:
            throw new NotImplementedException();
        }
    }

    private boolean isAllowedUsePossibleValuesCache(Long id, String fieldPath, String commonFieldPath) {
        List<PossibleValue> pvs = getPossibleValues(fieldPath, commonFieldPath);
        return findById(pvs, id) != null;
    }

    private PossibleValue findById(List<PossibleValue> possibleValues, Long id) {
        for (PossibleValue possibleValue : possibleValues) {
            if (id.equals(possibleValue.getId())) {
                return possibleValue;
            }
            PossibleValue childPossibleValue = findById(possibleValue.getChildren(), id);
            if (childPossibleValue != null) {
                return childPossibleValue;
            }
        }
        return null;
    }

    private boolean isAllowedUseIdsCache(Long id, String fieldPath, String commonFieldPath) {
        String actualFieldPath = commonFieldPath == null ? fieldPath : commonFieldPath;
        Set<Long> allowedIds = cacheIds.computeIfAbsent(actualFieldPath,
                key -> collectPossibleValueIds(new HashSet<Long>(), getPossibleValues(fieldPath, commonFieldPath)));
        return allowedIds.contains(id);
    }

    private boolean isAllowedUseDirectSource(Long id, String fieldPath) {
        PossibleValuesProvider pvp = pvEnumerator.getPossibleValuesProviderForField(fieldPath, null, apiFields);
        return pvp.isAllowed(id);
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
