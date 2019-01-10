package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Nadejda Manndrescu
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIType {

    @JsonProperty(ActivityEPConstants.FIELD_TYPE)
    private String fieldType;

    @JsonIgnore
    private Class<?> type;

    @JsonProperty(ActivityEPConstants.ITEM_TYPE)
    private String itemType;

    /**
     * Meaningful only when fieldType is list.
     */
    @JsonIgnore
    private Class<?> elementType;

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
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
        return this.itemType != null && !this.itemType.equals(ActivityEPConstants.FIELD_TYPE_OBJECT);
    }

}
