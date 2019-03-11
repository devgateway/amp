package org.digijava.module.aim.validator;

import static org.digijava.module.aim.validator.ConstraintMatchers.iterableNode;
import static org.digijava.module.aim.validator.ConstraintMatchers.propertyNode;
import static org.digijava.module.aim.validator.ConstraintMatchers.violationWithPath;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.validator.percentage.LocationTotalPercentage;
import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class LocationTotalPercentageValidatorTest extends AbstractTotalPercentageValidatorTest {

    @Test
    public void testNotAppliedInHibernate() {
        AmpActivity activity = new AmpActivity();
        activity.setLocations(ImmutableSet.of(newActivityLocation(33f)));

        Set<ConstraintViolation<AmpActivity>> violations = getValidator().validate(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullLocations() {
        AmpActivity activity = new AmpActivity();
        activity.setLocations(null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testEmptyLocations() {
        AmpActivity activity = new AmpActivity();
        activity.setLocations(ImmutableSet.of());

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullPercentage() {
        AmpActivity activity = new AmpActivity();
        activity.setLocations(ImmutableSet.of(newActivityLocation(null)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(locationPercentageViolation()));
    }

    @Test
    public void testValidPercentageOneLocation() {
        AmpActivity activity = new AmpActivity();
        activity.setLocations(ImmutableSet.of(newActivityLocation(100f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidPercentageManyLocations() {
        AmpActivity activity = new AmpActivity();
        activity.setLocations(ImmutableSet.of(
                newActivityLocation(33f),
                newActivityLocation(33f),
                newActivityLocation(34f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidPercentageWithOneLocation() {
        AmpActivity activity = new AmpActivity();
        activity.setLocations(ImmutableSet.of(newActivityLocation(33f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(locationPercentageViolation()));
    }

    @Test
    public void testInvalidPercentageWithTwoLocations() {
        AmpActivity activity = new AmpActivity();
        activity.setLocations(ImmutableSet.of(newActivityLocation(10f), newActivityLocation(20f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(locationPercentageViolation()));
    }

    /**
     * Matcher for an activity location percentage constraint violation.
     */
    private Matcher<ConstraintViolation> locationPercentageViolation() {
        return violationWithPath(LocationTotalPercentage.class,
                ImmutableList.of(iterableNode("locations"), propertyNode("locationPercentage")));
    }

    private AmpActivityLocation newActivityLocation(Float percentage) {
        AmpActivityLocation activityLocation = new AmpActivityLocation();
        activityLocation.setLocationPercentage(percentage);
        return activityLocation;
    }
}
