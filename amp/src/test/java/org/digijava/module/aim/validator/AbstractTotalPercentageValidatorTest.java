package org.digijava.module.aim.validator;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.validator.groups.API;
import org.digijava.module.aim.validator.percentage.AbstractTotalPercentageValidator;
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
        Set<ConstraintViolation<AmpActivity>> violations = getValidator().validate(activity, API.class, Default.class);
        violations.removeIf(v -> {
            List<?> classes = v.getConstraintDescriptor().getConstraintValidatorClasses();
            Long totPercClassesCount = classes.stream()
                    .filter(c -> AbstractTotalPercentageValidator.class.isAssignableFrom((Class<?>) c))
                    .count();
            return totPercClassesCount == 0;
        });
        return violations;
    }
}
