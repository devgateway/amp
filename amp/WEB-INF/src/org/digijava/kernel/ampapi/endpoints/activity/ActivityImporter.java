/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
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
	
	private AmpActivityVersion newActivity = null;
	private AmpActivityVersion oldActivity = null;
	private JsonBean oldJson = null;
	private JsonBean newJson = null;
	private Map<Integer, ApiErrorMessage> errors = new HashMap<Integer, ApiErrorMessage>();
	private boolean update  = false;
	private InputValidatorProcessor validator = new InputValidatorProcessor();
	private List<AmpContentTranslation> translations = new ArrayList<AmpContentTranslation>(); 
	
	/**
	 * Imports or Updates
	 * @param newJson new activity configuration
	 * @param update flags whether this is an import or an update request
	 * @return a list of API errors, that is empty if no error detected
	 */
	public List<ApiErrorMessage> importOrUpdate(JsonBean newJson, boolean update) {
		this.update = update;
		this.newJson = newJson;
		
		// retrieve fields definition
		List<JsonBean> fieldsDef = FieldsEnumerator.getAllAvailableFields();
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
		JsonBean oldFieldJson = oldJsonParent == null ? null : (JsonBean) oldJsonParent.get(fieldName);
		JsonBean newFieldJson = oldJsonParent == null ? null : (JsonBean) newJsonParent.get(fieldName);
		
		// validate sub-elements first
		boolean validSubElements = validateSubElements(fieldDef, newFieldJson, oldFieldJson, fieldPath);
		// then validate current field itself
		boolean valid = validator.isValid(oldActivity, newFieldJson, oldFieldJson, fieldDef, 
				fieldPath + "~" + fieldName, errors, update);
		valid = valid && validSubElements;
		// and set new field only if all sub-elements are valid
		if (valid && newParent != null) {
			setNewField(newParent, fieldDef, newJsonParent);
		}
		return valid;
	}
	
	protected boolean validateSubElements(JsonBean fieldDef, JsonBean newJsonParent, JsonBean oldJsonParent, 
			String fieldPath) {
		String fieldType = fieldDef.getString(ActivityEPConstants.FIELD_TYPE);
		/* 
		 * Sub-elements by default are valid when not provided. 
		 * Current field will be verified below and reported as invalid if sub-elements are mandatory and are not provided. 
		 */
		boolean validSubElements = true;
		
		// first validate all sub-elements
		List<JsonBean> childrenFields = (List<JsonBean>) fieldDef.get(ActivityEPConstants.CHILDREN);
		List<JsonBean> childrenNewValues = newJsonParent == null ? null : 
			new ArrayList<JsonBean>((List<JsonBean>) newJsonParent.get(ActivityEPConstants.CHILDREN));
		List<JsonBean> childrenOldValues = oldJsonParent == null ? null : 
			new ArrayList<JsonBean>((List<JsonBean>) oldJsonParent.get(ActivityEPConstants.CHILDREN));
		
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
		String fieldType = (String) field.get(ActivityEPConstants.FIELD_TYPE);
		// TODO: locate field, initialize intermediate objects
		Object fieldValue = newJsonParent.get(fieldName);
		Object oldValue = null;
		Object newValue = null;
		
		try {
			oldValue = newParent.getClass().getField("get" + fieldName.toUpperCase()).get(newParent);
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
		
		try {
			newParent.getClass().getField("set" + fieldName.toUpperCase()).set(newParent, newValue);
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
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
	 * @return the oldActivity
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
}
