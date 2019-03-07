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
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.ampapi.endpoints.activity.utils.AIHelper;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.common.ReflectionUtil;
import org.digijava.kernel.ampapi.endpoints.common.values.PossibleValuesCache;
import org.digijava.kernel.ampapi.endpoints.common.values.ValueConverter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author Octavian Ciubotaru
 */
public class ObjectImporter {

    private static final Logger logger = Logger.getLogger(ObjectImporter.class);

    private final InputValidatorProcessor formatValidator;
    private final InputValidatorProcessor businessRulesValidator;

    protected Map<Integer, ApiErrorMessage> errors = new HashMap<>();
    protected ValueConverter valueConverter = new ValueConverter();

    protected JsonBean newJson;
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

    public ObjectImporter(InputValidatorProcessor formatValidator, InputValidatorProcessor businessRulesValidator,
            List<APIField> apiFields) {
        this(formatValidator, businessRulesValidator, TranslationSettings.getCurrent(), apiFields);
    }

    public ObjectImporter(InputValidatorProcessor formatValidator, InputValidatorProcessor businessRulesValidator, 
            TranslationSettings trnSettings, List<APIField> apiFields) {
        this.formatValidator = formatValidator;
        this.businessRulesValidator = businessRulesValidator;
        this.trnSettings = trnSettings;
        this.apiFields = apiFields;
        this.possibleValuesCached = new PossibleValuesCache(apiFields);
    }

    public List<APIField> getApiFields() {
        return apiFields;
    }

    /**
     * Clean all importable fields and leave other fields intact.
     */
    protected void cleanImportableFields(List<APIField> fieldDefs, Object obj) {
        if (obj != null) {
            fieldDefs.stream()
                    .filter(APIField::isImportable)
                    .forEach(f -> cleanImportableField(f, obj));
        }
    }

