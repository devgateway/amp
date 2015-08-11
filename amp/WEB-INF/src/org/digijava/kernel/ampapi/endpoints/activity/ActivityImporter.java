/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.util.ChangeType;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings.TranslationType;
import org.digijava.kernel.ampapi.endpoints.activity.utils.ActivityImporterHelper;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrgRoleBudget;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

import javax.servlet.http.HttpServletResponse;

/**
 * Imports a new activity or updates an existing one
 * 
 * @author Nadejda Mandrescu
 */
public class ActivityImporter {
	private static final Logger logger = Logger.getLogger(ActivityImporter.class);
	private static final String SAVE_AS_DRAFT_PATH = "/Activity Form/Save as Draft";
	private static final boolean ALLOW_SAVE_AS_DRAFT_SHIFT = true;
	
	private AmpActivityVersion newActivity = null;
	private AmpActivityVersion oldActivity = null;
	private JsonBean oldJson = null;
	private JsonBean newJson = null;
	private Map<Integer, ApiErrorMessage> errors = new HashMap<Integer, ApiErrorMessage>();
	protected Map<String, List<JsonBean>> possibleValuesCached = new HashMap<String, List<JsonBean>>();
	protected Map<String, String> possibleValuesQuery = new HashMap<String, String>();
	protected Map<Object, Field> activityFieldsForPostprocess = new HashMap<Object, Field>();
	private boolean update  = false;
	private boolean saveAsDraft = false;
	private InputValidatorProcessor validator = new InputValidatorProcessor();
	private List<AmpContentTranslation> translations = new ArrayList<AmpContentTranslation>();
	private boolean isDraftFMEnabled;
	private boolean isMultilingual;
	private TranslationSettings trnSettings;
	private AmpTeamMember currentMember;
	private String sourceURL;
    private String endpointContextPath;
    // latest activity id in case there was attempt to update older version of an activity
    private Long latestActivityId;

    protected void init(JsonBean newJson, boolean update, String endpointContextPath) {
		this.sourceURL = TLSUtils.getRequest().getRequestURL().toString();
		this.update = update;
        this.currentMember = TeamMemberUtil.getCurrentAmpTeamMember(TLSUtils.getRequest());
		this.newJson = newJson;
		this.isDraftFMEnabled = FMVisibility.isFmPathEnabled(SAVE_AS_DRAFT_PATH);
		this.isMultilingual = ContentTranslationUtil.multilingualIsEnabled();
		this.trnSettings = TranslationSettings.getCurrent();
        this.endpointContextPath = endpointContextPath;
	}

