/**
 * IncompatibleColumnException.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.exception;

/**
 * thrown when a cell is addeed to a Column that already has subColumnS added
 * or when a Column is added to a Column that already has CellS added
 * A column is a XOR repository of either CellS or ColumnS
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 18, 2006
 * 
 */
public class IncompatibleColumnException extends Exception {

    /**
     * 
     */
    public IncompatibleColumnException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public IncompatibleColumnException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public IncompatibleColumnException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public IncompatibleColumnException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
