package org.digijava.kernel.ampapi.endpoints.activity.field;

import static java.util.stream.Collectors.toList;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.NONE;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.SUBMIT;
import static org.digijava.kernel.util.SiteUtils.DEFAULT_SITE_ID;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.DiscriminatedFieldAccessor;
import org.digijava.kernel.ampapi.endpoints.activity.FEContext;
import org.digijava.kernel.ampapi.endpoints.activity.FMService;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.activity.SimpleFieldAccessor;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.dto.UnwrappedTranslations;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.validation.ConstraintDescriptor;
import org.digijava.kernel.validation.ConstraintDescriptors;
import org.digijava.kernel.validators.common.TotalPercentageValidator;
import org.digijava.kernel.validators.common.RegexValidator;
import org.digijava.kernel.validators.activity.UniqueValidator;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.kernel.validators.common.SizeValidator;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Independent;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.validator.groups.Submit;

/**
 * Enumerate & describe all fields of an object used for import / export in API.
 *
 * @author acartaleanu, Octavian Ciubotaru
 */
public class FieldsEnumerator {

    private static final Logger LOGGER = Logger.getLogger(FieldsEnumerator.class);

    private FieldInfoProvider fieldInfoProvider;

    private FMService fmService;

    private TranslatorService translatorService;

    private Function<String, Boolean> allowMultiplePrograms;

