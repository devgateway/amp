package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.SaveMode.DRAFT;
import static org.digijava.kernel.ampapi.endpoints.activity.SaveMode.SUBMIT;
import static org.digijava.module.aim.util.ActivityUtil.loadActivity;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.ChangeType;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.ampapi.endpoints.AMPTeamMemberService;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings.TranslationType;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.utils.AIHelper;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.activity.validators.mapping.ActivityErrorsMapper;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.exception.ApiExceptionMapper;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceService;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.exception.ActivityLockNotGrantedException;
import org.digijava.kernel.ampapi.exception.ImportFailedException;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.DBPersistenceTransactionManager;
import org.digijava.kernel.persistence.PersistenceTransactionManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.TranslatedValueContext;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.validator.ActivityValidationContext;
import org.digijava.module.aim.validator.groups.API;
import org.digijava.module.aim.validator.groups.Submit;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.hibernate.StaleStateException;

/**
 * Imports a new activity or updates an existing one
 *
 * @author Nadejda Mandrescu
 */
public class ActivityImporter extends ObjectImporter<ActivitySummary> {

    private static final Logger logger = Logger.getLogger(ActivityImporter.class);
    /**
     * FM path for the "Save as Draft" feature being enabled
     */
    private static final String SAVE_AS_DRAFT_PATH = "/Activity Form/Save as Draft";

    private static final Class<?>[] DRAFT_VALIDATION_GROUPS = {API.class};
    private static final Class<?>[] SUBMIT_VALIDATION_GROUPS = {API.class, Submit.class};

    private AmpActivityVersion newActivity = null;
    private AmpActivityVersion oldActivity = null;
    private boolean update = false;
    private ActivityImportRules rules;
    private SaveContext saveContext;
    private SaveMode requestedSaveMode;
    private boolean downgradedToDraftSave = false;
    private boolean isDraftFMEnabled;
    private User currentUser;
    private AmpTeamMember modifiedBy;
    private String sourceURL;
    private String endpointContextPath;
    private FMService fmService;
    private ActivityService activityService;
    private TeamMemberService teamMemberService;
    private PersistenceTransactionManager persistenceTransactionManager;

    private ResourceService resourceService = new ResourceService();

    public ActivityImporter(APIField apiField, ActivityImportRules rules) {
        super(new InputValidatorProcessor(InputValidatorProcessor.getActivityFormatValidators()),
                new InputValidatorProcessor(InputValidatorProcessor.getActivityBusinessRulesValidators()),
                apiField, TLSUtils.getSite());
        setJsonErrorMapper(new ActivityErrorsMapper());
        this.rules = rules;
        this.saveContext = SaveContext.api(!rules.isProcessApprovalFields());
        this.activityService = new AMPActivityService();
        this.fmService = new AMPFMService();
        this.teamMemberService = new AMPTeamMemberService();
        this.persistenceTransactionManager = new DBPersistenceTransactionManager();
    }

    private void init(Map<String, Object> newJson, boolean update, String endpointContextPath, String sourceURL) {
        this.sourceURL = sourceURL;
        this.update = update;
        this.currentUser = TeamUtil.getCurrentUser();
        if (rules.isTrackEditors()) {
            modifiedBy = teamMemberService.getAmpTeamMember(AIHelper.getModifiedByOrNull(newJson));
        } else {
            TeamMember currentTeamMember = TeamUtil.getCurrentMember();
            modifiedBy = teamMemberService.getAmpTeamMember(currentTeamMember.getMemberId());
            Long mId = modifiedBy == null ? null : modifiedBy.getAmpTeamMemId();
            newJson.put(FieldMap.underscorify(ActivityFieldsConstants.MODIFIED_BY), mId);
            newJson.remove(FieldMap.underscorify(ActivityFieldsConstants.CREATED_BY));
        }
        if (!rules.isProcessApprovalFields()) {
            newJson.remove(FieldMap.underscorify(ActivityFieldsConstants.APPROVED_BY));
            newJson.remove(FieldMap.underscorify(ActivityFieldsConstants.APPROVAL_DATE));
            newJson.remove(FieldMap.underscorify(ActivityFieldsConstants.APPROVAL_STATUS));
        }
        this.newJson = newJson;
        this.isDraftFMEnabled = fmService.isVisible(SAVE_AS_DRAFT_PATH);
        this.endpointContextPath = endpointContextPath;
        initRequestedSaveMode();
    }

