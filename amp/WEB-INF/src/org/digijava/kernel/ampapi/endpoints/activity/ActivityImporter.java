/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

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

	/**
	 * Imports or Updates
	 * 
	 * @param newJson new activity configuration
	 * @param update  flags whether this is an import or an update request
	 * @return a list of API errors, that is empty if no error detected
	 */
	public List<ApiErrorMessage> importOrUpdate(JsonBean newJson, boolean update) {
		this.update = update;
		this.newJson = newJson;
		this.isDraftFMEnabled = FMVisibility.isFmPathEnabled(SAVE_AS_DRAFT_PATH);
		
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
		
		if(validateAndImport(oldActivity, newActivity, fieldsDef, newJson, oldJson, "")) {
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
	
	protected boolean validateAndImport(Object oldParent, Object newParent, List<JsonBean> fieldsDef, 
			JsonBean newJsonParent, JsonBean oldJsonParent, String fieldPath) {
		boolean valid = true;
		for (JsonBean fieldDef : fieldsDef) {
			if (!validateAndImport(oldParent, newParent, fieldDef, newJsonParent, oldJsonParent, fieldPath)) {
				// disable further configurations
				newParent = null;
				valid = false;
			}
		}
		return valid;
	}
	
	protected boolean validateAndImport(Object oldParent, Object newParent, JsonBean fieldDef, 
			JsonBean newJsonParent, JsonBean oldJsonParent, String fieldPath) {
		String fieldName = fieldDef.getString(ActivityEPConstants.FIELD_NAME);
		Object oldField = oldJsonParent == null ? null : oldJsonParent.get(fieldName);
		Object newField = newJsonParent == null ? null : newJsonParent.get(fieldName);
		
		// validate sub-elements first
		boolean validSubElements = validateSubElements(fieldDef, newField, oldField, fieldPath);
		// then validate current field itself
		boolean valid = validator.isValid(this, newJsonParent, oldJsonParent, fieldDef, 
				fieldPath + "~" + fieldName, errors);
		valid = valid && validSubElements;
		// and set new field only if all sub-elements are valid
		if (valid && newParent != null) {
			setNewField(newParent, fieldDef, newJsonParent);
		}
		return valid;
	}
	
	protected boolean validateSubElements(JsonBean fieldDef, Object newField, Object oldField, String fieldPath) {
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
		if (newField != null && newField instanceof JsonBean) { 
			childrenNewValues = (List<JsonBean>) ((JsonBean) newField).get(ActivityEPConstants.CHILDREN);
		}
		// identify children of the old field input
		if (oldField != null && oldField instanceof JsonBean) { 
			childrenNewValues = (List<JsonBean>) ((JsonBean) oldField).get(ActivityEPConstants.CHILDREN);
		}
		// validate children
		if ((ActivityEPConstants.FIELD_TYPE_LIST.equals(fieldType) || childrenFields != null && childrenFields.size() > 0)
				&& childrenNewValues != null) {
			Iterator<JsonBean> iterNew = childrenNewValues.iterator();
			while (iterNew.hasNext() && validSubElements) {
				JsonBean newChild = iterNew.next();
				JsonBean oldChild = getMatchedOldValue(newChild, childrenOldValues);
				JsonBean childFieldDef = getMatchedFieldDef(newChild, childrenFields);
				if (oldChild != null) {
					childrenOldValues.remove(oldChild);
				}
				validSubElements = validateAndImport(null, null, childFieldDef, newChild, oldChild, fieldPath);
			}
		}
		return validSubElements;
	}
	
	/**
	 * Configures new value, no validation outside of this method scope, it must be verified before
	 * @param newParent
	 * @param field
	 * @param newJson
	 */
	protected void setNewField(Object newParent, JsonBean field, JsonBean newJsonParent) {
		
		// note again: only checks in scope of this method are done here
		
		String fieldName = (String) field.get(ActivityEPConstants.FIELD_NAME);
		String actualFieldName = (String) field.get(ActivityEPConstants.FIELD_NAME_INTERNAL);
		String fieldType = (String) field.get(ActivityEPConstants.FIELD_TYPE);
		// TODO: locate field, initialize intermediate objects
		Object fieldValue = newJsonParent.get(fieldName);
		Object oldValue = null;
		Object newValue = null;
		Field objField = null;
		
		try {
			Class<?> clazz = newParent.getClass();
			while (objField == null && !clazz.equals(Object.class)) {
				try {
					objField = clazz.getDeclaredField(actualFieldName);
					objField.setAccessible(true);
					oldValue = objField.get(newParent);
				} catch (NoSuchFieldException ex) {
					clazz = clazz.getSuperclass();
				}
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		
		if (fieldValue == null && oldValue == null) {
			// nothing to do
		} else {
			// TODO:
		}
		
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
		/* just look inside this method, why during an import we have to call it multiple times?
		return FMVisibility.isFmPathEnabled(SAVE_AS_DRAFT_PATH);
		*/
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
}
