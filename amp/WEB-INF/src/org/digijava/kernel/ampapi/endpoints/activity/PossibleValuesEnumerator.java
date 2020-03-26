package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.discriminators.CurrencyCommonPossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.AbstractPossibleValuesBaseProvider;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.CategoryValuePossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.DiscriminatedPossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.GenericPossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.ImplementationLocationPossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.services.sync.model.SyncConstants.Entities;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import com.google.common.collect.ImmutableMultimap;
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
                .putAll(AmpCategoryValueLocations.class, Entities.CATEGORY_VALUE_LOCATION,
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
    public Predicate<APIField> fieldsWithPossibleValues() {
        return fieldsDependingOn(getAllSyncEntities())
                .or(fieldWithPossibleValueProvider());
    }

    /**
     * Returns a predicate that matches all fields that have a possible values provider.
     * @return a predicate
     */
    private Predicate<APIField> fieldWithPossibleValueProvider() {
        return f -> f.getPossibleValuesProviderClass() != null;
    }

    /**
     * Returns a predicate that matches fields that depend on specific changelog entities.
     * @param syncEntities sync entities that have changed
     * @return field filter
     */
    public Predicate<APIField> fieldsDependingOn(Set<String> syncEntities) {
        List<Class<?>> entityClasses = getEntityClasses(syncEntities);
        return f -> entityClasses.stream()
                .anyMatch(f.getApiType().getType()::isAssignableFrom);
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

    public List<PossibleValue> getPossibleValuesForField(String longFieldName, List<APIField> apiFields) {
        PossibleValuesProvider pvp = getPossibleValuesProviderForField(longFieldName, null, apiFields);
        try {
            return pvp == null ? Collections.emptyList() : pvp.getPossibleValues(translatorService);
        } catch (Exception e) {
            throw InterchangeUtils.newServerErrorException("Failed to obtain possible values.", e);
        }
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
    public PossibleValuesProvider getPossibleValuesProviderForField(String longFieldName, String discriminatorOption,
            List<APIField> apiFields) {

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
            
            return getPossibleValuesProviderForField(longFieldName.substring(longFieldName.indexOf('~') + 1),
                    configString, apiField.getChildren());
        } else {
            APIField apiField = findField(apiFields, longFieldName);
            try {
                return getPossibleValuesProvider(apiField, discriminatorOption);
            } catch (Exception e) {
                throw InterchangeUtils.newServerErrorException("Failed to obtain possible values.", e);
            }
        }
    }

    private APIField findField(List<APIField> apiFields, String fieldName) {
        return apiFields.stream()
                .filter(f -> f.getFieldName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> newBadRequestException(ValidationErrors.FIELD_INVALID.withDetails(fieldName)));
    }

    private PossibleValuesProvider getPossibleValuesProvider(APIField apiField, String discriminatorValue)
            throws Exception {
        PossibleValuesProvider pvp;

        Class<? extends PossibleValuesProvider> providerClass = apiField.getPossibleValuesProviderClass();
        boolean isDiscriminatedProvider = providerClass != null
                && DiscriminatedPossibleValuesProvider.class.isAssignableFrom(providerClass);
        if (providerClass != null && !isDiscriminatedProvider) {
            pvp = providerClass.newInstance();
        } else if (isDiscriminatedProvider || apiField.getDiscriminatorValue() != null || discriminatorValue != null) {
            pvp = getPossibleValuesForComplexField(apiField, discriminatorValue);
        } else {
            pvp = getPossibleValuesForField(apiField);
        }

        if (pvp != null && AbstractPossibleValuesBaseProvider.class.isAssignableFrom(pvp.getClass())) {
            ((AbstractPossibleValuesBaseProvider) pvp).setPossibleValuesDAO(possibleValuesDAO);
        }

        return pvp;
    }

    private ApiRuntimeException newBadRequestException(ApiErrorMessage message) {
        return new ApiRuntimeException(Response.Status.BAD_REQUEST, ApiError.toError(message));
    }

    /**
     * Complex fields are discriminated fields -- when several underscorified paths 
     * lead to the same Java field. This method gets possible values for such fields
     * @param configValue
     * @return
     * @throws Exception 
     */
    private PossibleValuesProvider getPossibleValuesForComplexField(APIField apiField, String discriminatorValue)
            throws Exception {
        Class<?> clazz = apiField.getApiType().getType();
        if (apiField.getDiscriminatorValue() != null) {
            discriminatorValue = apiField.getDiscriminatorValue();
        }
        if (StringUtils.isBlank(discriminatorValue)) {
            throw InterchangeUtils.newServerErrorException("Field without discriminator value. " + apiField);
        }

        Class<? extends PossibleValuesProvider> pvpClass = apiField.getPossibleValuesProviderClass(); 
        PossibleValuesProvider pvp = null;
        if (pvpClass != null) {
            pvp = pvpClass.getDeclaredConstructor(String.class).newInstance(discriminatorValue);
        }

        if (pvp == null) {
            if (clazz.equals(AmpCategoryValue.class)) {
                if (discriminatorValue.equals(CategoryConstants.IMPLEMENTATION_LOCATION_KEY)) {
                    pvp = new ImplementationLocationPossibleValuesProvider();
                } else {
                    pvp = new CategoryValuePossibleValuesProvider(discriminatorValue);
                }
            } else if (clazz.equals(AmpClassificationConfiguration.class)) {
                final String configType = discriminatorValue;
                Supplier<List> possibleValuesSupplier = () -> Collections.singletonList(
                        possibleValuesDAO.getAmpClassificationConfiguration(configType));
                pvp = new GenericPossibleValuesProvider(clazz, possibleValuesSupplier);
            } else {
                // not a complex field, after all
                pvp = getPossibleValuesForField(apiField);
            }
        }
        return pvp;
    }
    
    /**
     * Generic method for obtaining possible values for most cases (without any fancy special cases)
     * @return 
     * @throws Exception 
     */
    private PossibleValuesProvider getPossibleValuesForField(APIField apiField) throws Exception {
        Class<?> clazz = apiField.getApiType().getType();
        if (clazz.isAssignableFrom(AmpCategoryValue.class))
            return getPossibleValuesForComplexField(apiField, null);
        if (clazz.isAssignableFrom(AmpOrganisation.class)) {
            return new GenericPossibleValuesProvider(clazz, () -> possibleValuesDAO.getOrganisations(),
                    (id) -> possibleValuesDAO.isOrganizationValid(id));
        }
        if (clazz.isAssignableFrom(AmpCurrency.class)) {
            return new CurrencyCommonPossibleValuesProvider();
        }

        if (InterchangeUtils.isEnumerable(apiField.getApiType().getType())) {
            return new GenericPossibleValuesProvider(clazz, () -> possibleValuesDAO.getGenericValues(clazz));
        }
        return null;
    }

}
