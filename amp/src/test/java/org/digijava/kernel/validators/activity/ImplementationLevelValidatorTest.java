package org.digijava.kernel.validators.activity;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.persistence.InMemoryCategoryValuesManager;
import org.digijava.kernel.persistence.InMemoryLocationManager;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Set;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.digijava.kernel.validators.activity.ValidatorMatchers.containsInAnyOrder;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Octavian Ciubotaru
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FeaturesUtil.class})
public class ImplementationLevelValidatorTest {

    private static APIField activityField;
    private static InMemoryCategoryValuesManager categoryValueManager;
    private static InMemoryLocationManager locationManager;

    @Before
    public void setUp() {
        TransactionUtil.setUpWorkspaceEmptyPrefixes();
        PowerMockito.mockStatic(FeaturesUtil.class);
        activityField = ValidatorUtil.getMetaData();
        categoryValueManager = InMemoryCategoryValuesManager.getInstance();
        locationManager = InMemoryLocationManager.getInstance();
    }

    //--- impl loc tests

    @Test
    public void testValidImplementationLocation() {
        AmpCategoryValue regionalImplementationLevel = categoryValueManager.getImplementationLevels().getRegional();
        AmpCategoryValue regionImplementationLocation = categoryValueManager.getImplementationLocations().getRegion();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(regionalImplementationLevel, regionImplementationLocation)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidImplementationLocation() {
        AmpCategoryValue centralImplLevel = categoryValueManager.getImplementationLevels().getCentral();
        AmpCategoryValue zoneImplLocation = categoryValueManager.getImplementationLocations().getZone();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(centralImplLevel, zoneImplLocation)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(implLocViolation(ValidationErrors.DOESNT_MATCH_IMPLEMENTATION_LEVEL)));
    }

    @Test
    public void testMissingImplementationLocation() {
        AmpCategoryValue centralImplLevel = categoryValueManager.getImplementationLevels().getCentral();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(centralImplLevel)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testMissingImplementationLevel() {
        AmpCategoryValue zoneImplLocation = categoryValueManager.getImplementationLocations().getZone();

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
        AmpCategoryValue regionalImplementationLevel = categoryValueManager.getImplementationLevels().getRegional();
        AmpCategoryValue regionImplementationLocation = categoryValueManager.getImplementationLocations().getRegion();

        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(locationManager.getAmpLocation("Haiti"), 100f)
                .withCategories(regionalImplementationLevel, regionImplementationLocation)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(locViolation(96L)));
    }

    @Test
    public void testValidLocation() {
        AmpCategoryValue regionalImplementationLevel = categoryValueManager.getImplementationLevels().getRegional();
        AmpCategoryValue regionImplementationLocation = categoryValueManager.getImplementationLocations().getRegion();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(regionalImplementationLevel, regionImplementationLocation)
                .addLocation(locationManager.getAmpLocation("Haiti", "Artibonite"), 100f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidLocationWithoutImplLocation() {
        AmpCategoryValue regionalImplementationLevel = categoryValueManager.getImplementationLevels().getRegional();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(regionalImplementationLevel)
                .addLocation(locationManager.getAmpLocation("Haiti", "Artibonite"), 100f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidLocationIsUnrelatedToImplLocation() {
        AmpCategoryValue regionalImplementationLevel = categoryValueManager.getImplementationLevels().getRegional();
        AmpCategoryValue regionImplementationLocation = categoryValueManager.getImplementationLocations().getRegion();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(regionalImplementationLevel, regionImplementationLocation)
                .addLocation(locationManager.getAmpLocation("Haiti", "Artibonite", "Dessalines"), 100f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testLocationNotAllowedWithoutImplLevel() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(locationManager.getAmpLocation("Haiti"), 100f)
                .getActivity();
        mockValidation();
        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(locsViolation()));
    }

    private void mockValidation() {
        when(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM)).thenReturn(null);
    }

    @Test
    public void testTwoInvalidLocations() {
        AmpCategoryValue centralImplementationLevel = categoryValueManager.getImplementationLevels().getCentral();
        AmpCategoryValue countryImplementationLocation = categoryValueManager.getImplementationLocations().getCountry();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(centralImplementationLevel, countryImplementationLocation)
                .addLocation(locationManager.getAmpLocation("Haiti"), 33f)
                .addLocation(locationManager.getAmpLocation("Haiti", "Artibonite"), 33f)
                .addLocation(locationManager.getAmpLocation("Haiti", "Grande Anse"), 34f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, containsInAnyOrder(locViolation(1901L), locViolation(1903L)));
    }

    @Test
    public void testInvalidLocationAndImplLoc() {
        AmpCategoryValue centralImplementationLevel = categoryValueManager.getImplementationLevels().getCentral();
        AmpCategoryValue countryImplementationLocation = categoryValueManager.getImplementationLocations().getRegion();

        AmpActivityVersion activity = new ActivityBuilder()
                .withCategories(centralImplementationLevel, countryImplementationLocation)
                .addLocation(locationManager.getAmpLocation("Haiti", "Artibonite"), 100f)
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
