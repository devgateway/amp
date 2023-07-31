package org.digijava.module.aim.validator;

import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_FOR_ALL_EDITS;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_FOR_NEW_ONLY;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_OFF;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_ON;
import static org.digijava.module.aim.validator.ConstraintMatchers.hasViolation;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.validator.approval.AllowedApprover;
import org.digijava.module.aim.validator.approval.AllowedApproverConstraint;
import org.hamcrest.Matcher;
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
@PrepareForTest({ FeaturesUtil.class, DbUtil.class })
public class AllowedApproverConstraintTest extends AbstractActivityValidatorTest<AllowedApproverConstraint> {

    private static final Long NOT_APPROVER_TEAM_MEMBER_ID = 200l;
    private static final Long CROSS_TEAM_ID = 15l;

    private AmpTeamMember notApprover;
    private AmpTeam computedAmpTeam;
    private AmpTeamMemberRoles notApproverRoles;


    @Override
    @Before
    public void setUp() {
        super.setUp();
        PowerMockito.mockStatic(FeaturesUtil.class);
        PowerMockito.mockStatic(DbUtil.class);

        computedAmpTeam = mock(AmpTeam.class);
        when(computedAmpTeam.getAmpTeamId()).thenReturn(CROSS_TEAM_ID);
        when(computedAmpTeam.getCrossteamvalidation()).thenReturn(Boolean.TRUE);

        notApproverRoles = mock(AmpTeamMemberRoles.class);
        when(notApproverRoles.isApprover()).thenReturn(false);
        when(notApproverRoles.getTeamHead()).thenReturn(false);

        notApprover = mock(AmpTeamMember.class);
        when(notApprover.getAmpTeamMemId()).thenReturn(NOT_APPROVER_TEAM_MEMBER_ID);
        when(notApprover.getAmpTeam()).thenReturn(ampTeam);
        when(notApprover.getAmpMemberRole()).thenReturn(notApproverRoles);
    }

