package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.SaveMode.DRAFT;
import static org.digijava.kernel.ampapi.endpoints.activity.SaveMode.SUBMIT;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.dgfoundation.amp.algo.Memoizer;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.util.ChangeType;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings.TranslationType;
import org.digijava.kernel.ampapi.endpoints.activity.utils.AIHelper;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.exception.ApiExceptionMapper;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.filters.AmpOfflineModeHolder;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrgRoleBudget;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureCoordinate;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.OrganisationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
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
public class ActivityImporter extends ObjectImporter {
    private static final Logger logger = Logger.getLogger(ActivityImporter.class);
    /**
     * FM path for the "Save as Draft" feature being enabled 
     */
    private static final String SAVE_AS_DRAFT_PATH = "/Activity Form/Save as Draft";

    private AmpActivityVersion newActivity = null;
    private AmpActivityVersion oldActivity = null;
    private JsonBean oldJson = null;
    private boolean update  = false;
    private SaveMode requestedSaveMode;
    private boolean downgradedToDraftSave = false;
    private List<AmpContentTranslation> translations = new ArrayList<AmpContentTranslation>();
    private boolean isDraftFMEnabled;
    private boolean isMultilingual;
    private User currentUser;
    private String sourceURL;
    private String endpointContextPath;
    // latest activity id in case there was attempt to update older version of an activity
    private Long latestActivityId;

    private Memoizer<Map<String, AmpRole>> rolesByCode = new Memoizer<>(this::loadOrgRoles);

    private Memoizer<Map<String, AmpActivityProgramSettings>> programSettingsByName =
            new Memoizer<>(this::loadProgramSettings);

    public ActivityImporter() {
        super(AmpActivityFields.class, new InputValidatorProcessor(InputValidatorProcessor.getActivityValidators()));
    }

