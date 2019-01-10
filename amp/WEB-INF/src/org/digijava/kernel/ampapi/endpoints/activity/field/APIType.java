package org.digijava.kernel.ampapi.endpoints.activity.field;

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
    private FieldType fieldType;

    @JsonIgnore
    private Class<?> type;

    @JsonProperty(ActivityEPConstants.ITEM_TYPE)
    private FieldType itemType;

    /**
     * Meaningful only when fieldType is list.
     */
    @JsonIgnore
    private Class<?> elementType;

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public FieldType getItemType() {
        return itemType;
    }

    public void setItemType(FieldType itemType) {
        this.itemType = itemType;
    }

    public Class<?> getElementType() {
        return elementType;
    }

    public void setElementType(Class<?> elementType) {
        this.elementType = elementType;
    }

    @JsonIgnore
    public boolean isSimpleItemType() {
        return this.itemType != null && !this.itemType.isObject();
    }

}
