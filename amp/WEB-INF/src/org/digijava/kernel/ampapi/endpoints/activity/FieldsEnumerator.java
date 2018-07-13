package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.util.SiteUtils.DEFAULT_SITE_ID;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.resource.AmpResource;
import org.digijava.kernel.ampapi.filters.AmpOfflineModeHolder;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.RegexDiscriminator;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.util.ProgramUtil;

import com.google.common.collect.ImmutableSet;

/**
 * AMP Activity Endpoints for Activity Import / Export
 * 
 * @author acartaleanu
 */
public class FieldsEnumerator {
    
    private static final Logger LOGGER = Logger.getLogger(PossibleValuesEnumerator.class);

    /**
     * Fields that are importable & required by AMP Offline clients.
     */
    public static final Set<String> OFFLINE_REQUIRED_FIELDS = new ImmutableSet.Builder<String>()
            .add(ActivityFieldsConstants.IS_DRAFT)
            .add(ActivityFieldsConstants.APPROVAL_STATUS)
            .build();

    private boolean internalUse = false;

    private FieldInfoProvider fieldInfoProvider;

    private FMService fmService;

    private TranslatorService translatorService;

    private String iatiIdentifierField;

    /**
     * Fields Enumerator
     * 
     * @param internalUse flags if additional information for internal use is needed 
     */
    public FieldsEnumerator(FieldInfoProvider fieldInfoProvider, FMService fmService,
                            TranslatorService translatorService, boolean internalUse) {
        this.fieldInfoProvider = fieldInfoProvider;
        this.fmService = fmService;
        this.translatorService = translatorService;
        this.internalUse = internalUse;
        this.iatiIdentifierField = InterchangeUtils.getAmpIatiIdentifierFieldName();
    }
    
    /**
     * gets fields from the type of the field
     * 
     * @param field field to be described
     * @param context current context
     * @return field definitions
     */
    private List<APIField> getChildrenOfField(Field field, FEContext context) {
        if (!InterchangeUtils.isCollection(field))
            return getAllAvailableFields(field.getType(), context);
        else
            return getAllAvailableFields(InterchangeUtils.getGenericClass(field), context);
    }

