/**
 * 
 */
package org.digijava.kernel.ampapi.exception;

import org.dgfoundation.amp.error.AMPException;

/**
 * AMP API Exception
 * @author Nadejda Mandrescu
 *
 */
public class AmpApiException extends AMPException {

    /**
     * @param message
     */
    public AmpApiException(String message) {
        super(message);
    }
}
