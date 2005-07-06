/*
 *   UMException.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Aug 26, 2003
 * 	 CVS-ID: $Id: UMException.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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
package org.digijava.module.um.exception;

import org.digijava.kernel.exception.DgException;

public class UMException
    extends DgException {
    public UMException() {
    }

    public UMException(String message) {
        super(message);
    }

    public UMException(String message, Throwable cause) {
        super(message, cause);
    }

    public UMException(Throwable cause) {
        super(cause);
    }

}