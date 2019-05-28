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
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.validator.percentage.ProgramTotalPercentage;
import org.digijava.module.aim.validator.percentage.ProgramTotalPercentageValidator;
import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class ProgramTotalPercentageValidatorTest
extends AbstractActivityValidatorTest<ProgramTotalPercentageValidator> {

    private AmpActivityProgramSettings primaryProgram = newActProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
    private AmpActivityProgramSettings secondaryProgram = newActProgramSettings(ProgramUtil.SECONDARY_PROGRAM);

    @Test
    public void testNotAppliedInHibernate() {
        AmpActivity activity = new AmpActivity();
        activity.setActPrograms(ImmutableSet.of(newActProgram(primaryProgram, 33f)));

        Set<ConstraintViolation<AmpActivity>> violations = getValidator().validate(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullPrograms() {
        AmpActivity activity = new AmpActivity();
        activity.setActPrograms(null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testEmptyPrograms() {
        AmpActivity activity = new AmpActivity();
        activity.setActPrograms(ImmutableSet.of());

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullPercentage() {
        AmpActivity activity = new AmpActivity();
        activity.setActPrograms(ImmutableSet.of(newActProgram(primaryProgram, null)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(programPercentageViolation(primaryProgram)));
    }

    @Test
    public void testNullProgramSetting() {
        AmpActivity activity = new AmpActivity();
        activity.setActPrograms(ImmutableSet.of(newActProgram(null, 13f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(programPercentageViolation(null)));
    }

    @Test
    public void testValidPercentageOneProgram() {
        AmpActivity activity = new AmpActivity();
        activity.setActPrograms(ImmutableSet.of(newActProgram(primaryProgram, 100f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidPercentageOneProgram() {
        AmpActivity activity = new AmpActivity();
        activity.setActPrograms(ImmutableSet.of(newActProgram(primaryProgram, 13f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(programPercentageViolation(primaryProgram)));
    }

    @Test
    public void testValidPercentageTwoProgramSettings() {
        AmpActivity activity = new AmpActivity();
        activity.setActPrograms(ImmutableSet.of(
                newActProgram(primaryProgram, 4f),
                newActProgram(primaryProgram, 6f),
                newActProgram(primaryProgram, 90f),
                newActProgram(secondaryProgram, 30f),
                newActProgram(secondaryProgram, 70f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testInvalidPercentageTwoProgramSettings() {
        AmpActivity activity = new AmpActivity();
        activity.setActPrograms(ImmutableSet.of(
                newActProgram(primaryProgram, 4f),
                newActProgram(primaryProgram, 90f),
                newActProgram(secondaryProgram, 70f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, containsInAnyOrder(
                programPercentageViolation(primaryProgram),
                programPercentageViolation(secondaryProgram)));
    }

    @Test
    public void testMixedPercentages() {
        AmpActivity activity = new AmpActivity();
        activity.setActPrograms(ImmutableSet.of(
                newActProgram(primaryProgram, 100f),
                newActProgram(secondaryProgram, 70f)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, contains(programPercentageViolation(secondaryProgram)));
    }

    /**
     * Matcher for an activity program percentage constraint violation.
     */
    private Matcher<ConstraintViolation> programPercentageViolation(AmpActivityProgramSettings primaryProgram) {
        return violationWithPath(ProgramTotalPercentage.class,
                ImmutableList.of(propertyNode("actPrograms"), inIterableNodeAtKey("programPercentage", primaryProgram)));
    }

    private AmpActivityProgramSettings newActProgramSettings(String name) {
        AmpActivityProgramSettings primaryProgram = new AmpActivityProgramSettings();
        primaryProgram.setName(name);
        return primaryProgram;
    }

    private AmpActivityProgram newActProgram(AmpActivityProgramSettings programSettings, Float percentage) {
        AmpActivityProgram actProgram = new AmpActivityProgram();
        actProgram.setProgramSetting(programSettings);
        actProgram.setProgramPercentage(percentage);
        return actProgram;
    }
}