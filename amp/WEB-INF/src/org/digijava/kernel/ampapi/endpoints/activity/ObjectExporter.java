package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.editor.exception.EditorException;

/**
 * @author Octavian Ciubotaru
 */
public abstract class ObjectExporter<T> {

    private List<APIField> apiFields;

    public ObjectExporter(List<APIField> apiFields) {
        this.apiFields = apiFields;
    }

    public List<APIField> getApiFields() {
        return apiFields;
    }

    public JsonBean export(T object) {
        return getObjectJson(object, apiFields, null);
    }

    /**
     * Convert an object to json object.
     *
     * @param pathToObject the path to the object currently exported
     * @return itemJson object JSON containing the value of the item
     */
    private JsonBean getObjectJson(Object item, List<APIField> apiFields, String pathToObject) {

        JsonBean jsonObject = new JsonBean();

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
    private void readFieldValue(APIField field, Object object, JsonBean jsonObject, String fieldPath) {
        Object jsonValue;
        Object fieldValue = field.getFieldValueReader().get(object);

        if (field.isIdOnly()) {
            jsonValue = readFieldWithPossibleValues(field, fieldValue);
        } else if (field.getFieldType().equals(ActivityEPConstants.FIELD_TYPE_LIST)) {
            if (field.getFieldName().equals("activity_group")) { // FIXME hack because APIField.type cannot be object
                jsonValue = getObjectJson(fieldValue, field.getChildren(), fieldPath);
            } else {
                jsonValue = readCollection(field, fieldPath, (Collection) fieldValue);
            }
        } else {
            jsonValue = readPrimitive(field, object, fieldValue);
        }

        jsonObject.set(field.getFieldName(), jsonValue);
    }

    /**
     * Read value for a field that has possible values API.
     * <p>When a field is discriminated, then value is list. In this case the value is expected to be a collection
     * with one item.
     * <p>If the value is {@link Identifiable} then it's id is returned.
     */
    private Object readFieldWithPossibleValues(APIField field, Object value) {
        Object singleValue = getSingleValue(value);
        if (ApprovalStatus.class.isAssignableFrom(field.getType())) {
            return ((ApprovalStatus) value).getId();
        } else if (Identifiable.class.isAssignableFrom(field.getType())) {
            return singleValue == null ? null : ((Identifiable) singleValue).getIdentifier();
        } else if (InterchangeUtils.isSimpleType(field.getType())) {
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
            Iterator iterator = ((Collection) value).iterator();
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
        try {
            Field field = FieldUtils.getField(object.getClass(), apiField.getFieldNameInternal(), true);

            Class<?> objectClass = object.getClass();

            return InterchangeUtils.getTranslationValues(field, objectClass, fieldValue, object);
        } catch (NoSuchFieldException | IllegalAccessException | EditorException e) {
            throw new RuntimeException("Failed to read primitive field.", e);
        }
    }

    /**
     * Convert list of objects to a json array.
     */
    private List<JsonBean> readCollection(APIField field, String fieldPath, Collection value) {
        List<JsonBean> collectionJson = new ArrayList<>();
        if (value != null) {
            for (Object item : value) {
                collectionJson.add(getObjectJson(item, field.getChildren(), fieldPath));
            }
        }
        return collectionJson;
    }

    protected boolean isFiltered(String fieldPath) {
        return true;
    }
}
