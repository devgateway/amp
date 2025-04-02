/**
 * UnknownColumnTypeException.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.exception;

/**
 * thrown when new and partially unimplemented ColumnS are used.
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 22, 2006
 * 
 */
public class UnknownColumnTypeException extends Exception {

    /**
     * 
     */
    public UnknownColumnTypeException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public UnknownColumnTypeException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public UnknownColumnTypeException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public UnknownColumnTypeException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
