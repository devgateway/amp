package org.digijava.kernel.ampapi.endpoints.activity;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.discriminators.CurrencyCommonPossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.valueproviders.GenericInterchangeableValueProvider;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.exception.ApiExceptionMapper;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.services.sync.model.SyncConstants.Entities;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.annotations.interchange.InterchangeableValue;
import org.digijava.module.aim.annotations.interchange.InterchangeableValueProvider;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

/**
 * AMP Activity Endpoint for Possible Values -- /activity/fields/:fieldName
 * and all the methods only it might have use of
 * 
 * @author acartaleanu
 */
public class PossibleValuesEnumerator {
    
    public static final Logger LOGGER = Logger.getLogger(PossibleValuesEnumerator.class);

    public static final PossibleValuesEnumerator INSTANCE = new PossibleValuesEnumerator(new AmpPossibleValuesDAO(),
            AMPTranslatorService.INSTANCE);

    private static final Multimap<Class<?>, String> ENTITY_CLASS_TO_SYNC_ENTITIES =
            new ImmutableMultimap.Builder<Class<?>, String>()
                .putAll(AmpCategoryValue.class, Entities.CATEGORY_VALUE, Entities.CATEGORY_CLASS)
                .putAll(AmpLocation.class, Entities.LOCATION, Entities.CATEGORY_VALUE_LOCATION,
                        Entities.CATEGORY_VALUE, Entities.CATEGORY_CLASS)
                .putAll(AmpSector.class, Entities.SECTOR, Entities.SECTOR_SCHEME, Entities.CLASSIFICATION_CONFIG)
                .putAll(AmpTheme.class, Entities.THEME, Entities.ACTIVITY_PROGRAM_SETTINGS)
                .putAll(AmpOrganisation.class, Entities.ORGANISATION)
                .putAll(AmpRole.class, Entities.ROLE)
                .putAll(AmpCurrency.class, Entities.CURRENCY)
                .putAll(AmpContact.class, Entities.CONTACT)
                .putAll(AmpActivityProgramSettings.class, Entities.ACTIVITY_PROGRAM_SETTINGS)
                .putAll(AmpTeamMember.class, Entities.WORKSPACE_MEMBER, Entities.USER)
                .putAll(AmpTeam.class, Entities.WORKSPACES)
                .putAll(User.class, Entities.USER)
                .putAll(AmpComponentType.class, Entities.COMPONENT_TYPE)
                .putAll(AmpFiscalCalendar.class, Entities.CALENDAR)
                .build();

    private PossibleValuesDAO possibleValuesDAO;
    private TranslatorService translatorService;

    public PossibleValuesEnumerator(PossibleValuesDAO possibleValuesDAO, TranslatorService translatorService) {
        this.possibleValuesDAO = possibleValuesDAO;
        this.translatorService = translatorService;
    }

    /**
     * Returns all sync entities that need to be monitored for possible value changes.
     * @return a list of sync entities
     */
    public Set<String> getAllSyncEntities() {
        return new HashSet<>(PossibleValuesEnumerator.ENTITY_CLASS_TO_SYNC_ENTITIES.values());
    }

    /**
     * Returns a predicate that matches all fields that have possible values.
     * @return a predicate
     */
    public Predicate<Field> fieldsWithPossibleValues() {
        List<Class<?>> entityClasses = getEntityClasses(getAllSyncEntities());
        return classOfFieldIs(assignableFromAny(entityClasses))
                .or(fieldHasPossibleValuesProvider());
    }

    /**
     * Returns a predicate that matches all fields that have a possible values provider.
     * @return a predicate.
     */
    private Predicate<Field> fieldHasPossibleValuesProvider() {
        return field -> InterchangeUtils.getPossibleValuesProvider(field) != null;
    }

    /**
     * Returns a predicate that matches fields that depend on specific changelog entities.
     * @param syncEntities sync entities that have changed
     * @return field filter
     */
    public Predicate<Field> fieldsDependingOn(Set<String> syncEntities) {
        List<Class<?>> targetClasses = getEntityClasses(syncEntities);
        return classOfFieldIs(assignableFromAny(targetClasses))
                .or(classOfPossibleValuesIs(assignableFromAny(targetClasses)));
    }

    /**
     * Return a list of hibernate entity classes that are associated with the requested changelog entities.
     * @param syncEntities changelog entities
     * @return a list of hibernate entity types
     */
    private List<Class<?>> getEntityClasses(Set<String> syncEntities) {
        List<Class<?>> targetClasses = new ArrayList<>();
        PossibleValuesEnumerator.ENTITY_CLASS_TO_SYNC_ENTITIES.asMap().forEach((k, v) -> {
            if (v.stream().anyMatch(syncEntities::contains)) {
                targetClasses.add(k);
            }
        });
        return targetClasses;
    }

