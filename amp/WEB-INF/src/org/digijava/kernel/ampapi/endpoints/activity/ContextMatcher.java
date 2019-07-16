package org.digijava.kernel.ampapi.endpoints.activity;

/**
 * @author Octavian Ciubotaru
 */
public interface ContextMatcher {

    boolean inContext(FEContext context);
}
