package org.digijava.kernel.ampapi.endpoints.activity.validators;

import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_FOR_ALL_EDITS;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_FOR_NEW_ONLY;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_OFF;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_ON;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityImportRules;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Nadejda Mandrescu
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ TeamMemberUtil.class, FeaturesUtil.class, DbUtil.class })
public class ApprovedByValidatorTest {
    private static final String APPROVAL_STATUS_FIELD = FieldMap.underscorify(ActivityFieldsConstants.APPROVAL_STATUS);
    private static final String APPROVED_BY_FIELD = FieldMap.underscorify(ActivityFieldsConstants.APPROVED_BY);
    private static final String APPROVAL_DATE_FIELD = FieldMap.underscorify(ActivityFieldsConstants.APPROVAL_DATE);
    private static final String TEAM_FIELD = FieldMap.underscorify(ActivityFieldsConstants.TEAM);

    private ActivityImporter importer;
    private APIField approvedByFieldDesc;

    private static final Long VALID_TEAM_MEMBER_ID = 100l;
    private static final Long INVALID_TEAM_MEMBER_ID = 200l;
    private static final Long VALID_TEAM_ID = 5l;
    private static final Long VALID_CROSS_TEAM_ID = 15l;
    private static final Long INVALID_TEAM_ID = 20l;

    private AmpTeamMember invalidAmpTeamMember;
    private AmpTeamMember validAmpTeamMember;
    private AmpTeam validAmpTeam;
    private AmpTeam invalidAmpTeam;
    private AmpTeamMemberRoles teamHeadApproverRoles;
    private AmpTeamMemberRoles notApproverRoles;
    private ActivityImportRules importRules;

    @Before
    public void setUp() throws Exception {
        importer = mock(ActivityImporter.class);
        importRules = mock(ActivityImportRules.class);
        when(importer.getImportRules()).thenReturn(importRules);

        PowerMockito.mockStatic(TeamMemberUtil.class);
        PowerMockito.mockStatic(FeaturesUtil.class);
        PowerMockito.mockStatic(DbUtil.class);

        validAmpTeamMember = mock(AmpTeamMember.class);
        when(validAmpTeamMember.getAmpTeamMemId()).thenReturn(VALID_TEAM_MEMBER_ID);
        when(TeamMemberUtil.getAmpTeamMember(VALID_TEAM_MEMBER_ID)).thenReturn(validAmpTeamMember);

        validAmpTeam = mock(AmpTeam.class);
        when(validAmpTeamMember.getAmpTeam()).thenReturn(validAmpTeam);
        when(validAmpTeam.getAmpTeamId()).thenReturn(VALID_TEAM_ID);

        teamHeadApproverRoles = mock(AmpTeamMemberRoles.class);
        when(teamHeadApproverRoles.isApprover()).thenReturn(true);
        when(validAmpTeamMember.getAmpMemberRole()).thenReturn(teamHeadApproverRoles);

        notApproverRoles = mock(AmpTeamMemberRoles.class);
        when(notApproverRoles.isApprover()).thenReturn(false);
        when(notApproverRoles.getTeamHead()).thenReturn(false);

        invalidAmpTeamMember = mock(AmpTeamMember.class);
        when(invalidAmpTeamMember.getAmpTeamMemId()).thenReturn(INVALID_TEAM_MEMBER_ID);
        when(TeamMemberUtil.getAmpTeamMember(INVALID_TEAM_MEMBER_ID)).thenReturn(invalidAmpTeamMember);
        when(invalidAmpTeamMember.getAmpMemberRole()).thenReturn(notApproverRoles);

        invalidAmpTeam = mock(AmpTeam.class);
        when(invalidAmpTeam.getAmpTeamId()).thenReturn(INVALID_TEAM_ID);
        when(invalidAmpTeamMember.getAmpTeam()).thenReturn(invalidAmpTeam);

        approvedByFieldDesc = new APIField();
        approvedByFieldDesc.setFieldName(APPROVED_BY_FIELD);
        approvedByFieldDesc.setImportable(true);
        approvedByFieldDesc.setApiType(new APIType(Long.class));
    }