    /**
     * Imports or Updates
     *
     * @param newJson new activity configuration
     * @param update  flags whether this is an import or an update request
     * @return ActivityImporter instance
     */
    public ActivityImporter importOrUpdate(Map<String, Object> newJson, boolean update, String endpointContextPath,
                                           String sourceURL) {
        init(newJson, update, endpointContextPath, sourceURL);

        // get existing activity if this is an update request
        Long activityId = update ? AIHelper.getActivityIdOrNull(newJson) : null;
        
        if (update && activityId == null) {
            addError(ActivityErrors.FIELD_ACTIVITY_ID_NULL);
            return this;
        }

        if (modifiedBy == null) {
            addError(SecurityErrors.INVALID_TEAM.withDetails(ActivityErrors.INVALID_MODIFY_BY_FIELD));
            return this;
        }

        Long activityGroupVersion = AIHelper.getActivityGroupVersionOrNull(newJson);
        if (activityService.isActivityStale(activityId, activityGroupVersion)) {
            addError(ActivityErrors.ACTIVITY_IS_STALE.withDetails(ActivityErrors.ACTIVITY_NOT_LAST_VERSION));
            return this;
        }

        checkPermissions(update, activityId, modifiedBy);
        
        if (!errors.isEmpty()) {
            return this;
        }

        // check if any error were already detected in upper layers
        Map<Integer, ApiErrorMessage> existingErrors = (TreeMap<Integer, ApiErrorMessage>)
                newJson.get(ActivityEPConstants.INVALID);

        if (existingErrors != null && existingErrors.size() > 0) {
            errors.putAll(existingErrors);
        }

        try {
            ActivityGatekeeper.doWithLock(activityId, modifiedBy.getAmpTeamMemId(), persistenceTransactionManager,
                    () -> importOrUpdateActivity(activityId));
        } catch (Throwable e) {
            // error is not always logged at source; better duplicate it than have none
            logger.error("Import failed", e);
            if (e instanceof ActivityLockNotGrantedException) {
                logger.error("Cannot aquire lock during IATI update for activity " + activityId);
                Long userId = ((ActivityLockNotGrantedException) e).getUserId();
                String memberName = TeamMemberUtil.getTeamMember(userId).getMemberName();
                errors.put(ActivityErrors.ACTIVITY_IS_BEING_EDITED.id,
                        ActivityErrors.ACTIVITY_IS_BEING_EDITED.withDetails(memberName));
            } else if (e instanceof StaleStateException) {
                throw new RuntimeException("Activity updated in meantime without using gatekeeper.", e);
            } else if (errors.isEmpty()) {
                throw new RuntimeException(e);
            } else if (!(e instanceof ImportFailedException)) {
                addError(new ApiExceptionMapper().getApiErrorMessageFromException(e));
            }
        } finally {
            ActivityValidationContext.set(null);
        }
    
        updateResponse(update);

        return this;
    }
    
