/*
 *   DgException.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Aug 15, 2003
 * 	 CVS-ID: $Id: DgException.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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
package org.digijava.kernel.exception;

public class DgException extends Exception {

  public DgException() {
  }

  public DgException(String message) {
    super(message);
  }

  public DgException(String message, Throwable cause) {
    super(message, cause);
  }

  public DgException(Throwable cause) {
    super(cause);
  }
}