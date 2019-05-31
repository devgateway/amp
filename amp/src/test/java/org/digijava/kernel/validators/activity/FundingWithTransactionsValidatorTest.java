package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.validators.activity.ValidatorMatchers.containsInAnyOrder;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.*;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.activity.builder.FundingBuilder;
import org.dgfoundation.amp.activity.builder.TransactionBuilder;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.Validator;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class FundingWithTransactionsValidatorTest {

    private static APIField activityField;
    private static HardcodedCategoryValues categoryValues;

    @BeforeClass
    public static void setUp() {
        activityField = ValidatorUtil.getMetaData();
        categoryValues = new HardcodedCategoryValues();
    }

    @Test
    public void testEmptyTransactions() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder().getFunding())
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testBothFieldsRequired() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder()
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .getTransaction())
                        .getFunding())
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, containsInAnyOrder(
                violation("fundings~type_of_assistance"),
                violation("fundings~financing_instrument")));
    }

    @Test
    public void testOneFieldsPresent() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder()
                        .withTypeOfAssistance(categoryValues.getTypeOfAssistanceValues().getGrant())
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.COMMITMENT)
                                .getTransaction())
                        .getFunding())
                .getActivity();

        Set<ConstraintViolation> violations = getConstraintViolations(activity);

        assertThat(violations, contains(violation("fundings~financing_instrument")));
    }

    @Test
    public void testDisabledExpenditures() {
        AmpActivityVersion activity = new ActivityBuilder()
                .addFunding(new FundingBuilder()
                        .addTransaction(new TransactionBuilder()
                                .withTransactionType(Constants.EXPENDITURE)
                                .getTransaction())
                        .getFunding())
                .getActivity();

        Validator validator = new Validator();
        Set<String> disabledFm = ImmutableSet.of(
                "/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table");
        Set<ConstraintViolation> violations = validator.validate(ValidatorUtil.getMetaData(disabledFm), activity);

        assertThat(violations, emptyIterable());
    }

    private Set<ConstraintViolation> getConstraintViolations(AmpActivityVersion activity) {
        Validator validator = new Validator();
        return validator.validate(activityField, activity);
    }

    private Matcher<ConstraintViolation> violation(String path) {
        return ValidatorMatchers.violationFor(
                FundingWithTransactionsValidator.class, path, anything(), ActivityErrors.FIELD_REQUIRED);
    }
}