    /**
     * Import or updates activity.
     * @param activityId
     */
    private void importOrUpdateActivity(Long activityId) {
        boolean oldActivityDraft = false;
        try {
            if (activityId != null) {
                try {
                    oldActivity = loadActivity(activityId);
                } catch (DgException e) {
                    logger.error(e.getMessage());
                    errors.put(ActivityErrors.ACTIVITY_NOT_LOADED.id, ActivityErrors.ACTIVITY_NOT_LOADED);
                }
            }
    
            sanityChecks();
            
            if (oldActivity != null) {
                oldActivityDraft = oldActivity.getDraft();
            
                newActivity = oldActivity;
                oldActivity = ActivityVersionUtil.cloneActivity(oldActivity);
                oldActivity.setAmpId(newActivity.getAmpId());
                oldActivity.setAmpActivityGroup(newActivity.getAmpActivityGroup().clone());
            
                newActivity.getAmpActivityGroup().setVersion(-1L);
                // TODO AMP-28993: remove explicitly resetting createdBy since it is cleared during init
                if (!rules.isTrackEditors()) {
                    Long createdById = oldActivity.getActivityCreator().getAmpTeamMemId();
                    newJson.put(FieldMap.underscorify(ActivityFieldsConstants.CREATED_BY), createdById);
                }
                // TODO AMP-28993: remove explicitly resetting approval fields since they are cleared during init
                if (!rules.isProcessApprovalFields()) {
                    newJson.put(FieldMap.underscorify(ActivityFieldsConstants.APPROVED_BY),
                            oldActivity.getApprovedBy() == null ? null : oldActivity.getApprovedBy().getAmpTeamMemId());
                    newJson.put(FieldMap.underscorify(ActivityFieldsConstants.APPROVAL_DATE),
                            DateTimeUtil.formatISO8601Timestamp(oldActivity.getApprovalDate()));
                    newJson.put(FieldMap.underscorify(ActivityFieldsConstants.APPROVAL_STATUS),
                            oldActivity.getApprovalStatus().getId());
                }
            } else if (!update) {
                newActivity = new AmpActivityVersion();
            }
    
            validateAndImport(newActivity, newJson);
            
            if (errors.isEmpty()) {
                prepareToSave();
                boolean draftChange = ActivityUtil.detectDraftChange(newActivity, oldActivityDraft);
    
                newActivity = activityService.saveActivity(newActivity, getTranslations(), modifiedBy, draftChange,
                        saveContext, getEditorStore(), getSite());
                
                activityService.updateLuceneIndex(newActivity, oldActivity, update, trnSettings, getTranslations(),
                        getSite());
            }
        } catch (Exception e) {
            addError(new ApiExceptionMapper().getApiErrorMessageFromException(e));
        }
        
        if (!errors.isEmpty()) {
            throw new ImportFailedException("Trigger rollback");
        }
    }

    /**
     * Before proceeding with import, check if user provided correct amp id and activity id fields.
     */
    private void sanityChecks() {
        String ampIdFieldName = ActivityEPConstants.AMP_ID_FIELD_NAME;
        Object reqAmpIdObj = newJson.get(ampIdFieldName);
        if (update) {
            String ampId = oldActivity.getAmpId();
            String requestedAmpId = reqAmpIdObj instanceof String ? (String) reqAmpIdObj : null;
            if (!ampId.equals(requestedAmpId)) {
                // amp id must match amp id of the existing activity
                addError(ActivityErrors.FIELD_INVALID_VALUE.withDetails(ampIdFieldName));
            }
        } else if (reqAmpIdObj != null) {
            // amp id must be null on insert
            addError(ActivityErrors.FIELD_INVALID_VALUE.withDetails(ampIdFieldName));
        }

        // activity id must not be specified on insert
        String activityIdFieldName = ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME;
        Object activityId = newJson.get(activityIdFieldName);
        if (!update && activityId != null) {
            addError(ActivityErrors.FIELD_INVALID_VALUE.withDetails(activityIdFieldName));
        }
    }

    @Override
    protected void beforeViolationsCheck() {
        newActivity.setDraft(isDraft());

        if (rules.isProcessApprovalFields()) {
            Date newApprovalDate = newActivity.getApprovalDate();
            if (newApprovalDate != null
                    && (oldActivity == null || !newApprovalDate.equals(oldActivity.getApprovalDate()))
                    || AmpARFilter.VALIDATED_ACTIVITY_STATUS.contains(newActivity.getApprovalStatus())) {
                newActivity.setApprovalDate(new Date());
            }
        }

        ActivityValidationContext avc = new ActivityValidationContext();
        avc.setNewActivity(newActivity);
        avc.setOldActivity(oldActivity);
        ActivityValidationContext.set(avc);

        ActivityUtil.prepareToSave(newActivity, oldActivity, modifiedBy, newActivity.getDraft(), saveContext);
    }

