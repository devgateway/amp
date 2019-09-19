package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.digijava.kernel.validators.ValidatorUtil.getDefaultTranslationContext;
import static org.digijava.kernel.validators.activity.ValidatorMatchers.containsInAnyOrder;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class ImplementationLevelValidatorTest {

    private static APIField activityField;
    private static HardcodedCategoryValues categoryValues;
    private static HardcodedLocations locations;

    @BeforeClass
    public static void setUp() {
        activityField = ValidatorUtil.getMetaData();
        categoryValues = new HardcodedCategoryValues();
        locations = new HardcodedLocations(categoryValues);
    }

    //--- impl loc tests

    @Test
    public void testValidImplementationLocation() {
        AmpCategoryValue regionalImplementationLevel = categoryValues.getImplementationLevels().getRegional();
        AmpCategoryValue regionImplementationLocation = categoryValues.getImplementationLocations().getRegion();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(regionalImplementationLevel, regionImplementationLocation)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidImplementationLocation() {
        AmpCategoryValue centralImplLevel = categoryValues.getImplementationLevels().getCentral();
        AmpCategoryValue zoneImplLocation = categoryValues.getImplementationLocations().getZone();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(centralImplLevel, zoneImplLocation)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(implLocViolation(ValidationErrors.DOESNT_MATCH_IMPLEMENTATION_LEVEL)));
    }

    @Test
    public void testMissingImplementationLocation() {
        AmpCategoryValue centralImplLevel = categoryValues.getImplementationLevels().getCentral();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(centralImplLevel)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testMissingImplementationLevel() {
        AmpCategoryValue zoneImplLocation = categoryValues.getImplementationLocations().getZone();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(zoneImplLocation)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(implLocViolation(ValidationErrors.IMPLEMENTATION_LEVEL_NOT_SPECIFIED)));
    }

    @Test
    public void testMissingImplementationLevelAndLocation() {
        AmpActivityVersion activity = new ActivityBuilder().getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    //--- loc tests

    @Test
    public void testLocationAndImplLevelMismatch() {
        AmpCategoryValue regionalImplementationLevel = categoryValues.getImplementationLevels().getRegional();
        AmpCategoryValue regionImplementationLocation = categoryValues.getImplementationLocations().getRegion();

        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(locations.getAmpLocation("Haiti"), 100f)
                .withCategories(regionalImplementationLevel, regionImplementationLocation)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(locViolation(96L)));
    }

    @Test
    public void testValidLocation() {
        AmpCategoryValue regionalImplementationLevel = categoryValues.getImplementationLevels().getRegional();
        AmpCategoryValue regionImplementationLocation = categoryValues.getImplementationLocations().getRegion();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(regionalImplementationLevel, regionImplementationLocation)
                .addLocation(locations.getAmpLocation("Haiti", "Artibonite"), 100f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidLocationWithoutImplLocation() {
        AmpCategoryValue regionalImplementationLevel = categoryValues.getImplementationLevels().getRegional();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(regionalImplementationLevel)
                .addLocation(locations.getAmpLocation("Haiti", "Artibonite"), 100f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidLocationIsUnrelatedToImplLocation() {
        AmpCategoryValue regionalImplementationLevel = categoryValues.getImplementationLevels().getRegional();
        AmpCategoryValue regionImplementationLocation = categoryValues.getImplementationLocations().getRegion();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(regionalImplementationLevel, regionImplementationLocation)
                .addLocation(locations.getAmpLocation("Haiti", "Artibonite", "Dessalines"), 100f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullLocation() {
        AmpActivityLocation nullLocation = new AmpActivityLocation();
        nullLocation.setLocationPercentage(100f);

        AmpCategoryValue regionalImplementationLevel = categoryValues.getImplementationLevels().getRegional();
        AmpCategoryValue regionImplementationLocation = categoryValues.getImplementationLocations().getRegion();

        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(nullLocation)
                .withCategories(regionalImplementationLevel, regionImplementationLocation)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testLocationNotAllowedWithoutImplLevel() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(locations.getAmpLocation("Haiti"), 100f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(locsViolation()));
    }

    @Test
    public void testTwoInvalidLocations() {
        AmpCategoryValue centralImplementationLevel = categoryValues.getImplementationLevels().getCentral();
        AmpCategoryValue countryImplementationLocation = categoryValues.getImplementationLocations().getCountry();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(centralImplementationLevel, countryImplementationLocation)
                .addLocation(locations.getAmpLocation("Haiti"), 33f)
                .addLocation(locations.getAmpLocation("Haiti", "Artibonite"), 33f)
                .addLocation(locations.getAmpLocation("Haiti", "Grande Anse"), 34f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, containsInAnyOrder(locViolation(1901L), locViolation(1903L)));
    }

    @Test
    public void testInvalidLocationAndImplLoc() {
        AmpCategoryValue centralImplementationLevel = categoryValues.getImplementationLevels().getCentral();
        AmpCategoryValue countryImplementationLocation = categoryValues.getImplementationLocations().getRegion();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(centralImplementationLevel, countryImplementationLocation)
                .addLocation(locations.getAmpLocation("Haiti", "Artibonite"), 100f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, containsInAnyOrder(
                implLocViolation(ValidationErrors.DOESNT_MATCH_IMPLEMENTATION_LEVEL),
                locViolation(1901L)));
    }

    private Set<ConstraintViolation> getConstraintViolations(AmpActivityVersion activity) {
        Set<ConstraintViolation> violations = ActivityValidatorUtil.validate(activityField, activity);
        return filter(violations, ImplementationLevelValidator.class);
    }

    private Matcher<ConstraintViolation> implLocViolation(ApiErrorMessage message) {
        return violation("implementation_location", message);
    }

    private Matcher<ConstraintViolation> locsViolation() {
        return violation("locations", ValidationErrors.IMPLEMENTATION_LEVEL_NOT_SPECIFIED);
    }

    private Matcher<ConstraintViolation> locViolation(Long locId) {
        return allOf(
                violation("locations~location", ValidationErrors.DOESNT_MATCH_IMPLEMENTATION_LEVEL),
                hasProperty("attributes", hasEntry(ImplementationLevelValidator.ATTR_LOC_ID, locId)));
    }

    private Matcher<ConstraintViolation> violation(String path, ApiErrorMessage message) {
        return ValidatorMatchers.violationFor(ImplementationLevelValidator.class, path, anything(), message);
    }
}
