package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.*;

import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class AgreementCodeValidatorTest {

    private static APIField agreementField;

    @BeforeClass
    public static void setUp() {
        agreementField = ValidatorUtil.getMetaData(AmpAgreement.class);
    }

    @Test
    public void testNull() {
        AmpAgreement agreement = new AmpAgreement();
        agreement.setCode(null);

        Set<ConstraintViolation> violations = getConstraintViolations(agreement);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testEmpty() {
        AmpAgreement agreement = new AmpAgreement();
        agreement.setCode("");

        Set<ConstraintViolation> violations = getConstraintViolations(agreement);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testBlank() {
        AmpAgreement agreement = new AmpAgreement();
        agreement.setCode(" ");

        Set<ConstraintViolation> violations = getConstraintViolations(agreement);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNewCode() {
        AmpAgreement agreement = new AmpAgreement();
        agreement.setCode("2");

        Set<ConstraintViolation> violations = getConstraintViolations(agreement);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testExistingCode() {
        AmpAgreement agreement = new AmpAgreement();
        agreement.setCode("1");

        Set<ConstraintViolation> violations = getConstraintViolations(agreement);

        assertThat(violations, contains(violation()));
    }

    private Matcher<ConstraintViolation> violation() {
        return ValidatorMatchers.violationFor(AgreementCodeValidator.class, "code", anything(),
                ValidationErrors.AGREEMENT_CODE_UNIQUE);
    }

    private Set<ConstraintViolation> getConstraintViolations(AmpAgreement agreement) {
        Set<ConstraintViolation> violations = AgreementCodeValidator.withCounter(
                (code) -> code.equals("1") ? 1 : 0,
                () -> ActivityValidatorUtil.validate(agreementField, agreement));
        return filter(violations, AgreementCodeValidator.class);
    }
}
