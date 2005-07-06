/*
*   WorkerException.java
*   @Author Ashutosh Mishra
*   Created: Apr 10, 2003
*   CVS-ID: $Id: WorkerException.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
*
*   This file is part of DiGi project (www.digijava.org).
*   DiGi is a multi-site portal system written in Java/J2EE.
*
*   Confidential and Proprietary, Subject to the Non-Disclosure
*   Agreement, Version 1.0, between the Development Gateway
*   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
*   Gateway Foundation, Inc.
*
*   Unauthorized Disclosure Prohibited.
*
*************************************************************************/


package org.digijava.kernel.persistence;

//Application import files
//import org.developmentgateway.core.exception.ApplicationException;

/**
 * The wrapper class around the java.lang.Exception for DevelopmentGateway. Thrown by all the methods of the components
 * @author Ashutosh Mishra
 * @version 1.0
 */

public class WorkerException extends Exception {

	/**
	 * Default Constructor
	 */
	public WorkerException() {
		super();
	}

	/**
	 * Constructor. Creates a WorkerException object with a message
	 * @param message the exception message
	 */
	public WorkerException(String message) {
		super(message);
	}

	/**
	 * Constructor. Creates a WorkerException object with a single message and a nested exception
	 * @param message the exception message
	 * @param cause java.lang.Throwable, presumably an exception you've already caught
	 */

	public WorkerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor. Creates a WorkerException object with a nested exception
	 * @param cause java.lang.Throwable, presumably an exception you've already caught
		 */

	public WorkerException(Throwable cause) {
		super(cause);
	}

}