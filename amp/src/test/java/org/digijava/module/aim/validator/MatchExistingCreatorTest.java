package org.digijava.module.aim.validator;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.validator.user.MatchExistingCreator;
import org.digijava.module.aim.validator.user.MatchExistingCreatorConstraint;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.digijava.module.aim.validator.ConstraintMatchers.hasViolation;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Nadejda Mandrescu
 */
public class MatchExistingCreatorTest extends AbstractActivityValidatorTest<MatchExistingCreatorConstraint> {

    private static final Long TM2 = 2l;

    private AmpTeamMember newCreatedBy;
    private AmpActivityVersion oldActivity;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        oldActivity = mock(AmpActivityVersion.class);
        when(oldActivity.getActivityCreator()).thenReturn(ampTeamMember);

        newCreatedBy = mock(AmpTeamMember.class);
        when(newCreatedBy.getAmpTeamMemId()).thenReturn(TM2);
    }

    @Test
    public void testNotAppliedInHibernate() {
        AmpActivity activity = new AmpActivity();

        Set<ConstraintViolation<AmpActivity>> violations = getValidator().validate(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNewActivityNoCreator() {
        AmpActivity activity = new AmpActivity();

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(creatorViolation()));
    }

    @Test
    public void testNewActivityCreator() {
        AmpActivity activity = configure(new AmpActivity(), newCreatedBy, null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testExistingActivityMatchingCreator() {
        AmpActivity activity = configure(new AmpActivity(), ampTeamMember, oldActivity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testExistingActivityNoCreator() {
        AmpActivity activity = configure(new AmpActivity(), null, oldActivity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(creatorViolation()));
    }

    @Test
    public void testExistingActivityNotMatchingCreator() {
        AmpActivity activity = configure(new AmpActivity(), newCreatedBy, oldActivity);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(creatorViolation()));
    }

    private AmpActivity configure(AmpActivity newActivity, AmpTeamMember newCreatedBy,
            AmpActivityVersion oldActivity) {
        newActivity.setActivityCreator(newCreatedBy);
        ActivityValidationContext.getOrThrow().setOldActivity(oldActivity);
        return newActivity;
    }

    private Matcher<ConstraintViolation> creatorViolation() {
        return hasViolation(MatchExistingCreator.class);
    }

}
