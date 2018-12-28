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
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.filters.AmpOfflineModeHolder;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.dbentity.AmpActivityProgram;

import com.google.common.collect.ImmutableSet;

/**
 * Enumerate & describe all fields of an object used for import / export in API.
 * 
 * @author acartaleanu, Octavian Ciubotaru
 */
public class FieldsEnumerator {
    
    private static final Logger LOGGER = Logger.getLogger(PossibleValuesEnumerator.class);

    /**
     * Fields that are importable & required by AMP Offline clients.
     */
    private static final Set<String> OFFLINE_REQUIRED_FIELDS = new ImmutableSet.Builder<String>()
            .add(ActivityFieldsConstants.IS_DRAFT)
            .add(ActivityFieldsConstants.APPROVAL_STATUS)
            .build();

    private boolean internalUse;

    private FieldInfoProvider fieldInfoProvider;

    private FMService fmService;

    private TranslatorService translatorService;

    private InterchangeDependencyResolver interchangeDependencyResolver;

    private Function<String, Boolean> allowMultiplePrograms;

    /**
     * Fields Enumerator
     * 
     * @param internalUse flags if additional information for internal use is needed 
     */
    public FieldsEnumerator(FieldInfoProvider fieldInfoProvider, FMService fmService,
                            TranslatorService translatorService, boolean internalUse,
            Function<String, Boolean> allowMultiplePrograms) {
        this.fieldInfoProvider = fieldInfoProvider;
        this.fmService = fmService;
        this.translatorService = translatorService;
        this.internalUse = internalUse;
        interchangeDependencyResolver = new InterchangeDependencyResolver(fmService);
        this.allowMultiplePrograms = allowMultiplePrograms;
    }

    /**
     * describes a field in a complex JSON structure
     * see the wiki for details, too many options to be listed here
     * 
     * @param field field to be described
     * @param context current context
     * @return field definition
     */
    protected APIField describeField(Field field, FEContext context) {
        Interchangeable interchangeable = context.getIntchStack().peek();
        String fieldTitle = InterchangeUtils.underscorify(interchangeable.fieldTitle());

        APIField apiField = new APIField();
        apiField.setFieldName(fieldTitle);

        // for discriminated case we can override the type here
        apiField.setType(InterchangeUtils.getClassOfField(field));

        apiField.setPossibleValuesProviderClass(InterchangeUtils.getPossibleValuesProvider(field));

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

        if (interchangeable.percentageConstraint()){
            apiField.setPercentage(true);
        }
        List<String> actualDependencies =
                interchangeDependencyResolver.getActualDependencies(interchangeable.dependencies());
        if (actualDependencies != null) {
            apiField.setDependencies(actualDependencies);
        }

        apiField.setFieldNameInternal(field.getName());
        if (internalUse) {
            if (InterchangeUtils.isAmpActivityVersion(field.getType())) {
                apiField.setActivity(true);
            }
        }
        
        /* list type */
        
        apiField.setIdOnly(hasPossibleValues(field, interchangeable));

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
                
            } else if (!interchangeable.pickIdOnly()) {
                apiField.setMultipleValues(false);
            }

            // FIXME remove condition that excludes activties
            if (!interchangeable.pickIdOnly() && !InterchangeUtils.isAmpActivityVersion(field.getType())) {
                Class type = getType(field, context);
                List<APIField> children = getAllAvailableFields(type, context);
                if (InterchangeUtils.isCollection(field)) {
                    apiField.setElementType(type);
                }
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
        }

        if (StringUtils.isNotEmpty(interchangeable.discriminatorOption())) {
            apiField.setDiscriminatorValue(interchangeable.discriminatorOption());
        }

