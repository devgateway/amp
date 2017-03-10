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

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;

/**
 * AMP Activity Endpoints for Activity Import / Export
 * 
 * @author acartaleanu
 */
public class FieldsEnumerator {
	
	public static final Logger LOGGER = Logger.getLogger(PossibleValuesEnumerator.class);

	private boolean internalUse = false;

	private FieldInfoProvider fieldInfoProvider;

	private FMService fmService;

	private TranslatorService translatorService;

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
	}
	
	/**
	 * gets fields from the type of the field
	 * 
	 * @param field
	 * @param multilingual true if multilingual enabled
	 * @return a list of JsonBeans, each a description of @Interchangeable
	 *         fields in the definition of the field's class, or field's generic
	 *         type, if it's a collection
	 */
	private List<APIField> getChildrenOfField(Field field, Deque<Interchangeable> intchStack) {
		if (!InterchangeUtils.isCollection(field))
			return getAllAvailableFields(field.getType(), intchStack);
		else
			return getAllAvailableFields(InterchangeUtils.getGenericClass(field), intchStack);
	}
	
	/**
	 * describes a field in a complex JSON structure
	 * see the wiki for details, too many options to be listed here
	 * 
	 * @param field
	 * @return
	 */
	private APIField describeField(Field field, Deque<Interchangeable> intchStack) {
		Interchangeable interchangeable = intchStack.peek();

		APIField apiField = new APIField();
		apiField.setFieldName(InterchangeUtils.underscorify(interchangeable.fieldTitle()));
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
		

		apiField.setFieldLabel(InterchangeUtils.mapToBean(getLabelsForField(interchangeable.fieldTitle())));
		apiField.setRequired(InterchangeUtils.getRequiredValue(field, intchStack, fmService));
		apiField.setImportable(interchangeable.importable());
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
				if (!InterchangeUtils.hasMaxSizeValidatorEnabled(field, intchStack, fmService)
						&& interchangeable.multipleValues()) {
					apiField.setMultipleValues(true);
				} else {
					apiField.setMultipleValues(false);
				}
				
				if (InterchangeUtils.hasPercentageValidatorEnabled(field, intchStack, fmService)) {
					apiField.setPercentageConstraint(getPercentageConstraint(field, intchStack));
				}
				
				if (InterchangeUtils.hasUniqueValidatorEnabled(field, intchStack, fmService)) {
					apiField.setUniqueConstraint(getUniqueConstraint(field, intchStack));
				}
				
				if (InterchangeUtils.hasTreeCollectionValidatorEnabled(field, intchStack, fmService)) {
					apiField.setTreeCollectionConstraint(true);
				}
			}
			
			if (!interchangeable.pickIdOnly() && !InterchangeUtils.isAmpActivityVersion(field.getClass())) {
				List<APIField> children = getChildrenOfField(field, intchStack);
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
		return apiField;
	}
	
	public List<APIField> getAllAvailableFields(Class<?> clazz) {
		return getAllAvailableFields(clazz, new ArrayDeque<>()); // TODO fixme
	}

	/**
	 * Describes each @Interchangeable field of a class
	 * 
	 * @param clazz the class to be described
	 * @return
	 */
	private List<APIField> getAllAvailableFields(Class<?> clazz, Deque<Interchangeable> intchStack) {
		List<APIField> result = new ArrayList<>();
		//StopWatch.next("Descending into", false, clazz.getName());
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
			if (interchangeable == null || !internalUse && InterchangeUtils.isAmpActivityVersion(field.getType())) {
				continue;
			}
			intchStack.push(interchangeable);
			if (!InterchangeUtils.isCompositeField(field) || hasFieldDiscriminatorClass(field)) {
				if (fmService.isVisible(interchangeable.fmPath(), intchStack)) {
					APIField descr = describeField(field, intchStack);
					if (descr != null) {
						result.add(descr);
					}
				}
			} else {
				InterchangeableDiscriminator discriminator = field.getAnnotation(InterchangeableDiscriminator.class);
				Interchangeable[] settings = discriminator.settings();
				for (int i = 0; i < settings.length; i++) {
					String fmPath = settings[i].fmPath();
					if (fmService.isVisible(fmPath, intchStack)) {
						intchStack.push(settings[i]);
						APIField descr = describeField(field, intchStack);
						if (descr != null) {
							result.add(descr);
						}
						intchStack.pop();
					}
				}
			}
			intchStack.pop();
		}
		return result;
	}
	
	private static boolean hasFieldDiscriminatorClass(Field field) {
		try {
			return InterchangeUtils.getDiscriminatorClass(field) != null;
		} catch (ClassNotFoundException e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * Picks available translations for a string (supposedly field name)
	 * 
	 * @param fieldName the field name to be translated
	 * @return a map from the ISO2 code -> translation in said text
	 */
	private Map<String, String> getLabelsForField(String fieldName) {
		Map<String, String> translations = new HashMap<String, String>();
		try {
			Collection<Message> messages = translatorService.getAllTranslationOfBody(fieldName, DEFAULT_SITE_ID);
			for (Message m : messages) {
				translations.put(m.getLocale(), m.getMessage());
			}
			if (translations.isEmpty()) {
				translations.put("EN", fieldName);
			}
		} catch (WorkerException e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return translations;
	}
	
	/**
	 * 
	 * @param parentInterchangeable 
	 * 
	 * @param clazz the class to be described
	 * @return
	 */
	private String getPercentageConstraint(Field field, Deque<Interchangeable> intchStack) {
		Class<?> genericClass = InterchangeUtils.getGenericClass(field);
		Field[] fields = genericClass.getDeclaredFields();
		for (Field f : fields) {
			Interchangeable interchangeable = f.getAnnotation(Interchangeable.class);
			if (interchangeable != null && fmService.isVisible(interchangeable.fmPath(), intchStack)
					&& interchangeable.percentageConstraint()) {
				return InterchangeUtils.underscorify(interchangeable.fieldTitle());
			}
		}
		
		return null;
	}
	
	/**
	 * Describes each @Interchangeable field of a class
	 * @param parentInterchangeable 
	 * 
	 * @param clazz the class to be described
	 * @return
	 */
	private String getUniqueConstraint(Field field, Deque<Interchangeable> intchStack) {
		Class<?> genericClass = InterchangeUtils.getGenericClass(field);
		Field[] fields = genericClass.getDeclaredFields();
		for (Field f : fields) {
			Interchangeable interchangeable = f.getAnnotation(Interchangeable.class);
			if (interchangeable != null && fmService.isVisible(interchangeable.fmPath(), intchStack)
					&& interchangeable.uniqueConstraint()) {
				return InterchangeUtils.underscorify(interchangeable.fieldTitle());
			}
		}
		
		return null;
	}
	
}