    /**
     * Fields Enumerator
     */
    public FieldsEnumerator(FieldInfoProvider fieldInfoProvider, FMService fmService,
            TranslatorService translatorService, Function<String, Boolean> allowMultiplePrograms) {
        this.fieldInfoProvider = fieldInfoProvider;
        this.fmService = fmService;
        this.translatorService = translatorService;
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
        String fieldTitle = FieldMap.underscorify(interchangeable.fieldTitle());

        APIField apiField = new APIField();
        apiField.setFieldName(fieldTitle);

        apiField.setId(field.isAnnotationPresent(InterchangeableId.class));

        APIType apiType = getApiType(field, context, interchangeable);
        apiField.setApiType(apiType);

        boolean isList = apiType.getFieldType().isList();

        if (apiField.isId()
                && (apiType.getFieldType() == FieldType.OBJECT || apiType.getFieldType() == FieldType.LIST)) {
            throw new RuntimeException("Id must use primitive data type.");
        }

        apiField.setPossibleValuesProviderClass(getPossibleValuesProvider(field));
        String cPVPath = StringUtils.isBlank(interchangeable.commonPV()) ? null : interchangeable.commonPV();
        apiField.setCommonPossibleValuesPath(cPVPath);

        String label = getLabelOf(interchangeable);
        apiField.setFieldLabel(getTranslationsForLabel(label));
        apiField.setImportable(getImportableValue(context, fieldTitle, interchangeable));

        if (interchangeable.percentageConstraint()) {
            apiField.setPercentage(true);
        }
        List<String> actualDependencies = getActualDependencies(interchangeable);
        if (actualDependencies != null) {
            apiField.setDependencies(actualDependencies);
        }

        apiField.setFieldNameInternal(field.getName());

        /* list type */

        apiField.setIdOnly(hasPossibleValues(field, interchangeable));

        if (!InterchangeUtils.isSimpleType(field.getType())) {
            if (!interchangeable.pickIdOnly()) {
                apiField.setChildren(getAllAvailableFields(apiType.getType(), context));
            }
            if (isList) {
                String uniqueConstraint = getUniqueConstraint(apiField, field, context);
                if (hasTreeCollectionValidatorEnabled(context)) {
                    apiField.setTreeCollectionConstraint(true);
                    apiField.setUniqueConstraint(uniqueConstraint);
                } else if (hasUniqueValidatorEnabled(context)) {
                    apiField.setUniqueConstraint(uniqueConstraint);
                }
            }
        }

        List<ConstraintDescriptor> fieldConstraintDescriptors = new ArrayList<>();

        if (isFieldIatiIdentifier(fieldTitle) && AmpClientModeHolder.isIatiImporterClient()) {
            Map<String, String> args = ImmutableMap.of();
            Set<Class<?>> groups = ImmutableSet.of();
            fieldConstraintDescriptors.add(new ConstraintDescriptor(
                    RequiredValidator.class, args, groups, ConstraintDescriptor.ConstraintTarget.FIELD));
        }

        addProgramConstraints(fieldConstraintDescriptors, apiType, context);

        if (apiField.getUniqueConstraint() != null) {
            Map<String, String> args = ImmutableMap.of("field", apiField.getUniqueConstraint());
            Set<Class<?>> groups = ImmutableSet.of();
            fieldConstraintDescriptors.add(new ConstraintDescriptor(
                    UniqueValidator.class, args, groups, ConstraintDescriptor.ConstraintTarget.FIELD));
        }

        if (TranslationSettings.canBeTranslatable(field.getType())) {
            apiField.setTranslatable(fieldInfoProvider.isTranslatable(field));
            apiField.setTranslationType(fieldInfoProvider.getTranslatableType(field));
        }

        if (apiField.getTranslationType() == TranslationSettings.TranslationType.STRING
                && !Identifiable.class.isAssignableFrom(field.getDeclaringClass())) {
            throw new RuntimeException(field.getDeclaringClass() + " must implement " + Identifiable.class);
        }

        if (ActivityEPConstants.TYPE_VARCHAR.equals(fieldInfoProvider.getType(field))) {
            apiField.setFieldLength(fieldInfoProvider.getMaxLength(field));
        }
        if (StringUtils.isNotEmpty(interchangeable.discriminatorOption())) {
            apiField.setDiscriminatorValue(interchangeable.discriminatorOption());
        }

        apiField.setIndependent(field.isAnnotationPresent(Independent.class));

        if (apiField.getApiType().getFieldType() == FieldType.LIST
                && apiField.getApiType().getItemType() == FieldType.OBJECT) {
            List<APIField> idFields = apiField.getChildren().stream()
                    .filter(APIField::isId)
                    .limit(2)
                    .collect(toList());
            if (idFields.isEmpty()) {
                throw new RuntimeException("Id field is missing: " + apiField);
            }
            if (idFields.size() > 1) {
                throw new RuntimeException("Only one id field is expected.");
            }
            apiField.setIdChild(idFields.get(0));
        }

        List<ConstraintDescriptor> beanConstraintDescriptors =
                findBeanConstraints(apiField.getApiType().getType(), context);
        apiField.setBeanConstraints(new ConstraintDescriptors(beanConstraintDescriptors));

        fieldConstraintDescriptors.addAll(findFieldConstraints(interchangeable.interValidators(), context));
        apiField.setFieldConstraints(new ConstraintDescriptors(fieldConstraintDescriptors));

        apiField.setRegexPattern(findRegexPattern(fieldConstraintDescriptors));
        apiField.setMultipleValues(isMultipleValues(apiType, fieldConstraintDescriptors));
        apiField.setSizeLimit(findSizeLimit(fieldConstraintDescriptors));
        if (hasTotalPercentageConstraint(fieldConstraintDescriptors) && apiField.getPercentageField() != null) {
            apiField.setPercentageConstraint(apiField.getPercentageField().getFieldName());
        }

        apiField.setUnconditionalRequired(getUnconditionalRequiredValue(fieldConstraintDescriptors));
        apiField.setDependencyRequired(getDependencyRequiredValue(context));
        apiField.setRequired(getRequiredValue(context,
                apiField.getDependencyRequired(),
                apiField.getUnconditionalRequired()));

        return apiField;
    }

    private APIType getApiType(Field field, FEContext context, Interchangeable interchangeable) {
        Class<?> type = getType(field, context);
        FieldType fieldType;
        FieldType itemType;

        if (InterchangeUtils.isCollection(field) && interchangeable.multipleValues()) {
            fieldType = FieldType.LIST;
            itemType = getFieldType(type, interchangeable, field);
        } else {
            fieldType = getFieldType(type, interchangeable, field);
            itemType = null;
        }

        return new APIType(type, fieldType, itemType);
    }

