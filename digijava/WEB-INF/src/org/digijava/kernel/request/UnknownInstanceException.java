/*
 *   UnknownInstanceException.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 14, 2003
 * 	 CVS-ID: $Id: UnknownInstanceException.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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
package org.digijava.kernel.request;

public class UnknownInstanceException extends SecurityException {

    public UnknownInstanceException() {
    }

    public UnknownInstanceException(String message) {
        super(message);
    }
}