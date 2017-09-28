package org.dgfoundation.amp.ar;

/**
 * This exception is thrown when we try to open a report without context
 * 
 * @author Viorel Chihai
 */
public final class InvalidReportContextException extends IllegalStateException {
    
    public InvalidReportContextException(String s) {
        super(s);
    }
    
    public InvalidReportContextException(String s, Throwable t) {
        super(s, t);
    }

    public InvalidReportContextException(Throwable t) {
        super(t);
    }
}