    /**
     * Predicate that filters fields by the properties of possible values class.
     * @param classPredicate predicate for possible values class
     * @return a predicate
     */
    private Predicate<Field> classOfPossibleValuesIs(Predicate<Class<?>> classPredicate) {
        return field -> {
            Class<?> entityClass = InterchangeUtils.getPossibleValuesClass(field);
            return entityClass != null && classPredicate.test(entityClass);
        };
    }

    /**
     * Wrapper for a predicate that tests field's class.
     * @param p field's class predicate
     * @return field predicate
     */
    private Predicate<Field> classOfFieldIs(Predicate<Class<?>> p) {
        return field -> p.test(InterchangeUtils.getClassOfField(field));
    }

    /**
     * Returns true if predicate's class is assignable from any of the classes specified as parameter.
     * @param classes classes to test against
     * @return a predicate
     */
    private Predicate<Class<?>> assignableFromAny(List<Class<?>> classes) {
        return clazz -> classes.stream().anyMatch(clazz::isAssignableFrom);
    }

    public List<PossibleValue> getPossibleValuesForField(String longFieldName, List<APIField> apiFields) {
        return getPossibleValuesForField(longFieldName, null, apiFields);
    }

    /**
     * recursive method that gets possible values that can be held by a field
     *
     * FIXME maybe redo the way discriminatorOption is passed down the stack??
     * for example someone forgot to apply discrimination value for category value field and discriminatorOption from
     * stack is now acting like category class key?
     *
     * @param longFieldName underscorified field name
     * @param discriminatorOption recursive option to be passed down if there was a discriminator option higher up
     * @return JSON object containing the possible values that can be held by the field
     */
    private List<PossibleValue> getPossibleValuesForField(String longFieldName,
            String discriminatorOption, List<APIField> apiFields) {

        if (longFieldName.contains("~")) {
            /*
             * we might have a field containing a discriminator description,
             * but what we actually need is underneath -> obtain name of the field underneath
             * */
            String fieldName = longFieldName.substring(0, longFieldName.indexOf('~'));
            APIField apiField = findField(apiFields, fieldName);

            String configString = discriminatorOption;
            if (apiField.getDiscriminatorValue() != null) {
                configString = apiField.getDiscriminatorValue();
            }
            
            return getPossibleValuesForField(longFieldName.substring(longFieldName.indexOf('~') + 1),
                    configString, apiField.getChildren());
        } else {
            /*
             * the last field might contain discriminated values
             * if it is such a field, it's a special case for each class
             * 
             * */

            APIField apiField = findField(apiFields, longFieldName);

            String configString = discriminatorOption;
            if (apiField.getDiscriminatorValue() != null) {
                configString = apiField.getDiscriminatorValue();
            }

            try {
                Class<? extends PossibleValuesProvider> providerClass = apiField.getPossibleValuesProviderClass();
                if (providerClass != null) {
                    return getPossibleValuesDirectly(providerClass);
                }
            } catch (Exception e) {
                throw newServerErrorException("Failed to obtain possible values.", e);
            }
            if (apiField.getDiscriminatorValue() != null || configString != null) {
                return getPossibleValuesForComplexField(apiField, configString);
            }

            return getPossibleValuesForField(apiField);
        }
    }

    private APIField findField(List<APIField> apiFields, String fieldName) {
        return apiFields.stream()
                .filter(f -> f.getFieldName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> newBadRequestException(ActivityErrors.FIELD_INVALID.withDetails(fieldName)));
    }

    private ApiRuntimeException newServerErrorException(String message) {
        return newServerErrorException(message, null);
    }

    private ApiRuntimeException newServerErrorException(String message, Throwable e) {
        JsonBean error = ApiError.toError(ApiExceptionMapper.INTERNAL_ERROR
                .withDetails(message));
        return new ApiRuntimeException(Response.Status.INTERNAL_SERVER_ERROR, error, e);
    }

    private ApiRuntimeException newBadRequestException(ApiErrorMessage message) {
        return new ApiRuntimeException(Response.Status.BAD_REQUEST, ApiError.toError(message));
    }