    @Test
    public void testValidApprovedByWhenNotRequestedToProcess() {
        when(importRules.isProcessApprovalFields()).thenReturn(false);

        ApprovedByValidator validator = new ApprovedByValidator();
        Map<String, Object> activity = invalidApprovalFields();

        assertTrue("Approval fields must be valid when not requested to be processed",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testInvalidApprovedByWhenRequestedToProcess() {
        when(importRules.isProcessApprovalFields()).thenReturn(true);

        ApprovedByValidator validator = new ApprovedByValidator();
        Map<String, Object> activity = invalidApprovalFields();

        assertFalse("Invalid approved by must be dected when not requested to be processed",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    private Map<String, Object> invalidApprovalFields() {
        Map<String, Object> activity = new HashMap<>();
        activity.put(APPROVED_BY_FIELD, 1l);
        return activity;
    }

    @Test
    public void testValidApprovedByWhenRequestedToProcess() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, validAmpTeamMember);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_TEAM_ID, ApprovalStatus.APPROVED);

        assertTrue("Approved by must be valid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testValidApprovedByWhenValidationIsOffForNewActivity() {
        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, validAmpTeamMember);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_TEAM_ID,
                ApprovalStatus.STARTED_APPROVED);

        assertTrue("Approved by must be valid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testValidApprovedByWhenValidationIsOffForOldActivity() {
        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, validAmpTeamMember);

        AmpActivityVersion ampActivity = mock(AmpActivityVersion.class);
        when(importer.getOldActivity()).thenReturn(ampActivity);
        when(ampActivity.getApprovalStatus()).thenReturn(ApprovalStatus.STARTED_APPROVED);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_TEAM_ID, ApprovalStatus.APPROVED);

        assertTrue("Approved by must be valid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testValidApprovedByWhenValidationIsForNewOnly() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, validAmpTeamMember);
        when(validAmpTeamMember.getAmpMemberRole()).thenReturn(notApproverRoles);

        AmpActivityVersion ampActivity = mock(AmpActivityVersion.class);
        when(importer.getOldActivity()).thenReturn(ampActivity);
        when(ampActivity.getApprovalStatus()).thenReturn(ApprovalStatus.APPROVED);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_TEAM_ID, ApprovalStatus.APPROVED);

        assertTrue("Approved by must be valid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testInvalidApprovedByWhenValidationIsForNewOnlyExistingActivity() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, validAmpTeamMember);
        when(validAmpTeamMember.getAmpMemberRole()).thenReturn(notApproverRoles);

        AmpActivityVersion ampActivity = mock(AmpActivityVersion.class);
        when(importer.getOldActivity()).thenReturn(ampActivity);
        when(ampActivity.getApprovalStatus()).thenReturn(ApprovalStatus.STARTED);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_TEAM_ID, ApprovalStatus.APPROVED);

        assertFalse("Approved by must be invalid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testInvalidApprovedByWhenValidationIsForNewOnlyNewActivity() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, validAmpTeamMember);
        when(validAmpTeamMember.getAmpMemberRole()).thenReturn(notApproverRoles);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_TEAM_ID, ApprovalStatus.APPROVED);

