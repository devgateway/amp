package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.*;

import java.util.Set;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class TreeCollectionValidatorTest {

    private static APIField activityField;
    private static HardcodedLocations locations;

    @BeforeClass
    public static void setUp() {
        HardcodedCategoryValues categoryValues = new HardcodedCategoryValues();
        locations = new HardcodedLocations(categoryValues);

        activityField = ValidatorUtil.getMetaData();
    }

    @Test
    public void testEmptyCollection() {
        AmpActivityVersion activity = new ActivityBuilder().getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testAncestorIsPresent() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(locations.getAmpLocation("Haiti"), 50f)
                .addLocation(locations.getAmpLocation("Haiti", "Artibonite"), 50f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(violation()));
    }

    @Test
    public void testAncestorIsButInDifferentOrderPresent() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(locations.getAmpLocation("Haiti", "Artibonite"), 50f)
                .addLocation(locations.getAmpLocation("Haiti"), 50f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(violation()));
    }
    
    @Test
    public void testObjectWithNullEntityInCollection() {
        AmpLocation loc = new AmpLocation();
        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(loc, 50f)
                .addLocation(locations.getAmpLocation("Haiti"), 50f)
                .getActivity();
        
        Set<ConstraintViolation> violations = getConstraintViolations(activity);
    
        assertThat(violations, emptyIterable());
    }

    @Test
    public void testRepeatingItemsAreAllowed() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(locations.getAmpLocation("Haiti", "Artibonite"), 50f)
                .addLocation(locations.getAmpLocation("Haiti", "Artibonite"), 50f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    private Matcher<ConstraintViolation> violation() {
        return ValidatorMatchers.violationFor(TreeCollectionValidator.class, "locations", anything(),
                ValidationErrors.FIELD_PARENT_CHILDREN_NOT_ALLOWED);
    }

    private Set<ConstraintViolation> getConstraintViolations(AmpActivityVersion activity) {
        Set<ConstraintViolation> violations = ActivityValidatorUtil.validate(activityField, activity);
        return filter(violations, TreeCollectionValidator.class);
    }
}
