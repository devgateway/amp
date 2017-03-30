package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.services.sync.model.SyncConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
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

	private static final Map<Class<?>, List<String>> ENTITY_CLASS_TO_SYNC_ENTITIES;

	static {
		Map<Class<?>, List<String>> map = new HashMap<>();
		map.put(AmpCategoryValue.class, Arrays.asList(SyncConstants.Entities.CATEGORY_VALUE));
		map.put(AmpLocation.class, Arrays.asList(SyncConstants.Entities.LOCATION,
				SyncConstants.Entities.CATEGORY_VALUE_LOCATION, SyncConstants.Entities.CATEGORY_VALUE));
		map.put(AmpSector.class, Arrays.asList(SyncConstants.Entities.SECTOR));
		map.put(AmpTheme.class, Arrays.asList(SyncConstants.Entities.THEME));
		map.put(AmpOrganisation.class, Arrays.asList(SyncConstants.Entities.ORGANISATION));
		map.put(AmpRole.class, Arrays.asList(SyncConstants.Entities.ROLE));
		map.put(AmpCurrency.class, Arrays.asList(SyncConstants.Entities.CURRENCY));
		map.put(AmpContact.class, Arrays.asList(SyncConstants.Entities.CONTACT));
		map.put(AmpActivityProgramSettings.class, Arrays.asList(SyncConstants.Entities.ACTIVITY_PROGRAM_SETTINGS));
		ENTITY_CLASS_TO_SYNC_ENTITIES = Collections.unmodifiableMap(map);
	}

	private PossibleValuesDAO possibleValuesDAO;

	public PossibleValuesEnumerator(PossibleValuesDAO possibleValuesDAO) {
		this.possibleValuesDAO = possibleValuesDAO;
	}

	/**
	 * Returns all sync entities that need to be monitored for possible value changes.
	 * @return a list of sync entities
	 */
	public Set<String> getAllSyncEntities() {
		return PossibleValuesEnumerator.ENTITY_CLASS_TO_SYNC_ENTITIES.values().stream()
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());
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
		PossibleValuesEnumerator.ENTITY_CLASS_TO_SYNC_ENTITIES.forEach((k, v) -> {
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
	public List<JsonBean> getPossibleValuesForField(String longFieldName, Class<?> clazz, String discriminatorOption) {

		String fieldName = "";
		if (longFieldName.contains("~")) {
			/*
			 * we might have a field containing a discriminator description,
			 * but what we actually need is underneath -> obtain name of the field underneath
			 * */
			fieldName = longFieldName.substring(0, longFieldName.indexOf('~') );
			Field field = InterchangeUtils.getPotentiallyDiscriminatedField(clazz, fieldName);
			if (field == null) {
				List<JsonBean> result = new ArrayList<JsonBean>();
				result.add(ApiError.toError(ActivityErrors.FIELD_INVALID.withDetails(fieldName)));
				return result;
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
				List<JsonBean> result = new ArrayList<JsonBean>();
				result.add(ApiError.toError(ActivityErrors.FIELD_INVALID.withDetails(longFieldName)));
				return result;
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
					List<JsonBean> result = new ArrayList<JsonBean>();
					result.add(ApiError.toError(ActivityErrors.DISCRIMINATOR_CLASS_METHOD_ERROR
							.withDetails(Objects.toString(e.getMessage()))));
					return result;
				}				
				if (InterchangeUtils.isCompositeField(finalField) || configString != null) {
					return getPossibleValuesForComplexField(finalField, configString);
				}

				return getPossibleValuesForField(finalField);
			}
		}
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
	private List<JsonBean> getPossibleValuesDirectly(
			Class<? extends PossibleValuesProvider> possibleValuesProviderClass)
			throws IllegalAccessException, InstantiationException {
		PossibleValuesProvider provider = possibleValuesProviderClass.newInstance();
		Map<String, ?> vals = provider.getPossibleValues();
		List<JsonBean> result = new ArrayList<>();
		for (Map.Entry<String, ?> entry : vals.entrySet()) {
			JsonBean bean = new JsonBean();
			bean.set("id", entry.getKey());
			bean.set("value", entry.getValue());
			result.add(bean);
		}
		return result;
	}
	
	/**
	 * Complex fields are discriminated fields -- when several underscorified paths 
	 * lead to the same Java field. This method gets possible values for such fields
	 * @param field
	 * @param configValue
	 * @return
	 */
	private List<JsonBean> getPossibleValuesForComplexField(Field field, String configValue) {
		List<JsonBean> result = new ArrayList<JsonBean>();
		/*AmpActivitySector || AmpComponentFunding || AmpActivityProgram*/
		List<Object[]> items;
		Class<?> clazz = InterchangeUtils.getClassOfField(field);
		if (clazz.equals(AmpSector.class)) {
			items = possibleValuesDAO.getSectors(configValue);
		} else if (clazz.equals(AmpTheme.class)) {
			items = possibleValuesDAO.getThemes(configValue);
		} else if (clazz.equals(AmpCategoryValue.class)){
			return getPossibleCategoryValues(field, configValue);
		} else if (clazz.equals(AmpFundingAmount.class)){
			return new ArrayList<JsonBean>();
		} else {
			//not a complex field, after all
			return getPossibleValuesForField(field);
		}
		result = setProperties(items, result, false);
		return result;
	}
	
	/**
	 * Generic method for obtaining possible values for most cases (without any fancy special cases)
	 * @param field
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	private List<JsonBean> getPossibleValuesForField(Field field) {
		if (!InterchangeUtils.isFieldEnumerable(field))
			return new ArrayList<JsonBean>();
		List<JsonBean> result = new ArrayList<JsonBean>();
		Class<?> clazz = InterchangeUtils.getClassOfField(field);
		
		if (clazz.isAssignableFrom(AmpCategoryValue.class))
			return getPossibleCategoryValues(field, null);
		if (clazz.isAssignableFrom(AmpLocation.class))
			return getPossibleLocations();
		Field[] fields = InterchangeUtils.getClassOfField(field).getDeclaredFields();
		String idFieldName = null;
		String valueFieldName = null;
		for (Field passField : fields) {
			Interchangeable ant = passField.getAnnotation(Interchangeable.class);
			if (ant != null) {
				if (ant.id())
					idFieldName = passField.getName();
				if (ant.value())
					valueFieldName = passField.getName();
			}
		}
		if (idFieldName == null || valueFieldName == null) {
			String err = "Cannot provide possible values for " + clazz.getName() + 
					" since we need both 'id' and 'value' fields configured";
			LOGGER.error(err);
			return result;
		}
		List<Object[]> objectList = possibleValuesDAO.getGenericValues(clazz, idFieldName, valueFieldName);
		result = setProperties(objectList, result, false);
		return result;
	}

	private List<JsonBean> getPossibleLocations() {
		List<JsonBean> result = new ArrayList<>();

		for (Object[] item : possibleValuesDAO.getPossibleLocations()) {
			Long id = ((Number)(item[0])).longValue();
			String value = ((String)(item[1]));
			Long parentLocationId = item[2] == null? null : ((Number)(item[2])).longValue();
			String parentLocationName = item[3] == null? null : ((String)(item[3]));
			Long categoryValueId = item[4] == null? null : ((Number)(item[4])).longValue();
			String categoryValueName = item[5] == null? null : ((String)(item[5]));

			// FIXME remove this filter during AMP-25735
			if (value == null && parentLocationId == null && parentLocationName == null
					&& categoryValueId == null && categoryValueName == null) {
				continue;
			}
			
			JsonBean bean = new JsonBean();
			bean.set("id", id);
			bean.set("value", value);
			JsonBean extraInfo = new JsonBean();
			extraInfo.set("parent_location_id", parentLocationId);
			extraInfo.set("parent_location_name", parentLocationName);
			extraInfo.set("implementation_level_id", categoryValueId);
			extraInfo.set("implementation_location_name", categoryValueName);
			bean.set("extra_info", extraInfo);
			result.add(bean);
		}
		return result;		
	}

	/**
	 * Gets possible values for the AmpCategoryValue class
	 * @param field
	 * @param discriminatorOption 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<JsonBean> getPossibleCategoryValues(Field field, String discriminatorOption) {
		List <JsonBean> result = new ArrayList<JsonBean>();
		Interchangeable ant = field.getAnnotation(Interchangeable.class);
		if (StringUtils.isBlank(discriminatorOption)) {
			discriminatorOption = ant.discriminatorOption();
		}
		if (StringUtils.isNotBlank(discriminatorOption)) {
			List<Object[]> objColList = possibleValuesDAO.getCategoryValues(discriminatorOption);

			result = setProperties(objColList, result, true);
			return result;
		} else {
			LOGGER.error("discriminatorOption is not configured for CategoryValue [" + field.getName() + "]");
		}
		
		return result; 
	}
	
	private List<JsonBean> setProperties(List<Object[]> objColList, List<JsonBean> result, boolean checkDeleted) {
		
		for (Object[] item : objColList){
			Long id = ((Number)(item[0])).longValue();
			String value = ((String)(item[1]));
			boolean itemGood = !checkDeleted || Boolean.FALSE.equals((Boolean)(item[2])); 
//			Boolean deleted = ((Boolean)(item[2]));
			if (itemGood) {
				JsonBean bean = new JsonBean();
				bean.set("id", id);
				bean.set("value", value);
				result.add(bean);
			}
			
		}
		return result;
	}


}