        assertFalse("Approved by must be invalid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testValidApprovedByWhenNoApprovalStatusSet() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, validAmpTeamMember);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(null, VALID_TEAM_ID, null);

        assertTrue("Approved by must be valid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testInvalidApprovedByWhenNoApprovalStatusSet() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, validAmpTeamMember);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_TEAM_ID, null);

        assertFalse("Approved by must be invalid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testInvalidApprovedByWhenInvalidApprovalStatus() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, validAmpTeamMember);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_TEAM_ID, null);
        activity.put(APPROVAL_STATUS_FIELD, 999l);

        assertFalse("Approved by must be invalid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testValidApprovedByWhenNotApprovedAndInvalidApprover() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, validAmpTeamMember);
        when(validAmpTeamMember.getAmpMemberRole()).thenReturn(notApproverRoles);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_TEAM_ID, ApprovalStatus.EDITED);

        assertTrue("Approved by must be valid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }


    @Test
    public void testValidApprovedByWhenNotMatchingModifiedByValidateNewOnly() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, validAmpTeamMember);
        // reusing the mock with another name to avoid confusion
        AmpTeamMember crossTeamApprover = invalidAmpTeamMember;

        AmpActivityVersion ampActivity = mock(AmpActivityVersion.class);
        when(ampActivity.getApprovedBy()).thenReturn(crossTeamApprover);
        when(importer.getOldActivity()).thenReturn(ampActivity);
        when(ampActivity.getApprovalDate()).thenReturn(new Date());
        when(ampActivity.getApprovalStatus()).thenReturn(ApprovalStatus.APPROVED);

        when(crossTeamApprover.getAmpMemberRole()).thenReturn(teamHeadApproverRoles);
        when(crossTeamApprover.getAmpTeam().getCrossteamvalidation()).thenReturn(true);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(INVALID_TEAM_MEMBER_ID, VALID_TEAM_ID, ApprovalStatus.APPROVED);

        assertTrue("Approved by must be valid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testInvalidApprovedByForValidatedActivityWhenNotMatchingModifiedByAndPastApprovalValidateNewOnly() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, validAmpTeamMember);
        Date pastApprovalDate = new Date();

        AmpActivityVersion ampActivity = mock(AmpActivityVersion.class);
        when(ampActivity.getApprovedBy()).thenReturn(validAmpTeamMember);
        when(ampActivity.getApprovalDate()).thenReturn(pastApprovalDate);
        when(importer.getOldActivity()).thenReturn(ampActivity);
        when(ampActivity.getApprovalStatus()).thenReturn(ApprovalStatus.APPROVED);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(INVALID_TEAM_MEMBER_ID, VALID_TEAM_ID, ApprovalStatus.APPROVED);
        activity.put(APPROVAL_DATE_FIELD, DateTimeUtil.formatISO8601Timestamp(pastApprovalDate));

        assertFalse("Approved by must be invalid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testInvalidApprovedByForUnvalidatedActivityWhenNotMatchingModifiedByValidateNewOnly() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, validAmpTeamMember);

        AmpActivityVersion ampActivity = mock(AmpActivityVersion.class);
        when(importer.getOldActivity()).thenReturn(ampActivity);
        when(ampActivity.getApprovalStatus()).thenReturn(ApprovalStatus.STARTED);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(INVALID_TEAM_MEMBER_ID, VALID_TEAM_ID, ApprovalStatus.APPROVED);

        assertFalse("Approved by must be invalid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testInvalidApprovedByWhenNotMatchingModifiedByValidateAllEdits() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, invalidAmpTeamMember);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_TEAM_ID, ApprovalStatus.APPROVED);

        assertFalse("Approved by must be invalid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    @Test
    public void testValidApprovedByWhenCrossTeamValidation() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, validAmpTeamMember);
        when(validAmpTeam.getCrossteamvalidation()).thenReturn(true);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_CROSS_TEAM_ID,
                ApprovalStatus.APPROVED);

        assertTrue("Approved by must be valid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }


    @Test
    public void testInvalidApprovedByWhenCrossTeamValidationIsOff() {
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, validAmpTeamMember);
        when(validAmpTeam.getCrossteamvalidation()).thenReturn(false);

        ApprovedByValidator validator = new ApprovedByValidator();

        Map<String, Object> activity = approvalFields(VALID_TEAM_MEMBER_ID, VALID_CROSS_TEAM_ID,
                ApprovalStatus.APPROVED);

        assertFalse("Approved by must be invalid",
                validator.isValid(importer, activity, approvedByFieldDesc, APPROVED_BY_FIELD));
    }

    private void mockValidation(String gsValue, String ampTeamValue, AmpTeamMember modifiedBy) {
        when(importer.getModifiedBy()).thenReturn(modifiedBy);
        when(importRules.isProcessApprovalFields()).thenReturn(true);
        when(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION)).thenReturn(gsValue);
        when(DbUtil.getValidationFromTeamAppSettings(Matchers.anyLong())).thenReturn(ampTeamValue);
    }

    private Map<String, Object> approvalFields(Long approvedBy, Long teamId, ApprovalStatus approvalStatus) {
        Map<String, Object> activity = new HashMap<>();
        activity.put(APPROVAL_STATUS_FIELD, approvalStatus == null ? null : approvalStatus.getId());
        activity.put(APPROVED_BY_FIELD, approvedBy);
        activity.put(TEAM_FIELD, teamId);
        return activity;
    }

}
