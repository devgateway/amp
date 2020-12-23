package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.validators.ValidatorUtil.getDefaultTranslationContext;

import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.TranslationContext;
import org.digijava.kernel.validation.Validator;
import org.digijava.module.aim.dbentity.AmpActivityGroup;

/**
 * @author Octavian Ciubotaru
 */
public final class ActivityValidatorUtil {

    public static class DummyActivityTitleDAO implements UniqueActivityTitleValidator.ActivityTitleDAO {

        private boolean unique;

        public DummyActivityTitleDAO(boolean unique) {
            this.unique = unique;
        }

        @Override
        public boolean isUnique(Set<String> titles, AmpActivityGroup activityGroup) {
            return unique;
        }
    }

    private ActivityValidatorUtil() {
    }

    public static Set<ConstraintViolation> validate(APIField type, Object value, Class<?>... groups) {
        return validate(type, value, getDefaultTranslationContext(), true, groups);
    }

    public static Set<ConstraintViolation> validate(APIField type, Object value, TranslationContext translationContext,
            boolean activityTitleUnique, Class<?>... groups) {

        Validator validator = new Validator();
        return UniqueActivityTitleValidator.withDao(new DummyActivityTitleDAO(activityTitleUnique),
                () -> validator.validate(type, value, translationContext, groups));
    }
}