    /**
     * describes a field in a complex JSON structure
     * see the wiki for details, too many options to be listed here
     * 
     * @param field field to be described
     * @param context current context
     * @return field definition
     */
    private APIField describeField(Field field, FEContext context) {
        Interchangeable interchangeable = context.getIntchStack().peek();
        String fieldTitle = InterchangeUtils.underscorify(interchangeable.fieldTitle());

        APIField apiField = new APIField();
        apiField.setFieldName(fieldTitle);

        if (interchangeable.id()) {
            apiField.setId(interchangeable.id());
        }
        
        if (interchangeable.pickIdOnly()) {
            apiField.setFieldType(InterchangeableClassMapper.getCustomMapping(java.lang.Long.class));
        } else {

            Class<?> fieldType = field.getType();
            apiField.setFieldType(InterchangeableClassMapper.containsSimpleClass(fieldType)
                    ? InterchangeableClassMapper.getCustomMapping(fieldType) : ActivityEPConstants.FIELD_TYPE_LIST);
        }
        String label = getLabelOf(interchangeable);
        apiField.setFieldLabel(InterchangeUtils.mapToBean(getTranslationsForLabel(label)));
        apiField.setRequired(getRequiredValue(context, fieldTitle));
        apiField.setImportable(interchangeable.importable());
        if (AmpOfflineModeHolder.isAmpOfflineMode() && OFFLINE_REQUIRED_FIELDS.contains(interchangeable.fieldTitle())) {
            apiField.setRequired(ActivityEPConstants.FIELD_ALWAYS_REQUIRED);
            apiField.setImportable(true);
        }
        
        if (!AmpOfflineModeHolder.isAmpOfflineMode() && isFieldIatiIdentifier(fieldTitle)) {
            apiField.setRequired(ActivityEPConstants.FIELD_ALWAYS_REQUIRED);
            apiField.setImportable(true);
        }

        if (interchangeable.percentageConstraint()){
            apiField.setPercentage(true);
        }
        List<String> actualDependencies = InterchangeDependencyResolver.getActualDependencies(interchangeable.dependencies());
        if (actualDependencies != null) {
            apiField.setDependencies(actualDependencies);
        }
        
        if (internalUse) {
            apiField.setFieldNameInternal(field.getName());
            if (InterchangeUtils.isAmpActivityVersion(field.getType())) {
                apiField.setActivity(true);
            }
        }
        
        /* list type */
        
        if (interchangeable.pickIdOnly()) {
            apiField.setIdOnly(true);
        }
        
        if (!InterchangeUtils.isSimpleType(field.getType())) {
            if (InterchangeUtils.isCollection(field)) {
                if (!hasMaxSizeValidatorEnabled(field, context)
                        && interchangeable.multipleValues()) {
                    apiField.setMultipleValues(true);
                    
                    if (interchangeable.sizeLimit() > 1) {
                        apiField.setSizeLimit(interchangeable.sizeLimit());
                    }
                } else {
                    apiField.setMultipleValues(false);
                }
                
                
                if (hasPercentageValidatorEnabled(context)) {
                    apiField.setPercentageConstraint(getPercentageConstraint(field, context));
                }
                
                String uniqueConstraint = getUniqueConstraint(field, context);
                if (hasTreeCollectionValidatorEnabled(context)) {
                    apiField.setTreeCollectionConstraint(true);
                    apiField.setUniqueConstraint(uniqueConstraint);
                } else if (hasUniqueValidatorEnabled(context)) {
                    apiField.setUniqueConstraint(uniqueConstraint);
                }
                
            }
            
            if (!interchangeable.pickIdOnly() && !InterchangeUtils.isAmpActivityVersion(field.getClass())) {
                List<APIField> children = getChildrenOfField(field, context);
                if (children != null && children.size() > 0) {
                    apiField.setChildren(children);
                }
            }
        }
        
        // only String fields should clarify if they are translatable or not
        if (java.lang.String.class.equals(field.getType())) {
            apiField.setTranslatable(fieldInfoProvider.isTranslatable(field));
        }
        if (ActivityEPConstants.TYPE_VARCHAR.equals(fieldInfoProvider.getType(field))) {
            apiField.setFieldLength(fieldInfoProvider.getMaxLength(field));
        }
        
        if (StringUtils.isNotBlank(interchangeable.regexPattern())) {
            apiField.setRegexPattern(interchangeable.regexPattern());
        } else if (interchangeable.regexPatterns().length > 0) {
            String parentTitle = getParentTitle(context);
            for (RegexDiscriminator regexDiscr : interchangeable.regexPatterns()) {
                String parent = regexDiscr.parent();
                String regexPattern = regexDiscr.regexPattern();
                if (parent.equals(parentTitle)) {
                    apiField.setRegexPattern(regexPattern);
                }
            }
        }

        apiField.setDiscriminator(interchangeable.discriminatorOption());

        return apiField;
    }

    /**
     * Get the title of the parent field from the context interchangeable stack
     * 
     * @param context
     * @return
     */
    private String getParentTitle(FEContext context) {
        Interchangeable current = context.getIntchStack().pop();
        Interchangeable parent = context.getIntchStack().peek();
        context.getIntchStack().push(current);
        
        return parent.fieldTitle();
    }

    private String getLabelOf(Interchangeable interchangeable) {
        String label;
        if (interchangeable.label().equals(ActivityEPConstants.FIELD_TITLE)) {
            label = interchangeable.fieldTitle();
        } else {
            label = interchangeable.label();
        }
        return label;
    }

    public List<APIField> getAllAvailableFields() {
        return getAllAvailableFields(AmpActivityFields.class);
    }

    public List<APIField> getContactFields() {
        return getAllAvailableFields(AmpContact.class);
    }
    
    public List<APIField> getResourceFields() {
        return getAllAvailableFields(AmpResource.class);
    }

    List<APIField> getAllAvailableFields(Class<?> clazz) {
        return getAllAvailableFields(clazz, new FEContext());
    }