    private void cleanImportableField(APIField fieldDef, Object obj) {
        try {
            Field field = ReflectionUtil.getField(obj, fieldDef.getFieldNameInternal());
            if (InterchangeUtils.isCollection(field)) {
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
    public boolean validateAndImport(Object root, Map<String, Object> json) {
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
     * @return true if valid format. Check for all errors to find also business validation issues
     */
    protected boolean validateAndImport(Object newParent, List<APIField> fieldsDef,
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

            // and error anything remained
            // note: due to AMP-20766, we won't be able to fully detect invalid children
            String fieldPathPrefix = fieldPath == null ? "" : fieldPath + "~";
            if (fields.size() > 0 && !ignoreUnknownFields()) {
                isFormatValid = false;
                for (String invalidField : fields) {
                    // no need to go through deep-first validation flow
                    formatValidator.addError(newJsonParent, invalidField, fieldPathPrefix + invalidField,
                            ActivityErrors.FIELD_INVALID, errors);
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

    protected boolean ignoreUnknownFields() {
        return false;
    }

    /**
     * Validates and imports (if valid) a single element (and its subelements)
     * @param newParent parent object containing the field
     * @param fieldDef JsonBean holding the description of the field (obtained from the Fields Enumerator EP)
     * @param newJsonParent JSON as imported
     * @param fieldPath underscorified path to the field
     * @return true if valid format. Check errors to see also any business rules validation errors. 
     */
    private boolean validateAndImport(Object newParent, APIField fieldDef,
            Map<String, Object> newJsonParent, String fieldPath) {
        String fieldName = getFieldName(fieldDef, newJsonParent);
        String currentFieldPath = (fieldPath == null ? "" : fieldPath + "~") + fieldName;
        Object newJsonValue = newJsonParent == null ? null : newJsonParent.get(fieldName);

        boolean isValidFormat = formatValidator.isValid(this, newJsonParent, fieldDef, currentFieldPath, errors);
        if (isValidFormat) {
            businessRulesValidator.isValid(this, newJsonParent, fieldDef, currentFieldPath, errors);
            isValidFormat = validateSubElements(fieldDef, newParent, newJsonValue, currentFieldPath);
            setNewField(newParent, fieldDef, newJsonParent, currentFieldPath);
        }
        return isValidFormat;
    }

    /**
     * Configures new value with assumption that it was already validated before
     */
    private void setNewField(Object newParent, APIField fieldDef, Map<String, Object> newJsonParent, String fieldPath) {
        boolean importable = fieldDef.isImportable();
        String fieldName = fieldDef.getFieldName();
        String actualFieldName = fieldDef.getFieldNameInternal();
        Object fieldValue = newJsonParent.get(fieldName);
        Field objField = ReflectionUtil.getField(newParent, actualFieldName);
        if (objField == null) {
            String error = "Actual Field not found: " + actualFieldName + ", fieldPath: " + fieldPath; 
            logger.error(error);
            throw new RuntimeException(error);
        }

        if (importable) {
            Object newValue = getNewValue(objField, newParent, fieldValue, fieldDef);
            // first we clear all fields (as of now), that's why setting a value here only when it is present
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
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object getNewValue(Field field, Object parentObj, Object jsonValue, APIField fieldDef) {
        boolean isCollection = InterchangeUtils.isCollection(field);
        if (jsonValue == null && !isCollection) {
            return null;
        }

        FieldType fieldType = fieldDef.getApiType().getFieldType();
        boolean idOnly = fieldDef.isIdOnly();

        // on a business rule validation error we configure the input to progress with further validation
        if (jsonValue != null && JsonBean.class.isAssignableFrom(jsonValue.getClass())) {
            jsonValue = ((JsonBean) jsonValue).get(ActivityEPConstants.INPUT);
        }

        // this field has possible values
        if (!isCollection && idOnly) {
            return valueConverter.getObjectById(field.getType(), jsonValue);
        }

        Object value = null;

        try {
            if (isCollection) {
                value = field.get(parentObj);
                Collection col = (Collection) value;
                if (col == null) {
                    col = (Collection) valueConverter.getNewInstance(parentObj, field);
                }
                if (idOnly && jsonValue != null && !fieldDef.getApiType().isSimpleItemType()) {
                    Class<?> objectType = AIHelper.getGenericsParameterClass(field);
                    Object res = valueConverter.getObjectById(objectType, jsonValue);
                    col.add(res);
                }
            } else if (fieldType.isSimpleType()) {
                if (Date.class.equals(field.getType())) {
                    value = DateTimeUtil.parseISO8601DateTime((String) jsonValue);
                } else if (String.class.equals(field.getType())) {
                    value = extractString(field, parentObj, jsonValue);
                } else {
                    Method valueOf = field.getType().getDeclaredMethod("valueOf", String.class);
                    value = valueOf.invoke(field.getType(), String.valueOf(jsonValue));
                }
            } else if (AmpAgreement.class.equals(field.getType())) {
                value = field.get(parentObj);
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return value;
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
    private boolean validateSubElements(APIField fieldDef, Object newParent, Object newJsonValue, String fieldPath) {
        boolean isFormatValid = true;
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
            return isFormatValid;
        }
        
        // TODO AMP-28121 remove this temporary workaround
        boolean isObject = AmpActivityGroup.class.isAssignableFrom(fieldDef.getApiType().getType());

        // first validate all sub-elements
        List<APIField> childrenFields = fieldDef.getChildren();
        List<Map<String, Object>> childrenNewValues = getChildrenValues(newJsonValue, isList);

        // validate children, even if it is not a list -> to notify wrong entries
        if ((isList || childrenFields != null && childrenFields.size() > 0) && childrenNewValues != null) {
            String actualFieldName = fieldDef.getFieldNameInternal();
            Field newField = ReflectionUtil.getField(newParent, actualFieldName);
            Object newFieldValue;
            Class<?> subElementClass = fieldDef.getApiType().getElementType();
            try {
                newFieldValue = newField == null ? null : newField.get(newParent);
                if (newParent != null && newFieldValue == null) {
                    newFieldValue = valueConverter.getNewInstance(newParent, newField);
                }
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }

            if (isList && fieldDef.getApiType().isSimpleItemType()) {
                Collection nvs = ((Collection<?>) childrenNewValues).stream()
                        .map(v -> valueConverter.toSimpleTypeValue(v, subElementClass)).collect(Collectors.toList());
                ((Collection) newFieldValue).addAll(nvs);
            } else {
                // FIXME remove custom handling for agreements
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

                    if (isList && !isObject) {
                        try {
                            Object newSubElement = subElementClass.newInstance();
                            isFormatValid = validateAndImport(newSubElement, childrenFields, newChild, fieldPath)
                                    && isFormatValid;
                            if (isFormatValid) {
                                valueConverter.configureDiscriminationField(newSubElement, fieldDef);
                                // actual links will be updated
                                ((Collection) newFieldValue).add(newSubElement);
                            }
                        } catch (InstantiationException | IllegalAccessException e) {
                            logger.error(e.getMessage());
                            throw new RuntimeException(e);
                        }
                    } else {
                        isFormatValid = validateAndImport(newFieldValue, childFieldDef, newChild, fieldPath)
                                && isFormatValid;
                    }
                }
                // TODO: we also need to validate other children, some can be mandatory
            }
        }
        return isFormatValid;
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

    public PossibleValuesCache getPossibleValuesCache() {
        return this.possibleValuesCached;
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
