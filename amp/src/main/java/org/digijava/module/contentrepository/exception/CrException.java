/**
 * 
 */
package org.digijava.module.contentrepository.exception;

/**
 * Content Repository module exception
 * @author Alex Gartner
 *
 */
public abstract class CrException extends Exception {

    /**
     * 
     */
    public CrException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public CrException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public CrException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public CrException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
