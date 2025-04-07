package org.digijava.module.aim.validator;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.validator.approval.AllowedApprovalStatus;
import org.digijava.module.aim.validator.approval.ApprovalStatusConstraint;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.digijava.module.aim.dbentity.ApprovalStatus.*;
import static org.digijava.module.aim.helper.Constants.*;
import static org.digijava.module.aim.validator.ConstraintMatchers.hasViolation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.mockito.Mockito.when;

/**
 * @author Nadejda Mandrescu
 */
public class ApprovalStatusConstraintTest extends AbstractActivityValidatorTest<ApprovalStatusConstraint> {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
//        Mockito.mockStatic(FeaturesUtil.class);
        Mockito.mockStatic(DbUtil.class);
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
        activity.setApprovalStatus(startedapproved);

        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testDraftNewStartedApprovedActivityValidationOff() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(true);
        activity.setApprovalStatus(startedapproved);

        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testSubmitNewStartedApprovedActivityValidationOn() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(false);
        activity.setApprovalStatus(startedapproved);

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
