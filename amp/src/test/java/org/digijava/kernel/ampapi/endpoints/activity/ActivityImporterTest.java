package org.digijava.kernel.ampapi.endpoints.activity;

import static java.util.Collections.emptyMap;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.VERSION_FIELD_NAME;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors.ACTIVITY_IS_STALE;
import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.ACTIVITY_GROUP;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_FOR_ALL_EDITS;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_ON;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsMapContaining.hasValue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.values.PossibleValuesCache;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.ampapi.filters.ClientMode;
import org.digijava.kernel.persistence.InMemoryActivityManager;
import org.digijava.kernel.persistence.InMemoryPossibleValuesDAO;
import org.digijava.kernel.persistence.InMemoryTeamManager;
import org.digijava.kernel.persistence.InMemoryTeamMemberManager;
import org.digijava.kernel.persistence.InMemoryUserManager;
import org.digijava.kernel.persistence.InMemoryValueConverter;
import org.digijava.kernel.persistence.PersistenceTransactionManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.activity.ActivityValidatorUtil;
import org.digijava.kernel.validators.activity.UniqueActivityTitleValidator;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * It is important to set the correct context during the activity importer testing.
 *
 * The objects are loaded from memory (check {@code InMemoryPossibleValuesDAO} and {@code InMemoryValueConverter})
 *
 * If the test needs new type of objects a memory, a new object manager (by implementing @code InMemoryManager)
 * and add the corresponding lines in {@code InMemoryPossibleValuesDAO} and {@code InMemoryValueConverter}
 *
 * @author Octavian Ciubotaru
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FeaturesUtil.class, DbUtil.class})
public class ActivityImporterTest {
    
    public static final String ENDPOINT_CONTEXT_PATH = "/activity";
    public static final String SOURCE_URL = "";
    
    private static final Set<String> LOCALES = ImmutableSet.of("en", "fr");
    
    private static final Set<String> DISABLED_FM_PATHS = ImmutableSet.of(
            "/Activity Form/Identification/Required Validator for Description",
            "/Activity Form/Identification/Required Validator for Multi Stakeholder Partnership",
            "/Activity Form/Identification/Required Validator for Objective",
            "/Activity Form/Planning/Required Validator for Proposed Start Date",
            "/Activity Form/Planning/Required Validator for Original Completion Date",
            "/Activity Form/Sectors/Primary Sectors/minSizeSectorsValidator",
            "/Activity Form/Sectors/Secondary Sectors/minSizeSectorsValidator",
            "/Activity Form/Sectors/Tertiary Sectors/minSizeSectorsValidator",
            "/Activity Form/Sectors/Tag Sectors/minSizeSectorsValidator",
            "/Activity Form/Location/Locations/Location required validator",
            "/Activity Form/Organizations/Donor Organization/Required Validator",
            "/Activity Form/Organizations/Responsible Organization/Required Validator",
            "/Activity Form/Organizations/Executing Agency/Required Validator",
            "/Activity Form/Organizations/Implementing Agency/Required Validator",
            "/Activity Form/Organizations/Beneficiary Agency/Required Validator",
            "/Activity Form/Organizations/Contracting Agency/Required Validator",
            "/Activity Form/Organizations/Regional Group/Required Validator",
            "/Activity Form/Organizations/Sector Group/Required Validator",
            "/Activity Form/Program/National Plan Objective/minSizeProgramValidator",
            "/Activity Form/Program/Primary Programs/minSizeProgramValidator",
            "/Activity Form/Program/Secondary Programs/minSizeProgramValidator",
            "/Activity Form/Program/Tertiary Programs/minSizeProgramValidator",
            "/Activity Form/Identification/Required Validator for Humanitarian Aid"
    );
    
    @Rule
    public AMPRequestRule ampRequestRule = new AMPRequestRule();
    
    private TestTranslatorService translatorService;
    private FeatureManagerService fmService;
    private TeamMemberService tmService;
    private FieldsEnumerator enumerator;
    
    private PersistenceTransactionManager ptm;
    
    private APIField activityField;
    private PossibleValuesEnumerator pvEnumerator;
    private PossibleValuesCache possibleValuesCached;
    private InMemoryValueConverter valueConverter;
    
    TranslationSettings trnSettings;
    
