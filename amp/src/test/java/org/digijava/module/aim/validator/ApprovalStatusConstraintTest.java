package org.digijava.module.aim.validator;

import static org.digijava.module.aim.dbentity.ApprovalStatus.approved;
import static org.digijava.module.aim.dbentity.ApprovalStatus.rejected;
import static org.digijava.module.aim.dbentity.ApprovalStatus.started;
import static org.digijava.module.aim.dbentity.ApprovalStatus.started_approved;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_FOR_ALL_EDITS;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_OFF;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_ON;
import static org.digijava.module.aim.validator.ConstraintMatchers.hasViolation;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.validator.approval.AllowedApprovalStatus;
import org.digijava.module.aim.validator.approval.ApprovalStatusConstraint;
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
public class ApprovalStatusConstraintTest extends AbstractActivityValidatorTest<ApprovalStatusConstraint> {

    @Override
    @Before
    public void setUp() {
        super.setUp();
        PowerMockito.mockStatic(FeaturesUtil.class);
        PowerMockito.mockStatic(DbUtil.class);
    }

    @Test
    public void testNotAppliedInHibernate() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(false);

        Set<ConstraintViolation<AmpActivity>> violations = getValidator().validate(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNoApprovalStatus() {
        AmpActivity activity = new AmpActivity();

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testNeedsApprovalNewDraftActivityValidationOn() {
        AmpActivity activity = new AmpActivity();
        activity.setApprovalStatus(started);
        activity.setDraft(true);
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNeedsApprovalNewSubmittedActivityByApproverValidationOn() {
        AmpActivity activity = new AmpActivity();
        activity.setApprovalStatus(started);
        activity.setDraft(false);
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testNeedsApprovalNewSubmittedActivityByNonApproverValidationOn() {
        AmpActivity activity = new AmpActivity();
        activity.setApprovalStatus(started);
        activity.setDraft(false);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);
        when(roles.getTeamHead()).thenReturn(false);
        when(roles.isApprover()).thenReturn(false);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNeedsApprovalNewSubmittedActivityByNonApproverValidationOff() {
        AmpActivity activity = new AmpActivity();
        activity.setApprovalStatus(started);

        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testRejectedDraftActivityByApprover() throws CloneNotSupportedException {
        AmpActivity oldActivity = new AmpActivity();
        oldActivity.setAmpActivityId(1L);
        oldActivity.setDraft(false);
        ActivityValidationContext.getOrThrow().setOldActivity(oldActivity);
        
        AmpActivity activity = (AmpActivity) oldActivity.clone();
        activity.setApprovalStatus(rejected);
        activity.setDraft(true);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }
    
    @Test
    public void testRejectedNewDraftActivityByApprover() {
        AmpActivity activity = new AmpActivity();
        activity.setApprovalStatus(rejected);
        activity.setDraft(true);
        
        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testRejectedNewDraftActivityByNonApprover() {
        AmpActivity activity = new AmpActivity();
        activity.setApprovalStatus(rejected);
        activity.setDraft(true);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);
        when(roles.getTeamHead()).thenReturn(false);
        when(roles.isApprover()).thenReturn(false);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testApprovalStatusApprovedOnDraft() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(true);
        activity.setApprovalStatus(approved);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testSubmitNewApprovedActivityValidationOff() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(false);
        activity.setApprovalStatus(approved);

        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testDraftNewApprovedActivityValidationOff() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(true);
        activity.setApprovalStatus(approved);

        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testSubmitNewStartedApprovedActivityValidationOff() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(false);
        activity.setApprovalStatus(started_approved);

        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testDraftNewStartedApprovedActivityValidationOff() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(true);
        activity.setApprovalStatus(started_approved);

        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testSubmitNewStartedApprovedActivityValidationOn() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(false);
        activity.setApprovalStatus(started_approved);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testSubmitNewApprovedActivityValidationOn() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(false);
        activity.setApprovalStatus(approved);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    private void mockValidation(String gsValidation, String teamValidation, AmpActivity activity) {
        when(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION)).thenReturn(gsValidation);
        when(DbUtil.getValidationFromTeamAppSettings(Matchers.anyLong())).thenReturn(teamValidation);
        activity.setApprovedBy(ampTeamMember);
        activity.setModifiedBy(ampTeamMember);
        activity.setTeam(ampTeam);
        ActivityValidationContext.getOrThrow().setNewActivity(activity);
    }

    private Matcher<ConstraintViolation> approvalStatusViolation() {
        return hasViolation(AllowedApprovalStatus.class);
    }

}
