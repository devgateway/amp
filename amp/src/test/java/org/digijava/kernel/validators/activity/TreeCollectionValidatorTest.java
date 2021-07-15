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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Set;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Octavian Ciubotaru
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FeaturesUtil.class})
public class TreeCollectionValidatorTest {

    private static APIField activityField;
    private static InMemoryLocationManager locationManager;

    @Before
    public void setUp() {
        InMemoryCategoryValuesManager categoryValues = InMemoryCategoryValuesManager.getInstance();
        PowerMockito.mockStatic(FeaturesUtil.class);
        locationManager = InMemoryLocationManager.getInstance();

        TransactionUtil.setUpWorkspaceEmptyPrefixes();
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
        when(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM)).thenReturn(null);
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
