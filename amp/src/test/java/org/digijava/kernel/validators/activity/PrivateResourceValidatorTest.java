package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.validators.ValidatorUtil.filter;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.*;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class PrivateResourceValidatorTest {

    private static APIField docField;
    private static PrivateResourceValidator.InMemoryResourceDAO dao;

    @BeforeClass
    public static void setUp() {
        docField = ValidatorUtil.getMetaData(AmpActivityDocument.class);
        dao = new PrivateResourceValidator.InMemoryResourceDAO(ImmutableSet.of("def"));
    }

    @Test
    public void testNullValue() {
        AmpActivityDocument doc = new AmpActivityDocument();
        doc.setUuid(null);

        Set<ConstraintViolation> violations = getConstraintViolations(doc);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testEmptyValue() {
        AmpActivityDocument doc = new AmpActivityDocument();
        doc.setUuid("");

        Set<ConstraintViolation> violations = getConstraintViolations(doc);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testResourceNotFound() {
        AmpActivityDocument doc = new AmpActivityDocument();
        doc.setUuid("abc");

        Set<ConstraintViolation> violations = getConstraintViolations(doc);

        assertThat(violations, contains(violation()));
    }

    @Test
    public void testResourceFound() {
        AmpActivityDocument doc = new AmpActivityDocument();
        doc.setUuid("def");

        Set<ConstraintViolation> violations = getConstraintViolations(doc);

        assertThat(violations, emptyIterable());
    }

    private Matcher<ConstraintViolation> violation() {
        return ValidatorMatchers.violationFor(PrivateResourceValidator.class, "uuid", anything(),
                ActivityErrors.FIELD_INVALID_VALUE);
    }

    private Set<ConstraintViolation> getConstraintViolations(AmpActivityDocument doc) {
        Set<ConstraintViolation> violations = PrivateResourceValidator.withDao(
                dao,
                () -> ActivityValidatorUtil.validate(docField, doc));
        return filter(violations, PrivateResourceValidator.class);
    }
}
