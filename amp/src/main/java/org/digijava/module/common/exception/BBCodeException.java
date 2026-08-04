package org.digijava.module.common.exception;
import org.digijava.kernel.exception.DgException;


public class BBCodeException
    extends DgException {

    public BBCodeException() {
    }

    public BBCodeException(String message) {
        super(message);
    }
    public BBCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BBCodeException(Throwable cause) {
        super(cause);
    }

}


