package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ErrorDecorator;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.activity.validators.mapping.DefaultErrorsMapper;
import org.digijava.kernel.ampapi.endpoints.activity.validators.mapping.JsonConstraintViolation;
import org.digijava.kernel.ampapi.endpoints.activity.validators.mapping.JsonErrorIntegrator;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.common.values.BadInput;
import org.digijava.kernel.ampapi.endpoints.common.values.PossibleValuesCache;
import org.digijava.kernel.ampapi.endpoints.common.values.ValueConverter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.digijava.module.aim.validator.groups.API;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author Octavian Ciubotaru
 */
public class ObjectImporter {

    private static final Logger logger = Logger.getLogger(ObjectImporter.class);

    private final InputValidatorProcessor formatValidator;
    private final InputValidatorProcessor businessRulesValidator;

    protected Map<Integer, ApiErrorMessage> errors = new HashMap<>();
    protected Map<Integer, ApiErrorMessage> warnings = new HashMap<>();
    protected ValueConverter valueConverter;

    protected Map<String, Object> newJson;
    protected TranslationSettings trnSettings;

    private List<APIField> apiFields;

    private PossibleValuesCache possibleValuesCached;

    /**
     * This field is used for storing the current json values during field validation
     * E.g: validate the pledge field (present in funding_details)
     * fundings - will contain the json values of the parent of funding_details
     * fundings~funding_details - will contain the json values of the parent of pledge
     */
    private Map<String, Object> branchJsonVisitor = new HashMap<>();

    private Deque<Object> backReferenceStack = new ArrayDeque<>();

    private Validator beanValidator;

    private Function<ConstraintViolation, JsonConstraintViolation> jsonErrorMapper = new DefaultErrorsMapper();

    public ObjectImporter(InputValidatorProcessor formatValidator, InputValidatorProcessor businessRulesValidator,
            List<APIField> apiFields) {
        this(formatValidator, businessRulesValidator, TranslationSettings.getCurrent(), apiFields,
                new ValueConverter());
    }

    public ObjectImporter(InputValidatorProcessor formatValidator, InputValidatorProcessor businessRulesValidator,
            TranslationSettings trnSettings, List<APIField> apiFields, ValueConverter valueConverter) {
        this.formatValidator = formatValidator;
        this.businessRulesValidator = businessRulesValidator;
        this.trnSettings = trnSettings;
        this.apiFields = apiFields;
        this.possibleValuesCached = new PossibleValuesCache(PossibleValuesEnumerator.INSTANCE, apiFields);
        this.valueConverter = valueConverter;

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        beanValidator = validatorFactory.getValidator();
    }

    public void setJsonErrorMapper(Function<ConstraintViolation, JsonConstraintViolation> jsonErrorMapper) {
        this.jsonErrorMapper = jsonErrorMapper;
    }

    public List<APIField> getApiFields() {
        return apiFields;
    }

    protected void beforeViolationsCheck() {
    }

    /**
     * Entrypoint for converting and validation of json structure to internal object model.
     *
     * @param root
     * @param json
     * @return
     */
    public boolean validateAndImport(Object root, Map<String, Object> json) {
        return validateAndImport(root, json, false);
    }

    /**
     * This method is used to bypass violations and other configurations.
     * TODO to be updated during refactoring for Activity Importer unit tests
     * @param root
     * @param json
     * @param validateFormatOnly set it to true for test only
     * @return
     */
    public boolean validateAndImport(Object root, Map<String, Object> json, boolean validateFormatOnly) {
        boolean isFormatValid = validateAndImport(root, apiFields, json, null);
        if (isFormatValid && !validateFormatOnly) {
            beforeViolationsCheck();
            processViolationsForTypes(json, root);
        }
        return isFormatValid;
    }

