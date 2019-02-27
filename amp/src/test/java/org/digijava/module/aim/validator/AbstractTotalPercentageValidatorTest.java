package org.digijava.module.aim.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.validator.groups.API;
import org.junit.Before;

/**
 * @author Octavian Ciubotaru
 */
public abstract class AbstractTotalPercentageValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Validator getValidator() {
        return validator;
    }

    public Set<ConstraintViolation<AmpActivity>> validateForAPI(AmpActivity activity) {
        return getValidator().validate(activity, API.class, Default.class);
    }
}
