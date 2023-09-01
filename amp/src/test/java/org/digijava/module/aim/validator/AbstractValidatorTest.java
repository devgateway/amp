package org.digijava.module.aim.validator;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.validator.groups.API;
import org.junit.Before;

import javax.validation.*;
import javax.validation.groups.Default;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

/**
 * @author Octavian Ciubotaru
 */
public abstract class AbstractValidatorTest<T extends ConstraintValidator> {

    private Class<T> clazz;
    private Validator validator;

    public AbstractValidatorTest() {
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

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
            Long classesCount = classes.stream()
                    .filter(c -> clazz.isAssignableFrom((Class<?>) c))
                    .count();
            return classesCount == 0;
        });
        return violations;
    }
}
