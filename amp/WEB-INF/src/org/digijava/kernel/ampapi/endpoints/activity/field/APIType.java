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
    
    /**
     * Meaningful only when fieldType is list.
     */
    @JsonIgnore
    private final Class<?> elementType;
    
    @JsonCreator
    public APIType(@JsonProperty(ActivityEPConstants.FIELD_TYPE) String fieldType) {
        this(null, FieldType.valueOf(fieldType.toUpperCase()),
                FieldType.valueOf(fieldType.toUpperCase()) == FieldType.LIST ? Object.class : null);
    }

    public APIType(Class<?> type) {
        this(type, null, null);
    }

    public APIType(Class<?> type, Class<?> elementType) {
        this(type, null, elementType);
    }

    public APIType(Class<?> type, FieldType fieldType, Class<?> elementType) {
        this.type = type;
        if (fieldType == null) {
            if (InterchangeableClassMapper.containsSimpleClass(type)) {
                fieldType = InterchangeableClassMapper.getCustomMapping(type);
            } else {
                fieldType = FieldType.LIST;
            }
        }
        if (fieldType.isList()) {
            if (elementType == null) {
                throw new RuntimeException("A list type must clarify the elementType"); 
            }
            this.itemType = InterchangeableClassMapper.containsSimpleClass(elementType)
                    ? InterchangeableClassMapper.getCustomMapping(elementType) : FieldType.OBJECT;
             
        } else if (elementType != null) {
            throw new RuntimeException("Only a list type can specify an elementType");
        } else {
            this.itemType = null;
        }
        this.fieldType = fieldType;
        this.elementType = elementType;
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

    public Class<?> getElementType() {
        return elementType;
    }

    @JsonIgnore
    public boolean isSimpleItemType() {
        return this.itemType != null && !this.itemType.isObject();
    }

}
