package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayDeque;
import java.util.Deque;

import org.dgfoundation.amp.nireports.ImmutablePair;
import org.digijava.module.aim.annotations.interchange.Interchangeable;

/**
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