    public boolean isDraft() {
        return DRAFT.equals(requestedSaveMode) || this.downgradedToDraftSave;
    }

    /**
     * Determine team member responsible for modification.
     * For AMP Offline clients this is the value retrieved from modified_by field of the activity. For other clients
     * it is the session user.
     */
    public AmpTeamMember getModifiedBy() {
        return modifiedBy;
    }
    
    /**
     * Check if specified team member can add/edit the activity in question.
     *
     * @param update true for edit, false for add
     * @param ampActivityId activity id to check, used only for edit case
     * @param ampTeamMember amp team member to check
     *
     */
    private void checkPermissions(boolean update, Long ampActivityId, AmpTeamMember ampTeamMember) {
        if (update) {
            checkEditPermissions(ampTeamMember, ampActivityId);
        } else {
            checkAddPermissions(ampTeamMember);
        }
    }

    /**
     * Check if team member can add activities.
     */
    private void checkAddPermissions(AmpTeamMember ampTeamMember) {
        if (!activityService.addActivityAllowed(ampTeamMember)) {
            addError(SecurityErrors.NOT_ALLOWED.withDetails(ActivityErrors.ADD_ACTIVITY_NOT_ALLOWED));
        }
    }

    /**
     * Check if team member can edit the activity.
     */
    private void checkEditPermissions(AmpTeamMember ampTeamMember, Long activityId) {
        if (!activityService.isEditableActivity(ampTeamMember, activityId)) {
            addError(SecurityErrors.NOT_ALLOWED.withDetails(ActivityErrors.EDIT_ACTIVITY_NOT_ALLOWED));
        }
    }

    @Override
    protected Object extractString(APIField apiField, Object parentObj, Object jsonValue) {
        return extractTranslationsOrSimpleValue(apiField, parentObj, jsonValue);
    }

    protected String extractTranslationsOrSimpleValue(APIField apiField, Object parentObj, Object jsonValue) {
        TranslationType trnType = apiField.getTranslationType();
        // no translation expected
        if (TranslationType.NONE == trnType) {
            return (String) jsonValue;
        }
        // base table value
        String value = null;
        if (TranslationType.STRING == trnType) {
            value = extractContentTranslation(apiField, parentObj, (Map<String, Object>) jsonValue);
        } else {
            Map<String, Object> editorText = null;
            if (trnSettings.isMultilingual()) {
                editorText = (Map<String, Object>) jsonValue;
            } else {
                // simulate the lang-value map, since dg_editor is still stored per language
                editorText = new HashMap<String, Object>();
                editorText.put(trnSettings.getDefaultLangCode(), jsonValue);
            }
            value = extractTextTranslations(apiField, parentObj, editorText);
        }
        return value;
    }

    /**
     * Stores all provided translations
     *
     * @param apiField the api field
     * @param parentObj the object the field is part of
     * @param trnJson <lang, value> map of translations for each language
     * @return value to be stored in the base table
     */
    protected String extractContentTranslation(APIField apiField, Object parentObj, Map<String, Object> trnJson) {
        String value = null;
        String currentLangValue = null;
        String anyLangValue = null;

        String objectClass = parentObj.getClass().getName();
        Long objId = (Long) ((Identifiable) parentObj).getIdentifier();
        String fieldName = apiField.getFieldNameInternal();
        List<AmpContentTranslation> trnList = getContentTranslation(objectClass, objId, fieldName);
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
                act = new AmpContentTranslation(objectClass, objId, fieldName, langCode, translation);
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
        getTranslations().addAll(trnList);
        return value;
    }