    private FieldType getFieldType(Class<?> type, Interchangeable interchangeable, Field field) {
        if (interchangeable.pickIdOnly()) {
            return FieldType.LONG;
        } else {
            if (InterchangeUtils.isTimestampField(field)) {
                return FieldType.TIMESTAMP;
            } else {
                return InterchangeableClassMapper.containsSimpleClass(type)
                        ? InterchangeableClassMapper.getCustomMapping(type)
                        : FieldType.OBJECT;
            }
        }
    }

    private boolean hasTotalPercentageConstraint(List<ConstraintDescriptor> descriptors) {
        return descriptors.stream()
                .anyMatch(d -> d.getConstraintValidatorClass().equals(TotalPercentageValidator.class));
    }

    /**
     * Returns size limit only if it is present and greater than 1.
     */
    private Integer findSizeLimit(List<ConstraintDescriptor> descriptors) {
        String max = descriptors.stream()
                .filter(d -> d.getConstraintValidatorClass().equals(SizeValidator.class))
                .map(d -> d.getArguments().get("max"))
                .findAny()
                .orElse(null);
        Integer intMax = max != null ? Integer.parseInt(max) : null;
        return intMax != null && intMax > 1 ? intMax : null;
    }

    private Boolean isMultipleValues(APIType apiType, List<ConstraintDescriptor> descriptors) {
        if (apiType.getFieldType().isList()) {
            return descriptors.stream()
                    .noneMatch(d -> d.getConstraintValidatorClass().equals(SizeValidator.class)
                            && "1".equals(d.getArguments().get("max")));
        } else {
            return null;
        }
    }

    private String findRegexPattern(List<ConstraintDescriptor> descriptors) {
        return descriptors.stream()
                .filter(d -> d.getConstraintValidatorClass().equals(RegexValidator.class))
                .map(d -> d.getArguments().get("regex"))
                .findAny()
                .orElse(null);
    }

    private List<ConstraintDescriptor> findBeanConstraints(Class<?> type, FEContext context) {
        InterchangeableValidator[] validators = type.getAnnotationsByType(InterchangeableValidator.class);
        return getConstraintDescriptors(validators, ConstraintDescriptor.ConstraintTarget.TYPE, context);
    }

    private List<ConstraintDescriptor> findFieldConstraints(InterchangeableValidator[] validators, FEContext context) {
        return getConstraintDescriptors(validators, ConstraintDescriptor.ConstraintTarget.FIELD, context);
    }

    private List<ConstraintDescriptor> getConstraintDescriptors(InterchangeableValidator[] validators,
            ConstraintDescriptor.ConstraintTarget constraintTarget, FEContext context) {
        List<ConstraintDescriptor> descriptors = new ArrayList<>();
        for (InterchangeableValidator validator : validators) {
            if (isVisible(validator.fmPath(), context)) {
                ImmutableSet<Class<?>> groups = ImmutableSet.copyOf(validator.groups());
                Map<String, String> attributes = parseAttributes(validator);
                descriptors.add(new ConstraintDescriptor(validator.value(), attributes, groups, constraintTarget));
            }
        }
        return descriptors;
    }

    private Map<String, String> parseAttributes(InterchangeableValidator validator) {
        Map<String, String> attributes;
        if (validator.attributes().isEmpty()) {
            attributes = ImmutableMap.of();
        } else {
            attributes = Splitter.on('&')
                    .withKeyValueSeparator('=')
                    .split(validator.attributes());
        }
        return attributes;
    }

    private boolean hasPossibleValues(Field field, Interchangeable interchangeable) {
        return interchangeable.pickIdOnly() || field.isAnnotationPresent(PossibleValues.class);
    }