    @Before
    public void setUp() {
        TransactionUtil.setUpWorkspaceEmptyPrefixes();

        ptm = new TestPersistenceTransactionManager();
    
        translatorService = new TestTranslatorService();
        fmService = new TestFMService(ImmutableSet.of(), DISABLED_FM_PATHS);
        tmService = new TestTeamMemberService(InMemoryTeamMemberManager.getInstance());
        enumerator = new FieldsEnumerator(new TestFieldInfoProvider(), fmService, translatorService, program -> false);
    
        trnSettings = new TranslationSettings("en", "en", LOCALES, LOCALES, false);
        
        activityField = enumerator.getMetaModel(AmpActivityFields.class);
        pvEnumerator = new PossibleValuesEnumerator(new InMemoryPossibleValuesDAO(), translatorService);
    
        List<APIField> apiFields = activityField.getChildren();
    
        possibleValuesCached = new PossibleValuesCache(pvEnumerator, apiFields);
        valueConverter = new InMemoryValueConverter();
    
        PowerMockito.mockStatic(FeaturesUtil.class);
        PowerMockito.mockStatic(DbUtil.class);
        
        when(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION)).thenReturn(PROJECT_VALIDATION_ON);
        when(DbUtil.getValidationFromTeamAppSettings(org.mockito.Matchers.anyLong())).thenReturn(PROJECT_VALIDATION_FOR_ALL_EDITS);
    
