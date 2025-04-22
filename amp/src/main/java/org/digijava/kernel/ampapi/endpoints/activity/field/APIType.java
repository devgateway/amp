package org.digijava.kernel.ampapi.endpoints.activity.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;

import java.util.Objects;

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
    public APIType(
            @JsonProperty(ActivityEPConstants.FIELD_TYPE) String fieldType,
            @JsonProperty(ActivityEPConstants.ITEM_TYPE) String itemType) {
        this(null, FieldType.valueOf(fieldType.toUpperCase()),
                itemType == null ? null : FieldType.valueOf(itemType.toUpperCase()));
    }

    public APIType(Class<?> type, FieldType fieldType) {
        this(type, fieldType, null);
    }

    public APIType(Class<?> type, FieldType fieldType, FieldType itemType) {
        if (fieldType == FieldType.LIST && (itemType == null || itemType == FieldType.LIST)) {
            throw new IllegalArgumentException("Invalid item type.");
        }
        if (fieldType != FieldType.LIST && itemType != null) {
            throw new IllegalArgumentException("Item type must be null.");
        }
        this.type = type;
        this.fieldType = Objects.requireNonNull(fieldType);
        this.itemType = itemType;
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

    @JsonIgnore
    public boolean isAListOfPrimitives() {
        return fieldType.isList() && itemType.isSimpleType();
    }
}