    /**
     * method employed for the scenario that possible values are to be obtained from
     * a PossibleValuesProvider-derived class, instead of the usual database queries
     * @param possibleValuesProviderClass
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private List<PossibleValue> getPossibleValuesDirectly(
            Class<? extends PossibleValuesProvider> possibleValuesProviderClass)
            throws IllegalAccessException, InstantiationException {
        PossibleValuesProvider provider = possibleValuesProviderClass.newInstance();
        return provider.getPossibleValues(translatorService);
    }
    
    /**
     * Complex fields are discriminated fields -- when several underscorified paths 
     * lead to the same Java field. This method gets possible values for such fields
     * @param configValue
     * @return
     */
    private List<PossibleValue> getPossibleValuesForComplexField(APIField apiField, String configValue) {
        /*AmpActivitySector || AmpComponentFunding || AmpActivityProgram*/
        List<Object[]> items;
        Class<?> clazz = apiField.getType();
        if (clazz.equals(AmpSector.class)) {
            return getPossibleSectors(configValue);
        } else if (clazz.equals(AmpTheme.class)) {
            return getPossibleThemes(configValue);
        } else if (clazz.equals(AmpCategoryValue.class)){
            return getPossibleCategoryValues(apiField);
        } else if (clazz.equals(AmpClassificationConfiguration.class)) {
            return getPossibleValuesGenericCase(clazz,
                    () -> Collections.singletonList(possibleValuesDAO.getAmpClassificationConfiguration(configValue)));
        }

        //not a complex field, after all
        return getPossibleValuesForField(apiField);
    }
    
    /**
     * Generic method for obtaining possible values for most cases (without any fancy special cases)
     * @return
     */
    private List<PossibleValue> getPossibleValuesForField(APIField apiField) {
        Class<?> clazz = apiField.getType();
        if (clazz.isAssignableFrom(AmpCategoryValue.class))
            return getPossibleCategoryValues(apiField);
        if (clazz.isAssignableFrom(AmpLocation.class))
            return getPossibleLocations();
        if (clazz.isAssignableFrom(AmpComponentType.class))
            return getComponentTypes();
        if (clazz.isAssignableFrom(AmpContact.class)) {
            return getPossibleContacts();
        }
        if (clazz.isAssignableFrom(AmpOrganisation.class)) {
            return getPossibleValuesGenericCase(clazz, () -> possibleValuesDAO.getOrganisations());
        }
        if (clazz.isAssignableFrom(AmpCurrency.class)) {
            return getPossibleCurrencies();
        }

        if (InterchangeUtils.isEnumerable(apiField.getType())) {
            return getPossibleValuesGenericCase(clazz, () -> possibleValuesDAO.getGenericValues(clazz));
        } else {
            return new ArrayList<>();
        }
    }

    private List<PossibleValue> getComponentTypes() {
        return possibleValuesDAO.getComponentTypes().stream()
                .map(this::toPossibleValue)
                .collect(toList());
    }

    private PossibleValue toPossibleValue(AmpComponentType type) {
        return new PossibleValue(type.getType_id(), type.getName(), translatorService.translateLabel(type.getName()));
    }
    
    private PossibleValue toPossibleValue(AmpContact contact) {
        return new PossibleValue(contact.getId(), contact.getNameAndLastName(),  ImmutableMap.of());
    }

    private <T> List<PossibleValue> getPossibleValuesGenericCase(Class<T> clazz,
            Supplier<List> possibleValuesSupplier) {

        String idFieldName = getIdFieldName(clazz);

        InterchangeableValueProvider<T> valueProvider = getValueProvider(clazz);

        List<T> objectList = possibleValuesSupplier.get();
        return objectList.stream()
                .map(o -> getGenericPossibleValue(o, idFieldName, valueProvider))
                .collect(toList());
    }

    private <T> InterchangeableValueProvider<T> getValueProvider(Class<T> clazz) {
        if (clazz.isAnnotationPresent(InterchangeableValue.class)) {
            return getInterchangeableValueProvider(clazz);
        }

        Field[] valueFields = FieldUtils.getFieldsWithAnnotation(clazz, PossibleValueValue.class);
        if (valueFields.length == 0) {
            throw newServerErrorException("Could not find a field annotated with @PossibleValueValue in " + clazz);
        }
        if (valueFields.length > 1) {
            throw newServerErrorException("Found more than one field annotated with @PossibleValueValue in " + clazz);
        }
        String valueFieldName = valueFields[0].getName();

        return new GenericInterchangeableValueProvider<>(clazz, valueFieldName);
    }

