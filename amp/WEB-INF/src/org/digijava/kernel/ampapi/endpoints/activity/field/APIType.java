package org.digijava.kernel.ampapi.endpoints.activity.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;

/**
 * @author Nadejda Manndrescu
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIType {

    @JsonProperty(ActivityEPConstants.FIELD_TYPE)
    private final FieldType fieldType;

    @JsonIgnore
    private final Class<?> type;

    @JsonProperty(ActivityEPConstants.ITEM_TYPE)
    private final FieldType itemType;
    
    @JsonCreator
    public APIType(@JsonProperty(ActivityEPConstants.FIELD_TYPE) String fieldType) {
        this(FieldType.valueOf(fieldType.toUpperCase()) == FieldType.LIST ? Object.class : null,
                FieldType.valueOf(fieldType.toUpperCase()));
    }

    public APIType(Class<?> type) {
        this(type, null);
    }

    public APIType(Class<?> type, FieldType fieldType) {
        this.type = type;
        if (fieldType == null) {
            if (InterchangeableClassMapper.containsSimpleClass(type)) {
                fieldType = InterchangeableClassMapper.getCustomMapping(type);
            } else {
                fieldType = FieldType.OBJECT;
            }
        }
        if (fieldType.isList()) {
            this.itemType = InterchangeableClassMapper.containsSimpleClass(type)
                    ? InterchangeableClassMapper.getCustomMapping(type) : FieldType.OBJECT;
             
        } else {
            this.itemType = null;
        }
        this.fieldType = fieldType;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public Class<?> getType() {
        return type;
    }

    public FieldType getItemType() {
        return itemType;
    }

    @JsonIgnore
    public boolean isSimpleItemType() {
        return this.itemType != null && !this.itemType.isObject();
    }

    @JsonIgnore
    public boolean isAnObject() {
        return fieldType.isObject();
    }

    @JsonIgnore
    public boolean isAListOfObjects() {
        return fieldType.isList() && itemType.isObject();
    }
}
