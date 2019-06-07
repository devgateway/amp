package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.SaveMode.DRAFT;
import static org.digijava.kernel.ampapi.endpoints.activity.SaveMode.SUBMIT;
import static org.digijava.module.aim.util.ActivityUtil.loadActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.ChangeType;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings.TranslationType;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.utils.AIHelper;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.activity.validators.mapping.ActivityErrorsMapper;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.exception.ApiExceptionMapper;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceService;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
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
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.validator.ActivityValidationContext;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
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

    private AmpActivityVersion newActivity = null;
    private AmpActivityVersion oldActivity = null;
    private boolean update = false;
    private ActivityImportRules rules;
    private SaveContext saveContext;
    private SaveMode requestedSaveMode;
    private boolean downgradedToDraftSave = false;
    private List<AmpContentTranslation> translations = new ArrayList<AmpContentTranslation>();
    private boolean isDraftFMEnabled;
    private boolean isMultilingual;
    private User currentUser;
    private AmpTeamMember modifiedBy;
    private String sourceURL;
    private String endpointContextPath;
    // latest activity id in case there was attempt to update older version of an activity
    private Long latestActivityId;

    private ResourceService resourceService = new ResourceService();

    public ActivityImporter(List<APIField> apiFields, ActivityImportRules rules) {
        super(new InputValidatorProcessor(InputValidatorProcessor.getActivityFormatValidators()),
                new InputValidatorProcessor(InputValidatorProcessor.getActivityBusinessRulesValidators()),
                apiFields);
        setJsonErrorMapper(new ActivityErrorsMapper());
        this.rules = rules;
        this.saveContext = SaveContext.api(!rules.isProcessApprovalFields());
    }

    private void init(Map<String, Object> newJson, boolean update, String endpointContextPath) {
        this.sourceURL = TLSUtils.getRequest().getRequestURL().toString();
        this.update = update;
        this.currentUser = TeamUtil.getCurrentUser();
        if (rules.isTrackEditors()) {
            modifiedBy = TeamMemberUtil.getAmpTeamMember(AIHelper.getModifiedByOrNull(newJson));
        } else {
            modifiedBy = TeamMemberUtil.getCurrentAmpTeamMember(TLSUtils.getRequest());
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
        this.isDraftFMEnabled = FMVisibility.isVisible(SAVE_AS_DRAFT_PATH, null);
        this.isMultilingual = ContentTranslationUtil.multilingualIsEnabled();
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
    public ActivityImporter importOrUpdate(Map<String, Object> newJson, boolean update, String endpointContextPath) {
        init(newJson, update, endpointContextPath);

        // retrieve fields definition for internal use
        List<APIField> fieldsDef = getApiFields();
        // get existing activity if this is an update request
        Long ampActivityId = update ? AIHelper.getActivityIdOrNull(newJson) : null;
        boolean oldActivityDraft = false;

        if (modifiedBy == null) {
            addError(SecurityErrors.INVALID_TEAM.withDetails("Invalid team member in modified_by field."));
            return this;
        }

        Long activityGroupVersion = AIHelper.getActivityGroupVersionOrNull(newJson);
        if (ActivityUtil.isActivityStale(ampActivityId, activityGroupVersion)) {
            addError(ActivityErrors.ACTIVITY_IS_STALE.withDetails("Activity is not the latest version."));
            return this;
        }

        checkPermissions(update, ampActivityId, modifiedBy);

        if (!errors.isEmpty()) {
            return this;
        }

        // check if any error were already detected in upper layers
        Map<Integer, ApiErrorMessage> existingErrors = (TreeMap<Integer, ApiErrorMessage>) newJson.get(ActivityEPConstants.INVALID);

        if (existingErrors != null && existingErrors.size() > 0) {
            errors.putAll(existingErrors);
        }

        if (ampActivityId != null) {
            try {
                oldActivity = loadActivity(ampActivityId);
            } catch (DgException e) {
                logger.error(e.getMessage());
                errors.put(ActivityErrors.ACTIVITY_NOT_LOADED.id, ActivityErrors.ACTIVITY_NOT_LOADED);
            }
        }

        String activityId = ampActivityId == null ? null : ampActivityId.toString();
        String key = null;

        Long currentVersion = null;

        try {
            // initialize new activity
            InterchangeUtils.getSessionWithPendingChanges();

            if (oldActivity != null) {
                currentVersion = oldActivity.getAmpActivityGroup().getVersion();
                oldActivityDraft = oldActivity.getDraft();

                key = ActivityGatekeeper.lockActivity(activityId, modifiedBy.getAmpTeamMemId());

                if (key == null) { //lock not acquired
                    logger.error("Cannot aquire lock during IATI update for activity " + activityId);
                    Long editingUserId = ActivityGatekeeper.getUserEditing(activityId);
                    String memberName = TeamMemberUtil.getTeamMember(editingUserId).getMemberName();
                    errors.put(ActivityErrors.ACTIVITY_IS_BEING_EDITED.id,
                            ActivityErrors.ACTIVITY_IS_BEING_EDITED.withDetails(memberName));
                }

                newActivity = oldActivity;
                oldActivity = ActivityVersionUtil.cloneActivity(oldActivity);
                oldActivity.setAmpId(newActivity.getAmpId());
                oldActivity.setAmpActivityGroup(newActivity.getAmpActivityGroup().clone());

                PersistenceManager.getSession().evict(newActivity.getAmpActivityGroup());
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
                newActivity = ActivityUtil.saveActivityNewVersion(newActivity, translations, modifiedBy,
                        Boolean.TRUE.equals(newActivity.getDraft()), draftChange,
                        PersistenceManager.getSession(), saveContext);

                postProcess();
            } else {
                // undo any pending changes
                PersistenceManager.getSession().clear();
            }

            updateResponse(update);

            PersistenceManager.flushAndCommit(PersistenceManager.getSession());
        } catch (Throwable e) {
            // error is not always logged at source; better duplicate it than have none
            logger.error(e);
            PersistenceManager.rollbackCurrentSessionTx();

            if (e instanceof StaleStateException) {
                String details = "Latest version is " + currentVersion;
                ApiErrorMessage error = ActivityErrors.ACTIVITY_IS_STALE.withDetails(details);
                errors.put(error.id, error);
            } else if (errors.isEmpty()) {
                throw new RuntimeException(e);
            } else {
                ApiExceptionMapper aem = new ApiExceptionMapper();
                ApiErrorMessage apiErrorMessageFromException = aem.getApiErrorMessageFromException(e);
                errors.put(apiErrorMessageFromException.id, apiErrorMessageFromException);
            }
        } finally {
            ActivityGatekeeper.unlockActivity(activityId, key);
            ActivityValidationContext.set(null);
        }

        return this;
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
     * @param teamMember team member to check
     *
     */
    private void checkPermissions(boolean update, Long ampActivityId, AmpTeamMember teamMember) {
        if (update) {
            checkEditPermissions(teamMember, ampActivityId);
        } else {
            checkAddPermissions(teamMember);
        }
    }

    /**
     * Check if team member can add activities.
     */
    private void checkAddPermissions(AmpTeamMember teamMember) {
        if (!ActivityInterchangeUtils.addActivityAllowed(new TeamMember(teamMember))) {
            addError(SecurityErrors.NOT_ALLOWED.withDetails("Adding activity is not allowed"));
        }
    }

    /**
     * Check if team member can edit the activity.
     */
    private void checkEditPermissions(AmpTeamMember ampTeamMember, Long activityId) {
        if (!ActivityInterchangeUtils.isEditableActivity(new TeamMember(ampTeamMember), activityId)) {
            addError(SecurityErrors.NOT_ALLOWED.withDetails("No right to edit this activity"));
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
        List<AmpContentTranslation> trnList = ContentTranslationUtil.loadFieldTranslations(objectClass, objId,
                apiField.getFieldNameInternal());
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
                String fieldName = apiField.getFieldNameInternal();
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
        if (isMultilingual)
            translations.addAll(trnList);
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
    protected String extractTextTranslations(APIField apiField, Object parentObj, Map<String, Object> trnJson) {
        String key = null;
        if (update) { // all editor keys must exist before
            key = (String) apiField.getFieldAccessor().get(parentObj);
        }
        if (key == null) { // init it in any case
            key = getEditorKey(apiField.getFieldNameInternal());
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
                    editor = DbUtil.createEditor(currentUser, langCode, sourceURL, key, null, translation,
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

    protected void postProcess() {
        String rootPath = TLSUtils.getRequest().getServletContext().getRealPath("/");
        Site site = TLSUtils.getSite();
        Locale lang = Locale.forLanguageTag(trnSettings.getDefaultLangCode());
        LuceneUtil.addUpdateActivity(rootPath, update, site, lang, newActivity, oldActivity, translations);
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
     * @return the isMultilingual
     */
    public boolean isMultilingual() {
        return isMultilingual;
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

    /**
     * @param latestActivityId
     */
    public void setLatestActivityId(Long latestActivityId) {
        this.latestActivityId = latestActivityId;
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

}