    private <T> String getIdFieldName(Class<T> clazz) {
        Field[] idFields = FieldUtils.getFieldsWithAnnotation(clazz, PossibleValueId.class);
        if (idFields.length == 0) {
            throw newServerErrorException("Could not find a field annotated with @PossibleValueId in " + clazz);
        }
        if (idFields.length > 1) {
            throw newServerErrorException("Found more than one field annotated with @PossibleValueId in " + clazz);
        }
        return idFields[0].getName();
    }

    private <T> InterchangeableValueProvider<T> getInterchangeableValueProvider(Class<T> clazz) {
        InterchangeableValue annotation = clazz.getAnnotation(InterchangeableValue.class);
        try {
            return annotation.value().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw newServerErrorException("Failed to instantiate value provider for " + clazz, e);
        }
    }

    private <T> PossibleValue getGenericPossibleValue(T object, String idProperty,
            InterchangeableValueProvider<T> valueProvider) {
        try {
            Long id = (Long) PropertyUtils.getProperty(object, idProperty);
            String value = valueProvider.getValue(object);
            Map<String, String> translatedValue = ImmutableMap.of();
            if (valueProvider.isTranslatable()) {
                translatedValue = translatorService.translateLabel(value);
            }
            Object extraInfo = valueProvider.getExtraInfo(object);
            return new PossibleValue(id, value, translatedValue, extraInfo);
        } catch (ReflectiveOperationException e) {
            throw newServerErrorException("Failed to extract possible value object from " + object, e);
        }
    }

    private List<PossibleValue> getPossibleLocations() {
        ListMultimap<Long, PossibleValue> groupedValues = ArrayListMultimap.create();

        Map<Long, Long> locCatToLocId = new HashMap<>();

        List<Object[]> possibleLocations = possibleValuesDAO.getPossibleLocations();
        for (Object[] item : possibleLocations) {
            Long locId = ((Number) item[PossibleValuesDAO.LOC_ID_POS]).longValue();

            // FIXME should be non-null AMP-25735
            Long locCatId = getLongOrNull((Number) item[PossibleValuesDAO.LOC_CAT_ID_POS]);

            if (locCatId != null) {
                locCatToLocId.put(locCatId, locId);
            }
        }

        for (Object[] item : possibleLocations) {
            Long locId = ((Number) item[PossibleValuesDAO.LOC_ID_POS]).longValue();
            String locCatName = ((String) item[PossibleValuesDAO.LOC_CAT_NAME_POS]);
            Long parentLocCatId = getLongOrNull((Number) item[PossibleValuesDAO.LOC_PARENT_CAT_ID_POS]);
            String parentLocCatName = ((String) item[PossibleValuesDAO.LOC_PARENT_CAT_NAME_POS]);
            Long categoryValueId = getLongOrNull((Number) item[PossibleValuesDAO.LOC_CAT_VAL_ID_POS]);
            String categoryValueName = ((String) item[PossibleValuesDAO.LOC_CAT_VAL_NAME_POS]);
            String iso = ((String) item[PossibleValuesDAO.LOC_ISO]);

            // FIXME remove this filter during AMP-25735
            if (locCatName == null && parentLocCatId == null && parentLocCatName == null
                    && categoryValueId == null && categoryValueName == null) {
                continue;
            }

            Long parentLocId = locCatToLocId.get(parentLocCatId);

            LocationExtraInfo extraInfo = new LocationExtraInfo(parentLocId, parentLocCatName,
                    categoryValueId, categoryValueName, iso);

            Map<String, String> translatedValues = translatorService.translateLabel(locCatName);
            groupedValues.put(parentLocId, new PossibleValue(locId, locCatName, translatedValues, extraInfo));
        }
        return convertToHierarchical(groupedValues);
    }

    private List<PossibleValue> getPossibleSectors(String configValue) {
        List<Object[]> items = possibleValuesDAO.getSectors(configValue);
        return setProperties(items, false, this::getSectorExtraInfo);
    }

    private List<PossibleValue> getPossibleThemes(String configValue) {
        List<Object[]> items = possibleValuesDAO.getThemes(configValue);
        return setProperties(items, false, this::getThemeExtraInfo);
    }

    private List<PossibleValue> getPossibleCurrencies() {
        CurrencyCommonPossibleValuesProvider provider = new CurrencyCommonPossibleValuesProvider();
        return provider.getPossibleValues(translatorService);
    }

    private Long getLongOrNull(Number number) {
        if (number != null) {
            return number.longValue();
        } else {
            return null;
        }
    }