    private void processViolationsForTypes(Map<String, Object> json, Object obj) {
        Set<ConstraintViolation<Object>> violations = beanValidator.validate(obj, API.class, Default.class);
        JsonErrorIntegrator jsonErrorIntegrator = new JsonErrorIntegrator(jsonErrorMapper);
        jsonErrorIntegrator.mapTypeErrors(json, violations, errors);
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
     * @return true if valid format. Check for all errors to find also business validation issues
     */
    private boolean validateAndImport(Object newParent, List<APIField> fieldsDef,
            Map<String, Object> newJsonParent, String fieldPath) {
        boolean isFormatValid = true;
        restoreBackReferences(newParent);
        try {
            backReferenceStack.push(newParent);

            Set<String> fields = new HashSet<String>(newJsonParent.keySet());
            // process all valid definitions
            for (APIField fieldDef : fieldsDef) {
                isFormatValid = validateAndImport(newParent, fieldDef, newJsonParent, fieldPath) && isFormatValid;
                fields.remove(fieldDef.getFieldName());
            }

            // and warn anything remained
            // note: due to AMP-20766, we won't be able to fully detect invalid children
            String fieldPathPrefix = fieldPath == null ? "" : fieldPath + "~";
            if (fields.size() > 0) {
                for (String invalidField : fields) {
                    // no need to go through deep-first validation flow
                    ErrorDecorator.addError(newJsonParent, invalidField, fieldPathPrefix + invalidField,
                            ActivityErrors.FIELD_INVALID, warnings);
                }
            }

            return isFormatValid;
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

    /**
     * Validates and imports (if valid) a single element (and its subelements)
     * @param newParent parent object containing the field
     * @param fieldDef APIField holding the description of the field (obtained from the Fields Enumerator EP)
     * @param newJsonParent JSON as imported
     * @param fieldPath underscorified path to the field
     * @return true if valid format. Check errors to see also any business rules validation errors.
     */
    private boolean validateAndImport(Object newParent, APIField fieldDef,
            Map<String, Object> newJsonParent, String fieldPath) {
        String fieldName = fieldDef.getFieldName();
        String currentFieldPath = (fieldPath == null ? "" : fieldPath + "~") + fieldName;
        Object newJsonValue = newJsonParent == null ? null : newJsonParent.get(fieldName);

        boolean isValidFormat = formatValidator.isValid(this, newJsonParent, fieldDef, currentFieldPath, errors);
        if (isValidFormat) {
            isValidFormat = validateSubElements(fieldDef, newParent, newJsonValue, currentFieldPath);
            if (isValidFormat) {
                businessRulesValidator.isValid(this, newJsonParent, fieldDef, currentFieldPath, errors);
            }

            if (fieldDef.isImportable()) {
                Object jsonValue = newJsonParent.get(fieldName);
                Object newValue = getNewValue(fieldDef, newParent, jsonValue);
                fieldDef.getFieldAccessor().set(newParent, newValue);
            }
        }
        return isValidFormat;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object getNewValue(APIField apiField, Object parentObj, Object jsonValue) {
        boolean isCollection = apiField.isCollection();
        // on a business rule validation error we configure the input to progress with further validation
        if (jsonValue != null && BadInput.class.isAssignableFrom(jsonValue.getClass())) {
            jsonValue = ((BadInput) jsonValue).getInput();
        }

        if (jsonValue == null && !isCollection) {
            return null;
        }

        FieldType fieldType = apiField.getApiType().getFieldType();
        boolean idOnly = apiField.isIdOnly();
        // this field has possible values
        if (!isCollection && idOnly) {
            return valueConverter.getObjectById(apiField.getApiType().getType(), jsonValue);
        }

        try {
            if (isCollection) {
                Collection collection = (Collection) apiField.getFieldAccessor().get(parentObj);
                if (idOnly && jsonValue != null && !apiField.getApiType().isSimpleItemType()) {
                    collection.clear();
                    collection.add(valueConverter.getObjectById(apiField.getApiType().getType(), jsonValue));
                }
                return collection;
            } else if (fieldType.isSimpleType()) {
                if (fieldType.isDateType() || fieldType.isTimestampType()) {
                    return DateTimeUtil.parseISO8601DateTimestamp((String) jsonValue, fieldType.isTimestampType());
                } else if (fieldType.isStringType()) {
                    return extractString(apiField, parentObj, jsonValue);
                } else {
                    Method valueOf = apiField.getApiType().getType().getDeclaredMethod("valueOf", String.class);
                    return valueOf.invoke(apiField.getApiType().getType(), String.valueOf(jsonValue));
                }
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return null;
    }

    protected Object extractString(APIField apiField, Object parentObj, Object jsonValue) {
        return jsonValue;
    }

    /**
     * Validates sub-elements (recursively)
     * @param fieldDef
     * @param newParent
     * @param newJsonValue
     * @param fieldPath
     * @return currently updated object or null if any validation error occurred
     */
    private boolean validateSubElements(APIField fieldDef, Object newParent, Object newJsonValue, String fieldPath) {
        boolean isFormatValid = true;
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
            return isFormatValid;
        }

        // first validate all sub-elements
        List<APIField> childrenFields = fieldDef.getChildren();
        List<Map<String, Object>> childrenNewValues = getChildrenValues(newJsonValue, fieldType);

        // validate children, even if it is not a list -> to notify wrong entries
        if (isList || childrenFields.size() > 0) {
            Class<?> subElementClass = fieldDef.getApiType().getType();
            Object newFieldValue = fieldDef.getFieldAccessor().get(newParent);

            if (newFieldValue == null) {
                if (isList) {
                    newFieldValue = new ArrayList<>();
                } else {
                    newFieldValue = valueConverter.getNewInstance(fieldDef.getApiType().getType());
                }

                fieldDef.getFieldAccessor().set(newParent, newFieldValue);
            }

            if (isList && fieldDef.getApiType().isSimpleItemType()) {
                Collection nvs = ((Collection<?>) childrenNewValues).stream()
                        .map(v -> valueConverter.toSimpleTypeValue(v, subElementClass)).collect(Collectors.toList());
                ((Collection) newFieldValue).addAll(nvs);
            } else {
                // FIXME remove custom handling for agreements
                if (AmpAgreement.class.isAssignableFrom(newFieldValue.getClass())
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
                if (isList) {
                    APIField idField = fieldDef.getIdChild();
                    Collection newFieldValueCollection = (Collection) newFieldValue;
                    Map<Object, Object> newValueById = groupById(idField, newFieldValueCollection);

                    removeElementsWithNullIds(idField, newFieldValueCollection);

                    // match elements by ids and import them
                    Set<Object> jsonIds = new HashSet<>();
                    for (Map<String, Object> newChild : childrenNewValues) {
                        Object jsonId = newChild.get(idField.getFieldName());
                        jsonId = convert(idField.getApiType().getType(), jsonId);

                        jsonIds.add(jsonId);

                        Object element = newValueById.get(jsonId);
                        boolean notYetAdded = false;
                        if (element == null) {
                            element = valueConverter.instantiate(subElementClass);
                            notYetAdded = true;
                        }
                        branchJsonVisitor.put(fieldPath, newChild);
                        isFormatValid = validateAndImport(element, childrenFields, newChild, fieldPath)
                                && isFormatValid;
                        if (isFormatValid) {
                            valueConverter.configureDiscriminationField(element, fieldDef);
                            // actual links will be updated
                            if (notYetAdded) {
                                newFieldValueCollection.add(element);
                            }
                        }
                    }

                    removeByIdExcept(idField, newFieldValueCollection, jsonIds);
                } else if (fieldDef.isDiscriminatedObject()) {
                    Collection newFieldValueCollection = (Collection) newFieldValue;
                    Map<String, Object> newChild = childrenNewValues.isEmpty()
                            ? Collections.emptyMap() : childrenNewValues.get(0);
                    if (newFieldValueCollection.size() > 1) {
                        throw new RuntimeException("Expected one element at most! At: " + fieldPath);
                    }
                    if (newJsonValue != null) { // avoid creating new object if it is missing in json
                        Object element;
                        boolean notYetAdded = false;
                        if (newFieldValueCollection.isEmpty()) {
                            element = valueConverter.instantiate(subElementClass);
                            notYetAdded = true;
                        } else {
                            element = newFieldValueCollection.iterator().next();
                        }
                        branchJsonVisitor.put(fieldPath, newChild);
                        isFormatValid = validateAndImport(element, childrenFields, newChild, fieldPath)
                                && isFormatValid;
                        if (isFormatValid) {
                            valueConverter.configureDiscriminationField(element, fieldDef);
                            if (notYetAdded) {
                                newFieldValueCollection.add(element);
                            }
                        }
                    }
                } else {
                    Iterator<Map<String, Object>> iterNew = childrenNewValues.iterator();
                    while (iterNew.hasNext()) {
                        Map<String, Object> newChild = iterNew.next();
                        branchJsonVisitor.put(fieldPath, newChild);

                        APIField childFieldDef = getMatchedFieldDef(newChild, childrenFields);
                        isFormatValid = validateAndImport(newFieldValue, childFieldDef, newChild, fieldPath)
                                && isFormatValid;
                    }
                }
                // TODO: we also need to validate other children, some can be mandatory
            }

            fieldDef.getFieldAccessor().set(newParent, newFieldValue);
        }
        return isFormatValid;
    }

    /**
     * <p>Convert a collection of elements to map by where key is the id and value is element from collection.</p>
     *
     * <p>Null ids are not added to the map. In case of duplicate ids an exception is raised.<p/>
     */
    private Map<Object, Object> groupById(APIField idField, Collection collection) {
        Map<Object, Object> groupedById = new HashMap<>();
        for (Object v : collection) {
            Object id = idField.getFieldAccessor().get(v);
            if (id != null) {
                Object old = groupedById.put(id, v);
                if (old != null) {
                    throw new IllegalStateException("Duplicate key " + id);
                }
            }
        }
        return groupedById;
    }

    /**
     * Remove elements from collection except specified ids. Elements with null ids are never removed.
     */
    private void removeByIdExcept(APIField idField, Collection collection, Set<Object> exceptIds) {
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            Object id = idField.getFieldAccessor().get(element);
            if (id != null && !exceptIds.contains(id)) {
                iterator.remove();
            }
        }
    }

    private void removeElementsWithNullIds(APIField idField, Collection newFieldValueCollection) {
        Iterator iterator = newFieldValueCollection.iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            Object id = idField.getFieldAccessor().get(element);
            if (id == null) {
                iterator.remove();
            }
        }
    }

    /**
     * Try to convert the value to the required type. Currently only supports Integer to Long conversion. If conversion
     * is not possible then it will raise an exception.
     *
     * Sometimes value has wrong type and a correction is needed. It comes from the fact that Jackson deserialization
     * target is a Map and thus small numbers are read as Integer and larger numbers as Long.
     */
    private Object convert(Class<?> requiredType, Object value) {
        if (value == null) {
            return value;
        }
        if (Long.class.equals(requiredType) && value instanceof Integer) {
            value = ((Integer) value).longValue();
        }
        if (!requiredType.isAssignableFrom(value.getClass())) {
            throw new RuntimeException("Cannot convert " + value + " to " + requiredType);
        }
        return value;
    }

    /**
     * Gets items marked under the "children" key in the hierarchical branch of the imported JSON
     * @param jsonValue
     * @param fieldType
     * @return
     */
    private List<Map<String, Object>> getChildrenValues(Object jsonValue, FieldType fieldType) {
        if (jsonValue != null) {
            if (fieldType.isList()) {
                return (List<Map<String, Object>>) jsonValue;
            } else if (fieldType.isObject()) {
                List<Map<String, Object>> jsonValues = new ArrayList<Map<String, Object>>();
                jsonValues.add((Map<String, Object>) jsonValue);
                return jsonValues;
            }
        }
        return Collections.emptyList();
    }

    private APIField getMatchedFieldDef(Map<String, Object> newValue, List<APIField> fieldDefs) {
        if (fieldDefs.size() > 0) {
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
        return null; // why this happens? is throwing an error a better option?
    }

    public PossibleValuesCache getPossibleValuesCache() {
        return this.possibleValuesCached;
    }

    /**
     * @return the newJson
     */
    public Map<String, Object> getNewJson() {
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

    public void addError(ApiErrorMessage error) {
        errors.put(error.id, error);
    }

    public Collection<ApiErrorMessage> getWarnings() {
        return warnings.values();
    }

    protected JsonApiResponse buildResponse(Map<String, Object> details, Object content) {
        return new JsonApiResponse<>(
                ApiError.formatNoWrap(errors.values()),
                ApiError.formatNoWrap(warnings.values()),
                details, content);
    }

}
