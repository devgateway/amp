package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.ampapi.endpoints.activity.utils.AIHelper;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.common.ReflectionUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceType;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.digijava.module.aim.dbentity.ApprovalStatus;

/**
 * @author Octavian Ciubotaru
 */
public class ObjectImporter {

    private static final Logger logger = Logger.getLogger(ObjectImporter.class);

    private final InputValidatorProcessor validator;

    protected Map<Integer, ApiErrorMessage> errors = new HashMap<>();

    protected JsonBean newJson;
    protected TranslationSettings trnSettings;

    private Map<String, List<PossibleValue>> possibleValuesCached = new HashMap<>();

    private List<APIField> apiFields;

    /**
     * This field is used for storing the current json values during field validation
     * E.g: validate the pledge field (present in funding_details)
     * fundings - will contain the json values of the parent of funding_details 
     * fundings~funding_details - will contain the json values of the parent of pledge
     */
    private Map<String, Object> branchJsonVisitor = new HashMap<>();

    private Map<Class<? extends DiscriminationConfigurer>, DiscriminationConfigurer> discriminatorConfigurerCache =
            new HashMap<>();

    private Deque<Object> backReferenceStack = new ArrayDeque<>();

    public ObjectImporter(InputValidatorProcessor validator, List<APIField> apiFields) {
        this(validator, TranslationSettings.getCurrent(), apiFields);
    }

    public ObjectImporter(InputValidatorProcessor validator, TranslationSettings trnSettings,
            List<APIField> apiFields) {
        this.validator = validator;
        this.trnSettings = trnSettings;
        this.apiFields = apiFields;
    }

    public List<APIField> getApiFields() {
        return apiFields;
    }

    /**
     * Clean all importable fields and leave other fields intact.
     */
    protected void cleanImportableFields(List<APIField> fieldDefs, Object obj) {
        if (obj == null) {
            return;
        }

        fieldDefs.stream()
                .filter(APIField::isImportable)
                .forEach(f -> cleanImportableField(f, obj));
    }

