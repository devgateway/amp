package org.digijava.kernel.geocoding.service;

/**
 * @author Octavian Ciubotaru
 */
public class GeneralGeoCodingException extends RuntimeException {

    public GeneralGeoCodingException(String message) {
        super(message);
    }

    public GeneralGeoCodingException(String message, Throwable cause) {
        super(message, cause);
    }
}
