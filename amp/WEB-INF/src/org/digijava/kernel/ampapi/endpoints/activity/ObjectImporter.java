package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.field.FieldType.LONG;

import java.lang.reflect.Field;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ErrorDecorator;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.activity.validators.mapping.DefaultErrorsMapper;
import org.digijava.kernel.ampapi.endpoints.activity.validators.mapping.JsonConstraintViolation;
import org.digijava.kernel.ampapi.endpoints.activity.validators.mapping.JsonErrorIntegrator;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.common.values.PossibleValuesCache;
import org.digijava.kernel.ampapi.endpoints.common.values.ValueConverter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.GenericErrors;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.validation.NotTranslatedValueContext;
import org.digijava.kernel.validation.TranslatedValueContext;
import org.digijava.kernel.validation.TranslationContext;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.validator.groups.API;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

/**
 * @author Octavian Ciubotaru
 */
public abstract class ObjectImporter<T> {

    private static final Logger logger = Logger.getLogger(ObjectImporter.class);

    private final InputValidatorProcessor formatValidator;

    protected Map<Integer, ApiErrorMessage> errors = new HashMap<>();
    protected Map<Integer, ApiErrorMessage> warnings = new HashMap<>();
    protected ValueConverter valueConverter;

    protected Map<String, Object> newJson;
    protected TranslationSettings trnSettings;

    private APIField apiField;

    private PossibleValuesCache possibleValuesCached;

    private Deque<Object> backReferenceStack = new ArrayDeque<>();

    private Validator beanValidator;

    private Function<ConstraintViolation, JsonConstraintViolation> jsonErrorMapper = new DefaultErrorsMapper();

    private ImporterInterchangeValidator importerInterchangeValidator;

    private List<AmpContentTranslation> translations = new ArrayList<>();
    private EditorStore editorStore = new EditorStore();
    private Site site;
    private TranslationContext translationContext;
    private TranslatedValueContext translatedValueContext;

    public ObjectImporter(InputValidatorProcessor formatValidator, APIField apiField, Site site) {
        this(formatValidator, apiField, site, new ValueConverter());
    }
    
    public ObjectImporter(InputValidatorProcessor formatValidator, APIField apiField, Site site,
                          ValueConverter valueConverter) {
        this(formatValidator, TranslationSettings.getCurrent(), apiField, site, valueConverter);
    }

    public ObjectImporter(InputValidatorProcessor formatValidator,
            TranslationSettings trnSettings, APIField apiField, Site site, ValueConverter valueConverter) {
        this.formatValidator = formatValidator;
        this.trnSettings = trnSettings;
        this.apiField = apiField;
        this.possibleValuesCached = new PossibleValuesCache(PossibleValuesEnumerator.INSTANCE, apiField.getChildren());
        this.valueConverter = valueConverter;
        this.site = site;

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        beanValidator = validatorFactory.getValidator();

        translationContext = new TranslationContext(trnSettings.getCurrentLangCode(), trnSettings.getDefaultLangCode(),
                editorStore, translations, this::getEditor, this::getContentTranslation);
        translatedValueContext = new NotTranslatedValueContext(translationContext);

        importerInterchangeValidator = new ImporterInterchangeValidator(errors, getExecutor());
    }

    /**
     * Builds a function that allows to execute the a supplier in a context. Default implementation is to execute with
     * any context.
     */
    protected Function<Supplier<Set<org.digijava.kernel.validation.ConstraintViolation>>,
            Set<org.digijava.kernel.validation.ConstraintViolation>> getExecutor() {
        return Supplier::get;
    }

    public void setJsonErrorMapper(Function<ConstraintViolation, JsonConstraintViolation> jsonErrorMapper) {
        this.jsonErrorMapper = jsonErrorMapper;
    }

    public APIField getApiField() {
        return apiField;
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
        boolean isFormatValid = deserializeObject(root, apiField.getChildren(), json, null);
        if (isFormatValid && !validateFormatOnly) {
            beforeViolationsCheck();
            processViolationsForTypes(json, root);
            processInterViolationsForTypes(json, root);
        }
        return isFormatValid;
    }