    /**
     * Stores Rich Text Editor entries
     *
     * @param apiField reference api field for the key
     * @param parentObj the object the field is part of
     * @param trnJson <lang, value> map of translations for each language
     * @return dg_editor key reference to be stored in the base table
     */
    private String extractTextTranslations(APIField apiField, Object parentObj, Map<String, Object> trnJson) {
        String oldKey = apiField.getFieldAccessor().get(parentObj);
        String newKey = AIHelper.getEditorKey(apiField.getFieldNameInternal());
        List<Editor> editorList;
        if (StringUtils.isNotBlank(oldKey)) {
            editorList = getEditor(oldKey);
            getEditorStore().getOldKey().put(newKey, oldKey);
        } else {
            editorList = ImmutableList.of();
        }

        Map<String, String> newValues = new TreeMap<>();

        for (Entry<String, Object> trn : trnJson.entrySet()) {
            String langCode = trn.getKey();
            // AMP-20884: no cleanup so far DgUtil.cleanHtmlTags((String) trn.getValue());
            String translation = (String) trn.getValue();
            newValues.put(langCode, translation);
        }

        // copy old values for languages not specified in json
        for (Editor editor : editorList) {
            if (!trnJson.keySet().contains(editor.getLanguage())) {
                newValues.put(editor.getLanguage(), editor.getBody());
            }
        }

        getEditorStore().getValues().put(newKey, newValues);

        return newKey;
    }

