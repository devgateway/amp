package org.digijava.kernel.validation;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Collects all found constraint violations.
 *
 * @author Octavian Ciubotaru
 */
public class ValidationContext {

    private final Set<ConstraintViolation> failingConstraintViolations = new LinkedHashSet<>();

    public Set<ConstraintViolation> getFailingConstraintViolations() {
        return failingConstraintViolations;
    }
}
