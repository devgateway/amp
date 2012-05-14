package org.digijava.module.dataExchange.Exception;

import org.digijava.kernel.exception.DgException;

public class AmpImportException extends DgException{
	
	public static int NO_ERROR = 0;

	public static int WARNNING = 1;
	
	public static int ERROR = 2;

	
	private int type = NO_ERROR;
	
	public AmpImportException() {
		super();
	}

	public AmpImportException(String message) {
		super(message);
	}
	
	public AmpImportException(String message, int type) {
		super(message);
		this.type = type;
	}

	public AmpImportException(String message, Throwable cause, int type) {
		super(message, cause);
		this.type = type;
	}

	public AmpImportException(Throwable cause, int type) {
		super(cause);
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}
}