    private void initEditor(Field field) {
        try {
            String currentValue = (String) field.get(newActivity);
            if (currentValue == null) {
                currentValue = AIHelper.getEditorKey(field.getName());
                field.setAccessible(true);
                field.set(newActivity, currentValue);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            logger.error("Failed to initialize editor field " + field, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Performs operations that need to be done before the activity is saved
     */
    protected void prepareToSave() {
        newActivity.setLastImportedAt(new Date());
        newActivity.setLastImportedBy(currentUser);

        if (!update) {
            newActivity.setAmpActivityGroup(null);
        }

        newActivity.setChangeType(determineChangeType().toString());
        initDefaults();
    }

    private ChangeType determineChangeType() {
        if (AmpClientModeHolder.isOfflineClient()) {
            return ChangeType.AMP_OFFLINE;
        } else if (AmpClientModeHolder.isIatiImporterClient()) {
            return ChangeType.IATI_IMPORTER;
        }

        return ChangeType.IMPORT;
    }

    /**
     * Initialize m2m-fields before saving them
     */
    protected void initDefaults() {
        for (Field field : AmpActivityFields.class.getFields()) {
            if (ActivityTranslationUtils.isVersionableTextField(field)) {
                initEditor(field);
            }
        }
        // REFACTOR: may no longer need some of these initializations
        initOrgRoles();
        initSectors();
        initLocations();
        initFundings();
        initContacts();
        updatePPCAmount();
        updateRoleFundings();
    }

    protected void initSectors() {
        if (newActivity.getSectors() == null) {
            newActivity.setSectors(new HashSet<AmpActivitySector>());
        }
    }

    protected void initFundings() {
        if (newActivity.getFunding() == null) {
            newActivity.setFunding(new HashSet<AmpFunding>());
        }
        Iterator<AmpFunding> iterator = newActivity.getFunding().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            AmpFunding funding = iterator.next();
            funding.setIndex(i++);
        }
    }

    protected void initOrgRoles() {
        if (newActivity.getOrgrole() == null) {
            newActivity.setOrgrole(new HashSet<>());
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
        boolean isAnnualBudget = fmService.isVisible(
                "/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost");

        if (isAnnualBudget && newActivity.getAnnualProjectBudgets() != null) {
            AmpFundingAmount ppc = newActivity.getProjectCostByType(AmpFundingAmount.FundingType.PROPOSED);
            double funAmount = 0d;
            for (AmpAnnualProjectBudget apb : newActivity.getAnnualProjectBudgets()) {
                funAmount += ActivityInterchangeUtils.doPPCCalculations(apb, ppc.getCurrencyCode());
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
        boolean isSourceRoleEnalbed = fmService.isVisible(
                "/Activity Form/Funding/Funding Group/Funding Item/Source Role");

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

    private void initRequestedSaveMode() {
        String draftFieldName = FieldMap.underscorify(ActivityFieldsConstants.IS_DRAFT);
        Object draftAsObj = newJson.get(draftFieldName);
        if (draftAsObj != null && draftAsObj instanceof Boolean) {
            boolean draft = (boolean) draftAsObj;
            requestedSaveMode = draft ? DRAFT : SUBMIT;
        }
    }

    /**
     * Return save mode for validation purposes. If this value is null then validators can assume that they validate
     * activity for submission (non-draft) with possibility to downgrade with draft save. However if returned value
     * is specified then validators must honor this setting and return appropriate errors.
     * @return requested SaveMode or null
     */
    public SaveMode getRequestedSaveMode() {
        return requestedSaveMode;
    }

    public ActivityImportRules getImportRules() {
        return rules;
    }

    /**
     * @return the sourceURL
     */
    public String getSourceURL() {
        return sourceURL;
    }

    public void downgradeToDraftSave() {
        this.downgradedToDraftSave = true;
    }

    public ResourceService getResourceService() {
        return this.resourceService;
    }
    
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }
    
    public void setFmService(FMService fmService) {
        this.fmService = fmService;
    }
    
    public void setTeamMemberService(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }
    
    public void setPersistenceTransactionManager(PersistenceTransactionManager persistenceTransactionManager) {
        this.persistenceTransactionManager = persistenceTransactionManager;
    }
    
    @Override
    public ActivitySummary getImportResult() {
        if (newActivity != null && newActivity.getAmpActivityId() != null) {
            // editable, viewable, since was just created/updated
            return ProjectList.getActivityInProjectListFormat(newActivity, true, true);
        }
        return null;
    }

    @Override
    protected String getInvalidInputFieldName() {
        return ActivityEPConstants.ACTIVITY;
    }

    @Override
    public void processInterViolationsForTypes(Map<String, Object> json, Object root) {

        Set<ConstraintViolation> violations = validateWithDowngrading(
                g -> getImporterInterchangeValidator().validate(getApiField(), root, getTranslationContext(), g));

        getImporterInterchangeValidator().integrateTypeErrorsIntoResult(violations, json);
    }

    @Override
    public void processInterViolationsForField(APIField type, Map<String, Object> parentJson, String fieldPath,
            Object fieldValue, TranslatedValueContext translatedValueContext) {

        Set<ConstraintViolation> violations = validateWithDowngrading(
                g -> getImporterInterchangeValidator().validateField(type, fieldValue, translatedValueContext, g));

        getImporterInterchangeValidator().integrateFieldErrorsIntoResult(violations, parentJson, fieldPath);
    }

    /**
     * <p>Invoke validation function with submit group if activity is being submitted and downgrading to draft didn't
     * happen yet.</p>
     * <p>If there are violations for submit group and downgrading is allowed, then validation is invoked second time
     * with the draft group. Draft field will change to true and downgradedToDraftSave flag is also set to true.</p>
     * <p>In all of the other cases, validation is invoked with draft group.</p>
     *
     * @param validationFn validation function that accepts validation groups and returns constraint violations
     * @return constraint violations generated by validation function
     */
    private Set<ConstraintViolation> validateWithDowngrading(Function<Class[], Set<ConstraintViolation>> validationFn) {
        Set<ConstraintViolation> violations;
        if (requestedSaveMode == SUBMIT && !downgradedToDraftSave) {
            violations = validationFn.apply(SUBMIT_VALIDATION_GROUPS);
            if (!violations.isEmpty() && getImportRules().isCanDowngradeToDraft()) {
                if (isDraftFMEnabled) {
                    downgradeToDraftSave();
                    newActivity.setDraft(true);
                    violations = validationFn.apply(DRAFT_VALIDATION_GROUPS);
                } else {
                    addError(ActivityErrors.SAVE_AS_DRAFT_FM_DISABLED);
                }
            }
        } else {
            violations = validationFn.apply(DRAFT_VALIDATION_GROUPS);
        }
        return violations;
    }
}
