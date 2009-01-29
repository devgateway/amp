package org.digijava.module.dataExchange.Exception;

import org.digijava.kernel.exception.DgException;

public class AmpExportException extends DgException{
	
	public static int NO_ERROR = 0;

	public static int ACTIVITY_LOAD = 1;
	
	public static int ACTIVITY_FORMAT = 2;

	public static int ACTIVITY_TRANSLATION = 3;
	
	public static int ACTIVITY_DATA_INEFFICIENT = 4;
	
	private int type = NO_ERROR;
	
	public AmpExportException() {
		super();
	}

	public AmpExportException(String message) {
		super(message);
	}
	
	public AmpExportException(String message, int type) {
		super(message);
		this.type = type;
	}

	public AmpExportException(String message, Throwable cause, int type) {
		super(message, cause);
		this.type = type;
	}

	public AmpExportException(Throwable cause, int type) {
		super(cause);
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}
}
