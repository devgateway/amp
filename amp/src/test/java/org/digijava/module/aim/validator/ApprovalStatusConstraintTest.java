package org.digijava.module.aim.validator;

import static org.digijava.module.aim.dbentity.ApprovalStatus.APPROVED;
import static org.digijava.module.aim.dbentity.ApprovalStatus.STARTED;
import static org.digijava.module.aim.dbentity.ApprovalStatus.STARTED_APPROVED;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_FOR_ALL_EDITS;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_OFF;
import static org.digijava.module.aim.helper.Constants.PROJECT_VALIDATION_ON;
import static org.digijava.module.aim.validator.ConstraintMatchers.hasViolation;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.validator.approval.ApprovalStatus;
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
public class ApprovalStatusConstraintTest extends AbstractValidatorTest<ApprovalStatusConstraint> {

    private static final Long TEAM_ID = 1l;

    private AmpTeamMember ampTeamMember;
    private AmpTeam ampTeam;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        PowerMockito.mockStatic(FeaturesUtil.class);
        PowerMockito.mockStatic(DbUtil.class);

        ampTeamMember = mock(AmpTeamMember.class);
        ampTeam = mock(AmpTeam.class);
        when(ampTeamMember.getAmpTeam()).thenReturn(ampTeam);
        when(ampTeam.getAmpTeamId()).thenReturn(TEAM_ID);
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
    public void testApprovalStatusNeedsApproval() {
        AmpActivity activity = new AmpActivity();
        activity.setApprovalStatus(STARTED);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testApprovalStatusApprovedOnDraft() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(true);
        activity.setApprovalStatus(APPROVED);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testSubmitNewApprovedActivityValidationOff() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(false);
        activity.setApprovalStatus(APPROVED);

        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testSubmitNewStartedApprovedActivityValidationOff() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(false);
        activity.setApprovalStatus(STARTED_APPROVED);

        mockValidation(PROJECT_VALIDATION_OFF, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testSubmitNewStartedApprovedActivityValidationOn() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(false);
        activity.setApprovalStatus(STARTED_APPROVED);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(approvalStatusViolation()));
    }

    @Test
    public void testSubmitNewApprovedActivityValidationOn() {
        AmpActivity activity = new AmpActivity();
        activity.setDraft(false);
        activity.setApprovalStatus(APPROVED);

        mockValidation(PROJECT_VALIDATION_ON, PROJECT_VALIDATION_FOR_ALL_EDITS, activity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    private void mockValidation(String gsValidation, String teamValidation, AmpActivity activity) {
        when(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION)).thenReturn(gsValidation);
        when(DbUtil.getValidationFromTeamAppSettings(Matchers.anyLong())).thenReturn(teamValidation);
        activity.setApprovedBy(ampTeamMember);
    }

    private Matcher<ConstraintViolation> approvalStatusViolation() {
        return hasViolation(ApprovalStatus.class);
    }

}
