package org.digijava.kernel.ampapi.endpoints.activity;

/**
 * @author Octavian Ciubotaru
 */
public class DefaultContextMatcher implements ContextMatcher {

    @Override
    public boolean inContext(FEContext context) {
        return true;
    }
}
