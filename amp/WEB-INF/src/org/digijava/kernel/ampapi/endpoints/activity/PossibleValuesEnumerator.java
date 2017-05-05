package org.digijava.kernel.ampapi.endpoints.activity;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import javax.ws.rs.core.Response;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.valueproviders.GenericInterchangeableValueProvider;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.services.sync.model.SyncConstants.Entities;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.annotations.interchange.InterchangeableValueProvider;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableValue;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * AMP Activity Endpoint for Possible Values -- /activity/fields/:fieldName
 * and all the methods only it might have use of
 * 
 * @author acartaleanu
 */
public class PossibleValuesEnumerator {
	
	public static final Logger LOGGER = Logger.getLogger(PossibleValuesEnumerator.class);

	public static final PossibleValuesEnumerator INSTANCE = new PossibleValuesEnumerator(new AmpPossibleValuesDAO());

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
				.build();

	private PossibleValuesDAO possibleValuesDAO;

	public PossibleValuesEnumerator(PossibleValuesDAO possibleValuesDAO) {
		this.possibleValuesDAO = possibleValuesDAO;
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

	/**
	 * recursive method that gets possible values that can be held by a field
	 * @param longFieldName underscorified field name 
	 * @param clazz the class on which the method operates
	 * @param discriminatorOption recursive option to be passed down if there was a discriminator option higher up
	 * @return JSON object containing the possible values that can be held by the field
	 */
	public List<PossibleValue> getPossibleValuesForField(String longFieldName, Class<?> clazz, String discriminatorOption) {

		String fieldName = "";
		if (longFieldName.contains("~")) {
			/*
			 * we might have a field containing a discriminator description,
			 * but what we actually need is underneath -> obtain name of the field underneath
			 * */
			fieldName = longFieldName.substring(0, longFieldName.indexOf('~') );
			Field field = InterchangeUtils.getPotentiallyDiscriminatedField(clazz, fieldName);
			if (field == null) {
				throw newBadRequestException(ActivityErrors.FIELD_INVALID.withDetails(fieldName));
			}
			
			String configString = discriminatorOption == null? null : discriminatorOption;
			if (InterchangeUtils.isCompositeField(field)) {
				configString =  InterchangeUtils.getConfigValue(fieldName, field);
			}
			
			return getPossibleValuesForField(longFieldName.substring(longFieldName.indexOf('~') + 1),
					InterchangeUtils.getClassOfField(field), configString);
		} else {
			/*
			 * the last field might contain discriminated values
			 * if it is such a field, it's a special case for each class
			 * 
			 * */
			Field finalField =  InterchangeUtils.getPotentiallyDiscriminatedField(clazz, longFieldName);
			if (finalField == null) {
				throw newBadRequestException(ActivityErrors.FIELD_INVALID.withDetails(longFieldName));
			} else {
				String configString = discriminatorOption == null? null : discriminatorOption;
				if (InterchangeUtils.isCompositeField(finalField)) {
					configString =  InterchangeUtils.getConfigValue(longFieldName, finalField);
				}

				try {
					Class<? extends PossibleValuesProvider> providerClass =
							InterchangeUtils.getPossibleValuesProvider(finalField);
					if (providerClass != null) {
						return getPossibleValuesDirectly(providerClass);
					}
				} catch (Exception e) {
					throw newBadRequestException(ActivityErrors.DISCRIMINATOR_CLASS_METHOD_ERROR
							.withDetails(Objects.toString(e.getMessage())));
				}
				if (InterchangeUtils.isCompositeField(finalField) || configString != null) {
					return getPossibleValuesForComplexField(finalField, configString);
				}

				return getPossibleValuesForField(finalField);
			}
		}
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
	 * @throws SecurityException
	 * @throws ExceptionInInitializerError
	 */
	private List<PossibleValue> getPossibleValuesDirectly(
			Class<? extends PossibleValuesProvider> possibleValuesProviderClass)
			throws IllegalAccessException, InstantiationException {
		PossibleValuesProvider provider = possibleValuesProviderClass.newInstance();
		return provider.getPossibleValues();
	}
	
	/**
	 * Complex fields are discriminated fields -- when several underscorified paths 
	 * lead to the same Java field. This method gets possible values for such fields
	 * @param field
	 * @param configValue
	 * @return
	 */
	private List<PossibleValue> getPossibleValuesForComplexField(Field field, String configValue) {
		/*AmpActivitySector || AmpComponentFunding || AmpActivityProgram*/
		List<Object[]> items;
		Class<?> clazz = InterchangeUtils.getClassOfField(field);
		if (clazz.equals(AmpSector.class)) {
			items = possibleValuesDAO.getSectors(configValue);
		} else if (clazz.equals(AmpTheme.class)) {
			items = possibleValuesDAO.getThemes(configValue);
		} else if (clazz.equals(AmpCategoryValue.class)){
			return getPossibleCategoryValues(field, configValue);
		} else {
			//not a complex field, after all
			return getPossibleValuesForField(field);
		}
		return setProperties(items, false);
	}
	
	/**
	 * Generic method for obtaining possible values for most cases (without any fancy special cases)
	 * @param field
	 * @return
	 */
	
	private List<PossibleValue> getPossibleValuesForField(Field field) {
		if (!InterchangeUtils.isFieldEnumerable(field))
			return new ArrayList<>();
		Class<?> clazz = InterchangeUtils.getClassOfField(field);

		if (clazz.isAssignableFrom(AmpCategoryValue.class))
			return getPossibleCategoryValues(field, null);
		if (clazz.isAssignableFrom(AmpLocation.class))
			return getPossibleLocations();
		return getPossibleValuesGenericCase(clazz);
	}

	private <T> List<PossibleValue> getPossibleValuesGenericCase(Class<T> clazz) {
		Field[] fields = FieldUtils.getFieldsWithAnnotation(clazz, Interchangeable.class);
		String idFieldName = null;
		String valueFieldName = null;
		for (Field passField : fields) {
			Interchangeable ant = passField.getAnnotation(Interchangeable.class);
			if (ant.id()) {
				idFieldName = passField.getName();
			}
			if (ant.value()) {
				valueFieldName = passField.getName();
			}
		}
		InterchangeableValueProvider<T> valueProvider = null;
		if (valueFieldName != null) {
			valueProvider = new GenericInterchangeableValueProvider<>(valueFieldName);
		}
		if (valueProvider == null && clazz.isAnnotationPresent(InterchangeableValue.class)) {
			valueProvider = getInterchangeableValueProvider(clazz);
		}
		if (idFieldName == null || valueProvider == null) {
			String err = "Cannot provide possible values for " + clazz.getName()
					+ " since we need both 'id' and 'value' fields configured";
			LOGGER.error(err);
			return Collections.emptyList();
		}
		List<T> objectList = possibleValuesDAO.getGenericValues(clazz);
		String finalIdFieldName = idFieldName;
		InterchangeableValueProvider<T> finalValueProvider = valueProvider;
		return objectList.stream()
				.map(o -> getGenericPossibleValue(o, finalIdFieldName, finalValueProvider))
				.collect(toList());
	}

	private <T> InterchangeableValueProvider<T> getInterchangeableValueProvider(Class<T> clazz) {
		InterchangeableValue annotation = clazz.getAnnotation(InterchangeableValue.class);
		try {
            return annotation.value().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to instantiate value provider for " + clazz, e);
        }
	}

	private <T> PossibleValue getGenericPossibleValue(T object, String idProperty,
			InterchangeableValueProvider<T> valueProvider) {
		try {
			Long id = (Long) PropertyUtils.getProperty(object, idProperty);
			String value = valueProvider.getValue(object);
			return new PossibleValue(id, value);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Failed to extract possible value object from " + object, e);
		}
	}

	private List<PossibleValue> getPossibleLocations() {
		ListMultimap<Long, PossibleValue> groupedValues = ArrayListMultimap.create();

		Map<Long, Long> locCatToLocId = new HashMap<>();

		List<Object[]> possibleLocations = possibleValuesDAO.getPossibleLocations();
		for (Object[] item : possibleLocations) {
			Long locId = ((Number)(item[0])).longValue();
			Long locCatId = item[1] == null ? null : ((Number)(item[1])).longValue(); // FIXME should be non-null AMP-25735
			if (locCatId != null) {
				locCatToLocId.put(locCatId, locId);
			}
		}

		for (Object[] item : possibleLocations) {
			Long locId = ((Number)(item[0])).longValue();
			String locCatName = ((String)(item[2]));
			Long parentLocCatId = item[3] == null? null : ((Number)(item[3])).longValue();
			String parentLocCatName = item[4] == null? null : ((String)(item[4]));
			Long categoryValueId = item[5] == null? null : ((Number)(item[5])).longValue();
			String categoryValueName = item[6] == null? null : ((String)(item[6]));

			// FIXME remove this filter during AMP-25735
			if (locCatName == null && parentLocCatId == null && parentLocCatName == null
					&& categoryValueId == null && categoryValueName == null) {
				continue;
			}

			Long parentLocId = locCatToLocId.get(parentLocCatId);

			LocationExtraInfo extraInfo = new LocationExtraInfo(parentLocId, parentLocCatName,
					categoryValueId, categoryValueName);

			groupedValues.put(parentLocId, new PossibleValue(locId, locCatName, extraInfo));
		}
		return convertToHierarchical(groupedValues);
	}

	/**
	 * Gets possible values for the AmpCategoryValue class
	 * @param field
	 * @param discriminatorOption 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<PossibleValue> getPossibleCategoryValues(Field field, String discriminatorOption) {
		Interchangeable ant = field.getAnnotation(Interchangeable.class);
		if (StringUtils.isBlank(discriminatorOption)) {
			discriminatorOption = ant.discriminatorOption();
		}
		if (StringUtils.isNotBlank(discriminatorOption)) {
			List<Object[]> objColList = possibleValuesDAO.getCategoryValues(discriminatorOption);
			return setProperties(objColList, true);
		} else {
			LOGGER.error("discriminatorOption is not configured for CategoryValue [" + field.getName() + "]");
		}
		
		return Collections.emptyList();
	}
	
	private List<PossibleValue> setProperties(List<Object[]> objColList, boolean checkDeleted) {
		ListMultimap<Long, PossibleValue> groupedValues = ArrayListMultimap.create();
		for (Object[] item : objColList){
			Long id = ((Number)(item[0])).longValue();
			String value = ((String)(item[1]));
			boolean itemGood = !checkDeleted || Boolean.FALSE.equals((Boolean)(item[2]));
			Long parentId = (!checkDeleted && item.length > 2) ? (Long) item[2] : null;
//			Boolean deleted = ((Boolean)(item[2]));
			if (itemGood) {
				PossibleValue possibleValue = new PossibleValue(id, value);
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
}
