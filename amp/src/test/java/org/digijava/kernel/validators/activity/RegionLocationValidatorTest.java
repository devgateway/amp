package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.persistence.InMemoryCategoryValuesManager;
import org.digijava.kernel.persistence.InMemoryLocationManager;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;
import org.hamcrest.Matcher;
import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class RegionLocationValidatorTest {

    private static APIField activityField;
    private static InMemoryCategoryValuesManager categoryValues;
    private static InMemoryLocationManager locations;

    @BeforeClass
    public static void setUp() {
        TransactionUtil.setUpWorkspaceEmptyPrefixes();
        activityField = ValidatorUtil.getMetaData();
        categoryValues =InMemoryCategoryValuesManager.getInstance();
        locations = InMemoryLocationManager.getInstance();
    }

    @Test
    public void testNoLocations() {
        AmpActivityVersion activity = new AmpActivityVersion();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullLocation() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(null, 100f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullRegionLocation() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addRegionalFunding(Constants.COMMITMENT, categoryValues.getAdjustmentTypes().getActual(),
                        new LocalDate(2011, 11, 12).toDate(),
                        100d, null, null)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidLocation() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(locations.getAmpLocation("Haiti"), 100f)
                .addRegionalFunding(Constants.COMMITMENT, categoryValues.getAdjustmentTypes().getActual(),
                        new LocalDate(2011, 11, 12).toDate(),
                        100d, null, locations.getAmpLocation("Haiti"))
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testMissingLocationInCommitments() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addRegionalFunding(Constants.COMMITMENT, categoryValues.getAdjustmentTypes().getActual(),
                        new LocalDate(2011, 11, 12).toDate(),
                        100d, null, locations.getAmpLocation("Haiti"))
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(locViolation("regional_commitments~region_location", contains(96L))));
    }

    @Test
    public void testMissingLocationInDisbursements() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addRegionalFunding(Constants.DISBURSEMENT, categoryValues.getAdjustmentTypes().getActual(),
                        new LocalDate(2011, 11, 12).toDate(),
                        100d, null, locations.getAmpLocation("Haiti", "Artibonite"))
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(locViolation("regional_disbursements~region_location", contains(1901L))));
    }

    @Test
    public void testMissingLocationsInExpenditures() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addRegionalFunding(Constants.EXPENDITURE, categoryValues.getAdjustmentTypes().getActual(),
                        new LocalDate(2011, 11, 12).toDate(),
                        100d, null, locations.getAmpLocation("Haiti", "Artibonite"))
                .addRegionalFunding(Constants.EXPENDITURE, categoryValues.getAdjustmentTypes().getActual(),
                        new LocalDate(2011, 11, 12).toDate(),
                        100d, null, locations.getAmpLocation("Haiti", "Grande Anse"))
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(
                locViolation("regional_expenditures~region_location", containsInAnyOrder(1901L, 1903L))));
    }

    private Matcher<ConstraintViolation> locViolation(String path, Matcher<Iterable<? extends Long>> locIds) {
        return allOf(
                violation(path),
                hasProperty("attributes",
                        hasEntry(is(RegionLocationValidator.ATTR_LOC_IDS), locIds)));
    }

    private Matcher<ConstraintViolation> violation(String path) {
        return ValidatorMatchers.violationFor(RegionLocationValidator.class, path, anything(),
                ValidationErrors.LOCATION_NOT_DECLARED);
    }

    private Set<ConstraintViolation> getConstraintViolations(AmpActivityVersion activity) {
        Set<ConstraintViolation> violations = ActivityValidatorUtil.validate(activityField, activity);
        return filter(violations, RegionLocationValidator.class);
    }
}