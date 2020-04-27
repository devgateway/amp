package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author Octavian Ciubotaru
 */
public class ObjectExporter<T> {

    private final List<APIField> apiFields;

    private final TranslatedFieldReader translatedFieldReader;

    public ObjectExporter(TranslatedFieldReader translatedFieldReader, List<APIField> apiFields) {
        this.translatedFieldReader = translatedFieldReader;
        this.apiFields = apiFields;
    }

    public List<APIField> getApiFields() {
        return apiFields;
    }

    public Map<String, Object> export(T object) {
        return getObjectJson(object, apiFields, null);
    }

    /**
     * Convert an object to json object.
     *
     * @param pathToObject the path to the object currently exported
     * @return itemJson object JSON containing the value of the item
     */
    private Map<String, Object> getObjectJson(Object item, List<APIField> apiFields, String pathToObject) {

        Map<String, Object> jsonObject = new LinkedHashMap<>();

        for (APIField field : apiFields) {
            String fieldTitle = field.getFieldName();
            String fieldPath = pathToObject == null ? fieldTitle : pathToObject + "~" + fieldTitle;

            if (isFiltered(fieldPath)) {
                readFieldValue(field, item, jsonObject, fieldPath);
            }
        }

        return jsonObject;
    }

    /**
     * Convert a field of an object to its json representation and set it on jsonObject.
     *
     * @param field the instance of the field
     * @param object the object of the field
     * @param jsonObject result JSON object which will be filled with the values of the fields
     * @param fieldPath the path of the field currently exported
     */
    private void readFieldValue(APIField field, Object object, Map<String, Object> jsonObject, String fieldPath) {
        Object jsonValue;
        Object fieldValue = field.getFieldAccessor().get(object);
        boolean isList = field.getApiType().getFieldType().isList();

        if (field.getApiType().getFieldType().isObject()) {
            if (fieldValue != null && Collection.class.isAssignableFrom(fieldValue.getClass())) {
                Collection<?> col = (Collection<?>) fieldValue;
                if (col.size() > 1) {
                    throw new RuntimeException("Multiple values found for an object field");
                }
                fieldValue = col.size() == 1 ? col.iterator().next() : null;
            }
            jsonValue = (fieldValue == null) ? null : getObjectJson(fieldValue, field.getChildren(), fieldPath);
        } else if (isList) {
            jsonValue = readCollection(field, fieldPath, object, (Collection<?>) fieldValue);
        } else {
            jsonValue = readPrimitive(field, object, fieldValue);
        }

        jsonObject.put(field.getFieldName(), jsonValue);
    }

    /**
     * Read value for a field that has possible values API.
     * <p>When a field is discriminated, then value is list. In this case the value is expected to be a collection
     * with one item.
     * <p>If the value is {@link Identifiable} then it's id is returned.
     */
    private Object readFieldWithPossibleValues(APIField field, Object value) {
        Object singleValue = getSingleValue(value);
        if (ApprovalStatus.class.isAssignableFrom(field.getApiType().getType())) {
            return value == null ? null : ((ApprovalStatus) value).getId();
        } else if (Identifiable.class.isAssignableFrom(field.getApiType().getType())) {
            return singleValue == null ? null : ((Identifiable) singleValue).getIdentifier();
        } else if (InterchangeUtils.isSimpleType(field.getApiType().getType())) {
            return singleValue;
        } else {
            throw new RuntimeException("Invalid field mapping. Must be either of simple type or identifiable. "
                    + "Field: " + field.getFieldName());
        }
    }

    /**
     * <p>When a field is discriminated, then value is list. In this case the value is expected to be a collection
     * with one item.
     */
    private Object getSingleValue(Object value) {
        Object singleValue = null;
        if (value instanceof Collection) {
            Iterator<?> iterator = ((Collection<?>) value).iterator();
            if (iterator.hasNext()) {
                singleValue = iterator.next();
            }
            if (iterator.hasNext()) {
                throw new RuntimeException("Value is a collection with more than one element.");
            }
        } else {
            singleValue = value;
        }
        return singleValue;
    }

    /**
     * Convert primitive value to json value.
     */
    private Object readPrimitive(APIField apiField, Object object, Object fieldValue) {
        if (apiField.isIdOnly()) {
            return readFieldWithPossibleValues(apiField, fieldValue);
        } else if (fieldValue instanceof Date) {
            boolean isTimestamp = apiField.getApiType().getFieldType() == FieldType.TIMESTAMP;
            return DateTimeUtil.formatISO8601DateTimestamp((Date) fieldValue, isTimestamp);
        } else {
            Field field = FieldUtils.getField(object.getClass(), apiField.getFieldNameInternal(), true);
            Class<?> objectClass = object.getClass();
            if (translatedFieldReader.isTranslatable(field, objectClass)) {
                return translatedFieldReader.get(field, objectClass, fieldValue, object);
            } else {
                return fieldValue;
            }
        }
    }

    /**
     * Convert list of objects to a json array.
     */
    private List<Object> readCollection(APIField field, String fieldPath, Object object, Collection<?> value) {
        List<Object> collectionOutput = new ArrayList<>();
        if (value != null) {
            if (field.getApiType().isSimpleItemType()) {
                for (Object item : value) {
                    collectionOutput.add(readPrimitive(field, object, item));
                }
            } else {
                for (Object item : value) {
                    collectionOutput.add(getObjectJson(item, field.getChildren(), fieldPath));
                }
            }
        }
        return collectionOutput;
    }

    protected boolean isFiltered(String fieldPath) {
        return true;
    }
}
