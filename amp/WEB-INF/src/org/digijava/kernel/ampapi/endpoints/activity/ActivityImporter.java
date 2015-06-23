/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.TeamUtil;

/**
 * Imports a new activity or updates an existing one
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
		List<JsonBean> fieldsDef = InterchangeUtils.getAllAvailableFields();
		// get existing activity if this is an update request
		Long ampActivityId = update ? (Long) newJson.get(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME) : null;
		oldJson = ampActivityId == null ? null : InterchangeUtils.getActivity(ampActivityId);
		
		if (ampActivityId != null) {
			try {
				oldActivity  = ActivityUtil.loadActivity(ampActivityId);
			} catch (DgException e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
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
		
		newActivity = (AmpActivityVersion) validateAndImport(oldActivity, newActivity, fieldsDef, newJson, oldJson);
		
		return new ArrayList<ApiErrorMessage>(errors.values());
	}
	
	protected Object validateAndImport(Object oldParent, Object newParent, List<JsonBean> fieldsDef, 
			JsonBean newJsonParent, JsonBean oldJsonParent) {
		return newParent;
	}
	protected Object validateAndImport(Object oldParent, Object newParent, JsonBean fieldsDef, 
			JsonBean newJsonParent, JsonBean oldJsonParent) {
		return newParent;
	}
	
	protected void setNewField(Object activityField, JsonBean field, JsonBean newJson) {
		if (activityField == null) return;
		// TODO:
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