    private void init(JsonBean newJson, boolean update, String endpointContextPath) {
        this.sourceURL = TLSUtils.getRequest().getRequestURL().toString();
        this.update = update;
        this.currentUser = TeamUtil.getCurrentUser();
        this.newJson = newJson;
        this.isDraftFMEnabled = FMVisibility.isVisible(SAVE_AS_DRAFT_PATH, null);
        this.isMultilingual = ContentTranslationUtil.multilingualIsEnabled();
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

        List<ApiErrorMessage> saveModeErrors = determineRequestedSaveMode();
        if (!saveModeErrors.isEmpty()) {
            return saveModeErrors;
        }

        // retrieve fields definition for internal use
        List<APIField> fieldsDef = AmpFieldsEnumerator.PRIVATE_ENUMERATOR.getAllAvailableFields();
        // get existing activity if this is an update request
        Long ampActivityId = update ? AIHelper.getActivityIdOrNull(newJson) : null;

        AmpTeamMember teamMember = getModifiedBy(newJson);
        if (teamMember == null) {
            return Collections.singletonList(
                    SecurityErrors.INVALID_TEAM.withDetails("Invalid team member in modified_by field."));
        }

        if (org.dgfoundation.amp.onepager.util.ActivityUtil.isActivityStale(ampActivityId)) {
            return Collections.singletonList(
                    ActivityErrors.ACTIVITY_IS_STALE.withDetails("Activity is not the latest version."));
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

        Long currentVersion = null;

        try {
            // initialize new activity
            InterchangeUtils.getSessionWithPendingChanges();
            
            if (oldActivity != null) {
                currentVersion = oldActivity.getAmpActivityGroup().getVersion();

                key = ActivityGatekeeper.lockActivity(activityId, teamMember.getAmpTeamMemId());
                
                if (key == null){ //lock not acquired
                    logger.error("Cannot aquire lock during IATI update for activity " + activityId);
                    Long editingUserId = ActivityGatekeeper.getUserEditing(activityId);
                    String memberName = TeamMemberUtil.getTeamMember(editingUserId).getMemberName();
                    errors.put(ActivityErrors.ACTIVITY_IS_BEING_EDITED.id,
                            ActivityErrors.ACTIVITY_IS_BEING_EDITED.withDetails(memberName));
                }
                
                newActivity = oldActivity;
                // REFACTOR: we may no longer need to use old activity
                oldActivity = ActivityVersionUtil.cloneActivity(oldActivity, teamMember);
                oldActivity.setAmpId(newActivity.getAmpId());
                oldActivity.setAmpActivityGroup(newActivity.getAmpActivityGroup().clone());

                cleanImportableFields(fieldsDef, newActivity);

                if (AmpOfflineModeHolder.isAmpOfflineMode()) {
                    PersistenceManager.getSession().evict(newActivity.getAmpActivityGroup());
                    newActivity.getAmpActivityGroup().setVersion(-1L);
                }
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
                boolean updateApprovalStatus = !AmpOfflineModeHolder.isAmpOfflineMode();
                newActivity = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(newActivity,
                        translations, teamMember, Boolean.TRUE.equals(newActivity.getDraft()),
                        PersistenceManager.getSession(), SaveContext.api(updateApprovalStatus));
                postProcess();
            } else {
                // undo any pending changes
                PersistenceManager.getSession().clear();
            }
            
            updateResponse(update);

            PersistenceManager.flushAndCommit(PersistenceManager.getSession());
        } catch (Throwable e) {
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
        }
        
        return new ArrayList<ApiErrorMessage>(errors.values());
    }

    private List<ApiErrorMessage> determineRequestedSaveMode() {
        if (AmpOfflineModeHolder.isAmpOfflineMode()) {
            String draftFieldName = InterchangeUtils.underscorify(ActivityFieldsConstants.IS_DRAFT);
            Object draftAsObj = newJson.get(draftFieldName);
            if (draftAsObj == null) {
                return Collections.singletonList(ActivityErrors.FIELD_REQUIRED.withDetails(draftFieldName));
            }
            if (!(draftAsObj instanceof Boolean)) {
                return Collections.singletonList(ActivityErrors.FIELD_INVALID_TYPE.withDetails(draftFieldName));
            }
            boolean draft = (boolean) draftAsObj;
            requestedSaveMode = draft ? DRAFT : SUBMIT;
            if (requestedSaveMode == DRAFT && !isDraftFMEnabled) {
                return Collections.singletonList(ActivityErrors.SAVE_AS_DRAFT_FM_DISABLED.withDetails(draftFieldName));
            }
        }

        return Collections.emptyList();
    }

    /**
     * Determine team member responsible for modification.
     * For AMP Offline clients this is the value retrieved from modified_by field of the activity. For other clients
     * it is the session user.
     */
    public AmpTeamMember getModifiedBy(JsonBean newJson) {
        if (AmpOfflineModeHolder.isAmpOfflineMode()) {
            return TeamMemberUtil.getAmpTeamMember(AIHelper.getModifiedByOrNull(newJson));
        } else {
            return TeamMemberUtil.getCurrentAmpTeamMember(TLSUtils.getRequest());
        }
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

    protected boolean ignoreUnknownFields() {
        return AmpOfflineModeHolder.isAmpOfflineMode();
    }

    /**
     * Identifies if an existing object has to be worked with
     * @param fieldDefOfAnObject
     * @param jsonValue
     * @return
     */
    private Long getElementId(APIField fieldDefOfAnObject, Object jsonValue) {
        List<APIField> children = fieldDefOfAnObject.getChildren();
        if (children != null && jsonValue != null) {
            for (APIField childDef : children) {
                if (Boolean.TRUE.equals(childDef.isId())) {
                    String idFieldName = childDef.getFieldName();
                    String idStr = String.valueOf(((List<Map<String, Object>>) jsonValue).get(0).get(idFieldName));
                    if (StringUtils.isNumeric(idStr))
                        return Long.valueOf(idStr);
                    break;
                }
            }
        }
        return null;
    }

    @Override
    protected String extractString(Field field, Object parentObj, Object jsonValue) {
        return extractTranslationsOrSimpleValue(field, parentObj, jsonValue);
    }

    protected String extractTranslationsOrSimpleValue(Field field, Object parentObj, Object jsonValue) {
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
            key = AIHelper.getEditorKey(field.getName());
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
    
    protected void initEditor(Field field) {
        try {
            String currentValue = (String) field.get(newActivity);
            if (currentValue == null) {
                currentValue = AIHelper.getEditorKey(field.getName());
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
        if (requestedSaveMode != null) {
            newActivity.setDraft(requestedSaveMode == DRAFT);
        } else {
            // IATI draft semantics
            if (update) {
                // on update try to keep previous status
                // if validation for non-draft activity failed but it succeeded for draft activity then change to draft true
                if (isDraftFMEnabled && downgradedToDraftSave) {
                    newActivity.setDraft(true);
                }
            } else {
                newActivity.setDraft(isDraftFMEnabled);
            }
        }
        initDefaults();
    }

    protected void setupNotImportableField(Object object, Field field) {
        if (InterchangeUtils.isAmpActivityVersion(field.getType())) {
            try {
                field.set(object, newActivity);
            } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
                logger.error("Failed to set activity backwards reference.", e);
                throw new RuntimeException(e);
            }
        }
    }

    private ChangeType determineChangeType() {
        if (AmpOfflineModeHolder.isAmpOfflineMode()) {
            return ChangeType.AMP_OFFLINE;
        } else {
            return ChangeType.IMPORT;
        }
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
        updateIssues();
        updatePPCAmount();
        updateRoleFundings();
        updateOrgRoles();
        initComponents();
        initStructures();
        initDocs();
    }

    private void initDocs() {
        if (newActivity.getActivityDocuments() != null) {
            newActivity.getActivityDocuments().forEach(ad -> ad.setAmpActivity(newActivity));
        }
    }

    private void initComponents() {
        if (newActivity.getComponents() != null) {
            newActivity.getComponents().forEach(component -> initComponent(newActivity, component));
        }
    }

    private void initComponent(AmpActivityVersion activity, AmpComponent component) {
        component.setActivity(activity);
        if (component.getFundings() != null) {
            component.getFundings().forEach(f -> initComponentFunding(component, f));
        }
    }

    private void initComponentFunding(AmpComponent component, AmpComponentFunding f) {
        f.setComponent(component);
    }
    
    private void initStructures() {
        if (newActivity.getStructures() != null) {
            newActivity.getStructures().forEach(structure -> initStructure(newActivity, structure));
        }
    }
    
    private void initStructure(AmpActivityVersion activity, AmpStructure structure) {
        structure.setActivities(new HashSet<>(Arrays.asList(activity)));
        if (structure.getCoordinates() != null) {
            structure.getCoordinates().forEach(coord -> initStructureCoordinate(structure, coord));
        }
    }
    
    private void initStructureCoordinate(AmpStructure structure, AmpStructureCoordinate coord) {
        coord.setStructure(structure);
    }

    private void updateIssues() {
        if (newActivity.getIssues() != null) {
            newActivity.getIssues().forEach(issue -> initIssue(newActivity, issue));
        }
    }

    private void initIssue(AmpActivityVersion activity, AmpIssues issue) {
        issue.setActivity(activity);
        if (issue.getMeasures() != null) {
            issue.getMeasures().forEach(m -> initMeasure(issue, m));
        }
    }

    private void initMeasure(AmpIssues issue, AmpMeasure measure) {
        measure.setIssue(issue);
        if (measure.getActors() != null) {
            measure.getActors().forEach(actor -> initActor(measure, actor));
        }
    }

    private void initActor(AmpMeasure measure, AmpActor actor) {
        actor.setMeasure(measure);
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
        Iterator<AmpFunding> iterator = newActivity.getFunding().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            AmpFunding funding = iterator.next();
            funding.setIndex(i++);
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

    protected void configureCustom(Object obj, APIField fieldDef) {
        if (obj instanceof AmpActivityContact) {
            AmpActivityContact contact = (AmpActivityContact) obj;
            contact.setContactType(fieldDef.getDiscriminator());
        }
        if (obj instanceof AmpOrgRole) {
            AmpOrgRole role = (AmpOrgRole) obj;
            role.setRole(rolesByCode.get().get(fieldDef.getDiscriminator()));
        }
        if (obj instanceof AmpActivityProgram) {
            AmpActivityProgram program = (AmpActivityProgram) obj;
            program.setProgramSetting(programSettingsByName.get().get(fieldDef.getDiscriminator()));
        }
        if (obj instanceof AmpFundingAmount) {
            Integer index = Integer.valueOf(fieldDef.getDiscriminator());
            AmpFundingAmount fundingAmount = (AmpFundingAmount) obj;
            fundingAmount.setFunType(AmpFundingAmount.FundingType.values()[index]);
        }
    }

    private Map<String, AmpActivityProgramSettings> loadProgramSettings() {
        Map<String, AmpActivityProgramSettings> programSettings = new HashMap<>();
        for (AmpActivityProgramSettings setting : ProgramUtil.getAmpActivityProgramSettingsList()) {
            programSettings.put(setting.getName(), setting);
        }
        return programSettings;
    }

    private Map<String, AmpRole> loadOrgRoles() {
        Map<String, AmpRole> roles = new HashMap<>();
        for (AmpRole role : OrganisationUtil.getOrgRoles()) {
            roles.put(role.getRoleCode(), role);
        }
        return roles;
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

    /**
     * @return the sourceURL
     */
    public String getSourceURL() {
        return sourceURL;
    }
    
    public void downgradeToDraftSave() {
        this.downgradedToDraftSave = true;
    }

    /**
     * 
     * @param latestActivityId
     */
    public void setLatestActivityId(Long latestActivityId) {
        this.latestActivityId = latestActivityId;
    }
    
}