    private void cleanImportableField(APIField fieldDef, Object obj) {
        try {
            Field field = ReflectionUtil.getField(obj, fieldDef.getFieldNameInternal());
            if (Collection.class.isAssignableFrom(field.getType())) {
                Collection collection = (Collection) PropertyUtils.getProperty(obj, fieldDef.getFieldNameInternal());
                if (collection != null) {
                    collection.clear();
                }
            } else {
                PropertyUtils.setProperty(obj, fieldDef.getFieldNameInternal(), null);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to clean importable field " + fieldDef, e);
        }
    }

    /**
     * Entrypoint for converting and validation of json structure to internal object model.
     *
     * @param root
     * @param json
     * @return
     */
    public Object validateAndImport(Object root, Map<String, Object> json) {
        return validateAndImport(root, apiFields, json, null);
    }

    /**
     * Recursive method (through ->validateAndImport->validateSubElements->[this method]
     * that attempts to validate the incoming JSON and import its data.
     * If there are any errors -> append them to the validator to propagate upwards
     * @param newParent Matched parent object in which resides the field of the activity we're importing or updating
     * (for example, AmpActivityVersion newActivity is newParent for 'sectors'
     * @param fieldsDef definitions of the fields in this parent (from Fields Enumeration EP)
     * @param newJsonParent parent JSON object in which reside the analyzed fields
     * @param fieldPath the underscorified path to the field currently validated & imported
     * @return currently updated object or null if any validation error occurred
     */
    protected Object validateAndImport(Object newParent, List<APIField> fieldsDef,
            Map<String, Object> newJsonParent, String fieldPath) {
        restoreBackReferences(newParent);
        try {
            backReferenceStack.push(newParent);

            Set<String> fields = new HashSet<String>(newJsonParent.keySet());
            // process all valid definitions
            for (APIField fieldDef : fieldsDef) {
                newParent = validateAndImport(newParent, fieldDef, newJsonParent, fieldPath);
                fields.remove(fieldDef.getFieldName());
            }

            // and error anything remained
            // note: due to AMP-20766, we won't be able to fully detect invalid children
            String fieldPathPrefix = fieldPath == null ? "" : fieldPath + "~";
            if (fields.size() > 0 && !ignoreUnknownFields()) {
                newParent = null;
                for (String invalidField : fields) {
                    // no need to go through deep-first validation flow
                    validator.addError(newJsonParent, invalidField, fieldPathPrefix + invalidField,
                            ActivityErrors.FIELD_INVALID, errors);
                }
            }

            return newParent;
        } finally {
            backReferenceStack.pop();
        }
    }

    private void restoreBackReferences(Object newParent) {
        try {
            Class<?> type = newParent.getClass();
            Field[] backRefFields = FieldUtils.getFieldsWithAnnotation(type, InterchangeableBackReference.class);
            for (Field backRefField : backRefFields) {
                FieldUtils.writeField(backRefField, newParent, backReferenceStack.peek(), true);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to restore back reference.", e);
        }
    }

    protected boolean ignoreUnknownFields() {
        return false;
    }

    /**
     * Validates and imports a single element (and its subelements)
     * @param newParent parent object containing the field
     * @param fieldDef JsonBean holding the description of the field (obtained from the Fields Enumerator EP)
     * @param newJsonParent JSON as imported
     * @param fieldPath underscorified path to the field
     * @return currently updated object or null if any validation error occurred
     */
    private Object validateAndImport(Object newParent, APIField fieldDef,
            Map<String, Object> newJsonParent, String fieldPath) {
        String fieldName = getFieldName(fieldDef, newJsonParent);
        String currentFieldPath = (fieldPath == null ? "" : fieldPath + "~") + fieldName;
        Object newJsonValue = newJsonParent == null ? null : newJsonParent.get(fieldName);

        boolean valid = validator.isValid(this, newJsonParent, fieldDef, currentFieldPath, errors);
        if (valid) {
            newParent = validateSubElements(fieldDef, newParent, newJsonValue, currentFieldPath);
            if (newParent != null) {
                newParent = setNewField(newParent, fieldDef, newJsonParent, currentFieldPath);
            }
        } else {
            newParent = null;
        }
        return newParent;
    }

    /**
     * Configures new value, no validation outside of this method scope, it must be verified before
     */
    private Object setNewField(Object newParent, APIField fieldDef, Map<String, Object> newJsonParent,
            String fieldPath) {
        boolean importable = fieldDef.isImportable();

        // note again: only checks in scope of this method are done here

        String fieldName = fieldDef.getFieldName();
        String actualFieldName = fieldDef.getFieldNameInternal();
        Object fieldValue = newJsonParent.get(fieldName);
        Field objField = ReflectionUtil.getField(newParent, actualFieldName);
        if (objField == null) {
            // cannot set
            logger.error("Actual Field not found: " + actualFieldName + ", fieldPath: " + fieldPath);
            return null;
        }

        if (!importable) {
            // skip reconfiguration at this level if the field is not importable
            return newParent;
        }

        Object newValue = getNewValue(objField, newParent, fieldValue, fieldDef);
        if (newValue != null) {
            try {
                if (newParent instanceof Collection) {
                    ((Collection<Object>) newParent).add(newValue);
                } else {
                    objField.set(newParent, newValue);
                }
            } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return newParent;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object getNewValue(Field field, Object parentObj, Object jsonValue, APIField fieldDef) {
        boolean isCollection = Collection.class.isAssignableFrom(field.getType());
        if (jsonValue == null && !isCollection) {
            return null;
        }

        Object value = null;
        FieldType fieldType = fieldDef.getApiType().getFieldType();
        boolean idOnly = fieldDef.isIdOnly();

        // this field has possible values
        if (!isCollection && idOnly) {
            return getObjectReferencedById(field.getType(), jsonValue);
        }

        // this is a collection
        if (isCollection) {
            try {
                value = field.get(parentObj);
                Collection col = (Collection) value;
                if (col == null) {
                    col = (Collection) getNewInstance(parentObj, field);
                }
                if (idOnly && jsonValue != null && !fieldDef.getApiType().isSimpleItemType()) {
                    Class<?> objectType = AIHelper.getGenericsParameterClass(field);
                    try {
                        Object res = getObjectReferencedById(objectType, jsonValue);
                        col.add(res);
                    } catch (IllegalArgumentException e) {
                        logger.error(e.getMessage());
                        throw new RuntimeException(e);
                    }

                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
            // this is a simple type
        } else if (fieldType.isSimpleType()) {
            try {
                if (Date.class.equals(field.getType())) {
                    value = InterchangeUtils.parseISO8601Date((String) jsonValue);
                } else if (String.class.equals(field.getType())) {
                    // check if this is a translatable that expects multiple entries
                    value = extractString(field, parentObj, jsonValue);
                } else {
                    // a valueOf should work
                    Method valueOf = field.getType().getDeclaredMethod("valueOf", String.class);

                    value = valueOf.invoke(field.getType(), String.valueOf(jsonValue));
                }
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException
                    | InvocationTargetException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            try {
                if (AmpAgreement.class.equals(field.getType())) {
                    value = field.get(parentObj);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        return value;
    }

    public List<PossibleValue> getPossibleValuesForFieldCached(String fieldPath) {
        if (!possibleValuesCached.containsKey(fieldPath)) {
            possibleValuesCached.put(fieldPath, PossibleValuesEnumerator.INSTANCE
                    .getPossibleValuesForField(fieldPath, apiFields));
        }
        return possibleValuesCached.get(fieldPath);
    }

    /**
     * Gets the object identified by an ID, from the Possible Values EP
     * @param objectType
     * @param value
     * @return
     */
    private Object getObjectReferencedById(Class<?> objectType, Object value) {
        if (Collection.class.isAssignableFrom(objectType)) {
            throw new RuntimeException("Can't handle a collection of ID-linked objects yet!");
        }
        if (ApprovalStatus.class.isAssignableFrom(objectType)) {
            return ApprovalStatus.fromId((Integer) value);
        } else if (ResourceType.class.isAssignableFrom(objectType)) {
            return ResourceType.fromId((Integer) value);
        } else if (InterchangeUtils.isSimpleType(objectType)) {
            return ConvertUtils.convert(value, objectType);
        } else {
            return InterchangeUtils.getObjectById(objectType, Long.valueOf(value.toString()));
        }
    }

    /**
     * Generates an instance of the type of the field
     * @param parent
     * @param field
     * @return
     */
    private Object getNewInstance(Object parent, Field field) {
        Object fieldValue;
        try {
            if (SortedSet.class.isAssignableFrom(field.getType())) {
                fieldValue = new TreeSet<>();
            } else if (Set.class.isAssignableFrom(field.getType())) {
                fieldValue = new HashSet<>();
            } else if (List.class.isAssignableFrom(field.getType())) {
                fieldValue = new ArrayList<>();
            } else if (Collection.class.isAssignableFrom(field.getType())) {
                fieldValue = new ArrayList<>();
            } else {
                fieldValue = field.getType().newInstance();
            }
            field.set(parent, fieldValue);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return fieldValue;
    }

    protected String extractString(Field field, Object parentObj, Object jsonValue) {
        return (String) jsonValue;
    }

    /**
     * Obtains the field name
     * @param fieldDef
     * @param newJsonParent
     * @return
     */
    protected String getFieldName(APIField fieldDef, Map<String, Object> newJsonParent) {
        if (fieldDef == null) {
            if (newJsonParent != null && newJsonParent.keySet().size() == 1) {
                return newJsonParent.keySet().iterator().next();
            }
        } else {
            return fieldDef.getFieldName();
        }
        return null;
    }

    /**
     * Validates sub-elements (recursively)
     * @param fieldDef
     * @param newParent
     * @param newJsonValue
     * @param fieldPath
     * @return currently updated object or null if any validation error occurred
     */
    private Object validateSubElements(APIField fieldDef, Object newParent, Object newJsonValue,
            String fieldPath) {
        // simulate temporarily fieldDef
        fieldDef = fieldDef == null ? new APIField() : fieldDef;
        FieldType fieldType = fieldDef.getApiType().getFieldType();
        /*
         * Sub-elements by default are valid when not provided.
         * Current field will be verified below and reported as invalid if sub-elements are mandatory and are
         * not provided.
         */

        // skip children validation immediately if only ID is expected
        boolean idOnly = fieldDef.isIdOnly();
        boolean isList = fieldType.isList();
        if (idOnly && !(isList && fieldDef.getApiType().isSimpleItemType())) {
            return newParent;
        }

        // first validate all sub-elements
        List<APIField> childrenFields = fieldDef.getChildren();
        List<Map<String, Object>> childrenNewValues = getChildrenValues(newJsonValue, isList);

        // validate children, even if it is not a list -> to notify wrong entries
        if ((isList || childrenFields != null && childrenFields.size() > 0) && childrenNewValues != null) {
            String actualFieldName = fieldDef.getFieldNameInternal();
            Field newField = ReflectionUtil.getField(newParent, actualFieldName);
            Object newFieldValue;
            Class<?> subElementClass = fieldDef.getApiType().getElementType();
            boolean isCollection = false;
            try {
                newFieldValue = newField == null ? null : newField.get(newParent);
                if (newParent != null && newFieldValue == null) {
                    newFieldValue = getNewInstance(newParent, newField);
                }
                // AMP-20766: we cannot correctly detect isCollection when current validation already failed
                // (no parent obj ref)
                if (newFieldValue != null && Collection.class.isAssignableFrom(newFieldValue.getClass())) {
                    isCollection = true;
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }

            if (isCollection && fieldDef.getApiType().isSimpleItemType()) {
                Collection nvs = ((Collection<?>) childrenNewValues).stream()
                        .map(v -> toSimpleTypeValue(v, subElementClass)).collect(Collectors.toList());
                ((Collection) newFieldValue).addAll(nvs);
            } else {
                if (newFieldValue != null && AmpAgreement.class.isAssignableFrom(newFieldValue.getClass())
                        && childrenNewValues.size() == 1) {
                    Map<String, Object> agreementMap = childrenNewValues.get(0);
                    childrenNewValues.clear();
                    for (String key : agreementMap.keySet()) {
                        HashMap<String, Object> kv = new HashMap<String, Object>();
                        Object val = agreementMap.get(key);

                        if (val instanceof String) {
                            val = StringUtils.trim((String) val);
                        }

                        kv.put(key, val);
                        childrenNewValues.add(kv);
                    }
                }

                // process children
                Iterator<Map<String, Object>> iterNew = childrenNewValues.iterator();
                while (iterNew.hasNext()) {
                    Map<String, Object> newChild = iterNew.next();
                    branchJsonVisitor.put(fieldPath, newChild);
                    APIField childFieldDef = getMatchedFieldDef(newChild, childrenFields);

                    Object res;
                    if (isCollection) {
                        try {
                            Object newSubElement = subElementClass.newInstance();
                            res = validateAndImport(newSubElement, childrenFields, newChild, fieldPath);
                        } catch (InstantiationException | IllegalAccessException e) {
                            logger.error(e.getMessage());
                            throw new RuntimeException(e);
                        }
                    } else {
                        res = validateAndImport(newFieldValue, childFieldDef, newChild, fieldPath);
                    }

                    if (res == null) {
                        // validation failed, reset parent to stop config
                        newParent = null;
                    } else if (newParent != null && isCollection) {
                        configureDiscriminationField(res, fieldDef);
                        // actual links will be updated
                        ((Collection) newFieldValue).add(res);
                    }
                }
                // TODO: we also need to validate other children, some can be mandatory
            }
        }
        return newParent;
    }

    private Object toSimpleTypeValue(Object value, Class<?> type) {
        if (value == null || type.isAssignableFrom(value.getClass())) {
            return value;
        }
        try {
            Method valueOf = type.getMethod("valueOf", String.class);
            return valueOf.invoke(type, value.toString());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            logger.error("Could not automatically convert the value. The deserializer configuration may be missing.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets items marked under the "children" key in the hierarchical branch of the imported JSON
     * @param jsonValue
     * @param isList
     * @return
     */
    private List<Map<String, Object>> getChildrenValues(Object jsonValue, boolean isList) {
        if (jsonValue != null) {
            if (jsonValue instanceof List) {
                return (List<Map<String, Object>>) jsonValue;
            } else if (isList && jsonValue instanceof Map) {
                List<Map<String, Object>> jsonValues = new ArrayList<Map<String, Object>>();
                jsonValues.add((Map<String, Object>) jsonValue);
                return jsonValues;
            }
        }
        return null;
    }

    private APIField getMatchedFieldDef(Map<String, Object> newValue, List<APIField> fieldDefs) {
        if (fieldDefs != null && fieldDefs.size() > 0) {
            // if we have only 1 child element, then this is a list of elements and only this definition is expected
            // or new value is empty, but we expect something
            if (fieldDefs.size() == 1 || newValue == null || newValue.isEmpty()) {
                return fieldDefs.get(0);
            } else {
                // this is a complex type => simple maps like { field_name : new_value_obj } are expected
                // TODO: if more than 1 value
                String fieldName = newValue.keySet().iterator().next();
                if (StringUtils.isNotBlank(fieldName)) {
                    for (APIField childDef : fieldDefs) {
                        if (fieldName.equals(childDef.getFieldName())) {
                            return childDef;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Used to restore the value of the discrimination field.
     */
    private void configureDiscriminationField(Object obj, APIField fieldDef) {
        if (fieldDef.getDiscriminationConfigurer() != null) {
            DiscriminationConfigurer configurer = discriminatorConfigurerCache.computeIfAbsent(
                    fieldDef.getDiscriminationConfigurer(), this::newConfigurer);
            configurer.configure(obj, fieldDef.getDiscriminatorField(), fieldDef.getDiscriminatorValue());
        }
    }

    private DiscriminationConfigurer newConfigurer(Class<? extends DiscriminationConfigurer> configurer) {
        try {
            return configurer.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to instantiate discriminator configurer " + configurer, e);
        }
    }

    /**
     * @return the newJson
     */
    public JsonBean getNewJson() {
        return newJson;
    }

    /**
     * @return the trnSettings
     */
    public TranslationSettings getTrnSettings() {
        return trnSettings;
    }

    public Map<String, Object> getBranchJsonVisitor() {
        return branchJsonVisitor;
    }
    
}
