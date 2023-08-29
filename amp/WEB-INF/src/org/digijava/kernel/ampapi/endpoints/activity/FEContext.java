package org.digijava.kernel.ampapi.endpoints.activity;

import org.dgfoundation.amp.nireports.ImmutablePair;
import org.digijava.module.aim.annotations.interchange.Interchangeable;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Field Enumeration Context.
 * Holds @Interchangeable and discrimination information about current and parent fields.
 *
 * @author Octavian Ciubotaru
 */
public class FEContext {

    private Deque<ImmutablePair<Class<?>, Object>> discriminationInfoStack = new ArrayDeque<>();
    private Deque<Interchangeable> intchStack = new ArrayDeque<>();

    public Deque<ImmutablePair<Class<?>, Object>> getDiscriminationInfoStack() {
        return discriminationInfoStack;
    }

    public Deque<Interchangeable> getIntchStack() {
        return intchStack;
    }
}
