package org.digijava.kernel.ampapi.endpoints.activity.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * Describes a list of fields and allows fast access.
 *
 * @author Octavian Ciubotaru
 */
public class Fields {

    private final List<APIField> list;

    private Map<String, APIField> fieldByName;
    private ListMultimap<String, APIField> fieldsByInternalName;
    private ListMultimap<String, APIField> fieldsByDependency;
    private APIField percentageField;

    public Fields(List<APIField> list) {
        this.list = ImmutableList.copyOf(list);

        fieldByName = new HashMap<>();
        for (APIField field : list) {
            fieldByName.put(field.getFieldName(), field);
            if (field.getPercentage() != null && field.getPercentage()) {
                percentageField = field;
            }
        }

        fieldsByInternalName = getFieldsByInternalName(list);
        fieldsByDependency = getFieldsByDependency(list);
    }

    private ListMultimap<String, APIField> getFieldsByInternalName(List<APIField> list) {
        ImmutableListMultimap.Builder<String, APIField> builder = ImmutableListMultimap.builder();
        for (APIField field : list) {
            if (field.getFieldNameInternal() != null) {
                builder.put(field.getFieldNameInternal(), field);
            }
        }
        return builder.build();
    }

    private ListMultimap<String, APIField> getFieldsByDependency(List<APIField> list) {
        ImmutableListMultimap.Builder<String, APIField> builder = ImmutableListMultimap.builder();
        for (APIField field : list) {
            for (String dep : field.getDependencies()) {
                builder.put(dep, field);
            }
        }
        return builder.build();
    }

    public APIField getField(String fieldName) {
        return fieldByName.get(fieldName);
    }

    public List<APIField> getFieldsForFieldNameInternal(String fieldNameInternal) {
        return fieldsByInternalName.get(fieldNameInternal);
    }

    public List<APIField> getFieldsWithDependency(String dependency) {
        return fieldsByDependency.get(dependency);
    }

    public List<APIField> getList() {
        return list;
    }

    public APIField getPercentageField() {
        return percentageField;
    }
}
