package org.digijava.kernel.validators.activity;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;
import org.digijava.kernel.validation.TranslatedValueContext;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.categorymanager.util.IdWithValueShim;

/**
 * Validates that project title is unique across AMP.
 *
 * @author Octavian Ciubotaru
 */
public class UniqueActivityTitleValidator implements ConstraintValidator {

    private static final String TITLE = FieldMap.underscorify(ActivityFieldsConstants.PROJECT_TITLE);

    private static ActivityTitleDAO activityTitleDAO;

    public interface ActivityTitleDAO {
        boolean isUnique(Set<String> titles, AmpActivityGroup activityGroup);
    }

    public static class DatabaseBackedEnvironment implements ActivityTitleDAO {

        @Override
        public boolean isUnique(Set<String> titles, AmpActivityGroup activityGroup) {
            for (String title : titles) {
                IdWithValueShim id = ActivityUtil.getActivityCollisions(title, activityGroup);
                if (id != null) {
                    return false;
                }
            }
            return true;
        }
    }

    public static <T> T withDao(ActivityTitleDAO dao, Supplier<T> runnable) {
        ActivityTitleDAO prevDAO = UniqueActivityTitleValidator.activityTitleDAO;
        try {
            UniqueActivityTitleValidator.activityTitleDAO = dao;
            return runnable.get();
        } finally {
            UniqueActivityTitleValidator.activityTitleDAO = prevDAO;
        }
    }

    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        if (activityTitleDAO == null) {
            throw new IllegalStateException("No environment specified. See UniqueActivityTitleValidator#withDao.");
        }

        AmpActivityVersion activity = (AmpActivityVersion) value;

        APIField titleField = type.getField(TITLE);
        TranslatedValueContext titleTranslatedValueContext =
                context.getTranslatedValueContext().forField(value, titleField);

        Set<String> titles;
        if (titleField.isTranslatable() != null && titleField.isTranslatable()) {
            Map<String, String> values = titleTranslatedValueContext.getValues();
            titles = values.values().stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        } else {
            String title = titleField.getFieldAccessor().get(value);
            titles = StringUtils.isBlank(title) ? ImmutableSet.of() : ImmutableSet.of(title);
        }

        AmpActivityGroup activityGroup = activity.getAmpActivityGroup();

        if (!titles.isEmpty() && !activityTitleDAO.isUnique(titles, activityGroup)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolation(ActivityErrors.UNIQUE_ACTIVITY_TITLE)
                    .addPropertyNode(TITLE)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return null;
    }
}
