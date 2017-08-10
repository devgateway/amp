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

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.filters.AmpOfflineModeHolder;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.util.ProgramUtil;
import org.h2.util.StringUtils;

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
			.add(ActivityFieldsConstants.APPROVED_BY)
			.add(ActivityFieldsConstants.APPROVAL_STATUS)
			.add(ActivityFieldsConstants.APPROVAL_DATE)
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

		apiField.setFieldLabel(InterchangeUtils.mapToBean(getLabelsForField(interchangeable.fieldTitle())));
		apiField.setRequired(getRequiredValue(intchStack, fmService));
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
				if (!hasMaxSizeValidatorEnabled(field, intchStack)
						&& interchangeable.multipleValues()) {
					apiField.setMultipleValues(true);
				} else {
					apiField.setMultipleValues(false);
				}
				
				if (hasPercentageValidatorEnabled(intchStack)) {
					apiField.setPercentageConstraint(getPercentageConstraint(field, intchStack));
				}
				
				if (hasUniqueValidatorEnabled(intchStack)) {
					apiField.setUniqueConstraint(getUniqueConstraint(field, intchStack));
				}
				
				if (hasTreeCollectionValidatorEnabled(intchStack)) {
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

		apiField.setDiscriminator(interchangeable.discriminatorOption());

		return apiField;
	}

	public List<APIField> getAllAvailableFields() {
		return getAllAvailableFields(AmpActivityFields.class);
	}

	List<APIField> getAllAvailableFields(Class<?> clazz) {
		return getAllAvailableFields(clazz, new ArrayDeque<>());
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
			if (!InterchangeUtils.isCompositeField(field)) {
				if (isVisible(interchangeable.fmPath(), intchStack)) {
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
					if (isVisible(fmPath, intchStack)) {
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
			if (interchangeable != null && isVisible(interchangeable.fmPath(), intchStack)
					&& interchangeable.percentageConstraint()) {
				return InterchangeUtils.underscorify(interchangeable.fieldTitle());
			}
		}
		
		return null;
	}
	
	/**
	 * Describes each @Interchangeable field of a class
	 */
	private String getUniqueConstraint(Field field, Deque<Interchangeable> intchStack) {
		Class<?> genericClass = InterchangeUtils.getGenericClass(field);
		Field[] fields = genericClass.getDeclaredFields();
		for (Field f : fields) {
			Interchangeable interchangeable = f.getAnnotation(Interchangeable.class);
			if (interchangeable != null && isVisible(interchangeable.fmPath(), intchStack)
					&& interchangeable.uniqueConstraint()) {
				return InterchangeUtils.underscorify(interchangeable.fieldTitle());
			}
		}
		
		return null;
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


	public List<String> findFieldPaths(Predicate<Field> fieldFilter) {
		FieldNameCollectingVisitor visitor = new FieldNameCollectingVisitor(fieldFilter);
		visit(AmpActivityFields.class, visitor, new VisitorContext());
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
		private Deque<Interchangeable> interchangeableStack = new ArrayDeque<>();
	}

	// TODO how to reuse this logic?
	private void visit(Class<?> clazz, InterchangeVisitor visitor, VisitorContext context) {
		for (Field field : clazz.getDeclaredFields()) {
			Interchangeable ant = field.getAnnotation(Interchangeable.class);
			if (ant != null) {
				context.interchangeableStack.push(ant);
				if (!InterchangeUtils.isCompositeField(field)) {
					if (isVisible(ant.fmPath(), context.interchangeableStack)) {
						visit(field, InterchangeUtils.underscorify(ant.fieldTitle()), ant, visitor, context);
					}
				} else {
					InterchangeableDiscriminator antd = field.getAnnotation(InterchangeableDiscriminator.class);
					Interchangeable[] settings = antd.settings();
					for (Interchangeable ants : settings) {
						if (isVisible(ants.fmPath(), context.interchangeableStack)) {
							context.interchangeableStack.push(ants);
							visit(field, InterchangeUtils.underscorify(ants.fieldTitle()), ant, visitor, context);
							context.interchangeableStack.pop();
						}
					}
				}
				context.interchangeableStack.pop();
			}
		}
	}

	private void visit(Field field, String fieldName, Interchangeable ant, InterchangeVisitor visitor,
			VisitorContext context) {
		context.pathStack.push(InterchangeUtils.underscorify(fieldName));

		visitor.visit(field, fieldName, context);

		Class classOfField = InterchangeUtils.getClassOfField(field);
		if (!InterchangeUtils.isSimpleType(classOfField) && !ant.pickIdOnly()) {
			visit(classOfField, visitor, context);
		}

		context.pathStack.pop();
	}

	/**
	 * Gets the field required value.
	 *
	 * @param Field the field to get its required value
	 * @return String with Y|ND|N, where Y (yes) = always required, ND=for draft status=false,
	 * N (no) = not required. .
	 */
	private String getRequiredValue(Deque<Interchangeable> intchStack, FMService fmService) {
		Interchangeable fieldIntch = intchStack.peek();
		String requiredValue = ActivityEPConstants.FIELD_NOT_REQUIRED;
		String required = fieldIntch.required();

		if (required.equals(ActivityEPConstants.REQUIRED_ALWAYS)) {
			requiredValue = ActivityEPConstants.FIELD_ALWAYS_REQUIRED;
		} else if (required.equals(ActivityEPConstants.REQUIRED_ND)
				|| (!required.equals(ActivityEPConstants.REQUIRED_NONE) && isVisible(required, intchStack))
				|| (hasRequiredValidatorEnabled(intchStack))) {
			requiredValue = ActivityEPConstants.FIELD_NON_DRAFT_REQUIRED;
		}
		return requiredValue;
	}

	/**
	 * Determine if the field contains unique validator
	 * @param intchStack
	 * @return boolean if the field contains unique validator
	 */
	private boolean hasUniqueValidatorEnabled(Deque<Interchangeable> intchStack) {
		return hasValidatorEnabled(intchStack, ActivityEPConstants.UNIQUE_VALIDATOR_NAME);
	}

	/**
	 * Determine if the field contains tree collection validator
	 * @param intchStack
	 * @return boolean if the field contains tree collection validator
	 */
	private boolean hasTreeCollectionValidatorEnabled(Deque<Interchangeable> intchStack) {
		return hasValidatorEnabled(intchStack, ActivityEPConstants.TREE_COLLECTION_VALIDATOR_NAME);
	}

	/**
	 * Determine if the field contains maxsize validator
	 * @param intchStack
	 * @return boolean if the field contains maxsize validator
	 */
	private boolean hasMaxSizeValidatorEnabled(Field field, Deque<Interchangeable> intchStack) {
		if (AmpActivityProgram.class.equals(InterchangeUtils.getGenericClass(field))) {
			try {
				AmpActivityProgramSettings setting = ProgramUtil.getAmpActivityProgramSettings(
						intchStack.peek().discriminatorOption());
				return setting != null && !setting.isAllowMultiple();
			} catch (DgException e) {
				throw new RuntimeException(e);
			}
		} else {
			return hasValidatorEnabled(intchStack, ActivityEPConstants.MAX_SIZE_VALIDATOR_NAME);
		}
	}

	/**
	 * Determine if the field contains required validator
	 * @param intchStack
	 * @return boolean if the field contains required validator
	 */
	private boolean hasRequiredValidatorEnabled(Deque<Interchangeable> intchStack) {
		return hasValidatorEnabled(intchStack, ActivityEPConstants.MIN_SIZE_VALIDATOR_NAME);
	}

	/**
	 * Determine if the field contains percentage validator
	 * @param intchStack
	 * @return boolean if the field contains percentage validator
	 */
	private boolean hasPercentageValidatorEnabled(Deque<Interchangeable> intchStack) {
		return hasValidatorEnabled(intchStack, ActivityEPConstants.PERCENTAGE_VALIDATOR_NAME);
	}

	/**
	 * Determine if the field contains a certain validator
	 * @param intchStack
	 * @param validatorName the name of the validator (unique, maxSize, minSize, percentage, treeCollection)
	 * @return boolean if the field contains unique validator
	 */
	private boolean hasValidatorEnabled(Deque<Interchangeable> intchStack, String validatorName) {
		boolean isEnabled = false;
		Interchangeable interchangeable = intchStack.peek();
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
			isEnabled = isVisible(validatorFmPath, intchStack);
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

	private boolean isVisible(String fmPath,  Deque<Interchangeable> intchStack) {
		Interchangeable interchangeable = intchStack.peek();
		String fieldTitle = InterchangeUtils.underscorify(interchangeable.fieldTitle());

		if (!AmpOfflineModeHolder.isAmpOfflineMode() && isFieldIatiIdentifier(fieldTitle)) {
			return true;
		} else {
			return fmService.isVisible(fmPath, intchStack);
		}
	}
}