    /**
     * Describes each @Interchangeable field of a class
     * 
     * @param clazz the class to be described
     * @param context current context
     * @return field definitions
     */
    private List<APIField> getAllAvailableFields(Class<?> clazz, FEContext context) {
        List<APIField> result = new ArrayList<>();
        //StopWatch.next("Descending into", false, clazz.getName());
        Field[] fields = FieldUtils.getFieldsWithAnnotation(clazz, Interchangeable.class);
        for (Field field : fields) {
            Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
            if (!internalUse && InterchangeUtils.isAmpActivityVersion(field.getType())) {
                continue;
            }
            context.getIntchStack().push(interchangeable);
            if (!InterchangeUtils.isCompositeField(field)) {
                if (isFieldVisible(context)) {
                    APIField descr = describeField(field, context);
                    if (descr != null) {
                        result.add(descr);
                    }
                }
            } else {
                InterchangeableDiscriminator discriminator = field.getAnnotation(InterchangeableDiscriminator.class);
                Interchangeable[] settings = discriminator.settings();
                for (int i = 0; i < settings.length; i++) {
                    context.getDiscriminationInfoStack().push(getDiscriminationInfo(field, settings[i]));
                    context.getIntchStack().push(settings[i]);
                    if (isFieldVisible(context)) {
                        APIField descr = describeField(field, context);
                        if (descr != null) {
                            result.add(descr);
                        }
                    }
                    context.getIntchStack().pop();
                    context.getDiscriminationInfoStack().pop();
                }
            }
            context.getIntchStack().pop();
        }
        return result;
    }

    private ImmutablePair<Class<?>, Object> getDiscriminationInfo(Field field, Interchangeable interchangeable) {
        Class<?> classOfField = InterchangeUtils.getClassOfField(field);
        String value = interchangeable.discriminatorOption();
        return new ImmutablePair<>(classOfField, value);
    }

