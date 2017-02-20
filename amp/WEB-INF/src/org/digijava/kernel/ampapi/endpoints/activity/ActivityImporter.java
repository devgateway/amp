/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.util.ChangeType;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings.TranslationType;
import org.digijava.kernel.ampapi.endpoints.activity.utils.AIHelper;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.exception.ApiExceptionMapper;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrgRoleBudget;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
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
	/**
	 * FM path for the "Save as Draft" feature being enabled 
	 */
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
		this.isDraftFMEnabled = FMVisibility.isVisible(SAVE_AS_DRAFT_PATH, null);
		this.isMultilingual = ContentTranslationUtil.multilingualIsEnabled();
		this.trnSettings = TranslationSettings.getCurrent();
        this.endpointContextPath = endpointContextPath;
	}

    /**
     * Cleans all the fields of the new activity (except for AMP ID and internal ID),
     * in the case it's an update process.
     * It has to be this way because otherwise it would contain leftover data from the old activity 
     * (in m2ms, like sectors)
     */
    private void cleanupNewActivity() {
    	if (newActivity == null)
    		return;
    	
		Map<String, Method> aafMethods = new HashMap<String, Method>();
		for (Method method : AmpActivityFields.class.getMethods()) {
			aafMethods.put(method.getName(), method);
		}
		
		for (Field field : AmpActivityFields.class.getDeclaredFields()) {
			Interchangeable ant = field.getAnnotation(Interchangeable.class);
			if (ant != null && ant.importable()) {
				try {
					if (ant.fieldTitle().equals(ActivityFieldsConstants.AMP_ACTIVITY_ID) ||
							ant.fieldTitle().equals(ActivityFieldsConstants.AMP_ID))
						continue;
					// clean up everything importable in the new activity
					Method setterMeth = aafMethods.get(InterchangeUtils.getSetterMethodName(field.getName()));
					Method getterMeth = aafMethods.get(InterchangeUtils.getGetterMethodName(field.getName()));
					if (Collection.class.isAssignableFrom(field.getType())) {
						@SuppressWarnings("unchecked")
						Collection<Object> col = (Collection<Object>) getterMeth.invoke(newActivity);
						if (col != null)
							col.clear();
					} else {
						setterMeth.invoke(newActivity, new Object[]{null});
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
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
		Long ampActivityId = update ? AIHelper.getActivityIdOrNull(newJson) : null;

		AmpTeamMember teamMember;
		try {
			teamMember = getAmpTeamMember(AIHelper.getTeamIdOrNull(newJson));
		} catch (RuntimeException e) {
			logger.error("Failed to find team member.", e);
			return Collections.singletonList(SecurityErrors.INVALID_TEAM);
		}

		List<ApiErrorMessage> messages = checkPermissions(update, ampActivityId, teamMember);
		if (!messages.isEmpty()) {
			return messages;
		}

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
				errors.put(ActivityErrors.ACTIVITY_NOT_LOADED.id, ActivityErrors.ACTIVITY_NOT_LOADED);
			}
		}
		
		String activityId = ampActivityId == null ? null : ampActivityId.toString();
		String key = null;

		try {
			// initialize new activity
			InterchangeUtils.getSessionWithPendingChanges();
			
			if (oldActivity != null) {
				key = ActivityGatekeeper.lockActivity(activityId, TeamUtil.getCurrentAmpTeamMember().getAmpTeamMemId());
				
				if (key == null){ //lock not acquired
					logger.error("Cannot aquire lock during IATI update for activity " + activityId);
					errors.put(ActivityErrors.ACTIVITY_IS_LOCKED.id, ActivityErrors.ACTIVITY_IS_LOCKED);
				}
				
				newActivity = oldActivity;
				// REFACTOR: we may no longer need to use old activity
				oldActivity = ActivityVersionUtil.cloneActivity(oldActivity, TeamUtil.getCurrentAmpTeamMember());
				oldActivity.setAmpId(newActivity.getAmpId());
				oldActivity.setAmpActivityGroup(newActivity.getAmpActivityGroup());
				
				cleanupNewActivity();
			} else if (!update) {
				newActivity = new AmpActivityVersion();
			}
			
			// REFACTOR: we may no longer need to use oldJson
			Map<String, Object> oldJsonParent = null;
			Map<String, Object> newJsonParent = newJson.any();
			
			newActivity = (AmpActivityVersion) validateAndImport(newActivity, oldActivity, fieldsDef, newJsonParent, 
					oldJsonParent, null);
			if (newActivity != null && errors.isEmpty()) {
				// save new activity
				prepareToSave();
				newActivity = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(newActivity, 
						translations, teamMember, Boolean.TRUE.equals(newActivity.getDraft()),
						PersistenceManager.getRequestDBSession(), false, false);
				postProcess();
			} else {
				// undo any pending changes
				PersistenceManager.getSession().clear();
			}
			
			updateResponse(update);
		} catch (Throwable e) {
			// if any unhandled issue, then cleanup pending changes 
			PersistenceManager.getSession().clear();
			
			if (errors.isEmpty()) {
				throw new RuntimeException(e);
			} else {
				ApiExceptionMapper aem = new ApiExceptionMapper();
				ApiErrorMessage apiErrorMessageFromException = aem.getApiErrorMessageFromException(e);
				errors.put(apiErrorMessageFromException.id, apiErrorMessageFromException);
			}
		} finally {
			ActivityGatekeeper.unlockActivity(activityId, key);
		}
		
		return new ArrayList<ApiErrorMessage>(errors.values());
	}

	/**
	 * Check if specified team member can add/edit the activity in question.
	 *
	 * @param update true for edit, false for add
	 * @param ampActivityId activity id to check, used only for edit case
	 * @param teamMember team member to check
	 * @return list of errors, in case of success list will be empty
	 */
	private List<ApiErrorMessage> checkPermissions(boolean update, Long ampActivityId, AmpTeamMember teamMember) {
		if (update) {
			return checkEditPermissions(teamMember, ampActivityId);
		} else {
			return checkAddPermissions(teamMember);
		}
	}

	/**
	 * Check if team member can add activities.
	 */
	private List<ApiErrorMessage> checkAddPermissions(AmpTeamMember teamMember) {
		if (!InterchangeUtils.addActivityAllowed(new TeamMember(teamMember))) {
			return Collections.singletonList(SecurityErrors.NOT_ALLOWED.withDetails("Adding activity is not allowed"));
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * Check if team member can edit the activity.
	 */
	private List<ApiErrorMessage> checkEditPermissions(AmpTeamMember ampTeamMember, Long activityId) {
		if (!InterchangeUtils.isEditableActivity(new TeamMember(ampTeamMember), activityId)) {
			return Collections.singletonList(SecurityErrors.NOT_ALLOWED.withDetails("No right to edit this activity"));
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * If teamId is specified, then try to find team membership for currently authenticated user. Otherwise use
	 * team membership stored in session.
	 *
	 * @param teamId    teamId to search
	 * @return			team membership or a RuntimeException in case teamId is wrong or user is not part of the team
	 */
	private AmpTeamMember getAmpTeamMember(Long teamId) {
		if (teamId == null) {
            return currentMember;
        } else {
            AmpTeam team = TeamUtil.getAmpTeam(teamId);

            User user = currentMember.getUser();

			AmpTeamMember  teamMember = TeamMemberUtil.getAmpTeamMemberByUserByTeam(user, team);

            if (teamMember == null) {
                throw new RuntimeException(String.format("User %s is not part of team with id %d.",
                        user.getEmail(), team.getAmpTeamId()));
            }
			return teamMember;
		}
	}
	
	/**
	 * Recursive method (through ->validateAndImport->validateSubElements->[this method]
	 * that attempts to validate the incoming JSON and import its data. 
	 * If there are any errors -> append them to the validator to propagate upwards
	 * @param newParent Matched parent object in which resides the field of the activity we're importing or updating
	 * 					(for example, AmpActivityVersion newActivity is newParent for 'sectors'
	 * @param oldParent Matched parent object in which the old activity field resides
	 * @param fieldsDef definitions of the fields in this parent (from Fields Enumeration EP)
	 * @param newJsonParent parent JSON object in which reside the analyzed fields 
	 * @param oldJsonParent old parent JSON
	 * @param fieldPath the underscorified path to the field currently validated & imported
	 * @return currently updated object or null if any validation error occurred
	 */
	protected Object validateAndImport(Object newParent, Object oldParent, List<JsonBean> fieldsDef, 
			Map<String, Object> newJsonParent, Map<String, Object> oldJsonParent, String fieldPath) {
		Set<String> fields = new HashSet<String>(newJsonParent.keySet());
		// process all valid definitions
		for (JsonBean fieldDef : fieldsDef) {
			newParent = validateAndImport(newParent, oldParent, fieldDef, newJsonParent, oldJsonParent, fieldPath);
			fields.remove(fieldDef.get(ActivityEPConstants.FIELD_NAME));
		}
		
		// and error anything remained
		// note: due to AMP-20766, we won't be able to fully detect invalid children
		String fieldPathPrefix = fieldPath == null ? "" : fieldPath + "~";
		if (fields.size() > 0) {
			newParent = null;
			for (String invalidField : fields) {
				// no need to go through deep-first validation flow
				validator.addError(newJsonParent, invalidField, fieldPathPrefix + invalidField, ActivityErrors.FIELD_INVALID, errors);
			}
		}
		
		return newParent;
	}

	/**
	 * Of all the fields from AmpActivityFields, these two (AMP ID and Internal ID) should be null 
	 * on a fresh project import and, therefore, shouldn't be deleted if they are null 
	 * in the new activity JSON.
	 * 
	 * @param currentFieldPath path to the field 
	 * @return true if it's not AMP ID or Internal ID, false otherwise
	 */
	protected boolean fieldDeletableOnNull(String currentFieldPath) {
		if (currentFieldPath.equals(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME))
			return false;
		if (currentFieldPath.equals(ActivityEPConstants.AMP_ID_FIELD_NAME))
			return false;
		return true;
	}
	
	/**
	 * Validates and imports a single element (and its subelements)  
	 * @param newParent parent object containing the field
	 * @param oldParent old parent (for activity)
	 * @param fieldDef JsonBean holding the description of the field (obtained from the Fields Enumerator EP)
	 * @param newJsonParent JSON as imported
	 * @param oldJsonParent JSON of the old activity (if it's update) from the Export Activity EP
	 * @param fieldPath underscorified path to the field
	 * @return currently updated object or null if any validation error occurred
	 */
	protected Object validateAndImport(Object newParent, Object oldParent, JsonBean fieldDef,
			Map<String, Object> newJsonParent, Map<String, Object> oldJsonParent, String fieldPath) {
		String fieldName = getFieldName(fieldDef, newJsonParent);
		String currentFieldPath = (fieldPath == null ? "" : fieldPath + "~") + fieldName;
		Object oldJsonValue = oldJsonParent == null ? null : oldJsonParent.get(fieldName);
		Object newJsonValue = newJsonParent == null ? null : newJsonParent.get(fieldName);
		// validate and import sub-elements first (if any)
		newParent = validateSubElements(fieldDef, newParent, oldParent, newJsonValue, oldJsonValue, currentFieldPath);
		// then validate current field itself
		boolean valid = validator.isValid(this, newJsonParent, oldJsonParent, fieldDef, currentFieldPath, errors);
		// and set new field only if all sub-elements are valid
		if (valid && newParent != null) {
			newParent = setNewField(newParent, fieldDef, newJsonParent, currentFieldPath);
		} else if (!valid) {
			newParent = null;
		}
		return newParent;
	}
	
	/**
	 * Obtains the field name
	 * @param fieldDef
	 * @param newJsonParent
	 * @return
	 */
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
	
	/**
	 * Validates sub-elements (recursively)
	 * @param fieldDef
	 * @param newParent
	 * @param oldParent
	 * @param newJsonValue
	 * @param oldJsonValue
	 * @param fieldPath
	 * @return currently updated object or null if any validation error occurred
	 */
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
		@SuppressWarnings("unchecked")
		List<JsonBean> childrenFields = (List<JsonBean>) fieldDef.get(ActivityEPConstants.CHILDREN);
		List<Map<String, Object>> childrenNewValues = getChildrenValues(newJsonValue, isList);
		List<Map<String, Object>> childrenOldValues = getChildrenValues(oldJsonValue, isList);
		
		// validate children, even if it is not a list -> to notify wrong entries
		if ((isList || childrenFields != null && childrenFields.size() > 0) && childrenNewValues != null) {
			String actualFieldName = fieldDef.getString(ActivityEPConstants.FIELD_NAME_INTERNAL);
			Field newField = getField(newParent, actualFieldName);
			// REFACTOR: remove old parent and field usage, not relevant anymore
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
				// AMP-20766: we cannot correctly detect isCollection when current validation already failed (no parent obj ref)
				if (newFieldValue != null && Collection.class.isAssignableFrom(newFieldValue.getClass())) {
					isCollection = true;
					subElementClass = AIHelper.getGenericsParameterClass(newField);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
			
			if (newFieldValue != null && AmpAgreement.class.isAssignableFrom(newFieldValue.getClass()) && childrenNewValues.size() == 1) {
				Map<String, Object> agreementMap = childrenNewValues.get(0);
				childrenNewValues.clear();
				for (String key : agreementMap.keySet()) {
					HashMap<String, Object> kv = new HashMap<String, Object>();
					Object val = agreementMap.get(key);
					
					if (val instanceof String) {
						val = StringUtils.trim((String) val);
					}
					
					kv.put(key, val);
					childrenNewValues.add(kv);
				}
			}
			
			// process children 
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
						Long objId = getElementId(fieldDef, newJsonValue);
						Object newSubElement = null;
						// TODO: make it generic. Given unexpected need to support, I have to proceed very custom...
						boolean isAFA = AmpFundingAmount.class.isAssignableFrom(subElementClass);
						if (isAFA && objId != null) {	
							newSubElement = getObjectReferencedById(subElementClass, objId);
						} else {
							newSubElement = subElementClass.newInstance();
						}
						res = validateAndImport(newSubElement, null, childrenFields, newChild, oldChild, fieldPath);
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
					configureCustom(newParent, res, fieldPath);
				}
			}
			// TODO: we also need to validate other children, some can be mandatory
		}
		return newParent;
	}
	
	/**
	 * Identifies if an existing object has to be worked with
	 * @param fieldDefOfAnObject
	 * @param jsonValue
	 * @return
	 */
	private Long getElementId(JsonBean fieldDefOfAnObject, Object jsonValue) {
		List<JsonBean> children  = (List<JsonBean>) fieldDefOfAnObject.get(ActivityEPConstants.CHILDREN);
		if (children != null && jsonValue != null) {
			for (JsonBean childDef : children) {
				if (Boolean.TRUE.equals(childDef.get(ActivityEPConstants.ID))) {
					String idFieldName = childDef.getString(ActivityEPConstants.FIELD_NAME);
					String idStr = String.valueOf(((List<Map<String, Object>>) jsonValue).get(0).get(idFieldName));
					if (StringUtils.isNumeric(idStr))
						return Long.valueOf(idStr);
					break;
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets items marked under the "children" key in the hierarchical branch of the imported JSON
	 * @param jsonValue
	 * @param isList
	 * @return
	 */
	private List<Map<String, Object>> getChildrenValues(Object jsonValue, boolean isList) {
		if (jsonValue != null) {
			if (jsonValue instanceof List) { 
				return (List<Map<String, Object>>) jsonValue;
			} else if (isList && jsonValue instanceof Map) {
				List<Map<String, Object>> jsonValues = new ArrayList<Map<String, Object>>();
				jsonValues.add((Map<String, Object>) jsonValue);
				return jsonValues;
			}
		}
		return null;
	}
	
	/**
	 * Generates an instance of the type of the field 
	 * @param parent
	 * @param field
	 * @return
	 */
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
			logger.error("Actual Field not found: " + actualFieldName + ", fieldPath: " + fieldPath);
			return null;
		}
		
		if (!importable) {
			// custom process amp_activity_id links to other structures
			if (InterchangeUtils.isAmpActivityVersion(objField.getType())) {
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
		
		// REFACTOR: remove old field usage
		Object oldValue;
		try {
			oldValue = objField.get(newParent);
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			logger.error(e1.getMessage());
			throw new RuntimeException(e1);
		}
		Object newValue = getNewValue(objField, newParent, fieldValue, fieldDef, fieldPath);
		
		if (newValue == null && oldValue == null/* || newValue != null && newValue.equals(oldValue) */) {
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

	//unused anywhere -- commenting out for now
	//please delete it if it's September 2015 or later and you're reading this
//	protected boolean valueChanged(JsonBean newValue, JsonBean oldValue) {
//		// TODO:
//		return true;
//	}
	/**
	 * Gets the object identified by an ID, from the Possible Values EP
	 * @param objectType
	 * @param objectId
	 * @return
	 */
	protected Object getObjectReferencedById(Class<?> objectType, Long objectId) {
		if (Collection.class.isAssignableFrom(objectType))
			throw new RuntimeException("Can't handle a collection of ID-linked objects yet!");
		return InterchangeUtils.getObjectById(objectType, objectId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object getNewValue(Field field, Object parentObj, Object jsonValue, JsonBean fieldDef, String fieldPath) {
		boolean isCollection = Collection.class.isAssignableFrom(field.getType());
		if (jsonValue == null && !isCollection)
			return null;
		
		Object value = null;
		String fieldType = (String) fieldDef.get(ActivityEPConstants.FIELD_TYPE);
		List<JsonBean> allowedValues = getPossibleValuesForFieldCached(fieldPath, AmpActivityFields.class, null);
		boolean idOnly = Boolean.TRUE.equals(fieldDef.get(ActivityEPConstants.ID_ONLY));
		
		// this is an object reference
		if (!isCollection && idOnly) {
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
			return getObjectReferencedById(field.getType(), ((Number)jsonValue).longValue());
		}
		
		// this is a collection
		if (Collection.class.isAssignableFrom(field.getType())) {
			try {
				value = field.get(parentObj);
				Collection col = (Collection) value;
				if (col == null) {
					col = (Collection) getNewInstance(parentObj, field);
				}
				if (idOnly && jsonValue != null) {
					Class<?> objectType = AIHelper.getGenericsParameterClass(field);
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
		// this is a simple type
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
			}
		} else {
			try {
				if (AmpAgreement.class.equals(field.getType())) {
					value = field.get(parentObj);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
		
		return value;
	}
	
	protected String extractTranslationsOrSimpleValue(Field field, Object parentObj, Object jsonValue, JsonBean fieldDef) {
		TranslationType trnType = trnSettings.getTranslatableType(field);
		// no translation expected
		if (TranslationType.NONE == trnType) {
			return (String) jsonValue;
		}
		// base table value
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
	
	/**
	 * Stores all provided translations
	 * @param field the field to translate
	 * @param parentObj the object the field is part of 
	 * @param trnJson <lang, value> map of translations for each language
	 * @return value to be stored in the base table
	 */
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
		// process translations
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
	
	/**
	 * Stores Rich Text Editor entries
	 * @param field reference field for the key
	 * @param parentObj the object the field is part of 
	 * @param trnJson <lang, value> map of translations for each language
	 * @return dg_editor key reference to be stored in the base table
	 */
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
	
	/**
	 * Performs operations that need to be done before the activity is saved
	 */
	protected void prepareToSave() {
        newActivity.setLastImportedAt(new Date());
        newActivity.setLastImportedBy(currentMember.getUser());

		newActivity.setChangeType(ChangeType.IMPORT.name());
		// configure draft status on import only, since on update we'll change to draft based on RequiredValidator
		if (!update) {
			newActivity.setDraft(isDraftFMEnabled);
		}
		initDefaults();
	}
	
	/**
	 * Initialize m2m-fields before saving them
	 */
	protected void initDefaults() {
		for (Field field : AmpActivityFields.class.getFields()) {
			if (InterchangeUtils.isVersionableTextField(field)) {
				initEditor(field);
			}
		}
		// REFACTOR: may no longer need some of these initializations
		initOrgRoles();
		initSectors();
		initLocations();
		initFundings();
        initContacts();
        postprocessActivityReferences();
        updatePPCAmount();
        updateRoleFundings();
        updateOrgRoles();
	}
	

	/*
	 * First, every reference to AmpActivityVersion in all the m2ms has been added to a map; 
	 * now, we're setting them all to point to the AmpActivityVersion we're importing
	 */
	// REFACTOR: not used anymore, candidate for removal
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
	
	/*
	 * this was the original way to circumvent Hibernate exceptions on save. 
	 * Since a lot of generic workarounds have been applied, don't know if it's still relevant 
	 */
	/**
	 * Do not remove, is relevant for AmpClassificationConfiguration configuration
	 */
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
        }
	}
	
	protected void initCategories() {
		if (newActivity.getCategories() == null) {
			newActivity.setCategories(new HashSet<AmpCategoryValue>());
		}
	}
	
	protected void initContacts() {
		if (newActivity.getActivityContacts() == null) {
        	newActivity.setActivityContacts(new HashSet<AmpActivityContact>());
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
    
    /**
	 * Updates Proposed Project Cost amount depending on configuration (annual budget)
	 */
	protected void updatePPCAmount() {
		boolean isAnnualBudget = FMVisibility.isVisible("/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost", null);

		if (isAnnualBudget && newActivity.getAnnualProjectBudgets() != null) {
			AmpFundingAmount ppc = newActivity.getProjectCostByType(AmpFundingAmount.FundingType.PROPOSED);
			double funAmount = 0d;
        	for(AmpAnnualProjectBudget apb : newActivity.getAnnualProjectBudgets()) {
        		funAmount += InterchangeUtils.doPPCCalculations(apb, ppc.getCurrencyCode());
        	}
			if (ppc != null) {
				ppc.setFunAmount(funAmount / AmountsUnits.getDefaultValue().divider);
			}
        }
	}
	
	 
    /**
	 * Updates activity fundings with a default source role if the item is disabled from FM (or is null)
	 */
	
	protected void updateRoleFundings() {
		boolean isSourceRoleEnalbed = FMVisibility.isVisible("/Activity Form/Funding/Funding Group/Funding Item/Source Role", null);

		if (!isSourceRoleEnalbed) {
			AmpRole role = org.digijava.module.aim.util.DbUtil.getAmpRole(Constants.FUNDING_AGENCY);
			for (AmpFunding f : newActivity.getFunding()) {
				if (f.getSourceRole() == null) {
					f.setSourceRole(role);
				}
			}
        }
	}
	
	/**
	 * Updates activity org roles (missing org roles from fundings)
	 */
	protected void updateOrgRoles() {
		for (AmpFunding f : newActivity.getFunding()) {
			if (f.getSourceRole() != null && f.getAmpDonorOrgId() != null) {
				boolean found = false;
				for (AmpOrgRole role : newActivity.getOrgrole()) {
						if (role.getRole().getRoleCode().equals(f.getSourceRole().getRoleCode()) 
								&& role.getOrganisation().getAmpOrgId().equals(f.getAmpDonorOrgId().getAmpOrgId())) {
							found = true;
							break;
						}
				}
			
				if (!found) {
					AmpOrgRole role = new AmpOrgRole();
					role.setOrganisation(f.getAmpDonorOrgId());
					role.setActivity(newActivity);
					role.setRole(f.getSourceRole());
					newActivity.getOrgrole().add(role);
				}
			}
		}
	}
	
	/**
	 * Execute custom configurations that is not worth to define generic for single use cases
	 * @param parent
	 * @param child
	 * @param fieldPath
	 */
	protected void configureCustom(Object parent, Object child, String fieldPath) {
		if (child instanceof AmpActivityContact) {
			configureContactType((AmpActivityContact) child, fieldPath);
		}
	}
	
	/**
	 * Custom configuration for the contact type 
	 * @param contact activity contact to configure
	 * @param contactGroup the contact group to configure
	 */
	protected void configureContactType(AmpActivityContact contact, String contactGroup) {
		// custom, but very special case no need to make generic
		String contactType = InterchangeableClassMapper.CONTACT_SET_NAME_TO_CONTACT_TYPE.get(
				InterchangeUtils.deunderscorify(contactGroup));
		if (contactType == null) {
			throw new RuntimeException("No contact type match found for contactGroup = " + contactGroup);
		}
		contact.setContactType(contactType);
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
	
	/**
	 * 
	 * @return
	 */
	public boolean isSaveAsDraft() {
		return saveAsDraft;
	}
	
	/**
	 * 
	 * @param saveAsDraft
	 */
	public void setSaveAsDraft(boolean saveAsDraft) {
		this.saveAsDraft = saveAsDraft;
	}

	/**
	 * 
	 * @param latestActivityId
	 */
    public void setLatestActivityId(Long latestActivityId) {
        this.latestActivityId = latestActivityId;
    }
    
}
