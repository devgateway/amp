package org.digijava.kernel.service;

/**
 * Used to interrupt {@link ServiceManager} from initializing.
 *
 * @author Octavian Ciubotaru
 */
public class FatalServiceException extends RuntimeException {

    public FatalServiceException(String s) {
        super(s);
    }
}
