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
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Set;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mockStatic;

/**
 * @author Viorel Chihai
 */
public class MultiStakeholderPartnershipValidatorTest {

    private static APIField activityField;

    @BeforeEach
    public void setUp() {
        TransactionUtil.setUpWorkspaceEmptyPrefixes();
        activityField = ValidatorUtil.getMetaData();
    }

    @Test
    public void testEmptyActivity() {
        AmpActivityVersion activity = new ActivityBuilder().getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        MatcherAssert.assertThat(violations, emptyIterable());
    }

    @Test
    public void testMultiStakeholderPartnershipFalse() {
        AmpActivityVersion activity = new ActivityBuilder().getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        MatcherAssert.assertThat(violations, emptyIterable());
    }

    @Test
    public void testMultiStakeholderPartnershipTrueNotRequiredOnDraftSave() {
        AmpActivityVersion activity = new ActivityBuilder()
                .withMultiStakeholderPartnership(true)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        MatcherAssert.assertThat(violations, emptyIterable());
    }

    @Test
    public void testMultiStakeholderPartnershipTrueMissingField() {
        try (MockedStatic<FeaturesUtil> mocked = mockStatic(FeaturesUtil.class)) {
            mocked.when(() -> FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM))
                    .thenReturn(null);

            AmpActivityVersion activity = new ActivityBuilder()
                    .withMultiStakeholderPartnership(true)
                    .getActivity();

            Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

            MatcherAssert.assertThat(violations, containsInAnyOrder(ImmutableList.of(
                    violation("multi_stakeholder_partners"))));
        }
    }

    @Test
    public void testMultiStakeholderPartnershipTruePartnersMissing() {
        AmpActivityVersion activity = new ActivityBuilder()
                .withMultiStakeholderPartnership(true)
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        MatcherAssert.assertThat(violations, containsInAnyOrder(ImmutableList.of(
                violation("multi_stakeholder_partners"))));
    }

    @Test
    public void testMultiStakeholderPartnershipTruePartnersSet() {
        AmpActivityVersion activity = new ActivityBuilder()
                .withMultiStakeholderPartnership(true)
                .getActivity();

        activity.setMultiStakeholderPartners("Partners");

        Set<ConstraintViolation> violations = getConstraintViolations(activity, Submit.class);

        MatcherAssert.assertThat(violations, emptyIterable());
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