	/**
	 * Imports or Updates
	 * 
	 * @param newJson new activity configuration
	 * @param update  flags whether this is an import or an update request
	 * @return a list of API errors, that is empty if no error detected
	 */
	public List<ApiErrorMessage> importOrUpdate(JsonBean newJson, boolean update, String endpointContextPath) {
		init(newJson, update, endpointContextPath);
		
		// retrieve fields definition for internal use
		List<JsonBean> fieldsDef = FieldsEnumerator.getAllAvailableFields(true);
		// get existing activity if this is an update request
		Long ampActivityId = update ? Long.decode(newJson.get(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME).toString()) : null;
		// check if any error were already detected in upper layers 
		Map<Integer, ApiErrorMessage> existingErrors = (TreeMap<Integer, ApiErrorMessage>) newJson.get(ActivityEPConstants.INVALID);
		
		if (existingErrors != null && existingErrors.size() > 0) {
			errors.putAll(existingErrors);
		}
		
		if (ampActivityId != null) {
			try {
				oldActivity  = ActivityUtil.loadActivity(ampActivityId);
				oldJson = InterchangeUtils.getActivity(oldActivity, null);
			} catch (DgException e) {
				logger.error(e.getMessage());
				/*
				 * Disabling Exception in order to continue general validation of fields  
				throw new RuntimeException(e);
				*/
			}
		}
		
		// initialize new activity
		if (oldActivity != null) {
//			try {
//				newActivity = ActivityVersionUtil.cloneActivity(oldActivity, TeamUtil.getCurrentAmpTeamMember());
				newActivity = oldActivity;
//				newActivity.setAmpActivityGroup(oldActivity.getAmpActivityGroup());
//			} catch (CloneNotSupportedException e) {
//				logger.error(e.getMessage());
//				throw new RuntimeException(e);
//			}
		} else if (!update) {
			newActivity = new AmpActivityVersion();
		}
		
		Map<String, Object> newJsonParent = newJson.any();
		Map<String, Object> oldJsonParent = oldJson == null ? null : oldJson.any();
		oldJsonParent = null;
		
		newActivity = (AmpActivityVersion) validateAndImport(newActivity, oldActivity, fieldsDef, 
				newJsonParent, oldJsonParent, null);
		if (newActivity != null && errors.isEmpty()) {

			// save new activity
			try {
				prepareToSave();
				org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(
						newActivity, translations, currentMember,
						Boolean.TRUE.equals(newActivity.getDraft()), PersistenceManager.getRequestDBSession(), false, false);
				postProcess();
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
		} else {
			// undo any pending changes
			PersistenceManager.getSession().clear();
		}

        updateResponse(update);
		
		return new ArrayList<ApiErrorMessage>(errors.values());
	}
	
	protected Object validateAndImport(Object newParent, Object oldParent, List<JsonBean> fieldsDef, 
			Map<String, Object> newJsonParent, Map<String, Object> oldJsonParent, String fieldPath) {
		for (JsonBean fieldDef : fieldsDef) {
			newParent = validateAndImport(newParent, oldParent, fieldDef, newJsonParent, oldJsonParent, fieldPath); 
		}
		return newParent;
	}
	
	protected Object validateAndImport(Object newParent, Object oldParent, JsonBean fieldDef,
			Map<String, Object> newJsonParent, Map<String, Object> oldJsonParent, String fieldPath) {
		String fieldName = getFieldName(fieldDef, newJsonParent);
		String currentFieldPath = (fieldPath == null ? "" : fieldPath + "~") + fieldName;
		Object oldJsonValue = oldJsonParent == null ? null : oldJsonParent.get(fieldName);
		Object newJsonValue = newJsonParent == null ? null : newJsonParent.get(fieldName);
		
		// validate sub-elements first
		newParent = validateSubElements(fieldDef, newParent, oldParent, newJsonValue, oldJsonValue, currentFieldPath);
		// then validate current field itself
		boolean valid = validator.isValid(this, newJsonParent, oldJsonParent, fieldDef, 
				currentFieldPath, errors);
		// and set new field only if all sub-elements are valid
		if (valid && newParent != null) {
			newParent = setNewField(newParent, fieldDef, newJsonParent, currentFieldPath);
		} else if (!valid) {
			newParent = null;
		}
		return newParent;
	}
	
	protected String getFieldName(JsonBean fieldDef, Map<String, Object> newJsonParent) {
		if (fieldDef == null) {
			if (newJsonParent != null && newJsonParent.keySet().size() == 1) {
				return newJsonParent.keySet().iterator().next();
			}
		} else {
			return fieldDef.getString(ActivityEPConstants.FIELD_NAME);
		}
		return null;
	}
	
	protected Object validateSubElements(JsonBean fieldDef, Object newParent, Object oldParent, Object newJsonValue, 
			Object oldJsonValue, String fieldPath) {
		// simulate temporarily fieldDef
		fieldDef = fieldDef == null ? new JsonBean() : fieldDef;
		String fieldType = fieldDef.getString(ActivityEPConstants.FIELD_TYPE);
		/* 
		 * Sub-elements by default are valid when not provided. 
		 * Current field will be verified below and reported as invalid if sub-elements are mandatory and are not provided. 
		 */
		
		// skip children validation immediately if only ID is expected
		boolean idOnly = Boolean.TRUE.equals(fieldDef.get(ActivityEPConstants.ID_ONLY));
		if (idOnly)
			return newParent;
		
		boolean isList = ActivityEPConstants.FIELD_TYPE_LIST.equals(fieldType);
		
		// first validate all sub-elements
		List<JsonBean> childrenFields = (List<JsonBean>) fieldDef.get(ActivityEPConstants.CHILDREN);
		List<Map<String, Object>> childrenNewValues = getChildrenValues(newJsonValue, isList);
		List<Map<String, Object>> childrenOldValues = getChildrenValues(oldJsonValue, isList);
		
		// validate children, even if it is not a list -> to notify wrong entries
		if ((isList || childrenFields != null && childrenFields.size() > 0) && childrenNewValues != null) {
			String actualFieldName = fieldDef.getString(ActivityEPConstants.FIELD_NAME_INTERNAL);
			Field newField = getField(newParent, actualFieldName);
			Field oldField = getField(oldParent, actualFieldName);
			Object newFieldValue = null;
			Object oldFieldValue = null;
			Class<?> subElementClass = null;
			boolean isCollection = false;
			try {
				newFieldValue = newField == null ? null : newField.get(newParent);
				oldFieldValue = oldField == null ? null : oldField.get(oldParent);
				if (newParent != null && newFieldValue == null) {
					newFieldValue = getNewInstance(newParent, newField);
				}
				if (newFieldValue != null && Collection.class.isAssignableFrom(newFieldValue.getClass())) {
					isCollection = true;
					subElementClass = ActivityImporterHelper.getGenericsParameterClass(newField);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
			//newSubElement = validateAndImport(newSubElement, oldSubElement, childrenFields, newChild, oldChild, fieldPath);
			Iterator<Map<String, Object>> iterNew = childrenNewValues.iterator();
			while (iterNew.hasNext()) {
				Map<String, Object> newChild = iterNew.next();
				JsonBean childFieldDef = getMatchedFieldDef(newChild, childrenFields);
				Map<String, Object> oldChild = getMatchedOldValue(childFieldDef, childrenOldValues);
				
				if (oldChild != null) {
					childrenOldValues.remove(oldChild);
				}
				Object res = null;
				if (isCollection) {
					try {
						Object newSubElement = subElementClass.newInstance();
						// TODO: detect matching
						Object oldSubElement = null;
						res = validateAndImport(newSubElement, oldSubElement, childrenFields, newChild, oldChild, fieldPath);
					} catch (InstantiationException | IllegalAccessException e) {
						logger.error(e.getMessage());
						throw new RuntimeException(e);
					}
				} else {
					res = validateAndImport(newFieldValue, oldFieldValue, childFieldDef, newChild, oldChild, fieldPath);
				}
				
				if (res == null) {
					// validation failed, reset parent to stop config
					newParent = null;
				} else if (newParent != null && isCollection) {
					// actual links will be updated
					((Collection) newFieldValue).add(res);
				}
			}
			// TODO: we also need to validate other children, some can be mandatory
		}
		return newParent;
	}
	
	private List<Map<String, Object>> getChildrenValues(Object jsonValue, boolean isList) {
		if (jsonValue != null) {
			if (jsonValue instanceof List) { 
				return (List<Map<String, Object>>) jsonValue;
			} else if (isList && jsonValue instanceof Map) {
				List<Map<String, Object>> jsonValues = new ArrayList<Map<String, Object>>();
				jsonValues.add((Map<String, Object>) jsonValue);
//				for (final Map.Entry<String, Object> entry : ((Map<String, Object>) jsonValue).entrySet()) {
//					jsonValues.add(new HashMap<String, Object> () {{put(entry.getKey(), entry.getValue());}});
//				}
				return jsonValues;
			}
		}
		return null;
	}
	
	protected Object getNewInstance(Object parent, Field field) {
		Object fieldValue = null;
		try {
			if (Set.class.isAssignableFrom(field.getType())) {
				fieldValue = new HashSet<Object>();
			} else if (List.class.isAssignableFrom(field.getType())) {
				fieldValue = new ArrayList<Object>();
			} else if (Collection.class.isAssignableFrom(field.getType())) {
				fieldValue = new ArrayList<Object>();
			} else {
				fieldValue = field.getType().newInstance();
			}
			field.set(parent, fieldValue);
		} catch(InstantiationException | IllegalAccessException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return fieldValue;
	}
	
	protected void addActivityFieldForPostprocessing(Field field, Object obj) {
		activityFieldsForPostprocess.put(obj, field);
	}
	
	
	/**
	 * Configures new value, no validation outside of this method scope, it must be verified before
	 * @param newParent
	 * @param field
	 * @param newJson
	 * @return 
	 */
	protected Object setNewField(Object newParent, JsonBean fieldDef, Map<String, Object> newJsonParent, 
			String fieldPath) {
		boolean importable = Boolean.TRUE.equals(fieldDef.get(ActivityEPConstants.IMPORTABLE));
		
		// note again: only checks in scope of this method are done here
		
		String fieldName = (String) fieldDef.get(ActivityEPConstants.FIELD_NAME);
		String actualFieldName = (String) fieldDef.get(ActivityEPConstants.FIELD_NAME_INTERNAL);
		String fieldType = (String) fieldDef.get(ActivityEPConstants.FIELD_TYPE);
		Object fieldValue = newJsonParent.get(fieldName);
		Field objField = getField(newParent, actualFieldName);
		if (objField == null) {
			// cannot set
			logger.error("Actual Field not found: " + actualFieldName + ", fieldPaht: " + fieldPath);
			return null;
		}
		
		if (!importable) {
			if (InterchangeUtils.isAmpActivityVersion(objField.getType())) {
//				addActivityFieldForPostprocessing(objField, newParent);
				try {
					objField.set(newParent, this.getNewActivity());
				} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
					logger.error(e.getMessage());
					throw new RuntimeException(e);
				}
			}
			// skip reconfiguration at this level if the field is not importable
			return newParent;
		}
		
		Object oldValue;
		try {
			oldValue = objField.get(newParent);
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			logger.error(e1.getMessage());
			throw new RuntimeException(e1);
		}
		Object newValue = getNewValue(objField, newParent, fieldValue, fieldDef, fieldPath);
		
		if (newValue == null && oldValue == null || newValue != null && newValue.equals(oldValue)) {
			// nothing to do
		} else {
			if (objField != null) {
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
		return newParent;
	}
	
	protected Field getField(Object parent, String actualFieldName) {
		if (parent == null) {
			return null;
		}
		Field field = null;
		try {
			Class<?> clazz = parent.getClass();
			while (field == null && !clazz.equals(Object.class)) {
				try {
					field = clazz.getDeclaredField(actualFieldName);
					field.setAccessible(true);
				} catch (NoSuchFieldException ex) {
					clazz = clazz.getSuperclass();
				}
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return field;
	}
	
	protected Map<String, Object> getMatchedOldValue(JsonBean childDef, List<Map<String, Object>> oldValues) {
		if (childDef != null && oldValues != null && oldValues.size() > 0) {
			String fieldName = (String) childDef.get(ActivityEPConstants.FIELD_NAME);
			if (StringUtils.isNotBlank(fieldName)) {
				for (Map<String, Object> oldValue : oldValues) {
					if (oldValue.containsKey(fieldName)) {
						return oldValue;
					}
				}
			}
		}
		
		return null;
	}
	
	protected JsonBean getMatchedFieldDef(Map<String, Object> newValue, List<JsonBean> fieldDefs) {
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
					for (JsonBean childDef : fieldDefs) {
						if (fieldName.equals(childDef.get(ActivityEPConstants.FIELD_NAME))) {
							return childDef;
						}
					}
				}
			}
		}
		return null;
	}
	
	protected boolean valueChanged(JsonBean newValue, JsonBean oldValue) {
		// TODO:
		return true;
	}
	
	protected Object getObjectReferencedById(Class<?> objectType, Long objectId) {
		if (Collection.class.isAssignableFrom(objectType))
			throw new RuntimeException("Can't handle a collection of ID-linked objects yet!");
		return InterchangeUtils.getObjectById(objectType, objectId);
	}
	
	protected Object getNewValue(Field field, Object parentObj, Object jsonValue, JsonBean fieldDef, String fieldPath) {
		boolean isCollection = Collection.class.isAssignableFrom(field.getType());
		if (jsonValue == null && !isCollection)
			return null;
		Object value = null;
		String fieldType = (String) fieldDef.get(ActivityEPConstants.FIELD_TYPE);
		fieldPath = fieldPath.substring(1);
		List<JsonBean> allowedValues = getPossibleValuesForFieldCached(fieldPath, AmpActivityFields.class, null);
		boolean idOnly = Boolean.TRUE.equals(fieldDef.get(ActivityEPConstants.ID_ONLY)); 
		if (!isCollection && idOnly) {
			//Number->String->Long. 
			//if anyone has a better idea how to make this conversion painless, please ping me. @acartaleanu
			
			InterchangeableDiscriminator discr = field.getAnnotation(InterchangeableDiscriminator.class);
			if (discr != null && discr.discriminatorClass().length() > 0) {
				try {
					@SuppressWarnings("unchecked")
					Class<FieldsDiscriminator> discrClass = (Class<FieldsDiscriminator>) Class.forName(discr.discriminatorClass());
					FieldsDiscriminator disc = discrClass.newInstance();
					return disc.toAmpFormat(jsonValue);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					throw new RuntimeException("Cannot instantiate discriminator class " + discr.discriminatorClass());
				}				
			}
			return getObjectReferencedById(field.getType(), Long.valueOf(jsonValue.toString()));
		}
		
		if (Collection.class.isAssignableFrom(field.getType())) {
			try {
				value = field.get(parentObj);
				Collection col = (Collection) value;
				if (col == null) {
					col = (Collection) getNewInstance(parentObj, field);
				}
				if (idOnly && jsonValue != null) {
					Class<?> objectType = ActivityImporterHelper.getGenericsParameterClass(field);
					try {
						Object res = getObjectReferencedById(objectType, Long.valueOf(jsonValue.toString()));
						col.add(res);
					} catch (IllegalArgumentException e) {
						logger.error(e.getMessage());
						throw new RuntimeException(e);
					}
					
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
		} else if (InterchangeableClassMapper.SIMPLE_TYPES.contains(fieldType)) {
			if (jsonValue == null)
				return null;
			try {
				if (Date.class.equals(field.getType())) {
					value = InterchangeUtils.parseISO8601Date((String) jsonValue);
				} else if (String.class.equals(field.getType())) {
					// check if this is a translatable that expects multiple entries
					value = extractTranslationsOrSimpleValue(field, parentObj, jsonValue, fieldDef);
				} else {
					// a valueOf should work
					Method valueOf = field.getType().getDeclaredMethod("valueOf", String.class);
					
					value = valueOf.invoke(field.getType(), String.valueOf(jsonValue));
				}
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException 
					| InvocationTargetException e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
		} else if (allowedValues != null && allowedValues.size() > 0 && fieldDef != null){
			// => this is an object => it has children elements
			if (fieldDef.get(ActivityEPConstants.CHILDREN) != null) {
				for (JsonBean childDef : (List<JsonBean>) fieldDef.get(ActivityEPConstants.CHILDREN)) {
					if (Boolean.TRUE.equals(childDef.get(ActivityEPConstants.ID))) {
						Long id = ((Integer) ((Map<String, Object>) jsonValue).get(childDef.getString(ActivityEPConstants.FIELD_NAME))).longValue();
						value = InterchangeUtils.getObjectById(field.getType(), id);
						break;
					}
				}
			} else {
				// TODO:
			}
		}
		return value;
	}
	
	protected String extractTranslationsOrSimpleValue(Field field, Object parentObj, Object jsonValue, JsonBean fieldDef) {
		TranslationType trnType = trnSettings.getTranslatableType(field);
		if (TranslationType.NONE == trnType) {
			return (String) jsonValue;
		}
		
		String value = null;
		if (TranslationType.STRING == trnType) {
			value = extractContentTranslation(field, parentObj, (Map<String, Object>) jsonValue);
		} else {
			Map<String, Object> editorText = null;
			if (trnSettings.isMultilingual()) {
				editorText = (Map<String, Object>) jsonValue;
			} else {
				// simulate the lang-value map, since dg_editor is still stored per language
				editorText = new HashMap<String, Object>();
				editorText.put(trnSettings.getDefaultLangCode(), jsonValue);
			}
			value = extractTextTranslations(field, parentObj, editorText);
		}
		return value;
	}
		
	protected String extractContentTranslation(Field field, Object parentObj, Map<String, Object> trnJson) {
		String value = null;
		String currentLangValue = null;
		String anyLangValue = null;
		
		String objectClass = parentObj.getClass().getName();
		Long objId = (Long) ((Identifiable) parentObj).getIdentifier();
		List<AmpContentTranslation> trnList = ContentTranslationUtil.loadFieldTranslations(objectClass, objId, field.getName());
		if (objId == null) {
			objId = (long) System.identityHashCode(parentObj);
		}
		for (Entry<String, Object> trn : trnJson.entrySet()) {
			String langCode = trn.getKey();
			String translation = DgUtil.cleanHtmlTags((String) trn.getValue());
			AmpContentTranslation act = null;
			for (AmpContentTranslation existingAct : trnList) {
				if (langCode.equalsIgnoreCase(existingAct.getLocale())) {
					act = existingAct;
					break;
				}
			}
			// if translation to be removed
			if (translation == null) {
				trnList.remove(act);
			} else if (act == null) {
				act = new AmpContentTranslation(objectClass, objId, field.getName(), langCode, translation);
				trnList.add(act);
			} else {
				act.setTranslation(translation);
			}
			if (trnSettings.isDefaultLanguage(langCode)) {
				// set default language value as well
				value = translation;
			}
			if (anyLangValue == null) {
				anyLangValue = translation;
			}
			if (trnSettings.getCurrentLangCode().equals(langCode)) {
				currentLangValue = translation;
			}
		}
		// if default language still not set, let's determine it
		if (value == null) {
			value = currentLangValue != null ? currentLangValue : anyLangValue;
		}
		if (isMultilingual)
			translations.addAll(trnList);
		return value;
	}
	
	protected String extractTextTranslations(Field field, Object parentObj, Map<String, Object> trnJson) {
		String key = null;
		if (update) { // all editor keys must exist before
			try {
				key = (String) field.get(parentObj);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
		if (key == null) { // init it in any case
			key = getEditorKey(field.getName());
		}
		for (Entry<String, Object> trn : trnJson.entrySet()) {
			String langCode = trn.getKey();
			// AMP-20884: no cleanup so far DgUtil.cleanHtmlTags((String) trn.getValue());
			String translation = (String) trn.getValue();
			Editor editor;
			try {
				editor = DbUtil.getEditor(key, langCode);
				if (translation == null) {
					// remove existing translations
					if (editor != null) {
						DbUtil.deleteEditor(editor);
					}
				} else if (editor == null) {
					// create new
					editor = DbUtil.createEditor(currentMember.getUser(), langCode, sourceURL, key, null, translation,
                            "Activities API", TLSUtils.getRequest());
					DbUtil.saveEditor(editor);
				} else if (!editor.getBody().equals(translation)) {
					// update existing if needed
					editor.setBody(translation);
					DbUtil.updateEditor(editor);
				}
			} catch (EditorException e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
		return key;
	}
	
	private String getEditorKey(String fieldName) {
		// must start with "aim-" since it is expected by AF like this...
		return "aim-import-" + fieldName + "-" + System.currentTimeMillis();
	}
	
	protected void initEditor(Field field) {
		try {
			String currentValue = (String) field.get(newActivity);
			if (currentValue == null) {
				currentValue = getEditorKey(field.getName());
				field.setAccessible(true);
				field.set(newActivity, currentValue);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	protected void prepareToSave() {
        newActivity.setLastImportedAt(new Date());
        newActivity.setLastImportedBy(currentMember);

		newActivity.setChangeType(ChangeType.IMPORT.name());
		// configure draft status on import only, since on update we'll change to draft based on RequiredValidator
		if (!update) {
			newActivity.setDraft(isDraftFMEnabled);
		}
		initDefaults();
	}
	
	protected void initDefaults() {
		for (Field field : AmpActivityFields.class.getFields()) {
			if (InterchangeUtils.isVersionableTextField(field)) {
				initEditor(field);
			}
		}
		initOrgRoles();
		initSectors();
		initLocations();
		initFundings();
        initContacts();
        postprocessActivityReferences();
        updatePPCAmount();
		//initActivityReferences(newActivity, ActivityImporterHelper.getActivityRefPathsSet()); //ActivityImporterHelper.ACTIVITY_REFERENCES);
	}
	

	protected void postprocessActivityReferences() {
		for (Map.Entry<Object, Field> entry : activityFieldsForPostprocess.entrySet()) {
			Field field = entry.getValue();
			Object obj = entry.getKey();
			try {
				field.set(obj, this.newActivity);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
	}
	
	protected void initSectors() {
		if (newActivity.getSectors() == null) {
			newActivity.setSectors(new HashSet<AmpActivitySector>());
		} else if (newActivity.getSectors().size() > 0) {
			
			Map<Long, AmpClassificationConfiguration> foundClassifications = new TreeMap<Long, AmpClassificationConfiguration>();
			for(AmpActivitySector acs : newActivity.getSectors()) {
				acs.setActivityId(newActivity);
				if (acs.getClassificationConfig() == null) {
					Long ampSecSchemeId = acs.getSectorId().getAmpSecSchemeId().getAmpSecSchemeId();
					if (!foundClassifications.containsKey(ampSecSchemeId)) {
						foundClassifications.put(ampSecSchemeId, SectorUtil.getClassificationConfigBySectorSchemeId(ampSecSchemeId));
					}
					acs.setClassificationConfig(foundClassifications.get(ampSecSchemeId));
				}
			}
		}
	}
	
	protected void initFundings() {
		if (newActivity.getFunding() == null) {
			newActivity.setFunding(new HashSet<AmpFunding>());
        } else {
        	// TODO:
        }
	}
	
	protected void initOrgRoles() {
		if (newActivity.getOrgrole() == null) {
        	newActivity.setOrgrole(new HashSet<AmpOrgRole>());
        } else {
        	for (AmpOrgRole aor : newActivity.getOrgrole()) {
        		//set budgets, or we'll have errors on several entities pointing to the same set
        		if (aor.getBudgets() != null) {
        			Set<AmpOrgRoleBudget> aorbSet = new HashSet<AmpOrgRoleBudget>();
        			aorbSet.addAll(aor.getBudgets());
	        		aor.setBudgets(aorbSet);
        		}
        	}
        }
	}
	
	protected void initLocations() {
		if (newActivity.getLocations() == null) {
        	newActivity.setLocations(new HashSet<AmpActivityLocation>());
        } else {
        	// TODO:
        }
	}
	
	protected void initCategories() {
		if (newActivity.getCategories() == null) {
			newActivity.setCategories(new HashSet<AmpCategoryValue>());
		} else {
			// TODO:
		}
	}
	
	protected void initContacts() {
		if (newActivity.getActivityContacts() == null) {
        	newActivity.setActivityContacts(new HashSet<AmpActivityContact>());
        } else {
        	// TODO:
        }
	}

    /**
     * Updates response header and status based on activity validation results
     *
     * @param update - flag indicating activity create/update operation
     */
    private void updateResponse(boolean update) {
        String locationUrl = endpointContextPath + "/";

        if (update) {
            if (errors == null || errors.isEmpty() && newActivity != null) {
                /** update http status to SC_OK (activity has been successfully updated)
                 * EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_OK);
                 * The 200 status is sent by default
                 */
                locationUrl += newActivity.getAmpActivityId();
            } else if (errors.containsKey(ActivityErrors.UPDATE_ID_IS_OLD.id)) {
                // update http status to SC_CONFLICT (old version was sent for update)
                EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_CONFLICT);
                locationUrl += this.latestActivityId;
            } else {
                // any other error occurred during the update
                locationUrl = null;
            }
        } else {
            if (newActivity != null) {
                // update http status to SC_CREATED (activity has been created)
                EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_CREATED);
                locationUrl += newActivity.getAmpActivityId();
            } else {
                // if activity is not created, then we cannot provide an URL
                locationUrl = null;
            }
        }

        // configure Header in the response
        if (locationUrl != null) {
            EndpointUtils.addResponseHeaderMarker("Location", locationUrl);
        }
    }


	protected void updatePPCAmount() {
		boolean isAnnualBudget = FMVisibility.isFmPathEnabled("/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost");

		if (isAnnualBudget && newActivity.getAnnualProjectBudgets() != null) {
			double funAmount = 0d;
        	for(AmpAnnualProjectBudget apb : newActivity.getAnnualProjectBudgets()) {
        		funAmount += InterchangeUtils.doPPCCalculations(apb, newActivity.getCurrencyCode());
        	}
        	
        	newActivity.setFunAmount(funAmount / FeaturesUtil.getAmountMultiplier());
        }
	}
	
	protected void postProcess() {
		LuceneUtil.addUpdateActivity(TLSUtils.getRequest().getServletContext().getRealPath("/"), update,
        		TLSUtils.getSite(), Locale.forLanguageTag(trnSettings.getDefaultLangCode()), newActivity, oldActivity);
	}

	/**
	 * @return the newActivity
	 */
	public AmpActivityVersion getNewActivity() {
		return newActivity;
	}

	/**
	 * @return the oldActivity or null if none found
	 */
	public AmpActivityVersion getOldActivity() {
		return oldActivity;
	}

	/**
	 * @return the oldJson
	 */
	public JsonBean getOldJson() {
		return oldJson;
	}

	/**
	 * @return the newJson
	 */
	public JsonBean getNewJson() {
		return newJson;
	}

	/**
	 * @return the errors
	 */
	public Map<Integer, ApiErrorMessage> getErrors() {
		return errors;
	}

	/**
	 * @return true if Save as Draft is enabled in FM
	 */
	public boolean isDraftFMEnabled() {
		return isDraftFMEnabled;
	}

	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @return the translations
	 */
	public List<AmpContentTranslation> getTranslations() {
		return translations;
	}
	
	/**
	 * Defines if changing the Saving process from "Save" to "Save as draft" is allowed or not.
	 * @return true if it is allowed, false otherwise
	 */
	public boolean getAllowSaveAsDraftShift () {
		return ALLOW_SAVE_AS_DRAFT_SHIFT;
	}
	
	// what is object for?
	public List<JsonBean> getPossibleValuesForFieldCached(String fieldPath, 
			Class<AmpActivityFields> clazz, Object object) {
		if (!possibleValuesCached.containsKey(fieldPath)) {
			possibleValuesCached.put(fieldPath, PossibleValuesEnumerator.getPossibleValuesForField(fieldPath, clazz, null));
		}
		return possibleValuesCached.get(fieldPath);
	}
	
	/**
	 * @return the isMultilingual
	 */
	public boolean isMultilingual() {
		return isMultilingual;
	}

	/**
	 * @return the trnSettings
	 */
	public TranslationSettings getTrnSettings() {
		return trnSettings;
	}

	/**
	 * @return the sourceURL
	 */
	public String getSourceURL() {
		return sourceURL;
	}

	public boolean isSaveAsDraft() {
		return saveAsDraft;
	}

	public void setSaveAsDraft(boolean saveAsDraft) {
		this.saveAsDraft = saveAsDraft;
	}

    public void setLatestActivityId(Long latestActivityId) {
        this.latestActivityId = latestActivityId;
    }


}
