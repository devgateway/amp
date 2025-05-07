package org.digijava.module.xmlpatcher.exception;

/**
 * @author Octavian Ciubotaru
 */
public class WrappedCheckedException extends RuntimeException {

    public WrappedCheckedException(Throwable cause) {
        super(cause);
    }
}
