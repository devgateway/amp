/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.FieldsDescriptor.TranslationType;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

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
	private boolean update  = false;
	private InputValidatorProcessor validator = new InputValidatorProcessor();
	private List<AmpContentTranslation> translations = new ArrayList<AmpContentTranslation>();
	private boolean isDraftFMEnabled;
	private boolean isMultilingual;
	private TranslationSettings trnSettings;
	private TeamMember teamMember;
	private User user;
	private String sourceURL;
	
	protected void init(JsonBean newJson, boolean update) {
		this.teamMember = (TeamMember) TLSUtils.getRequest().getAttribute(Constants.CURRENT_MEMBER);
		this.user = RequestUtils.getUser(TLSUtils.getRequest());
		this.sourceURL = RequestUtils.getSourceURL(TLSUtils.getRequest());
		this.update = update;
		this.newJson = newJson;
		this.isDraftFMEnabled = FMVisibility.isFmPathEnabled(SAVE_AS_DRAFT_PATH);
		this.isMultilingual = ContentTranslationUtil.multilingualIsEnabled();
		this.trnSettings = (TranslationSettings) TLSUtils.getRequest().getAttribute(EPConstants.TRANSLATIONS);
		if (trnSettings == null) {
			trnSettings = TranslationSettings.getDefault();
		}
	}

	/**
	 * Imports or Updates
	 * 
	 * @param newJson new activity configuration
	 * @param update  flags whether this is an import or an update request
	 * @return a list of API errors, that is empty if no error detected
	 */
	public List<ApiErrorMessage> importOrUpdate(JsonBean newJson, boolean update) {
		init(newJson, update);
		
		// retrieve fields definition for internal use
		List<JsonBean> fieldsDef = FieldsEnumerator.getAllAvailableFields(true);
		// get existing activity if this is an update request
		Long ampActivityId = update ? (Long) newJson.get(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME) : null;
		
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
			try {
				newActivity = ActivityVersionUtil.cloneActivity(oldActivity, TeamUtil.getCurrentAmpTeamMember());
			} catch (CloneNotSupportedException e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
		} else if (!update) {
			newActivity = new AmpActivityVersion(); 
		}
		
		newActivity = (AmpActivityVersion) validateAndImport(oldActivity, newActivity, fieldsDef, newJson, oldJson, ""); 
		if(newActivity != null) {
			// save new activity
			try {
				org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(
						newActivity, translations, TeamMemberUtil.getCurrentAmpTeamMember(TLSUtils.getRequest()), 
						newActivity.getDraft(), PersistenceManager.getRequestDBSession(), false, false);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
		
		return new ArrayList<ApiErrorMessage>(errors.values());
	}
	
	protected Object validateAndImport(Object oldParent, Object newParent, List<JsonBean> fieldsDef, 
			JsonBean newJsonParent, JsonBean oldJsonParent, String fieldPath) {
		for (JsonBean fieldDef : fieldsDef) {
			newParent = validateAndImport(oldParent, newParent, fieldDef, newJsonParent, oldJsonParent, fieldPath); 
		}
		return newParent;
	}
	
	protected Object validateAndImport(Object oldParent, Object newParent, JsonBean fieldDef, 
			JsonBean newJsonParent, JsonBean oldJsonParent, String fieldPath) {
		String fieldName = fieldDef.getString(ActivityEPConstants.FIELD_NAME);
		Object oldJsonValue = oldJsonParent == null ? null : oldJsonParent.get(fieldName);
		Object newJsonValue = newJsonParent == null ? null : newJsonParent.get(fieldName);
		
		// validate sub-elements first
		newParent = validateSubElements(fieldDef, newParent, oldParent, newJsonValue, oldJsonValue, fieldPath);
		// then validate current field itself
		boolean valid = validator.isValid(this, newJsonParent, oldJsonParent, fieldDef, 
				fieldPath + "~" + fieldName, errors);
		// and set new field only if all sub-elements are valid
		if (valid && newParent != null) {
			newParent = setNewField(newParent, fieldDef, newJsonParent, fieldPath);
		}
		return newParent;
	}
	
	protected Object validateSubElements(JsonBean fieldDef, Object newParent, Object oldParent, Object newJsonValue, 
			Object oldJsonValue, String fieldPath) {
		String fieldType = fieldDef.getString(ActivityEPConstants.FIELD_TYPE);
		/* 
		 * Sub-elements by default are valid when not provided. 
		 * Current field will be verified below and reported as invalid if sub-elements are mandatory and are not provided. 
		 */
		boolean validSubElements = true;
		
		// first validate all sub-elements
		List<JsonBean> childrenFields = (List<JsonBean>) fieldDef.get(ActivityEPConstants.CHILDREN);
		List<JsonBean> childrenNewValues = null;
		List<JsonBean> childrenOldValues = null;
		
		// identify children of the new field input
		if (newJsonValue != null && newJsonValue instanceof JsonBean) { 
			childrenNewValues = (List<JsonBean>) ((JsonBean) newJsonValue).get(ActivityEPConstants.CHILDREN);
		}
		// identify children of the old field input
		if (oldJsonValue != null && oldJsonValue instanceof JsonBean) { 
			childrenNewValues = (List<JsonBean>) ((JsonBean) oldJsonValue).get(ActivityEPConstants.CHILDREN);
		}
		// validate children
		if ((ActivityEPConstants.FIELD_TYPE_LIST.equals(fieldType) || childrenFields != null && childrenFields.size() > 0)
				&& childrenNewValues != null) {
			String actualFieldName = fieldDef.getString(ActivityEPConstants.FIELD_NAME_INTERNAL);
			Field newField = getField(newParent, actualFieldName);
			Field oldField = getField(oldParent, actualFieldName);
			Object newFiledValue = null;
			Object oldFiledValue = null;
			try {
				newFiledValue = newField == null ? null : newField.get(newParent);
				oldFiledValue = oldField == null ? null : oldField.get(oldParent);
				if (newParent != null && newFiledValue == null) {
					newFiledValue = newField.getType().newInstance();
					newField.set(newParent, newFiledValue);
				}
			} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
			
			Iterator<JsonBean> iterNew = childrenNewValues.iterator();
			while (iterNew.hasNext() && validSubElements) {
				JsonBean newChild = iterNew.next();
				JsonBean oldChild = getMatchedOldValue(newChild, childrenOldValues);
				JsonBean childFieldDef = getMatchedFieldDef(newChild, childrenFields);
				if (oldChild != null) {
					childrenOldValues.remove(oldChild);
				}
				newFiledValue = validateAndImport(newFiledValue, oldFiledValue, childFieldDef, newChild, oldChild, fieldPath);
				if (newFiledValue == null) {
					// validation failed, reset parent to stop config
					newParent = null;
				}
			}
		}
		return newParent;
	}
	
	/**
	 * Configures new value, no validation outside of this method scope, it must be verified before
	 * @param newParent
	 * @param field
	 * @param newJson
	 * @return 
	 */
	protected Object setNewField(Object newParent, JsonBean fieldDef, JsonBean newJsonParent, String fieldPath) {
		// by default importable = true
		boolean importable = !Boolean.FALSE.equals(fieldDef.get(ActivityEPConstants.IMPORTABLE));
		if (!importable) {
			// skip reconfiguration at this level if the field is not importable
			return newParent;
		}
		
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
			if (ActivityEPConstants.FIELD_TYPE_LIST.equals(fieldType)) {
				// TODO: 
			}
			
			if (objField != null) {
				try {
					objField.set(newParent, newValue);
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
	
	protected JsonBean getMatchedOldValue(JsonBean newValue, List<JsonBean> oldValues) {
		// TODO:
		return null;
	}
	
	protected JsonBean getMatchedFieldDef(JsonBean newValue, List<JsonBean> fieldDefs) {
		// TODO:
		return null;
	}
	
	protected boolean valueChanged(JsonBean newValue, JsonBean oldValue) {
		// TODO:
		return true;
	}
	
	protected Object getNewValue(Field field, Object parentObj, Object jsonValue, JsonBean fieldDef, String fieldPath) {
		Object value = null;
		if (InterchangeUtils.isCompositeField(field)) {
			// TODO:
		} else if (InterchangeableClassMapper.containsSimpleClass(field.getType())) {
			if (jsonValue == null)
				return null;
			try {
				if (Date.class.equals(field.getType())) {
					// TODO: custom for date
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
		} else {
			// this is a list
			// TODO:
		}
		return value;
	}
	
	protected String extractTranslationsOrSimpleValue(Field field, Object parentObj, Object jsonValue, JsonBean fieldDef) {
		TranslationType trnType = FieldsDescriptor.getTranslatableType(field);
		if (TranslationType.NONE == trnType) {
			return (String) jsonValue;
		}
		
		String value = null;
		if (TranslationType.STRING == trnType) {
			value = extractContentTranslation(field, parentObj, (Map<String, Object>) jsonValue);
		} else {
			value = extractTextTranslations(field, parentObj, (Map<String, Object>) jsonValue);
		}
		return value;
	}
		
	protected String extractContentTranslation(Field field, Object parentObj, Map<String, Object> trnJson) {
		String value = null;
		String currentLangValue = null;
		String anyLangValue = null;
		
		String objectClass = parentObj.getClass().getName();
		Long objId = (Long) ((Identifiable) parentObj).getIdentifier();
		List<AmpContentTranslation> trnList = isMultilingual ? 
				ContentTranslationUtil.loadFieldTranslations(objectClass, objId, field.getName()) : null;
		for (Entry<String, Object> trn : trnJson.entrySet()) {
			String langCode = trn.getKey();
			String translation = DgUtil.cleanHtmlTags((String) trn.getValue());
			if (isMultilingual) {
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
			key = "activity-import-" + System.currentTimeMillis();
		}
		for (Entry<String, Object> trn : trnJson.entrySet()) {
			String langCode = trn.getKey();
			String translation = DgUtil.cleanHtmlTags((String) trn.getValue());
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
					editor = DbUtil.createEditor(user, langCode, sourceURL, key, null, translation, "Activities API", TLSUtils.getRequest());
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
	
	Map<String, List<JsonBean>> possibleValuesCached = new HashMap<String, List<JsonBean>>();

	public List<JsonBean> getPossibleValuesForFieldCached(String fieldPath,
			Class<AmpActivityFields> clazz, Object object) {
		if (possibleValuesCached.containsKey(fieldPath))
			return possibleValuesCached.get(fieldPath);
		else possibleValuesCached.put(fieldPath, PossibleValuesEnumerator.getPossibleValuesForField(fieldPath, clazz, null));
		return null;
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
	 * @return the teamMember
	 */
	public TeamMember getTeamMember() {
		return teamMember;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the sourceURL
	 */
	public String getSourceURL() {
		return sourceURL;
	}

}