        InMemoryActivityManager.getInstance().reset();
    }
    
    @Test
    public void testValidationReportUnknownField() {
        Map<String, Object> json = new HashMap<>();
        json.put("foo", "bar");

        Collection<ApiErrorMessage> actualErrors = new ArrayList<>(validateAndRetrieveImporter(json).getWarnings());
        Collection<ApiErrorMessage> expectedErrors = Arrays.asList(ValidationErrors.FIELD_INVALID.withDetails("foo"));

        assertThat(actualErrors, is(expectedErrors));
    }

    @Test
    public void testValidationIgnoreUnknownFieldInAmpOffline() {
        try {
            AmpClientModeHolder.setClientMode(ClientMode.AMP_OFFLINE);

            Map<String, Object> json = new HashMap<>();
            json.put("foo", "bar");

            Map<Integer, ApiErrorMessage> actualErrors = validate(json);

            assertThat(actualErrors, is(emptyMap()));
        } finally {
            AmpClientModeHolder.setClientMode(null);
        }
    }
    
    @Test
    public void testInsertActivityImporterAddNotAllowed() {
        Map<String, Object> json = new HashMap<>();
    
        ActivityImporter importer = buildActivityImporter(false, false);
        importer.importOrUpdate(json, false, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer.errors,
                hasValue(error(SecurityErrors.NOT_ALLOWED, ActivityErrors.ADD_ACTIVITY_NOT_ALLOWED)));
    }
    
    @Test
    public void testUpdateActivityImporterEditNotAllowed() {
        Map<String, Object> json = new HashMap<>();
        json.put(AMP_ACTIVITY_ID_FIELD_NAME, 2L);
        json.put(ACTIVITY_GROUP, ImmutableMap.of(VERSION_FIELD_NAME, 2L));
        
        ActivityImporter importer = buildActivityImporter(true, false);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer.errors,
                hasValue(error(SecurityErrors.NOT_ALLOWED, ActivityErrors.EDIT_ACTIVITY_NOT_ALLOWED)));
    }
    
    @Test
    public void testUpdateActivityImporterWrongModifiedBy() {
        Map<String, Object> json = new HashMap<>();
        json.put(AMP_ACTIVITY_ID_FIELD_NAME, 2L);
        json.put(ACTIVITY_GROUP, ImmutableMap.of(VERSION_FIELD_NAME, 2L));
    
        ActivityImporter importer = buildActivityImporter(true, true, true);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer.errors,
                hasValue(error(SecurityErrors.INVALID_TEAM, ActivityErrors.INVALID_MODIFY_BY_FIELD)));
    }
    
    @Test
    public void testUpdateActivityImporterStale() {
        Map<String, Object> json = new HashMap<>();
        json.put(AMP_ACTIVITY_ID_FIELD_NAME, 2L);
        json.put(ActivityEPConstants.MODIFIED_BY_FIELD_NAME, 1L);
    
        ActivityImporter importer = buildActivityImporter(true, true);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer.errors,
                hasValue(error(ACTIVITY_IS_STALE, ActivityErrors.ACTIVITY_NOT_LAST_VERSION)));
    }
    
    private Map<Integer, ApiErrorMessage> validate(Map<String, Object> json) {
        return validateAndRetrieveImporter(json).getErrors();
    }
    
    private ActivityImporter validateAndRetrieveImporter(Map<String, Object> json) {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setApprovalStatus(ApprovalStatus.STARTED);
        APIField activityField = new APIField();
        ActivityImporter importer = new ActivityImporter(activityField, new ActivityImportRules(true, false, false));
        importer.validateAndImport(activity, json, true);
        return importer;
    }
    
    private Matcher<ApiErrorMessage> error(ApiErrorMessage errorMessage) {
        return allOf(hasProperty("id", Matchers.is(errorMessage.id)),
                hasProperty("description", Matchers.is(errorMessage.description)));
    }
    
    private Matcher<ApiErrorMessage> error(ApiErrorMessage errorMessage, String detailMessage) {
        return allOf(
                hasProperty("id", Matchers.is(errorMessage.id)),
                hasProperty("description", Matchers.is(errorMessage.description)),
                hasProperty("values", contains(detailMessage))
        );
    }
    
    private ActivityImporter buildActivityImporter(boolean addActivity, boolean editActivity) {
        return buildActivityImporter(addActivity, editActivity, false);
    }
    
    private ActivityImporter buildActivityImporter(boolean addActivity, boolean editActivity, boolean isTrackEditors) {
        return buildActivityImporter(addActivity, editActivity, isTrackEditors, true);
    }
    
    /**
     * Build an instance of activity importer by provding paramters for setting the context.
     *
     * @param addActivity
     * @param editActivity
     * @param isTrackEditors
     * @param canDowngradeToDraft
     * @return
     */
    private ActivityImporter buildActivityImporter(boolean addActivity, boolean editActivity, boolean isTrackEditors, boolean canDowngradeToDraft) {
        TestTeamMemberContext tmContext = getTeamMemberContext(addActivity, editActivity);
        TLSUtils.getRequest().getSession().setAttribute(Constants.CURRENT_USER, tmContext.getUser());
        TLSUtils.getRequest().getSession().setAttribute(Constants.CURRENT_MEMBER, tmContext.getTeamMember());
    
        ActivityImporter importer = new ActivityImporter(activityField, new ActivityImportRules(canDowngradeToDraft, false,                isTrackEditors), valueConverter);
        importer.setActivityService(new TestActivityService(tmContext));
        importer.setFmService(fmService);
        importer.setTeamMemberService(tmService);
        importer.setPersistenceTransactionManager(ptm);
    
        importer = Mockito.spy(importer);
        
        when(importer.getPossibleValuesCache()).thenReturn(possibleValuesCached);
        when(importer.getTrnSettings()).thenReturn(trnSettings);
    
    
        Function<Supplier<Set<ConstraintViolation>>, Set<ConstraintViolation>> executorSupplier =
                supplier -> UniqueActivityTitleValidator.withDao(new ActivityValidatorUtil.DummyActivityTitleDAO(true), supplier);
        ImporterInterchangeValidator importerInterchangeValidator = new ImporterInterchangeValidator(importer.getErrors(), executorSupplier);
        when(importer.getImporterInterchangeValidator()).thenReturn(importerInterchangeValidator);
        
        return importer;
    }
    
    private TestTeamMemberContext getTeamMemberContext(boolean addActivity, boolean editActivity) {
        User user = InMemoryUserManager.getInstance().getUser(InMemoryUserManager.TEST_USER_NAME);
        AmpTeam team = InMemoryTeamManager.getInstance().getTeam(InMemoryTeamManager.TEST_TEAM_NAME);
        AmpTeamMember atm = InMemoryTeamMemberManager.getInstance().getTeamMember(InMemoryTeamMemberManager.TEST_TEAM_MEMBER_ID);
        TeamMember teamMember = new TeamMember(user);
        teamMember.setTeamId(team.getAmpTeamId());
        
        return new TestTeamMemberContext(addActivity, editActivity, user, team, atm, teamMember);
    }
    
    @Test
    public void testImportActivityDraft() {
        Map<String, Object> json = new HashMap<>();
        json.put("project_title", "Title");
        json.put("is_draft", true);
        json.put("activity_status", 263L);
        
        ActivityImporter importer = buildActivityImporter(true, true);
        importer.importOrUpdate(json, false, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
    
        Assert.assertThat(importer, allOf(
                hasProperty("errors", Matchers.is(emptyMap())),
                hasProperty("newActivity", allOf(
                        hasProperty("name", equalTo("Title")),
                        hasProperty("draft", equalTo(true))
        ))));
    }
    
    @Test
    public void testSubmitNotDraftActBudget() {
        Map<String, Object> json = new HashMap<>();
        json.put("project_title", "Title");
        json.put("is_draft", false);
        json.put("activity_status", 263L);
        json.put("activity_budget", 260L);
        
        ActivityImporter importer = buildActivityImporter(true, true, false, false);
        importer.importOrUpdate(json, false, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer, allOf(
                hasProperty("errors", Matchers.is(emptyMap())),
                hasProperty("newActivity", allOf(
                        hasProperty("name", equalTo("Title")),
                        hasProperty("draft", equalTo(false))
                ))));
    }
    
    @Test
    public void testSubmitNotDraftSimpleTextFields() {
        Map<String, Object> json = new HashMap<>();
        json.put("project_title", "Title");
        json.put("is_draft", false);
        json.put("activity_status", 263L);
        json.put("activity_budget", 260L);
        json.put("description", "Descrip");
        json.put("objective", "Objective");
        
        ActivityImporter importer = buildActivityImporter(true, true, false, false);
        importer.importOrUpdate(json, false, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer, allOf(
                hasProperty("errors", Matchers.is(emptyMap())),
                hasProperty("newActivity", allOf(
                        hasProperty("name", equalTo("Title")),
                        hasProperty("draft", equalTo(false)),
                        hasProperty("description", startsWith("aim-importer-description-")),
                        hasProperty("objective", startsWith("aim-importer-objective-"))
                ))));
    }
    
    @Test
    public void testUpdateActivityImporterWithoutId() {
        Map<String, Object> json = new HashMap<>();
        json.put(ActivityEPConstants.MODIFIED_BY_FIELD_NAME, 1L);
        
        ActivityImporter importer = buildActivityImporter(true, true);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer.errors, hasValue(error(ActivityErrors.FIELD_ACTIVITY_ID_NULL)));
    }
    
    @Test
    public void testUpdateActivityStale() {
        Map<String, Object> json = new HashMap<>();
        json.put(AMP_ACTIVITY_ID_FIELD_NAME, 1L);
        json.put("project_title", "Title");
        json.put("is_draft", true);
        json.put("activity_status", 263L);
        json.put("activity_budget", 260L);
        
        ActivityImporter importer = buildActivityImporter(true, true, false, false);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer.errors,
                hasValue(error(ActivityErrors.ACTIVITY_IS_STALE, ActivityErrors.ACTIVITY_NOT_LAST_VERSION)));
    }
    
    @Test
    public void testUpdateActivityDraftInvalidAmpId() {
        Map<String, Object> json = new HashMap<>();
        json.put(AMP_ACTIVITY_ID_FIELD_NAME, 1L);
        json.put(ACTIVITY_GROUP, ImmutableMap.of(VERSION_FIELD_NAME, 1L));
        json.put("project_title", "Title");
        json.put("is_draft", true);
        json.put("activity_status", 263L);
        json.put("activity_budget", 260L);
        
        ActivityImporter importer = buildActivityImporter(true, true, false, false);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);

        Assert.assertThat(importer.errors,
                hasValue(error(ValidationErrors.FIELD_INVALID_VALUE, "amp_id")));
    }
    
    @Test
    public void testUpdateActivityDraft() {
        Map<String, Object> json = new HashMap<>();
        json.put(AMP_ACTIVITY_ID_FIELD_NAME, 1L);
        json.put(ACTIVITY_GROUP, ImmutableMap.of(VERSION_FIELD_NAME, 1L));
        json.put("amp_id", "12345678");
        json.put("project_title", "Title");
        json.put("is_draft", true);
        json.put("activity_status", 263L);
        json.put("activity_budget", 260L);
        
        ActivityImporter importer = buildActivityImporter(true, true, false, false);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
    
        Assert.assertThat(importer, allOf(
                hasProperty("errors", Matchers.is(emptyMap())),
                hasProperty("oldActivity", allOf(
                        hasProperty("name", equalTo("Activity 1")),
                        hasProperty("draft", equalTo(false)),
                        hasProperty("ampId", equalTo("12345678")),
                        hasProperty("approvalStatus", equalTo(ApprovalStatus.STARTED_APPROVED))
                )),
                hasProperty("newActivity", allOf(
                        hasProperty("name", equalTo("Title")),
                        hasProperty("draft", equalTo(true)),
                        hasProperty("ampId", equalTo("12345678")),
                        hasProperty("approvalStatus", equalTo(ApprovalStatus.EDITED))
                ))));
    }
    
    @Test
    public void testUpdateActivitySubmit() {
        Map<String, Object> json = new HashMap<>();
        json.put(AMP_ACTIVITY_ID_FIELD_NAME, 1L);
        json.put(ACTIVITY_GROUP, ImmutableMap.of(VERSION_FIELD_NAME, 1L));
        json.put("amp_id", "12345678");
        json.put("project_title", "Title");
        json.put("is_draft", false);
        json.put("activity_status", 263L);
        json.put("activity_budget", 260L);
        
        ActivityImporter importer = buildActivityImporter(true, true, false, false);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer, allOf(
                hasProperty("errors", Matchers.is(emptyMap())),
                hasProperty("oldActivity", allOf(
                        hasProperty("name", equalTo("Activity 1")),
                        hasProperty("draft", equalTo(false)),
                        hasProperty("ampId", equalTo("12345678")),
                        hasProperty("approvalStatus", equalTo(ApprovalStatus.STARTED_APPROVED))
                )),
                hasProperty("newActivity", allOf(
                        hasProperty("name", equalTo("Title")),
                        hasProperty("draft", equalTo(false)),
                        hasProperty("ampId", equalTo("12345678")),
                        hasProperty("approvalStatus", equalTo(ApprovalStatus.EDITED))
                ))));
    }
    
}