        return apiField;
    }

    private boolean hasPossibleValues(Field field, Interchangeable interchangeable) {
        return interchangeable.pickIdOnly() || field.isAnnotationPresent(PossibleValues.class);
    }

    private Class<?> getType(Field field, FEContext context) {
        if (!context.getIntchStack().isEmpty()) {
            Interchangeable interchangeable = context.getIntchStack().peek();
            Class<?> type = interchangeable.type();
            if (type != Interchangeable.DefaultType.class) {
                return type;
            }
        }
    
        return InterchangeUtils.getClassOfField(field);
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
        for (Field field : InterchangeUtils.getFieldsAnnotatedWith(clazz,
                Interchangeable.class, InterchangeableDiscriminator.class)) {

            if (!internalUse && InterchangeUtils.isAmpActivityVersion(field.getType())) {
                continue;
            }
            Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
            if (interchangeable != null) {
                context.getIntchStack().push(interchangeable);
                if (isFieldVisible(context)) {
                    APIField descr = describeField(field, context);
                    descr.setFieldValueReader(new SimpleFieldValueReader(field.getName()));
                    result.add(descr);
                }
                context.getIntchStack().pop();
            }
            InterchangeableDiscriminator discriminator = field.getAnnotation(InterchangeableDiscriminator.class);
            if (discriminator != null) {
                Interchangeable[] settings = discriminator.settings();
                for (int i = 0; i < settings.length; i++) {
                    context.getDiscriminationInfoStack().push(getDiscriminationInfo(field, settings[i]));
                    context.getIntchStack().push(settings[i]);
                    if (isFieldVisible(context)) {
                        APIField descr = describeField(field, context);
                        descr.setDiscriminatorField(discriminator.discriminatorField());
                        descr.setDiscriminationConfigurer(discriminator.configurer());
                        descr.setFieldValueReader(new DiscriminatedFieldValueReader(field.getName(),
                                discriminator.discriminatorField(), settings[i].discriminatorOption()));
                        result.add(descr);
                    }
                    context.getIntchStack().pop();
                    context.getDiscriminationInfoStack().pop();
                }
            }
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
    
    List<String> findFieldPaths(Predicate<Field> fieldFilter, Class<?> clazz) {
        FieldNameCollectingVisitor visitor = new FieldNameCollectingVisitor(fieldFilter);
        visit(clazz, visitor, new VisitorContext());
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
        for (Field field : InterchangeUtils.getFieldsAnnotatedWith(clazz,
                Interchangeable.class, InterchangeableDiscriminator.class)) {

            Interchangeable ant = field.getAnnotation(Interchangeable.class);
            if (ant != null) {
                context.feContext.getIntchStack().push(ant);
                if (isFieldVisible(context.feContext)) {
                    visit(field, InterchangeUtils.underscorify(ant.fieldTitle()), ant, visitor, context);
                }
                context.feContext.getIntchStack().pop();
            }

            InterchangeableDiscriminator antd = field.getAnnotation(InterchangeableDiscriminator.class);
            if (antd != null) {
                Interchangeable[] settings = antd.settings();
                for (Interchangeable ants : settings) {
                    context.feContext.getDiscriminationInfoStack().push(getDiscriminationInfo(field, ants));
                    context.feContext.getIntchStack().push(ants);
                    if (isFieldVisible(context.feContext)) {
                        visit(field, InterchangeUtils.underscorify(ants.fieldTitle()), ants, visitor, context);
                    }
                    context.feContext.getIntchStack().pop();
                    context.feContext.getDiscriminationInfoStack().pop();
                }
            }
        }
    }

    private void visit(Field field, String fieldName, Interchangeable ant, InterchangeVisitor visitor,
            VisitorContext context) {
        context.pathStack.push(InterchangeUtils.underscorify(fieldName));

        visitor.visit(field, fieldName, context);
        Class<?> classOfField = getType(field, context.feContext);
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
            return allowMultiplePrograms.apply(context.getIntchStack().peek().discriminatorOption());


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

    private boolean isFieldVisible(FEContext context) {
        Interchangeable interchangeable = context.getIntchStack().peek();

        return isVisible(interchangeable.fmPath(), context);
    }

    protected boolean isVisible(String fmPath, FEContext context) {
        return fmService.isVisible(fmPath, context.getIntchStack());
    }
}