    /**
     * Gets possible values for the AmpCategoryValue class
     */
    @SuppressWarnings("unchecked")
    private List<PossibleValue> getPossibleCategoryValues(APIField apiField) {
        String discriminatorOption = apiField.getDiscriminatorValue();
        if (StringUtils.isBlank(discriminatorOption)) {
            throw newServerErrorException("Category value field without discriminator value. " + apiField);
        }

        if (discriminatorOption.equals(CategoryConstants.IMPLEMENTATION_LOCATION_KEY)) {
            return getImplementationLocationValues();
        } else {
            List<Object[]> objColList = possibleValuesDAO.getCategoryValues(discriminatorOption);
            return setProperties(objColList, true, this::getCategoryValueExtraInfo);
        }
    }

    private List<PossibleValue> getImplementationLocationValues() {
        String key = CategoryConstants.IMPLEMENTATION_LOCATION_KEY;
        List<AmpCategoryValue> collectionByKey = CategoryManagerUtil.getAllAcceptableValuesForACVClass(key, null);
        return collectionByKey.stream()
                .filter(AmpCategoryValue::isVisible)
                .map(this::getImplementationLocationValue)
                .collect(toList());
    }

    private PossibleValue getImplementationLocationValue(AmpCategoryValue locCategory) {
        Long id = locCategory.getId();
        String value = locCategory.getValue();
        Map<String, String> translatedValues = translatorService.translateLabel(value);

        List<Long> implementationLevels = locCategory.getUsedValues().stream()
                .map(AmpCategoryValue::getId)
                .collect(toList());
        ImplementationLocationExtraInfo extraInfo = new ImplementationLocationExtraInfo(locCategory.getIndex(),
                implementationLevels);

        return new PossibleValue(id, value, translatedValues, extraInfo);
    }
    
    /**
     * Gets possible values for the AmpContact class
     * @return contact possible values 
     */
    @SuppressWarnings("unchecked")
    private List<PossibleValue> getPossibleContacts() {
        return possibleValuesDAO.getContacts().stream()
                .map(this::toPossibleValue)
                .collect(toList());
    }

    private List<PossibleValue> setProperties(List<Object[]> objColList, boolean checkDeleted,
            Function<Object[], Object> extraInfoFunc) {
        ListMultimap<Long, PossibleValue> groupedValues = ArrayListMultimap.create();
        for (Object[] item : objColList){
            Long id = ((Number)(item[0])).longValue();
            String value = ((String)(item[1]));
            boolean itemGood = !checkDeleted || Boolean.FALSE.equals((Boolean)(item[2]));
            Long parentId = (!checkDeleted && item.length > 2) ? (Long) item[2] : null;
            Object extraInfo = extraInfoFunc != null ? extraInfoFunc.apply(item) : null;
//          Boolean deleted = ((Boolean)(item[2]));
            if (itemGood) {
                Map<String, String> translatedValues = translatorService.translateLabel(value);
                PossibleValue possibleValue = new PossibleValue(id, value, translatedValues, extraInfo);
                groupedValues.put(parentId, possibleValue);
            }
        }

        return convertToHierarchical(groupedValues);
    }

    private List<PossibleValue> convertToHierarchical(ListMultimap<Long, PossibleValue> groupedValues) {
        return convertToHierarchical(groupedValues.get(null), groupedValues);
    }

    private List<PossibleValue> convertToHierarchical(List<PossibleValue> flatValues,
            ListMultimap<Long, PossibleValue> groupedValues) {
        List<PossibleValue> hierarchicalValues = new ArrayList<>();
        for (PossibleValue possibleValue : flatValues) {
            Long id = (Long) possibleValue.getId();
            List<PossibleValue> children = convertToHierarchical(groupedValues.get(id), groupedValues);
            hierarchicalValues.add(possibleValue.withChildren(children));
        }
        return hierarchicalValues;
    }
    
    private Object getCategoryValueExtraInfo(Object[] item) {
        Integer index = ((Number) (item[CategoryValueExtraInfo.EXTRA_INFO_START_INDEX])).intValue();
        return new CategoryValueExtraInfo(index);
    }

    private Object getSectorExtraInfo(Object[] item) {
        Long parentSectorId = item.length > 2 ? (Long) item[PossibleValuesDAO.SECTOR_PARENT_ID_POS] : null;

        return new SectorExtraInfo(parentSectorId);
    }

    private Object getThemeExtraInfo(Object[] item) {
        Long parentProgramId = item.length > 2 ? (Long) item[PossibleValuesDAO.THEME_PARENT_ID_POS] : null;

        return new ProgramExtraInfo(parentProgramId);
    }
}