    @Test
    public void testNotAppliedInHibernate() {
        AmpActivity activity = new AmpActivity();

        Set<ConstraintViolation<AmpActivity>> violations = getValidator().validate(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidApprovedByWhenNoApprovalStatus() {
        AmpActivity newActivity = getDefaultActivity();
        newActivity.setApprovedBy(ampTeamMember);

        mockValidation("any", "any", newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, contains(approverViolation()));
    }

    @Test
    public void testValidNullApprovedByWhenNoApprovalStatus() {
        AmpActivity newActivity = getDefaultActivity();

        mockValidation("any", "any", newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidNullApprovedByWhenNewActivityNotApproved() {
        AmpActivity newActivity = getAmpActivity(null, notApprover, ApprovalStatus.started);

        mockValidation("any", "any", newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidNullApprovedByWhenPreviouselyApprovedActivityNotApproved() {
        AmpActivity newActivity = getAmpActivity(null, notApprover, ApprovalStatus.edited);
        AmpActivity oldActivity = getAmpActivity(ampTeamMember, ampTeamMember, ApprovalStatus.approved);

        mockValidation("any", "any", newActivity, oldActivity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, contains(approverViolation()));
    }

    @Test
    public void testNullApprovedByWhenApproved() {
        AmpActivity newActivity = getAmpActivity(null, ampTeamMember, ApprovalStatus.approved);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, contains(approverViolation()));
    }

    @Test
    public void testValidApprovedByWhenNotApprovedAndApproverCannotApproveNow() {
        AmpActivity newActivity = getAmpActivity(notApprover, ampTeamMember, ApprovalStatus.edited);

        mockValidation("any", "any", newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, emptyIterable());

    }

    @Test
    public void testValidApprovedByWhenNotMatchingModifiedByValidateNewOnly() {
        AmpActivity newActivity = getAmpActivity(notApprover, ampTeamMember, ApprovalStatus.approved);
        AmpActivity oldActivity = getAmpActivity(notApprover, notApprover, ApprovalStatus.approved);
        oldActivity.setApprovalDate(new Date());
        newActivity.setApprovalDate(oldActivity.getApprovalDate());

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, newActivity, oldActivity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, emptyIterable());
    }

    public void testInvalidApprovedByForValidatedActivityWhenNotMatchingModifiedByAndPastApprovedByValidateNewOnly() {
        AmpActivity newActivity = getAmpActivity(notApprover, ampTeamMember, ApprovalStatus.approved);
        AmpActivity oldActivity = getAmpActivity(ampTeamMember, ampTeamMember, ApprovalStatus.approved);
        oldActivity.setApprovalDate(new Date());
        newActivity.setApprovalDate(oldActivity.getApprovalDate());

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, newActivity, oldActivity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, contains(approverViolation()));
    }

    @Test
    public void testInvalidApprovedByForUnvalidatedActivityWhenNotMatchingModifiedByAndPastAppDateValidateNewOnly() {
        AmpActivity newActivity = getAmpActivity(notApprover, ampTeamMember, ApprovalStatus.approved);
        AmpActivity oldActivity = getAmpActivity(ampTeamMember, ampTeamMember, ApprovalStatus.approved);
        
        Date approvalDate = new Date();
        oldActivity.setApprovalDate(approvalDate);
        newActivity.setApprovalDate(approvalDate);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, newActivity, oldActivity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, contains(approverViolation()));
    }

    @Test
    public void testInvalidApprovedByForValidatedNewActivityWhenNotMatchingModifiedBy() {
        AmpActivity newActivity = getAmpActivity(notApprover, ampTeamMember, ApprovalStatus.approved);

        mockValidation("any", "any", newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, contains(approverViolation()));
    }

    @Test
    public void testInvalidApprovedByForValidatedExistingActivityWhenNotMatchingModifiedByValidateAll() {
        AmpActivity newActivity = getAmpActivity(notApprover, ampTeamMember, ApprovalStatus.approved);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, contains(approverViolation()));
    }

    @Test
    public void testValidApprovedByForNewActivityValidationOn() {
        AmpActivity newActivity = getAmpActivity(ampTeamMember, ampTeamMember, ApprovalStatus.approved);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidApprovedByForNewActivityValidationOff() {
        AmpActivity newActivity = getAmpActivity(ampTeamMember, ampTeamMember, ApprovalStatus.started_approved);

        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidApprovedByWhenValidationIsOffForOldActivity() {
        AmpActivity newActivity = getAmpActivity(ampTeamMember, ampTeamMember, ApprovalStatus.approved);
        AmpActivity oldActivity = getAmpActivity(ampTeamMember, ampTeamMember, ApprovalStatus.started_approved);

        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, newActivity, oldActivity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidApprovedByWhenValidationIsForNewOnlyExistingActivity() {
        AmpActivity newActivity = getAmpActivity(notApprover, notApprover, ApprovalStatus.approved);
        AmpActivity oldActivity = getAmpActivity(ampTeamMember, ampTeamMember, ApprovalStatus.started);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, newActivity, oldActivity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, contains(approverViolation()));
    }

    @Test
    public void testInvalidApprovedByWhenValidationIsForNewOnlyNewActivity() {
        AmpActivity newActivity = getAmpActivity(notApprover, notApprover, ApprovalStatus.approved);
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_NEW_ONLY, newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, contains(approverViolation()));
    }

    @Test
    public void testValidApprovedByWhenCrossTeamValidation() {
        AmpTeamMember crossTeamValidator = ampTeamMember;
        when(ampTeamMember.getAmpTeam()).thenReturn(computedAmpTeam);

        AmpActivity newActivity = getAmpActivity(crossTeamValidator, crossTeamValidator, ApprovalStatus.approved);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidApprovedByWhenCrossTeamValidationIsOff() {
        AmpTeamMember crossTeamValidator = ampTeamMember;
        when(ampTeamMember.getAmpTeam()).thenReturn(computedAmpTeam);
        when(computedAmpTeam.getCrossteamvalidation()).thenReturn(Boolean.FALSE);

        AmpActivity newActivity = getAmpActivity(crossTeamValidator, crossTeamValidator, ApprovalStatus.approved);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, newActivity, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(newActivity);

        assertThat(violations, contains(approverViolation()));
    }

    private void mockValidation(String gsValue, String ampTeamValue, AmpActivity newActivity, AmpActivity oldActivity) {
        when(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION)).thenReturn(gsValue);
        when(DbUtil.getValidationFromTeamAppSettings(Matchers.anyLong())).thenReturn(ampTeamValue);
        ActivityValidationContext.getOrThrow().setNewActivity(newActivity);
        ActivityValidationContext.getOrThrow().setOldActivity(oldActivity);
    }

    private AmpActivity getAmpActivity(AmpTeamMember approvedBy, AmpTeamMember modifiedBy,
            ApprovalStatus approvalStatus) {
        AmpActivity activity = getDefaultActivity();
        activity.setDraft(false);
        activity.setApprovedBy(approvedBy);
        activity.setModifiedBy(modifiedBy);
        activity.setApprovalStatus(approvalStatus);
        return activity;
    }

    private Matcher<ConstraintViolation> approverViolation() {
        return hasViolation(AllowedApprover.class);
    }
}
