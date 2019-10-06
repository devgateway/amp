package org.digijava.kernel.ampapi.endpoints.activity;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsMapContaining.hasValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.ampapi.filters.ClientMode;
import org.digijava.kernel.persistence.InMemoryTeamManager;
import org.digijava.kernel.persistence.InMemoryTeamMemberManager;
import org.digijava.kernel.persistence.InMemoryUserManager;
import org.digijava.kernel.persistence.PersistenceTransactionManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class ActivityImporterTest {
    
    public static final String ENDPOINT_CONTEXT_PATH = "/activity";
    public static final String SOURCE_URL = "";
    @Rule
    public AMPRequestRule ampRequestRule = new AMPRequestRule();
    
    private TestTranslatorService translatorService;
    private FMService fmService;
    private TeamMemberService tmService;
    private FieldsEnumerator enumerator;
    
    private PersistenceTransactionManager ptm;
    
    private InMemoryUserManager userManager;
    private InMemoryTeamManager teamManager;
    private InMemoryTeamMemberManager teamMemberManager;
    
    @Before
    public void setUp() {
        userManager = new InMemoryUserManager();
        teamManager = new InMemoryTeamManager();
        teamMemberManager = new InMemoryTeamMemberManager();
        userManager.init();
        teamManager.init();
        teamMemberManager.init(userManager, teamManager);
    
        ptm = new TestPersistenceTransactionManager();
        
        translatorService = new TestTranslatorService();
        fmService = new TestFMService();
        tmService = new TestTeamMemberService(teamMemberManager);
        enumerator = new FieldsEnumerator(new TestFieldInfoProvider(), fmService, translatorService, program -> false);
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
        json.put(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME, 2L);
        json.put(ActivityFieldsConstants.ACTIVITY_GROUP, ImmutableMap.of(ActivityEPConstants.VERSION_FIELD_NAME, 2L));
        
        ActivityImporter importer = buildActivityImporter(true, false);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer.errors,
                hasValue(error(SecurityErrors.NOT_ALLOWED, ActivityErrors.EDIT_ACTIVITY_NOT_ALLOWED)));
    }
    
    @Test
    public void testUpdateActivityImporterWrongModifiedBy() {
        Map<String, Object> json = new HashMap<>();
        json.put(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME, 2L);
        json.put(ActivityFieldsConstants.ACTIVITY_GROUP, ImmutableMap.of(ActivityEPConstants.VERSION_FIELD_NAME, 2L));
    
        ActivityImporter importer = buildActivityImporter(true, true, true);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer.errors,
                hasValue(error(SecurityErrors.INVALID_TEAM, ActivityErrors.INVALID_MODIFY_BY_FIELD)));
    }
    
    @Test
    public void testUpdateActivityImporterStale() {
        Map<String, Object> json = new HashMap<>();
        json.put(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME, 2L);
        json.put(ActivityEPConstants.MODIFIED_BY_FIELD_NAME, 1L);
    
        ActivityImporter importer = buildActivityImporter(true, true);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
        
        Assert.assertThat(importer.errors,
                hasValue(error(ActivityErrors.ACTIVITY_IS_STALE, ActivityErrors.ACTIVITY_NOT_LAST_VERSION)));
    }
    
//    TODO validate and create the activity in non-DB context
//    @Test
//    public void testImportActivity() {
//        Map<String, Object> json = new HashMap<>();
//        json.put(ActivityEPConstants.FIELD_TITLE, "Activity title");
//
//        ActivityImporter importer = buildActivityImporter(true, true);
//        importer.importOrUpdate(json, false, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
//
//        Assert.assertThat(importer.errors, is(emptyMap()));
//    }
    
    @Test
    public void testUpdateActivityImporter() {
        Map<String, Object> json = new HashMap<>();
        json.put(ActivityEPConstants.MODIFIED_BY_FIELD_NAME, 1L);
    
        ActivityImporter importer = buildActivityImporter(true, true);
        importer.importOrUpdate(json, true, ENDPOINT_CONTEXT_PATH, SOURCE_URL);
    
        Assert.assertThat(importer.errors, hasValue(error(ActivityErrors.FIELD_ACTIVITY_ID_NULL)));
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
        TestTeamMemberContext tmContext = getTeamMemberContext(addActivity, editActivity);
        TLSUtils.getRequest().getSession().setAttribute(Constants.CURRENT_USER, tmContext.getUser());
        TLSUtils.getRequest().getSession().setAttribute(Constants.CURRENT_MEMBER, tmContext.getTeamMember());
        
        APIField activityField = enumerator.getMetaModel(AmpActivityFields.class);
        ActivityImporter importer = new ActivityImporter(activityField, new ActivityImportRules(true, false,                isTrackEditors));
        importer.setActivityService(new TestActivityService(tmContext));
        importer.setFmService(fmService);
        importer.setTeamMemberService(tmService);
        importer.setPersistenceTransactionManager(ptm);
        
        return importer;
    }
    
    private TestTeamMemberContext getTeamMemberContext(boolean addActivity, boolean editActivity) {
        User user = userManager.getUser(InMemoryUserManager.TEST_USER_NAME);
        AmpTeam team = teamManager.getTeam(InMemoryTeamManager.TEST_TEAM_NAME);
        AmpTeamMember atm = teamMemberManager.getTeamMember(InMemoryTeamMemberManager.TEST_TEAM_MEMBER_ID);
        TeamMember teamMember = new TeamMember(user);
        teamMember.setTeamId(team.getAmpTeamId());
        
        return new TestTeamMemberContext(addActivity, editActivity, user, team, atm, teamMember);
    }
}