    /**
     * Obtains the possible values provider class of the field, if it has one
     * @param field
     * @return null if the field doesn't have a provider class attached, otherwise -- the class
     */
    public Class<? extends PossibleValuesProvider> getPossibleValuesProvider(Field field) {
        PossibleValues ant = field.getAnnotation(PossibleValues.class);
        if (ant != null) {
            return ant.value();
        }
        return null;
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

    public APIField getMetaModel(Class<?> clazz) {
        APIField root = new APIField();
        root.setApiType(new APIType(clazz, FieldType.OBJECT));
        FEContext context = new FEContext();
        root.setChildren(getAllAvailableFields(clazz, context));
        root.setBeanConstraints(new ConstraintDescriptors(findBeanConstraints(clazz, context)));
        return root;
    }

    public List<APIField> getAllAvailableFields(Class<?> clazz) {
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
            Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
            if (interchangeable != null) {
                context.getIntchStack().push(interchangeable);
                if (isFieldVisible(context)) {
                    APIField descr = describeField(field, context);
                    descr.setFieldAccessor(new SimpleFieldAccessor(field.getName()));
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
                        descr.setFieldAccessor(new DiscriminatedFieldAccessor(new SimpleFieldAccessor(field.getName()),
                                discriminator.discriminatorField(), settings[i].discriminatorOption(),
                                settings[i].multipleValues()));
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
    private UnwrappedTranslations getTranslationsForLabel(String label) {
        UnwrappedTranslations translations = new UnwrappedTranslations();
        try {
            Collection<Message> messages = translatorService.getAllTranslationOfBody(label, DEFAULT_SITE_ID);
            for (Message m : messages) {
                translations.set(m.getLocale(), m.getMessage());
            }
            if (translations.isEmpty()) {
                translations.set("EN", label);
            }
        } catch (WorkerException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return translations;
    }

    /**
     * Describes each @Interchangeable field of a class
     */
    private String getUniqueConstraint(APIField apiField, Field field, FEContext context) {
        if (apiField.getApiType().isSimpleItemType()) {
            Interchangeable interchangeable = context.getIntchStack().peek();
            return interchangeable.uniqueConstraint() ? apiField.getFieldName() : null;
        }
        Class<?> genericClass = InterchangeUtils.getGenericClass(field);
        Field[] fields = FieldUtils.getFieldsWithAnnotation(genericClass, Interchangeable.class);
        for (Field f : fields) {
            Interchangeable interchangeable = f.getAnnotation(Interchangeable.class);
            if (isVisible(interchangeable.fmPath(), context) && interchangeable.uniqueConstraint()) {
                return FieldMap.underscorify(interchangeable.fieldTitle());
            }
        }

        return null;
    }

    /**
     * Return all fields paths that match the field filter.
     */
    public List<String> findFieldPaths(Predicate<APIField> fieldFilter, List<APIField> fields) {
        return findFieldPaths(fieldFilter, fields, "");
    }

    private List<String> findFieldPaths(Predicate<APIField> fieldFilter, List<APIField> fields, String prefix) {
        List<String> paths = new ArrayList<>();
        for (APIField f : fields) {
            if (fieldFilter.test(f)) {
                paths.add(prefix + f.getFieldName());
            }
            paths.addAll(findFieldPaths(fieldFilter, f.getChildren(), prefix + f.getFieldName() + "~"));
        }
        return paths;
    }

    private String getRequiredValue(FEContext context, String dependencyRequired, String unconditionalRequired) {

        Interchangeable fieldIntch = context.getIntchStack().peek();

        if (fieldIntch.requiredDependencies().length > 0) {
            return dependencyRequired;
        } else {
            return unconditionalRequired;
        }
    }

    private String getDependencyRequiredValue(FEContext context) {
        Interchangeable fieldIntch = context.getIntchStack().peek();

        if (fieldIntch.requiredDependencies().length > 0) {
            if (fieldIntch.dependencyRequired() == NONE) {
                throw new IllegalStateException("Interchangeable.dependencyRequired is incorrect! Field: "
                        + context.getIntchStack().peek().fieldTitle());
            }

            String fmPath = fieldIntch.dependencyRequiredFMPath();
            if (fmPath.isEmpty() || isVisible(fmPath, context)) {
                return fieldIntch.dependencyRequired() == SUBMIT
                        ? ActivityEPConstants.FIELD_NON_DRAFT_REQUIRED
                        : ActivityEPConstants.FIELD_ALWAYS_REQUIRED;
            }
        }

        return ActivityEPConstants.FIELD_NOT_REQUIRED;
    }

    /**
     * Gets the field required value.
     *
     * @param fieldConstraintDescriptors constraint descriptors for field
     * @return String with Y|ND|N, where Y (yes) = always required, ND=for draft status=false,
     *         N (no) = not required.
     */
    private String getUnconditionalRequiredValue(List<ConstraintDescriptor> fieldConstraintDescriptors) {
        return fieldConstraintDescriptors.stream()
                .filter(cd -> cd.getConstraintValidatorClass().equals(RequiredValidator.class))
                .map(cd -> cd.getGroups().contains(Submit.class)
                        ? ActivityEPConstants.FIELD_NON_DRAFT_REQUIRED
                        : ActivityEPConstants.FIELD_ALWAYS_REQUIRED)
                .findFirst()
                .orElse(ActivityEPConstants.FIELD_NOT_REQUIRED);
    }

    private boolean getImportableValue(FEContext context, String fieldTitle, Interchangeable interchangeable) {
        if (isFieldIatiIdentifier(fieldTitle) && AmpClientModeHolder.isIatiImporterClient()) {
            return true;
        }

        boolean importable = interchangeable.importable();

        if (StringUtils.isNotBlank(interchangeable.readOnlyFmPath())) {
            return importable && !isVisible(interchangeable.readOnlyFmPath(), context);
        }

        return importable;
    }

    /**
     * Determine if the field contains unique validator
     *
     * @param context current context
     * @return boolean if the field contains unique validator
     */
    private boolean hasUniqueValidatorEnabled(FEContext context) {
        return hasValidatorEnabled(context, ActivityEPConstants.UNIQUE_VALIDATOR_NAME);
    }

    /**
     * Determine if the field contains tree collection validator
     *
     * @param context current context
     * @return boolean if the field contains tree collection validator
     */
    private boolean hasTreeCollectionValidatorEnabled(FEContext context) {
        return hasValidatorEnabled(context, ActivityEPConstants.TREE_COLLECTION_VALIDATOR_NAME);
    }

    /**
     * If current field is for programs and at most one program is allowed then add this constraint to the list.
     *
     * FIXME This hack can be removed once AMP-29247 is solved.
     *
     * @param constraintDescriptors list of constraint descriptors for field
     * @param apiType type of the current field
     * @param context current context
     */
    private void addProgramConstraints(List<ConstraintDescriptor> constraintDescriptors,
            APIType apiType, FEContext context) {
        if (AmpActivityProgram.class.equals(apiType.getType())) {
            String programSettingName = context.getIntchStack().peek().discriminatorOption();
            if (!allowMultiplePrograms.apply(programSettingName)
                    && isVisible(FMVisibility.PARENT_FM + "/max Size Program Validator", context)) {
                Map<String, String> args = ImmutableMap.of("max", "1");
                Set<Class<?>> groups = ImmutableSet.of();
                constraintDescriptors.add(new ConstraintDescriptor(SizeValidator.class, args, groups,
                        ConstraintDescriptor.ConstraintTarget.FIELD));
            }
        }
    }

    /**
     * Determine if the field contains a certain validator
     *
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

    protected boolean isRequiredVisible(String fmPath, FEContext context) {
        Interchangeable peek = context.getIntchStack().pop();
        boolean isVisible = fmService.isVisible(FMVisibility.handleParentFMPath(fmPath, context.getIntchStack()));
        context.getIntchStack().push(peek);

        return isVisible;
    }

    protected boolean isVisible(String fmPath, FEContext context) {
        return fmService.isVisible(FMVisibility.handleParentFMPath(fmPath, context.getIntchStack()));
    }

    private boolean isFieldIatiIdentifier(String fieldName) {
        return StringUtils.equals(FieldMap.underscorify(ActivityFieldsConstants.IATI_IDENTIFIER), fieldName);
    }

    /**
     * Verifies each configures dependency for any additional checks
     * and builds up the final (actual) list of dependencies
     * @param interchangeable for dependency info
     * @return actual dependencies list or null if no dependency
     */
    private List<String> getActualDependencies(Interchangeable interchangeable) {
        return new ImmutableList.Builder<String>()
                .add(interchangeable.dependencies())
                .add(interchangeable.requiredDependencies())
                .build();
    }
}
