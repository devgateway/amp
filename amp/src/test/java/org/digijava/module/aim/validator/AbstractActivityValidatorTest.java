package org.digijava.module.aim.validator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidator;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.junit.Before;

/**
 * @author Nadejda Mandrescu
 */
public abstract class AbstractActivityValidatorTest<T extends ConstraintValidator> extends AbstractValidatorTest<T> {
    protected static final Long TEAM_ID = 1l;
    protected static final Long TM1 = 1l;

    protected AmpTeamMember ampTeamMember;
    protected AmpTeamMemberRoles roles;
    protected AmpTeam ampTeam;

    public AbstractActivityValidatorTest() {
        super();
        ActivityValidationContext avc = new ActivityValidationContext();
        avc.setNewActivity(new AmpActivity());
        ActivityValidationContext.set(avc);
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        ampTeamMember = mock(AmpTeamMember.class);
        roles = mock(AmpTeamMemberRoles.class);
        ampTeam = mock(AmpTeam.class);
        when(ampTeamMember.getAmpTeamMemId()).thenReturn(TM1);
        when(ampTeamMember.getAmpTeam()).thenReturn(ampTeam);
        when(ampTeam.getAmpTeamId()).thenReturn(TEAM_ID);
        when(ampTeamMember.getAmpMemberRole()).thenReturn(roles);
        when(roles.getTeamHead()).thenReturn(true);
        when(roles.isApprover()).thenReturn(true);
    }

    public AmpActivity getDefaultActivity() {
        AmpActivity activity = new AmpActivity();
        activity.setActivityCreator(ampTeamMember);
        activity.setTeam(ampTeam);
        activity.setDraft(true);
        return activity;
    }
    
    public void setActivityInContext(AmpActivity activity) {
        ActivityValidationContext avc = new ActivityValidationContext();
        avc.setNewActivity(activity);
        ActivityValidationContext.set(avc);
    }

}
