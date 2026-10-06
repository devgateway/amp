package org.digijava.module.contentrepository.exception;

public class JCRSessionException extends RuntimeException {

    public JCRSessionException() {
        super();
    }

    public JCRSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public JCRSessionException(String message) {
        super(message);
    }

    public JCRSessionException(Throwable cause) {
        super(cause);
    }
}
