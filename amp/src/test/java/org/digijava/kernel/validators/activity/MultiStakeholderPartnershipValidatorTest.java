package org.digijava.kernel.validators.activity;

import com.google.common.collect.ImmutableList;
import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.validator.groups.Submit;
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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Viorel Chihai
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({FeaturesUtil.class})
public class MultiStakeholderPartnershipValidatorTest {

    private static APIField activityField;

    @Before
    public void setUp() {
        TransactionUtil.setUpWorkspaceEmptyPrefixes();
        PowerMockito.mockStatic(FeaturesUtil.class);
        activityField = ValidatorUtil.getMetaData();
    }

    @Test
    public void testEmptyActivity() {
        AmpActivityVersion activity = new ActivityBuilder().getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testMultiStakeholderPartnershipFalse() {
        AmpActivityVersion activity = new ActivityBuilder()
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testMultiStakeholderPartnershipTrueNotRequiredOnDraftSave() {
        AmpActivityVersion activity = new ActivityBuilder()
                .withMultiStakeholderPartnership(true)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testMultiStakeholderPartnershipTrueMissingField() {
        AmpActivityVersion activity = new ActivityBuilder()
                .withMultiStakeholderPartnership(true)
                .getActivity();

        mockValidation();
        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, containsInAnyOrder(ImmutableList.of(
                violation("multi_stakeholder_partners"))));
    }

    private void mockValidation() {
        when(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM)).thenReturn(null);
    }

    @Test
    public void testMultiStakeholderPartnershipTruePartnersMissing() {
        AmpActivityVersion activity = new ActivityBuilder()
                .withMultiStakeholderPartnership(true)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, containsInAnyOrder(ImmutableList.of(
                violation("multi_stakeholder_partners"))));
    }

    @Test
    public void testMultiStakeholderPartnershipTruePartnersSet() {
        AmpActivityVersion activity = new ActivityBuilder()
                .withMultiStakeholderPartnership(true)
                .getActivity();

        activity.setMultiStakeholderPartners("Partners");

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        assertThat(violations, emptyIterable());
    }

    private Matcher<ConstraintViolation> violation(String path) {
        return ValidatorMatchers.violationFor(MultiStakeholderPartnershipValidator.class, path, anything(),
                ValidationErrors.FIELD_REQUIRED);
    }

    private Set<ConstraintViolation> getConstraintViolations(Object object, Class<?>... groups) {
        return getConstraintViolations(activityField, object, groups);
    }

    private Set<ConstraintViolation> getConstraintViolations(APIField type, Object object, Class<?>... groups) {
        Set<ConstraintViolation> violations = ActivityValidatorUtil.validate(type, object, groups);
        return filter(violations, MultiStakeholderPartnershipValidator.class);
    }
}