    /**
     * Picks available translations for a label.
     * 
     * @param label the label to be translated
     * @return a map from the ISO2 code -> translation in said text
     */
    private Map<String, String> getTranslationsForLabel(String label) {
        Map<String, String> translations = new HashMap<>();
        try {
            Collection<Message> messages = translatorService.getAllTranslationOfBody(label, DEFAULT_SITE_ID);
            for (Message m : messages) {
                translations.put(m.getLocale(), m.getMessage());
            }
            if (translations.isEmpty()) {
                translations.put("EN", label);
            }
        } catch (WorkerException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return translations;
    }
    
    /**
     * Find nested field with a percentage constraint.
     * @param field field to check
     * @param context current context
     * @return name of the field with percentage constraint
     */
    private String getPercentageConstraint(Field field, FEContext context) {
        Class<?> genericClass = InterchangeUtils.getGenericClass(field);
        Field[] fields = FieldUtils.getFieldsWithAnnotation(genericClass, Interchangeable.class);
        for (Field f : fields) {
            Interchangeable interchangeable = f.getAnnotation(Interchangeable.class);
            if (isVisible(interchangeable.fmPath(), context) && interchangeable.percentageConstraint()) {
                return InterchangeUtils.underscorify(interchangeable.fieldTitle());
            }
        }
        
        return null;
    }
    
    /**
     * Describes each @Interchangeable field of a class
     */
    private String getUniqueConstraint(Field field, FEContext context) {
        Class<?> genericClass = InterchangeUtils.getGenericClass(field);
        Field[] fields = FieldUtils.getFieldsWithAnnotation(genericClass, Interchangeable.class);
        for (Field f : fields) {
            Interchangeable interchangeable = f.getAnnotation(Interchangeable.class);
            if (isVisible(interchangeable.fmPath(), context) && interchangeable.uniqueConstraint()) {
                return InterchangeUtils.underscorify(interchangeable.fieldTitle());
            }
        }
        
        return null;
    }
    
    public List<String> findActivityFieldPaths(Predicate<Field> fieldFilter) {
        FieldNameCollectingVisitor visitor = new FieldNameCollectingVisitor(fieldFilter);
        visit(AmpActivityFields.class, visitor, new VisitorContext());
        return visitor.fields;
    }

    public List<String> findContactFieldPaths(Predicate<Field> fieldFilter) {
        FieldNameCollectingVisitor visitor = new FieldNameCollectingVisitor(fieldFilter);
        visit(AmpContact.class, visitor, new VisitorContext());
        return visitor.fields;
    }
    
    public List<String> findResourceFieldPaths(Predicate<Field> fieldFilter) {
        FieldNameCollectingVisitor visitor = new FieldNameCollectingVisitor(fieldFilter);
        visit(AmpResource.class, visitor, new VisitorContext());
        return visitor.fields;
    }

    private class FieldNameCollectingVisitor implements InterchangeVisitor {

        private List<String> fields = new ArrayList<>();

        private Predicate<Field> fieldFilter;

        FieldNameCollectingVisitor(Predicate<Field> fieldFilter) {
            this.fieldFilter = fieldFilter;
        }

        @Override
        public void visit(Field field, String fieldName, VisitorContext context) {
            if (fieldFilter.test(field)) {
                StringJoiner fieldPath = new StringJoiner("~");
                context.pathStack.descendingIterator().forEachRemaining(fieldPath::add);
                fields.add(fieldPath.toString());
            }
        }
    }

    public interface InterchangeVisitor {
        void visit(Field field, String fieldName, VisitorContext context);
    }

    private class VisitorContext {
        private Deque<String> pathStack = new ArrayDeque<>();
        private FEContext feContext = new FEContext();
    }

    // TODO how to reuse this logic?
    private void visit(Class<?> clazz, InterchangeVisitor visitor, VisitorContext context) {
        for (Field field : FieldUtils.getFieldsWithAnnotation(clazz, Interchangeable.class)) {
            Interchangeable ant = field.getAnnotation(Interchangeable.class);
            context.feContext.getIntchStack().push(ant);
            if (!InterchangeUtils.isCompositeField(field)) {
                if (isFieldVisible(context.feContext)) {
                    visit(field, InterchangeUtils.underscorify(ant.fieldTitle()), ant, visitor, context);
                }
            } else {
                InterchangeableDiscriminator antd = field.getAnnotation(InterchangeableDiscriminator.class);
                Interchangeable[] settings = antd.settings();
                for (Interchangeable ants : settings) {
                    context.feContext.getDiscriminationInfoStack().push(getDiscriminationInfo(field, ants));
                    context.feContext.getIntchStack().push(ants);
                    if (isFieldVisible(context.feContext)) {
                        visit(field, InterchangeUtils.underscorify(ants.fieldTitle()), ant, visitor, context);
                    }
                    context.feContext.getIntchStack().pop();
                    context.feContext.getDiscriminationInfoStack().pop();
                }
            }
            context.feContext.getIntchStack().pop();
        }
    }

    private void visit(Field field, String fieldName, Interchangeable ant, InterchangeVisitor visitor,
            VisitorContext context) {
        context.pathStack.push(InterchangeUtils.underscorify(fieldName));

        visitor.visit(field, fieldName, context);

        Class<?> classOfField = InterchangeUtils.getClassOfField(field);
        if (!InterchangeUtils.isSimpleType(classOfField) && !ant.pickIdOnly()) {
            visit(classOfField, visitor, context);
        }

        context.pathStack.pop();
    }

    /**
     * Gets the field required value.
     *
     * @param context current context
     * @param fieldTitle the field to get its required value
     * @return String with Y|ND|N, where Y (yes) = always required, ND=for draft status=false,
     * N (no) = not required. .
     */
    private String getRequiredValue(FEContext context, String fieldTitle) {
        Interchangeable fieldIntch = context.getIntchStack().peek();
        String requiredValue = ActivityEPConstants.FIELD_NOT_REQUIRED;
        String required = fieldIntch.required();

        if (required.equals(ActivityEPConstants.REQUIRED_ALWAYS)) {
            requiredValue = ActivityEPConstants.FIELD_ALWAYS_REQUIRED;
        } else if (required.equals(ActivityEPConstants.REQUIRED_ND)
                || (!required.equals(ActivityEPConstants.REQUIRED_NONE) && isVisible(required, context))
                || (hasRequiredValidatorEnabled(context))) {
            if (fieldTitle.equals("location_percentage")) {
                requiredValue = ActivityEPConstants.FIELD_ALWAYS_REQUIRED;
            } else {
                requiredValue = ActivityEPConstants.FIELD_NON_DRAFT_REQUIRED;
            }
        }
        return requiredValue;
    }

    /**
     * Determine if the field contains unique validator
     * @param context current context
     * @return boolean if the field contains unique validator
     */
    private boolean hasUniqueValidatorEnabled(FEContext context) {
        return hasValidatorEnabled(context, ActivityEPConstants.UNIQUE_VALIDATOR_NAME);
    }

    /**
     * Determine if the field contains tree collection validator
     * @param context current context
     * @return boolean if the field contains tree collection validator
     */
    private boolean hasTreeCollectionValidatorEnabled(FEContext context) {
        return hasValidatorEnabled(context, ActivityEPConstants.TREE_COLLECTION_VALIDATOR_NAME);
    }

    /**
     * Determine if the field contains maxsize validator
     * @param context current context
     * @return boolean if the field contains maxsize validator
     */
    private boolean hasMaxSizeValidatorEnabled(Field field, FEContext context) {
        if (AmpActivityProgram.class.equals(InterchangeUtils.getGenericClass(field))) {
            try {
                AmpActivityProgramSettings setting = ProgramUtil.getAmpActivityProgramSettings(
                        context.getIntchStack().peek().discriminatorOption());
                return setting != null && !setting.isAllowMultiple();
            } catch (DgException e) {
                throw new RuntimeException(e);
            }
        } else {
            return hasValidatorEnabled(context, ActivityEPConstants.MAX_SIZE_VALIDATOR_NAME);
        }
    }

    /**
     * Determine if the field contains required validator
     * @param context current context
     * @return boolean if the field contains required validator
     */
    private boolean hasRequiredValidatorEnabled(FEContext context) {
        return hasValidatorEnabled(context, ActivityEPConstants.MIN_SIZE_VALIDATOR_NAME);
    }

    /**
     * Determine if the field contains percentage validator
     * @param context current context
     * @return boolean if the field contains percentage validator
     */
    private boolean hasPercentageValidatorEnabled(FEContext context) {
        return hasValidatorEnabled(context, ActivityEPConstants.PERCENTAGE_VALIDATOR_NAME);
    }

    /**
     * Determine if the field contains a certain validator
     * @param context current context
     * @param validatorName the name of the validator (unique, maxSize, minSize, percentage, treeCollection)
     * @return boolean if the field contains unique validator
     */
    private boolean hasValidatorEnabled(FEContext context, String validatorName) {
        boolean isEnabled = false;
        Interchangeable interchangeable = context.getIntchStack().peek();
        Validators validators = interchangeable.validators();

        String validatorFmPath = "";

        if (ActivityEPConstants.UNIQUE_VALIDATOR_NAME.equals(validatorName)) {
            validatorFmPath = validators.unique();
        } else if (ActivityEPConstants.MAX_SIZE_VALIDATOR_NAME.equals(validatorName)) {
            validatorFmPath = validators.maxSize();
        } else if (ActivityEPConstants.MIN_SIZE_VALIDATOR_NAME.equals(validatorName)) {
            validatorFmPath = validators.minSize();
        } else if (ActivityEPConstants.PERCENTAGE_VALIDATOR_NAME.equals(validatorName)) {
            validatorFmPath = validators.percentage();
        } else if (ActivityEPConstants.TREE_COLLECTION_VALIDATOR_NAME.equals(validatorName)) {
            validatorFmPath = validators.treeCollection();
        }

        if (StringUtils.isNotBlank(validatorFmPath)) {
            isEnabled = isVisible(validatorFmPath, context);
        }

        return isEnabled;
    }

    /**
     * Decides whether a field stores iati-identifier value
     *
     * @param fieldName
     * @return true if is iati-identifier
     */
    private boolean isFieldIatiIdentifier(String fieldName) {
        return StringUtils.equals(this.iatiIdentifierField, fieldName);
    }

    private boolean isFieldVisible(FEContext context) {
        Interchangeable interchangeable = context.getIntchStack().peek();

        try {
            Class<? extends ContextMatcher> cmc = interchangeable.context();
            ContextMatcher contextMatcher = cmc.newInstance();
            if (!contextMatcher.inContext(context)) {
                return false;
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Context matcher failure", e);
        }

        return isVisible(interchangeable.fmPath(), context);
    }

    private boolean isVisible(String fmPath, FEContext context) {
        Interchangeable interchangeable = context.getIntchStack().peek();
        String fieldTitle = InterchangeUtils.underscorify(interchangeable.fieldTitle());

        if (!AmpOfflineModeHolder.isAmpOfflineMode() && isFieldIatiIdentifier(fieldTitle)) {
            return true;
        } else {
            return fmService.isVisible(fmPath, context.getIntchStack());
        }
    }
}
