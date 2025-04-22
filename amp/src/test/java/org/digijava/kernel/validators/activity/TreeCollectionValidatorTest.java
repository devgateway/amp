package org.digijava.kernel.validators.activity;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.persistence.InMemoryCategoryValuesManager;
import org.digijava.kernel.persistence.InMemoryLocationManager;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Set;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class TreeCollectionValidatorTest {

    private static APIField activityField;
    private static InMemoryLocationManager locationManager;
    private MockedStatic<FeaturesUtil> featuresUtilMock;

    @BeforeEach
    public void setUp() {
        InMemoryCategoryValuesManager categoryValues = InMemoryCategoryValuesManager.getInstance();
        locationManager = InMemoryLocationManager.getInstance();
        TransactionUtil.setUpWorkspaceEmptyPrefixes();
        activityField = ValidatorUtil.getMetaData();

        // Setup static mocking for FeaturesUtil
        featuresUtilMock = mockStatic(FeaturesUtil.class);
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
                .addLocation(locationManager.getAmpLocation("Haiti"), 50f)
                .addLocation(locationManager.getAmpLocation("Haiti", "Artibonite"), 50f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(violation()));
    }

    @Test
    public void testAncestorIsButInDifferentOrderPresent() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(locationManager.getAmpLocation("Haiti", "Artibonite"), 50f)
                .addLocation(locationManager.getAmpLocation("Haiti"), 50f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(violation()));
    }

    @Test
    public void testObjectWithNullEntityInCollection() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(locationManager.getAmpLocation("Haiti"), 50f)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testRepeatingItemsAreAllowed() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addLocation(locationManager.getAmpLocation("Haiti", "Artibonite"), 50f)
                .addLocation(locationManager.getAmpLocation("Haiti", "Artibonite"), 50f)
                .getActivity();

        mockValidation();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    private void mockValidation() {
        featuresUtilMock.when(() ->
                FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM)
        ).thenReturn(null);
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
