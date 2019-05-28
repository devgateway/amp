package org.digijava.module.aim.validator;

import static org.digijava.module.aim.validator.ConstraintMatchers.inIterableNode;
import static org.digijava.module.aim.validator.ConstraintMatchers.inIterableNodeAtKey;
import static org.digijava.module.aim.validator.ConstraintMatchers.nodeAtKey;
import static org.digijava.module.aim.validator.ConstraintMatchers.propertyNode;
import static org.digijava.module.aim.validator.ConstraintMatchers.violationWithPath;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.validator.percentage.SectorsTotalPercentage;
import org.digijava.module.aim.validator.percentage.SectorsTotalPercentageValidator;
import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class SectorsTotalPercentageValidatorTest
extends AbstractActivityValidatorTest<SectorsTotalPercentageValidator> {

    private AmpClassificationConfiguration primarySectors =
            newConfig(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME);

    private AmpClassificationConfiguration secondarySectors =
            newConfig(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME);

    @Test
    public void testNotAppliedInHibernate() {
        AmpActivity activity = new AmpActivity();
        activity.setSectors(ImmutableSet.of(newActivitySector(primarySectors, 33f)));

        Set<ConstraintViolation<AmpActivity>> violations = getValidator().validate(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullSectors() {
        AmpActivity activity = new AmpActivity();
        activity.setSectors(null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testEmptySectors() {
        AmpActivity activity = new AmpActivity();
        activity.setSectors(ImmutableSet.of());

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullClassification() {
        AmpActivity activity = new AmpActivity();
        activity.setSectors(ImmutableSet.of(
                newActivitySector(null, 50f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(sectorPercentageViolation(null)));
    }

    @Test
    public void testNullPercentage() {
        AmpActivity activity = new AmpActivity();
        activity.setSectors(ImmutableSet.of(
                newActivitySector(primarySectors, null)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(sectorPercentageViolation(primarySectors)));
    }

    @Test
    public void testValidPercentageTwoSectorsOneClassification() {
        AmpActivity activity = new AmpActivity();
        activity.setSectors(ImmutableSet.of(
                newActivitySector(primarySectors, 33f),
                newActivitySector(primarySectors, 67f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidPercentageTwoClassifications() {
        AmpActivity activity = new AmpActivity();
        activity.setSectors(ImmutableSet.of(
                newActivitySector(primarySectors, 100f),
                newActivitySector(secondarySectors, 100f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidPercentageTwoClassifications() {
        AmpActivity activity = new AmpActivity();
        activity.setSectors(ImmutableSet.of(
                newActivitySector(primarySectors, 50f),
                newActivitySector(secondarySectors, 99f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations,
                containsInAnyOrder(
                        sectorPercentageViolation(primarySectors),
                        sectorPercentageViolation(secondarySectors)));
    }

    @Test
    public void testMixedPercentage() {
        AmpActivity activity = new AmpActivity();
        activity.setSectors(ImmutableSet.of(
                newActivitySector(primarySectors, 100f),
                newActivitySector(secondarySectors, 99f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(sectorPercentageViolation(secondarySectors)));
    }

    /**
     * Matcher for an activity sector percentage constraint violation.
     */
    private Matcher<ConstraintViolation> sectorPercentageViolation(AmpClassificationConfiguration config) {
        return violationWithPath(SectorsTotalPercentage.class,
                ImmutableList.of(propertyNode("sectors"), inIterableNodeAtKey("sectorPercentage", config)));
    }

    private AmpActivitySector newActivitySector(AmpClassificationConfiguration config, Float percentage) {
        AmpActivitySector activitySector = new AmpActivitySector();
        activitySector.setClassificationConfig(config);
        activitySector.setSectorPercentage(percentage);
        return activitySector;
    }

    private AmpClassificationConfiguration newConfig(String name) {
        AmpClassificationConfiguration configuration = new AmpClassificationConfiguration();
        configuration.setName(name);
        return configuration;
    }
}