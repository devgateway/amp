/**
 * 
 */
package org.digijava.kernel.ampapi.exception;

import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.error.AMPUncheckedException;

/**
 * AMP API Exception
 * @author Nadejda Mandrescu
 *
 */
public class AmpApiException extends AMPException {
	public static final int MONDRIAN_ERROR = 1;
	public static final int SYNTAX_ERROR = 2;
	/**
	 * 
	 */
	public AmpApiException() {
		super();
	}

	/**
	 * @param message
	 */
	public AmpApiException(String message) {
		super(message);
	}

	public AmpApiException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * @param level
	 * @param continuable
	 * @param message
	 */
	public AmpApiException(int level, boolean continuable, String message) {
		super(level, continuable, message);
	}

	/**
	 * @param ae
	 */
	public AmpApiException(AMPException ae) {
		super(ae);
	}

	/**
	 * @param ae
	 */
	public AmpApiException(AMPUncheckedException ae) {
		super(ae);
	}

	/**
	 * @param cause
	 */
	public AmpApiException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param level
	 * @param continuable
	 */
	public AmpApiException(int level, boolean continuable) {
		super(level, continuable);
	}

	/**
	 * @param level
	 * @param continuable
	 * @param cause
	 */
	public AmpApiException(int level, boolean continuable, AMPException cause) {
		super(level, continuable, cause);
	}

	/**
	 * @param level
	 * @param continuable
	 * @param cause
	 */
	public AmpApiException(int level, boolean continuable, AMPUncheckedException cause) {
		super(level, continuable, cause);
	}

	/**
	 * @param level
	 * @param continuable
	 * @param cause
	 */
	public AmpApiException(int level, boolean continuable, Throwable cause) {
		super(level, continuable, cause);
	}

}
