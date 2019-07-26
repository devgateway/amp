package org.digijava.kernel.validators.activity;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.content.ContentRepositoryManager;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;

/**
 * Validates if the value points to an existing private resource.
 *
 * @author Octavian Ciubotaru
 */
public class PrivateResourceValidator implements ConstraintValidator {

    private interface ResourceDAO {
        boolean privateResourceExists(String uuid);
    }

    public static final class InMemoryResourceDAO implements ResourceDAO {

        private Set<String> knownResources;

        public InMemoryResourceDAO(Set<String> knownResources) {
            this.knownResources = knownResources;
        }

        @Override
        public boolean privateResourceExists(String uuid) {
            return knownResources.contains(uuid);
        }
    }

    public static final class AmpResourceDAO implements ResourceDAO {

        @Override
        public boolean privateResourceExists(String uuid) {
            return ContentRepositoryManager.getPrivateUuids().contains(uuid);
        }
    }

    private static ResourceDAO resourceDAO;

    public static <T> T withDao(ResourceDAO dao, Supplier<T> supplier) {
        ResourceDAO prevDAO = resourceDAO;
        try {
            resourceDAO = dao;
            return supplier.get();
        } finally {
            resourceDAO = prevDAO;
        }
    }

    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        if (resourceDAO == null) {
            throw new IllegalStateException("No environment specified. See PrivateResourceValidator#withDao.");
        }

        String uuid = (String) value;

        return StringUtils.isBlank(uuid)
                || resourceDAO.privateResourceExists(uuid);
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.FIELD_INVALID_VALUE;
    }
}