    /**
     * Invokes interchangeable validation and then integrates all constraint violations directly into json object.
     * @param json json representation of the object
     * @param root internal representation of the object
     */
    public void processInterViolationsForTypes(Map<String, Object> json, Object root) {
        importerInterchangeValidator.integrateTypeErrorsIntoResult(
                importerInterchangeValidator.validate(apiField, root, getTranslationContext()), json);
    }

    public ImporterInterchangeValidator getImporterInterchangeValidator() {
        return importerInterchangeValidator;
    }

    /**
     * Invokes bean validation and then integrates all constraint violations directly into json object.
     * @param json json representation of the object
     * @param obj internal representation of the object
     */
    private void processViolationsForTypes(Map<String, Object> json, Object obj) {
        Set<ConstraintViolation<Object>> violations = beanValidator.validate(obj, API.class, Default.class);
        JsonErrorIntegrator jsonErrorIntegrator = new JsonErrorIntegrator(jsonErrorMapper);
        jsonErrorIntegrator.mapTypeErrors(json, violations, errors);
    }

    /**
     * Deserialize one object. If JSON or deserialized object is invalid then a corresponding error will be added to
     * {@link errors}.
     *
     * @param newParent Matched parent object in which resides the field of the activity we're importing or updating
     * (for example, AmpActivityVersion newActivity is newParent for 'sectors'
     * @param fieldsDef definitions of the fields in this parent (from Fields Enumeration EP)
     * @param newJsonParent parent JSON object in which reside the analyzed fields
     * @param fieldPath the underscorified path to the field currently validated & imported
     * @return true if valid format. Check for all errors to find also business validation issues
     */
    private boolean deserializeObject(Object newParent, List<APIField> fieldsDef,
            Map<String, Object> newJsonParent, String fieldPath) {
        boolean isFormatValid = true;
        restoreBackReferences(newParent);
        try {
            backReferenceStack.push(newParent);

            Set<String> fields = new HashSet<String>(newJsonParent.keySet());
            // process all valid definitions
            for (APIField fieldDef : fieldsDef) {
                if (fieldDef.isImportable()) {
                    // format & business validators no longer invoked on read only fields
                    isFormatValid = deserializeField(newParent, fieldDef, newJsonParent, fieldPath) && isFormatValid;
                }
                fields.remove(fieldDef.getFieldName());
            }

            // and warn anything remained
            // note: due to AMP-20766, we won't be able to fully detect invalid children
            String fieldPathPrefix = fieldPath == null ? "" : fieldPath + "~";
            if (fields.size() > 0) {
                for (String invalidField : fields) {
                    // no need to go through deep-first validation flow
                    ErrorDecorator.addError(newJsonParent, invalidField, fieldPathPrefix + invalidField,
                            ValidationErrors.FIELD_INVALID, warnings);
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
     * Deserialize and validate one field. If field type is an object or list of objects those objects will be
     * deserialized recursively.
     *
     * @param newParent parent object containing the field
     * @param fieldDef APIField holding the description of the field (obtained from the Fields Enumerator EP)
     * @param newJsonParent JSON as imported
     * @param fieldPath underscorified path to the field
     * @return true if valid format. Check errors to see also any business rules validation errors.
     */
    private boolean deserializeField(Object newParent, APIField fieldDef,
            Map<String, Object> newJsonParent, String fieldPath) {
        String fieldName = fieldDef.getFieldName();
        String currentFieldPath = (fieldPath == null ? "" : fieldPath + "~") + fieldName;
        Object newJsonValue = newJsonParent.get(fieldName);

        boolean isValidFormat = formatValidator.isValid(this, newParent, newJsonParent, fieldDef, currentFieldPath);
        if (isValidFormat) {
            if (newJsonParent.containsKey(fieldName)) {
                isValidFormat = deserializeField(fieldDef, newParent, newJsonValue, currentFieldPath);
            }

            processInterViolationsForField(fieldDef, newParent, newJsonParent, fieldPath);
        }
        return isValidFormat;
    }

    private void processInterViolationsForField(APIField field, Object parentObject, Map<String, Object> parentJson,
            String fieldPath) {
        Object fieldValue = field.getFieldAccessor().get(parentObject);

        TranslatedValueContext fieldTranslatedValueContext = translatedValueContext.forField(parentObject, field);

        processInterViolationsForField(field, parentJson, fieldPath, fieldValue, fieldTranslatedValueContext);
    }

    public void processInterViolationsForField(APIField type, Map<String, Object> parentJson, String fieldPath,
            Object fieldValue, TranslatedValueContext translatedValueContext) {

        Set<org.digijava.kernel.validation.ConstraintViolation> violations =
                importerInterchangeValidator.validateField(type, fieldValue, translatedValueContext);

        importerInterchangeValidator.integrateFieldErrorsIntoResult(violations, parentJson, fieldPath);
    }

    /**
     * Deserialize a primitive or an id only field.
     */
    private Object deserializePrimitive(APIField apiField, Object parentObj, Object jsonValue) {
        FieldType fieldType = apiField.getApiType().getFieldType();
        Class<?> type = apiField.getApiType().getType();
        if (apiField.isIdOnly()) {
            // this field has possible values
            jsonValue = convert(fieldType, jsonValue);
            return jsonValue != null ?  valueConverter.getObjectById(type, jsonValue) : null;
        } else if (fieldType.isDateType() || fieldType.isTimestampType()) {
            return DateTimeUtil.parseISO8601DateTimestamp((String) jsonValue, fieldType.isTimestampType());
        } else if (fieldType.isStringType()) {
            return extractString(apiField, parentObj, jsonValue);
        } else {
            return valueConverter.toSimpleTypeValue(type, jsonValue);
        }
    }

    protected Object extractString(APIField apiField, Object parentObj, Object jsonValue) {
        return jsonValue;
    }

    /**
     * Deserialize one field and set it on the object.
     * <p>The json value was already verified and is valid.</p>
     * <p>Resulting  is not invoked.</p>
     *
     * Validates sub-elements (recursively).
     *
     * @param fieldDef
     * @param newParent
     * @param newJsonValue
     * @param fieldPath
     * @return true if the json format was valid
     */
    private boolean deserializeField(APIField fieldDef, Object newParent, Object newJsonValue, String fieldPath) {
        boolean isFormatValid = true;
        FieldType fieldType = fieldDef.getApiType().getFieldType();

        Object newFieldValue = fieldDef.getFieldAccessor().get(newParent);

        if (fieldType.isList()) {
            isFormatValid = deserializeList(fieldDef, fieldPath, newJsonValue, (Collection) newFieldValue);
        } else if (fieldType.isObject()) {
            if (newJsonValue != null) {
                if (fieldDef.isIndependent() || newFieldValue == null) {
                    newFieldValue = valueConverter.instantiate(fieldDef);
                }
                isFormatValid = deserializeObject(newFieldValue, fieldDef.getChildren(),
                        (Map<String, Object>) newJsonValue, fieldPath);
            } else {
                newFieldValue = null;
            }
        } else if (fieldType.isSimpleType()) {
            newFieldValue = deserializePrimitive(fieldDef, newParent, newJsonValue);
        } else {
            throw new RuntimeException("Unsupported case " + fieldDef.getApiType());
        }

        fieldDef.getFieldAccessor().set(newParent, newFieldValue);

        return isFormatValid;
    }

    private boolean deserializeList(APIField fieldDef, String fieldPath, Object newJsonValue, Collection collection) {
        boolean isFormatValid = true;
        FieldType fieldType = fieldDef.getApiType().getFieldType();
        Class<?> subElementClass = fieldDef.getApiType().getType();
        List<Map<String, Object>> childrenNewValues = getChildrenValues(newJsonValue, fieldType);

        if (fieldDef.getApiType().isSimpleItemType()) {
            // list of primitives
            // FIXME why not call deserializePrimitive ?
            Collection nvs = ((Collection<?>) childrenNewValues).stream()
                    .map(v -> valueConverter.toSimpleTypeValue(subElementClass, v)).collect(Collectors.toList());
            collection.clear();
            collection.addAll(nvs);
        } else {
            // list of objects
            APIField idField = fieldDef.getIdChild();
            Map<Object, Object> newValueById = groupById(idField, collection);

            removeElementsWithNullIds(idField, collection);

            // match elements by ids and import them
            Set<Object> jsonIds = new HashSet<>();
            for (Map<String, Object> newChild : childrenNewValues) {
                Object jsonId = newChild.get(idField.getFieldName());
                jsonId = convert(idField.getApiType().getFieldType(), jsonId);

                jsonIds.add(jsonId);

                Object element = newValueById.get(jsonId);
                boolean notYetAdded = false;
                if (element == null) {
                    element = valueConverter.instantiate(fieldDef);
                    notYetAdded = true;
                }
                isFormatValid &= deserializeObject(element, fieldDef.getChildren(), newChild, fieldPath);
                if (isFormatValid) {
                    // actual links will be updated
                    if (notYetAdded) {
                        collection.add(element);
                    }
                }
            }

            removeByIdExcept(idField, collection, jsonIds);
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
     * Try to convert the value to the required type. Currently only supports Integer to Long conversion. For the rest
     * of the cases it assumes that value parameter is valid and will return it.
     *
     * Sometimes value has wrong type and a correction is needed. It comes from the fact that Jackson deserialization
     * target is a Map and thus small numbers are read as Integer and larger numbers as Long.
     */
    private Object convert(FieldType requiredType, Object value) {
        if (value == null) {
            return value;
        }
        if (requiredType == LONG && value instanceof Integer) {
            value = ((Integer) value).longValue();
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

    public void addError(ApiErrorMessage error) {
        errors.put(error.id, error);
    }

    public Map<Integer, ApiErrorMessage> getErrors() {
        return errors;
    }

    public Collection<ApiErrorMessage> getWarnings() {
        return warnings.values();
    }

    public abstract T getImportResult();

    protected String getInvalidInputFieldName() {
        return null;
    }

    /**
     * Provides import/update result
     *
     * @return JsonApiResponse the result of the import or update action
     */
    public JsonApiResponse<T> getResult() {
        Map<String, Object> details = null;
        T content = errors.isEmpty() ? getImportResult() : null;

        if (content == null) {
            String invalidInput = getInvalidInputFieldName();
            if (invalidInput != null) {
                details = new HashMap<String, Object>();
                details.put(invalidInput, getNewJson());
            }
            if (errors.isEmpty()) {
                addError(GenericErrors.UNKNOWN_ERROR);
            }
        }
        return buildResponse(details, content);
    }

    protected JsonApiResponse<T> buildResponse(Map<String, Object> details, T content) {
        return new JsonApiResponse<>(
                ApiError.formatNoWrap(errors.values()),
                ApiError.formatNoWrap(warnings.values()),
                details, content);
    }

    public EditorStore getEditorStore() {
        return editorStore;
    }

    public List<AmpContentTranslation> getTranslations() {
        return translations;
    }

    public TranslationContext getTranslationContext() {
        return translationContext;
    }

    protected List<Editor> getEditor(String editorKey) {
        try {
            return DbUtil.getEditorList(editorKey, site);
        } catch (EditorException e) {
            throw new RuntimeException("Failed to load editor from db", e);
        }
    }

    protected List<AmpContentTranslation> getContentTranslation(String objectClass, Long objectId, String fieldName) {
        return ContentTranslationUtil.loadFieldTranslations(objectClass, objectId, fieldName);
    }

    public Site getSite() {
        return site;
    }
}
