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

    public Fields(List<APIField> list) {
        this.list = ImmutableList.copyOf(list);

        fieldByName = new HashMap<>();
        for (APIField field : list) {
            fieldByName.put(field.getFieldName(), field);
        }

        ImmutableListMultimap.Builder<String, APIField> builder = ImmutableListMultimap.builder();
        for (APIField field : list) {
            builder.put(field.getFieldNameInternal(), field);
        }
        fieldsByInternalName = builder.build();
    }

    public APIField getField(String fieldName) {
        return fieldByName.get(fieldName);
    }

    public List<APIField> getFieldsForFieldNameInternal(String fieldNameInternal) {
        return fieldsByInternalName.get(fieldNameInternal);
    }

    public List<APIField> getList() {
        return list;
    }
}
