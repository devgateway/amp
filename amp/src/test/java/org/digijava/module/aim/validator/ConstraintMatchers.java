package org.digijava.module.aim.validator;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInOrder;

/**
 * Matchers for Bean Validation classes.
 *
 * @author Octavian Ciubotaru
 */
public final class ConstraintMatchers {

    private ConstraintMatchers() {
    }

    static Matcher<ConstraintViolation> hasViolation(Class<? extends Annotation> constraintAnnotation) {
        return hasProperty("constraintDescriptor",
                hasProperty("annotation", is(instanceOf(constraintAnnotation))));
    }

    /**
     * Matches a constraint violation with property path matching all node matchers in the specified order.
     */
    static Matcher<ConstraintViolation> violationWithPath(Class<? extends Annotation> constraintAnnotation,
            List<Matcher<Path.Node>> nodeMatchers) {
        return allOf(
                hasProperty("constraintDescriptor",
                        hasProperty("annotation", is(instanceOf(constraintAnnotation)))),
                hasProperty("propertyPath", new IsIterableContainingInOrder(nodeMatchers)));
    }

    /**
     * Matches a node that is an iterable field.
     */
    static Matcher<Path.Node> inIterableNode(String name) {
        return describedAs(name, node(name, true, nullValue()));
    }

    /**
     * Matches a node that is an iterable field.
     */
    static Matcher<Path.Node> propertyNode(String name) {
        return describedAs(name, node(name, false, nullValue()));
    }

    /**
     * Matches a node that is part of iterable at specified key.
     */
    static Matcher<Path.Node> nodeAtKey(String name, Object key) {
        return describedAs(name + "[" + key + "]", node(name, false, sameInstance(key)));
    }
    
    /**
     * Matches a node that is part of iterable at specified key.
     */
    static Matcher<Path.Node> inIterableNodeAtKey(String name, Object key) {
        return describedAs(name + "[" + key + "]", node(name, true, sameInstance(key)));
    }

    private static Matcher<Path.Node> node(String name, boolean inIterable, Matcher<Object> keyMatcher) {
        return allOf(
                hasProperty("name", is(name)),
                hasProperty("inIterable", is(inIterable)),
                hasProperty("index", nullValue()),
                hasProperty("key", keyMatcher));
    }
}
